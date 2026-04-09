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

// 6.1.4. Directives (Retirei elas por enquanto, como elas são exatamente iguais a identificadores,
// elas acabam pegando tokens que deveriam ser identificadores. Isso parece ser melhor na parte do
// Parser, não temos contexto para isso no lexer) DIRECTIVE: [a-zA-Z][a-zA-Z0-9]*;

// 6.1.5. Numbers
fragment DIGIT: [0-9];
fragment DIGIT_SEQUENCE: DIGIT+;
fragment SIGN: [+-];
fragment SCALE_FACTOR: SIGN? DIGIT_SEQUENCE;
fragment FRACTIONAL_PART: DIGIT_SEQUENCE;
fragment UNSIGNED_INTEGER: DIGIT_SEQUENCE;
fragment UNSIGNED_REAL: (
		DIGIT_SEQUENCE '.' FRACTIONAL_PART ('e' SCALE_FACTOR)?
	)
	| (DIGIT_SEQUENCE 'e' SCALE_FACTOR);
fragment SIGNED_INTEGER: SIGN? UNSIGNED_INTEGER;
fragment SIGNED_REAL: SIGN? UNSIGNED_REAL;

// Number Types
UNSIGNED_INTEGER_TYPE: UNSIGNED_INTEGER;
SIGNED_INTEGER_TYPE: SIGNED_INTEGER;
UNSIGNED_REAL_TYPE: UNSIGNED_REAL;
SIGNED_REAL_TYPE: SIGNED_REAL;

// 6.1.6 Labels
LABEL_TOKEN: DIGIT_SEQUENCE;

// 6.1.7. String literals
fragment APOSTROPHE_IMAGE: '\'\'';
fragment STRING_CHARACTER: ~['\r\n];
fragment STRING_ELEMENT: APOSTROPHE_IMAGE | STRING_CHARACTER;
CHARACTER_STRING: '\'' STRING_ELEMENT+ '\'';

STRING_TYPE: CHARACTER_STRING;

// 6.1.8 Token separators
COMMENTARY: (('{' .*? '}') | ('(*' .*? '*)')) -> skip;

// 6.1.3. Identifiers (Fora da ordem da ISO para não pegar componentes de outros tokens)
ID: [a-zA-Z][a-zA-Z0-9]*;

// 6.1.9 Lexical alternatives Was not implemented because we either don't support the reference
// token or the alternative token is not commonly used

// Ignore separator characters
WS: [ \t\r\n]+ -> skip;