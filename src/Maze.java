import java.util.*;


/**
 * SESI 1: BASE - Maze Class
 * Generate labirin menggunakan DFS algorithm
 */
public class Maze {
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



