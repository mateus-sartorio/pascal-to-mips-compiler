lexer grammar PascalLexer;

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
LESS_THAN: '<';
GREATER_THAN: '>';
OPEN_BRACKET: '[' | '(.';
CLOSE_BRACKET: ']' | '.)';
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
DO: 'do';
DOWNTO: 'downto';
ELSE: 'else';
END: 'end';
FOR: 'for';
FUNCTION: 'function';
IF: 'if';
LABEL: 'label';
NIL: 'nil';
NOT: 'not';
OF: 'of';
OR: 'or';
PACKED: 'packed';
PROCEDURE: 'procedure';
PROGRAM: 'program';
THEN: 'then';
TO: 'to';
TYPE: 'type';
VAR: 'var';

// 6.1.4. Directives
// Not needed

// Types identifiers
INTEGER: 'integer';
REAL: 'real';
BOOLEAN: 'boolean';
CHAR: 'char';
STRING: 'string';

// 6.1.5. Numbers
fragment DIGIT: [0-9];
fragment DIGIT_SEQUENCE: DIGIT+;
fragment SCALE_FACTOR: SIGN? DIGIT_SEQUENCE;
fragment FRACTIONAL_PART: DIGIT_SEQUENCE;

SIGN: [+-];

UNSIGNED_INTEGER: DIGIT_SEQUENCE;

UNSIGNED_REAL: (DIGIT_SEQUENCE '.' FRACTIONAL_PART ('e' SCALE_FACTOR)?) | (DIGIT_SEQUENCE 'e' SCALE_FACTOR);

// 6.1.6 Labels
// Not used

// 6.1.7. String literals
fragment APOSTROPHE_IMAGE: '\'\'';
fragment STRING_CHARACTER: ~['\r\n];
fragment STRING_ELEMENT: APOSTROPHE_IMAGE | STRING_CHARACTER;
CHARACTER_STRING: '\'' STRING_ELEMENT+ '\'';

// 6.1.8 Token separators
COMMENTARY: (('{' .*? '}') | ('(*' .*? '*)')) -> skip;

// 6.1.3. Identifiers (Fora da ordem da ISO para não pegar componentes de outros tokens)
IDENTIFIER: [a-zA-Z][a-zA-Z0-9]*;

// 6.1.9 Lexical alternatives Was not implemented because we either don't support the reference token or the alternative token is not commonly used

// Ignore separator characters
WS: [ \t\r\n]+ -> skip;