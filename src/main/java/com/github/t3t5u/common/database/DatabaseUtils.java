package com.github.t3t5u.common.database;

import java.util.List;
import java.util.concurrent.Callable;

import javax.annotation.Nullable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.t3t5u.common.util.CollectionUtils;
import com.github.t3t5u.common.util.EscapeUtils;
import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.Lists;

public final class DatabaseUtils {
	private static final Logger LOGGER = LoggerFactory.getLogger(DatabaseUtils.class);

	private DatabaseUtils() {
	}

	public static String escape(final Character c) {
		return EscapeUtils.escape(c, '\'');
	}

	public static String escape(final Character c, final boolean backslash) {
		return backslash ? EscapeUtils.escape(escape(c), '\\') : escape(c);
	}

	public static String escape(final CharSequence cs) {
		return EscapeUtils.escape(cs, '\'');
	}

	public static String escape(final CharSequence cs, final boolean backslash) {
		return backslash ? EscapeUtils.escape(escape(cs), '\\') : escape(cs);
	}

	public static <E extends Entity> E cascade(final CascadableDao<E> dao, final E entity) {
		return (dao != null) && (entity != null) ? dao.find(entity.getId(), true) : null;
	}

	public static <E extends Entity> List<E> cascade(final CascadableDao<E> dao, final List<E> entities) {
		if ((dao == null) || (entities == null)) {
			return null;
		}
		return Lists.transform(entities, new Function<E, E>() {
			@Override
			@Nullable
			public E apply(@Nullable final E input) {
				return find(dao, input);
			}
		});
	}

	private static <E extends Entity> E find(final CascadableDao<E> dao, final E entity) {
		return entity != null ? dao.find(entity.getId(), true) : null;
	}

	public static <E extends Entity> long deleteAll(final CascadableDao<E> dao, final List<E> entities, final boolean cascade) {
		if ((dao == null) || (entities == null)) {
			return 0;
		}
		return CollectionUtils.count(cascade ? cascade(dao, entities) : entities, new Predicate<E>() {
			@Override
			public boolean apply(@Nullable final E input) {
				return delete(dao, input, cascade);
			}
		});
	}

	private static <E extends Entity> boolean delete(final CascadableDao<E> dao, final E entity, final boolean cascade) {
		return (entity != null) && dao.delete(entity, cascade);
	}

	public static <V> V transaction(final Transactional transactional, final Callable<V> callable, final V defaultValue) {
		if ((transactional == null) || (callable == null)) {
			return defaultValue;
		}
		try {
			transactional.begin();
			final V result = callable.call();
			transactional.commit();
			return result;
		} catch (final Throwable t) {
			LOGGER.warn("transaction", t);
			rollback(transactional);
			return defaultValue;
		}
	}

	public static <F, T> T transaction(final Transactional transactional, final Function<F, T> function, final F input, final T defaultValue) {
		if ((transactional == null) || (function == null)) {
			return defaultValue;
		}
		try {
			transactional.begin();
			final T result = function.apply(input);
			transactional.commit();
			return result;
		} catch (final Throwable t) {
			LOGGER.warn("transaction", t);
			rollback(transactional);
			return defaultValue;
		}
	}

	private static void rollback(final Transactional transactional) {
		try {
			transactional.rollback();
		} catch (final Throwable t) {
			LOGGER.warn("rollback", t);
		}
	}
}
