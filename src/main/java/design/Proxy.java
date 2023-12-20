package design;

public class Proxy {
    public interface File{
        void open();
    }
    static class LocalFile implements File{
        @Override
        public void open() {
            System.out.println("打开本地文件");
        }
    }
    static class FileProxy implements File{
        private final File file;
        public FileProxy(File file){
            this.file=file;
        }
        @Override
        public void open() {
            file.open();
        }
    }
    public static void main(String[] args) {
        File file=new LocalFile();
        FileProxy fileProxy=new FileProxy(file);
        fileProxy.open();
    }
}
