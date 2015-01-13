package com.github.t3t5u.common.database;

import java.io.Serializable;

public interface Visitor<R> extends com.github.t3t5u.common.expression.Visitor<R> {
	<T extends CharSequence & Serializable> R visit(Like<T> expression);

	<T extends Serializable> R visit(Column<T> expression);
}
