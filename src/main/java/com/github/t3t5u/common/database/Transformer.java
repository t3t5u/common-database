package com.github.t3t5u.common.database;

import java.io.Serializable;

import com.github.t3t5u.common.expression.Expression;
import com.github.t3t5u.common.expression.Matcher;
import com.github.t3t5u.common.expression.MatcherExpression;

public abstract class Transformer extends com.github.t3t5u.common.expression.Transformer implements Visitor<Expression<? extends Serializable>> {
	@Override
	public final <T extends CharSequence & Serializable> Expression<? extends Serializable> visit(final Like<T> expression) {
		return transform(expression);
	}

	@Override
	public final <T extends Serializable> Expression<? extends Serializable> visit(final Column<T> expression) {
		return transform(expression);
	}

	protected <T extends CharSequence & Serializable> MatcherExpression<T, ? extends Matcher<T>> transform(final Like<T> expression) {
		return expression;
	}

	protected <T extends Serializable> Expression<T> transform(final Column<T> expression) {
		return expression;
	}
}
