public class Cell {
    private int row;
    private int col;
    private CellType type;
    private boolean isWall;
    private boolean isStart;
    private boolean isEnd;
    private boolean isPath;
    private boolean isExploring;
    private int visitOrder;

    public Cell(int row, int col) {
        this.row = row;
        this.col = col;
        this.type = CellType.EMPTY;
        this.isWall = false;
        this.isStart = false;
        this.isEnd = false;
        this.isPath = false;
        this.isExploring = false;
        this.visitOrder = -1;
    }

    // ========== GETTER METHODS ==========

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public CellType getType() {
        return type;
    }

    public boolean isWall() {
        return isWall;
    }

    public boolean isStart() {
        return isStart;
    }

    public boolean isEnd() {
        return isEnd;
    }

    public boolean isPath() {
        return isPath;
    }

    public boolean isExploring() {
        return isExploring;
    }

    public int getVisitOrder() {
        return visitOrder;
    }

    // ========== SETTER METHODS ==========

    public void setType(CellType type) {
        this.type = type;
    }

    public void setWall(boolean wall) {
        this.isWall = wall;
    }

    public void setStart(boolean start) {
        this.isStart = start;
    }

    public void setEnd(boolean end) {
        this.isEnd = end;
    }

    public void setPath(boolean path) {
        this.isPath = path;
    }

    public void setExploring(boolean exploring) {
        this.isExploring = exploring;
    }

    public void setVisitOrder(int order) {
        this.visitOrder = order;
    }

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
}
