package webapp.controller;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import webapp.dto.CompilerRequest;
import webapp.dto.CompilerResponse;
import webapp.dto.ReferenceResponse;
import webapp.service.CompilerService;
import webapp.service.ReferenceService;

@RestController
public class CompilerController {
  private final CompilerService compilerService;
  private final ReferenceService referenceService;

  public CompilerController(CompilerService compilerService, ReferenceService referenceService) {
    this.compilerService = compilerService;
    this.referenceService = referenceService;
  }

  @PostMapping(value = "/api/compile", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  public CompilerResponse compile(@RequestBody CompilerRequest request) {
    return compilerService.compile(request);
  }

  @GetMapping(value = "/api/reference", produces = MediaType.APPLICATION_JSON_VALUE)
  public ReferenceResponse reference() {
    return referenceService.loadReference();
  }
}