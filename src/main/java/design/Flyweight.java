package design;

import java.util.HashMap;

public class Flyweight {
    public interface Font{
        String getFont();
    }
    public static class Song implements Font{
        @Override
        public String getFont(){
            return "宋体";
        }
    }
    public static class Kai implements Font{
        @Override
        public String getFont(){
            return "楷体";
        }
    }
    public static class FontFactory{
        private final HashMap<String, Font> fonts;
        private static final FontFactory factory=new FontFactory();
        private FontFactory(){
            fonts=new HashMap<>();
            fonts.put("Song", new Song());
            fonts.put("Kai", new Kai());
        }
        public static FontFactory getInstance(){
            return factory;
        }
        public Font getFont(String fontName){
            return fonts.get(fontName);
        }
    }
    public static void main(String[] args) {
        FontFactory factory=FontFactory.getInstance();
        System.out.println(factory.getFont("Song").getFont());
        System.out.println(factory.getFont("Kai").getFont());
    }
}