package br.com.priscyladepaula.desafioitau.handler;

import br.com.priscyladepaula.desafioitau.dto.ErroDTO;
import br.com.priscyladepaula.desafioitau.exception.CustomException;
import br.com.priscyladepaula.desafioitau.exception.NotFoundException;
import br.com.priscyladepaula.desafioitau.exception.ValidationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ErroDTO> tratarErroGeral(CustomException e) {

        ErroDTO err = new ErroDTO("erro_interno", e.getMessage());

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(err);
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ErroDTO> tratarErroValidacao(ValidationException ex) {

        ErroDTO err = new ErroDTO("erro_validacao", ex.getMessage());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(err);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErroDTO> tratarErroNaoEncontrado(NotFoundException ex) {

        ErroDTO err = new ErroDTO("erro_nao_encontrado", ex.getMessage());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(err);
    }
}
