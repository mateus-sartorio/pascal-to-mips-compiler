// Generated from src/PascalParser.g by ANTLR 4.13.2

package parser;

import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link PascalParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface PascalParserVisitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by {@link PascalParser#block}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBlock(PascalParser.BlockContext ctx);
	/**
	 * Visit a parse tree produced by {@link PascalParser#type_definition_part}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitType_definition_part(PascalParser.Type_definition_partContext ctx);
	/**
	 * Visit a parse tree produced by {@link PascalParser#variable_declaration_part}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVariable_declaration_part(PascalParser.Variable_declaration_partContext ctx);
	/**
	 * Visit a parse tree produced by {@link PascalParser#procedure_and_function_declaration_part}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitProcedure_and_function_declaration_part(PascalParser.Procedure_and_function_declaration_partContext ctx);
	/**
	 * Visit a parse tree produced by {@link PascalParser#constant_deifinition}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitConstant_deifinition(PascalParser.Constant_deifinitionContext ctx);
	/**
	 * Visit a parse tree produced by {@link PascalParser#constant}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitConstant(PascalParser.ConstantContext ctx);
	/**
	 * Visit a parse tree produced by {@link PascalParser#primitive_type}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPrimitive_type(PascalParser.Primitive_typeContext ctx);
	/**
	 * Visit a parse tree produced by {@link PascalParser#type_definition}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitType_definition(PascalParser.Type_definitionContext ctx);
	/**
	 * Visit a parse tree produced by {@link PascalParser#type_denoter}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitType_denoter(PascalParser.Type_denoterContext ctx);
	/**
	 * Visit a parse tree produced by {@link PascalParser#identifier_list}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIdentifier_list(PascalParser.Identifier_listContext ctx);
	/**
	 * Visit a parse tree produced by {@link PascalParser#subrange_type}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSubrange_type(PascalParser.Subrange_typeContext ctx);
	/**
	 * Visit a parse tree produced by {@link PascalParser#array_type}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitArray_type(PascalParser.Array_typeContext ctx);
	/**
	 * Visit a parse tree produced by {@link PascalParser#variable_declaration}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVariable_declaration(PascalParser.Variable_declarationContext ctx);
	/**
	 * Visit a parse tree produced by {@link PascalParser#variable_access}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVariable_access(PascalParser.Variable_accessContext ctx);
	/**
	 * Visit a parse tree produced by {@link PascalParser#indexed_variable}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIndexed_variable(PascalParser.Indexed_variableContext ctx);
	/**
	 * Visit a parse tree produced by {@link PascalParser#procedure_declaration}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitProcedure_declaration(PascalParser.Procedure_declarationContext ctx);
	/**
	 * Visit a parse tree produced by {@link PascalParser#procedure_heading}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitProcedure_heading(PascalParser.Procedure_headingContext ctx);
	/**
	 * Visit a parse tree produced by {@link PascalParser#function_declaration}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFunction_declaration(PascalParser.Function_declarationContext ctx);
	/**
	 * Visit a parse tree produced by {@link PascalParser#function_heading}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFunction_heading(PascalParser.Function_headingContext ctx);
	/**
	 * Visit a parse tree produced by {@link PascalParser#formal_parameter_list}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFormal_parameter_list(PascalParser.Formal_parameter_listContext ctx);
	/**
	 * Visit a parse tree produced by {@link PascalParser#formal_parameter_section}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFormal_parameter_section(PascalParser.Formal_parameter_sectionContext ctx);
	/**
	 * Visit a parse tree produced by {@link PascalParser#value_parameter_speficiation}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitValue_parameter_speficiation(PascalParser.Value_parameter_speficiationContext ctx);
	/**
	 * Visit a parse tree produced by {@link PascalParser#variable_parameter_specification}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVariable_parameter_specification(PascalParser.Variable_parameter_specificationContext ctx);
	/**
	 * Visit a parse tree produced by {@link PascalParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpression(PascalParser.ExpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link PascalParser#simple_expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSimple_expression(PascalParser.Simple_expressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link PascalParser#term}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTerm(PascalParser.TermContext ctx);
	/**
	 * Visit a parse tree produced by {@link PascalParser#factor}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFactor(PascalParser.FactorContext ctx);
	/**
	 * Visit a parse tree produced by {@link PascalParser#unsigned_constant}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUnsigned_constant(PascalParser.Unsigned_constantContext ctx);
	/**
	 * Visit a parse tree produced by {@link PascalParser#multiplying_operator}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMultiplying_operator(PascalParser.Multiplying_operatorContext ctx);
	/**
	 * Visit a parse tree produced by {@link PascalParser#adding_operator}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAdding_operator(PascalParser.Adding_operatorContext ctx);
	/**
	 * Visit a parse tree produced by {@link PascalParser#relational_operator}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRelational_operator(PascalParser.Relational_operatorContext ctx);
	/**
	 * Visit a parse tree produced by {@link PascalParser#function_designator}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFunction_designator(PascalParser.Function_designatorContext ctx);
	/**
	 * Visit a parse tree produced by {@link PascalParser#actual_parameter_list}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitActual_parameter_list(PascalParser.Actual_parameter_listContext ctx);
	/**
	 * Visit a parse tree produced by {@link PascalParser#actual_parameter}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitActual_parameter(PascalParser.Actual_parameterContext ctx);
	/**
	 * Visit a parse tree produced by {@link PascalParser#statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStatement(PascalParser.StatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link PascalParser#simple_statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSimple_statement(PascalParser.Simple_statementContext ctx);
	/**
	 * Visit a parse tree produced by {@link PascalParser#empty_statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEmpty_statement(PascalParser.Empty_statementContext ctx);
	/**
	 * Visit a parse tree produced by {@link PascalParser#assignment_statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAssignment_statement(PascalParser.Assignment_statementContext ctx);
	/**
	 * Visit a parse tree produced by {@link PascalParser#procedure_statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitProcedure_statement(PascalParser.Procedure_statementContext ctx);
	/**
	 * Visit a parse tree produced by {@link PascalParser#structured_statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStructured_statement(PascalParser.Structured_statementContext ctx);
	/**
	 * Visit a parse tree produced by {@link PascalParser#statement_sequence}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStatement_sequence(PascalParser.Statement_sequenceContext ctx);
	/**
	 * Visit a parse tree produced by {@link PascalParser#compound_statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCompound_statement(PascalParser.Compound_statementContext ctx);
	/**
	 * Visit a parse tree produced by {@link PascalParser#if_statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIf_statement(PascalParser.If_statementContext ctx);
	/**
	 * Visit a parse tree produced by {@link PascalParser#else_part}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitElse_part(PascalParser.Else_partContext ctx);
	/**
	 * Visit a parse tree produced by {@link PascalParser#for_statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFor_statement(PascalParser.For_statementContext ctx);
	/**
	 * Visit a parse tree produced by {@link PascalParser#program}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitProgram(PascalParser.ProgramContext ctx);
	/**
	 * Visit a parse tree produced by {@link PascalParser#program_heading}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitProgram_heading(PascalParser.Program_headingContext ctx);
}