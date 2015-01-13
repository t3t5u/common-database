package com.github.t3t5u.common.database;

import java.io.Serializable;

import com.github.t3t5u.common.expression.AbstractExpression;

@SuppressWarnings("serial")
public class Column<T extends Serializable> extends AbstractExpression<T> {
	private final String tableName;
	private final String columnName;

	Column(final Class<T> expressionClass, final String tableName, final String columnName) {
		super(expressionClass);
		this.tableName = tableName;
		this.columnName = columnName;
	}

	public String getTableName() {
		return tableName;
	}

	public String getColumnName() {
		return columnName;
	}

	@Override
	public T evaluate() {
		return null;
	}

	@Override
	public <R> R accept(final com.github.t3t5u.common.expression.Visitor<R> visitor) {
		return visitor instanceof Visitor ? ((Visitor<R>) visitor).visit(this) : null;
	}
}
