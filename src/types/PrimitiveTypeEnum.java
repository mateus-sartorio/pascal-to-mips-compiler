package types;

public enum PrimitiveTypeEnum {
  INTEGER,
  REAL,
  STRING,
  BOOLEAN,
  CHAR,
  NO_TYPE;

  public static PrimitiveTypeEnum getType(String typeDenoter) {
    switch (typeDenoter.toLowerCase()) {
      case "integer":
        return PrimitiveTypeEnum.INTEGER;
      case "real":
        return PrimitiveTypeEnum.REAL;
      case "string":
        return PrimitiveTypeEnum.STRING;
      case "boolean":
        return PrimitiveTypeEnum.BOOLEAN;
      case "char":
        return PrimitiveTypeEnum.CHAR;
      default:
        System.err.println("ERROR: invalid lexer token type for variable declaration.");
        System.exit(1);
        return PrimitiveTypeEnum.NO_TYPE;
    }
  }

  @Override
  public String toString() {
    switch (this) {
      case INTEGER:
        return "integer";
      case REAL:
        return "real";
      case STRING:
        return "string";
      case BOOLEAN:
        return "boolean";
      case CHAR:
        return "char";
      default:
        System.err.println("ERROR: type enumaration has an invalid value.");
        System.exit(1);
        return "";
    }
  }
}