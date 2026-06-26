package ast.types;

public abstract class AstNode {
  public final int id;

  public AstNode(int id) {
    this.id = id;
  }

  public String getDotNotationIdentifier() {
    return "\"%s#%d\"".formatted(getClass().getSimpleName(), id);
  }

  public abstract String toDotNotation();
}
