package br.com.priscyladepaula.desafioitau;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@TestPropertySource(properties = "api.key=chave-de-teste")
class ControleFinanceiroApplicationTests {

    @Test
    void contextLoads() {
    }

}
