package com.xenosnowfox.streamagrams.twitch;

/**
 * Listener callback for Twitch IRC messages.
 */
public interface TwitchIRCMessageListener {

	/**
	 * Called when a new twitch message is received.
	 *
	 * @param withMessage
	 * 		Twitch message.
	 */
	void onMessage(TwitchIRCMessage withMessage);
}
