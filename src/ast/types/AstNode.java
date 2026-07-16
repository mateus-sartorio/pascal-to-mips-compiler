package ast.types;

/**
 * Representa um nó na árvore de sintaxe abstrata (AST).
 * Cada nó possui um identificador único.
 */
public abstract class AstNode {
  /**
   * Identificador único do nó.
   */
  public final int id;

  /**
   * Construtor para criar um nó AST com um identificador único.
   *
   * @param id Identificador único para o nó.
   */
  public AstNode(int id) {
    this.id = id;
  }

  /**
   * Retorna o identificador do nó no formato de notação DOT.
   *
   * @return Identificador do nó no formato de notação DOT.
   */
  public String getDotNotationIdentifier() {
    return "\"%s#%d\"".formatted(getClass().getSimpleName(), id);
  }

  /**
   * Retorna a representação do nó em notação DOT.
   *
   * @return Representação do nó em notação DOT.
   */
  public abstract String toDotNotation();
}
