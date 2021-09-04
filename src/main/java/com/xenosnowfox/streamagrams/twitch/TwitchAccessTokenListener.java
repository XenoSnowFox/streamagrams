package com.xenosnowfox.streamagrams.twitch;

/**
 * Callback listener for retrieving the user's OAuth2 Access Token.
 */
public interface TwitchAccessTokenListener {

	/**
	 * Called with the access token.
	 *
	 * @param withAccessToken Twitch OAuth2 Access Token
	 */
	void onAccessToken(String withAccessToken);
}
