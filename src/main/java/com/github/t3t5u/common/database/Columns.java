package com.github.t3t5u.common.database;

import java.math.BigDecimal;
import java.util.Date;

public final class Columns {
	private Columns() {
	}

	public static Column<Boolean> asBoolean(final String tableName, final String columnName) {
		return new Column<Boolean>(Boolean.class, tableName, columnName);
	}

	public static Column<BigDecimal> asBigDecimal(final String tableName, final String columnName) {
		return new Column<BigDecimal>(BigDecimal.class, tableName, columnName);
	}

	public static Column<Byte> asByte(final String tableName, final String columnName) {
		return new Column<Byte>(Byte.class, tableName, columnName);
	}

	public static Column<Short> asShort(final String tableName, final String columnName) {
		return new Column<Short>(Short.class, tableName, columnName);
	}

	public static Column<Integer> asInteger(final String tableName, final String columnName) {
		return new Column<Integer>(Integer.class, tableName, columnName);
	}

	public static Column<Long> asLong(final String tableName, final String columnName) {
		return new Column<Long>(Long.class, tableName, columnName);
	}

	public static Column<Float> asFloat(final String tableName, final String columnName) {
		return new Column<Float>(Float.class, tableName, columnName);
	}

	public static Column<Double> asDouble(final String tableName, final String columnName) {
		return new Column<Double>(Double.class, tableName, columnName);
	}

	public static Column<Character> asCharacter(final String tableName, final String columnName) {
		return new Column<Character>(Character.class, tableName, columnName);
	}

	public static Column<String> asString(final String tableName, final String columnName) {
		return new Column<String>(String.class, tableName, columnName);
	}

	public static <T extends Enum<T>> Column<T> asEnum(final Class<T> expressionClass, final String tableName, final String columnName) {
		return new Column<T>(expressionClass, tableName, columnName);
	}

	public static Column<Date> asDate(final String tableName, final String columnName) {
		return new Column<Date>(Date.class, tableName, columnName);
	}
}
