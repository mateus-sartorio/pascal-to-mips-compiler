package webapp.dto;

/**
 * Um literal de string coletado durante a análise semântica.
 *
 * @param label o rótulo emitido no código MIPS (por exemplo {@code __string0})
 * @param value o conteúdo do literal
 */
public record StringLiteralDto(
  String label,
  String value
) {
}
