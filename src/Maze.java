import java.util.*;

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
                grid[i][j].setPath(false);
                grid[i][j].setExploring(false);
                grid[i][j].setVisitOrder(-1);
                grid[i][j].setStart(false);
                grid[i][j].setEnd(false);
            }
        }

        // Mulai dari posisi (1,1)
        int startRow = 1;
        int startCol = 1;
        Stack<Cell> stack = new Stack<>();
        Cell current = grid[startRow][startCol];
        current.setWall(false);
        stack.push(current);

        // DFS algorithm untuk carving maze
        while (!stack.isEmpty()) {
            current = stack.peek();
            List<Cell> unvisitedNeighbors = getUnvisitedNeighbors(current);

            if (!unvisitedNeighbors.isEmpty()) {
                Cell next = unvisitedNeighbors.get(
                        random.nextInt(unvisitedNeighbors.size()));
                removeWallBetween(current, next);
                next.setWall(false);
                stack.push(next);
            } else {
                stack.pop();
            }
        }

        List<Cell> availableCells = new ArrayList<>();
        for (int i = 1; i < rows - 1; i++) {
            for (int j = 1; j < cols - 1; j++) {
                if (!grid[i][j].isWall()) {
                    availableCells.add(grid[i][j]);
                }
            }
        }

        // Pick random start
        if (!availableCells.isEmpty()) {
            start = availableCells.get(random.nextInt(availableCells.size()));
            start.setStart(true);
            start.setWall(false);
            availableCells.remove(start);
        }

        // Pick random end (far from start for better challenge)
        if (!availableCells.isEmpty()) {
            Cell farthestCell = null;
            int maxDistance = 0;

            // Sample beberapa cell dan pilih yang paling jauh
            int samplesToCheck = Math.min(20, availableCells.size());
            for (int i = 0; i < samplesToCheck; i++) {
                Cell candidate = availableCells.get(
                        random.nextInt(availableCells.size()));
                int distance = Math.abs(candidate.getRow() - start.getRow()) +
                        Math.abs(candidate.getCol() - start.getCol());

                if (distance > maxDistance) {
                    maxDistance = distance;
                    farthestCell = candidate;
                }
            }

            end = farthestCell;
            end.setEnd(true);
            end.setWall(false);
        }


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
                if (!grid[i][j].isWall() &&
                        !grid[i][j].isStart() && !grid[i][j].isEnd()) {
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

    public int getRows() {
        return rows;
    }

    public int getCols() {
        return cols;
    }

    public Cell[][] getGrid() {
        return grid;
    }

    public Cell getStart() {
        return start;
    }

    public Cell getEnd() {
        return end;
    }

    public Cell getCell(int row, int col) {
        return grid[row][col];
    }
}
