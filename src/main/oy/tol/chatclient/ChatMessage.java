package oy.tol.chatclient;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

import org.json.JSONException;
import org.json.JSONObject;


public class ChatMessage {
	public LocalDateTime sent;
	public String nick;
	public String message;

	static public ChatMessage from(JSONObject jsonObject) throws JSONException {
		ChatMessage message = new ChatMessage();
		message.nick = jsonObject.getString("user");
		String dateStr = jsonObject.getString("sent");
		OffsetDateTime odt = OffsetDateTime.parse(dateStr);
		message.sent = LocalDateTime.ofInstant(odt.toInstant(), ZoneId.systemDefault());
		message.message = jsonObject.getString("message");
		return message;
	}
	
	public String sentAsString() {
		String str = "";
		LocalDateTime now = LocalDateTime.now();
		long diff = Math.abs(ChronoUnit.HOURS.between(now, sent));
		if (diff <= 24) {
			str += sent.format(DateTimeFormatter.ofPattern("HH:mm:ss"));
		} else {
			str += sent.format(DateTimeFormatter.ofPattern("yyyy.MM.dd hh:mm:ss"));
		}
		return str;
	}
	
}
