package com.github.t3t5u.common.database;

import java.io.Serializable;

import com.github.t3t5u.common.expression.BinaryExpression;
import com.google.common.base.Function;

public abstract class Inspector extends com.github.t3t5u.common.expression.Inspector implements Visitor<Boolean> {
	public Inspector(final boolean external, final Function<com.github.t3t5u.common.expression.Inspector, Function<BinaryExpression<?, ?, ?>, Boolean>> internal) {
		super(external, internal);
	}

	@Override
	public final <T extends CharSequence & Serializable> Boolean visit(final Like<T> expression) {
		return inspect(expression);
	}

	@Override
	public final <T extends Serializable> Boolean visit(final Column<T> expression) {
		return inspect(expression);
	}

	protected <T extends CharSequence & Serializable> boolean inspect(final Like<T> expression) {
		return getExternal();
	}

	protected <T extends Serializable> boolean inspect(final Column<T> expression) {
		return getExternal();
	}
}
