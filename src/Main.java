import java.io.File;
import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        System.out.println("Hello world!");

        String currentDir = System.getProperty("user.dir");
        String logPath = currentDir + File.separator + "test.log";

        try {
            File file = new File(logPath);
            if(!file.exists()){
                file.createNewFile();

            }
        } catch (
                IOException e) {
            System.out.println(e.getMessage());
            return;
        }

        LogGenerator generator = new LogGenerator(logPath);
        generator.generateTestLogs();

        LogMonitor monitor = new LogMonitor(logPath);
        monitor.start();
    }
}