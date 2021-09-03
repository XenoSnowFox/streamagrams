package com.xenosnowfox.streamagrams.twitch;

import lombok.NonNull;
import lombok.Setter;

import javax.websocket.ClientEndpoint;
import javax.websocket.CloseReason;
import javax.websocket.ContainerProvider;
import javax.websocket.DeploymentException;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.WebSocketContainer;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Locale;
import java.util.concurrent.Future;

/**
 * Twitch IRC Client.
 */
@ClientEndpoint
public class TwitchIRCClient {

	/**
	 * URL of the Twitch WebSocket endpoint.
	 */
	private static final String WEBSOCKET_URI = "wss://irc-ws.chat.twitch.tv:443";

	/**
	 * WebSocket container.
	 */
	private final WebSocketContainer webSocketContainer;

	/**
	 * User Session.
	 */
	private Session userSession = null;

	/**
	 * Callback listener for receiving IRC messages.
	 */
	@Setter
	private TwitchIRCMessageListener messageListener;

	/**
	 * Default constructor.
	 */
	public TwitchIRCClient() {
		this.webSocketContainer = ContainerProvider.getWebSocketContainer();
	}

	/**
	 * Connects to the IRC.
	 */
	public void connect() {
		try {
			this.userSession = this.webSocketContainer.connectToServer(this, new URI(TwitchIRCClient.WEBSOCKET_URI));
		} catch (URISyntaxException | DeploymentException | IOException ex) {
			throw new RuntimeException(ex);
		}
	}

	/**
	 * Disconnects from IRC.
	 *
	 * @throws IOException
	 * 		if there is an error closing the connection.
	 */
	public void disconnect() throws IOException {
		if (this.userSession != null) {
			this.userSession.close();
		}
		this.userSession = null;
	}

	/**
	 * Issues an authentication command.
	 *
	 * @param withAccessToken the user's access token
	 * @return a future that will be resolved in the future
	 */
	public Future<Void> authenticate(@NonNull final String withAccessToken) {
		return this.sendMessage("PASS oauth:" + withAccessToken);
	}

	/**
	 * Issues a command to set the user's nickname.
	 *
	 * @param withNickname Nickname to use.
	 * @return a future that will be resolved in the future
	 */
	public Future<Void> setNickname(@NonNull final String withNickname) {
		return this.sendMessage("NICK " + withNickname);
	}

	/**
	 * Issues a command to join a channel and receive chat messages.
	 *
	 * @param withChannelName Twitch channel name to join.
	 * @return a future that will be resolved in the future
	 */
	public Future<Void> joinChannel(@NonNull final String withChannelName) {
		return this.sendMessage("JOIN #" + withChannelName.toLowerCase(Locale.ROOT));
	}

	/**
	 * Issues a `pong` command in response to a `ping`.
	 *
	 * @return a future that will be resolved in the future
	 */
	public Future<Void> pong() {
		return this.sendMessage("PONG :tmi.twitch.tv");
	}

	/**
	 * Sends a private message to the specified channel.
	 *
	 * @param withNickname your nickname to use (twitch doesn't seem to use this).
	 * @param withChannelName channel to send the message to.
	 * @param withMessage message to send
	 * @return a future that will be resolved in the future
	 */
	public Future<Void> sendPrivateMessage(String withNickname, String withChannelName, String withMessage) {
		String msg = ":" + withNickname + "!" + withNickname + "@" + withNickname + ".tmi.twitch.tv PRIVMSG #"
				+ withChannelName.toLowerCase(
				Locale.ROOT) + " :" + withMessage;
		return this.sendMessage(msg);
	}

	/**
	 * Sends a raw message.
	 *
	 * @param message raw IRC message
	 */
	private Future<Void> sendMessage(String message) {
		return this.userSession
				.getAsyncRemote()
				.sendText(message);
	}

	/**
	 * Callback hook for Connection open events.
	 *
	 * @param userSession
	 * 		the userSession which is opened.
	 */
	@OnOpen
	public void onOpen(Session userSession) {
		this.userSession = userSession;
	}

	/**
	 * Callback hook for Connection close events.
	 *
	 * @param userSession
	 * 		the userSession which is getting closed.
	 * @param reason
	 * 		the reason for connection close
	 */
	@OnClose
	public void onClose(Session userSession, CloseReason reason) {
		this.userSession = null;
	}

	/**
	 * Callback hook for Message Events. This method will be invoked when a client send a message.
	 *
	 * @param withMessageString
	 * 		The text message
	 */
	@OnMessage
	public void onMessage(String withMessageString) {
		TwitchIRCMessage message = TwitchIRCMessage.fromString(withMessageString);

		if (message.getCommand()
				.equalsIgnoreCase("ping")) {
			this.pong();
		}

		if (this.messageListener != null) {
			this.messageListener.onMessage(message);
		}
	}
}
