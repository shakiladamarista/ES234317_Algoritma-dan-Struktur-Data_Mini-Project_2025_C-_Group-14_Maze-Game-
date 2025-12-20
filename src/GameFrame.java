import javax.swing.*;
import java.awt.*;


/**
 * GameFrame Class - Main window dengan modern design
 * UPDATED: Clean, minimalist forest theme
 */
public class GameFrame extends JFrame {
    private MazePanel mazePanel;
    private ControlPanel controlPanel;


    private static final Color BG_DARK = new Color(34, 44, 47);
    private static final Color BG_MEDIUM = new Color(42, 54, 59);
    private static final Color ACCENT_GREEN = new Color(106, 168, 79);


    public GameFrame() {
        setTitle("Forest Pathfinder - Maze Game");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(0, 0));


        getContentPane().setBackground(BG_DARK);


        // Main content panel dengan spacing dan custom background
        JPanel contentPanel = new JPanel(new BorderLayout(12, 12)) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;


                // Subtle texture pattern
                g2d.setColor(new Color(255, 255, 255, 5));
                for (int i = 0; i < getWidth(); i += 20) {
                    for (int j = 0; j < getHeight(); j += 20) {
                        if ((i/20 + j/20) % 2 == 0) {
                            g2d.fillRect(i, j, 2, 2);
                        }
                    }
                }


                // Decorative lines
                g2d.setColor(new Color(106, 168, 79, 20));
                g2d.setStroke(new BasicStroke(2));
                g2d.drawLine(0, getHeight()/3, getWidth(), getHeight()/3);
                g2d.drawLine(0, 2*getHeight()/3, getWidth(), 2*getHeight()/3);
            }
        };
        contentPanel.setBackground(BG_DARK);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));


        // Maze panel
        mazePanel = new MazePanel();
        contentPanel.add(mazePanel, BorderLayout.CENTER);


        // Control panel
        controlPanel = new ControlPanel(mazePanel);
        contentPanel.add(controlPanel, BorderLayout.EAST);


        // Header
        add(createHeaderPanel(), BorderLayout.NORTH);
        add(contentPanel, BorderLayout.CENTER);


        pack();
        setLocationRelativeTo(null);
        setResizable(false);
    }


    /**
     * Header panel dengan gradient effect dan decorative pattern
     */
    private JPanel createHeaderPanel() {
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);


                // Multi-layer gradient background
                GradientPaint gp1 = new GradientPaint(
                        0, 0, new Color(52, 67, 73),
                        0, getHeight(), new Color(42, 54, 59)
                );
                g2d.setPaint(gp1);
                g2d.fillRect(0, 0, getWidth(), getHeight());


                // Decorative triangle pattern overlay
                g2d.setColor(new Color(106, 168, 79, 30));
                for (int i = 0; i < getWidth(); i += 60) {
                    int[] xPoints = {i, i + 30, i + 60};
                    int[] yPoints = {getHeight(), 0, getHeight()};
                    g2d.fillPolygon(xPoints, yPoints, 3);
                }


                // Subtle dots pattern
                g2d.setColor(new Color(255, 255, 255, 15));
                for (int i = 10; i < getWidth(); i += 30) {
                    for (int j = 10; j < getHeight(); j += 20) {
                        g2d.fillOval(i, j, 3, 3);
                    }
                }


                // Bottom border accent
                g2d.setColor(ACCENT_GREEN);
                g2d.fillRect(0, getHeight() - 3, getWidth(), 3);
            }
        };


        panel.setPreferredSize(new Dimension(0, 60));  // More compact
        panel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 15));


        // Title dengan icon
        JLabel title = new JLabel("PATHFINDER GAME");
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));  // Slightly smaller
        title.setForeground(ACCENT_GREEN);


        panel.add(title);
        return panel;
    }
}
