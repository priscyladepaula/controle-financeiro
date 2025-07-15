package br.com.priscyladepaula.desafioitau.handler;

import br.com.priscyladepaula.desafioitau.dto.ErroDTO;
import br.com.priscyladepaula.desafioitau.exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

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

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(err);
    }

    @ExceptionHandler(InvalidOperationException.class)
    public ResponseEntity<ErroDTO> tratarErroOperacaoInvalida(InvalidOperationException ex) {

        ErroDTO err = new ErroDTO("erro_operacao_invalida", ex.getMessage());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(err);
    }

    @ExceptionHandler(DuplicationException.class)
    public ResponseEntity<ErroDTO> tratarErroDuplicidade(DuplicationException ex) {

        ErroDTO err = new ErroDTO("erro_duplicidade", ex.getMessage());

        return ResponseEntity.status(HttpStatus.CONFLICT).body(err);
    }

}
