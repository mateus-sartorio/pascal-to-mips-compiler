package webapp.exception;

import webapp.dto.CompilationIssue;

public class CompilationStageException extends CompilerException {
  private final CompilationIssue issue;

  public CompilationStageException(CompilationIssue issue) {
    super(issue.message());
    this.issue = issue;
  }

  public CompilationIssue issue() {
    return issue;
  }
}