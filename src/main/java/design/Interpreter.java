package design;

public class Interpreter {
    interface Expression{
        int calculate();
    }
    static class Number implements Expression{
        private final int value;
        public Number(int value){
            this.value=value;
        }
        @Override
        public int calculate() {
            return value;
        }
    }
    static class Add implements Expression{
        private final Expression left, right;
        public Add(Expression left, Expression right){
            this.left=left;
            this.right=right;
        }
        @Override
        public int calculate() { return left.calculate()+right.calculate(); }
    }
    static class Subtract implements Expression{
        private final Expression left, right;
        public Subtract(Expression left, Expression right){
            this.left=left;
            this.right=right;
        }
        @Override
        public int calculate() {
            return left.calculate()-right.calculate();
        }
    }

    public static void main(String[] args) {
        Expression expr=new Add(new Subtract(new Number(1), new Number(2)), new Number(3));
        System.out.printf("1-2+3=%d\n", expr.calculate());
    }
}
