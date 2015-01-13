package com.github.t3t5u.common.database;

import java.io.Serializable;

import com.github.t3t5u.common.expression.Matcher;

@SuppressWarnings("serial")
public class LikeMatcher<T extends CharSequence & Serializable> implements Matcher<T> {
	private final T like;
	private final Character escape;

	LikeMatcher(final T like) {
		this(like, Like.DEFAULT_ESCAPE);
	}

	LikeMatcher(final T like, final Character escape) {
		this.like = like;
		this.escape = escape;
	}

	public T getLike() {
		return like;
	}

	public Character getEscape() {
		return escape;
	}

	@Override
	public boolean match(final T sequence) {
		return false;
	}
}
