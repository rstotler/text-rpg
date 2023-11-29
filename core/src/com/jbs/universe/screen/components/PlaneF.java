package com.jbs.universe.screen.components;

public class PlaneF {
    public PointF location;
    public float width;
    public float height;

    public PlaneF(PointF location, float width, float height) {
        this.location = location;
        this.width = width;
        this.height = height;
    }

    public PlaneF(float x, float y, float width, float height) {
        this.location = new PointF(x, y);
        this.width = width;
        this.height = height;
    }

    public PlaneF(float width, float height) {
        this.location = new PointF(0, 0);
        this.width = width;
        this.height = height;
    }
}
