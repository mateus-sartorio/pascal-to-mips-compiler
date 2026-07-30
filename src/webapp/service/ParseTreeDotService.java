package webapp.service;

import java.util.Arrays;
import java.util.List;

import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.TerminalNode;
import org.antlr.v4.runtime.tree.Trees;
import org.springframework.stereotype.Service;

/**
 * Converte a árvore de derivação do ANTLR em notação DOT, para que ela possa ser
 * desenhada da mesma forma que a AST.
 *
 * <p>Os rótulos vêm de {@link Trees#getNodeText}, o mesmo mecanismo usado por
 * {@code toStringTree}, de modo que a imagem e o texto sempre concordam.
 */
@Service
public class ParseTreeDotService {
  public String toDotNotation(ParseTree tree, Parser parser) {
    if (tree == null || parser == null) {
      return null;
    }

    List<String> ruleNames = Arrays.asList(parser.getRuleNames());
    StringBuilder dot = new StringBuilder("digraph ParseTree {\n");

    appendNode(dot, tree, ruleNames, new int[] { 0 });

    return dot.append("}\n").toString();
  }

  /**
   * Percorre a árvore em profundidade, numerando os nós. O contador é compartilhado por
   * referência para que cada nó receba um identificador único.
   *
   * @return o identificador atribuído ao nó visitado
   */
  private int appendNode(StringBuilder dot, ParseTree node, List<String> ruleNames, int[] counter) {
    int id = counter[0]++;
    String label = escape(Trees.getNodeText(node, ruleNames));

    // Folhas são tokens do programa; nós internos são regras da gramática. A forma
    // distingue os dois de relance, e a cor é aplicada na renderização.
    if (node instanceof TerminalNode) {
      dot.append("  n%d [label=\"%s\", shape=box, fillcolor=\"#1d3a5c\"];\n".formatted(id, label));
    } else {
      dot.append("  n%d [label=\"%s\"];\n".formatted(id, label));
    }

    for (int index = 0; index < node.getChildCount(); index++) {
      int childId = appendNode(dot, node.getChild(index), ruleNames, counter);
      dot.append("  n%d -> n%d;\n".formatted(id, childId));
    }

    return id;
  }

  /**
   * Protege o rótulo contra os caracteres que encerrariam a string DOT.
   */
  private String escape(String text) {
    return text
      .replace("\\", "\\\\")
      .replace("\"", "\\\"")
      .replace("\n", "\\n")
      .replace("\r", "");
  }
}
