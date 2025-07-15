package br.com.priscyladepaula.desafioitau.annotations;

import org.springframework.format.annotation.DateTimeFormat;

import java.lang.annotation.*;

@Documented
@Target({ElementType.PARAMETER, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@DateTimeFormat(pattern = "dd/MM/yyyy")
public @interface BrazilianDateFormat {
}
