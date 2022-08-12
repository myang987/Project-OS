package test;

import driver.Controller;
import io.Directory;
import io.FileEditor;
import io.FileManager;
import util.PathInterpreter;

/**
 * A light-weight file system that omits commands-related initialization.
 */
public class MockController extends Controller {

  private Directory workingDir;
  private Directory rootDir;
  private final PathInterpreter pathInterpreter;

  public MockController() {
    this.rootDir = new Directory("/", new FileManager(new FileEditor()));
    this.workingDir = rootDir;
    this.pathInterpreter = new PathInterpreter(this);
  }

  public void clear() {
    this.rootDir = null;
    this.workingDir = null;
  }

  @Override
  public void setWorkingDir(Directory workingDir) {
    this.workingDir = workingDir;
  }

  @Override
  public PathInterpreter getPathInterpreter() {
    return pathInterpreter;
  }

  @Override
  public Directory getRootDir() {
    return rootDir;
  }

  @Override
  public Directory getWorkingDir() {
    return workingDir;
  }
}
