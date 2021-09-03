package com.xenosnowfox.streamagrams.twitch;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * IRC Message based upon <a href="https://datatracker.ietf.org/doc/html/rfc1459">RFC1459</a>.
 */
@Setter
@Getter
@ToString
public class TwitchIRCMessage {
	/**
	 * Returns an IRC Message from the raw string representation.
	 *
	 * @param withMessage
	 * 		Raw IRC Message.
	 * @return IRC Message object.
	 * @throws IllegalArgumentException
	 * 		if the provided message is blank or empty.
	 */
	public static TwitchIRCMessage fromString(@NonNull final String withMessage) {

		if (withMessage.isBlank()) {
			throw new IllegalStateException("Empty Line.");
		}

		TwitchIRCMessage message = new TwitchIRCMessage();

		String str = withMessage.trim();

		if (str.startsWith(":")) {
			var s = str.split(" ", 2);
			message.setPrefix(s[0]);
			str = s[1];
		}

		List<String> args = new ArrayList<>();
		if (str.contains(" :")) {
			var s = str.split(" :", 2);

			args.addAll(Arrays.asList(s[0].split(" ")));
			args.add(s[1]);
		} else {
			args.addAll(Arrays.asList(str.split(" ")));
		}

		message.setCommand(args.remove(0));
		message.setArguments(args.toArray(String[]::new));
		return message;
	}

	/**
	 * Message prefix data.
	 */
	private String prefix;

	/**
	 * Command.
	 */
	private String command;

	/**
	 * Command arguments.
	 */
	private String[] arguments;
}
