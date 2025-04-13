package br.com.priscyladepaula.desafioitau.exception;

public class ValidationException extends RuntimeException {

    public ValidationException(String mensagem) {
        super(mensagem);
    }
}
