import java.awt.Color;

/**
 * SESI 3: + UI/UX - CellType (FINAL!)
 * Modern nature-inspired color palette
 */
public enum CellType {
    // Palet warna forest/nature yang harmonis
    EMPTY(0, new Color(245, 247, 242), "Clear Path"), // Off-white
    GRASS(1, new Color(106, 168, 79), "Grass"), // Forest green
    DIRT(5, new Color(180, 142, 91), "Dirt Path"), // Warm brown
    WATER(10, new Color(61, 133, 198), "Water"); // River blue

    private final int cost;
    private final Color color;
    private final String name;

    CellType(int cost, Color color, String name) {
        this.cost = cost;
        this.color = color;
        this.name = name;
    }

    public int getCost() {
        return cost;
    }

    public Color getColor() {
        return color;
    }

    public String getName() {
        return name;
    }

    /**
     * Mendapatkan warna yang sedikit lebih gelap (untuk border/shadow)
     */
    public Color getDarkerColor() {
        return new Color(
                Math.max(0, color.getRed() - 30),
                Math.max(0, color.getGreen() - 30),
                Math.max(0, color.getBlue() - 30));
    }
}
