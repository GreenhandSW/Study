package design;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Composite {
    interface FileSystem{
        void info(int space);
    }
    static class File implements FileSystem{
        private final String name;
        public File(String name){
            this.name=name;
        }
        @Override
        public void info(int space) {
            System.out.printf("%s文件：%s\n", " ".repeat(space), name);
        }
    }
    static class Directory implements FileSystem{
        private final String name;
        private final List<FileSystem> files;

        public Directory(String name){
            this.name=name;
            this.files=new ArrayList<>();
        }
        public void addFileSystem(FileSystem... files){
            this.files.addAll(Arrays.asList(files));
        }
        @Override
        public void info(int space) {
            System.out.printf("%s文件夹：%s\n", " ".repeat(space), name);
            files.forEach(file->file.info(space+2));
        }
    }
    public static void main(String[] args) {
        Directory dir1=new Directory("dir1");
        dir1.addFileSystem(new File("f1.txt"), new File("f2.txt"));
        Directory dir2=new Directory("dir2");
        dir2.addFileSystem(new File("f3.txt"), dir1, dir1);
        dir2.info(0);
    }
}
