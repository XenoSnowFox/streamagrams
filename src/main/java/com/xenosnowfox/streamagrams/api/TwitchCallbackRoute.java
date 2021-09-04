package com.xenosnowfox.streamagrams.api;

import com.xenosnowfox.streamagrams.twitch.TwitchAccessTokenListener;
import lombok.NonNull;
import lombok.Setter;
import spark.Request;
import spark.Response;
import spark.Route;
import spark.Spark;

/**
 * Spark Route to handle the OAuth2 callback from Twitch.
 */
public class TwitchCallbackRoute implements Route {

	/**
	 * Returns a new instance.
	 *
	 * @return new TwitchCallbackRoute instance.
	 */
	public static TwitchCallbackRoute newInstance() {
		return new TwitchCallbackRoute();
	}

	/**
	 * Listener that is called upon receiving an access token.
	 */
	@Setter
	private TwitchAccessTokenListener twitchAccessTokenListener = null;

	/**
	 * Defines the listener to call when receiving an access token.
	 *
	 * @param withListener Listener.
	 * @return this instance to allow for method chaining.
	 */
	public TwitchCallbackRoute withTwitchAccessTokenListener(@NonNull final TwitchAccessTokenListener withListener) {
		this.setTwitchAccessTokenListener(withListener);
		return this;
	}

	@Override
	public Object handle(final Request request, final Response response) {
		final String accessToken = request.queryParams("access_token");

		if (this.twitchAccessTokenListener != null && accessToken != null) {
			this.twitchAccessTokenListener.onAccessToken(accessToken);
		}

		return "<!DOCTYPE html><html><head><script type='text/javascript'>"
				+ "var h = window.location.hash.substr(1);"
				+ "if (h) {"
				+ "window.location = \"http://localhost:" + Spark.port() + "/callback/twitch?\" + h;"
				+ "} else {"
				+ "window.close();"
				+ "}"
				+ "</script></head><body>You may now close this browser window.</body></html>";
	}
}
