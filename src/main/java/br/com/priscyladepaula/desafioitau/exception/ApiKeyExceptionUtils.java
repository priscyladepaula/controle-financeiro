package br.com.priscyladepaula.desafioitau.exception;

import br.com.priscyladepaula.desafioitau.dto.ErroDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class ApiKeyExceptionUtils extends RuntimeException {

    private final ObjectMapper mapper;

    public ApiKeyExceptionUtils(ObjectMapper mapper) {
        this.mapper = mapper;
    }

    public void erroDetalhado(HttpServletResponse response, HttpStatus status, String codigo, String mensagem) throws IOException {

        ErroDTO err = new ErroDTO(codigo, mensagem);

        response.setStatus(status.value());
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write(mapper.writeValueAsString(err));

    }
}
