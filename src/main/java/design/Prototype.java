package design;

public class Prototype {
    /**
     * 抽象原型是Cloneable，声明了clone()方法
     * 具体原型是Shape，需要实现clone()
     */
    public static class Shape implements Cloneable {
        private String type;

        public Shape(String type) {
            this.type = type;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        @Override
        public Shape clone() throws CloneNotSupportedException {
            Shape shape = (Shape) super.clone();
            // 深拷贝需要在调用父类的clone()后，继续复制子类特有的的一些属性
            shape.setType(type);
            return shape;
        }

        @Override
        public String toString() {
            return type;
        }
    }

    /**
     * 客户端
     */
    public static class Client {
        public static void main(String[] args) throws CloneNotSupportedException {
            Shape shape = new Shape("Circle");
            Shape cloned = shape.clone();
            System.out.printf("original: %s\n", shape);
            System.out.printf("cloned: %s\n", cloned);
        }
    }
}
