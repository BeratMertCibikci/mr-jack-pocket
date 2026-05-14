package reseau;

import java.io.*;
import java.net.*;
import javax.swing.SwingUtilities;

public class GameClient {
    private Socket socket;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    private String role;
    private MessageListener listener; 

    public interface MessageListener {
        void onMessageReceived(Message msg);
    }

    public GameClient(String ipAddress, int port, String role, MessageListener listener) {
        this.role = role;
        this.listener = listener;
        connectToServer(ipAddress, port);
    }

    private void connectToServer(String ipAddress, int port) {
        try {
            socket = new Socket(ipAddress, port);
            
            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());

            Message connectMsg = new Message(Message.MessageType.CONNECT, role);
            sendMessage(connectMsg);

            new Thread(new ServerListener()).start();

        } catch (IOException e) {
            System.err.println("Connection error: " + e.getMessage());
        }
    }

    public void sendMessage(Message msg) {
        try {
            if (out != null) {
                out.writeObject(msg);
                out.flush();
                out.reset();
            }
        } catch (IOException e) {
            System.err.println("Send error: " + e.getMessage());
        }
    }

    private class ServerListener implements Runnable {
        @Override
        public void run() {
            try {
                while (true) {
                    Message msg = (Message) in.readObject();
                    
                    if (msg != null && listener != null) {
                        SwingUtilities.invokeLater(() -> listener.onMessageReceived(msg));
                    }
                }
            } catch (Exception e) {
                System.out.println("Disconnected from server.");
            }
        }
    }
}