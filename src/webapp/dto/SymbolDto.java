package webapp.dto;

/**
 * Uma variável, parâmetro ou variável local da tabela de símbolos.
 *
 * @param identifier o nome declarado
 * @param type o tipo já formatado (por exemplo {@code integer} ou {@code array[1..10] of integer})
 * @param line a linha da declaração, ou null para parâmetros embutidos
 */
public record SymbolDto(
  String identifier,
  String type,
  Integer line
) {
}
