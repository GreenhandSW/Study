package design;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Visitor {
    interface AllFile{
        void show(Explorer explorer);
    }
    static class File implements AllFile{
        private final String name;
        public File(String name){
            this.name=name;
        }
        @Override
        public void show(Explorer explorer) {
            System.out.printf("这是个文件：%s\n", name);
            explorer.show(this);
        }
    }
    static class Directory implements AllFile{
        private final String name;
        private final List<File> files=new ArrayList<>();
        public Directory(String name){
            this.name=name;
        }
        public void addFile(File... files){
            this.files.addAll(Arrays.asList(files));
        }
        @Override
        public void show(Explorer explorer) {
            System.out.printf("这是个文件夹：%s\n", name);
            explorer.show(this);
            for(File file: files){
                file.show(explorer);
            }
        }
    }
    interface Explorer{
        void show(File file);
        void show(Directory directory);
    }
    static class FileExplorer implements Explorer{
        @Override
        public void show(File file) {
            System.out.println("雷吼，介似个文件："+file.name);
        }
        @Override
        public void show(Directory directory) {
            System.out.println("雷吼，介似个文件夹："+directory.name);
        }
    }
    public static void main(String[] args) {
        File f1=new File("f1");
        File f2=new File("f2");
        File f3=new File("f3");
        Directory d1=new Directory("d1");
        d1.addFile(f1, f2);
        Explorer explorer=new FileExplorer();
        f3.show(explorer);
        d1.show(explorer);
    }
}
