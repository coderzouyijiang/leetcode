package cn.zyj.tunnel.pathfind;

public class PathCost {

    public int toStart;
    public int toTarget;
    public int totalCost;

    public Vec point;

    public PathCost(int toStart, int toTarget, Vec point) {
        this.toStart = toStart;
        this.toTarget = toTarget;
        this.totalCost = toStart + toTarget;
        this.point = point;
    }

    @Override
    public String toString() {
        return "PathCost[" +
                "" + toStart +
                "," + toTarget +
                "," + totalCost +
                ']';
    }
}
