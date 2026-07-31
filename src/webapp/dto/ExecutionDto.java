package webapp.dto;

/**
 * O resultado de executar o programa no interpretador.
 *
 * @param output tudo o que o programa escreveu na saída padrão
 * @param timedOut true quando o programa foi interrompido por exceder o tempo limite
 * @param truncated true quando a saída passou do limite e foi cortada
 * @param error a mensagem de erro de execução, ou null quando o programa terminou bem
 */
public record ExecutionDto(
  String output,
  boolean timedOut,
  boolean truncated,
  String error
) {
}
