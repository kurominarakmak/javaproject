package map;

public class MapObstacle {
    protected static int[][] mapObstacle;

    public MapObstacle(int[][] mapObstacle) {
        this.mapObstacle = mapObstacle;  
    }

    public static int[][] getMapObstacle() {
        return mapObstacle;
    }
}
