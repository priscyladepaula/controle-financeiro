package br.com.priscyladepaula.desafioitau.exception;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.Collections;
import java.util.Map;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class GlobalExceptionHandlerTest {

    @InjectMocks
    private GlobalExceptionHandler globalExceptionHandler;

    @Mock
    private MethodArgumentNotValidException methodArgumentNotValidException;

    @Mock
    private BindingResult bindingResult;

    @Mock
    private FieldError fieldError;

    @Test
    @DisplayName("Deve tratar MethodArgumentNotValidException corretamente")
    void deveTratatMethodArgumentNotValidException() {
        // Arrange
        when(methodArgumentNotValidException.getBindingResult()).thenReturn(bindingResult);
        when(bindingResult.getAllErrors()).thenReturn(Collections.singletonList(fieldError));
        when(fieldError.getDefaultMessage()).thenReturn("Campo obrigatório");

        // Act
        ResponseEntity<Map<String, String>> response = globalExceptionHandler.handleValidationExceptions(methodArgumentNotValidException);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

        Map<String, String> responseBody = response.getBody();
        assertNotNull(responseBody);
        assertEquals("erro_validacao", responseBody.get("codigo"));
        assertEquals("Campo obrigatório", responseBody.get("mensagem"));
    }

    @Test
    @DisplayName("Deve tratar IllegalArgumentException corretamente")
    void deveTratatIllegalArgumentException() {
        // Arrange
        IllegalArgumentException ex = new IllegalArgumentException("Argumento inválido");

        // Act
        ResponseEntity<Map<String, String>> response = globalExceptionHandler.handleIllegalArgumentException(ex);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

        Map<String, String> responseBody = response.getBody();
        assertNotNull(responseBody);
        assertEquals("erro_validacao", responseBody.get("codigo"));
        assertEquals("Argumento inválido", responseBody.get("mensagem"));
    }

    @Test
    @DisplayName("Deve tratar Exception genérica corretamente")
    void deveTratatExceptionGenerica() {
        // Arrange
        Exception ex = new Exception("Erro genérico");

        // Act
        ResponseEntity<Map<String, String>> response = globalExceptionHandler.handleGenericExceptions(ex);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

        Map<String, String> responseBody = response.getBody();
        assertNotNull(responseBody);
        assertEquals("erro_generico", responseBody.get("codigo"));
        assertEquals("Erro genérico", responseBody.get("mensagem"));
    }

    @Test
    @DisplayName("Deve tratar NoSuchElementException corretamente")
    void deveTratatNoSuchElementException() {
        // Arrange
        NoSuchElementException ex = new NoSuchElementException("Elemento não encontrado");

        // Act
        ResponseEntity<Map<String, String>> response = globalExceptionHandler.handleNoSuchElementException(ex);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

        Map<String, String> responseBody = response.getBody();
        assertNotNull(responseBody);
        assertEquals("erro_nao_encontrado", responseBody.get("codigo"));
        assertEquals("Elemento não encontrado", responseBody.get("mensagem"));
    }

    @Test
    @DisplayName("Deve tratar EmptyResultDataAccessException corretamente")
    void deveTratatEmptyResultDataAccessException() {
        // Arrange
        EmptyResultDataAccessException ex = new EmptyResultDataAccessException("Registro não encontrado", 1);

        // Act
        ResponseEntity<Map<String, String>> response = globalExceptionHandler.handleNotFoundException(ex);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());

        Map<String, String> responseBody = response.getBody();
        assertNotNull(responseBody);
        assertEquals("erro_nao_encontrado", responseBody.get("codigo"));
        assertEquals("Registro não encontrado", responseBody.get("mensagem"));
    }

    @Test
    @DisplayName("Deve tratar HttpMessageNotReadableException corretamente")
    void deveTratatHttpMessageNotReadableException() {
        // Arrange
        HttpMessageNotReadableException ex = new HttpMessageNotReadableException("Erro ao processar JSON");

        // Act
        ResponseEntity<Map<String, String>> response = globalExceptionHandler.handleJsonParseException(ex);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

        Map<String, String> responseBody = response.getBody();
        assertNotNull(responseBody);
        assertEquals("erro_json", responseBody.get("codigo"));
        assertEquals("Erro ao processar a requisição. Verifique os dados enviados.", responseBody.get("mensagem"));
    }

    @Test
    @DisplayName("Deve tratar DataIntegrityViolationException corretamente")
    void deveTratatDataIntegrityViolationException() {
        // Arrange
        DataIntegrityViolationException ex = new DataIntegrityViolationException("Violação de integridade");

        // Act
        ResponseEntity<Map<String, String>> response = globalExceptionHandler.handleDataIntegrityViolation(ex);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

        Map<String, String> responseBody = response.getBody();
        assertNotNull(responseBody);
        assertEquals("erro_validacao", responseBody.get("codigo"));

        // Verifica se a mensagem existe e não é vazia, sem depender do texto exato
        assertNotNull(responseBody.get("mensagem"));
        assertFalse(responseBody.get("mensagem").isEmpty());
    }
}