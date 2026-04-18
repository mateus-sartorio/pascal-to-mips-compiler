parser grammar PascalParser;

options {
    tokenVocab = PascalLexer;
}

// Fix this because we did not define DIRECTIVE in the lexer.
directive : IDENTIFIER ;

// -------------------- 6.2 Blocks, scopes, and activations --------------------

block : label_declaration_part type_definition_part variable_declaration_part procedure_and_function_declaration_part statement_part ;

label_declaration_part : ( LABEL IDENTIFIER ( COMMA LABEL )* SEMICOLON )? ;
type_definition_part : ( TYPE type_definition COMMA ( type_definition SEMICOLON )* )? ;
variable_declaration_part : ( VAR variable_declaration COMMA ( variable_declaration SEMICOLON )* )? ;
procedure_and_function_declaration_part : ( ( procedure_declaration | function_declaration ) SEMICOLON )* ;

statement_part : compound_statement ;


// -------------------- 6.3 Constant-definitions --------------------

// Will not be implemented


// -------------------- 6.4 Type-definitions --------------------

type_definition : IDENTIFIER EQUAL_TO type_denoter ;
type_denoter : type_identifier | new_type ;

new_type : new_structured_type ;

simple_type_identifier : type_identifier ;
structured_type_identifier : type_identifier ;
pointer_type_identifier : type_identifier ;
type_identifier : IDENTIFIER ;

// 6.4.2 Simple-types
simple_type : ordinal_type | real_type_identifier ;
ordinal_type : ordinal_type_identifier ;
ordinal_type_identifier : type_identifier ;
real_type_identifier : type_identifier ;

// 6.4.2.3 Enumerated-types
identifier_list : IDENTIFIER ( COMMA IDENTIFIER )* ;
// The rest will not be implemented

// 6.4.2.4 Subrange-types
// Will not be implemented

// 6.4.3 Structured-types
structured_type : new_structured_type | structured_type_identifier ;
new_structured_type : ( PACKED )? unpacked_structured_type ;
unpacked_structured_type : array_type ;

// 6.4.3.2 Array-types
array_type : ARRAY OPEN_BRACKET index_type ( COMMA index_type )* CLOSE_BRACKET OF component_type ;
index_type : ordinal_type ;
component_type : type_denoter ;

// 6.4.3.3 Record-types
// Will not be implemented

// 6.4.3.4 Set-types
// Will not be implemented

// 6.4.3.5 File-types
// Will not be implemented

// 6.4.4 Pointer-types
// Will not be implemented


// -------------------- 6.5 Declarations and denotations of variables --------------------

// 6.5.1 Variable-declarations
variable_declaration : identifier_list COLON type_denoter ;

variable_access : entire_variable | component_variable ;

// 6.5.2 Entire-variables
entire_variable : variable_identifier ;
variable_identifier : IDENTIFIER ;

// 6.5.3 Component-variables
component_variable : indexed_variable ;

// 6.5.3.2 Indexed-variables
indexed_variable : array_variable OPEN_BRACKET ( index_expression  ( COMMA index_expression )* ) CLOSE_BRACKET ;
array_variable : variable_access ;
index_expression : expression ;

// 6.5.3.3 Field-designators
// Will not be implemented

// 6.5.5 Buffer-variables
// Will not be implemented


// -------------------- 6.6 Procedure and function declarations --------------------

// 6.6.1 Procedure-declarations

procedure_declaration : procedure_heading SEMICOLON directive
                        | procedure_identification SEMICOLON procedure_block
                        | procedure_heading SEMICOLON procedure_block ;

procedure_heading : PROCEDURE IDENTIFIER ( formal_parameter_list )? ;
procedure_identification : PROCEDURE procedure_identifier ;
procedure_identifier : IDENTIFIER ;
procedure_block : block ;

// 6.6.2 Function-declarations

function_declaration : function_heading SEMICOLON directive
                        | function_identification SEMICOLON function_block
                        | function_heading SEMICOLON function_block ;

function_heading : FUNCTION IDENTIFIER ( formal_parameter_list )? COLON result_type ;
function_identification : FUNCTION function_identifier ;
function_identifier : IDENTIFIER ;

result_type : simple_type_identifier;
function_block : block ;

// 6.6.3 Parameters

// 6.6.3.1 General

formal_parameter_list : OPEN_BRACKET formal_parameter_section ( SEMICOLON formal_parameter_section )* CLOSE_BRACKET ;
formal_parameter_section : value_parameter_speficiation
                        | variable_parameter_specification
                        | procedural_parameter_specification
                        | functional_parameter_specification ;

value_parameter_speficiation : identifier_list COLON type_identifier ;
variable_parameter_specification : VAR identifier_list COLON type_identifier ;
procedural_parameter_specification : procedure_heading ;
functional_parameter_specification : function_heading ;

// 6.6.3.7 Conformant array parameters
// Will not be implemented

// -------------------- 6.7 Expressions --------------------

// 6.7.1 General

expression : simple_expression ( relational_operator simple_expression )? ;
simple_expression : ( SIGN )? term ( adding_operator term )* ;
term : factor ( multiplying_operator factor )* ;
    
factor : variable_access
        | unsigned_constant
        | function_designator
        | OPEN_PARENTHESIS expression CLOSE_PARENTHESIS
        | NOT factor ;

unsigned_constant : UNSIGNED_INTEGER | UNSIGNED_REAL | CHARACTER_STRING | NIL;
member_designator : expression | ( RANGE expression )? ;

// 6.7.2 Operators

// 6.7.2.1 General

multiplying_operator : MULTIPLICATION | AND ;
adding_operator : ADDITION | SUBTRACTION | OR ;
relational_operator : EQUAL_TO | NOT_EQUAL_TO | LESS_THAN | LESS_THAN_OR_EQUAL_TO | GREATER_THAN | GREATER_THAN_OR_EQUAL_TO ;

// 6.7.2.3 Boolean operators
boolean_expression : expression ;

// 6.7.3 Function-designators
function_designator : function_identifier ( actual_parameter_list )? ;
actual_parameter_list : OPEN_PARENTHESIS actual_parameter ( COMMA actual_parameter )* CLOSE_PARENTHESIS ;
actual_parameter : expression | variable_access | procedure_identifier | function_identifier ;


// -------------------- 6.8 Statements --------------------

// 6.8.1 General

statement : ( IDENTIFIER COLON )? ( simple_statement | structured_statement ) ;

// 6.8.2 Simple-statements

// 6.8.2.1 General

simple_statement :  empty_statement
                  | assignment_statement
                  | procedure_statement
                 ;

empty_statement : /* empty */ ;

// 6.8.2.2 Assignment-statements
assignment_statement : variable_access ASSIGNMENT expression ;

// 6.8.2.3 Procedure-statements

procedure_statement : procedure_identifier ( actual_parameter_list )? ;

// 6.8.2.4 Goto-statements
// Will not be implemented

// 6.8.3 Structured-statements

// 6.8.3.1 General

structured_statement : compound_statement
                     | conditional_statement
                     | repetitive_statement
                     ;

statement_sequence : statement ( SEMICOLON statement )* ;

// 6.8.3.2 Compound-statements
compound_statement : BEGIN statement_sequence END ;

// 6.8.3.3 Conditional-statements
conditional_statement : if_statement ;

// 6 .8 .3 .4 If-statements
if_statement : IF boolean_expression THEN statement ( else_part )? ;
else_part : ELSE statement ;

// 6.8.3.5 Case-statements
// Will not be implemented

// 6.8.3.6 Repetitive-statements
repetitive_statement : for_statement ;

// 6.8.3.7 Repeat-statements
// Will not be implemented

// 6.8.3.8 While-statements
// Will not be implemented

// 6.8.3.9 For-statements
for_statement : FOR control_variable ASSIGNMENT initial_value ( TO | DOWNTO ) final_value DO statement ;
control_variable : entire_variable ;
initial_value : expression ;
final_value : expression ;

// 6.8.3.10 With-statements
// Will not be implemented


// -------------------- 6.9 Input and output --------------------

// Will not be implemented


// -------------------- 6.10 Programs --------------------

program : program_heading SEMICOLON program_block PERIOD ;
program_heading : PROGRAM IDENTIFIER ( OPEN_BRACKET program_parameter_list CLOSE_BRACKET )? ;
program_parameter_list : identifier_list ;
program_block : block ;