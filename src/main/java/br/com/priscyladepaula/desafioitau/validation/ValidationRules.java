package br.com.priscyladepaula.desafioitau.validation;

import br.com.priscyladepaula.desafioitau.exception.ValidationException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class ValidationRules<T> {

    private final T objeto;

    private ValidationRules(T objeto){
        this.objeto = objeto;
    }

    public static <T> ValidationRules<T> com(T objeto){

        return new ValidationRules<>(objeto);
    }

    public ValidationRules<T> notEmpty(Function<T, String> getter, String msgErro){

        String value = getter.apply(objeto);

        if (value == null || value.trim().isEmpty()){
            throw new ValidationException(msgErro);
        }

        return this;
    }

    public ValidationRules<T> validId(Function<T, Long> getter, String msgErro){

        Long id = getter.apply(objeto);

        if (id == null || id <= 0){
            throw new ValidationException(msgErro);
        }

        return this;
    }

    public ValidationRules<T> validNumber(Function<T, BigDecimal> getter, String msgErro){
        BigDecimal value = getter.apply(objeto);

        if (value == null || value.compareTo(BigDecimal.ZERO) == 0) {
            throw new ValidationException(msgErro);
        }

        return this;
    }

    public ValidationRules<T> defaultDateIfNull(
            Function<T, LocalDate> getter,
            BiConsumer<T, LocalDate> setter,
            Supplier<LocalDate> defaultValue
    ) {
        if (getter.apply(objeto) == null) {
            setter.accept(objeto, defaultValue.get());
        }
        return this;
    }


    public static void validPeriod(LocalDate inicio, LocalDate fim, String msgErro) {
        if (inicio != null && fim != null && inicio.isAfter(fim)) {
            throw new ValidationException(msgErro);
        }
    }


    public void execute(){

    }
}
