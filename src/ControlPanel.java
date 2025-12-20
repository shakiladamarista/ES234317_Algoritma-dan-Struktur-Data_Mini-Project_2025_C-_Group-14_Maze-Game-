import javax.swing.*;
import java.awt.*;

public class ControlPanel extends JPanel {
    private MazePanel mazePanel;
    private JTextArea resultArea;


    public ControlPanel(MazePanel mazePanel) {
        this.mazePanel = mazePanel;


        setLayout(new BorderLayout(10, 10));
        setPreferredSize(new Dimension(300, 500));
        setBackground(Color.LIGHT_GRAY);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));


        add(createTopPanel(), BorderLayout.NORTH);
        add(createAlgorithmPanel(), BorderLayout.CENTER);
        add(createResultPanel(), BorderLayout.SOUTH);
    }


    private JPanel createTopPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(Color.LIGHT_GRAY);


        JLabel title = new JLabel("Pathfinder", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 18));


        JButton newMazeBtn = new JButton("New Maze");
        newMazeBtn.addActionListener(e -> {
            mazePanel.generateNewMaze();
            resultArea.setText("New maze generated!\n\nSelect algorithm to find path.");
        });


        panel.add(title, BorderLayout.NORTH);
        panel.add(newMazeBtn, BorderLayout.CENTER);


        return panel;
    }


    // ✨ BARU: Algorithm buttons
    private JPanel createAlgorithmPanel() {
        JPanel panel = new JPanel(new GridLayout(5, 1, 10, 10));
        panel.setBackground(Color.LIGHT_GRAY);
        panel.setBorder(BorderFactory.createTitledBorder("Algorithms"));


        JButton bfsBtn = new JButton("BFS");
        JButton dfsBtn = new JButton("DFS");
        JButton dijkstraBtn = new JButton("Dijkstra");
        JButton astarBtn = new JButton("A*");


        bfsBtn.addActionListener(e -> runAlgorithm("BFS"));
        dfsBtn.addActionListener(e -> runAlgorithm("DFS"));
        dijkstraBtn.addActionListener(e -> runAlgorithm("Dijkstra"));
        astarBtn.addActionListener(e -> runAlgorithm("A*"));


        panel.add(bfsBtn);
        panel.add(dfsBtn);
        panel.add(dijkstraBtn);
        panel.add(astarBtn);


        return panel;
    }


    // ✨ BARU: Result panel
    private JPanel createResultPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.LIGHT_GRAY);


        JLabel label = new JLabel("Results:");
        label.setFont(new Font("Arial", Font.BOLD, 13));


        resultArea = new JTextArea(8, 20);
        resultArea.setEditable(false);
        resultArea.setFont(new Font("Monospaced", Font.PLAIN, 11));
        resultArea.setText("Welcome!\n\nGenerate maze and\nrun algorithm.");


        JScrollPane scrollPane = new JScrollPane(resultArea);


        panel.add(label, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);


        return panel;
    }


    // ✨ BARU: Run algorithm
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
            mazePanel.showPath(result);
            displayResult(result);
        }
    }


    // ✨ BARU: Display result
    private void displayResult(PathResult result) {
        StringBuilder sb = new StringBuilder();
        sb.append("=== ").append(result.getAlgorithmName()).append(" ===\n\n");


        if (result.getPath() != null) {
            sb.append("Path Found!\n\n");
            sb.append("Total Cost: ").append(result.getTotalCost()).append("\n");
            sb.append("Path Length: ").append(result.getPathLength()).append(" steps\n");
            sb.append("Execution Time: ").append(result.getExecutionTime()).append(" μs\n");
        } else {
            sb.append("No path found!");
        }


        resultArea.setText(sb.toString());
    }
}



