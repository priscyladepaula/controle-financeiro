package br.com.priscyladepaula.desafioitau.dto;

import lombok.Data;

@Data
public class ErroDTO {

    private String erro;
    private String mensagem;

    public ErroDTO(String erro, String mensagem) {
        this.erro = erro;
        this.mensagem = mensagem;
    }
}
