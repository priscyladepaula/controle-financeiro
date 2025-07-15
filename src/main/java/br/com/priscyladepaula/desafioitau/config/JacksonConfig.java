package br.com.priscyladepaula.desafioitau.config;

import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import java.time.format.DateTimeFormatter;

@Configuration
public class JacksonConfig {

    @Bean
    public Jackson2ObjectMapperBuilder jacksonObjectMapperBuilder(){

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        return new Jackson2ObjectMapperBuilder()
                .modules(new JavaTimeModule())
                .featuresToDisable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                .simpleDateFormat("dd/MM/yyyy")
                .serializers(new com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer(formatter))
                .deserializers(new com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer(formatter));
    }
}
