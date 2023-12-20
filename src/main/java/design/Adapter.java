package design;

public class Adapter {
    static class LegacyRectangle {
        public void display(int x1, int y1, int x2, int y2) {
            System.out.printf("旧的矩形：(%d, %d), (%d, %d)\n", x1, y1, x2, y2);
        }
    }

    interface Shape {
        void draw(int x, int y, int width, int height);
    }

    static class RectangleAdapter implements Shape {
        private final LegacyRectangle legacyRectangle;

        public RectangleAdapter(LegacyRectangle legacyRectangle) {
            this.legacyRectangle = legacyRectangle;
        }

        @Override
        public void draw(int x, int y, int width, int height) {
            int x2 = x + width, y2 = y + height;
            legacyRectangle.display(x, y, x2, y2);
        }
    }

    public static void main(String[] args) {
        LegacyRectangle legacyRectangle = new LegacyRectangle();
        Shape shapeAdapter = new RectangleAdapter(legacyRectangle);
        shapeAdapter.draw(10, 15, 50, 100);
    }
}