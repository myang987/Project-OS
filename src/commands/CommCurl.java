package commands;

import driver.Controller;
import exceptions.ConnectionFailedException;
import exceptions.JShellException;
import exceptions.WrongSyntaxException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import util.CmdOutput;
import util.CmdOutput.OutputBuilder;
import util.Message;

/**
 * Syntax: 1. curl URL
 * <p>
 * Retrieve the file at that URL and add it to the current working directory
 * <p>
 */

public class CommCurl extends AbstractCommand {

  public static final String alias = "curl";
  private static CommCurl instance;

  protected final Controller controller;

  /**
   * Sole constructor
   */
  protected CommCurl(Controller controller) {
    this.controller = controller;
    controller.registerCommand(this);
  }

  @Override
  public CmdOutput execute(Message args, OutputBuilder builder)
      throws JShellException {
    if (args.length() != 1) {
      throw new WrongSyntaxException("curl: Unknown syntax");
    }
    Iterator<String> itr = args.iterator();
    String url = itr.next();
    try {
      String c = getContents(url);
      String fileName = getFileName(url);
      controller.getWorkingDir().createFile(fileName).overwriteContentsAs(c);
    } catch (IOException e) {
      e.printStackTrace();
      System.out.println("curl: an error has occurred while reading this URL");
    }
    return builder.isIgnored(true).build();
  }

  private String getContents(String inputUrl)
      throws IOException, ConnectionFailedException {
    int httpResult;
    String contents;
    URL url = new URL(inputUrl);
    URLConnection urlConn = url.openConnection();
    urlConn.connect();
    HttpURLConnection httpConn = (HttpURLConnection) urlConn;
    httpResult = httpConn.getResponseCode();
    if (httpResult != HttpURLConnection.HTTP_OK) {
      throw new ConnectionFailedException("curl: connection failed or invalid "
          + "url");
    } else {
      InputStreamReader isr = new InputStreamReader(
          urlConn.getInputStream(), StandardCharsets.UTF_8);
      BufferedReader reader = new BufferedReader(isr);
      StringBuilder sb = new StringBuilder();
      String line;
      line = reader.readLine();
      while (line != null) {
        sb.append(line).append("\r\n");
        line = reader.readLine();
      }
      contents = sb.toString();
    }
    return contents;
  }


  /**
   * Get the file name from URL
   *
   * @param url the url that user input
   * @return the file name
   * @throws WrongSyntaxException if user input is not in accordance with the
   *                              syntax.
   */
  private String getFileName(String url)
      throws WrongSyntaxException {
    String fileName1;
    String[] urlMod = url.split("/");
    fileName1 = urlMod[urlMod.length - 1];
    if (fileName1.equals("")) {
      throw new WrongSyntaxException(": Invalid url");
    }
    String fileName;
    fileName = fileName1.replace(".", "_");
    return fileName;
  }


  /**
   * Returns a instance of this class. This method always return the same
   * instance on every call.
   *
   * @param controller the controller that this command belongs to.
   * @return the sole instance of this class
   */
  public static CommCurl getInstance(Controller controller) {
    if (instance == null) {
      instance = new CommCurl(controller);
    }
    return instance;
  }

  @Override
  public String getAlias() {
    return alias;
  }

  /**
   * @return the manual of this command
   */
  @Override
  public Message getManual() {
    String manual =
        "Syntax: 1. curl URL\n"
            + "\n"
            + "Retrieve the file at that URL and add it to the current working directory.";
    return new Message(manual);
  }
}
