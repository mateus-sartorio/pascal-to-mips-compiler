parser grammar PascalParser;

options {
	tokenVocab = PascalLexer;
}

// -------------------- Blocks and declaration parts --------------------
block: variable_declaration_part procedure_and_function_declaration_part compound_statement ;

variable_declaration_part: ( VAR variable_declaration ( SEMICOLON variable_declaration )* SEMICOLON )* ;
procedure_and_function_declaration_part: ( ( procedure_declaration | function_declaration ) SEMICOLON )* ;


// -------------------- Types --------------------
primitive_type: INTEGER | REAL | BOOLEAN | CHAR | STRING ;
type_denoter: primitive_type | array_type ;

identifier_list: IDENTIFIER ( COMMA IDENTIFIER )* ;
subrange_type: UNSIGNED_INTEGER RANGE UNSIGNED_INTEGER ;
array_type: ( PACKED )? ARRAY OPEN_BRACKET subrange_type CLOSE_BRACKET OF primitive_type ;


// -------------------- Variables --------------------
variable_declaration: identifier_list COLON type_denoter ;
variable_access: IDENTIFIER | indexed_variable ;
indexed_variable: IDENTIFIER OPEN_BRACKET ( expression ) CLOSE_BRACKET ;


// -------------------- Procedures and functions --------------------
procedure_declaration: procedure_heading compound_statement ;
procedure_heading: PROCEDURE IDENTIFIER ( formal_parameter_list )? SEMICOLON variable_declaration_part ;

function_declaration: function_heading compound_statement ;
function_heading: FUNCTION IDENTIFIER ( formal_parameter_list )? COLON type_denoter SEMICOLON variable_declaration_part ;

formal_parameter_list: OPEN_PARENTHESIS ( value_parameter_speficiation ( SEMICOLON value_parameter_speficiation )* )? CLOSE_PARENTHESIS ;
value_parameter_speficiation: identifier_list COLON type_denoter ;


// -------------------- Expressions --------------------
expression: simple_expression ( relational_operator simple_expression )? ;
simple_expression: term ( adding_operator term )* ;
term: factor ( multiplying_operator factor )* ;

factor:
	variable_access                                     # VariableAccess
  | CHARACTER_STRING                                  # StringConstant
	| numeric_constant                                  # NumericConstant
  | boolean_constant                                  # BooleanConstant
  | function_designator                               # FunctionCall
  | OPEN_PARENTHESIS expression CLOSE_PARENTHESIS     # ParenthesisExpression
  | NOT factor                                        # NotFactor
  ;


// -------------------- Constants --------------------
numeric_constant: ( PLUS | MINUS )? ( UNSIGNED_INTEGER | UNSIGNED_REAL ) ;
boolean_constant: TRUE | FALSE ;


// -------------------- Operators --------------------
multiplying_operator: MULTIPLICATION | DIVISION | DIV | AND ;
adding_operator: PLUS | MINUS | OR ;
relational_operator:
	EQUAL_TO
	| NOT_EQUAL_TO
	| LESS_THAN
	| LESS_THAN_OR_EQUAL_TO
	| GREATER_THAN
	| GREATER_THAN_OR_EQUAL_TO
  ;


// -------------------- Function calls --------------------
function_designator: IDENTIFIER ( actual_parameter_list )? ;
actual_parameter_list: OPEN_PARENTHESIS ( actual_parameter ( COMMA actual_parameter )* )? CLOSE_PARENTHESIS ;
actual_parameter: expression | variable_access ;


// -------------------- Statements --------------------
statement: simple_statement | structured_statement ;
simple_statement:
	empty_statement
	| assignment_statement
	| procedure_statement
  | exit_statement
  ;

empty_statement: /* empty */;
exit_statement : EXIT ;
assignment_statement: variable_access ASSIGNMENT expression ;
procedure_statement: IDENTIFIER ( actual_parameter_list )? ;


// -------------------- Structured statements --------------------
structured_statement:
  compound_statement
	| if_statement
	| for_statement
  ;

statement_sequence: statement ( SEMICOLON statement )* ;
compound_statement: BEGIN statement_sequence END ;
if_statement: IF expression THEN statement ( else_part )? ;
else_part: ELSE statement ;
for_statement: FOR IDENTIFIER ASSIGNMENT expression ( TO | DOWNTO ) expression DO statement ;


// -------------------- Program --------------------
program: program_heading SEMICOLON block PERIOD ;
program_heading: PROGRAM IDENTIFIER ;