package webapp.dto;

/**
 * Um literal de string coletado durante a análise semântica.
 *
 * <p>Só o conteúdo: o rótulo pelo qual o literal aparece na seção {@code .data} é coisa
 * do gerador de código, e a análise semântica não sabe nada sobre MIPS.
 *
 * @param value o conteúdo do literal
 */
public record StringLiteralDto(
  String value
) {
}