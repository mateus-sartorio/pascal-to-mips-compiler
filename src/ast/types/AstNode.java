package ast.types;

public abstract class AstNode {
  public String getDotNotationIdentifier() {
    return "\"" +getClass().getSimpleName() + "#" + System.identityHashCode(this) + "\"";
  }

  public abstract String toDotNotation();
}
