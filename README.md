# Problem statement
This problem requires you to implement a log watching solution (similar to the `tail -f` command in UNIX). However, in this case, the log file is hosted on a remote machine (same machine as your server code).

The log file is in append-only mode.  

You may implement the server in any programming language.

You have to implement the following:

1. A server side program to monitor the given log file and capable of streaming updates that happen in it. This will run on the same machine as the log file.
2. A web based client (accessible via URL like http://localhost/log) that prints the updates in the file **as and when they happen and NOT upon page refresh**. The page should be loaded once and it should keep getting updated in real-time.
3. The user sees the last 10 lines in the file when they visit the page for the first time.

### Problem Constraints
1. The server should push updates to the clients as we have to be as real time as possible.
2. Be aware that the log file may be several GB, how to optimise for retrieving the last 10 lines?
3. The server should not retransmit the entire file every time. It should only send the updates.
4. The server should be able to handle multiple clients at the same time.
5. The web page should not stay in loading state post the first load and it should not reload thereafter as well.
6. You may not use off-the-shelf external libraries or tools to provide tail-like functionalities.

# My solution
This problem was given to me in an interview, and I had 2 hours to come up with a working solution. I had forgotten Computer Networks concepts, as well as how to set up a front-end and link it to a back-end. So, since time was constrained, instead of implementing the full client-server solution across machines, I implemented a very basic local version that demonstrates the core functionality. This simplified version uses threads in Java to simulate different components that would normally run on separate machines, and a front-end display using Java's Swing framework (JavaFX would've been better but I did not wish to take a risk with the set-up complexity).

### Key Components:

#### LogMonitor class
- Acts as the 'server' component but runs locally.
- Uses `RandomAccessFile` for efficient file reading, especially important for large files.
- Implements the "last N lines" reading by scanning backwards from file end using seek.
- Has a separate monitoring thread that simulates a server continuously watching for changes.
- [`SwingUtilities.invokeLater(...)`](https://docs.oracle.com/javase/8/docs/api/javax/swing/SwingUtilities.html) causes asynchronous execution on the AWT event dispatching thread.
- Uses Swing for display (simulating what would be a web client in the full solution).


#### LogDisplay class
- Simulates what would be the web client in the full solution.
- Uses Swing instead of HTML/JavaScript for simplicity.
- Maintains a maximum line limit (1000) to prevent memory issues.


#### LogGenerator class
- Simulates the log-writing application that would run on a remote machine.
- Uses a separate thread to continuously generate log entries.
- Demonstrates append-only behavior of a real log file.

### Thread Simulation:
- Main Thread: Handles UI events and display.
- Monitor Thread: Simulates the server process watching the file.
- Generator Thread: Simulates the remote application writing logs.

### Running the program
Run by compiling any `javac` compiler (>17) and then running using `java`:
```bash
javac Main.java LogMonitor.java LogGenerator.java LogDisplay.java
java Main
```

### The full solution would replace:
- Swing UI with a web interface
- Local file access with network communication
- Direct thread communication with WebSocket protocol
- Single display with multiple client support
