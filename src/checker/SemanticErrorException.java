package checker;

/**
 * Sinaliza que a análise semântica encontrou um erro no programa analisado.
 *
 * <p>A mensagem voltada a quem escreveu o programa — "SEMANTIC ERROR (linha): ..." — já foi
 * impressa por quem lança a exceção; ela serve apenas para interromper a análise.
 *
 * <p>Existe como tipo próprio para separar as duas coisas que podiam estourar como
 * {@link RuntimeException}: um programa Pascal inválido, que termina com a mensagem impressa
 * e só isso, e um defeito do compilador, que continua estourando com o rastro de pilha —
 * justamente onde o rastro é útil.
 */
public class SemanticErrorException extends RuntimeException {
  public SemanticErrorException() {
    // A mensagem é a mesma de antes: a interface web usa esse texto como último recurso
    // quando não conseguiu capturar a linha impressa pelo checker.
    super("SEMANTIC ERROR");
  }
}
