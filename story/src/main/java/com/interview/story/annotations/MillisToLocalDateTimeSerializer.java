package com.interview.story.annotations;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

public class MillisToLocalDateTimeSerializer extends JsonSerializer<Integer> {

    @Override
    public void serialize(Integer value, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        LocalDateTime dateTime = LocalDateTime.ofEpochSecond(value / 1, 0, ZoneOffset.UTC);
        jsonGenerator.writeString(dateTime.toString());
    }
}
