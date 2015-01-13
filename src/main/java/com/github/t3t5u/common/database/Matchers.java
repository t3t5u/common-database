package com.github.t3t5u.common.database;

public final class Matchers {
	private Matchers() {
	}

	public static Like<String> like(final String like) {
		return new Like<String>(new LikeMatcher<String>(like));
	}

	public static Like<String> like(final String like, final Character escape) {
		return new Like<String>(new LikeMatcher<String>(like, escape));
	}
}
