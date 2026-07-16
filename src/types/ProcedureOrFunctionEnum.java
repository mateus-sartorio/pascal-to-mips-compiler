package types;

/**
 * Enumeração que representa os tipos de subprogramas: PROCEDURE e FUNCTION.
 */
public enum ProcedureOrFunctionEnum {
  PROCEDURE,
  FUNCTION;

  @Override
  public String toString() {
    return name().toLowerCase();
  }
}