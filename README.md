# Custom Proxy Server
## Configuration Files

Ensure the following files are present in the config directory before running the application.

### 1. `server_config.txt`
This file defines the server's network and logging configuration.
* **Line 1:** The port number on which the server will listen.
* **Line 2:** The filename for the log file (stored automatically in the `logs` folder).

**Example `server_config.txt`:**
```text
7000
logFile.log
```

### 2. `blocked_domains.txt`
This file lists the domains that the proxy should block.
* Add each blocked domain on a new line.

**Example `blocked_domains.txt`:**
```text
example.com
doubleclick.net
facebook.com
```

## Setup & Execution

To run the proxy server, compile the source code and execute the `Main` class.

**Command Line Instructions:**

```bash
# Compile the Java file
javac Main.java

# Run the Server
java Main
```
