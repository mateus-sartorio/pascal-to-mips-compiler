package webapp.dto;

public record CompilationIssue(String stage, int line, int column, String message) {
}