import javax.swing.JFrame;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;
import javax.swing.WindowConstants;

public class LogDisplay {
    private final JFrame frame;
    private final JTextArea textArea;
    private int lineCount = 0;
    private static final int MAX_LINES = 1000;

    public LogDisplay() {
        frame = new JFrame("Log monitor");
        textArea = new JTextArea(10,10); // fit
        textArea.setEditable(false);

        JScrollPane pane = new JScrollPane(textArea);
        frame.add(pane);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    // adding n lines

    public void addLine(String line) {
        if(lineCount >= MAX_LINES) {
            String text = textArea.getText();
            int firstLineEnd = text.lastIndexOf("\n");
            if(firstLineEnd >=0) {
                textArea.setText(text.substring(firstLineEnd + 1));
                lineCount--;
            }
        }
        textArea.append(line + "\n");
        lineCount++;
    }

    public void show() {
        frame.setVisible(true);
    }

    public void close() {
        frame.dispose();
    }
}
