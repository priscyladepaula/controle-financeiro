package br.com.priscyladepaula.desafioitau.exception;

public class NotFoundException extends RuntimeException {

    public NotFoundException(String mensagem) {
        super(mensagem);
    }
}
