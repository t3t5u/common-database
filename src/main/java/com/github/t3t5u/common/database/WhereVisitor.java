package com.github.t3t5u.common.database;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.util.Date;

import com.github.t3t5u.common.expression.Add;
import com.github.t3t5u.common.expression.And;
import com.github.t3t5u.common.expression.BooleanLiteral;
import com.github.t3t5u.common.expression.CharSequenceLiteral;
import com.github.t3t5u.common.expression.CharacterLiteral;
import com.github.t3t5u.common.expression.DateLiteral;
import com.github.t3t5u.common.expression.Divide;
import com.github.t3t5u.common.expression.EnumLiteral;
import com.github.t3t5u.common.expression.Equal;
import com.github.t3t5u.common.expression.GreaterThan;
import com.github.t3t5u.common.expression.GreaterThanOrEqual;
import com.github.t3t5u.common.expression.Keyword;
import com.github.t3t5u.common.expression.KeywordMatcher;
import com.github.t3t5u.common.expression.LessThan;
import com.github.t3t5u.common.expression.LessThanOrEqual;
import com.github.t3t5u.common.expression.Match;
import com.github.t3t5u.common.expression.MatchType;
import com.github.t3t5u.common.expression.Matcher;
import com.github.t3t5u.common.expression.Multiply;
import com.github.t3t5u.common.expression.Negate;
import com.github.t3t5u.common.expression.Not;
import com.github.t3t5u.common.expression.NotEqual;
import com.github.t3t5u.common.expression.NotMatch;
import com.github.t3t5u.common.expression.NullLiteral;
import com.github.t3t5u.common.expression.NumberLiteral;
import com.github.t3t5u.common.expression.Or;
import com.github.t3t5u.common.expression.Pattern;
import com.github.t3t5u.common.expression.Subtract;

public class WhereVisitor implements Visitor<String> {
	protected static final String NULL = "NULL";
	private final boolean backslash;

	public WhereVisitor() {
		this(false);
	}

	public WhereVisitor(final boolean backslash) {
		this.backslash = backslash;
	}

	@Override
	public <T extends Serializable> String visit(final NullLiteral<T> expression) {
		return NULL;
	}

	@Override
	public String visit(final BooleanLiteral expression) {
		final Boolean value = expression.evaluate();
		return value == null ? NULL : value ? "1" : "0";
	}

	@Override
	public <T extends Number> String visit(final NumberLiteral<T> expression) {
		final Number value = expression.evaluate();
		final NumberFormat format = expression.getFormat();
		return value == null ? NULL : format == null ? value.toString() : format.format(value);
	}

	@Override
	public String visit(final CharacterLiteral expression) {
		final Character value = expression.evaluate();
		return value == null ? NULL : String.format("'%s'", escape(value));
	}

	@Override
	public <T extends CharSequence & Serializable> String visit(final CharSequenceLiteral<T> expression) {
		final CharSequence value = expression.evaluate();
		return value == null ? NULL : String.format("'%s'", escape(value));
	}

	@Override
	public <T extends Enum<T>> String visit(final EnumLiteral<T> expression) {
		final Enum<?> value = expression.evaluate();
		return value == null ? NULL : String.format("'%s'", escape(value.name()));
	}

	@Override
	public String visit(final DateLiteral expression) {
		final Date value = expression.evaluate();
		final DateFormat format = expression.getFormat();
		return value == null ? NULL : format == null ? value.toString() : format.format(value);
	}

	@Override
	public String visit(final Not expression) {
		return String.format("NOT %s", expression.getExpression().accept(this));
	}

	@Override
	public <T extends Number> String visit(final Negate<T> expression) {
		return String.format("-(%s)", expression.getExpression().accept(this));
	}

	@Override
	public String visit(final And expression) {
		return String.format("(%s AND %s)", expression.getLeftExpression().accept(this), expression.getRightExpression().accept(this));
	}

	@Override
	public String visit(final Or expression) {
		return String.format("(%s OR %s)", expression.getLeftExpression().accept(this), expression.getRightExpression().accept(this));
	}

	@Override
	public <T extends Number> String visit(final Add<T> expression) {
		return String.format("(%s + %s)", expression.getLeftExpression().accept(this), expression.getRightExpression().accept(this));
	}

	@Override
	public <T extends Number> String visit(final Subtract<T> expression) {
		return String.format("(%s - %s)", expression.getLeftExpression().accept(this), expression.getRightExpression().accept(this));
	}

	@Override
	public <T extends Number> String visit(final Divide<T> expression) {
		return String.format("(%s * %s)", expression.getLeftExpression().accept(this), expression.getRightExpression().accept(this));
	}

	@Override
	public <T extends Number> String visit(final Multiply<T> expression) {
		return String.format("(%s / %s)", expression.getLeftExpression().accept(this), expression.getRightExpression().accept(this));
	}

	@Override
	public <T extends Comparable<T> & Serializable> String visit(final LessThan<T> expression) {
		return String.format("(%s < %s)", expression.getLeftExpression().accept(this), expression.getRightExpression().accept(this));
	}

	@Override
	public <T extends Comparable<T> & Serializable> String visit(final LessThanOrEqual<T> expression) {
		return String.format("(%s <= %s)", expression.getLeftExpression().accept(this), expression.getRightExpression().accept(this));
	}

	@Override
	public <T extends Comparable<T> & Serializable> String visit(final GreaterThan<T> expression) {
		return String.format("(%s > %s)", expression.getLeftExpression().accept(this), expression.getRightExpression().accept(this));
	}

	@Override
	public <T extends Comparable<T> & Serializable> String visit(final GreaterThanOrEqual<T> expression) {
		return String.format("(%s >= %s)", expression.getLeftExpression().accept(this), expression.getRightExpression().accept(this));
	}

	@Override
	public <T extends Serializable> String visit(final Equal<T> expression) {
		final String left = expression.getLeftExpression().accept(this);
		final String right = expression.getRightExpression().accept(this);
		return NULL.equals(right) ? String.format("(%s IS NULL)", left) : String.format("(%s = %s)", left, right);
	}

	@Override
	public <T extends Serializable> String visit(final NotEqual<T> expression) {
		final String left = expression.getLeftExpression().accept(this);
		final String right = expression.getRightExpression().accept(this);
		return NULL.equals(right) ? String.format("(%s IS NOT NULL)", left) : String.format("(%s <> %s)", left, right);
	}

	@Override
	public <T extends Serializable, M extends Matcher<T>> String visit(final Match<T, M> expression) {
		return String.format("(%s LIKE %s)", expression.getLeftExpression().accept(this), expression.getRightExpression().accept(this));
	}

	@Override
	public <T extends Serializable, M extends Matcher<T>> String visit(final NotMatch<T, M> expression) {
		return String.format("(%s NOT LIKE %s)", expression.getLeftExpression().accept(this), expression.getRightExpression().accept(this));
	}

	@Override
	public <T extends CharSequence & Serializable> String visit(final Pattern<T> expression) {
		final java.util.regex.Pattern pattern = expression.evaluate().getPattern();
		return pattern == null ? NULL : String.format("'%s'", escape(pattern.toString()));
	}

	@Override
	public <T extends CharSequence & Serializable> String visit(final Keyword<T> expression) {
		final KeywordMatcher<? extends CharSequence> matcher = expression.evaluate();
		final CharSequence keyword = matcher.getKeyword();
		final MatchType matchType = matcher.getMatchType();
		return keyword == null ? NULL : visit(Matchers.like(String.format(MatchType.FULL.equals(matchType) ? "%s" : MatchType.FORWARD.equals(matchType) ? "%s%%" : MatchType.BACKWARD.equals(matchType) ? "%%%s" : "%%%s%%", Like.escape(keyword))));
	}

	@Override
	public <T extends CharSequence & Serializable> String visit(final Like<T> expression) {
		final LikeMatcher<? extends CharSequence> matcher = expression.evaluate();
		final CharSequence like = matcher.getLike();
		final Character escape = matcher.getEscape();
		return like == null ? NULL : escape != null ? String.format("'%s' ESCAPE '%s'", escape(like), escape(escape)) : String.format("'%s'", escape(like));
	}

	@Override
	public <T extends Serializable> String visit(final Column<T> expression) {
		return String.format("`%s`.`%s`", expression.getTableName(), expression.getColumnName());
	}

	protected String escape(final Character c) {
		return DatabaseUtils.escape(c, backslash);
	}

	protected String escape(final CharSequence cs) {
		return DatabaseUtils.escape(cs, backslash);
	}
}
