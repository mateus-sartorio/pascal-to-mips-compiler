package webapp.dto;

import java.util.List;

/**
 * Um procedimento ou função, embutido ou declarado pelo usuário. O front-end monta a
 * assinatura a partir destes campos, em vez de receber texto já formatado.
 *
 * @param identifier o nome do procedimento ou função
 * @param kind {@code "procedure"} ou {@code "function"}
 * @param returnType o tipo de retorno, ou null para procedimentos
 * @param line a linha da declaração, ou null para rotinas embutidas
 * @param parameters os parâmetros, na ordem de declaração
 * @param localVariables as variáveis locais, vazio para rotinas embutidas
 */
public record RoutineDto(
  String identifier,
  String kind,
  String returnType,
  Integer line,
  List<SymbolDto> parameters,
  List<SymbolDto> localVariables
) {
}
