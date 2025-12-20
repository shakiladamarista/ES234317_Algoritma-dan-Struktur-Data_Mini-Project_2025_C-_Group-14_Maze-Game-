import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class ControlPanel extends JPanel {
    private MazePanel mazePanel;
    private JTextArea resultArea;
    private JComboBox<String> algorithmSelector;
    private List<PathResult> allResults;


    private static final Color BG_PRIMARY = new Color(42, 54, 59);
    private static final Color BG_SECONDARY = new Color(52, 67, 73);
    private static final Color ACCENT_GREEN = new Color(106, 168, 79);
    private static final Color ACCENT_BLUE = new Color(61, 133, 198);
    private static final Color TEXT_PRIMARY = new Color(245, 247, 242);
    private static final Color TEXT_SECONDARY = new Color(180, 190, 195);


    public ControlPanel(MazePanel mazePanel) {
        this.mazePanel = mazePanel;
        this.allResults = new ArrayList<>();


        setLayout(new BorderLayout(15, 15));
        setPreferredSize(new Dimension(350, 500));
        setBackground(BG_PRIMARY);
        setBorder(new EmptyBorder(20, 20, 20, 20));


        add(createHeaderPanel(), BorderLayout.NORTH);
        add(createMainControlsPanel(), BorderLayout.CENTER);
        add(createResultsPanel(), BorderLayout.SOUTH);
    }


    private JPanel createHeaderPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(BG_PRIMARY);


        JLabel title = new JLabel("Path Finder");
        title.setFont(new Font("Segoe UI", Font.BOLD, 20));
        title.setForeground(ACCENT_GREEN);


        JButton newMazeBtn = createModernButton("New Maze", ACCENT_GREEN);
        newMazeBtn.setPreferredSize(new Dimension(140, 40));
        newMazeBtn.setMaximumSize(new Dimension(140, 40));


        newMazeBtn.addActionListener(e -> {
            SoundManager.playSound("click.wav");  // ✅ TETAP DI SINI (INSIDE ACTION LISTENER)
            mazePanel.generateNewMaze();


            Maze maze = mazePanel.getMaze();
            if (maze.getStart() != null && maze.getEnd() != null) {
                int distance = Math.abs(maze.getStart().getRow() - maze.getEnd().getRow()) +
                        Math.abs(maze.getStart().getCol() - maze.getEnd().getCol());


                resultArea.setText(
                        " New maze generated!\n\n" +
                                " Start: (" + maze.getStart().getRow() + ", " + maze.getStart().getCol() + ")\n" +
                                " End: (" + maze.getEnd().getRow() + ", " + maze.getEnd().getCol() + ")\n" +
                                " Distance: " + distance + " units\n\n" +
                                " Select algorithm to find path...\n"
                );
            }


            allResults.clear();
        });


        panel.add(title, BorderLayout.WEST);
        panel.add(newMazeBtn, BorderLayout.EAST);


        return panel;
    }


    private JPanel createMainControlsPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(BG_PRIMARY);


        panel.add(createAlgorithmSelectorPanel());
        panel.add(Box.createVerticalStrut(15));
        panel.add(createActionButtonsPanel());
        panel.add(Box.createVerticalStrut(15));
        panel.add(createLegendPanel());


        return panel;
    }


    private JPanel createAlgorithmSelectorPanel() {
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;


                g2d.setColor(BG_SECONDARY);
                g2d.fillRect(0, 0, getWidth(), getHeight());


                g2d.setColor(new Color(106, 168, 79, 15));
                for (int i = 0; i < getWidth(); i += 40) {
                    for (int j = 0; j < getHeight(); j += 40) {
                        g2d.fillOval(i - 5, j - 5, 10, 10);
                    }
                }
            }
        };
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(BG_SECONDARY);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(70, 85, 90), 2),
                new EmptyBorder(15, 15, 15, 15)
        ));


        JLabel label = new JLabel("Select Algorithm:");
        label.setFont(new Font("Segoe UI", Font.BOLD, 13));
        label.setForeground(TEXT_PRIMARY);
        label.setAlignmentX(Component.CENTER_ALIGNMENT);


        String[] algorithms = {"BFS - Breadth First Search", "DFS - Depth First Search",
                "Dijkstra - Shortest Path", "A* - Optimal Pathfinding"};
        algorithmSelector = new JComboBox<>(algorithms);
        algorithmSelector.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        algorithmSelector.setBackground(Color.WHITE);
        algorithmSelector.setForeground(new Color(42, 54, 59));


        algorithmSelector.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(180, 190, 195), 1),
                new EmptyBorder(5, 10, 5, 10)
        ));


        algorithmSelector.setMaximumSize(new Dimension(310, 40));
        algorithmSelector.setPreferredSize(new Dimension(310, 40));
        algorithmSelector.setAlignmentX(Component.CENTER_ALIGNMENT);


        algorithmSelector.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value,
                                                          int index, boolean isSelected, boolean cellHasFocus) {
                JLabel label = (JLabel) super.getListCellRendererComponent(
                        list, value, index, isSelected, cellHasFocus);
                label.setBorder(new EmptyBorder(8, 12, 8, 12));
                label.setFont(new Font("Segoe UI", Font.PLAIN, 12));


                if (isSelected) {
                    label.setBackground(ACCENT_GREEN);
                    label.setForeground(Color.WHITE);
                } else {
                    label.setBackground(Color.WHITE);
                    label.setForeground(new Color(42, 54, 59));
                }


                return label;
            }
        });


        panel.add(label);
        panel.add(Box.createVerticalStrut(10));
        panel.add(algorithmSelector);


        return panel;
    }

    private JPanel createActionButtonsPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(BG_PRIMARY);


        // Find Path Button
        JButton runBtn = createModernButton("Find Path", ACCENT_BLUE);
        runBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        runBtn.setAlignmentX(Component.CENTER_ALIGNMENT);


        runBtn.addActionListener(e -> {
            SoundManager.playSound("click.wav");  // ✅ DI SINI!
            runSelectedAlgorithm();
        });


        JButton compareBtn = createModernButton("Compare All Algorithms", new Color(255, 180, 50));
        compareBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        compareBtn.setAlignmentX(Component.CENTER_ALIGNMENT);


        compareBtn.addActionListener(e -> {
            SoundManager.playSound("click.wav");  // ✅ DI SINI!
            compareAllAlgorithms();
        });


        panel.add(runBtn);
        panel.add(Box.createVerticalStrut(12));
        panel.add(compareBtn);


        return panel;
    }


    private JButton createModernButton(String text, Color bgColor) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);


                if (getModel().isRollover()) {
                    g2d.setColor(brighten(bgColor));
                } else {
                    g2d.setColor(bgColor);
                }
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);


                g2d.setColor(getForeground());
                g2d.setFont(getFont());
                FontMetrics fm = g2d.getFontMetrics();
                int textX = (getWidth() - fm.stringWidth(getText())) / 2;
                int textY = (getHeight() + fm.getAscent() - fm.getDescent()) / 2;
                g2d.drawString(getText(), textX, textY);


                g2d.dispose();
            }
        };


        button.setFont(new Font("Segoe UI", Font.BOLD, 13));
        button.setForeground(Color.WHITE);
        button.setBackground(bgColor);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setOpaque(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(200, 45));

        return button;
    }


    private JPanel createLegendPanel() {
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;


                g2d.setColor(BG_SECONDARY);
                g2d.fillRect(0, 0, getWidth(), getHeight());


                g2d.setColor(new Color(255, 255, 255, 8));
                for (int i = -getHeight(); i < getWidth(); i += 15) {
                    g2d.drawLine(i, 0, i + getHeight(), getHeight());
                }
            }
        };
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(BG_SECONDARY);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(70, 85, 90), 2),
                new EmptyBorder(12, 12, 12, 12)
        ));


        JLabel title = new JLabel("Legend");
        title.setFont(new Font("Segoe UI", Font.BOLD, 13));
        title.setForeground(TEXT_PRIMARY);
        title.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(title);
        panel.add(Box.createVerticalStrut(10));


        panel.add(createCompactLegendItem("Start", new Color(76, 175, 80)));
        panel.add(createCompactLegendItem("End", new Color(244, 67, 54)));
        panel.add(createCompactLegendItem("Path", new Color(255, 193, 7)));
        panel.add(createCompactLegendItem("Wall", new Color(55, 71, 79)));
        panel.add(Box.createVerticalStrut(5));
        panel.add(createCompactLegendItem(CellType.EMPTY.getName() + " (0)", CellType.EMPTY.getColor()));
        panel.add(createCompactLegendItem(CellType.GRASS.getName() + " (1)", CellType.GRASS.getColor()));
        panel.add(createCompactLegendItem(CellType.DIRT.getName() + " (5)", CellType.DIRT.getColor()));
        panel.add(createCompactLegendItem(CellType.WATER.getName() + " (10)", CellType.WATER.getColor()));


        return panel;
    }


    private JPanel createCompactLegendItem(String text, Color color) {
        JPanel item = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 2));
        item.setBackground(BG_SECONDARY);


        JPanel colorBox = new JPanel();
        colorBox.setPreferredSize(new Dimension(16, 16));
        colorBox.setBackground(color);
        colorBox.setBorder(BorderFactory.createLineBorder(new Color(70, 85, 90)));


        JLabel label = new JLabel(text);
        label.setForeground(TEXT_SECONDARY);
        label.setFont(new Font("Segoe UI", Font.PLAIN, 11));


        item.add(colorBox);
        item.add(label);


        return item;
    }


    private JPanel createResultsPanel() {
        JPanel panel = new JPanel(new BorderLayout(0, 8));
        panel.setBackground(BG_PRIMARY);


        JLabel titleLabel = new JLabel("Results:");
        titleLabel.setForeground(TEXT_PRIMARY);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));


        resultArea = new JTextArea(9, 25);
        resultArea.setEditable(false);
        resultArea.setFont(new Font("Consolas", Font.PLAIN, 11));
        resultArea.setBackground(BG_SECONDARY);
        resultArea.setForeground(TEXT_SECONDARY);
        resultArea.setBorder(new EmptyBorder(10, 10, 10, 10));
        resultArea.setText("Welcome! \nGenerate a maze and select an algorithm to start.\n");


        JScrollPane scrollPane = new JScrollPane(resultArea);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(70, 85, 90), 2));


        panel.add(titleLabel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);


        return panel;
    }


    private void runSelectedAlgorithm() {
        int selectedIndex = algorithmSelector.getSelectedIndex();
        String[] algorithmNames = {"BFS", "DFS", "Dijkstra", "A*"};
        runAlgorithm(algorithmNames[selectedIndex]);
    }


    private void runAlgorithm(String algorithm) {
        PathResult result = null;


        switch (algorithm) {
            case "BFS":
                result = PathFinder.findPathBFS(mazePanel.getMaze());
                break;
            case "DFS":
                result = PathFinder.findPathDFS(mazePanel.getMaze());
                break;
            case "Dijkstra":
                result = PathFinder.findPathDijkstra(mazePanel.getMaze());
                break;
            case "A*":
                result = PathFinder.findPathAStar(mazePanel.getMaze());
                break;
        }


        if (result != null) {
            mazePanel.showPathWithAnimation(result);
            displayResult(result);
        }
    }


    private void displayResult(PathResult result) {
        StringBuilder sb = new StringBuilder();
        sb.append("═══ ").append(result.getAlgorithmName()).append(" ═══\n\n");


        if (result.getPath() != null) {
            sb.append("Path Found!\n\n");
            sb.append("Statistics:\n");
            sb.append("  • Total Cost: ").append(result.getTotalCost()).append("\n");
            sb.append("  • Path Length: ").append(result.getPathLength()).append(" steps\n");


            double avgCost = (double) result.getTotalCost() / result.getPathLength();
            sb.append("  • Avg Cost/Step: ").append(String.format("%.2f", avgCost)).append("\n");
            sb.append("  • Execution Time: ").append(result.getExecutionTime()).append(" μs\n\n");


            sb.append(" About ").append(result.getAlgorithmName()).append(":\n");
            switch (result.getAlgorithmName()) {
                case "BFS":
                    sb.append("  Finds shortest path by steps,\n");
                    sb.append("  but may not have lowest cost\n");
                    break;
                case "DFS":
                    sb.append("  Explores deeply first,\n");
                    sb.append("  not guaranteed to be optimal\n");
                    break;
                case "Dijkstra":
                    sb.append("  Guaranteed lowest cost path,\n");
                    sb.append("  considers terrain cost\n");
                    break;
                case "A*":
                    sb.append("  Optimal & efficient,\n");
                    sb.append("  uses smart heuristics\n");
                    break;
            }
        } else {
            sb.append("No path found!\n");
        }


        resultArea.setText(sb.toString());
    }


    private void compareAllAlgorithms() {
        allResults.clear();


        allResults.add(PathFinder.findPathBFS(mazePanel.getMaze()));
        allResults.add(PathFinder.findPathDFS(mazePanel.getMaze()));
        allResults.add(PathFinder.findPathDijkstra(mazePanel.getMaze()));
        allResults.add(PathFinder.findPathAStar(mazePanel.getMaze()));


        PathResult bestCost = null;
        PathResult shortestPath = null;
        PathResult fastest = null;


        for (PathResult result : allResults) {
            if (result.getPath() != null) {
                if (bestCost == null || result.getTotalCost() < bestCost.getTotalCost()) {
                    bestCost = result;
                }
                if (shortestPath == null || result.getPathLength() < shortestPath.getPathLength()) {
                    shortestPath = result;
                }
                if (fastest == null || result.getExecutionTime() < fastest.getExecutionTime()) {
                    fastest = result;
                }
            }
        }


        StringBuilder sb = new StringBuilder();
        sb.append("═══ ALGORITHM COMPARISON ═══\n\n");


        for (PathResult result : allResults) {
            sb.append("【 ").append(result.getAlgorithmName()).append(" 】\n");
            if (result.getPath() != null) {
                sb.append("  • Total Cost: ").append(result.getTotalCost());
                if (result.equals(bestCost)) {
                    sb.append(" = LOWEST COST!");
                }
                sb.append("\n");


                sb.append("  • Path Length: ").append(result.getPathLength()).append(" steps");
                if (result.equals(shortestPath)) {
                    sb.append(" = SHORTEST!");
                }
                sb.append("\n");


                sb.append("  • Exec Time: ").append(result.getExecutionTime()).append(" μs");
                if (result.equals(fastest)) {
                    sb.append(" = FASTEST!");
                }
                sb.append("\n");


                double avgCost = (double) result.getTotalCost() / result.getPathLength();
                sb.append("  • Avg Cost/Step: ").append(String.format("%.2f", avgCost)).append("\n");


            } else {
                sb.append("No path found\n");
            }
            sb.append("\n");
        }


        if (bestCost != null) {
            sb.append("━━━━━━━━━━━━━━━━━━━━━━━━━━\n");
            sb.append("OPTIMAL (Lowest Cost): \n");
            sb.append("   ").append(bestCost.getAlgorithmName());
            sb.append(" - Cost: ").append(bestCost.getTotalCost()).append("\n");


            if (bestCost.equals(shortestPath)) {
                sb.append("   Also shortest path!\n");
            } else {
                sb.append("   Takes longer route but\n");
                sb.append("   avoids expensive terrain\n");
            }


            mazePanel.showPathWithAnimation(bestCost);
        }


        resultArea.setText(sb.toString());
    }


    private Color brighten(Color color) {
        int r = Math.min(255, color.getRed() + 30);
        int g = Math.min(255, color.getGreen() + 30);
        int b = Math.min(255, color.getBlue() + 30);
        return new Color(r, g, b);
    }
}
