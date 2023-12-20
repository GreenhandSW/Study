package design;

public class Factory {
    public interface Shape {
        void draw();
    }

    public static class Circle implements Shape {
        @Override
        public void draw() {
            System.out.println("Circle");
        }
    }

    public static class Rectangle implements Shape {
        @Override
        public void draw() {
            System.out.println("Rectangle");
        }
    }

    public static abstract class ShapeFactory {
        abstract Shape createShape();
    }

    public static class CircleFactory extends ShapeFactory {

        @Override
        Shape createShape() {
            return new Circle();
        }
    }

    public static class RectangleFactory extends ShapeFactory {

        @Override
        Shape createShape() {
            return new Rectangle();
        }
    }

    public static void main(String[] args) {
        ShapeFactory circleFactory = new CircleFactory();
        Shape circle = circleFactory.createShape();
        circle.draw();

        ShapeFactory rectangleFactory = new RectangleFactory();
        Shape rectangle = rectangleFactory.createShape();
        rectangle.draw();
    }
}
