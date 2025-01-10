import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;
import javax.swing.SwingUtilities;

public class LogMonitor {
    private final String logFilePath;
    private boolean running = true;
    private final LogDisplay display;
    private long lastPosition = 0;


    public LogMonitor(String logFilePath) {
        this.logFilePath = logFilePath;
        this.display = new LogDisplay();
    }

    public void start() {
        // last 10 lines ---- TODO (extend till N as we need more later)
        List<String> lastLines = getLastNLines(11); // N->10
        for (String line: lastLines) {
            display.addLine(line); // TODO: display functionallity

        }
        // real time, multiple clients --- multithreading??

        Thread monitorThead = new Thread(this::monitorFile);
        monitorThead.start();

        display.show();

    }

    private void monitorFile() {
        try (RandomAccessFile file = new RandomAccessFile(logFilePath, "r")) {
            lastPosition = file.length();

            while(running) {
                long length = file.length();
                if (length > lastPosition) {
                    file.seek(lastPosition);
                    String line;
                    while ((line = file.readLine()) != null) {
                        String finalLine = line;
                        SwingUtilities.invokeLater(() -> display.addLine(finalLine));
                    }
                    lastPosition = file.getFilePointer();
                }
            }
        } catch (
                IOException e) {
            // throw new RuntimeException(e);
            e.printStackTrace();
        }
    }

    private List<String> getLastNLines(int n) {
        List<String> lines = new ArrayList<>();
        try(RandomAccessFile file = new RandomAccessFile(logFilePath, "r")) {
            long fileLength = file.length();
            if (fileLength ==0) {
                return lines;
            }
            // end se
            long pointer = fileLength -1;
            int linesFound = 0;
            StringBuilder sb = new StringBuilder();

            while (pointer >= 0 && linesFound < n) {
                file.seek(pointer); // going to end
                int b = file.read();
                pointer--; // go up

                if(b == '\n' || pointer < 0) {
                    String line = sb.reverse().toString();
                    if(!line.isEmpty()) {
                        lines.add(0, line);
                        linesFound++;
                    }
                    sb = new StringBuilder();
                }
                else {
                    sb.append((char) b);
                }
            }

        } catch (
                IOException e) {
            e.printStackTrace();
        }
        return lines;
    }

    public void stop() {
        running = false;
        display.close();
    }
}
