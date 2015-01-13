package com.github.t3t5u.common.database;

import java.io.Serializable;

import com.github.t3t5u.common.expression.AbstractMatcherExpression;
import com.github.t3t5u.common.util.EscapeUtils;

@SuppressWarnings("serial")
public class Like<T extends CharSequence & Serializable> extends AbstractMatcherExpression<T, LikeMatcher<T>> {
	public static final char DEFAULT_ESCAPE = '\\';

	Like(final LikeMatcher<T> matcher) {
		super(matcher);
	}

	@Override
	public <R> R accept(final com.github.t3t5u.common.expression.Visitor<R> visitor) {
		return visitor instanceof Visitor ? ((Visitor<R>) visitor).visit(this) : null;
	}

	public static String escape(final CharSequence cs) {
		return escape(cs, DEFAULT_ESCAPE);
	}

	public static String escape(final CharSequence cs, final Character escape) {
		return EscapeUtils.escape(EscapeUtils.escape(cs, '%', escape), '_', escape);
	}
}
