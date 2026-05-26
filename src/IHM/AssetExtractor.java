package IHM;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.*;
import java.util.Comparator;
import java.util.jar.JarFile;
import java.util.stream.Stream;

public final class AssetExtractor {

    private AssetExtractor() {
    }

    public static void prepareAssetsDirectory() {
        try {
            URL assetsUrl = AssetExtractor.class.getResource("/assets");

            if (assetsUrl == null) {
                System.out.println("Assets introuvables dans le jar.");
                return;
            }

            if ("file".equals(assetsUrl.getProtocol())) {
                // Normal IDE / javac çalıştırma: assets zaten dosya sisteminde.
                return;
            }

            if (!"jar".equals(assetsUrl.getProtocol())) {
                return;
            }

            Path tempRoot = Files.createTempDirectory("mr-jack-pocket-");
            Path targetAssets = tempRoot.resolve("assets");

            extractAssetsFromJar(targetAssets);

            System.setProperty("user.dir", tempRoot.toAbsolutePath().toString());

            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                try {
                    deleteDirectory(tempRoot);
                } catch (Exception ignored) {
                }
            }));

            System.out.println("Assets extracted to: " + targetAssets);

        } catch (Exception e) {
            System.out.println("Erreur extraction assets: " + e.getMessage());
        }
    }

    private static void extractAssetsFromJar(Path targetAssets) throws Exception {
        String classPath = AssetExtractor.class
                .getProtectionDomain()
                .getCodeSource()
                .getLocation()
                .toURI()
                .getPath();

        try (JarFile jarFile = new JarFile(new File(classPath))) {
            jarFile.stream()
                    .filter(entry -> entry.getName().startsWith("assets/"))
                    .forEach(entry -> {
                        try {
                            Path outputPath = targetAssets.getParent().resolve(entry.getName());

                            if (entry.isDirectory()) {
                                Files.createDirectories(outputPath);
                                return;
                            }

                            Files.createDirectories(outputPath.getParent());

                            try (InputStream inputStream = jarFile.getInputStream(entry)) {
                                Files.copy(inputStream, outputPath, StandardCopyOption.REPLACE_EXISTING);
                            }

                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    });
        }
    }

    private static void deleteDirectory(Path directory) throws IOException {
        if (directory == null || !Files.exists(directory)) {
            return;
        }

        try (Stream<Path> walk = Files.walk(directory)) {
            walk.sorted(Comparator.reverseOrder())
                    .forEach(path -> {
                        try {
                            Files.deleteIfExists(path);
                        } catch (IOException ignored) {
                        }
                    });
        }
    }
}