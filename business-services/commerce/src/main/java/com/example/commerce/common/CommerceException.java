package com.example.commerce.common;
import org.springframework.http.HttpStatus;
public class CommerceException extends RuntimeException {
  private final HttpStatus status;
  public CommerceException(HttpStatus s, String m) {
    super(m);
    status = s;
  }
  public HttpStatus getStatus() {
    return status;
  }
}
