package logicrepository;

public class LogicException extends Exception {
  private static final long serialVersionUID = -1L;

  public LogicException(Exception e) {
    super("C Monitor Generation  Exception:" + e.getMessage());
  }

  public LogicException(String str) {
    super(str);
  }

  public String toString() {
    return getMessage();
  }
}
