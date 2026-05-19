package typing;

import parser.PascalLexer;

public enum Pascaltype {
  INTEGER,
  REAL,
  STRING,
  BOOLEAN,
  CHAR;

  public static Pascaltype getVarType(int lexerTokenType) {
    switch (lexerTokenType) {
      case PascalLexer.INTEGER:
        return Pascaltype.INTEGER;
      case PascalLexer.REAL:
        return Pascaltype.REAL;
      case PascalLexer.STRING:
        return Pascaltype.STRING;
      case PascalLexer.BOOLEAN:
        return Pascaltype.BOOLEAN;
      case PascalLexer.CHAR:
        return Pascaltype.CHAR;
      default:
        System.err.println("ERROR: invalid lexer token type for variable declaration");
        System.exit(1);
        return null;
    }

  }

  @Override
  public String toString() {
    switch (this) {
      case INTEGER:
        return "INTEGER";
      case REAL:
        return "REAL";
      case STRING:
        return "STRING";
      case BOOLEAN:
        return "BOOLEAN";
      case CHAR:
        return "CHAR";
      default:
        System.err.println("ERROR: type enumaration has an invalid value");
        System.exit(1);
        return "";
    }
  }
}