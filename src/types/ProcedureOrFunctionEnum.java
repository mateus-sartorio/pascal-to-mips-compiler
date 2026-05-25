package types;

public enum ProcedureOrFunctionEnum {
  PROCEDURE,
  FUNCTION;

  @Override
  public String toString() {
    return name().toLowerCase();
  }
}