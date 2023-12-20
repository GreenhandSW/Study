package design;

public class AbstractFactory {
    public interface Pen {
        void stroke();
    }

    public static class Pencil implements Pen {
        @Override
        public void stroke() {
            System.out.println("Will draw with Pencil");
        }
    }

    public static class Marker implements Pen {
        @Override
        public void stroke() {
            System.out.println("Will draw with Marker");
        }
    }

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

    public static abstract class DrawShapeFactory {
        abstract Pen getPen();

        abstract Shape createShape();
    }

    public static class DrawCircleFactory extends DrawShapeFactory {
        @Override
        Pen getPen() {
            return new Pencil();
        }

        @Override
        Shape createShape() {
            return new Circle();
        }
    }

    public static class DrawRectangleFactory extends DrawShapeFactory {
        @Override
        Pen getPen() {
            return new Marker();
        }

        @Override
        Shape createShape() {
            return new Rectangle();
        }
    }

    public static void main(String[] args) {
        DrawShapeFactory circleFactory = new DrawCircleFactory();
        Pen pencil = circleFactory.getPen();
        Shape circle = circleFactory.createShape();
        pencil.stroke();
        circle.draw();

        DrawShapeFactory rectangleFactory = new DrawRectangleFactory();
        Pen marker = rectangleFactory.getPen();
        Shape rectangle = rectangleFactory.createShape();
        marker.stroke();
        rectangle.draw();
    }
}
