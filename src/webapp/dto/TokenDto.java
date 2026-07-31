package webapp.dto;

/**
 * Um token do lexer.
 *
 * <p>startIndex e stopIndex são os deslocamentos do token no código-fonte, ambos
 * inclusivos, como o ANTLR os informa. É por eles que a interface encontra o token no
 * editor quando o mouse passa sobre a lista: linha e coluna exigiriam recontar as linhas
 * no navegador, e o texto do token nem sempre tem o mesmo tamanho do trecho que ele
 * ocupa na fonte.
 */
public record TokenDto(int index, String type, String text, int line, int column, int startIndex, int stopIndex) {
}
