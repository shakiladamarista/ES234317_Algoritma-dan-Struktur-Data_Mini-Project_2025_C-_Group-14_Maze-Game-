import javax.swing.*;
import java.awt.*;
import java.util.List;


/**
 * SESI 2: MazePanel (UPDATED)
 * Tambah path visualization
 */
public class MazePanel extends JPanel {
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


