package webapp.service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import webapp.dto.ReferenceResponse;

@Service
public class ReferenceService {
  public ReferenceResponse loadReference() {
    return new ReferenceResponse(
      readResource("reference/pascal-lexer.g4"),
      readResource("reference/pascal-parser.g4"),
      readResource("reference/syntax-rules.txt")
    );
  }

  private String readResource(String path) {
    try {
      return new String(new ClassPathResource(path).getInputStream().readAllBytes(), StandardCharsets.UTF_8);
    } catch (IOException exception) {
      throw new IllegalStateException("Unable to load reference content: " + path, exception);
    }
  }
}