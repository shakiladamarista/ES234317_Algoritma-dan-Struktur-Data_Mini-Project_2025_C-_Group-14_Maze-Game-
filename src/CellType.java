import java.awt.Color;

public enum CellType {
    EMPTY(0, Color.WHITE, "Clear Path"),
    GRASS(1, Color.GREEN, "Grass"),
    DIRT(5, Color.ORANGE, "Dirt Path"),
    WATER(10, Color.BLUE, "Water");


    private final int cost;
    private final Color color;
    private final String name;


    CellType(int cost, Color color, String name) {
        this.cost = cost;
        this.color = color;
        this.name = name;
    }


    public int getCost() { return cost; }
    public Color getColor() { return color; }
    public String getName() { return name; }
}



