package design;

public class Bridge {
    interface Lunch {
        String getLunch();
    }

    static class Rice implements Lunch {
        @Override
        public String getLunch() {
            return "rice";
        }
    }

    static class Noodle implements Lunch {
        @Override
        public String getLunch() {
            return "noodle";
        }
    }

    abstract static class MakeLunch {
        protected Lunch lunch;

        public MakeLunch(Lunch lunch) {
            this.lunch = lunch;
        }

        abstract void make();
    }

    static class FriedLunch extends MakeLunch {
        public FriedLunch(Lunch lunch) {
            super(lunch);
        }

        public void make() {
            System.out.printf("Will make fried %s\n", lunch.getLunch());
        }
    }

    static class CookLunch extends MakeLunch {
        public CookLunch(Lunch lunch) {
            super(lunch);
        }

        public void make() {
            System.out.printf("Will make cook %s\n", lunch.getLunch());
        }
    }

    public static void main(String[] args) {
        MakeLunch cookNoodle = new CookLunch(new Noodle());
        cookNoodle.make();
        MakeLunch cookRice = new CookLunch(new Rice());
        cookRice.make();
        MakeLunch friedNoodle = new FriedLunch(new Noodle());
        friedNoodle.make();
        MakeLunch friedRice = new FriedLunch(new Rice());
        friedRice.make();
    }
}
