package com.jbs.universe.screen.components;

public class Plane {
    public Point location;
    public int width;
    public int height;

    public Plane(Point location, int width, int height) {
        this.location = location;
        this.width = width;
        this.height = height;
    }

    public Plane(int x, int y, int width, int height) {
        this.location = new Point(x, y);
        this.width = width;
        this.height = height;
    }

    public Plane(int width, int height) {
        this.location = new Point(0, 0);
        this.width = width;
        this.height = height;
    }
}
