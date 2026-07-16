package types;

public abstract class VariableType {
  public final PrimitiveTypeEnum basePrimitiveType;
  public final boolean isIndexed;

  /**
   * Construtor da classe VariableType
   * @param basePrimitiveType
   * @param isIndexed
   */
  public VariableType(PrimitiveTypeEnum basePrimitiveType, boolean isIndexed) {
    this.basePrimitiveType = basePrimitiveType;
    this.isIndexed = isIndexed;
  }

  /**
   * Método abstrato que deve ser implementado pelas subclasses para verificar se o tipo de variável é equivalente a outro tipo de variável
   * @param other O outro tipo de variável a ser comparado
   * @return true se os tipos de variável forem equivalentes, false caso contrário
   */
  public abstract boolean isEquivalent(VariableType other);

  /**
   * Método abstrato que deve ser implementado pelas subclasses para verificar se o tipo de variável é ordinal
   * @return true se o tipo de variável for ordinal, false caso contrário
   */
  public abstract boolean isOrdinal();
}