package ch.blockwoche.pixelmaler;

import android.graphics.Point;

import java.util.Objects;

public class PointColor {
    private Point point;
    private String color;

    public PointColor(Point point, String color) {
        this.point = point;
        this.color = color;
    }

    public Point getPoint() {
        return point;
    }

    public void setPoint(Point point) {
        this.point = point;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PointColor that = (PointColor) o;
        return that.point.x == point.x && that.point.y == point.y && color.equals(that.color);
    }

    @Override
    public int hashCode() {
        return Objects.hash(point, color);
    }

    @Override
    public String toString() {
        return "PointColor{" +
                "point=" + point +
                ", color='" + color + '\'' +
                '}';
    }
}
