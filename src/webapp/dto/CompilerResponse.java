package webapp.dto;

import java.util.List;

public record CompilerResponse(
  boolean success,
  List<TokenDto> tokens,
  String parseTree,
  String parseTreeSvg,
  String astDot,
  String astPng,
  String mipsCode,
  SymbolTablesDto symbolTables,
  ExecutionDto execution,
  List<CompilationIssue> issues
) {
}