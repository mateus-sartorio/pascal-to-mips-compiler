// Generated from src/PascalParser.g by ANTLR 4.13.2

package parser;

import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.*;
import org.antlr.v4.runtime.tree.*;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast", "CheckReturnValue", "this-escape"})
public class PascalParser extends Parser {
	static { RuntimeMetaData.checkVersion("4.13.2", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		ASSIGNMENT=1, NOT_EQUAL_TO=2, LESS_THAN_OR_EQUAL_TO=3, GREATER_THAN_OR_EQUAL_TO=4, 
		RANGE=5, PLUS=6, MINUS=7, EQUAL_TO=8, MULTIPLICATION=9, DIVISION=10, LESS_THAN=11, 
		GREATER_THAN=12, OPEN_BRACKET=13, CLOSE_BRACKET=14, PERIOD=15, COMMA=16, 
		COLON=17, SEMICOLON=18, OPEN_PARENTHESIS=19, CLOSE_PARENTHESIS=20, AND=21, 
		ARRAY=22, BEGIN=23, DO=24, DOWNTO=25, ELSE=26, END=27, FOR=28, FUNCTION=29, 
		IF=30, NOT=31, OF=32, OR=33, PACKED=34, PROCEDURE=35, PROGRAM=36, THEN=37, 
		TO=38, TYPE=39, VAR=40, INTEGER=41, REAL=42, BOOLEAN=43, CHAR=44, STRING=45, 
		UNSIGNED_INTEGER=46, UNSIGNED_REAL=47, CHARACTER_STRING=48, COMMENTARY=49, 
		IDENTIFIER=50, WS=51;
	public static final int
		RULE_block = 0, RULE_variable_declaration_part = 1, RULE_procedure_and_function_declaration_part = 2, 
		RULE_primitive_type = 3, RULE_type_denoter = 4, RULE_identifier_list = 5, 
		RULE_subrange_type = 6, RULE_array_type = 7, RULE_variable_declaration = 8, 
		RULE_variable_access = 9, RULE_indexed_variable = 10, RULE_procedure_declaration = 11, 
		RULE_procedure_heading = 12, RULE_function_declaration = 13, RULE_function_heading = 14, 
		RULE_formal_parameter_list = 15, RULE_value_parameter_speficiation = 16, 
		RULE_expression = 17, RULE_simple_expression = 18, RULE_term = 19, RULE_factor = 20, 
		RULE_unsigned_constant = 21, RULE_multiplying_operator = 22, RULE_adding_operator = 23, 
		RULE_relational_operator = 24, RULE_function_designator = 25, RULE_actual_parameter_list = 26, 
		RULE_actual_parameter = 27, RULE_statement = 28, RULE_simple_statement = 29, 
		RULE_empty_statement = 30, RULE_assignment_statement = 31, RULE_procedure_statement = 32, 
		RULE_structured_statement = 33, RULE_statement_sequence = 34, RULE_compound_statement = 35, 
		RULE_if_statement = 36, RULE_else_part = 37, RULE_for_statement = 38, 
		RULE_program = 39, RULE_program_heading = 40;
	private static String[] makeRuleNames() {
		return new String[] {
			"block", "variable_declaration_part", "procedure_and_function_declaration_part", 
			"primitive_type", "type_denoter", "identifier_list", "subrange_type", 
			"array_type", "variable_declaration", "variable_access", "indexed_variable", 
			"procedure_declaration", "procedure_heading", "function_declaration", 
			"function_heading", "formal_parameter_list", "value_parameter_speficiation", 
			"expression", "simple_expression", "term", "factor", "unsigned_constant", 
			"multiplying_operator", "adding_operator", "relational_operator", "function_designator", 
			"actual_parameter_list", "actual_parameter", "statement", "simple_statement", 
			"empty_statement", "assignment_statement", "procedure_statement", "structured_statement", 
			"statement_sequence", "compound_statement", "if_statement", "else_part", 
			"for_statement", "program", "program_heading"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, "':='", "'<>'", "'<='", "'>='", "'..'", "'+'", "'-'", "'='", "'*'", 
			"'/'", "'<'", "'>'", null, null, "'.'", "','", "':'", "';'", "'('", "')'", 
			"'and'", "'array'", "'begin'", "'do'", "'downto'", "'else'", "'end'", 
			"'for'", "'function'", "'if'", "'not'", "'of'", "'or'", "'packed'", "'procedure'", 
			"'program'", "'then'", "'to'", "'type'", "'var'", "'integer'", "'real'", 
			"'boolean'", "'char'", "'string'"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, "ASSIGNMENT", "NOT_EQUAL_TO", "LESS_THAN_OR_EQUAL_TO", "GREATER_THAN_OR_EQUAL_TO", 
			"RANGE", "PLUS", "MINUS", "EQUAL_TO", "MULTIPLICATION", "DIVISION", "LESS_THAN", 
			"GREATER_THAN", "OPEN_BRACKET", "CLOSE_BRACKET", "PERIOD", "COMMA", "COLON", 
			"SEMICOLON", "OPEN_PARENTHESIS", "CLOSE_PARENTHESIS", "AND", "ARRAY", 
			"BEGIN", "DO", "DOWNTO", "ELSE", "END", "FOR", "FUNCTION", "IF", "NOT", 
			"OF", "OR", "PACKED", "PROCEDURE", "PROGRAM", "THEN", "TO", "TYPE", "VAR", 
			"INTEGER", "REAL", "BOOLEAN", "CHAR", "STRING", "UNSIGNED_INTEGER", "UNSIGNED_REAL", 
			"CHARACTER_STRING", "COMMENTARY", "IDENTIFIER", "WS"
		};
	}
	private static final String[] _SYMBOLIC_NAMES = makeSymbolicNames();
	public static final Vocabulary VOCABULARY = new VocabularyImpl(_LITERAL_NAMES, _SYMBOLIC_NAMES);

	/**
	 * @deprecated Use {@link #VOCABULARY} instead.
	 */
	@Deprecated
	public static final String[] tokenNames;
	static {
		tokenNames = new String[_SYMBOLIC_NAMES.length];
		for (int i = 0; i < tokenNames.length; i++) {
			tokenNames[i] = VOCABULARY.getLiteralName(i);
			if (tokenNames[i] == null) {
				tokenNames[i] = VOCABULARY.getSymbolicName(i);
			}

			if (tokenNames[i] == null) {
				tokenNames[i] = "<INVALID>";
			}
		}
	}

	@Override
	@Deprecated
	public String[] getTokenNames() {
		return tokenNames;
	}

	@Override

	public Vocabulary getVocabulary() {
		return VOCABULARY;
	}

	@Override
	public String getGrammarFileName() { return "PascalParser.g"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public ATN getATN() { return _ATN; }

	public PascalParser(TokenStream input) {
		super(input);
		_interp = new ParserATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	@SuppressWarnings("CheckReturnValue")
	public static class BlockContext extends ParserRuleContext {
		public Variable_declaration_partContext variable_declaration_part() {
			return getRuleContext(Variable_declaration_partContext.class,0);
		}
		public Procedure_and_function_declaration_partContext procedure_and_function_declaration_part() {
			return getRuleContext(Procedure_and_function_declaration_partContext.class,0);
		}
		public Compound_statementContext compound_statement() {
			return getRuleContext(Compound_statementContext.class,0);
		}
		public BlockContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_block; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PascalParserVisitor ) return ((PascalParserVisitor<? extends T>)visitor).visitBlock(this);
			else return visitor.visitChildren(this);
		}
	}

	public final BlockContext block() throws RecognitionException {
		BlockContext _localctx = new BlockContext(_ctx, getState());
		enterRule(_localctx, 0, RULE_block);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(82);
			variable_declaration_part();
			setState(83);
			procedure_and_function_declaration_part();
			setState(84);
			compound_statement();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class Variable_declaration_partContext extends ParserRuleContext {
		public List<TerminalNode> VAR() { return getTokens(PascalParser.VAR); }
		public TerminalNode VAR(int i) {
			return getToken(PascalParser.VAR, i);
		}
		public List<Variable_declarationContext> variable_declaration() {
			return getRuleContexts(Variable_declarationContext.class);
		}
		public Variable_declarationContext variable_declaration(int i) {
			return getRuleContext(Variable_declarationContext.class,i);
		}
		public List<TerminalNode> SEMICOLON() { return getTokens(PascalParser.SEMICOLON); }
		public TerminalNode SEMICOLON(int i) {
			return getToken(PascalParser.SEMICOLON, i);
		}
		public Variable_declaration_partContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_variable_declaration_part; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PascalParserVisitor ) return ((PascalParserVisitor<? extends T>)visitor).visitVariable_declaration_part(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Variable_declaration_partContext variable_declaration_part() throws RecognitionException {
		Variable_declaration_partContext _localctx = new Variable_declaration_partContext(_ctx, getState());
		enterRule(_localctx, 2, RULE_variable_declaration_part);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(99);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==VAR) {
				{
				{
				setState(86);
				match(VAR);
				setState(87);
				variable_declaration();
				setState(92);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,0,_ctx);
				while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
					if ( _alt==1 ) {
						{
						{
						setState(88);
						match(SEMICOLON);
						setState(89);
						variable_declaration();
						}
						} 
					}
					setState(94);
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,0,_ctx);
				}
				setState(95);
				match(SEMICOLON);
				}
				}
				setState(101);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class Procedure_and_function_declaration_partContext extends ParserRuleContext {
		public List<TerminalNode> SEMICOLON() { return getTokens(PascalParser.SEMICOLON); }
		public TerminalNode SEMICOLON(int i) {
			return getToken(PascalParser.SEMICOLON, i);
		}
		public List<Procedure_declarationContext> procedure_declaration() {
			return getRuleContexts(Procedure_declarationContext.class);
		}
		public Procedure_declarationContext procedure_declaration(int i) {
			return getRuleContext(Procedure_declarationContext.class,i);
		}
		public List<Function_declarationContext> function_declaration() {
			return getRuleContexts(Function_declarationContext.class);
		}
		public Function_declarationContext function_declaration(int i) {
			return getRuleContext(Function_declarationContext.class,i);
		}
		public Procedure_and_function_declaration_partContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_procedure_and_function_declaration_part; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PascalParserVisitor ) return ((PascalParserVisitor<? extends T>)visitor).visitProcedure_and_function_declaration_part(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Procedure_and_function_declaration_partContext procedure_and_function_declaration_part() throws RecognitionException {
		Procedure_and_function_declaration_partContext _localctx = new Procedure_and_function_declaration_partContext(_ctx, getState());
		enterRule(_localctx, 4, RULE_procedure_and_function_declaration_part);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(110);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==FUNCTION || _la==PROCEDURE) {
				{
				{
				setState(104);
				_errHandler.sync(this);
				switch (_input.LA(1)) {
				case PROCEDURE:
					{
					setState(102);
					procedure_declaration();
					}
					break;
				case FUNCTION:
					{
					setState(103);
					function_declaration();
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				setState(106);
				match(SEMICOLON);
				}
				}
				setState(112);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class Primitive_typeContext extends ParserRuleContext {
		public TerminalNode INTEGER() { return getToken(PascalParser.INTEGER, 0); }
		public TerminalNode REAL() { return getToken(PascalParser.REAL, 0); }
		public TerminalNode BOOLEAN() { return getToken(PascalParser.BOOLEAN, 0); }
		public TerminalNode CHAR() { return getToken(PascalParser.CHAR, 0); }
		public TerminalNode STRING() { return getToken(PascalParser.STRING, 0); }
		public Primitive_typeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_primitive_type; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PascalParserVisitor ) return ((PascalParserVisitor<? extends T>)visitor).visitPrimitive_type(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Primitive_typeContext primitive_type() throws RecognitionException {
		Primitive_typeContext _localctx = new Primitive_typeContext(_ctx, getState());
		enterRule(_localctx, 6, RULE_primitive_type);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(113);
			_la = _input.LA(1);
			if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & 68169720922112L) != 0)) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class Type_denoterContext extends ParserRuleContext {
		public Primitive_typeContext primitive_type() {
			return getRuleContext(Primitive_typeContext.class,0);
		}
		public Array_typeContext array_type() {
			return getRuleContext(Array_typeContext.class,0);
		}
		public Type_denoterContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_type_denoter; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PascalParserVisitor ) return ((PascalParserVisitor<? extends T>)visitor).visitType_denoter(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Type_denoterContext type_denoter() throws RecognitionException {
		Type_denoterContext _localctx = new Type_denoterContext(_ctx, getState());
		enterRule(_localctx, 8, RULE_type_denoter);
		try {
			setState(117);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case INTEGER:
			case REAL:
			case BOOLEAN:
			case CHAR:
			case STRING:
				enterOuterAlt(_localctx, 1);
				{
				setState(115);
				primitive_type();
				}
				break;
			case ARRAY:
			case PACKED:
				enterOuterAlt(_localctx, 2);
				{
				setState(116);
				array_type();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class Identifier_listContext extends ParserRuleContext {
		public List<TerminalNode> IDENTIFIER() { return getTokens(PascalParser.IDENTIFIER); }
		public TerminalNode IDENTIFIER(int i) {
			return getToken(PascalParser.IDENTIFIER, i);
		}
		public List<TerminalNode> COMMA() { return getTokens(PascalParser.COMMA); }
		public TerminalNode COMMA(int i) {
			return getToken(PascalParser.COMMA, i);
		}
		public Identifier_listContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_identifier_list; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PascalParserVisitor ) return ((PascalParserVisitor<? extends T>)visitor).visitIdentifier_list(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Identifier_listContext identifier_list() throws RecognitionException {
		Identifier_listContext _localctx = new Identifier_listContext(_ctx, getState());
		enterRule(_localctx, 10, RULE_identifier_list);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(119);
			match(IDENTIFIER);
			setState(124);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==COMMA) {
				{
				{
				setState(120);
				match(COMMA);
				setState(121);
				match(IDENTIFIER);
				}
				}
				setState(126);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class Subrange_typeContext extends ParserRuleContext {
		public List<TerminalNode> UNSIGNED_INTEGER() { return getTokens(PascalParser.UNSIGNED_INTEGER); }
		public TerminalNode UNSIGNED_INTEGER(int i) {
			return getToken(PascalParser.UNSIGNED_INTEGER, i);
		}
		public TerminalNode RANGE() { return getToken(PascalParser.RANGE, 0); }
		public Subrange_typeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_subrange_type; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PascalParserVisitor ) return ((PascalParserVisitor<? extends T>)visitor).visitSubrange_type(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Subrange_typeContext subrange_type() throws RecognitionException {
		Subrange_typeContext _localctx = new Subrange_typeContext(_ctx, getState());
		enterRule(_localctx, 12, RULE_subrange_type);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(127);
			match(UNSIGNED_INTEGER);
			setState(128);
			match(RANGE);
			setState(129);
			match(UNSIGNED_INTEGER);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class Array_typeContext extends ParserRuleContext {
		public TerminalNode ARRAY() { return getToken(PascalParser.ARRAY, 0); }
		public TerminalNode OPEN_BRACKET() { return getToken(PascalParser.OPEN_BRACKET, 0); }
		public Subrange_typeContext subrange_type() {
			return getRuleContext(Subrange_typeContext.class,0);
		}
		public TerminalNode CLOSE_BRACKET() { return getToken(PascalParser.CLOSE_BRACKET, 0); }
		public TerminalNode OF() { return getToken(PascalParser.OF, 0); }
		public Primitive_typeContext primitive_type() {
			return getRuleContext(Primitive_typeContext.class,0);
		}
		public TerminalNode PACKED() { return getToken(PascalParser.PACKED, 0); }
		public Array_typeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_array_type; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PascalParserVisitor ) return ((PascalParserVisitor<? extends T>)visitor).visitArray_type(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Array_typeContext array_type() throws RecognitionException {
		Array_typeContext _localctx = new Array_typeContext(_ctx, getState());
		enterRule(_localctx, 14, RULE_array_type);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(132);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==PACKED) {
				{
				setState(131);
				match(PACKED);
				}
			}

			setState(134);
			match(ARRAY);
			setState(135);
			match(OPEN_BRACKET);
			setState(136);
			subrange_type();
			setState(137);
			match(CLOSE_BRACKET);
			setState(138);
			match(OF);
			setState(139);
			primitive_type();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class Variable_declarationContext extends ParserRuleContext {
		public Identifier_listContext identifier_list() {
			return getRuleContext(Identifier_listContext.class,0);
		}
		public TerminalNode COLON() { return getToken(PascalParser.COLON, 0); }
		public Type_denoterContext type_denoter() {
			return getRuleContext(Type_denoterContext.class,0);
		}
		public Variable_declarationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_variable_declaration; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PascalParserVisitor ) return ((PascalParserVisitor<? extends T>)visitor).visitVariable_declaration(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Variable_declarationContext variable_declaration() throws RecognitionException {
		Variable_declarationContext _localctx = new Variable_declarationContext(_ctx, getState());
		enterRule(_localctx, 16, RULE_variable_declaration);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(141);
			identifier_list();
			setState(142);
			match(COLON);
			setState(143);
			type_denoter();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class Variable_accessContext extends ParserRuleContext {
		public TerminalNode IDENTIFIER() { return getToken(PascalParser.IDENTIFIER, 0); }
		public Indexed_variableContext indexed_variable() {
			return getRuleContext(Indexed_variableContext.class,0);
		}
		public Variable_accessContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_variable_access; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PascalParserVisitor ) return ((PascalParserVisitor<? extends T>)visitor).visitVariable_access(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Variable_accessContext variable_access() throws RecognitionException {
		Variable_accessContext _localctx = new Variable_accessContext(_ctx, getState());
		enterRule(_localctx, 18, RULE_variable_access);
		try {
			setState(147);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,7,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(145);
				match(IDENTIFIER);
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(146);
				indexed_variable();
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class Indexed_variableContext extends ParserRuleContext {
		public TerminalNode IDENTIFIER() { return getToken(PascalParser.IDENTIFIER, 0); }
		public TerminalNode OPEN_BRACKET() { return getToken(PascalParser.OPEN_BRACKET, 0); }
		public TerminalNode CLOSE_BRACKET() { return getToken(PascalParser.CLOSE_BRACKET, 0); }
		public List<ExpressionContext> expression() {
			return getRuleContexts(ExpressionContext.class);
		}
		public ExpressionContext expression(int i) {
			return getRuleContext(ExpressionContext.class,i);
		}
		public List<TerminalNode> COMMA() { return getTokens(PascalParser.COMMA); }
		public TerminalNode COMMA(int i) {
			return getToken(PascalParser.COMMA, i);
		}
		public Indexed_variableContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_indexed_variable; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PascalParserVisitor ) return ((PascalParserVisitor<? extends T>)visitor).visitIndexed_variable(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Indexed_variableContext indexed_variable() throws RecognitionException {
		Indexed_variableContext _localctx = new Indexed_variableContext(_ctx, getState());
		enterRule(_localctx, 20, RULE_indexed_variable);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(149);
			match(IDENTIFIER);
			setState(150);
			match(OPEN_BRACKET);
			{
			setState(151);
			expression();
			setState(156);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==COMMA) {
				{
				{
				setState(152);
				match(COMMA);
				setState(153);
				expression();
				}
				}
				setState(158);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
			setState(159);
			match(CLOSE_BRACKET);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class Procedure_declarationContext extends ParserRuleContext {
		public Procedure_headingContext procedure_heading() {
			return getRuleContext(Procedure_headingContext.class,0);
		}
		public Compound_statementContext compound_statement() {
			return getRuleContext(Compound_statementContext.class,0);
		}
		public Procedure_declarationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_procedure_declaration; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PascalParserVisitor ) return ((PascalParserVisitor<? extends T>)visitor).visitProcedure_declaration(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Procedure_declarationContext procedure_declaration() throws RecognitionException {
		Procedure_declarationContext _localctx = new Procedure_declarationContext(_ctx, getState());
		enterRule(_localctx, 22, RULE_procedure_declaration);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(161);
			procedure_heading();
			setState(162);
			compound_statement();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class Procedure_headingContext extends ParserRuleContext {
		public TerminalNode PROCEDURE() { return getToken(PascalParser.PROCEDURE, 0); }
		public TerminalNode IDENTIFIER() { return getToken(PascalParser.IDENTIFIER, 0); }
		public TerminalNode SEMICOLON() { return getToken(PascalParser.SEMICOLON, 0); }
		public Variable_declaration_partContext variable_declaration_part() {
			return getRuleContext(Variable_declaration_partContext.class,0);
		}
		public Formal_parameter_listContext formal_parameter_list() {
			return getRuleContext(Formal_parameter_listContext.class,0);
		}
		public Procedure_headingContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_procedure_heading; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PascalParserVisitor ) return ((PascalParserVisitor<? extends T>)visitor).visitProcedure_heading(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Procedure_headingContext procedure_heading() throws RecognitionException {
		Procedure_headingContext _localctx = new Procedure_headingContext(_ctx, getState());
		enterRule(_localctx, 24, RULE_procedure_heading);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(164);
			match(PROCEDURE);
			setState(165);
			match(IDENTIFIER);
			setState(167);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==OPEN_PARENTHESIS) {
				{
				setState(166);
				formal_parameter_list();
				}
			}

			setState(169);
			match(SEMICOLON);
			setState(170);
			variable_declaration_part();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class Function_declarationContext extends ParserRuleContext {
		public Function_headingContext function_heading() {
			return getRuleContext(Function_headingContext.class,0);
		}
		public Compound_statementContext compound_statement() {
			return getRuleContext(Compound_statementContext.class,0);
		}
		public Function_declarationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_function_declaration; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PascalParserVisitor ) return ((PascalParserVisitor<? extends T>)visitor).visitFunction_declaration(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Function_declarationContext function_declaration() throws RecognitionException {
		Function_declarationContext _localctx = new Function_declarationContext(_ctx, getState());
		enterRule(_localctx, 26, RULE_function_declaration);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(172);
			function_heading();
			setState(173);
			compound_statement();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class Function_headingContext extends ParserRuleContext {
		public TerminalNode FUNCTION() { return getToken(PascalParser.FUNCTION, 0); }
		public TerminalNode IDENTIFIER() { return getToken(PascalParser.IDENTIFIER, 0); }
		public TerminalNode COLON() { return getToken(PascalParser.COLON, 0); }
		public Type_denoterContext type_denoter() {
			return getRuleContext(Type_denoterContext.class,0);
		}
		public TerminalNode SEMICOLON() { return getToken(PascalParser.SEMICOLON, 0); }
		public Variable_declaration_partContext variable_declaration_part() {
			return getRuleContext(Variable_declaration_partContext.class,0);
		}
		public Formal_parameter_listContext formal_parameter_list() {
			return getRuleContext(Formal_parameter_listContext.class,0);
		}
		public Function_headingContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_function_heading; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PascalParserVisitor ) return ((PascalParserVisitor<? extends T>)visitor).visitFunction_heading(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Function_headingContext function_heading() throws RecognitionException {
		Function_headingContext _localctx = new Function_headingContext(_ctx, getState());
		enterRule(_localctx, 28, RULE_function_heading);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(175);
			match(FUNCTION);
			setState(176);
			match(IDENTIFIER);
			setState(178);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==OPEN_PARENTHESIS) {
				{
				setState(177);
				formal_parameter_list();
				}
			}

			setState(180);
			match(COLON);
			setState(181);
			type_denoter();
			setState(182);
			match(SEMICOLON);
			setState(183);
			variable_declaration_part();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class Formal_parameter_listContext extends ParserRuleContext {
		public TerminalNode OPEN_PARENTHESIS() { return getToken(PascalParser.OPEN_PARENTHESIS, 0); }
		public TerminalNode CLOSE_PARENTHESIS() { return getToken(PascalParser.CLOSE_PARENTHESIS, 0); }
		public List<Value_parameter_speficiationContext> value_parameter_speficiation() {
			return getRuleContexts(Value_parameter_speficiationContext.class);
		}
		public Value_parameter_speficiationContext value_parameter_speficiation(int i) {
			return getRuleContext(Value_parameter_speficiationContext.class,i);
		}
		public List<TerminalNode> SEMICOLON() { return getTokens(PascalParser.SEMICOLON); }
		public TerminalNode SEMICOLON(int i) {
			return getToken(PascalParser.SEMICOLON, i);
		}
		public Formal_parameter_listContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_formal_parameter_list; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PascalParserVisitor ) return ((PascalParserVisitor<? extends T>)visitor).visitFormal_parameter_list(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Formal_parameter_listContext formal_parameter_list() throws RecognitionException {
		Formal_parameter_listContext _localctx = new Formal_parameter_listContext(_ctx, getState());
		enterRule(_localctx, 30, RULE_formal_parameter_list);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(185);
			match(OPEN_PARENTHESIS);
			setState(194);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==IDENTIFIER) {
				{
				setState(186);
				value_parameter_speficiation();
				setState(191);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==SEMICOLON) {
					{
					{
					setState(187);
					match(SEMICOLON);
					setState(188);
					value_parameter_speficiation();
					}
					}
					setState(193);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				}
			}

			setState(196);
			match(CLOSE_PARENTHESIS);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class Value_parameter_speficiationContext extends ParserRuleContext {
		public Identifier_listContext identifier_list() {
			return getRuleContext(Identifier_listContext.class,0);
		}
		public TerminalNode COLON() { return getToken(PascalParser.COLON, 0); }
		public Type_denoterContext type_denoter() {
			return getRuleContext(Type_denoterContext.class,0);
		}
		public Value_parameter_speficiationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_value_parameter_speficiation; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PascalParserVisitor ) return ((PascalParserVisitor<? extends T>)visitor).visitValue_parameter_speficiation(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Value_parameter_speficiationContext value_parameter_speficiation() throws RecognitionException {
		Value_parameter_speficiationContext _localctx = new Value_parameter_speficiationContext(_ctx, getState());
		enterRule(_localctx, 32, RULE_value_parameter_speficiation);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(198);
			identifier_list();
			setState(199);
			match(COLON);
			setState(200);
			type_denoter();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ExpressionContext extends ParserRuleContext {
		public List<Simple_expressionContext> simple_expression() {
			return getRuleContexts(Simple_expressionContext.class);
		}
		public Simple_expressionContext simple_expression(int i) {
			return getRuleContext(Simple_expressionContext.class,i);
		}
		public Relational_operatorContext relational_operator() {
			return getRuleContext(Relational_operatorContext.class,0);
		}
		public ExpressionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_expression; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PascalParserVisitor ) return ((PascalParserVisitor<? extends T>)visitor).visitExpression(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ExpressionContext expression() throws RecognitionException {
		ExpressionContext _localctx = new ExpressionContext(_ctx, getState());
		enterRule(_localctx, 34, RULE_expression);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(202);
			simple_expression();
			setState(206);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & 6428L) != 0)) {
				{
				setState(203);
				relational_operator();
				setState(204);
				simple_expression();
				}
			}

			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class Simple_expressionContext extends ParserRuleContext {
		public List<TermContext> term() {
			return getRuleContexts(TermContext.class);
		}
		public TermContext term(int i) {
			return getRuleContext(TermContext.class,i);
		}
		public List<Adding_operatorContext> adding_operator() {
			return getRuleContexts(Adding_operatorContext.class);
		}
		public Adding_operatorContext adding_operator(int i) {
			return getRuleContext(Adding_operatorContext.class,i);
		}
		public TerminalNode PLUS() { return getToken(PascalParser.PLUS, 0); }
		public TerminalNode MINUS() { return getToken(PascalParser.MINUS, 0); }
		public Simple_expressionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_simple_expression; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PascalParserVisitor ) return ((PascalParserVisitor<? extends T>)visitor).visitSimple_expression(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Simple_expressionContext simple_expression() throws RecognitionException {
		Simple_expressionContext _localctx = new Simple_expressionContext(_ctx, getState());
		enterRule(_localctx, 36, RULE_simple_expression);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(209);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==PLUS || _la==MINUS) {
				{
				setState(208);
				_la = _input.LA(1);
				if ( !(_la==PLUS || _la==MINUS) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
			}

			setState(211);
			term();
			setState(217);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & 8589934784L) != 0)) {
				{
				{
				setState(212);
				adding_operator();
				setState(213);
				term();
				}
				}
				setState(219);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class TermContext extends ParserRuleContext {
		public List<FactorContext> factor() {
			return getRuleContexts(FactorContext.class);
		}
		public FactorContext factor(int i) {
			return getRuleContext(FactorContext.class,i);
		}
		public List<Multiplying_operatorContext> multiplying_operator() {
			return getRuleContexts(Multiplying_operatorContext.class);
		}
		public Multiplying_operatorContext multiplying_operator(int i) {
			return getRuleContext(Multiplying_operatorContext.class,i);
		}
		public TermContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_term; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PascalParserVisitor ) return ((PascalParserVisitor<? extends T>)visitor).visitTerm(this);
			else return visitor.visitChildren(this);
		}
	}

	public final TermContext term() throws RecognitionException {
		TermContext _localctx = new TermContext(_ctx, getState());
		enterRule(_localctx, 38, RULE_term);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(220);
			factor();
			setState(226);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & 2098688L) != 0)) {
				{
				{
				setState(221);
				multiplying_operator();
				setState(222);
				factor();
				}
				}
				setState(228);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class FactorContext extends ParserRuleContext {
		public Variable_accessContext variable_access() {
			return getRuleContext(Variable_accessContext.class,0);
		}
		public Unsigned_constantContext unsigned_constant() {
			return getRuleContext(Unsigned_constantContext.class,0);
		}
		public Function_designatorContext function_designator() {
			return getRuleContext(Function_designatorContext.class,0);
		}
		public TerminalNode OPEN_PARENTHESIS() { return getToken(PascalParser.OPEN_PARENTHESIS, 0); }
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public TerminalNode CLOSE_PARENTHESIS() { return getToken(PascalParser.CLOSE_PARENTHESIS, 0); }
		public TerminalNode NOT() { return getToken(PascalParser.NOT, 0); }
		public FactorContext factor() {
			return getRuleContext(FactorContext.class,0);
		}
		public FactorContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_factor; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PascalParserVisitor ) return ((PascalParserVisitor<? extends T>)visitor).visitFactor(this);
			else return visitor.visitChildren(this);
		}
	}

	public final FactorContext factor() throws RecognitionException {
		FactorContext _localctx = new FactorContext(_ctx, getState());
		enterRule(_localctx, 40, RULE_factor);
		try {
			setState(238);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,17,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(229);
				variable_access();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(230);
				unsigned_constant();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(231);
				function_designator();
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(232);
				match(OPEN_PARENTHESIS);
				setState(233);
				expression();
				setState(234);
				match(CLOSE_PARENTHESIS);
				}
				break;
			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(236);
				match(NOT);
				setState(237);
				factor();
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class Unsigned_constantContext extends ParserRuleContext {
		public TerminalNode UNSIGNED_INTEGER() { return getToken(PascalParser.UNSIGNED_INTEGER, 0); }
		public TerminalNode UNSIGNED_REAL() { return getToken(PascalParser.UNSIGNED_REAL, 0); }
		public TerminalNode CHARACTER_STRING() { return getToken(PascalParser.CHARACTER_STRING, 0); }
		public Unsigned_constantContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_unsigned_constant; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PascalParserVisitor ) return ((PascalParserVisitor<? extends T>)visitor).visitUnsigned_constant(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Unsigned_constantContext unsigned_constant() throws RecognitionException {
		Unsigned_constantContext _localctx = new Unsigned_constantContext(_ctx, getState());
		enterRule(_localctx, 42, RULE_unsigned_constant);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(240);
			_la = _input.LA(1);
			if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & 492581209243648L) != 0)) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class Multiplying_operatorContext extends ParserRuleContext {
		public TerminalNode MULTIPLICATION() { return getToken(PascalParser.MULTIPLICATION, 0); }
		public TerminalNode DIVISION() { return getToken(PascalParser.DIVISION, 0); }
		public TerminalNode AND() { return getToken(PascalParser.AND, 0); }
		public Multiplying_operatorContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_multiplying_operator; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PascalParserVisitor ) return ((PascalParserVisitor<? extends T>)visitor).visitMultiplying_operator(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Multiplying_operatorContext multiplying_operator() throws RecognitionException {
		Multiplying_operatorContext _localctx = new Multiplying_operatorContext(_ctx, getState());
		enterRule(_localctx, 44, RULE_multiplying_operator);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(242);
			_la = _input.LA(1);
			if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & 2098688L) != 0)) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class Adding_operatorContext extends ParserRuleContext {
		public TerminalNode PLUS() { return getToken(PascalParser.PLUS, 0); }
		public TerminalNode MINUS() { return getToken(PascalParser.MINUS, 0); }
		public TerminalNode OR() { return getToken(PascalParser.OR, 0); }
		public Adding_operatorContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_adding_operator; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PascalParserVisitor ) return ((PascalParserVisitor<? extends T>)visitor).visitAdding_operator(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Adding_operatorContext adding_operator() throws RecognitionException {
		Adding_operatorContext _localctx = new Adding_operatorContext(_ctx, getState());
		enterRule(_localctx, 46, RULE_adding_operator);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(244);
			_la = _input.LA(1);
			if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & 8589934784L) != 0)) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class Relational_operatorContext extends ParserRuleContext {
		public TerminalNode EQUAL_TO() { return getToken(PascalParser.EQUAL_TO, 0); }
		public TerminalNode NOT_EQUAL_TO() { return getToken(PascalParser.NOT_EQUAL_TO, 0); }
		public TerminalNode LESS_THAN() { return getToken(PascalParser.LESS_THAN, 0); }
		public TerminalNode LESS_THAN_OR_EQUAL_TO() { return getToken(PascalParser.LESS_THAN_OR_EQUAL_TO, 0); }
		public TerminalNode GREATER_THAN() { return getToken(PascalParser.GREATER_THAN, 0); }
		public TerminalNode GREATER_THAN_OR_EQUAL_TO() { return getToken(PascalParser.GREATER_THAN_OR_EQUAL_TO, 0); }
		public Relational_operatorContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_relational_operator; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PascalParserVisitor ) return ((PascalParserVisitor<? extends T>)visitor).visitRelational_operator(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Relational_operatorContext relational_operator() throws RecognitionException {
		Relational_operatorContext _localctx = new Relational_operatorContext(_ctx, getState());
		enterRule(_localctx, 48, RULE_relational_operator);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(246);
			_la = _input.LA(1);
			if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & 6428L) != 0)) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class Function_designatorContext extends ParserRuleContext {
		public TerminalNode IDENTIFIER() { return getToken(PascalParser.IDENTIFIER, 0); }
		public Actual_parameter_listContext actual_parameter_list() {
			return getRuleContext(Actual_parameter_listContext.class,0);
		}
		public Function_designatorContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_function_designator; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PascalParserVisitor ) return ((PascalParserVisitor<? extends T>)visitor).visitFunction_designator(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Function_designatorContext function_designator() throws RecognitionException {
		Function_designatorContext _localctx = new Function_designatorContext(_ctx, getState());
		enterRule(_localctx, 50, RULE_function_designator);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(248);
			match(IDENTIFIER);
			setState(250);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==OPEN_PARENTHESIS) {
				{
				setState(249);
				actual_parameter_list();
				}
			}

			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class Actual_parameter_listContext extends ParserRuleContext {
		public TerminalNode OPEN_PARENTHESIS() { return getToken(PascalParser.OPEN_PARENTHESIS, 0); }
		public List<Actual_parameterContext> actual_parameter() {
			return getRuleContexts(Actual_parameterContext.class);
		}
		public Actual_parameterContext actual_parameter(int i) {
			return getRuleContext(Actual_parameterContext.class,i);
		}
		public TerminalNode CLOSE_PARENTHESIS() { return getToken(PascalParser.CLOSE_PARENTHESIS, 0); }
		public List<TerminalNode> COMMA() { return getTokens(PascalParser.COMMA); }
		public TerminalNode COMMA(int i) {
			return getToken(PascalParser.COMMA, i);
		}
		public Actual_parameter_listContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_actual_parameter_list; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PascalParserVisitor ) return ((PascalParserVisitor<? extends T>)visitor).visitActual_parameter_list(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Actual_parameter_listContext actual_parameter_list() throws RecognitionException {
		Actual_parameter_listContext _localctx = new Actual_parameter_listContext(_ctx, getState());
		enterRule(_localctx, 52, RULE_actual_parameter_list);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(252);
			match(OPEN_PARENTHESIS);
			setState(253);
			actual_parameter();
			setState(258);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==COMMA) {
				{
				{
				setState(254);
				match(COMMA);
				setState(255);
				actual_parameter();
				}
				}
				setState(260);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(261);
			match(CLOSE_PARENTHESIS);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class Actual_parameterContext extends ParserRuleContext {
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public Variable_accessContext variable_access() {
			return getRuleContext(Variable_accessContext.class,0);
		}
		public Actual_parameterContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_actual_parameter; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PascalParserVisitor ) return ((PascalParserVisitor<? extends T>)visitor).visitActual_parameter(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Actual_parameterContext actual_parameter() throws RecognitionException {
		Actual_parameterContext _localctx = new Actual_parameterContext(_ctx, getState());
		enterRule(_localctx, 54, RULE_actual_parameter);
		try {
			setState(265);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,20,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(263);
				expression();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(264);
				variable_access();
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class StatementContext extends ParserRuleContext {
		public Simple_statementContext simple_statement() {
			return getRuleContext(Simple_statementContext.class,0);
		}
		public Structured_statementContext structured_statement() {
			return getRuleContext(Structured_statementContext.class,0);
		}
		public StatementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_statement; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PascalParserVisitor ) return ((PascalParserVisitor<? extends T>)visitor).visitStatement(this);
			else return visitor.visitChildren(this);
		}
	}

	public final StatementContext statement() throws RecognitionException {
		StatementContext _localctx = new StatementContext(_ctx, getState());
		enterRule(_localctx, 56, RULE_statement);
		try {
			setState(269);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case SEMICOLON:
			case ELSE:
			case END:
			case IDENTIFIER:
				enterOuterAlt(_localctx, 1);
				{
				setState(267);
				simple_statement();
				}
				break;
			case FOR:
			case IF:
				enterOuterAlt(_localctx, 2);
				{
				setState(268);
				structured_statement();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class Simple_statementContext extends ParserRuleContext {
		public Empty_statementContext empty_statement() {
			return getRuleContext(Empty_statementContext.class,0);
		}
		public Assignment_statementContext assignment_statement() {
			return getRuleContext(Assignment_statementContext.class,0);
		}
		public Procedure_statementContext procedure_statement() {
			return getRuleContext(Procedure_statementContext.class,0);
		}
		public Simple_statementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_simple_statement; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PascalParserVisitor ) return ((PascalParserVisitor<? extends T>)visitor).visitSimple_statement(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Simple_statementContext simple_statement() throws RecognitionException {
		Simple_statementContext _localctx = new Simple_statementContext(_ctx, getState());
		enterRule(_localctx, 58, RULE_simple_statement);
		try {
			setState(274);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,22,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(271);
				empty_statement();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(272);
				assignment_statement();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(273);
				procedure_statement();
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class Empty_statementContext extends ParserRuleContext {
		public Empty_statementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_empty_statement; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PascalParserVisitor ) return ((PascalParserVisitor<? extends T>)visitor).visitEmpty_statement(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Empty_statementContext empty_statement() throws RecognitionException {
		Empty_statementContext _localctx = new Empty_statementContext(_ctx, getState());
		enterRule(_localctx, 60, RULE_empty_statement);
		try {
			enterOuterAlt(_localctx, 1);
			{
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class Assignment_statementContext extends ParserRuleContext {
		public Variable_accessContext variable_access() {
			return getRuleContext(Variable_accessContext.class,0);
		}
		public TerminalNode ASSIGNMENT() { return getToken(PascalParser.ASSIGNMENT, 0); }
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public Assignment_statementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_assignment_statement; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PascalParserVisitor ) return ((PascalParserVisitor<? extends T>)visitor).visitAssignment_statement(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Assignment_statementContext assignment_statement() throws RecognitionException {
		Assignment_statementContext _localctx = new Assignment_statementContext(_ctx, getState());
		enterRule(_localctx, 62, RULE_assignment_statement);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(278);
			variable_access();
			setState(279);
			match(ASSIGNMENT);
			setState(280);
			expression();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class Procedure_statementContext extends ParserRuleContext {
		public TerminalNode IDENTIFIER() { return getToken(PascalParser.IDENTIFIER, 0); }
		public Actual_parameter_listContext actual_parameter_list() {
			return getRuleContext(Actual_parameter_listContext.class,0);
		}
		public Procedure_statementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_procedure_statement; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PascalParserVisitor ) return ((PascalParserVisitor<? extends T>)visitor).visitProcedure_statement(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Procedure_statementContext procedure_statement() throws RecognitionException {
		Procedure_statementContext _localctx = new Procedure_statementContext(_ctx, getState());
		enterRule(_localctx, 64, RULE_procedure_statement);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(282);
			match(IDENTIFIER);
			setState(284);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==OPEN_PARENTHESIS) {
				{
				setState(283);
				actual_parameter_list();
				}
			}

			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class Structured_statementContext extends ParserRuleContext {
		public If_statementContext if_statement() {
			return getRuleContext(If_statementContext.class,0);
		}
		public For_statementContext for_statement() {
			return getRuleContext(For_statementContext.class,0);
		}
		public Structured_statementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_structured_statement; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PascalParserVisitor ) return ((PascalParserVisitor<? extends T>)visitor).visitStructured_statement(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Structured_statementContext structured_statement() throws RecognitionException {
		Structured_statementContext _localctx = new Structured_statementContext(_ctx, getState());
		enterRule(_localctx, 66, RULE_structured_statement);
		try {
			setState(288);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case IF:
				enterOuterAlt(_localctx, 1);
				{
				setState(286);
				if_statement();
				}
				break;
			case FOR:
				enterOuterAlt(_localctx, 2);
				{
				setState(287);
				for_statement();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class Statement_sequenceContext extends ParserRuleContext {
		public List<StatementContext> statement() {
			return getRuleContexts(StatementContext.class);
		}
		public StatementContext statement(int i) {
			return getRuleContext(StatementContext.class,i);
		}
		public List<TerminalNode> SEMICOLON() { return getTokens(PascalParser.SEMICOLON); }
		public TerminalNode SEMICOLON(int i) {
			return getToken(PascalParser.SEMICOLON, i);
		}
		public Statement_sequenceContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_statement_sequence; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PascalParserVisitor ) return ((PascalParserVisitor<? extends T>)visitor).visitStatement_sequence(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Statement_sequenceContext statement_sequence() throws RecognitionException {
		Statement_sequenceContext _localctx = new Statement_sequenceContext(_ctx, getState());
		enterRule(_localctx, 68, RULE_statement_sequence);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(290);
			statement();
			setState(295);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==SEMICOLON) {
				{
				{
				setState(291);
				match(SEMICOLON);
				setState(292);
				statement();
				}
				}
				setState(297);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class Compound_statementContext extends ParserRuleContext {
		public TerminalNode BEGIN() { return getToken(PascalParser.BEGIN, 0); }
		public Statement_sequenceContext statement_sequence() {
			return getRuleContext(Statement_sequenceContext.class,0);
		}
		public TerminalNode END() { return getToken(PascalParser.END, 0); }
		public Compound_statementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_compound_statement; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PascalParserVisitor ) return ((PascalParserVisitor<? extends T>)visitor).visitCompound_statement(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Compound_statementContext compound_statement() throws RecognitionException {
		Compound_statementContext _localctx = new Compound_statementContext(_ctx, getState());
		enterRule(_localctx, 70, RULE_compound_statement);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(298);
			match(BEGIN);
			setState(299);
			statement_sequence();
			setState(300);
			match(END);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class If_statementContext extends ParserRuleContext {
		public TerminalNode IF() { return getToken(PascalParser.IF, 0); }
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public TerminalNode THEN() { return getToken(PascalParser.THEN, 0); }
		public StatementContext statement() {
			return getRuleContext(StatementContext.class,0);
		}
		public Else_partContext else_part() {
			return getRuleContext(Else_partContext.class,0);
		}
		public If_statementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_if_statement; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PascalParserVisitor ) return ((PascalParserVisitor<? extends T>)visitor).visitIf_statement(this);
			else return visitor.visitChildren(this);
		}
	}

	public final If_statementContext if_statement() throws RecognitionException {
		If_statementContext _localctx = new If_statementContext(_ctx, getState());
		enterRule(_localctx, 72, RULE_if_statement);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(302);
			match(IF);
			setState(303);
			expression();
			setState(304);
			match(THEN);
			setState(305);
			statement();
			setState(307);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,26,_ctx) ) {
			case 1:
				{
				setState(306);
				else_part();
				}
				break;
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class Else_partContext extends ParserRuleContext {
		public TerminalNode ELSE() { return getToken(PascalParser.ELSE, 0); }
		public StatementContext statement() {
			return getRuleContext(StatementContext.class,0);
		}
		public Else_partContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_else_part; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PascalParserVisitor ) return ((PascalParserVisitor<? extends T>)visitor).visitElse_part(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Else_partContext else_part() throws RecognitionException {
		Else_partContext _localctx = new Else_partContext(_ctx, getState());
		enterRule(_localctx, 74, RULE_else_part);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(309);
			match(ELSE);
			setState(310);
			statement();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class For_statementContext extends ParserRuleContext {
		public TerminalNode FOR() { return getToken(PascalParser.FOR, 0); }
		public TerminalNode IDENTIFIER() { return getToken(PascalParser.IDENTIFIER, 0); }
		public TerminalNode ASSIGNMENT() { return getToken(PascalParser.ASSIGNMENT, 0); }
		public List<ExpressionContext> expression() {
			return getRuleContexts(ExpressionContext.class);
		}
		public ExpressionContext expression(int i) {
			return getRuleContext(ExpressionContext.class,i);
		}
		public TerminalNode DO() { return getToken(PascalParser.DO, 0); }
		public StatementContext statement() {
			return getRuleContext(StatementContext.class,0);
		}
		public TerminalNode TO() { return getToken(PascalParser.TO, 0); }
		public TerminalNode DOWNTO() { return getToken(PascalParser.DOWNTO, 0); }
		public For_statementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_for_statement; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PascalParserVisitor ) return ((PascalParserVisitor<? extends T>)visitor).visitFor_statement(this);
			else return visitor.visitChildren(this);
		}
	}

	public final For_statementContext for_statement() throws RecognitionException {
		For_statementContext _localctx = new For_statementContext(_ctx, getState());
		enterRule(_localctx, 76, RULE_for_statement);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(312);
			match(FOR);
			setState(313);
			match(IDENTIFIER);
			setState(314);
			match(ASSIGNMENT);
			setState(315);
			expression();
			setState(316);
			_la = _input.LA(1);
			if ( !(_la==DOWNTO || _la==TO) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			setState(317);
			expression();
			setState(318);
			match(DO);
			setState(319);
			statement();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ProgramContext extends ParserRuleContext {
		public Program_headingContext program_heading() {
			return getRuleContext(Program_headingContext.class,0);
		}
		public TerminalNode SEMICOLON() { return getToken(PascalParser.SEMICOLON, 0); }
		public BlockContext block() {
			return getRuleContext(BlockContext.class,0);
		}
		public TerminalNode PERIOD() { return getToken(PascalParser.PERIOD, 0); }
		public ProgramContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_program; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PascalParserVisitor ) return ((PascalParserVisitor<? extends T>)visitor).visitProgram(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ProgramContext program() throws RecognitionException {
		ProgramContext _localctx = new ProgramContext(_ctx, getState());
		enterRule(_localctx, 78, RULE_program);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(321);
			program_heading();
			setState(322);
			match(SEMICOLON);
			setState(323);
			block();
			setState(324);
			match(PERIOD);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class Program_headingContext extends ParserRuleContext {
		public TerminalNode PROGRAM() { return getToken(PascalParser.PROGRAM, 0); }
		public TerminalNode IDENTIFIER() { return getToken(PascalParser.IDENTIFIER, 0); }
		public Program_headingContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_program_heading; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PascalParserVisitor ) return ((PascalParserVisitor<? extends T>)visitor).visitProgram_heading(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Program_headingContext program_heading() throws RecognitionException {
		Program_headingContext _localctx = new Program_headingContext(_ctx, getState());
		enterRule(_localctx, 80, RULE_program_heading);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(326);
			match(PROGRAM);
			setState(327);
			match(IDENTIFIER);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static final String _serializedATN =
		"\u0004\u00013\u014a\u0002\u0000\u0007\u0000\u0002\u0001\u0007\u0001\u0002"+
		"\u0002\u0007\u0002\u0002\u0003\u0007\u0003\u0002\u0004\u0007\u0004\u0002"+
		"\u0005\u0007\u0005\u0002\u0006\u0007\u0006\u0002\u0007\u0007\u0007\u0002"+
		"\b\u0007\b\u0002\t\u0007\t\u0002\n\u0007\n\u0002\u000b\u0007\u000b\u0002"+
		"\f\u0007\f\u0002\r\u0007\r\u0002\u000e\u0007\u000e\u0002\u000f\u0007\u000f"+
		"\u0002\u0010\u0007\u0010\u0002\u0011\u0007\u0011\u0002\u0012\u0007\u0012"+
		"\u0002\u0013\u0007\u0013\u0002\u0014\u0007\u0014\u0002\u0015\u0007\u0015"+
		"\u0002\u0016\u0007\u0016\u0002\u0017\u0007\u0017\u0002\u0018\u0007\u0018"+
		"\u0002\u0019\u0007\u0019\u0002\u001a\u0007\u001a\u0002\u001b\u0007\u001b"+
		"\u0002\u001c\u0007\u001c\u0002\u001d\u0007\u001d\u0002\u001e\u0007\u001e"+
		"\u0002\u001f\u0007\u001f\u0002 \u0007 \u0002!\u0007!\u0002\"\u0007\"\u0002"+
		"#\u0007#\u0002$\u0007$\u0002%\u0007%\u0002&\u0007&\u0002\'\u0007\'\u0002"+
		"(\u0007(\u0001\u0000\u0001\u0000\u0001\u0000\u0001\u0000\u0001\u0001\u0001"+
		"\u0001\u0001\u0001\u0001\u0001\u0005\u0001[\b\u0001\n\u0001\f\u0001^\t"+
		"\u0001\u0001\u0001\u0001\u0001\u0005\u0001b\b\u0001\n\u0001\f\u0001e\t"+
		"\u0001\u0001\u0002\u0001\u0002\u0003\u0002i\b\u0002\u0001\u0002\u0001"+
		"\u0002\u0005\u0002m\b\u0002\n\u0002\f\u0002p\t\u0002\u0001\u0003\u0001"+
		"\u0003\u0001\u0004\u0001\u0004\u0003\u0004v\b\u0004\u0001\u0005\u0001"+
		"\u0005\u0001\u0005\u0005\u0005{\b\u0005\n\u0005\f\u0005~\t\u0005\u0001"+
		"\u0006\u0001\u0006\u0001\u0006\u0001\u0006\u0001\u0007\u0003\u0007\u0085"+
		"\b\u0007\u0001\u0007\u0001\u0007\u0001\u0007\u0001\u0007\u0001\u0007\u0001"+
		"\u0007\u0001\u0007\u0001\b\u0001\b\u0001\b\u0001\b\u0001\t\u0001\t\u0003"+
		"\t\u0094\b\t\u0001\n\u0001\n\u0001\n\u0001\n\u0001\n\u0005\n\u009b\b\n"+
		"\n\n\f\n\u009e\t\n\u0001\n\u0001\n\u0001\u000b\u0001\u000b\u0001\u000b"+
		"\u0001\f\u0001\f\u0001\f\u0003\f\u00a8\b\f\u0001\f\u0001\f\u0001\f\u0001"+
		"\r\u0001\r\u0001\r\u0001\u000e\u0001\u000e\u0001\u000e\u0003\u000e\u00b3"+
		"\b\u000e\u0001\u000e\u0001\u000e\u0001\u000e\u0001\u000e\u0001\u000e\u0001"+
		"\u000f\u0001\u000f\u0001\u000f\u0001\u000f\u0005\u000f\u00be\b\u000f\n"+
		"\u000f\f\u000f\u00c1\t\u000f\u0003\u000f\u00c3\b\u000f\u0001\u000f\u0001"+
		"\u000f\u0001\u0010\u0001\u0010\u0001\u0010\u0001\u0010\u0001\u0011\u0001"+
		"\u0011\u0001\u0011\u0001\u0011\u0003\u0011\u00cf\b\u0011\u0001\u0012\u0003"+
		"\u0012\u00d2\b\u0012\u0001\u0012\u0001\u0012\u0001\u0012\u0001\u0012\u0005"+
		"\u0012\u00d8\b\u0012\n\u0012\f\u0012\u00db\t\u0012\u0001\u0013\u0001\u0013"+
		"\u0001\u0013\u0001\u0013\u0005\u0013\u00e1\b\u0013\n\u0013\f\u0013\u00e4"+
		"\t\u0013\u0001\u0014\u0001\u0014\u0001\u0014\u0001\u0014\u0001\u0014\u0001"+
		"\u0014\u0001\u0014\u0001\u0014\u0001\u0014\u0003\u0014\u00ef\b\u0014\u0001"+
		"\u0015\u0001\u0015\u0001\u0016\u0001\u0016\u0001\u0017\u0001\u0017\u0001"+
		"\u0018\u0001\u0018\u0001\u0019\u0001\u0019\u0003\u0019\u00fb\b\u0019\u0001"+
		"\u001a\u0001\u001a\u0001\u001a\u0001\u001a\u0005\u001a\u0101\b\u001a\n"+
		"\u001a\f\u001a\u0104\t\u001a\u0001\u001a\u0001\u001a\u0001\u001b\u0001"+
		"\u001b\u0003\u001b\u010a\b\u001b\u0001\u001c\u0001\u001c\u0003\u001c\u010e"+
		"\b\u001c\u0001\u001d\u0001\u001d\u0001\u001d\u0003\u001d\u0113\b\u001d"+
		"\u0001\u001e\u0001\u001e\u0001\u001f\u0001\u001f\u0001\u001f\u0001\u001f"+
		"\u0001 \u0001 \u0003 \u011d\b \u0001!\u0001!\u0003!\u0121\b!\u0001\"\u0001"+
		"\"\u0001\"\u0005\"\u0126\b\"\n\"\f\"\u0129\t\"\u0001#\u0001#\u0001#\u0001"+
		"#\u0001$\u0001$\u0001$\u0001$\u0001$\u0003$\u0134\b$\u0001%\u0001%\u0001"+
		"%\u0001&\u0001&\u0001&\u0001&\u0001&\u0001&\u0001&\u0001&\u0001&\u0001"+
		"\'\u0001\'\u0001\'\u0001\'\u0001\'\u0001(\u0001(\u0001(\u0001(\u0000\u0000"+
		")\u0000\u0002\u0004\u0006\b\n\f\u000e\u0010\u0012\u0014\u0016\u0018\u001a"+
		"\u001c\u001e \"$&(*,.02468:<>@BDFHJLNP\u0000\u0007\u0001\u0000)-\u0001"+
		"\u0000\u0006\u0007\u0001\u0000.0\u0002\u0000\t\n\u0015\u0015\u0002\u0000"+
		"\u0006\u0007!!\u0003\u0000\u0002\u0004\b\b\u000b\f\u0002\u0000\u0019\u0019"+
		"&&\u013f\u0000R\u0001\u0000\u0000\u0000\u0002c\u0001\u0000\u0000\u0000"+
		"\u0004n\u0001\u0000\u0000\u0000\u0006q\u0001\u0000\u0000\u0000\bu\u0001"+
		"\u0000\u0000\u0000\nw\u0001\u0000\u0000\u0000\f\u007f\u0001\u0000\u0000"+
		"\u0000\u000e\u0084\u0001\u0000\u0000\u0000\u0010\u008d\u0001\u0000\u0000"+
		"\u0000\u0012\u0093\u0001\u0000\u0000\u0000\u0014\u0095\u0001\u0000\u0000"+
		"\u0000\u0016\u00a1\u0001\u0000\u0000\u0000\u0018\u00a4\u0001\u0000\u0000"+
		"\u0000\u001a\u00ac\u0001\u0000\u0000\u0000\u001c\u00af\u0001\u0000\u0000"+
		"\u0000\u001e\u00b9\u0001\u0000\u0000\u0000 \u00c6\u0001\u0000\u0000\u0000"+
		"\"\u00ca\u0001\u0000\u0000\u0000$\u00d1\u0001\u0000\u0000\u0000&\u00dc"+
		"\u0001\u0000\u0000\u0000(\u00ee\u0001\u0000\u0000\u0000*\u00f0\u0001\u0000"+
		"\u0000\u0000,\u00f2\u0001\u0000\u0000\u0000.\u00f4\u0001\u0000\u0000\u0000"+
		"0\u00f6\u0001\u0000\u0000\u00002\u00f8\u0001\u0000\u0000\u00004\u00fc"+
		"\u0001\u0000\u0000\u00006\u0109\u0001\u0000\u0000\u00008\u010d\u0001\u0000"+
		"\u0000\u0000:\u0112\u0001\u0000\u0000\u0000<\u0114\u0001\u0000\u0000\u0000"+
		">\u0116\u0001\u0000\u0000\u0000@\u011a\u0001\u0000\u0000\u0000B\u0120"+
		"\u0001\u0000\u0000\u0000D\u0122\u0001\u0000\u0000\u0000F\u012a\u0001\u0000"+
		"\u0000\u0000H\u012e\u0001\u0000\u0000\u0000J\u0135\u0001\u0000\u0000\u0000"+
		"L\u0138\u0001\u0000\u0000\u0000N\u0141\u0001\u0000\u0000\u0000P\u0146"+
		"\u0001\u0000\u0000\u0000RS\u0003\u0002\u0001\u0000ST\u0003\u0004\u0002"+
		"\u0000TU\u0003F#\u0000U\u0001\u0001\u0000\u0000\u0000VW\u0005(\u0000\u0000"+
		"W\\\u0003\u0010\b\u0000XY\u0005\u0012\u0000\u0000Y[\u0003\u0010\b\u0000"+
		"ZX\u0001\u0000\u0000\u0000[^\u0001\u0000\u0000\u0000\\Z\u0001\u0000\u0000"+
		"\u0000\\]\u0001\u0000\u0000\u0000]_\u0001\u0000\u0000\u0000^\\\u0001\u0000"+
		"\u0000\u0000_`\u0005\u0012\u0000\u0000`b\u0001\u0000\u0000\u0000aV\u0001"+
		"\u0000\u0000\u0000be\u0001\u0000\u0000\u0000ca\u0001\u0000\u0000\u0000"+
		"cd\u0001\u0000\u0000\u0000d\u0003\u0001\u0000\u0000\u0000ec\u0001\u0000"+
		"\u0000\u0000fi\u0003\u0016\u000b\u0000gi\u0003\u001a\r\u0000hf\u0001\u0000"+
		"\u0000\u0000hg\u0001\u0000\u0000\u0000ij\u0001\u0000\u0000\u0000jk\u0005"+
		"\u0012\u0000\u0000km\u0001\u0000\u0000\u0000lh\u0001\u0000\u0000\u0000"+
		"mp\u0001\u0000\u0000\u0000nl\u0001\u0000\u0000\u0000no\u0001\u0000\u0000"+
		"\u0000o\u0005\u0001\u0000\u0000\u0000pn\u0001\u0000\u0000\u0000qr\u0007"+
		"\u0000\u0000\u0000r\u0007\u0001\u0000\u0000\u0000sv\u0003\u0006\u0003"+
		"\u0000tv\u0003\u000e\u0007\u0000us\u0001\u0000\u0000\u0000ut\u0001\u0000"+
		"\u0000\u0000v\t\u0001\u0000\u0000\u0000w|\u00052\u0000\u0000xy\u0005\u0010"+
		"\u0000\u0000y{\u00052\u0000\u0000zx\u0001\u0000\u0000\u0000{~\u0001\u0000"+
		"\u0000\u0000|z\u0001\u0000\u0000\u0000|}\u0001\u0000\u0000\u0000}\u000b"+
		"\u0001\u0000\u0000\u0000~|\u0001\u0000\u0000\u0000\u007f\u0080\u0005."+
		"\u0000\u0000\u0080\u0081\u0005\u0005\u0000\u0000\u0081\u0082\u0005.\u0000"+
		"\u0000\u0082\r\u0001\u0000\u0000\u0000\u0083\u0085\u0005\"\u0000\u0000"+
		"\u0084\u0083\u0001\u0000\u0000\u0000\u0084\u0085\u0001\u0000\u0000\u0000"+
		"\u0085\u0086\u0001\u0000\u0000\u0000\u0086\u0087\u0005\u0016\u0000\u0000"+
		"\u0087\u0088\u0005\r\u0000\u0000\u0088\u0089\u0003\f\u0006\u0000\u0089"+
		"\u008a\u0005\u000e\u0000\u0000\u008a\u008b\u0005 \u0000\u0000\u008b\u008c"+
		"\u0003\u0006\u0003\u0000\u008c\u000f\u0001\u0000\u0000\u0000\u008d\u008e"+
		"\u0003\n\u0005\u0000\u008e\u008f\u0005\u0011\u0000\u0000\u008f\u0090\u0003"+
		"\b\u0004\u0000\u0090\u0011\u0001\u0000\u0000\u0000\u0091\u0094\u00052"+
		"\u0000\u0000\u0092\u0094\u0003\u0014\n\u0000\u0093\u0091\u0001\u0000\u0000"+
		"\u0000\u0093\u0092\u0001\u0000\u0000\u0000\u0094\u0013\u0001\u0000\u0000"+
		"\u0000\u0095\u0096\u00052\u0000\u0000\u0096\u0097\u0005\r\u0000\u0000"+
		"\u0097\u009c\u0003\"\u0011\u0000\u0098\u0099\u0005\u0010\u0000\u0000\u0099"+
		"\u009b\u0003\"\u0011\u0000\u009a\u0098\u0001\u0000\u0000\u0000\u009b\u009e"+
		"\u0001\u0000\u0000\u0000\u009c\u009a\u0001\u0000\u0000\u0000\u009c\u009d"+
		"\u0001\u0000\u0000\u0000\u009d\u009f\u0001\u0000\u0000\u0000\u009e\u009c"+
		"\u0001\u0000\u0000\u0000\u009f\u00a0\u0005\u000e\u0000\u0000\u00a0\u0015"+
		"\u0001\u0000\u0000\u0000\u00a1\u00a2\u0003\u0018\f\u0000\u00a2\u00a3\u0003"+
		"F#\u0000\u00a3\u0017\u0001\u0000\u0000\u0000\u00a4\u00a5\u0005#\u0000"+
		"\u0000\u00a5\u00a7\u00052\u0000\u0000\u00a6\u00a8\u0003\u001e\u000f\u0000"+
		"\u00a7\u00a6\u0001\u0000\u0000\u0000\u00a7\u00a8\u0001\u0000\u0000\u0000"+
		"\u00a8\u00a9\u0001\u0000\u0000\u0000\u00a9\u00aa\u0005\u0012\u0000\u0000"+
		"\u00aa\u00ab\u0003\u0002\u0001\u0000\u00ab\u0019\u0001\u0000\u0000\u0000"+
		"\u00ac\u00ad\u0003\u001c\u000e\u0000\u00ad\u00ae\u0003F#\u0000\u00ae\u001b"+
		"\u0001\u0000\u0000\u0000\u00af\u00b0\u0005\u001d\u0000\u0000\u00b0\u00b2"+
		"\u00052\u0000\u0000\u00b1\u00b3\u0003\u001e\u000f\u0000\u00b2\u00b1\u0001"+
		"\u0000\u0000\u0000\u00b2\u00b3\u0001\u0000\u0000\u0000\u00b3\u00b4\u0001"+
		"\u0000\u0000\u0000\u00b4\u00b5\u0005\u0011\u0000\u0000\u00b5\u00b6\u0003"+
		"\b\u0004\u0000\u00b6\u00b7\u0005\u0012\u0000\u0000\u00b7\u00b8\u0003\u0002"+
		"\u0001\u0000\u00b8\u001d\u0001\u0000\u0000\u0000\u00b9\u00c2\u0005\u0013"+
		"\u0000\u0000\u00ba\u00bf\u0003 \u0010\u0000\u00bb\u00bc\u0005\u0012\u0000"+
		"\u0000\u00bc\u00be\u0003 \u0010\u0000\u00bd\u00bb\u0001\u0000\u0000\u0000"+
		"\u00be\u00c1\u0001\u0000\u0000\u0000\u00bf\u00bd\u0001\u0000\u0000\u0000"+
		"\u00bf\u00c0\u0001\u0000\u0000\u0000\u00c0\u00c3\u0001\u0000\u0000\u0000"+
		"\u00c1\u00bf\u0001\u0000\u0000\u0000\u00c2\u00ba\u0001\u0000\u0000\u0000"+
		"\u00c2\u00c3\u0001\u0000\u0000\u0000\u00c3\u00c4\u0001\u0000\u0000\u0000"+
		"\u00c4\u00c5\u0005\u0014\u0000\u0000\u00c5\u001f\u0001\u0000\u0000\u0000"+
		"\u00c6\u00c7\u0003\n\u0005\u0000\u00c7\u00c8\u0005\u0011\u0000\u0000\u00c8"+
		"\u00c9\u0003\b\u0004\u0000\u00c9!\u0001\u0000\u0000\u0000\u00ca\u00ce"+
		"\u0003$\u0012\u0000\u00cb\u00cc\u00030\u0018\u0000\u00cc\u00cd\u0003$"+
		"\u0012\u0000\u00cd\u00cf\u0001\u0000\u0000\u0000\u00ce\u00cb\u0001\u0000"+
		"\u0000\u0000\u00ce\u00cf\u0001\u0000\u0000\u0000\u00cf#\u0001\u0000\u0000"+
		"\u0000\u00d0\u00d2\u0007\u0001\u0000\u0000\u00d1\u00d0\u0001\u0000\u0000"+
		"\u0000\u00d1\u00d2\u0001\u0000\u0000\u0000\u00d2\u00d3\u0001\u0000\u0000"+
		"\u0000\u00d3\u00d9\u0003&\u0013\u0000\u00d4\u00d5\u0003.\u0017\u0000\u00d5"+
		"\u00d6\u0003&\u0013\u0000\u00d6\u00d8\u0001\u0000\u0000\u0000\u00d7\u00d4"+
		"\u0001\u0000\u0000\u0000\u00d8\u00db\u0001\u0000\u0000\u0000\u00d9\u00d7"+
		"\u0001\u0000\u0000\u0000\u00d9\u00da\u0001\u0000\u0000\u0000\u00da%\u0001"+
		"\u0000\u0000\u0000\u00db\u00d9\u0001\u0000\u0000\u0000\u00dc\u00e2\u0003"+
		"(\u0014\u0000\u00dd\u00de\u0003,\u0016\u0000\u00de\u00df\u0003(\u0014"+
		"\u0000\u00df\u00e1\u0001\u0000\u0000\u0000\u00e0\u00dd\u0001\u0000\u0000"+
		"\u0000\u00e1\u00e4\u0001\u0000\u0000\u0000\u00e2\u00e0\u0001\u0000\u0000"+
		"\u0000\u00e2\u00e3\u0001\u0000\u0000\u0000\u00e3\'\u0001\u0000\u0000\u0000"+
		"\u00e4\u00e2\u0001\u0000\u0000\u0000\u00e5\u00ef\u0003\u0012\t\u0000\u00e6"+
		"\u00ef\u0003*\u0015\u0000\u00e7\u00ef\u00032\u0019\u0000\u00e8\u00e9\u0005"+
		"\u0013\u0000\u0000\u00e9\u00ea\u0003\"\u0011\u0000\u00ea\u00eb\u0005\u0014"+
		"\u0000\u0000\u00eb\u00ef\u0001\u0000\u0000\u0000\u00ec\u00ed\u0005\u001f"+
		"\u0000\u0000\u00ed\u00ef\u0003(\u0014\u0000\u00ee\u00e5\u0001\u0000\u0000"+
		"\u0000\u00ee\u00e6\u0001\u0000\u0000\u0000\u00ee\u00e7\u0001\u0000\u0000"+
		"\u0000\u00ee\u00e8\u0001\u0000\u0000\u0000\u00ee\u00ec\u0001\u0000\u0000"+
		"\u0000\u00ef)\u0001\u0000\u0000\u0000\u00f0\u00f1\u0007\u0002\u0000\u0000"+
		"\u00f1+\u0001\u0000\u0000\u0000\u00f2\u00f3\u0007\u0003\u0000\u0000\u00f3"+
		"-\u0001\u0000\u0000\u0000\u00f4\u00f5\u0007\u0004\u0000\u0000\u00f5/\u0001"+
		"\u0000\u0000\u0000\u00f6\u00f7\u0007\u0005\u0000\u0000\u00f71\u0001\u0000"+
		"\u0000\u0000\u00f8\u00fa\u00052\u0000\u0000\u00f9\u00fb\u00034\u001a\u0000"+
		"\u00fa\u00f9\u0001\u0000\u0000\u0000\u00fa\u00fb\u0001\u0000\u0000\u0000"+
		"\u00fb3\u0001\u0000\u0000\u0000\u00fc\u00fd\u0005\u0013\u0000\u0000\u00fd"+
		"\u0102\u00036\u001b\u0000\u00fe\u00ff\u0005\u0010\u0000\u0000\u00ff\u0101"+
		"\u00036\u001b\u0000\u0100\u00fe\u0001\u0000\u0000\u0000\u0101\u0104\u0001"+
		"\u0000\u0000\u0000\u0102\u0100\u0001\u0000\u0000\u0000\u0102\u0103\u0001"+
		"\u0000\u0000\u0000\u0103\u0105\u0001\u0000\u0000\u0000\u0104\u0102\u0001"+
		"\u0000\u0000\u0000\u0105\u0106\u0005\u0014\u0000\u0000\u01065\u0001\u0000"+
		"\u0000\u0000\u0107\u010a\u0003\"\u0011\u0000\u0108\u010a\u0003\u0012\t"+
		"\u0000\u0109\u0107\u0001\u0000\u0000\u0000\u0109\u0108\u0001\u0000\u0000"+
		"\u0000\u010a7\u0001\u0000\u0000\u0000\u010b\u010e\u0003:\u001d\u0000\u010c"+
		"\u010e\u0003B!\u0000\u010d\u010b\u0001\u0000\u0000\u0000\u010d\u010c\u0001"+
		"\u0000\u0000\u0000\u010e9\u0001\u0000\u0000\u0000\u010f\u0113\u0003<\u001e"+
		"\u0000\u0110\u0113\u0003>\u001f\u0000\u0111\u0113\u0003@ \u0000\u0112"+
		"\u010f\u0001\u0000\u0000\u0000\u0112\u0110\u0001\u0000\u0000\u0000\u0112"+
		"\u0111\u0001\u0000\u0000\u0000\u0113;\u0001\u0000\u0000\u0000\u0114\u0115"+
		"\u0001\u0000\u0000\u0000\u0115=\u0001\u0000\u0000\u0000\u0116\u0117\u0003"+
		"\u0012\t\u0000\u0117\u0118\u0005\u0001\u0000\u0000\u0118\u0119\u0003\""+
		"\u0011\u0000\u0119?\u0001\u0000\u0000\u0000\u011a\u011c\u00052\u0000\u0000"+
		"\u011b\u011d\u00034\u001a\u0000\u011c\u011b\u0001\u0000\u0000\u0000\u011c"+
		"\u011d\u0001\u0000\u0000\u0000\u011dA\u0001\u0000\u0000\u0000\u011e\u0121"+
		"\u0003H$\u0000\u011f\u0121\u0003L&\u0000\u0120\u011e\u0001\u0000\u0000"+
		"\u0000\u0120\u011f\u0001\u0000\u0000\u0000\u0121C\u0001\u0000\u0000\u0000"+
		"\u0122\u0127\u00038\u001c\u0000\u0123\u0124\u0005\u0012\u0000\u0000\u0124"+
		"\u0126\u00038\u001c\u0000\u0125\u0123\u0001\u0000\u0000\u0000\u0126\u0129"+
		"\u0001\u0000\u0000\u0000\u0127\u0125\u0001\u0000\u0000\u0000\u0127\u0128"+
		"\u0001\u0000\u0000\u0000\u0128E\u0001\u0000\u0000\u0000\u0129\u0127\u0001"+
		"\u0000\u0000\u0000\u012a\u012b\u0005\u0017\u0000\u0000\u012b\u012c\u0003"+
		"D\"\u0000\u012c\u012d\u0005\u001b\u0000\u0000\u012dG\u0001\u0000\u0000"+
		"\u0000\u012e\u012f\u0005\u001e\u0000\u0000\u012f\u0130\u0003\"\u0011\u0000"+
		"\u0130\u0131\u0005%\u0000\u0000\u0131\u0133\u00038\u001c\u0000\u0132\u0134"+
		"\u0003J%\u0000\u0133\u0132\u0001\u0000\u0000\u0000\u0133\u0134\u0001\u0000"+
		"\u0000\u0000\u0134I\u0001\u0000\u0000\u0000\u0135\u0136\u0005\u001a\u0000"+
		"\u0000\u0136\u0137\u00038\u001c\u0000\u0137K\u0001\u0000\u0000\u0000\u0138"+
		"\u0139\u0005\u001c\u0000\u0000\u0139\u013a\u00052\u0000\u0000\u013a\u013b"+
		"\u0005\u0001\u0000\u0000\u013b\u013c\u0003\"\u0011\u0000\u013c\u013d\u0007"+
		"\u0006\u0000\u0000\u013d\u013e\u0003\"\u0011\u0000\u013e\u013f\u0005\u0018"+
		"\u0000\u0000\u013f\u0140\u00038\u001c\u0000\u0140M\u0001\u0000\u0000\u0000"+
		"\u0141\u0142\u0003P(\u0000\u0142\u0143\u0005\u0012\u0000\u0000\u0143\u0144"+
		"\u0003\u0000\u0000\u0000\u0144\u0145\u0005\u000f\u0000\u0000\u0145O\u0001"+
		"\u0000\u0000\u0000\u0146\u0147\u0005$\u0000\u0000\u0147\u0148\u00052\u0000"+
		"\u0000\u0148Q\u0001\u0000\u0000\u0000\u001b\\chnu|\u0084\u0093\u009c\u00a7"+
		"\u00b2\u00bf\u00c2\u00ce\u00d1\u00d9\u00e2\u00ee\u00fa\u0102\u0109\u010d"+
		"\u0112\u011c\u0120\u0127\u0133";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}