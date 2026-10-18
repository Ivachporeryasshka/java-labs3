package geometry2d;

import exceptions.InvalidParameterException;

public class Rectangle implements Figure {
    private static final long serialVersionUID = 1L;
    private final double width;
    private final double height;

    public Rectangle(double width, double height) {
        if (!Double.isFinite(width) || width <= 0) {
            throw new InvalidParameterException("Ширина должна быть положительным конечным числом. Получено: " + width);
        }
        if (!Double.isFinite(height) || height <= 0) {
            throw new InvalidParameterException("Высота должна быть положительным конечным числом. Получено: " + height);
        }
        this.width = width;
        this.height = height;
    }

    public double getWidth() {
        return width;
    }

    public double getHeight() {
        return height;
    }

    @Override
    public double area() {
        return width * height;
    }

    @Override
    public double perimeter() {
        return 2 * (width + height);
    }

    @Override
    public String toString() {
        return String.format("Rectangle(ширина=%.2f, высота=%.2f)", width, height);
    }
}