package application.helper;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import application.entity.Conversation;
import application.entity.Message;

public class ConversationSerializer extends JsonSerializer<Conversation>{

	@Override
	public void serialize(Conversation conv, JsonGenerator jgen, SerializerProvider provider)
			throws IOException, JsonProcessingException {
		jgen.writeStartObject();
		jgen.writeNumberField("id",conv.getId());
		jgen.writeStringField("title",conv.getTitle());
		jgen.writeStringField("updated",conv.getUpdated().toString());
		jgen.writeStringField("created",conv.getCreated().toString());
		jgen.writeNumberField("offerId",conv.getOffer().getId());
		jgen.writeStringField("offerTitle",conv.getOffer().getTitle());
		jgen.writeEndObject();
	}

}
