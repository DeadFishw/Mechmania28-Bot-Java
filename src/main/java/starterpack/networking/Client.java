package starterpack.networking;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import starterpack.game.GameState;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Client {
  private final int portNumber;
  private Socket socket;
  private BufferedReader in;
  private PrintWriter out;
  private boolean connected = false;

  private static final Logger LOGGER = LogManager.getLogger(Client.class.getName());


  public Client(int portNumber) {
    this.portNumber = portNumber;
  }

  /** Connects to the server at the port number passed into the constructor. */
  public void connect() {
    try {
      this.socket = new Socket("localhost", portNumber);
      this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
      this.out = new PrintWriter(socket.getOutputStream(), true);
      this.connected = true;
    } catch (IOException e) {
      LOGGER.info("Connect to engine failed...");
    }
  }

  /**
   * Reads a single line from a server at the port number passed into the constructor.
   *
   * @return Line read from server.
   */
  public String read() {
    String readLine = null;
    try {
      readLine = in.readLine();
    } catch (IOException e) {
      e.printStackTrace();
    }
    LOGGER.debug("Received message " + readLine);
    return readLine;
  }

  /**
   * Writes an object as JSON into the server's output stream.
   *
   * @param obj Object to be written.
   */
  public void write(Object obj) {
    try {
      write(new ObjectMapper().writeValueAsString(obj));
    } catch (JsonProcessingException e) {
      e.printStackTrace();
    }
  }

  /**
   * Writes a string into the server's output stream.
   *
   * @param string String to be written.
   */
  public void write(String string) {
    LOGGER.debug("Sending message " + string);
    out.println(string);
  }

  public void disconnect() {
    if (!socket.isClosed()) {
      try {
        socket.close();
      } catch (IOException e) {
        e.printStackTrace();
        return;
      }
    }
    this.connected = false;
  }

  public static GameState parseMessageAsGameState(String message) {

    GameState result;
    try {
      result = new ObjectMapper().readValue(message, GameState.class);
      return result;
    } catch (JsonProcessingException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }

    return null;
  }

  public boolean isConnected() {
    return connected;
  }
}
