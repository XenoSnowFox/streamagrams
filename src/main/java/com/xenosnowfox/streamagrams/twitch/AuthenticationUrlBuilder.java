package com.xenosnowfox.streamagrams.twitch;

import lombok.NonNull;
import org.h2.util.StringUtils;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.EnumSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Builder class for generating
 */
public class AuthenticationUrlBuilder {

	/**
	 * Application's client ID.
	 */
	private String clientId;

	/**
	 * Callback URL.
	 */
	private URI redirectUri;

	/**
	 * Set of authentication scopes.
	 */
	private final Set<Scope> scopes = EnumSet.noneOf(Scope.class);

	/**
	 * Returns a builder capable of producing an authentication url for Twitch.
	 *
	 * @return New builder instance.
	 */
	public static AuthenticationUrlBuilder builder() {
		return new AuthenticationUrlBuilder();
	}

	/**
	 * Hidden constructor.
	 */
	private AuthenticationUrlBuilder() {
	}

	/**
	 * Defines the client ID used by the application.
	 *
	 * @param withClientId
	 * 		Client ID.
	 * @return this builder instance to allow for method chaining
	 */
	public AuthenticationUrlBuilder withClientId(@NonNull final String withClientId) {
		this.clientId = withClientId;
		return this;
	}

	/**
	 * Defines the set of scopes the application requires.
	 *
	 * @param withScopes
	 * 		Scope set.
	 * @return this builder instance to allow for method chaining
	 */
	public AuthenticationUrlBuilder withScopes(@NonNull final Set<Scope> withScopes) {
		this.scopes.clear();
		this.scopes.addAll(withScopes);
		return this;
	}

	/**
	 * Defines the redirect/callback URI that Twitch will redirect to.
	 *
	 * @param withUri
	 * 		redirect uri.
	 * @return this builder instance to allow for method chaining
	 */
	public AuthenticationUrlBuilder withRedirectUri(@NonNull final URI withUri) {
		this.redirectUri = withUri;
		return this;
	}

	/**
	 * Builds and returns the Authentication URL as a URL object.
	 *
	 * @return Authentication URL.
	 * @throws URISyntaxException
	 * 		if the URL string is malformed or invalid.
	 * @throws IllegalStateException
	 * 		if either the clint id or scopes are missing.
	 */
	public URI toUri() throws URISyntaxException {
		return new URI(this.build());
	}

	/**
	 * Returns the Authentication URL from the details provided.
	 *
	 * @return Authentication URL.
	 * @throws IllegalStateException
	 * 		if either the clint id or scopes are missing.
	 */
	public String build() {
		if (StringUtils.isNullOrEmpty(this.clientId)) {
			throw new IllegalStateException("Client ID has not been defined.");
		}

		if (this.redirectUri == null) {
			throw new IllegalStateException("Redirect URI not provided.");
		}

		if (this.scopes.isEmpty()) {
			throw new IllegalStateException("Scopes have note been defined.");
		}

		String scope = this.scopes.stream()
				.map(Scope::toString)
				.collect(Collectors.joining("%20"));

		return "https://id.twitch.tv/oauth2/authorize?client_id=" + this.clientId
				+ "&redirect_uri=" + this.redirectUri.toString()
				+ "&response_type=token"
				+ "&scope=" + scope;
	}
}
