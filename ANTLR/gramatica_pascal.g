lexer grammar gramatica_pascal;

// 6.1.2. Special symbols
ATRIBUITION: ':=';
NOTEQUALTO: '<>';
LESSTHENEQTO: '<=';
GRETERTHENEQTO: '>=';
RANGE: '..';
ADD: '+';
SUB: '-';
EQUAL: '=';
VERSUS: '*';
DIVISOR: '/';
LESSTHEN: '<';
MORETHEN: '>';
OPBRACKET: '[';
CLBRACKET: ']';
DOT: '.';
COMMAN: ',';
COLON: ':';
SEMICOLON: ';';
OPPARENTHESES: '(';
CLPARENTHESES: ')';

// Word-symbols
AND: 'and';
ARRAY: 'array';
BEGIN: 'begin';
CASE: 'case';
CONST: 'const';
DIV: 'div';
DO: 'do';
DOWNTO: 'downto';
ELSE: 'else';
END: 'end';
FILE: 'file';
FOR: 'for';
FUNCTION: 'function';
GOTO: 'goto';
IF: 'if';
IN: 'in';
LABEL: 'label';
MOD: 'mod';
NIL: 'nil';
NOT: 'not';
OF: 'of';
OR: 'or';
PACKED: 'packed';
PROCEDURE: 'procedure';
PROGRAM: 'program';
RECORD: 'record';
REPEAT: 'repeat';
SET: 'set';
THEN: 'then';
TO: 'to';
TYPE: 'type';
UNTIL: 'until';
VAR: 'var';
WHILE: 'while';
WITH: 'with';

// 6.1.3. Identifiers 
ID: [a-zA-Z] [a-zA-Z0-9]*;

// 6.1.4. Directives 
DIRECTIVE: [a-zA-Z] [a-zA-Z0-9]*;

// 6.1.5. Numbers
fragment DIGIT: [0-9];
fragment DIGIT_SEQUENCE: DIGIT+;
fragment SIGN: [+-];
fragment SCALE_FACTOR: SIGN? DIGIT_SEQUENCE;
fragment FRACTIONAL_PART: DIGIT_SEQUENCE;
fragment UNSIGNED_INTEGER: DIGIT_SEQUENCE;
fragment UNSIGNED_REAL: (DIGIT_SEQUENCE '.' FRACTIONAL_PART ('e' SCALE_FACTOR)?) | (DIGIT_SEQUENCE 'e' SCALE_FACTOR);
fragment SIGNED_INTEGER: SIGN? UNSIGNED_INTEGER;
fragment SIGNED_REAL: SIGN? UNSIGNED_REAL;

UNSIGNED_NUMBER: UNSIGNED_INTEGER | UNSIGNED_REAL;
SIGNED_NUMBER: SIGNED_INTEGER | SIGNED_REAL;