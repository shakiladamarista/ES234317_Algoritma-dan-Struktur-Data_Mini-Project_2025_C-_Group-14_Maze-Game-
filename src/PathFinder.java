import java.util.*;

public class PathFinder {


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


