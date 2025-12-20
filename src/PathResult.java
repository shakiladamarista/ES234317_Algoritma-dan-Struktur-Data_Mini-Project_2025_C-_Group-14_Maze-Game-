import java.util.List;

public class PathResult {
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



