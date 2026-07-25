package com.example.commerce.common;
import java.time.Instant; import java.util.*; import java.util.stream.Collectors;
import org.springframework.http.*; import org.springframework.security.access.AccessDeniedException; import org.springframework.web.bind.MethodArgumentNotValidException; import org.springframework.web.bind.annotation.*;
@RestControllerAdvice
public class ApiErrorHandler {
 @ExceptionHandler(CommerceException.class) ResponseEntity<Map<String,Object>> business(CommerceException e){return response(e.getStatus(),e.getMessage(),null);}
 @ExceptionHandler(MethodArgumentNotValidException.class) ResponseEntity<Map<String,Object>> validation(MethodArgumentNotValidException e){Map<String,String> fields=e.getBindingResult().getFieldErrors().stream().collect(Collectors.toMap(x->x.getField(),x->x.getDefaultMessage(),(a,b)->a,LinkedHashMap::new));return response(HttpStatus.BAD_REQUEST,"Request validation failed",fields);}
 @ExceptionHandler(AccessDeniedException.class) ResponseEntity<Map<String,Object>> denied(){return response(HttpStatus.FORBIDDEN,"Access denied",null);}
 private ResponseEntity<Map<String,Object>> response(HttpStatus s,String m,Object d){Map<String,Object> b=new LinkedHashMap<>();b.put("timestamp",Instant.now());b.put("status",s.value());b.put("message",m);if(d!=null)b.put("errors",d);return ResponseEntity.status(s).body(b);}
}
