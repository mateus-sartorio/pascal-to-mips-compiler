package types;

import parser.PascalLexer;

public enum PrimitiveType {
  INTEGER,
  REAL,
  STRING,
  BOOLEAN,
  CHAR;

  public static PrimitiveType getVarType(int lexerTokenType) throws Exception {
    switch (lexerTokenType) {
      case PascalLexer.INTEGER:
        return PrimitiveType.INTEGER;
      case PascalLexer.REAL:
        return PrimitiveType.REAL;
      case PascalLexer.STRING:
        return PrimitiveType.STRING;
      case PascalLexer.BOOLEAN:
        return PrimitiveType.BOOLEAN;
      case PascalLexer.CHAR:
        return PrimitiveType.CHAR;
      default:
        System.err.println("ERROR: invalid lexer token type for variable declaration.");
        throw new Exception();
    }
  }

  public static PrimitiveType getVarType(String type) {
    switch (type) {
      case "integer":
        return PrimitiveType.INTEGER;
      case "real":
        return PrimitiveType.REAL;
      case "string":
        return PrimitiveType.STRING;
      case "boolean":
        return PrimitiveType.BOOLEAN;
      case "char":
        return PrimitiveType.CHAR;
      default:
        System.err.println("ERROR: invalid lexer token type for variable declaration.");
        return PrimitiveType.CHAR;
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
        return "";
    }
  }
}