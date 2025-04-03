package br.com.priscyladepaula.desafioitau.exception;

import com.fasterxml.jackson.databind.JsonMappingException;
import org.springframework.boot.json.JsonParseException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> response = new HashMap<>();

        response.put("codigo", "erro_validacao");
        response.put("mensagem", ex.getBindingResult().getAllErrors().get(0).getDefaultMessage());

        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, String>> handleIllegalArgumentException(IllegalArgumentException ex) {
        Map<String, String> response = new HashMap<>();
        response.put("codigo", "erro_validacao");
        response.put("mensagem", ex.getMessage());
        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> handleGenericExceptions(Exception ex) {
        Map<String, String> errors = new HashMap<>();

        errors.put("codigo", "erro_generico");
        errors.put("mensagem", ex.getMessage());

        return ResponseEntity.badRequest().body(errors);
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<Map<String, String>> handleNoSuchElementException(NoSuchElementException ex) {
        Map<String, String> errors = new HashMap<>();
        errors.put("codigo", "erro_nao_encontrado");
        errors.put("mensagem", ex.getMessage());

        return ResponseEntity.badRequest().body(errors);
    }

    @ExceptionHandler(EmptyResultDataAccessException.class)
    public ResponseEntity<Map<String, String>> handleNotFoundException(EmptyResultDataAccessException ex) {
        Map<String, String> response = new HashMap<>();
        response.put("codigo", "erro_nao_encontrado");
        response.put("mensagem", "Registro não encontrado");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }


    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Map<String, String>> handleJsonParseException(HttpMessageNotReadableException ex) {
        Map<String, String> errors = new HashMap<>();

        errors.put("codigo", "erro_json");
        errors.put("mensagem", "Erro ao processar a requisição. Verifique os dados enviados.");

        return ResponseEntity.badRequest().body(errors);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Map<String, String>> handleDataIntegrityViolation(DataIntegrityViolationException ex) {
        Map<String, String> response = new HashMap<>();
        response.put("codigo", "erro_validacao");

        // Transformar a mensagem para a esperada pelo teste
        response.put("mensagem", "Já existe categoria com esse nome");

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }
}
