lexer grammar gramatica_pascal;

// Palavras reservadas 6.1.2

AND         : 'and';
ARRAY       : 'array';
BEGIN       : 'begin';
CASE        : 'case';
CONST       : 'const';
DIV         : 'div';
DOWNTO      : 'downto';
ELSE        : 'else';
END         : 'end';
FILE        : 'file';
FOR         : 'for';
FUNCTION    : 'function';
GOTO        : 'goto';
IF          : 'if';
IN          : 'in';
LABEL       : 'label';
MOD         : 'mod';
NIL         : 'nil';
NOT         : 'not';
OF          : 'of';
OR          : 'or';
PACKED      : 'packed';
PROCEDURE   : 'procedure';
PROGRAM     : 'program';
RECORD      : 'record';
REPEAT      : 'repeat';
SET         : 'set';
THEN        : 'then';
TO          : 'to';
TYPE        : 'type';
UNTIL       : 'until';
VAR         : 'var';
WHILE       : 'while';
WITH        : 'with';

// Operadores 6.1.2
ATRIBUITION     : ':=';
NOTEQUALTO      : '<>';
LESSTHENEQTO    : '<=';
GRETERTHENEQTO  : '>=';
RANGE           : '..';
ADD             : '+';
SUB             : '-';
EQUAL           : '=';
VERSUS          : '*';
DIVISOR         : '/';
LESSTHEN        : '<';
MORETHEN        : '>';
OPBRACKET       : '[';
CLBRACKET       : ']';
DOT             : '.';
COMMAN          : ',';
COLON           : ':';
SEMICOLON       : ';';
OPPARENTHESES   : '(';
CLPARENTHESES   : ')';



// Identifiers 6.1.3
ID        : [a-zA-Z] [a-zA-Z0-9]*;

// Directives 6.1.4
DIRECTIVE : [a-zA-Z] [a-zA-Z0-9]*;

// NUMBERS
fragment DIGIT          : [0-9];
fragment DIGIT_SEQUENCE : DIGIT+;
fragment SIGN           : [+-];
fragment SCALE_FACTOR   : [SIGN]? DIGIT_SEQUENCE;
fragment FRACTIONAL_PART: DIGIT_SEQUENCE;
fragment UNSIGNED_ITEGER: DIGIT_SEQUENCE;
fragment UNSIGNED_REAL  : DIGIT_SEQUENCE '.' FRACTIONAL_PART ('e'SCALE_FACTOR)? | DIGIT_SEQUENCE 'e' SCALE_FACTOR;
UNSIGNED_NUMBER : UNSIGNED_ITEGER | UNSIGNED_REAL ;
SIGNED_INTEGER: SIGN? UNSIGNED_ITEGER;