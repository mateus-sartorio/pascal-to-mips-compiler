package types;

public class TypeRules {
  /**
   * Resolves types for the Addition (+) operator.
   * Pascal allows widening (Integer + Real = Real).
   */
  public static final PrimitiveTypeEnum[][] PLUS_TABLE = {
    // RHS:             INTEGER                    REAL                       STRING                     BOOLEAN                    CHAR                       NO_TYPE
    /* LHS INTEGER */ { PrimitiveTypeEnum.INTEGER, PrimitiveTypeEnum.REAL,    PrimitiveTypeEnum.NO_TYPE, PrimitiveTypeEnum.NO_TYPE, PrimitiveTypeEnum.NO_TYPE, PrimitiveTypeEnum.NO_TYPE },
    /* LHS REAL    */ { PrimitiveTypeEnum.REAL,    PrimitiveTypeEnum.REAL,    PrimitiveTypeEnum.NO_TYPE, PrimitiveTypeEnum.NO_TYPE, PrimitiveTypeEnum.NO_TYPE, PrimitiveTypeEnum.NO_TYPE },
    /* LHS STRING  */ { PrimitiveTypeEnum.NO_TYPE, PrimitiveTypeEnum.NO_TYPE, PrimitiveTypeEnum.STRING,  PrimitiveTypeEnum.NO_TYPE, PrimitiveTypeEnum.STRING,  PrimitiveTypeEnum.NO_TYPE },
    /* LHS BOOLEAN */ { PrimitiveTypeEnum.NO_TYPE, PrimitiveTypeEnum.NO_TYPE, PrimitiveTypeEnum.NO_TYPE, PrimitiveTypeEnum.NO_TYPE, PrimitiveTypeEnum.NO_TYPE, PrimitiveTypeEnum.NO_TYPE },
    /* LHS CHAR    */ { PrimitiveTypeEnum.NO_TYPE, PrimitiveTypeEnum.NO_TYPE, PrimitiveTypeEnum.STRING,  PrimitiveTypeEnum.NO_TYPE, PrimitiveTypeEnum.STRING,  PrimitiveTypeEnum.NO_TYPE },
    /* LHS NO_TYPE */ { PrimitiveTypeEnum.NO_TYPE, PrimitiveTypeEnum.NO_TYPE, PrimitiveTypeEnum.NO_TYPE, PrimitiveTypeEnum.NO_TYPE, PrimitiveTypeEnum.NO_TYPE, PrimitiveTypeEnum.NO_TYPE }
  };

  /**
   * Resolves types for Subtraction (-) and Multiplication (*).
   */
  public static final PrimitiveTypeEnum[][] MATH_TABLE = {
    // RHS:             INTEGER                    REAL                       STRING                     BOOLEAN                    CHAR                       NO_TYPE
    /* LHS INTEGER */ { PrimitiveTypeEnum.INTEGER, PrimitiveTypeEnum.REAL,    PrimitiveTypeEnum.NO_TYPE, PrimitiveTypeEnum.NO_TYPE, PrimitiveTypeEnum.NO_TYPE, PrimitiveTypeEnum.NO_TYPE },
    /* LHS REAL    */ { PrimitiveTypeEnum.REAL,    PrimitiveTypeEnum.REAL,    PrimitiveTypeEnum.NO_TYPE, PrimitiveTypeEnum.NO_TYPE, PrimitiveTypeEnum.NO_TYPE, PrimitiveTypeEnum.NO_TYPE },
    /* LHS STRING  */ { PrimitiveTypeEnum.NO_TYPE, PrimitiveTypeEnum.NO_TYPE, PrimitiveTypeEnum.NO_TYPE, PrimitiveTypeEnum.NO_TYPE, PrimitiveTypeEnum.NO_TYPE, PrimitiveTypeEnum.NO_TYPE },
    /* LHS BOOLEAN */ { PrimitiveTypeEnum.NO_TYPE, PrimitiveTypeEnum.NO_TYPE, PrimitiveTypeEnum.NO_TYPE, PrimitiveTypeEnum.NO_TYPE, PrimitiveTypeEnum.NO_TYPE, PrimitiveTypeEnum.NO_TYPE },
    /* LHS CHAR    */ { PrimitiveTypeEnum.NO_TYPE, PrimitiveTypeEnum.NO_TYPE, PrimitiveTypeEnum.NO_TYPE, PrimitiveTypeEnum.NO_TYPE, PrimitiveTypeEnum.NO_TYPE, PrimitiveTypeEnum.NO_TYPE },
    /* LHS NO_TYPE */ { PrimitiveTypeEnum.NO_TYPE, PrimitiveTypeEnum.NO_TYPE, PrimitiveTypeEnum.NO_TYPE, PrimitiveTypeEnum.NO_TYPE, PrimitiveTypeEnum.NO_TYPE, PrimitiveTypeEnum.NO_TYPE }
  };

  /**
   * Resolves types for Real Division (/).
   * In Pascal, standard division ALWAYS results in a REAL.
   */
  public static final PrimitiveTypeEnum[][] REAL_DIVISION_TABLE = {
    // RHS:             INTEGER                    REAL                       STRING                     BOOLEAN                    CHAR                       NO_TYPE
    /* LHS INTEGER */ { PrimitiveTypeEnum.REAL,    PrimitiveTypeEnum.REAL,    PrimitiveTypeEnum.NO_TYPE, PrimitiveTypeEnum.NO_TYPE, PrimitiveTypeEnum.NO_TYPE, PrimitiveTypeEnum.NO_TYPE },
    /* LHS REAL    */ { PrimitiveTypeEnum.REAL,    PrimitiveTypeEnum.REAL,    PrimitiveTypeEnum.NO_TYPE, PrimitiveTypeEnum.NO_TYPE, PrimitiveTypeEnum.NO_TYPE, PrimitiveTypeEnum.NO_TYPE },
    /* LHS STRING  */ { PrimitiveTypeEnum.NO_TYPE, PrimitiveTypeEnum.NO_TYPE, PrimitiveTypeEnum.NO_TYPE, PrimitiveTypeEnum.NO_TYPE, PrimitiveTypeEnum.NO_TYPE, PrimitiveTypeEnum.NO_TYPE },
    /* LHS BOOLEAN */ { PrimitiveTypeEnum.NO_TYPE, PrimitiveTypeEnum.NO_TYPE, PrimitiveTypeEnum.NO_TYPE, PrimitiveTypeEnum.NO_TYPE, PrimitiveTypeEnum.NO_TYPE, PrimitiveTypeEnum.NO_TYPE },
    /* LHS CHAR    */ { PrimitiveTypeEnum.NO_TYPE, PrimitiveTypeEnum.NO_TYPE, PrimitiveTypeEnum.NO_TYPE, PrimitiveTypeEnum.NO_TYPE, PrimitiveTypeEnum.NO_TYPE, PrimitiveTypeEnum.NO_TYPE },
    /* LHS NO_TYPE */ { PrimitiveTypeEnum.NO_TYPE, PrimitiveTypeEnum.NO_TYPE, PrimitiveTypeEnum.NO_TYPE, PrimitiveTypeEnum.NO_TYPE, PrimitiveTypeEnum.NO_TYPE, PrimitiveTypeEnum.NO_TYPE }
  };

  /**
   * Resolves types for Relational/Comparison operators (=, <>, <, >, <=, >=).
   */
  public static final PrimitiveTypeEnum[][] RELATIONAL_TABLE = {
    // RHS:             INTEGER                    REAL                       STRING                     BOOLEAN                    CHAR                       NO_TYPE
    /* LHS INTEGER */ { PrimitiveTypeEnum.BOOLEAN, PrimitiveTypeEnum.BOOLEAN, PrimitiveTypeEnum.NO_TYPE, PrimitiveTypeEnum.NO_TYPE, PrimitiveTypeEnum.NO_TYPE, PrimitiveTypeEnum.NO_TYPE },
    /* LHS REAL    */ { PrimitiveTypeEnum.BOOLEAN, PrimitiveTypeEnum.BOOLEAN, PrimitiveTypeEnum.NO_TYPE, PrimitiveTypeEnum.NO_TYPE, PrimitiveTypeEnum.NO_TYPE, PrimitiveTypeEnum.NO_TYPE },
    /* LHS STRING  */ { PrimitiveTypeEnum.NO_TYPE, PrimitiveTypeEnum.NO_TYPE, PrimitiveTypeEnum.BOOLEAN, PrimitiveTypeEnum.NO_TYPE, PrimitiveTypeEnum.NO_TYPE, PrimitiveTypeEnum.NO_TYPE },
    /* LHS BOOLEAN */ { PrimitiveTypeEnum.NO_TYPE, PrimitiveTypeEnum.NO_TYPE, PrimitiveTypeEnum.NO_TYPE, PrimitiveTypeEnum.BOOLEAN, PrimitiveTypeEnum.NO_TYPE, PrimitiveTypeEnum.NO_TYPE },
    /* LHS CHAR    */ { PrimitiveTypeEnum.NO_TYPE, PrimitiveTypeEnum.NO_TYPE, PrimitiveTypeEnum.NO_TYPE, PrimitiveTypeEnum.NO_TYPE, PrimitiveTypeEnum.BOOLEAN, PrimitiveTypeEnum.NO_TYPE },
    /* LHS NO_TYPE */ { PrimitiveTypeEnum.NO_TYPE, PrimitiveTypeEnum.NO_TYPE, PrimitiveTypeEnum.NO_TYPE, PrimitiveTypeEnum.NO_TYPE, PrimitiveTypeEnum.NO_TYPE, PrimitiveTypeEnum.NO_TYPE }
  };

  /**
   * Resolves validity for logical operators (AND, OR).
   */
  public static final PrimitiveTypeEnum[][] LOGICAL_TABLE = {
    // RHS:             INTEGER                    REAL                       STRING                     BOOLEAN                    CHAR                       NO_TYPE
    /* LHS INTEGER */ { PrimitiveTypeEnum.NO_TYPE, PrimitiveTypeEnum.NO_TYPE, PrimitiveTypeEnum.NO_TYPE, PrimitiveTypeEnum.NO_TYPE, PrimitiveTypeEnum.NO_TYPE, PrimitiveTypeEnum.NO_TYPE },
    /* LHS REAL    */ { PrimitiveTypeEnum.NO_TYPE, PrimitiveTypeEnum.NO_TYPE, PrimitiveTypeEnum.NO_TYPE, PrimitiveTypeEnum.NO_TYPE, PrimitiveTypeEnum.NO_TYPE, PrimitiveTypeEnum.NO_TYPE },
    /* LHS STRING  */ { PrimitiveTypeEnum.NO_TYPE, PrimitiveTypeEnum.NO_TYPE, PrimitiveTypeEnum.NO_TYPE, PrimitiveTypeEnum.NO_TYPE, PrimitiveTypeEnum.NO_TYPE, PrimitiveTypeEnum.NO_TYPE },
    /* LHS BOOLEAN */ { PrimitiveTypeEnum.NO_TYPE, PrimitiveTypeEnum.NO_TYPE, PrimitiveTypeEnum.NO_TYPE, PrimitiveTypeEnum.BOOLEAN, PrimitiveTypeEnum.NO_TYPE, PrimitiveTypeEnum.NO_TYPE },
    /* LHS CHAR    */ { PrimitiveTypeEnum.NO_TYPE, PrimitiveTypeEnum.NO_TYPE, PrimitiveTypeEnum.NO_TYPE, PrimitiveTypeEnum.NO_TYPE, PrimitiveTypeEnum.NO_TYPE, PrimitiveTypeEnum.NO_TYPE },
    /* LHS NO_TYPE */ { PrimitiveTypeEnum.NO_TYPE, PrimitiveTypeEnum.NO_TYPE, PrimitiveTypeEnum.NO_TYPE, PrimitiveTypeEnum.NO_TYPE, PrimitiveTypeEnum.NO_TYPE, PrimitiveTypeEnum.NO_TYPE }
  };

  /**
   * Resolves validity for the Assignment (:=) operator.
   */
  public static final PrimitiveTypeEnum[][] ASSIGNMENT_TABLE = {
    // RHS:             INTEGER                    REAL                       STRING                     BOOLEAN                    CHAR                       NO_TYPE
    /* LHS INTEGER */ { PrimitiveTypeEnum.INTEGER, PrimitiveTypeEnum.NO_TYPE, PrimitiveTypeEnum.NO_TYPE, PrimitiveTypeEnum.NO_TYPE, PrimitiveTypeEnum.NO_TYPE, PrimitiveTypeEnum.NO_TYPE },
    /* LHS REAL    */ { PrimitiveTypeEnum.REAL,    PrimitiveTypeEnum.REAL,    PrimitiveTypeEnum.NO_TYPE, PrimitiveTypeEnum.NO_TYPE, PrimitiveTypeEnum.NO_TYPE, PrimitiveTypeEnum.NO_TYPE },
    /* LHS STRING  */ { PrimitiveTypeEnum.NO_TYPE, PrimitiveTypeEnum.NO_TYPE, PrimitiveTypeEnum.STRING,  PrimitiveTypeEnum.NO_TYPE, PrimitiveTypeEnum.STRING,  PrimitiveTypeEnum.NO_TYPE },
    /* LHS BOOLEAN */ { PrimitiveTypeEnum.NO_TYPE, PrimitiveTypeEnum.NO_TYPE, PrimitiveTypeEnum.NO_TYPE, PrimitiveTypeEnum.BOOLEAN, PrimitiveTypeEnum.NO_TYPE, PrimitiveTypeEnum.NO_TYPE },
    /* LHS CHAR    */ { PrimitiveTypeEnum.NO_TYPE, PrimitiveTypeEnum.NO_TYPE, PrimitiveTypeEnum.NO_TYPE, PrimitiveTypeEnum.NO_TYPE, PrimitiveTypeEnum.CHAR,    PrimitiveTypeEnum.NO_TYPE },
    /* LHS NO_TYPE */ { PrimitiveTypeEnum.NO_TYPE, PrimitiveTypeEnum.NO_TYPE, PrimitiveTypeEnum.NO_TYPE, PrimitiveTypeEnum.NO_TYPE, PrimitiveTypeEnum.NO_TYPE, PrimitiveTypeEnum.NO_TYPE }
  };

  /**
   * Helper method to look up types. 
   */
  public static PrimitiveTypeEnum getResultType(PrimitiveTypeEnum[][] table, PrimitiveTypeEnum lhs, PrimitiveTypeEnum rhs) {
    return table[lhs.ordinal()][rhs.ordinal()];
  }
}