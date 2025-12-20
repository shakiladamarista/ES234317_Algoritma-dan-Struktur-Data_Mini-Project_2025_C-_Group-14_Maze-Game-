import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;

public class Cell {
    private int row;
    private int col;
    private CellType type;
    private boolean isWall;
    private boolean isStart;
    private boolean isEnd;
    private boolean isPath;  // ✨ BARU!


    public Cell(int row, int col) {
        this.row = row;
        this.col = col;
        this.type = CellType.EMPTY;
        this.isWall = false;
        this.isStart = false;
        this.isEnd = false;
        this.isPath = false;  // ✨ BARU!
    }


    public int getRow() { return row; }
    public int getCol() { return col; }
    public CellType getType() { return type; }
    public boolean isWall() { return isWall; }
    public boolean isStart() { return isStart; }
    public boolean isEnd() { return isEnd; }
    public boolean isPath() { return isPath; }  // ✨ BARU!


    public void setType(CellType type) { this.type = type; }
    public void setWall(boolean wall) { this.isWall = wall; }
    public void setStart(boolean start) { this.isStart = start; }
    public void setEnd(boolean end) { this.isEnd = end; }
    public void setPath(boolean path) { this.isPath = path; }  // ✨ BARU!


    public int getCost() {
        return type.getCost();
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Cell cell = (Cell) o;
        return row == cell.row && col == cell.col;
    }


    @Override
    public int hashCode() {
        return 31 * row + col;
    }

    public static class PathFinder {


        /**
         * BFS - Breadth First Search
         */
        public static PathResult findPathBFS(Maze maze) {
            long startTime = System.nanoTime();


            Queue<Cell> queue = new LinkedList<>();
            Set<Cell> visited = new HashSet<>();
            Map<Cell, Cell> parent = new HashMap<>();


            Cell start = maze.getStart();
            Cell end = maze.getEnd();


            queue.offer(start);
            visited.add(start);
            parent.put(start, null);


            while (!queue.isEmpty()) {
                Cell current = queue.poll();


                if (current.equals(end)) {
                    List<Cell> path = reconstructPath(parent, end);
                    int totalCost = calculateTotalCost(path);
                    long executionTime = (System.nanoTime() - startTime) / 1000;
                    return new PathResult(path, totalCost, executionTime, "BFS");
                }


                for (Cell neighbor : maze.getNeighbors(current)) {
                    if (!visited.contains(neighbor)) {
                        visited.add(neighbor);
                        parent.put(neighbor, current);
                        queue.offer(neighbor);
                    }
                }
            }


            long executionTime = (System.nanoTime() - startTime) / 1000;
            return new PathResult(null, -1, executionTime, "BFS");
        }


        /**
         * DFS - Depth First Search
         */
        public static PathResult findPathDFS(Maze maze) {
            long startTime = System.nanoTime();


            Stack<Cell> stack = new Stack<>();
            Set<Cell> visited = new HashSet<>();
            Map<Cell, Cell> parent = new HashMap<>();


            Cell start = maze.getStart();
            Cell end = maze.getEnd();


            stack.push(start);
            visited.add(start);
            parent.put(start, null);


            while (!stack.isEmpty()) {
                Cell current = stack.pop();


                if (current.equals(end)) {
                    List<Cell> path = reconstructPath(parent, end);
                    int totalCost = calculateTotalCost(path);
                    long executionTime = (System.nanoTime() - startTime) / 1000;
                    return new PathResult(path, totalCost, executionTime, "DFS");
                }


                for (Cell neighbor : maze.getNeighbors(current)) {
                    if (!visited.contains(neighbor)) {
                        visited.add(neighbor);
                        parent.put(neighbor, current);
                        stack.push(neighbor);
                    }
                }
            }


            long executionTime = (System.nanoTime() - startTime) / 1000;
            return new PathResult(null, -1, executionTime, "DFS");
        }


        /**
         * Dijkstra - Shortest path by COST
         */
        public static PathResult findPathDijkstra(Maze maze) {
            long startTime = System.nanoTime();


            PriorityQueue<Node> pq = new PriorityQueue<>(Comparator.comparingInt(n -> n.cost));
            Map<Cell, Integer> distances = new HashMap<>();
            Map<Cell, Cell> parent = new HashMap<>();
            Set<Cell> visited = new HashSet<>();


            Cell start = maze.getStart();
            Cell end = maze.getEnd();


            for (int i = 0; i < maze.getRows(); i++) {
                for (int j = 0; j < maze.getCols(); j++) {
                    distances.put(maze.getCell(i, j), Integer.MAX_VALUE);
                }
            }


            distances.put(start, 0);
            pq.offer(new Node(start, 0));
            parent.put(start, null);


            while (!pq.isEmpty()) {
                Node currentNode = pq.poll();
                Cell current = currentNode.cell;


                if (visited.contains(current)) continue;
                visited.add(current);


                if (current.equals(end)) {
                    List<Cell> path = reconstructPath(parent, end);
                    int totalCost = distances.get(end);
                    long executionTime = (System.nanoTime() - startTime) / 1000;
                    return new PathResult(path, totalCost, executionTime, "Dijkstra");
                }


                for (Cell neighbor : maze.getNeighbors(current)) {
                    if (!visited.contains(neighbor)) {
                        int newCost = distances.get(current) + neighbor.getCost();


                        if (newCost < distances.get(neighbor)) {
                            distances.put(neighbor, newCost);
                            parent.put(neighbor, current);
                            pq.offer(new Node(neighbor, newCost));
                        }
                    }
                }
            }


            long executionTime = (System.nanoTime() - startTime) / 1000;
            return new PathResult(null, -1, executionTime, "Dijkstra");
        }


        /**
         * A* - Optimal with heuristic
         */
        public static PathResult findPathAStar(Maze maze) {
            long startTime = System.nanoTime();


            PriorityQueue<AStarNode> pq = new PriorityQueue<>(Comparator.comparingInt(n -> n.fScore));
            Map<Cell, Integer> gScore = new HashMap<>();
            Map<Cell, Cell> parent = new HashMap<>();
            Set<Cell> visited = new HashSet<>();


            Cell start = maze.getStart();
            Cell end = maze.getEnd();


            for (int i = 0; i < maze.getRows(); i++) {
                for (int j = 0; j < maze.getCols(); j++) {
                    gScore.put(maze.getCell(i, j), Integer.MAX_VALUE);
                }
            }


            gScore.put(start, 0);
            int hStart = heuristic(start, end);
            pq.offer(new AStarNode(start, 0, hStart));
            parent.put(start, null);


            while (!pq.isEmpty()) {
                AStarNode currentNode = pq.poll();
                Cell current = currentNode.cell;


                if (visited.contains(current)) continue;
                visited.add(current);


                if (current.equals(end)) {
                    List<Cell> path = reconstructPath(parent, end);
                    int totalCost = gScore.get(end);
                    long executionTime = (System.nanoTime() - startTime) / 1000;
                    return new PathResult(path, totalCost, executionTime, "A*");
                }


                for (Cell neighbor : maze.getNeighbors(current)) {
                    if (!visited.contains(neighbor)) {
                        int tentativeGScore = gScore.get(current) + neighbor.getCost();


                        if (tentativeGScore < gScore.get(neighbor)) {
                            gScore.put(neighbor, tentativeGScore);
                            parent.put(neighbor, current);
                            int fScore = tentativeGScore + heuristic(neighbor, end);
                            pq.offer(new AStarNode(neighbor, tentativeGScore, fScore));
                        }
                    }
                }
            }


            long executionTime = (System.nanoTime() - startTime) / 1000;
            return new PathResult(null, -1, executionTime, "A*");
        }


        private static int heuristic(Cell current, Cell end) {
            return Math.abs(current.getRow() - end.getRow()) +
                    Math.abs(current.getCol() - end.getCol());
        }


        private static List<Cell> reconstructPath(Map<Cell, Cell> parent, Cell end) {
            List<Cell> path = new ArrayList<>();
            Cell current = end;


            while (current != null) {
                path.add(0, current);
                current = parent.get(current);
            }


            return path;
        }


        private static int calculateTotalCost(List<Cell> path) {
            int totalCost = 0;
            for (Cell cell : path) {
                totalCost += cell.getCost();
            }
            return totalCost;
        }


        private static class Node {
            Cell cell;
            int cost;


            Node(Cell cell, int cost) {
                this.cell = cell;
                this.cost = cost;
            }
        }


        private static class AStarNode {
            Cell cell;
            int gScore;
            int fScore;


            AStarNode(Cell cell, int gScore, int fScore) {
                this.cell = cell;
                this.gScore = gScore;
                this.fScore = fScore;
            }
        }
    }

    public static class GameFrame extends JFrame {
        private MazePanel mazePanel;
        private ControlPanel controlPanel;


        public GameFrame() {
            setTitle("Pathfinder Game - SESI 2: + ALGORITMA");
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            setLayout(new BorderLayout(10, 10));


            mazePanel = new MazePanel();
            controlPanel = new ControlPanel(mazePanel);


            add(mazePanel, BorderLayout.CENTER);
            add(controlPanel, BorderLayout.EAST);


            pack();
            setLocationRelativeTo(null);
            setResizable(false);
        }
    }

    public static class Maze {
        private int rows;
        private int cols;
        private Cell[][] grid;
        private Cell start;
        private Cell end;
        private Random random;


        public Maze(int rows, int cols) {
            this.rows = rows;
            this.cols = cols;
            this.grid = new Cell[rows][cols];
            this.random = new Random();
            initializeGrid();
        }


        private void initializeGrid() {
            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < cols; j++) {
                    grid[i][j] = new Cell(i, j);
                }
            }
        }


        public void generateMaze() {
            // Reset semua cell jadi tembok
            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < cols; j++) {
                    grid[i][j].setWall(true);
                }
            }


            // DFS algorithm untuk carving maze
            Stack<Cell> stack = new Stack<>();
            Cell current = grid[1][1];
            current.setWall(false);
            stack.push(current);


            while (!stack.isEmpty()) {
                current = stack.peek();
                List<Cell> unvisitedNeighbors = getUnvisitedNeighbors(current);


                if (!unvisitedNeighbors.isEmpty()) {
                    Cell next = unvisitedNeighbors.get(random.nextInt(unvisitedNeighbors.size()));
                    removeWallBetween(current, next);
                    next.setWall(false);
                    stack.push(next);
                } else {
                    stack.pop();
                }
            }


            // Set start dan end
            start = grid[1][1];
            end = grid[rows - 2][cols - 2];
            start.setStart(true);
            start.setWall(false);
            end.setEnd(true);
            end.setWall(false);


            // Assign random terrain
            assignTerrainTypes();
        }


        private List<Cell> getUnvisitedNeighbors(Cell cell) {
            List<Cell> neighbors = new ArrayList<>();
            int[][] directions = {{-2, 0}, {2, 0}, {0, -2}, {0, 2}};


            for (int[] dir : directions) {
                int newRow = cell.getRow() + dir[0];
                int newCol = cell.getCol() + dir[1];


                if (isValid(newRow, newCol) && grid[newRow][newCol].isWall()) {
                    neighbors.add(grid[newRow][newCol]);
                }
            }
            return neighbors;
        }


        private void removeWallBetween(Cell current, Cell next) {
            int wallRow = (current.getRow() + next.getRow()) / 2;
            int wallCol = (current.getCol() + next.getCol()) / 2;
            grid[wallRow][wallCol].setWall(false);
        }


        private void assignTerrainTypes() {
            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < cols; j++) {
                    if (!grid[i][j].isWall() && !grid[i][j].isStart() && !grid[i][j].isEnd()) {
                        double rand = random.nextDouble();
                        if (rand < 0.4) {
                            grid[i][j].setType(CellType.EMPTY);
                        } else if (rand < 0.7) {
                            grid[i][j].setType(CellType.GRASS);
                        } else if (rand < 0.9) {
                            grid[i][j].setType(CellType.DIRT);
                        } else {
                            grid[i][j].setType(CellType.WATER);
                        }
                    }
                }
            }
        }


        private boolean isValid(int row, int col) {
            return row > 0 && row < rows - 1 && col > 0 && col < cols - 1;
        }


        public List<Cell> getNeighbors(Cell cell) {
            List<Cell> neighbors = new ArrayList<>();
            int[][] directions = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};


            for (int[] dir : directions) {
                int newRow = cell.getRow() + dir[0];
                int newCol = cell.getCol() + dir[1];


                if (newRow >= 0 && newRow < rows && newCol >= 0 && newCol < cols
                        && !grid[newRow][newCol].isWall()) {
                    neighbors.add(grid[newRow][newCol]);
                }
            }
            return neighbors;
        }


        public int getRows() { return rows; }
        public int getCols() { return cols; }
        public Cell[][] getGrid() { return grid; }
        public Cell getStart() { return start; }
        public Cell getEnd() { return end; }
        public Cell getCell(int row, int col) { return grid[row][col]; }
    }

    public static class Main {
        public static void main(String[] args) {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }


            SwingUtilities.invokeLater(() -> {
                GameFrame frame = new GameFrame();
                frame.setVisible(true);
            });
        }
    }

    public static class PathResult {
        private List<Cell> path;
        private int totalCost;
        private long executionTime;
        private String algorithmName;


        public PathResult(List<Cell> path, int totalCost, long executionTime, String algorithmName) {
            this.path = path;
            this.totalCost = totalCost;
            this.executionTime = executionTime;
            this.algorithmName = algorithmName;
        }


        public List<Cell> getPath() { return path; }
        public int getTotalCost() { return totalCost; }
        public long getExecutionTime() { return executionTime; }
        public String getAlgorithmName() { return algorithmName; }


        public int getPathLength() {
            return path != null ? path.size() : 0;
        }
    }

    public static class MazePanel extends JPanel {
        private static final int CELL_SIZE = 20;
        private static final int MAZE_SIZE = 25;


        private Maze maze;
        private List<Cell> currentPath;  // ✨ BARU!


        public MazePanel() {
            this.maze = new Maze(MAZE_SIZE, MAZE_SIZE);
            this.maze.generateMaze();
            this.currentPath = null;


            int panelSize = MAZE_SIZE * CELL_SIZE;
            setPreferredSize(new Dimension(panelSize, panelSize));
            setBackground(Color.LIGHT_GRAY);
        }


        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);


            Cell[][] grid = maze.getGrid();
            for (int i = 0; i < maze.getRows(); i++) {
                for (int j = 0; j < maze.getCols(); j++) {
                    Cell cell = grid[i][j];
                    int x = j * CELL_SIZE;
                    int y = i * CELL_SIZE;


                    Color cellColor;
                    if (cell.isWall()) {
                        cellColor = Color.BLACK;
                    } else if (cell.isStart()) {
                        cellColor = Color.GREEN;
                    } else if (cell.isEnd()) {
                        cellColor = Color.RED;
                    } else if (cell.isPath() && currentPath != null) {  // ✨ BARU!
                        cellColor = Color.YELLOW;
                    } else {
                        cellColor = cell.getType().getColor();
                    }


                    g.setColor(cellColor);
                    g.fillRect(x, y, CELL_SIZE, CELL_SIZE);


                    g.setColor(Color.GRAY);
                    g.drawRect(x, y, CELL_SIZE, CELL_SIZE);
                }
            }
        }


        public void generateNewMaze() {
            maze.generateMaze();
            currentPath = null;
            repaint();
        }


        // ✨ BARU: Show path
        public void showPath(PathResult result) {
            clearPath();


            if (result.getPath() != null) {
                currentPath = result.getPath();
                for (Cell cell : currentPath) {
                    if (!cell.isStart() && !cell.isEnd()) {
                        cell.setPath(true);
                    }
                }
            }


            repaint();
        }


        // ✨ BARU: Clear path
        public void clearPath() {
            for (int i = 0; i < maze.getRows(); i++) {
                for (int j = 0; j < maze.getCols(); j++) {
                    Cell cell = maze.getGrid()[i][j];
                    cell.setPath(false);
                }
            }
            currentPath = null;
        }


        public Maze getMaze() {
            return maze;
        }
    }
}


