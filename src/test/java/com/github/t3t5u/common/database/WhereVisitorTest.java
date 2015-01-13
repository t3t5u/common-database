package com.github.t3t5u.common.database;

import static com.github.t3t5u.common.database.Columns.*;
import static com.github.t3t5u.common.database.Matchers.*;
import static com.github.t3t5u.common.expression.Binaries.*;
import static com.github.t3t5u.common.expression.Literals.*;
import static com.github.t3t5u.common.expression.Unaries.*;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.math.BigDecimal;

import javax.annotation.Nullable;

import org.junit.Test;

import com.github.t3t5u.common.expression.And;
import com.github.t3t5u.common.expression.Expression;
import com.github.t3t5u.common.expression.Or;
import com.google.common.base.Function;

public class WhereVisitorTest {
	@Test
	public void test() {
		final Visitor<String> visitor = new WhereVisitor();
		final Transformer transformer = new Transformer() {
		};
		final Column<Boolean> columnTrue = asBoolean("table_true", "column_true");
		final Column<Boolean> columnFalse = asBoolean("table_false", "column_false");
		final Column<String> columnA = asString("table_a", "column_a");
		final Column<String> columnB = asString("table_b", "column_b");
		final Column<BigDecimal> column1 = asBigDecimal("table_1", "column_1");
		final Column<BigDecimal> column2 = asBigDecimal("table_2", "column_2");
		assertThat(accept(visitor, transformer, not(and(or(asBoolean(true), asBoolean(false)), asBoolean(true)))), is("NOT ((1 OR 0) AND 1)"));
		assertThat(accept(visitor, transformer, not(and(or(equal(asString("a"), asString("a")), notEqual(asString("b"), asString("b"))), asBoolean(true)))), is("NOT ((('a' = 'a') OR ('b' <> 'b')) AND 1)"));
		assertThat(accept(visitor, transformer, not(and(or(equal(asString("a"), asString("b")), notEqual(asString("a"), asString("b"))), asBoolean(true)))), is("NOT ((('a' = 'b') OR ('a' <> 'b')) AND 1)"));
		assertThat(accept(visitor, transformer, not(and(or(lessThan(asInteger(1), asInteger(2)), greaterThanOrEqual(asInteger(1), asInteger(2))), asBoolean(true)))), is("NOT (((1 < 2) OR (1 >= 2)) AND 1)"));
		assertThat(accept(visitor, transformer, not(and(or(lessThan(add(asInteger(1), asInteger(1)), asInteger(2)), greaterThanOrEqual(add(asInteger(1), asInteger(1)), asInteger(2))), asBoolean(true)))), is("NOT ((((1 + 1) < 2) OR ((1 + 1) >= 2)) AND 1)"));
		assertThat(accept(visitor, transformer, not(and(or(lessThan(subtract(asInteger(1), asInteger(1)), asInteger(2)), greaterThanOrEqual(subtract(asInteger(1), asInteger(1)), asInteger(2))), asBoolean(true)))), is("NOT ((((1 - 1) < 2) OR ((1 - 1) >= 2)) AND 1)"));
		assertThat(accept(visitor, transformer, not(and(or(lessThan(divide(asInteger(1), asInteger(1)), asInteger(2)), greaterThanOrEqual(divide(asInteger(1), asInteger(1)), asInteger(2))), asBoolean(true)))), is("NOT ((((1 * 1) < 2) OR ((1 * 1) >= 2)) AND 1)"));
		assertThat(accept(visitor, transformer, not(and(or(lessThan(multiply(asInteger(1), asInteger(1)), asInteger(2)), greaterThanOrEqual(multiply(asInteger(1), asInteger(1)), asInteger(2))), asBoolean(true)))), is("NOT ((((1 / 1) < 2) OR ((1 / 1) >= 2)) AND 1)"));
		assertThat(accept(visitor, transformer, not(and(or(greaterThan(asInteger(1), asInteger(2)), lessThanOrEqual(asInteger(1), asInteger(2))), asBoolean(true)))), is("NOT (((1 > 2) OR (1 <= 2)) AND 1)"));
		assertThat(accept(visitor, transformer, not(and(or(greaterThan(add(asInteger(1), asInteger(1)), asInteger(2)), lessThanOrEqual(add(asInteger(1), asInteger(1)), asInteger(2))), asBoolean(true)))), is("NOT ((((1 + 1) > 2) OR ((1 + 1) <= 2)) AND 1)"));
		assertThat(accept(visitor, transformer, not(and(or(greaterThan(subtract(asInteger(1), asInteger(1)), asInteger(2)), lessThanOrEqual(subtract(asInteger(1), asInteger(1)), asInteger(2))), asBoolean(true)))), is("NOT ((((1 - 1) > 2) OR ((1 - 1) <= 2)) AND 1)"));
		assertThat(accept(visitor, transformer, not(and(or(greaterThan(divide(asInteger(1), asInteger(1)), asInteger(2)), lessThanOrEqual(divide(asInteger(1), asInteger(1)), asInteger(2))), asBoolean(true)))), is("NOT ((((1 * 1) > 2) OR ((1 * 1) <= 2)) AND 1)"));
		assertThat(accept(visitor, transformer, not(and(or(greaterThan(multiply(asInteger(1), asInteger(1)), asInteger(2)), lessThanOrEqual(multiply(asInteger(1), asInteger(1)), asInteger(2))), asBoolean(true)))), is("NOT ((((1 / 1) > 2) OR ((1 / 1) <= 2)) AND 1)"));
		assertThat(accept(visitor, transformer, not(and(or(match(asString("a"), like("%a%", null)), notMatch(asString("b"), like("%b%", null))), asBoolean(true)))), is("NOT ((('a' LIKE '%a%') OR ('b' NOT LIKE '%b%')) AND 1)"));
		assertThat(accept(visitor, transformer, not(and(or(match(asString("a"), like("%b%", null)), notMatch(asString("a"), like("%b%", null))), asBoolean(true)))), is("NOT ((('a' LIKE '%b%') OR ('a' NOT LIKE '%b%')) AND 1)"));
		assertThat(accept(visitor, transformer, not(and(or(match(asString("a"), like("%'\\%_a_\\%'%", '\'')), notMatch(asString("b"), like("%'\\%_b_\\%'%", '\''))), asBoolean(true)))), is("NOT ((('a' LIKE '%''\\%_a_\\%''%' ESCAPE '''') OR ('b' NOT LIKE '%''\\%_b_\\%''%' ESCAPE '''')) AND 1)"));
		assertThat(accept(visitor, transformer, not(and(or(match(asString("a"), like("%'\\%_b_\\%'%", '\'')), notMatch(asString("a"), like("%'\\%_b_\\%'%", '\''))), asBoolean(true)))), is("NOT ((('a' LIKE '%''\\%_b_\\%''%' ESCAPE '''') OR ('a' NOT LIKE '%''\\%_b_\\%''%' ESCAPE '''')) AND 1)"));
		assertThat(accept(visitor, transformer, not(and(or(match(asString("a"), like("%" + Like.escape("'\\%_a_\\%'", '#') + "%", '#')), notMatch(asString("b"), like("%" + Like.escape("'\\%_b_\\%'", '#') + "%", '#'))), asBoolean(true)))),
				is("NOT ((('a' LIKE '%''\\#%#_a#_\\#%''%' ESCAPE '#') OR ('b' NOT LIKE '%''\\#%#_b#_\\#%''%' ESCAPE '#')) AND 1)"));
		assertThat(accept(visitor, transformer, not(and(or(match(asString("a"), like("%" + Like.escape("'\\%_b_\\%'", '#') + "%", '#')), notMatch(asString("a"), like("%" + Like.escape("'\\%_b_\\%'", '#') + "%", '#'))), asBoolean(true)))),
				is("NOT ((('a' LIKE '%''\\#%#_b#_\\#%''%' ESCAPE '#') OR ('a' NOT LIKE '%''\\#%#_b#_\\#%''%' ESCAPE '#')) AND 1)"));
		assertThat(accept(visitor, transformer, not(and(or(match(asString("a"), like("%" + Like.escape("'\\%_a_\\%'", '\'') + "%", '\'')), notMatch(asString("b"), like("%" + Like.escape("'\\%_b_\\%'", '\'') + "%", '\''))), asBoolean(true)))),
				is("NOT ((('a' LIKE '%''\\''%''_a''_\\''%''%' ESCAPE '''') OR ('b' NOT LIKE '%''\\''%''_b''_\\''%''%' ESCAPE '''')) AND 1)"));
		assertThat(accept(visitor, transformer, not(and(or(match(asString("a"), like("%" + Like.escape("'\\%_b_\\%'", '\'') + "%", '\'')), notMatch(asString("a"), like("%" + Like.escape("'\\%_b_\\%'", '\'') + "%", '\''))), asBoolean(true)))),
				is("NOT ((('a' LIKE '%''\\''%''_b''_\\''%''%' ESCAPE '''') OR ('a' NOT LIKE '%''\\''%''_b''_\\''%''%' ESCAPE '''')) AND 1)"));
		assertThat(accept(visitor, transformer, not(and(or(match(asString("a"), like("%" + Like.escape("'\\%_a_\\%'") + "%", '\\')), notMatch(asString("b"), like("%" + Like.escape("'\\%_b_\\%'") + "%", '\\'))), asBoolean(true)))),
				is("NOT ((('a' LIKE '%''\\\\%\\_a\\_\\\\%''%' ESCAPE '\\') OR ('b' NOT LIKE '%''\\\\%\\_b\\_\\\\%''%' ESCAPE '\\')) AND 1)"));
		assertThat(accept(visitor, transformer, not(and(or(match(asString("a"), like("%" + Like.escape("'\\%_b_\\%'") + "%", '\\')), notMatch(asString("a"), like("%" + Like.escape("'\\%_b_\\%'") + "%", '\\'))), asBoolean(true)))),
				is("NOT ((('a' LIKE '%''\\\\%\\_b\\_\\\\%''%' ESCAPE '\\') OR ('a' NOT LIKE '%''\\\\%\\_b\\_\\\\%''%' ESCAPE '\\')) AND 1)"));
		assertThat(accept(visitor, transformer, not(and(or(lessThan(asBigDecimal(new BigDecimal(1)), asBigDecimal(new BigDecimal(2))), greaterThanOrEqual(asBigDecimal(new BigDecimal(1)), asBigDecimal(new BigDecimal(2)))), asBoolean(true)))), is("NOT (((1 < 2) OR (1 >= 2)) AND 1)"));
		assertThat(accept(visitor, transformer, not(and(or(lessThan(add(asBigDecimal(new BigDecimal(1)), asBigDecimal(new BigDecimal(1))), asBigDecimal(new BigDecimal(2))), greaterThanOrEqual(add(asBigDecimal(new BigDecimal(1)), asBigDecimal(new BigDecimal(1))), asBigDecimal(new BigDecimal(2)))), asBoolean(true)))),
				is("NOT ((((1 + 1) < 2) OR ((1 + 1) >= 2)) AND 1)"));
		assertThat(
				accept(visitor, transformer,
						not(and(or(lessThan(subtract(asBigDecimal(new BigDecimal(1)), asBigDecimal(new BigDecimal(1))), asBigDecimal(new BigDecimal(2))), greaterThanOrEqual(subtract(asBigDecimal(new BigDecimal(1)), asBigDecimal(new BigDecimal(1))), asBigDecimal(new BigDecimal(2)))), asBoolean(true)))),
				is("NOT ((((1 - 1) < 2) OR ((1 - 1) >= 2)) AND 1)"));
		assertThat(
				accept(visitor, transformer, not(and(or(lessThan(divide(asBigDecimal(new BigDecimal(1)), asBigDecimal(new BigDecimal(1))), asBigDecimal(new BigDecimal(2))), greaterThanOrEqual(divide(asBigDecimal(new BigDecimal(1)), asBigDecimal(new BigDecimal(1))), asBigDecimal(new BigDecimal(2)))), asBoolean(true)))),
				is("NOT ((((1 * 1) < 2) OR ((1 * 1) >= 2)) AND 1)"));
		assertThat(
				accept(visitor, transformer,
						not(and(or(lessThan(multiply(asBigDecimal(new BigDecimal(1)), asBigDecimal(new BigDecimal(1))), asBigDecimal(new BigDecimal(2))), greaterThanOrEqual(multiply(asBigDecimal(new BigDecimal(1)), asBigDecimal(new BigDecimal(1))), asBigDecimal(new BigDecimal(2)))), asBoolean(true)))),
				is("NOT ((((1 / 1) < 2) OR ((1 / 1) >= 2)) AND 1)"));
		assertThat(accept(visitor, transformer, not(and(or(greaterThan(asBigDecimal(new BigDecimal(1)), asBigDecimal(new BigDecimal(2))), lessThanOrEqual(asBigDecimal(new BigDecimal(1)), asBigDecimal(new BigDecimal(2)))), asBoolean(true)))), is("NOT (((1 > 2) OR (1 <= 2)) AND 1)"));
		assertThat(accept(visitor, transformer, not(and(or(greaterThan(add(asBigDecimal(new BigDecimal(1)), asBigDecimal(new BigDecimal(1))), asBigDecimal(new BigDecimal(2))), lessThanOrEqual(add(asBigDecimal(new BigDecimal(1)), asBigDecimal(new BigDecimal(1))), asBigDecimal(new BigDecimal(2)))), asBoolean(true)))),
				is("NOT ((((1 + 1) > 2) OR ((1 + 1) <= 2)) AND 1)"));
		assertThat(
				accept(visitor, transformer,
						not(and(or(greaterThan(subtract(asBigDecimal(new BigDecimal(1)), asBigDecimal(new BigDecimal(1))), asBigDecimal(new BigDecimal(2))), lessThanOrEqual(subtract(asBigDecimal(new BigDecimal(1)), asBigDecimal(new BigDecimal(1))), asBigDecimal(new BigDecimal(2)))), asBoolean(true)))),
				is("NOT ((((1 - 1) > 2) OR ((1 - 1) <= 2)) AND 1)"));
		assertThat(
				accept(visitor, transformer, not(and(or(greaterThan(divide(asBigDecimal(new BigDecimal(1)), asBigDecimal(new BigDecimal(1))), asBigDecimal(new BigDecimal(2))), lessThanOrEqual(divide(asBigDecimal(new BigDecimal(1)), asBigDecimal(new BigDecimal(1))), asBigDecimal(new BigDecimal(2)))), asBoolean(true)))),
				is("NOT ((((1 * 1) > 2) OR ((1 * 1) <= 2)) AND 1)"));
		assertThat(
				accept(visitor, transformer,
						not(and(or(greaterThan(multiply(asBigDecimal(new BigDecimal(1)), asBigDecimal(new BigDecimal(1))), asBigDecimal(new BigDecimal(2))), lessThanOrEqual(multiply(asBigDecimal(new BigDecimal(1)), asBigDecimal(new BigDecimal(1))), asBigDecimal(new BigDecimal(2)))), asBoolean(true)))),
				is("NOT ((((1 / 1) > 2) OR ((1 / 1) <= 2)) AND 1)"));
		assertThat(accept(visitor, transformer, not(and(or(columnTrue, columnFalse), columnTrue))), is("NOT ((`table_true`.`column_true` OR `table_false`.`column_false`) AND `table_true`.`column_true`)"));
		assertThat(accept(visitor, transformer, not(and(or(equal(columnA, columnA), notEqual(columnB, columnB)), columnTrue))), is("NOT (((`table_a`.`column_a` = `table_a`.`column_a`) OR (`table_b`.`column_b` <> `table_b`.`column_b`)) AND `table_true`.`column_true`)"));
		assertThat(accept(visitor, transformer, not(and(or(equal(columnA, columnB), notEqual(columnA, columnB)), columnTrue))), is("NOT (((`table_a`.`column_a` = `table_b`.`column_b`) OR (`table_a`.`column_a` <> `table_b`.`column_b`)) AND `table_true`.`column_true`)"));
		assertThat(accept(visitor, transformer, not(and(or(lessThan(column1, column2), greaterThanOrEqual(column1, column2)), columnTrue))), is("NOT (((`table_1`.`column_1` < `table_2`.`column_2`) OR (`table_1`.`column_1` >= `table_2`.`column_2`)) AND `table_true`.`column_true`)"));
		assertThat(accept(visitor, transformer, not(and(or(lessThan(add(column1, column1), column2), greaterThanOrEqual(add(column1, column1), column2)), columnTrue))),
				is("NOT ((((`table_1`.`column_1` + `table_1`.`column_1`) < `table_2`.`column_2`) OR ((`table_1`.`column_1` + `table_1`.`column_1`) >= `table_2`.`column_2`)) AND `table_true`.`column_true`)"));
		assertThat(accept(visitor, transformer, not(and(or(lessThan(subtract(column1, column1), column2), greaterThanOrEqual(subtract(column1, column1), column2)), columnTrue))),
				is("NOT ((((`table_1`.`column_1` - `table_1`.`column_1`) < `table_2`.`column_2`) OR ((`table_1`.`column_1` - `table_1`.`column_1`) >= `table_2`.`column_2`)) AND `table_true`.`column_true`)"));
		assertThat(accept(visitor, transformer, not(and(or(lessThan(divide(column1, column1), column2), greaterThanOrEqual(divide(column1, column1), column2)), columnTrue))),
				is("NOT ((((`table_1`.`column_1` * `table_1`.`column_1`) < `table_2`.`column_2`) OR ((`table_1`.`column_1` * `table_1`.`column_1`) >= `table_2`.`column_2`)) AND `table_true`.`column_true`)"));
		assertThat(accept(visitor, transformer, not(and(or(lessThan(multiply(column1, column1), column2), greaterThanOrEqual(multiply(column1, column1), column2)), columnTrue))),
				is("NOT ((((`table_1`.`column_1` / `table_1`.`column_1`) < `table_2`.`column_2`) OR ((`table_1`.`column_1` / `table_1`.`column_1`) >= `table_2`.`column_2`)) AND `table_true`.`column_true`)"));
		assertThat(accept(visitor, transformer, not(and(or(greaterThan(column1, column2), lessThanOrEqual(column1, column2)), columnTrue))), is("NOT (((`table_1`.`column_1` > `table_2`.`column_2`) OR (`table_1`.`column_1` <= `table_2`.`column_2`)) AND `table_true`.`column_true`)"));
		assertThat(accept(visitor, transformer, not(and(or(greaterThan(add(column1, column1), column2), lessThanOrEqual(add(column1, column1), column2)), columnTrue))),
				is("NOT ((((`table_1`.`column_1` + `table_1`.`column_1`) > `table_2`.`column_2`) OR ((`table_1`.`column_1` + `table_1`.`column_1`) <= `table_2`.`column_2`)) AND `table_true`.`column_true`)"));
		assertThat(accept(visitor, transformer, not(and(or(greaterThan(subtract(column1, column1), column2), lessThanOrEqual(subtract(column1, column1), column2)), columnTrue))),
				is("NOT ((((`table_1`.`column_1` - `table_1`.`column_1`) > `table_2`.`column_2`) OR ((`table_1`.`column_1` - `table_1`.`column_1`) <= `table_2`.`column_2`)) AND `table_true`.`column_true`)"));
		assertThat(accept(visitor, transformer, not(and(or(greaterThan(divide(column1, column1), column2), lessThanOrEqual(divide(column1, column1), column2)), columnTrue))),
				is("NOT ((((`table_1`.`column_1` * `table_1`.`column_1`) > `table_2`.`column_2`) OR ((`table_1`.`column_1` * `table_1`.`column_1`) <= `table_2`.`column_2`)) AND `table_true`.`column_true`)"));
		assertThat(accept(visitor, transformer, not(and(or(greaterThan(multiply(column1, column1), column2), lessThanOrEqual(multiply(column1, column1), column2)), columnTrue))),
				is("NOT ((((`table_1`.`column_1` / `table_1`.`column_1`) > `table_2`.`column_2`) OR ((`table_1`.`column_1` / `table_1`.`column_1`) <= `table_2`.`column_2`)) AND `table_true`.`column_true`)"));
		assertThat(accept(visitor, transformer, not(and(or(match(columnA, like("%a%", null)), notMatch(columnB, like("%b%", null))), asBoolean(true)))), is("NOT (((`table_a`.`column_a` LIKE '%a%') OR (`table_b`.`column_b` NOT LIKE '%b%')) AND 1)"));
		assertThat(accept(visitor, transformer, not(and(or(match(columnA, like("%b%", null)), notMatch(columnA, like("%b%", null))), asBoolean(true)))), is("NOT (((`table_a`.`column_a` LIKE '%b%') OR (`table_a`.`column_a` NOT LIKE '%b%')) AND 1)"));
		assertThat(accept(visitor, transformer, not(and(or(match(columnA, like("%'\\%_a_\\%'%", '\'')), notMatch(columnB, like("%'\\%_b_\\%'%", '\''))), asBoolean(true)))), is("NOT (((`table_a`.`column_a` LIKE '%''\\%_a_\\%''%' ESCAPE '''') OR (`table_b`.`column_b` NOT LIKE '%''\\%_b_\\%''%' ESCAPE '''')) AND 1)"));
		assertThat(accept(visitor, transformer, not(and(or(match(columnA, like("%'\\%_b_\\%'%", '\'')), notMatch(columnA, like("%'\\%_b_\\%'%", '\''))), asBoolean(true)))), is("NOT (((`table_a`.`column_a` LIKE '%''\\%_b_\\%''%' ESCAPE '''') OR (`table_a`.`column_a` NOT LIKE '%''\\%_b_\\%''%' ESCAPE '''')) AND 1)"));
		assertThat(accept(visitor, transformer, not(and(or(match(columnA, like("%" + Like.escape("'\\%_a_\\%'", '#') + "%", '#')), notMatch(columnB, like("%" + Like.escape("'\\%_b_\\%'", '#') + "%", '#'))), asBoolean(true)))),
				is("NOT (((`table_a`.`column_a` LIKE '%''\\#%#_a#_\\#%''%' ESCAPE '#') OR (`table_b`.`column_b` NOT LIKE '%''\\#%#_b#_\\#%''%' ESCAPE '#')) AND 1)"));
		assertThat(accept(visitor, transformer, not(and(or(match(columnA, like("%" + Like.escape("'\\%_b_\\%'", '#') + "%", '#')), notMatch(columnA, like("%" + Like.escape("'\\%_b_\\%'", '#') + "%", '#'))), asBoolean(true)))),
				is("NOT (((`table_a`.`column_a` LIKE '%''\\#%#_b#_\\#%''%' ESCAPE '#') OR (`table_a`.`column_a` NOT LIKE '%''\\#%#_b#_\\#%''%' ESCAPE '#')) AND 1)"));
		assertThat(accept(visitor, transformer, not(and(or(match(columnA, like("%" + Like.escape("'\\%_a_\\%'", '\'') + "%", '\'')), notMatch(columnB, like("%" + Like.escape("'\\%_b_\\%'", '\'') + "%", '\''))), asBoolean(true)))),
				is("NOT (((`table_a`.`column_a` LIKE '%''\\''%''_a''_\\''%''%' ESCAPE '''') OR (`table_b`.`column_b` NOT LIKE '%''\\''%''_b''_\\''%''%' ESCAPE '''')) AND 1)"));
		assertThat(accept(visitor, transformer, not(and(or(match(columnA, like("%" + Like.escape("'\\%_b_\\%'", '\'') + "%", '\'')), notMatch(columnA, like("%" + Like.escape("'\\%_b_\\%'", '\'') + "%", '\''))), asBoolean(true)))),
				is("NOT (((`table_a`.`column_a` LIKE '%''\\''%''_b''_\\''%''%' ESCAPE '''') OR (`table_a`.`column_a` NOT LIKE '%''\\''%''_b''_\\''%''%' ESCAPE '''')) AND 1)"));
		assertThat(accept(visitor, transformer, not(and(or(match(columnA, like("%" + Like.escape("'\\%_a_\\%'") + "%", '\\')), notMatch(columnB, like("%" + Like.escape("'\\%_b_\\%'") + "%", '\\'))), asBoolean(true)))),
				is("NOT (((`table_a`.`column_a` LIKE '%''\\\\%\\_a\\_\\\\%''%' ESCAPE '\\') OR (`table_b`.`column_b` NOT LIKE '%''\\\\%\\_b\\_\\\\%''%' ESCAPE '\\')) AND 1)"));
		assertThat(accept(visitor, transformer, not(and(or(match(columnA, like("%" + Like.escape("'\\%_b_\\%'") + "%", '\\')), notMatch(columnA, like("%" + Like.escape("'\\%_b_\\%'") + "%", '\\'))), asBoolean(true)))),
				is("NOT (((`table_a`.`column_a` LIKE '%''\\\\%\\_b\\_\\\\%''%' ESCAPE '\\') OR (`table_a`.`column_a` NOT LIKE '%''\\\\%\\_b\\_\\\\%''%' ESCAPE '\\')) AND 1)"));
		assertThat(accept(visitor, transformer, not(and(or(columnTrue, asBoolean(null)), columnTrue))), is("NOT ((`table_true`.`column_true` OR NULL) AND `table_true`.`column_true`)"));
		assertThat(accept(visitor, transformer, not(and(or(equal(columnA, asString(null)), notEqual(columnA, asString(null))), columnTrue))), is("NOT (((`table_a`.`column_a` IS NULL) OR (`table_a`.`column_a` IS NOT NULL)) AND `table_true`.`column_true`)"));
		assertThat(accept(visitor, transformer, not(and(or(lessThan(column1, asBigDecimal(null)), greaterThanOrEqual(column1, asBigDecimal(null))), columnTrue))), is("NOT (((`table_1`.`column_1` < NULL) OR (`table_1`.`column_1` >= NULL)) AND `table_true`.`column_true`)"));
		assertThat(accept(visitor, transformer, not(and(or(lessThan(add(column1, column1), asBigDecimal(null)), greaterThanOrEqual(add(column1, column1), asBigDecimal(null))), columnTrue))),
				is("NOT ((((`table_1`.`column_1` + `table_1`.`column_1`) < NULL) OR ((`table_1`.`column_1` + `table_1`.`column_1`) >= NULL)) AND `table_true`.`column_true`)"));
		assertThat(accept(visitor, transformer, not(and(or(lessThan(subtract(column1, column1), asBigDecimal(null)), greaterThanOrEqual(subtract(column1, column1), asBigDecimal(null))), columnTrue))),
				is("NOT ((((`table_1`.`column_1` - `table_1`.`column_1`) < NULL) OR ((`table_1`.`column_1` - `table_1`.`column_1`) >= NULL)) AND `table_true`.`column_true`)"));
		assertThat(accept(visitor, transformer, not(and(or(lessThan(divide(column1, column1), asBigDecimal(null)), greaterThanOrEqual(divide(column1, column1), asBigDecimal(null))), columnTrue))),
				is("NOT ((((`table_1`.`column_1` * `table_1`.`column_1`) < NULL) OR ((`table_1`.`column_1` * `table_1`.`column_1`) >= NULL)) AND `table_true`.`column_true`)"));
		assertThat(accept(visitor, transformer, not(and(or(lessThan(multiply(column1, column1), asBigDecimal(null)), greaterThanOrEqual(multiply(column1, column1), asBigDecimal(null))), columnTrue))),
				is("NOT ((((`table_1`.`column_1` / `table_1`.`column_1`) < NULL) OR ((`table_1`.`column_1` / `table_1`.`column_1`) >= NULL)) AND `table_true`.`column_true`)"));
		assertThat(accept(visitor, transformer, not(and(or(greaterThan(column1, asBigDecimal(null)), lessThanOrEqual(column1, asBigDecimal(null))), columnTrue))), is("NOT (((`table_1`.`column_1` > NULL) OR (`table_1`.`column_1` <= NULL)) AND `table_true`.`column_true`)"));
		assertThat(accept(visitor, transformer, not(and(or(greaterThan(add(column1, column1), asBigDecimal(null)), lessThanOrEqual(add(column1, column1), asBigDecimal(null))), columnTrue))),
				is("NOT ((((`table_1`.`column_1` + `table_1`.`column_1`) > NULL) OR ((`table_1`.`column_1` + `table_1`.`column_1`) <= NULL)) AND `table_true`.`column_true`)"));
		assertThat(accept(visitor, transformer, not(and(or(greaterThan(subtract(column1, column1), asBigDecimal(null)), lessThanOrEqual(subtract(column1, column1), asBigDecimal(null))), columnTrue))),
				is("NOT ((((`table_1`.`column_1` - `table_1`.`column_1`) > NULL) OR ((`table_1`.`column_1` - `table_1`.`column_1`) <= NULL)) AND `table_true`.`column_true`)"));
		assertThat(accept(visitor, transformer, not(and(or(greaterThan(divide(column1, column1), asBigDecimal(null)), lessThanOrEqual(divide(column1, column1), asBigDecimal(null))), columnTrue))),
				is("NOT ((((`table_1`.`column_1` * `table_1`.`column_1`) > NULL) OR ((`table_1`.`column_1` * `table_1`.`column_1`) <= NULL)) AND `table_true`.`column_true`)"));
		assertThat(accept(visitor, transformer, not(and(or(greaterThan(multiply(column1, column1), asBigDecimal(null)), lessThanOrEqual(multiply(column1, column1), asBigDecimal(null))), columnTrue))),
				is("NOT ((((`table_1`.`column_1` / `table_1`.`column_1`) > NULL) OR ((`table_1`.`column_1` / `table_1`.`column_1`) <= NULL)) AND `table_true`.`column_true`)"));
		assertThat(accept(visitor, transformer, not(and(or(match(columnA, like(null, null)), notMatch(columnA, like(null, null))), columnTrue))), is("NOT (((`table_a`.`column_a` LIKE NULL) OR (`table_a`.`column_a` NOT LIKE NULL)) AND `table_true`.`column_true`)"));
	}

	@Test
	public void testBackslash() {
		final Visitor<String> visitor = new WhereVisitor(true);
		final Transformer transformer = new Transformer() {
		};
		final Column<Boolean> columnTrue = asBoolean("table_true", "column_true");
		final Column<Boolean> columnFalse = asBoolean("table_false", "column_false");
		final Column<String> columnA = asString("table_a", "column_a");
		final Column<String> columnB = asString("table_b", "column_b");
		final Column<BigDecimal> column1 = asBigDecimal("table_1", "column_1");
		final Column<BigDecimal> column2 = asBigDecimal("table_2", "column_2");
		assertThat(accept(visitor, transformer, not(and(or(asBoolean(true), asBoolean(false)), asBoolean(true)))), is("NOT ((1 OR 0) AND 1)"));
		assertThat(accept(visitor, transformer, not(and(or(equal(asString("a"), asString("a")), notEqual(asString("b"), asString("b"))), asBoolean(true)))), is("NOT ((('a' = 'a') OR ('b' <> 'b')) AND 1)"));
		assertThat(accept(visitor, transformer, not(and(or(equal(asString("a"), asString("b")), notEqual(asString("a"), asString("b"))), asBoolean(true)))), is("NOT ((('a' = 'b') OR ('a' <> 'b')) AND 1)"));
		assertThat(accept(visitor, transformer, not(and(or(lessThan(asInteger(1), asInteger(2)), greaterThanOrEqual(asInteger(1), asInteger(2))), asBoolean(true)))), is("NOT (((1 < 2) OR (1 >= 2)) AND 1)"));
		assertThat(accept(visitor, transformer, not(and(or(lessThan(add(asInteger(1), asInteger(1)), asInteger(2)), greaterThanOrEqual(add(asInteger(1), asInteger(1)), asInteger(2))), asBoolean(true)))), is("NOT ((((1 + 1) < 2) OR ((1 + 1) >= 2)) AND 1)"));
		assertThat(accept(visitor, transformer, not(and(or(lessThan(subtract(asInteger(1), asInteger(1)), asInteger(2)), greaterThanOrEqual(subtract(asInteger(1), asInteger(1)), asInteger(2))), asBoolean(true)))), is("NOT ((((1 - 1) < 2) OR ((1 - 1) >= 2)) AND 1)"));
		assertThat(accept(visitor, transformer, not(and(or(lessThan(divide(asInteger(1), asInteger(1)), asInteger(2)), greaterThanOrEqual(divide(asInteger(1), asInteger(1)), asInteger(2))), asBoolean(true)))), is("NOT ((((1 * 1) < 2) OR ((1 * 1) >= 2)) AND 1)"));
		assertThat(accept(visitor, transformer, not(and(or(lessThan(multiply(asInteger(1), asInteger(1)), asInteger(2)), greaterThanOrEqual(multiply(asInteger(1), asInteger(1)), asInteger(2))), asBoolean(true)))), is("NOT ((((1 / 1) < 2) OR ((1 / 1) >= 2)) AND 1)"));
		assertThat(accept(visitor, transformer, not(and(or(greaterThan(asInteger(1), asInteger(2)), lessThanOrEqual(asInteger(1), asInteger(2))), asBoolean(true)))), is("NOT (((1 > 2) OR (1 <= 2)) AND 1)"));
		assertThat(accept(visitor, transformer, not(and(or(greaterThan(add(asInteger(1), asInteger(1)), asInteger(2)), lessThanOrEqual(add(asInteger(1), asInteger(1)), asInteger(2))), asBoolean(true)))), is("NOT ((((1 + 1) > 2) OR ((1 + 1) <= 2)) AND 1)"));
		assertThat(accept(visitor, transformer, not(and(or(greaterThan(subtract(asInteger(1), asInteger(1)), asInteger(2)), lessThanOrEqual(subtract(asInteger(1), asInteger(1)), asInteger(2))), asBoolean(true)))), is("NOT ((((1 - 1) > 2) OR ((1 - 1) <= 2)) AND 1)"));
		assertThat(accept(visitor, transformer, not(and(or(greaterThan(divide(asInteger(1), asInteger(1)), asInteger(2)), lessThanOrEqual(divide(asInteger(1), asInteger(1)), asInteger(2))), asBoolean(true)))), is("NOT ((((1 * 1) > 2) OR ((1 * 1) <= 2)) AND 1)"));
		assertThat(accept(visitor, transformer, not(and(or(greaterThan(multiply(asInteger(1), asInteger(1)), asInteger(2)), lessThanOrEqual(multiply(asInteger(1), asInteger(1)), asInteger(2))), asBoolean(true)))), is("NOT ((((1 / 1) > 2) OR ((1 / 1) <= 2)) AND 1)"));
		assertThat(accept(visitor, transformer, not(and(or(match(asString("a"), like("%a%", null)), notMatch(asString("b"), like("%b%", null))), asBoolean(true)))), is("NOT ((('a' LIKE '%a%') OR ('b' NOT LIKE '%b%')) AND 1)"));
		assertThat(accept(visitor, transformer, not(and(or(match(asString("a"), like("%b%", null)), notMatch(asString("a"), like("%b%", null))), asBoolean(true)))), is("NOT ((('a' LIKE '%b%') OR ('a' NOT LIKE '%b%')) AND 1)"));
		assertThat(accept(visitor, transformer, not(and(or(match(asString("a"), like("%'\\%_a_\\%'%", '\'')), notMatch(asString("b"), like("%'\\%_b_\\%'%", '\''))), asBoolean(true)))), is("NOT ((('a' LIKE '%''\\\\%_a_\\\\%''%' ESCAPE '''') OR ('b' NOT LIKE '%''\\\\%_b_\\\\%''%' ESCAPE '''')) AND 1)"));
		assertThat(accept(visitor, transformer, not(and(or(match(asString("a"), like("%'\\%_b_\\%'%", '\'')), notMatch(asString("a"), like("%'\\%_b_\\%'%", '\''))), asBoolean(true)))), is("NOT ((('a' LIKE '%''\\\\%_b_\\\\%''%' ESCAPE '''') OR ('a' NOT LIKE '%''\\\\%_b_\\\\%''%' ESCAPE '''')) AND 1)"));
		assertThat(accept(visitor, transformer, not(and(or(match(asString("a"), like("%" + Like.escape("'\\%_a_\\%'", '#') + "%", '#')), notMatch(asString("b"), like("%" + Like.escape("'\\%_b_\\%'", '#') + "%", '#'))), asBoolean(true)))),
				is("NOT ((('a' LIKE '%''\\\\#%#_a#_\\\\#%''%' ESCAPE '#') OR ('b' NOT LIKE '%''\\\\#%#_b#_\\\\#%''%' ESCAPE '#')) AND 1)"));
		assertThat(accept(visitor, transformer, not(and(or(match(asString("a"), like("%" + Like.escape("'\\%_b_\\%'", '#') + "%", '#')), notMatch(asString("a"), like("%" + Like.escape("'\\%_b_\\%'", '#') + "%", '#'))), asBoolean(true)))),
				is("NOT ((('a' LIKE '%''\\\\#%#_b#_\\\\#%''%' ESCAPE '#') OR ('a' NOT LIKE '%''\\\\#%#_b#_\\\\#%''%' ESCAPE '#')) AND 1)"));
		assertThat(accept(visitor, transformer, not(and(or(match(asString("a"), like("%" + Like.escape("'\\%_a_\\%'", '\'') + "%", '\'')), notMatch(asString("b"), like("%" + Like.escape("'\\%_b_\\%'", '\'') + "%", '\''))), asBoolean(true)))),
				is("NOT ((('a' LIKE '%''\\\\''%''_a''_\\\\''%''%' ESCAPE '''') OR ('b' NOT LIKE '%''\\\\''%''_b''_\\\\''%''%' ESCAPE '''')) AND 1)"));
		assertThat(accept(visitor, transformer, not(and(or(match(asString("a"), like("%" + Like.escape("'\\%_b_\\%'", '\'') + "%", '\'')), notMatch(asString("a"), like("%" + Like.escape("'\\%_b_\\%'", '\'') + "%", '\''))), asBoolean(true)))),
				is("NOT ((('a' LIKE '%''\\\\''%''_b''_\\\\''%''%' ESCAPE '''') OR ('a' NOT LIKE '%''\\\\''%''_b''_\\\\''%''%' ESCAPE '''')) AND 1)"));
		assertThat(accept(visitor, transformer, not(and(or(match(asString("a"), like("%" + Like.escape("'\\%_a_\\%'") + "%", '\\')), notMatch(asString("b"), like("%" + Like.escape("'\\%_b_\\%'") + "%", '\\'))), asBoolean(true)))),
				is("NOT ((('a' LIKE '%''\\\\\\\\%\\\\_a\\\\_\\\\\\\\%''%' ESCAPE '\\\\') OR ('b' NOT LIKE '%''\\\\\\\\%\\\\_b\\\\_\\\\\\\\%''%' ESCAPE '\\\\')) AND 1)"));
		assertThat(accept(visitor, transformer, not(and(or(match(asString("a"), like("%" + Like.escape("'\\%_b_\\%'") + "%", '\\')), notMatch(asString("a"), like("%" + Like.escape("'\\%_b_\\%'") + "%", '\\'))), asBoolean(true)))),
				is("NOT ((('a' LIKE '%''\\\\\\\\%\\\\_b\\\\_\\\\\\\\%''%' ESCAPE '\\\\') OR ('a' NOT LIKE '%''\\\\\\\\%\\\\_b\\\\_\\\\\\\\%''%' ESCAPE '\\\\')) AND 1)"));
		assertThat(accept(visitor, transformer, not(and(or(lessThan(asBigDecimal(new BigDecimal(1)), asBigDecimal(new BigDecimal(2))), greaterThanOrEqual(asBigDecimal(new BigDecimal(1)), asBigDecimal(new BigDecimal(2)))), asBoolean(true)))), is("NOT (((1 < 2) OR (1 >= 2)) AND 1)"));
		assertThat(accept(visitor, transformer, not(and(or(lessThan(add(asBigDecimal(new BigDecimal(1)), asBigDecimal(new BigDecimal(1))), asBigDecimal(new BigDecimal(2))), greaterThanOrEqual(add(asBigDecimal(new BigDecimal(1)), asBigDecimal(new BigDecimal(1))), asBigDecimal(new BigDecimal(2)))), asBoolean(true)))),
				is("NOT ((((1 + 1) < 2) OR ((1 + 1) >= 2)) AND 1)"));
		assertThat(
				accept(visitor, transformer,
						not(and(or(lessThan(subtract(asBigDecimal(new BigDecimal(1)), asBigDecimal(new BigDecimal(1))), asBigDecimal(new BigDecimal(2))), greaterThanOrEqual(subtract(asBigDecimal(new BigDecimal(1)), asBigDecimal(new BigDecimal(1))), asBigDecimal(new BigDecimal(2)))), asBoolean(true)))),
				is("NOT ((((1 - 1) < 2) OR ((1 - 1) >= 2)) AND 1)"));
		assertThat(
				accept(visitor, transformer, not(and(or(lessThan(divide(asBigDecimal(new BigDecimal(1)), asBigDecimal(new BigDecimal(1))), asBigDecimal(new BigDecimal(2))), greaterThanOrEqual(divide(asBigDecimal(new BigDecimal(1)), asBigDecimal(new BigDecimal(1))), asBigDecimal(new BigDecimal(2)))), asBoolean(true)))),
				is("NOT ((((1 * 1) < 2) OR ((1 * 1) >= 2)) AND 1)"));
		assertThat(
				accept(visitor, transformer,
						not(and(or(lessThan(multiply(asBigDecimal(new BigDecimal(1)), asBigDecimal(new BigDecimal(1))), asBigDecimal(new BigDecimal(2))), greaterThanOrEqual(multiply(asBigDecimal(new BigDecimal(1)), asBigDecimal(new BigDecimal(1))), asBigDecimal(new BigDecimal(2)))), asBoolean(true)))),
				is("NOT ((((1 / 1) < 2) OR ((1 / 1) >= 2)) AND 1)"));
		assertThat(accept(visitor, transformer, not(and(or(greaterThan(asBigDecimal(new BigDecimal(1)), asBigDecimal(new BigDecimal(2))), lessThanOrEqual(asBigDecimal(new BigDecimal(1)), asBigDecimal(new BigDecimal(2)))), asBoolean(true)))), is("NOT (((1 > 2) OR (1 <= 2)) AND 1)"));
		assertThat(accept(visitor, transformer, not(and(or(greaterThan(add(asBigDecimal(new BigDecimal(1)), asBigDecimal(new BigDecimal(1))), asBigDecimal(new BigDecimal(2))), lessThanOrEqual(add(asBigDecimal(new BigDecimal(1)), asBigDecimal(new BigDecimal(1))), asBigDecimal(new BigDecimal(2)))), asBoolean(true)))),
				is("NOT ((((1 + 1) > 2) OR ((1 + 1) <= 2)) AND 1)"));
		assertThat(
				accept(visitor, transformer,
						not(and(or(greaterThan(subtract(asBigDecimal(new BigDecimal(1)), asBigDecimal(new BigDecimal(1))), asBigDecimal(new BigDecimal(2))), lessThanOrEqual(subtract(asBigDecimal(new BigDecimal(1)), asBigDecimal(new BigDecimal(1))), asBigDecimal(new BigDecimal(2)))), asBoolean(true)))),
				is("NOT ((((1 - 1) > 2) OR ((1 - 1) <= 2)) AND 1)"));
		assertThat(
				accept(visitor, transformer, not(and(or(greaterThan(divide(asBigDecimal(new BigDecimal(1)), asBigDecimal(new BigDecimal(1))), asBigDecimal(new BigDecimal(2))), lessThanOrEqual(divide(asBigDecimal(new BigDecimal(1)), asBigDecimal(new BigDecimal(1))), asBigDecimal(new BigDecimal(2)))), asBoolean(true)))),
				is("NOT ((((1 * 1) > 2) OR ((1 * 1) <= 2)) AND 1)"));
		assertThat(
				accept(visitor, transformer,
						not(and(or(greaterThan(multiply(asBigDecimal(new BigDecimal(1)), asBigDecimal(new BigDecimal(1))), asBigDecimal(new BigDecimal(2))), lessThanOrEqual(multiply(asBigDecimal(new BigDecimal(1)), asBigDecimal(new BigDecimal(1))), asBigDecimal(new BigDecimal(2)))), asBoolean(true)))),
				is("NOT ((((1 / 1) > 2) OR ((1 / 1) <= 2)) AND 1)"));
		assertThat(accept(visitor, transformer, not(and(or(columnTrue, columnFalse), columnTrue))), is("NOT ((`table_true`.`column_true` OR `table_false`.`column_false`) AND `table_true`.`column_true`)"));
		assertThat(accept(visitor, transformer, not(and(or(equal(columnA, columnA), notEqual(columnB, columnB)), columnTrue))), is("NOT (((`table_a`.`column_a` = `table_a`.`column_a`) OR (`table_b`.`column_b` <> `table_b`.`column_b`)) AND `table_true`.`column_true`)"));
		assertThat(accept(visitor, transformer, not(and(or(equal(columnA, columnB), notEqual(columnA, columnB)), columnTrue))), is("NOT (((`table_a`.`column_a` = `table_b`.`column_b`) OR (`table_a`.`column_a` <> `table_b`.`column_b`)) AND `table_true`.`column_true`)"));
		assertThat(accept(visitor, transformer, not(and(or(lessThan(column1, column2), greaterThanOrEqual(column1, column2)), columnTrue))), is("NOT (((`table_1`.`column_1` < `table_2`.`column_2`) OR (`table_1`.`column_1` >= `table_2`.`column_2`)) AND `table_true`.`column_true`)"));
		assertThat(accept(visitor, transformer, not(and(or(lessThan(add(column1, column1), column2), greaterThanOrEqual(add(column1, column1), column2)), columnTrue))),
				is("NOT ((((`table_1`.`column_1` + `table_1`.`column_1`) < `table_2`.`column_2`) OR ((`table_1`.`column_1` + `table_1`.`column_1`) >= `table_2`.`column_2`)) AND `table_true`.`column_true`)"));
		assertThat(accept(visitor, transformer, not(and(or(lessThan(subtract(column1, column1), column2), greaterThanOrEqual(subtract(column1, column1), column2)), columnTrue))),
				is("NOT ((((`table_1`.`column_1` - `table_1`.`column_1`) < `table_2`.`column_2`) OR ((`table_1`.`column_1` - `table_1`.`column_1`) >= `table_2`.`column_2`)) AND `table_true`.`column_true`)"));
		assertThat(accept(visitor, transformer, not(and(or(lessThan(divide(column1, column1), column2), greaterThanOrEqual(divide(column1, column1), column2)), columnTrue))),
				is("NOT ((((`table_1`.`column_1` * `table_1`.`column_1`) < `table_2`.`column_2`) OR ((`table_1`.`column_1` * `table_1`.`column_1`) >= `table_2`.`column_2`)) AND `table_true`.`column_true`)"));
		assertThat(accept(visitor, transformer, not(and(or(lessThan(multiply(column1, column1), column2), greaterThanOrEqual(multiply(column1, column1), column2)), columnTrue))),
				is("NOT ((((`table_1`.`column_1` / `table_1`.`column_1`) < `table_2`.`column_2`) OR ((`table_1`.`column_1` / `table_1`.`column_1`) >= `table_2`.`column_2`)) AND `table_true`.`column_true`)"));
		assertThat(accept(visitor, transformer, not(and(or(greaterThan(column1, column2), lessThanOrEqual(column1, column2)), columnTrue))), is("NOT (((`table_1`.`column_1` > `table_2`.`column_2`) OR (`table_1`.`column_1` <= `table_2`.`column_2`)) AND `table_true`.`column_true`)"));
		assertThat(accept(visitor, transformer, not(and(or(greaterThan(add(column1, column1), column2), lessThanOrEqual(add(column1, column1), column2)), columnTrue))),
				is("NOT ((((`table_1`.`column_1` + `table_1`.`column_1`) > `table_2`.`column_2`) OR ((`table_1`.`column_1` + `table_1`.`column_1`) <= `table_2`.`column_2`)) AND `table_true`.`column_true`)"));
		assertThat(accept(visitor, transformer, not(and(or(greaterThan(subtract(column1, column1), column2), lessThanOrEqual(subtract(column1, column1), column2)), columnTrue))),
				is("NOT ((((`table_1`.`column_1` - `table_1`.`column_1`) > `table_2`.`column_2`) OR ((`table_1`.`column_1` - `table_1`.`column_1`) <= `table_2`.`column_2`)) AND `table_true`.`column_true`)"));
		assertThat(accept(visitor, transformer, not(and(or(greaterThan(divide(column1, column1), column2), lessThanOrEqual(divide(column1, column1), column2)), columnTrue))),
				is("NOT ((((`table_1`.`column_1` * `table_1`.`column_1`) > `table_2`.`column_2`) OR ((`table_1`.`column_1` * `table_1`.`column_1`) <= `table_2`.`column_2`)) AND `table_true`.`column_true`)"));
		assertThat(accept(visitor, transformer, not(and(or(greaterThan(multiply(column1, column1), column2), lessThanOrEqual(multiply(column1, column1), column2)), columnTrue))),
				is("NOT ((((`table_1`.`column_1` / `table_1`.`column_1`) > `table_2`.`column_2`) OR ((`table_1`.`column_1` / `table_1`.`column_1`) <= `table_2`.`column_2`)) AND `table_true`.`column_true`)"));
		assertThat(accept(visitor, transformer, not(and(or(match(columnA, like("%a%", null)), notMatch(columnB, like("%b%", null))), asBoolean(true)))), is("NOT (((`table_a`.`column_a` LIKE '%a%') OR (`table_b`.`column_b` NOT LIKE '%b%')) AND 1)"));
		assertThat(accept(visitor, transformer, not(and(or(match(columnA, like("%b%", null)), notMatch(columnA, like("%b%", null))), asBoolean(true)))), is("NOT (((`table_a`.`column_a` LIKE '%b%') OR (`table_a`.`column_a` NOT LIKE '%b%')) AND 1)"));
		assertThat(accept(visitor, transformer, not(and(or(match(columnA, like("%'\\%_a_\\%'%", '\'')), notMatch(columnB, like("%'\\%_b_\\%'%", '\''))), asBoolean(true)))),
				is("NOT (((`table_a`.`column_a` LIKE '%''\\\\%_a_\\\\%''%' ESCAPE '''') OR (`table_b`.`column_b` NOT LIKE '%''\\\\%_b_\\\\%''%' ESCAPE '''')) AND 1)"));
		assertThat(accept(visitor, transformer, not(and(or(match(columnA, like("%'\\%_b_\\%'%", '\'')), notMatch(columnA, like("%'\\%_b_\\%'%", '\''))), asBoolean(true)))),
				is("NOT (((`table_a`.`column_a` LIKE '%''\\\\%_b_\\\\%''%' ESCAPE '''') OR (`table_a`.`column_a` NOT LIKE '%''\\\\%_b_\\\\%''%' ESCAPE '''')) AND 1)"));
		assertThat(accept(visitor, transformer, not(and(or(match(columnA, like("%" + Like.escape("'\\%_a_\\%'", '#') + "%", '#')), notMatch(columnB, like("%" + Like.escape("'\\%_b_\\%'", '#') + "%", '#'))), asBoolean(true)))),
				is("NOT (((`table_a`.`column_a` LIKE '%''\\\\#%#_a#_\\\\#%''%' ESCAPE '#') OR (`table_b`.`column_b` NOT LIKE '%''\\\\#%#_b#_\\\\#%''%' ESCAPE '#')) AND 1)"));
		assertThat(accept(visitor, transformer, not(and(or(match(columnA, like("%" + Like.escape("'\\%_b_\\%'", '#') + "%", '#')), notMatch(columnA, like("%" + Like.escape("'\\%_b_\\%'", '#') + "%", '#'))), asBoolean(true)))),
				is("NOT (((`table_a`.`column_a` LIKE '%''\\\\#%#_b#_\\\\#%''%' ESCAPE '#') OR (`table_a`.`column_a` NOT LIKE '%''\\\\#%#_b#_\\\\#%''%' ESCAPE '#')) AND 1)"));
		assertThat(accept(visitor, transformer, not(and(or(match(columnA, like("%" + Like.escape("'\\%_a_\\%'", '\'') + "%", '\'')), notMatch(columnB, like("%" + Like.escape("'\\%_b_\\%'", '\'') + "%", '\''))), asBoolean(true)))),
				is("NOT (((`table_a`.`column_a` LIKE '%''\\\\''%''_a''_\\\\''%''%' ESCAPE '''') OR (`table_b`.`column_b` NOT LIKE '%''\\\\''%''_b''_\\\\''%''%' ESCAPE '''')) AND 1)"));
		assertThat(accept(visitor, transformer, not(and(or(match(columnA, like("%" + Like.escape("'\\%_b_\\%'", '\'') + "%", '\'')), notMatch(columnA, like("%" + Like.escape("'\\%_b_\\%'", '\'') + "%", '\''))), asBoolean(true)))),
				is("NOT (((`table_a`.`column_a` LIKE '%''\\\\''%''_b''_\\\\''%''%' ESCAPE '''') OR (`table_a`.`column_a` NOT LIKE '%''\\\\''%''_b''_\\\\''%''%' ESCAPE '''')) AND 1)"));
		assertThat(accept(visitor, transformer, not(and(or(match(columnA, like("%" + Like.escape("'\\%_a_\\%'") + "%", '\\')), notMatch(columnB, like("%" + Like.escape("'\\%_b_\\%'") + "%", '\\'))), asBoolean(true)))),
				is("NOT (((`table_a`.`column_a` LIKE '%''\\\\\\\\%\\\\_a\\\\_\\\\\\\\%''%' ESCAPE '\\\\') OR (`table_b`.`column_b` NOT LIKE '%''\\\\\\\\%\\\\_b\\\\_\\\\\\\\%''%' ESCAPE '\\\\')) AND 1)"));
		assertThat(accept(visitor, transformer, not(and(or(match(columnA, like("%" + Like.escape("'\\%_b_\\%'") + "%", '\\')), notMatch(columnA, like("%" + Like.escape("'\\%_b_\\%'") + "%", '\\'))), asBoolean(true)))),
				is("NOT (((`table_a`.`column_a` LIKE '%''\\\\\\\\%\\\\_b\\\\_\\\\\\\\%''%' ESCAPE '\\\\') OR (`table_a`.`column_a` NOT LIKE '%''\\\\\\\\%\\\\_b\\\\_\\\\\\\\%''%' ESCAPE '\\\\')) AND 1)"));
		assertThat(accept(visitor, transformer, not(and(or(columnTrue, asBoolean(null)), columnTrue))), is("NOT ((`table_true`.`column_true` OR NULL) AND `table_true`.`column_true`)"));
		assertThat(accept(visitor, transformer, not(and(or(equal(columnA, asString(null)), notEqual(columnA, asString(null))), columnTrue))), is("NOT (((`table_a`.`column_a` IS NULL) OR (`table_a`.`column_a` IS NOT NULL)) AND `table_true`.`column_true`)"));
		assertThat(accept(visitor, transformer, not(and(or(lessThan(column1, asBigDecimal(null)), greaterThanOrEqual(column1, asBigDecimal(null))), columnTrue))), is("NOT (((`table_1`.`column_1` < NULL) OR (`table_1`.`column_1` >= NULL)) AND `table_true`.`column_true`)"));
		assertThat(accept(visitor, transformer, not(and(or(lessThan(add(column1, column1), asBigDecimal(null)), greaterThanOrEqual(add(column1, column1), asBigDecimal(null))), columnTrue))),
				is("NOT ((((`table_1`.`column_1` + `table_1`.`column_1`) < NULL) OR ((`table_1`.`column_1` + `table_1`.`column_1`) >= NULL)) AND `table_true`.`column_true`)"));
		assertThat(accept(visitor, transformer, not(and(or(lessThan(subtract(column1, column1), asBigDecimal(null)), greaterThanOrEqual(subtract(column1, column1), asBigDecimal(null))), columnTrue))),
				is("NOT ((((`table_1`.`column_1` - `table_1`.`column_1`) < NULL) OR ((`table_1`.`column_1` - `table_1`.`column_1`) >= NULL)) AND `table_true`.`column_true`)"));
		assertThat(accept(visitor, transformer, not(and(or(lessThan(divide(column1, column1), asBigDecimal(null)), greaterThanOrEqual(divide(column1, column1), asBigDecimal(null))), columnTrue))),
				is("NOT ((((`table_1`.`column_1` * `table_1`.`column_1`) < NULL) OR ((`table_1`.`column_1` * `table_1`.`column_1`) >= NULL)) AND `table_true`.`column_true`)"));
		assertThat(accept(visitor, transformer, not(and(or(lessThan(multiply(column1, column1), asBigDecimal(null)), greaterThanOrEqual(multiply(column1, column1), asBigDecimal(null))), columnTrue))),
				is("NOT ((((`table_1`.`column_1` / `table_1`.`column_1`) < NULL) OR ((`table_1`.`column_1` / `table_1`.`column_1`) >= NULL)) AND `table_true`.`column_true`)"));
		assertThat(accept(visitor, transformer, not(and(or(greaterThan(column1, asBigDecimal(null)), lessThanOrEqual(column1, asBigDecimal(null))), columnTrue))), is("NOT (((`table_1`.`column_1` > NULL) OR (`table_1`.`column_1` <= NULL)) AND `table_true`.`column_true`)"));
		assertThat(accept(visitor, transformer, not(and(or(greaterThan(add(column1, column1), asBigDecimal(null)), lessThanOrEqual(add(column1, column1), asBigDecimal(null))), columnTrue))),
				is("NOT ((((`table_1`.`column_1` + `table_1`.`column_1`) > NULL) OR ((`table_1`.`column_1` + `table_1`.`column_1`) <= NULL)) AND `table_true`.`column_true`)"));
		assertThat(accept(visitor, transformer, not(and(or(greaterThan(subtract(column1, column1), asBigDecimal(null)), lessThanOrEqual(subtract(column1, column1), asBigDecimal(null))), columnTrue))),
				is("NOT ((((`table_1`.`column_1` - `table_1`.`column_1`) > NULL) OR ((`table_1`.`column_1` - `table_1`.`column_1`) <= NULL)) AND `table_true`.`column_true`)"));
		assertThat(accept(visitor, transformer, not(and(or(greaterThan(divide(column1, column1), asBigDecimal(null)), lessThanOrEqual(divide(column1, column1), asBigDecimal(null))), columnTrue))),
				is("NOT ((((`table_1`.`column_1` * `table_1`.`column_1`) > NULL) OR ((`table_1`.`column_1` * `table_1`.`column_1`) <= NULL)) AND `table_true`.`column_true`)"));
		assertThat(accept(visitor, transformer, not(and(or(greaterThan(multiply(column1, column1), asBigDecimal(null)), lessThanOrEqual(multiply(column1, column1), asBigDecimal(null))), columnTrue))),
				is("NOT ((((`table_1`.`column_1` / `table_1`.`column_1`) > NULL) OR ((`table_1`.`column_1` / `table_1`.`column_1`) <= NULL)) AND `table_true`.`column_true`)"));
		assertThat(accept(visitor, transformer, not(and(or(match(columnA, like(null, null)), notMatch(columnA, like(null, null))), columnTrue))), is("NOT (((`table_a`.`column_a` LIKE NULL) OR (`table_a`.`column_a` NOT LIKE NULL)) AND `table_true`.`column_true`)"));
	}

	@Test
	public void testAndOr() {
		final Visitor<String> visitor = new WhereVisitor();
		final Transformer transformer = new Transformer() {
		};
		final Function<Boolean, Expression<Boolean>> function = new Function<Boolean, Expression<Boolean>>() {
			@Override
			@Nullable
			public Expression<Boolean> apply(@Nullable final Boolean input) {
				return asBoolean(input);
			}
		};
		assertThat(accept(visitor, transformer, And.and(new Boolean[] { true, false, true, false }, function)), is("(((1 AND 0) AND 1) AND 0)"));
		assertThat(accept(visitor, transformer, And.and(new Boolean[] { true, false, true }, function)), is("((1 AND 0) AND 1)"));
		assertThat(accept(visitor, transformer, And.and(new Boolean[] { true, false }, function)), is("(1 AND 0)"));
		assertThat(accept(visitor, transformer, And.and(new Boolean[] { true }, function)), is("(1 AND 1)"));
		assertThat(accept(visitor, transformer, And.and(new Boolean[] {}, function)), is("(NULL AND NULL)"));
		assertThat(accept(visitor, transformer, And.and(null, function)), is("(NULL AND NULL)"));
		assertThat(accept(visitor, transformer, Or.or(new Boolean[] { true, false, true, false }, function)), is("(((1 OR 0) OR 1) OR 0)"));
		assertThat(accept(visitor, transformer, Or.or(new Boolean[] { true, false, true }, function)), is("((1 OR 0) OR 1)"));
		assertThat(accept(visitor, transformer, Or.or(new Boolean[] { true, false }, function)), is("(1 OR 0)"));
		assertThat(accept(visitor, transformer, Or.or(new Boolean[] { true }, function)), is("(0 OR 1)"));
		assertThat(accept(visitor, transformer, Or.or(new Boolean[] {}, function)), is("(NULL OR NULL)"));
		assertThat(accept(visitor, transformer, Or.or(null, function)), is("(NULL OR NULL)"));
	}

	@SuppressWarnings("unchecked")
	private static <T extends Expression<?>> String accept(final Visitor<String> visitor, final Transformer transformer, final T expression) {
		return ((T) expression.accept(transformer)).accept(visitor);
	}
}
