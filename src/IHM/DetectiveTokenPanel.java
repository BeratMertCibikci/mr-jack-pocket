package IHM;

import javax.swing.*;
import java.awt.*;

public class DetectiveTokenPanel extends JPanel {

    private String nom;

    public DetectiveTokenPanel(String nom) {
        this.nom = nom;

        setPreferredSize(new Dimension(80, 80));
        setOpaque(false);
        setLayout(new BorderLayout());

        JLabel label = new JLabel(nom, SwingConstants.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 12));
        label.setForeground(new Color(245, 235, 210));

        add(label, BorderLayout.CENTER);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int size = Math.min(getWidth(), getHeight()) - 10;
        int x = (getWidth() - size) / 2;
        int y = (getHeight() - size) / 2;

        g2.setColor(new Color(35, 30, 25));
        g2.fillOval(x, y, size, size);

        g2.setColor(new Color(212, 175, 55));
        g2.setStroke(new BasicStroke(3));
        g2.drawOval(x, y, size, size);
    }
}