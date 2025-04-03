package br.com.priscyladepaula.desafioitau;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class ControleFinanceiroApplication {

    public static void main(String[] args) {
        SpringApplication.run(ControleFinanceiroApplication.class, args);
    }

}
