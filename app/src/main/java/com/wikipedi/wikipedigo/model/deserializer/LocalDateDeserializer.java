package com.wikipedi.wikipedigo.model.deserializer;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import org.joda.time.LocalDate;

import java.lang.reflect.Type;
import java.util.Date;

/**
 * Created by E460 on 12/01/2017.
 */
public class LocalDateDeserializer implements JsonDeserializer<LocalDate> {
	@Override
	public LocalDate deserialize(JsonElement json, Type type, JsonDeserializationContext context)
			throws JsonParseException {
		try {
			return LocalDate.parse(json.getAsString());
		} catch (IllegalArgumentException e) {
			Date date = context.deserialize(json, Date.class);
			return new LocalDate(date);
		}
	}
}
