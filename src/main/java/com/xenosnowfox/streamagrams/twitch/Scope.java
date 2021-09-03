package com.xenosnowfox.streamagrams.twitch;

import lombok.NonNull;

/**
 * Scopes available to the Twitch API.
 * <p>
 * See <a href="https://dev.twitch.tv/docs/authentication#scopes">Twitch API: Authentication Scopes</a>.
 */
public enum Scope {
	/**
	 * View analytics data for the Twitch Extensions owned by the authenticated account.
	 */
	ANALYTICS_READ_EXTENSIONS("analytics:read:extensions"),

	/**
	 * View analytics data for the games owned by the authenticated account.
	 */
	ANALYTICS_READ_GAMES("analytics:read:games"),

	/**
	 * View Bits information for a channel.
	 */
	BITS_READ("bits:read"),

	/**
	 * Run commercials on a channel.
	 */
	CHANNEL_EDIT_COMMERCIAL("channel:edit:commercial"),

	/**
	 * Manage a channel’s broadcast configuration, including updating channel configuration and managing stream markers
	 * and stream tags.
	 */
	CHANNEL_MANAGE_BROADCAST("channel:manage:broadcast"),

	/**
	 * Manage a channel’s Extension configuration, including activating Extensions.
	 */
	CHANNEL_MANAGE_EXTENSIONS("channel:manage:extensions"),

	/**
	 * Manage a channel’s polls.
	 */
	CHANNEL_MANAGE_POLLS("channel:manage:polls"),

	/**
	 * Manage of channel’s Channel Points Predictions
	 */
	CHANNEL_MANAGE_PREDICTIONS("channel:manage:predictions"),

	/**
	 * Manage Channel Points custom rewards and their redemptions on a channel.
	 */
	CHANNEL_MANAGE_REDEMPTIONS("channel:manage:redemptions"),

	/**
	 * Manage a channel’s stream schedule.
	 */
	CHANNEL_MANAGE_SCHEDULE("channel:manage:schedule"),

	/**
	 * Manage a channel’s videos, including deleting videos.
	 */
	CHANNEL_MANAGE_VIDEOS("channel:manage:videos"),

	/**
	 * View a list of users with the editor role for a channel.
	 */
	CHANNEL_READ_EDITORS("channel:read:editors"),

	/**
	 * View Hype Train information for a channel.
	 */
	CHANNEL_READ_HYPETRAIN("channel:read:hype_train"),

	/**
	 * View a channel’s polls.
	 */
	CHANNEL_READ_POLLS("channel:read:polls"),

	/**
	 * View a channel’s Channel Points Predictions.
	 */
	CHANNEL_READ_PREDICTIONS("channel:read:predictions"),

	/**
	 * View Channel Points custom rewards and their redemptions on a channel.
	 */
	CHANNEL_READ_REDEMPTIONS("channel:read:redemptions"),

	/**
	 * View an authorized user’s stream key.
	 */
	CHANNEL_READ_STREAMKEY("channel:read:stream_key"),

	/**
	 * View a list of all subscribers to a channel and check if a user is subscribed to a channel.
	 */
	CHANNEL_READ_SUBSCRIPTIONS("channel:read:subscriptions"),

	/**
	 * Manage Clips for a channel.
	 */
	CLIPS_EDIT("clips:edit"),

	/**
	 * View a channel’s moderation data including Moderators, Bans, Timeouts, and Automod settings.
	 */
	MODERATION_READ("moderation:read"),

	/**
	 * Manage messages held for review by AutoMod in channels where you are a moderator.
	 */
	MODERATION_MANAGE_AUTOMOD("moderation:manage:automod"),

	/**
	 * Manage a user object.
	 */
	USER_EDIT("user:edit"),

	/**
	 * Manage the block list of a user.
	 */
	USER_MANAGE_BLOCKEDUSERS("user:manage:blocked_users"),

	/**
	 * View the block list of a user.
	 */
	USER_READ_BLOCKEDUSERS("user:read:blocked_users"),

	/**
	 * View a user’s broadcasting configuration, including Extension configurations.
	 */
	USER_READ_BROADCAST("user:read:broadcast"),

	/**
	 * View a user’s email address.
	 */
	USER_READ_EMAIL("user:read:email"),

	/**
	 * View the list of channels a user follows.
	 */
	USER_READ_FOLLOWS("user:read:follows"),

	/**
	 * View if an authorized user is subscribed to specific channels.
	 */
	USER_READ_SUBSCRIPTIONS("user:read:subscriptions"),

	/**
	 * Perform moderation actions in a channel. The user requesting the scope must be a moderator in the channel.
	 */
	CHANNEL_MODERATE("channel:moderate"),

	/**
	 * Send live stream chat and rooms messages.
	 */
	CHAT_EDIT("chat:edit"),

	/**
	 * View live stream chat and rooms messages.
	 */
	CHAT_READ("chat:read"),

	/**
	 * View your whisper messages.
	 */
	WHISPERS_READ("whispers:read"),

	/**
	 * Send whisper messages.
	 */
	WHISPERS_EDIT("whispers:edit");

	/**
	 * Scope name as provided by Twitch.
	 */
	private final String value;

	/**
	 * Instantiates a new instance.
	 *
	 * @param withValue
	 * 		Scope name as provided by Twitch.
	 */
	Scope(@NonNull final String withValue) {
		this.value = withValue;
	}

	@Override
	public String toString() {
		return this.value;
	}
}
