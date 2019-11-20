package cn.zyj.tunnel.pathfind;

import java.util.Arrays;

public class Vec {

    private final int[] data;

    public Vec(int[] data) {
        this.data = Arrays.copyOf(data, data.length);
    }

    public Vec(int dimNum, int val) {
        data = new int[dimNum];
        for (int i = 0; i < data.length; i++) {
            data[i] = val;
        }
    }

    public Vec copy() {
        return new Vec(Arrays.copyOf(data, data.length));
    }

    public int get(int i) {
        return data[i];
    }

    public Vec set(int i, int val) {
        data[i] = val;
        return this;
    }

    public Vec add(Vec v) {
        for (int i = 0; i < data.length; i++) {
            data[i] += v.data[i];
        }
        return new Vec(data);
    }

    public Vec subtract(Vec v) {
        for (int i = 0; i < data.length; i++) {
            data[i] -= v.data[i];
        }
        return new Vec(data);
    }

    public Vec negate(Vec v) {
        for (int i = 0; i < data.length; i++) {
            data[i] = -v.data[i];
        }
        return new Vec(data);
    }

    public int distance() {
        int distance = 0;
        for (int i = 0; i < data.length; i++) {
            distance += Math.abs(data[i]);
        }
        return distance;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Vec vec = (Vec) o;

        return Arrays.equals(data, vec.data);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(data);
    }

    @Override
    public String toString() {
        return "Vec" + Arrays.toString(data);
    }
}
