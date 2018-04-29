package application.helper;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import application.entity.Message;

public class MessageSerializer extends JsonSerializer<Message>{

	@Override
	public void serialize(Message msg, JsonGenerator jgen, SerializerProvider provider)
			throws IOException, JsonProcessingException {
		jgen.writeStartObject();
		jgen.writeNumberField("id",msg.getId());
		jgen.writeStringField("msg",msg.getMsg());
		jgen.writeStringField("created",msg.getCreated().toString());
		jgen.writeNumberField("userId",msg.getUser().getId());
		jgen.writeStringField("userName",msg.getUser().getUserName());
		jgen.writeEndObject();
	}

}
