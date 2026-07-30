package webapp.dto;

import java.util.List;

/**
 * As tabelas produzidas pela análise semântica, em forma estruturada.
 *
 * @param globalVariables as variáveis globais do programa
 * @param routines os procedimentos e funções declarados pelo usuário
 * @param builtInRoutines os procedimentos e funções embutidos do compilador
 * @param stringLiterals os literais de string coletados, com o rótulo usado no MIPS
 */
public record SymbolTablesDto(
  List<SymbolDto> globalVariables,
  List<RoutineDto> routines,
  List<RoutineDto> builtInRoutines,
  List<StringLiteralDto> stringLiterals
) {
}
