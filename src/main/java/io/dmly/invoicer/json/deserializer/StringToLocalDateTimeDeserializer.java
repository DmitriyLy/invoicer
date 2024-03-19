package io.dmly.invoicer.json.deserializer;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import static io.dmly.invoicer.constants.Constants.INVOICE_DATE_PATTERN;

@Component
public class StringToLocalDateTimeDeserializer extends JsonDeserializer<LocalDateTime> {
    private final DateTimeFormatter pattern = DateTimeFormatter.ofPattern(INVOICE_DATE_PATTERN);

    @Override
    public LocalDateTime deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JacksonException {
        LocalDate date = LocalDate.parse(p.getText(), pattern);
        return LocalDateTime.of(date, LocalTime.MIDNIGHT);
    }
}
