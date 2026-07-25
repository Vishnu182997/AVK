package com.example.appointment.common;
import org.springframework.http.HttpStatus;
public class DomainException extends RuntimeException {
  private final String code;
  private final HttpStatus status;
  public DomainException(HttpStatus s, String c, String m) {
    super(m);
    status = s;
    code = c;
  }
  public String getCode() {
    return code;
  }
  public HttpStatus getStatus() {
    return status;
  }
  public static DomainException notFound(String m) {
    return new DomainException(HttpStatus.NOT_FOUND, "RESOURCE_NOT_FOUND", m);
  }
  public static DomainException conflict(String c, String m) {
    return new DomainException(HttpStatus.CONFLICT, c, m);
  }
  public static DomainException bad(String c, String m) {
    return new DomainException(HttpStatus.BAD_REQUEST, c, m);
  }
}
