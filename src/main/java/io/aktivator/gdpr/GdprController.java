package io.aktivator.gdpr;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/public/gdpr")
public class GdprController {

  @GetMapping("/conditions")
  public ResponseEntity<String> getConditions() throws IOException {
    return new ResponseEntity<>(getContents("gdpr/Conditions.txt"), HttpStatus.OK);
  }

  private String getContents(String path) throws IOException {
    Resource resource = new ClassPathResource(path);
    return new BufferedReader(
            new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8))
        .lines()
        .collect(Collectors.joining("\n"));
  }

  @GetMapping("/privacy")
  public ResponseEntity<String> getPrivacy() throws IOException {
    return new ResponseEntity<>(getContents("gdpr/Privacy.txt"), HttpStatus.OK);
  }
}
