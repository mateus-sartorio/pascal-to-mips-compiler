package types;

/**
 * Classe que representa um tipo de variável primitiva, que pode ser um tipo primitivo básico (como INTEGER, CHAR, BOOLEAN) e pode ser indexado (array).
 */
public class PrimitiveVariableType extends VariableType {
  public PrimitiveVariableType(PrimitiveTypeEnum basePrimitiveType) {
    super(basePrimitiveType, false);
  }

  /**
   * Construtor que cria um tipo de variável primitiva com base no tipo primitivo fornecido e se é indexado.
   * @param basePrimitiveType O tipo primitivo base.
   * @param isIndexed Indica se a variável é indexada (array).
   */
  public PrimitiveVariableType(PrimitiveTypeEnum basePrimitiveType, boolean isIndexed) {
    super(basePrimitiveType, isIndexed);
  }

  /**
   * Verifica se este tipo de variável é equivalente a outro tipo de variável.
   * @param other O outro tipo de variável a ser comparado.
   * @return true se os tipos forem equivalentes, false caso contrário.
   */
  @Override
  public boolean isEquivalent(VariableType other) {
    if(!(other instanceof PrimitiveVariableType)) {
      return false;
    }

    PrimitiveTypeEnum resultType = TypeRules.getResultType(
      TypeRules.ASSIGNMENT_TABLE,
      other.basePrimitiveType,
      this.basePrimitiveType
    );

    return resultType != PrimitiveTypeEnum.NO_TYPE;
  }

  /**
   * Retorna o tipo primitivo base deste tipo de variável.
   * @return O tipo primitivo base.
   */
  @Override
  public String toString() {
    return basePrimitiveType.toString();
  }

  /**
   * Verifica se o tipo primitivo é ordinal (INTEGER, CHAR ou BOOLEAN).
   * @return true se o tipo primitivo for ordinal, false caso contrário.
   */
  @Override
  public boolean isOrdinal() {
    return (
      basePrimitiveType == PrimitiveTypeEnum.INTEGER ||
      basePrimitiveType == PrimitiveTypeEnum.CHAR ||
      basePrimitiveType == PrimitiveTypeEnum.BOOLEAN
    );
  }
}