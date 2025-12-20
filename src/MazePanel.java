import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.util.List;


/**
 * MazePanel Class - Panel dengan animasi smooth dan modern design
 * UPDATED: Added animations, rounded corners, shadows, glow effects
 */
public class MazePanel extends JPanel {
    private static final int CELL_SIZE = 18;
    private static final int MAZE_SIZE = 25;
    private static final int CORNER_RADIUS = 3;


    private Maze maze;
    private List<Cell> currentPath;
    private Timer animationTimer;
    private int animationStep = 0;
    private static final Color BG_COLOR = new Color(34, 44, 47);
    private static final Color WALL_COLOR = new Color(55, 71, 79);
    private static final Color START_COLOR = new Color(76, 175, 80);
    private static final Color END_COLOR = new Color(244, 67, 54);
    private static final Color PATH_COLOR = new Color(255, 193, 7);
    private static final Color EXPLORING_COLOR = new Color(100, 181, 246);


    public MazePanel() {
        this.maze = new Maze(MAZE_SIZE, MAZE_SIZE);
        this.maze.generateMaze();


        int panelSize = MAZE_SIZE * CELL_SIZE;
        setPreferredSize(new Dimension(panelSize, panelSize));
        setBackground(BG_COLOR);
    }


    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;


        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING,
                RenderingHints.VALUE_RENDER_QUALITY);


        drawBackgroundPattern(g2d);


        Cell[][] grid = maze.getGrid();
        for (int i = 0; i < maze.getRows(); i++) {
            for (int j = 0; j < maze.getCols(); j++) {
                Cell cell = grid[i][j];
                int x = j * CELL_SIZE;
                int y = i * CELL_SIZE;
                drawCell(g2d, cell, x, y);
            }
        }
    }


    private void drawBackgroundPattern(Graphics2D g2d) {
        int width = getWidth();
        int height = getHeight();


        GradientPaint gradient = new GradientPaint(
                0, 0, new Color(34, 44, 47),
                width, height, new Color(42, 54, 59, 200)
        );
        g2d.setPaint(gradient);
        g2d.fillRect(0, 0, width, height);


        g2d.setColor(new Color(255, 255, 255, 8));
        for (int i = 0; i < width; i += 40) {
            g2d.drawLine(i, 0, i, height);
        }
        for (int j = 0; j < height; j += 40) {
            g2d.drawLine(0, j, width, j);
        }


        drawCornerAccent(g2d, 0, 0, true);
        drawCornerAccent(g2d, width, 0, false);
        drawCornerAccent(g2d, 0, height, true);
        drawCornerAccent(g2d, width, height, false);
    }


    private void drawCornerAccent(Graphics2D g2d, int x, int y, boolean isLeft) {
        g2d.setColor(new Color(106, 168, 79, 30));
        int size = 80;
        int sign = isLeft ? 1 : -1;
        g2d.fillOval(x - size / 2, y - size / 2, size, size);


        g2d.setColor(new Color(106, 168, 79, 15));
        int size2 = 120;
        g2d.fillOval(x - size2 / 2 + sign * 20, y - size2 / 2 + 20, size2, size2);
    }


    // ⭐⭐⭐ METHOD INI ADA PERUBAHAN - Tambahan glow effect ⭐⭐⭐
    private void drawCell(Graphics2D g2d, Cell cell, int x, int y) {
        Color cellColor;
        boolean drawIcon = false;
        String icon = "";


        if (cell.isWall()) {
            cellColor = WALL_COLOR;
        } else if (cell.isStart()) {
            cellColor = START_COLOR;
            drawIcon = true;
            icon = "S";
            drawGlowEffect(g2d, x, y, START_COLOR);  // ⭐ GLOW!
        } else if (cell.isEnd()) {
            cellColor = END_COLOR;
            drawIcon = true;
            icon = "E";
            drawGlowEffect(g2d, x, y, END_COLOR);    // ⭐ GLOW!
        } else if (cell.isPath() && currentPath != null) {
            cellColor = PATH_COLOR;
        } else if (cell.isExploring()) {
            int alpha = 100 + (int) (Math.sin(animationStep * 0.3) * 50);
            cellColor = new Color(
                    EXPLORING_COLOR.getRed(),
                    EXPLORING_COLOR.getGreen(),
                    EXPLORING_COLOR.getBlue(),
                    Math.max(0, Math.min(255, alpha))
            );
        } else {
            cellColor = cell.getType().getColor();
        }


        if (!cell.isWall()) {
            g2d.setColor(new Color(0, 0, 0, 30));
            g2d.fill(new RoundRectangle2D.Float(x + 2, y + 2, CELL_SIZE - 2,
                    CELL_SIZE - 2, CORNER_RADIUS, CORNER_RADIUS));
        }


        g2d.setColor(cellColor);
        g2d.fill(new RoundRectangle2D.Float(x, y, CELL_SIZE - 1, CELL_SIZE - 1,
                CORNER_RADIUS, CORNER_RADIUS));


        if (!cell.isWall()) {
            g2d.setColor(cell.getType().getDarkerColor());
            g2d.setStroke(new BasicStroke(1.5f));
            g2d.draw(new RoundRectangle2D.Float(x, y, CELL_SIZE - 1, CELL_SIZE - 1,
                    CORNER_RADIUS, CORNER_RADIUS));
        }


        if (drawIcon) {
            g2d.setColor(Color.WHITE);
            g2d.setFont(new Font("Segoe UI", Font.BOLD, 12));
            FontMetrics fm = g2d.getFontMetrics();
            int iconX = x + (CELL_SIZE - fm.stringWidth(icon)) / 2;
            int iconY = y + ((CELL_SIZE - fm.getHeight()) / 2) + fm.getAscent();
            g2d.drawString(icon, iconX, iconY);
        }
    }


    // ⭐⭐⭐ METHOD BARU: Glow effect! ⭐⭐⭐
    private void drawGlowEffect(Graphics2D g2d, int x, int y, Color baseColor) {
        int glowSize = 4;
        for (int i = 0; i < glowSize; i++) {
            int alpha = (glowSize - i) * 15;
            g2d.setColor(new Color(
                    baseColor.getRed(),
                    baseColor.getGreen(),
                    baseColor.getBlue(),
                    alpha
            ));
            g2d.drawRoundRect(
                    x - i, y - i,
                    CELL_SIZE + i * 2 - 1,
                    CELL_SIZE + i * 2 - 1,
                    CORNER_RADIUS + i,
                    CORNER_RADIUS + i
            );
        }
    }


    public void generateNewMaze() {
        if (animationTimer != null && animationTimer.isRunning()) {
            animationTimer.stop();
        }


        maze.generateMaze();
        currentPath = null;
        animationStep = 0;
        repaint();
    }


    public void showPathWithAnimation(PathResult result) {
        if (animationTimer != null && animationTimer.isRunning()) {
            animationTimer.stop();
        }


        clearPath();


        if (result.getPath() == null) {
            return;
        }


        currentPath = result.getPath();
        animationStep = 0;


        animationTimer = new Timer(30, e -> {
            animationStep++;
            if (animationStep < currentPath.size()) {
                Cell cell = currentPath.get(animationStep);
                if (!cell.isStart() && !cell.isEnd()) {
                    cell.setPath(true);
                }
                repaint();
            } else {
                ((Timer) e.getSource()).stop();
            }
        });
        animationTimer.start();
    }


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


    public void clearPath() {
        for (int i = 0; i < maze.getRows(); i++) {
            for (int j = 0; j < maze.getCols(); j++) {
                Cell cell = maze.getGrid()[i][j];
                cell.setPath(false);
                cell.setExploring(false);
                cell.setVisitOrder(-1);
            }
        }
        currentPath = null;
    }


    public Maze getMaze() {
        return maze;
    }
}
