lexer grammar pascal_grammar;

// -------------------- 6.1. Lexical Tokens --------------------

// 6.1.2. Special symbols
ASSIGNMENT: ':=';
NOT_EQUAL_TO: '<>';
LESS_THAN_OR_EQUAL_TO: '<=';
GREATER_THAN_OR_EQUAL_TO: '>=';
RANGE: '..';
ADDITION: '+';
SUBTRACTION: '-';
EQUAL_TO: '=';
MULTIPLICATION: '*';
DIVISION: '/';
LESS_THAN: '<';
GREATER_THAN: '>';
OPEN_BRACKET: '[';
CLOSE_BRACKET: ']';
PERIOD: '.';
COMMA: ',';
COLON: ':';
SEMICOLON: ';';
OPEN_PARENTHESIS: '(';
CLOSE_PARENTHESIS: ')';

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
ID: [a-zA-Z][a-zA-Z0-9]*;

// 6.1.4. Directives 
DIRECTIVE: [a-zA-Z][a-zA-Z0-9]*;

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

// 6.1.6 Labels
LABEL_TOKEN: DIGIT_SEQUENCE;

// 6.1.7. String literals
fragment APOSTROPHE_IMAGE : '\'\'';
fragment STRING_CHARACTER: ~['\r\n];
fragment STRING_ELEMENT: APOSTROPHE_IMAGE | STRING_CHARACTER;
CHARACTER_STRING : '\'' STRING_ELEMENT+ '\'';

// 6.1.8 Token separators
WHITESPACE: [ \t\r\n\f]+ -> skip;

// 6.1.9 Lexical alternatives
// Was not implemented because we either don't support the reference token or the alternative token is not commonly used

// -------------------- 6.2. Lexical Tokens --------------------

// TODO: Para ser continuado (ou não, não sei se o 6.2 entra nessa atividade)