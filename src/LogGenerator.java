import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LogGenerator {
    private final String logFilePath;
    private boolean running = true;
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    public LogGenerator(String logFilePath) {
        this.logFilePath = logFilePath;
    }

    public void generateTestLogs() throws IOException {
        try (FileWriter writer = new FileWriter(logFilePath)) {
            for (int i = 0; i < 15; i++) {
                String logEntry = String.format("[%s] Initial entry #%d%n", LocalDateTime.now().format(formatter), i);
                writer.write(logEntry);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        new Thread(() -> {
            try {
                int counter = 1;
                while (running) {
                    try (FileWriter writer = new FileWriter(logFilePath, true)) {
                        String logEntry = String.format("[%s] Live log entry #%d - Sample log message%n", LocalDateTime.now().format(formatter), counter++);
                        writer.write(logEntry);
                        writer.flush();
                    }
                    Thread.sleep(2000);
                }
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }

    public void stop() {
        running = false;
    }
}