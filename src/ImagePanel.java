import javax.swing.*;
import java.awt.*;
import javax.imageio.ImageIO;
import java.io.InputStream;

public class ImagePanel extends JPanel {
    private Image backgroundImage;
    private boolean imageLoaded = false;

    public ImagePanel(String imagePath) {
        loadImage(imagePath);
        setLayout(null);
    }

    private void loadImage(String imagePath) {
        try {
            InputStream imageStream = getClass().getResourceAsStream("/" + imagePath);
            if (imageStream != null) {
                backgroundImage = ImageIO.read(imageStream);
                imageLoaded = true;
                System.out.println("✅ Background image loaded: " + imagePath);
            } else {
                System.err.println("❌ Image not found: " + imagePath);
                imageLoaded = false;
            }
        } catch (Exception e) {
            System.err.println("❌ Error loading image: " + e.getMessage());
            e.printStackTrace();
            imageLoaded = false;
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        // Enable antialiasing untuk visual lebih smooth
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING,
                RenderingHints.VALUE_RENDER_QUALITY);

        if (imageLoaded && backgroundImage != null) {
            // Draw image dengan scale ke ukuran panel
            g2d.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        } else {
            // Fallback: gradient background kalau image gagal load
            drawFallbackBackground(g2d);
        }
    }

    private void drawFallbackBackground(Graphics2D g2d) {
        // Dark forest gradient
        GradientPaint gradient = new GradientPaint(
                0, 0, new Color(25, 40, 42),
                0, getHeight(), new Color(18, 28, 32));
        g2d.setPaint(gradient);
        g2d.fillRect(0, 0, getWidth(), getHeight());

        // Add subtle tree silhouettes
        g2d.setColor(new Color(15, 25, 28, 100));
        for (int i = 0; i < getWidth(); i += 150) {
            // Tree shape
            int[] xPoints = { i + 50, i + 30, i + 70 };
            int[] yPoints = { 0, getHeight(), getHeight() };
            g2d.fillPolygon(xPoints, yPoints, 3);
        }

        // Stars/particles
        g2d.setColor(new Color(255, 255, 255, 30));
        for (int i = 0; i < 50; i++) {
            int x = (int) (Math.random() * getWidth());
            int y = (int) (Math.random() * getHeight());
            g2d.fillOval(x, y, 2, 2);
        }
    }
}
