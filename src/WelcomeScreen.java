import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;

/**
 * WelcomeScreen - Beautiful welcome screen
 * Better fonts (Verdana + Arial Black for modern look)
 * Compact terrain box (not too wide!)
 * Perfect alignment
 */

public class WelcomeScreen extends JFrame {

    // Color palette
    private static final Color TRANSPARENT_DARK = new Color(35, 50, 55, 240);
    private static final Color TRANSPARENT_LIGHT = new Color(45, 65, 70, 200);
    private static final Color ACCENT_GREEN = new Color(106, 168, 79);
    private static final Color ACCENT_GOLD = new Color(212, 175, 55);
    private static final Color TEXT_PRIMARY = new Color(255, 255, 255);
    private static final Color TEXT_SECONDARY = new Color(220, 230, 225);

    public WelcomeScreen() {
        setTitle("Forest Pathfinder - Welcome");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(950, 700);
        setLocationRelativeTo(null);
        setResizable(false);

        // Main panel dengan background image
        ImagePanel mainPanel = new ImagePanel("background.png");
        mainPanel.setLayout(new BorderLayout());

        // Add content
        mainPanel.add(createHeaderPanel(), BorderLayout.NORTH);
        mainPanel.add(createInstructionPanel(), BorderLayout.CENTER);
        mainPanel.add(createButtonPanel(), BorderLayout.SOUTH);

        add(mainPanel);

        // Play BGM
        SwingUtilities.invokeLater(() -> {
            SoundManager.playBGM("bgm.wav");
        });
    }

    /**
     * Header panel - Modern bold fonts
     */
    private JPanel createHeaderPanel() {
        JPanel panel = new JPanel();
        panel.setOpaque(false);
        panel.setBorder(new EmptyBorder(35, 20, 20, 20));
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        // "WELCOME TO" text - Clean font
        JLabel welcomeLabel = new JLabel("WELCOME TO");
        welcomeLabel.setFont(new Font("Verdana", Font.PLAIN, 22));
        welcomeLabel.setForeground(ACCENT_GOLD);
        welcomeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // "FOREST PATHFINDER" title - BOLD & IMPACTFUL
        JLabel titleLabel = new JLabel("FOREST PATHFINDER");
        titleLabel.setFont(new Font("Arial Black", Font.BOLD, 46));
        titleLabel.setForeground(ACCENT_GOLD);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        panel.add(welcomeLabel);
        panel.add(Box.createVerticalStrut(3));
        panel.add(titleLabel);

        return panel;
    }

    /**
     * Instruction panel - Compact terrain box
     */
    private JPanel createInstructionPanel() {
        JPanel outerPanel = new JPanel();
        outerPanel.setOpaque(false);
        outerPanel.setBorder(new EmptyBorder(0, 110, 0, 110));
        outerPanel.setLayout(new BorderLayout());

        // Semi-transparent container
        JPanel container = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON);

                // Background
                g2d.setColor(TRANSPARENT_DARK);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);

                // Border
                g2d.setColor(new Color(106, 168, 79, 140));
                g2d.setStroke(new BasicStroke(2.5f));
                g2d.drawRoundRect(1, 1, getWidth() - 3, getHeight() - 3, 20, 20);
            }
        };
        container.setOpaque(false);
        container.setLayout(new BorderLayout());
        container.setBorder(new EmptyBorder(28, 40, 28, 40));

        // Content panel
        JPanel contentPanel = new JPanel();
        contentPanel.setOpaque(false);
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));

        // Title: "How to Play" - Bold & Clean
        JLabel howToPlayLabel = new JLabel("How to Play");
        howToPlayLabel.setFont(new Font("Verdana", Font.BOLD, 26));
        howToPlayLabel.setForeground(TEXT_PRIMARY);
        howToPlayLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        contentPanel.add(howToPlayLabel);
        contentPanel.add(Box.createVerticalStrut(20));

        // Instruction 1
        contentPanel.add(createInstructionLabel(
                "• Navigate through the maze from the Start (S) to the Exit (E)."));
        contentPanel.add(Box.createVerticalStrut(10));

        // Instruction 2
        contentPanel.add(createInstructionLabel(
                "• Each path type has a different cost:"));
        contentPanel.add(Box.createVerticalStrut(12));

        // ⭐ COMPACT TERRAIN COSTS BOX
        JPanel terrainBox = createTerrainCostsBox();
        terrainBox.setAlignmentX(Component.LEFT_ALIGNMENT);
        contentPanel.add(terrainBox);
        contentPanel.add(Box.createVerticalStrut(14));

        // Instruction 3
        contentPanel.add(createInstructionLabel(
                "• Use different pathfinding algorithms to solve the maze:"));
        contentPanel.add(Box.createVerticalStrut(8));

        // Algorithm list - Styled
        JLabel algoList = new JLabel("    BFS, DFS, Dijkstra, and A*");
        algoList.setFont(new Font("Verdana", Font.BOLD, 15));
        algoList.setForeground(new Color(255, 193, 7));
        algoList.setAlignmentX(Component.LEFT_ALIGNMENT);
        contentPanel.add(algoList);
        contentPanel.add(Box.createVerticalStrut(10));

        // Instruction 4 & 5
        contentPanel.add(createInstructionLabel(
                "• Find the path with the lowest total cost."));
        contentPanel.add(Box.createVerticalStrut(6));
        contentPanel.add(createInstructionLabel(
                "   Avoid higher-cost paths to minimize the score!"));

        container.add(contentPanel, BorderLayout.CENTER);
        outerPanel.add(container, BorderLayout.CENTER);
        return outerPanel;
    }

    /**
     * Create instruction label - Verdana for readability
     */
    private JLabel createInstructionLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Verdana", Font.PLAIN, 14));
        label.setForeground(TEXT_SECONDARY);
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        return label;
    }

    /**
     * ⭐ COMPACT TERRAIN COSTS BOX - Width fits content!
     */
    private JPanel createTerrainCostsBox() {
        JPanel boxPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON);

                // Inner box background
                g2d.setColor(TRANSPARENT_LIGHT);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);

                // Border
                g2d.setColor(new Color(80, 100, 105, 180));
                g2d.setStroke(new BasicStroke(1.5f));
                g2d.drawRoundRect(1, 1, getWidth() - 3, getHeight() - 3, 12, 12);
            }
        };
        boxPanel.setOpaque(false);
        boxPanel.setLayout(new BoxLayout(boxPanel, BoxLayout.Y_AXIS));
        boxPanel.setBorder(new EmptyBorder(12, 20, 12, 20));

        // ⭐ TIDAK SET MaximumSize - biar otomatis fit content!
        boxPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Add terrain items
        boxPanel.add(createTerrainItem("Grass: 1 point", CellType.GRASS.getColor()));
        boxPanel.add(Box.createVerticalStrut(8));
        boxPanel.add(createTerrainItem("Dirt: 5 points", CellType.DIRT.getColor()));
        boxPanel.add(Box.createVerticalStrut(8));
        boxPanel.add(createTerrainItem("Water: 10 points", CellType.WATER.getColor()));
        boxPanel.add(Box.createVerticalStrut(8));
        boxPanel.add(createTerrainItem("Safe Tiles: 0 points", CellType.EMPTY.getColor()));

        return boxPanel;
    }

    /**
     * Create terrain cost item - Compact
     */
    private JPanel createTerrainItem(String text, Color color) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        panel.setOpaque(false);

        // ⭐ SET preferred width yang pas (bukan maximum!)
        panel.setPreferredSize(new Dimension(220, 24));
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Color box
        JPanel colorBox = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON);

                // Fill
                g2d.setColor(color);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 5, 5);

                // Border
                g2d.setColor(color.darker());
                g2d.setStroke(new BasicStroke(2f));
                g2d.drawRoundRect(1, 1, getWidth() - 2, getHeight() - 2, 5, 5);
            }
        };
        colorBox.setPreferredSize(new Dimension(20, 20));
        colorBox.setOpaque(false);

        // Text - Verdana for consistency
        JLabel textLabel = new JLabel(text);
        textLabel.setFont(new Font("Verdana", Font.PLAIN, 14));
        textLabel.setForeground(TEXT_PRIMARY);

        panel.add(colorBox);
        panel.add(textLabel);

        return panel;
    }

    /**
     * Button panel - Bold font
     */
    private JPanel createButtonPanel() {
        JPanel panel = new JPanel();
        panel.setOpaque(false);
        panel.setBorder(new EmptyBorder(18, 20, 40, 20));

        JButton startButton = createModernButton("Start Game", ACCENT_GREEN);
        startButton.setPreferredSize(new Dimension(300, 58));
        startButton.setFont(new Font("Arial Black", Font.BOLD, 22));

        startButton.addActionListener(e -> {
            SoundManager.playSound("click.wav");
            startGame();
        });

        panel.add(startButton);
        return panel;
    }

    /**
     * Create modern styled button
     */
    private JButton createModernButton(String text, Color bgColor) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING,
                        RenderingHints.VALUE_RENDER_QUALITY);

                // Button color with hover
                Color btnColor = getModel().isRollover() ? brighten(bgColor) : bgColor;

                // Shadow
                g2d.setColor(new Color(0, 0, 0, 100));
                g2d.fillRoundRect(4, 4, getWidth() - 4, getHeight() - 4, 14, 14);

                // Button background
                g2d.setColor(btnColor);
                g2d.fillRoundRect(0, 0, getWidth() - 4, getHeight() - 4, 14, 14);

                // Glossy highlight
                GradientPaint highlight = new GradientPaint(
                        0, 0, new Color(255, 255, 255, 60),
                        0, getHeight() / 2, new Color(255, 255, 255, 0));
                g2d.setPaint(highlight);
                g2d.fillRoundRect(0, 0, getWidth() - 4, getHeight() / 2 - 2, 14, 14);

                // Border
                g2d.setColor(btnColor.darker().darker());
                g2d.setStroke(new BasicStroke(2.5f));
                g2d.drawRoundRect(1, 1, getWidth() - 6, getHeight() - 6, 14, 14);

                // Text
                g2d.setColor(getForeground());
                g2d.setFont(getFont());
                FontMetrics fm = g2d.getFontMetrics();
                int textX = (getWidth() - fm.stringWidth(getText())) / 2;
                int textY = (getHeight() + fm.getAscent() - fm.getDescent()) / 2 - 2;

                // Text shadow
                g2d.setColor(new Color(0, 0, 0, 100));
                g2d.drawString(getText(), textX + 1, textY + 1);

                // Text
                g2d.setColor(getForeground());
                g2d.drawString(getText(), textX, textY);

                g2d.dispose();
            }
        };

        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setOpaque(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        return button;
    }

    /**
     * Brighten color untuk hover effect
     */
    private Color brighten(Color color) {
        int r = Math.min(255, color.getRed() + 35);
        int g = Math.min(255, color.getGreen() + 35);
        int b = Math.min(255, color.getBlue() + 35);
        return new Color(r, g, b);
    }

    /**
     * Start the main game
     */
    private void startGame() {
        this.dispose();

        SwingUtilities.invokeLater(() -> {
            GameFrame gameFrame = new GameFrame();
            gameFrame.setVisible(true);
        });
    }
}
