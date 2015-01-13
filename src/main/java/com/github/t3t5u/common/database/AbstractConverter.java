package com.github.t3t5u.common.database;

import java.util.List;

import javax.annotation.Nullable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Function;
import com.google.common.collect.Lists;

public abstract class AbstractConverter<T, E extends Entity> implements Converter<T, E> {
	private static final Logger LOGGER = LoggerFactory.getLogger(AbstractConverter.class);

	@Override
	public final T toModel(final E entity) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("toModel: " + entity);
		}
		if (!isAcceptable(entity)) {
			return null;
		}
		final T result = toModelAccepted(entity);
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("toModel: " + result);
		}
		return result;
	}

	protected boolean isAcceptable(final E entity) {
		return entity != null;
	}

	protected abstract T toModelAccepted(E entity);

	@Override
	@Deprecated
	public final T toModel(final E entity, final T model) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("toModel: " + entity + ", " + model);
		}
		if (!isAcceptable(entity, model)) {
			return null;
		}
		final T result = toModelAccepted(entity, model);
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("toModel: " + result);
		}
		return result;
	}

	@Deprecated
	protected final boolean isAcceptable(final E entity, final T model) {
		return (entity != null) && (model != null);
	}

	@Deprecated
	protected final T toModelAccepted(final E entity, final T model) {
		return model;
	}

	@Override
	public final List<T> toModels(final List<E> entities) {
		if (entities == null) {
			return null;
		}
		return Lists.transform(entities, new Function<E, T>() {
			@Override
			@Nullable
			public T apply(@Nullable final E input) {
				return toModel(input);
			}
		});
	}

	@Override
	public final E toEntity(final T model) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("toEntity: " + model);
		}
		if (!isAcceptable(model)) {
			return null;
		}
		final E result = toEntityAccepted(model);
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("toEntity: " + result);
		}
		return result;
	}

	protected boolean isAcceptable(final T model) {
		return model != null;
	}

	protected abstract E toEntityAccepted(T model);

	@Override
	public final E toEntity(final T model, final E entity) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("toEntity: " + model + ", " + entity);
		}
		if (!isAcceptable(model, entity)) {
			return null;
		}
		final E result = toEntityAccepted(model, entity);
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("toEntity: " + result);
		}
		return result;
	}

	protected boolean isAcceptable(final T model, final E entity) {
		return (model != null) && (entity != null);
	}

	protected E toEntityAccepted(final T model, final E entity) {
		return entity;
	}

	@Override
	public final List<E> toEntities(final List<T> models) {
		if (models == null) {
			return null;
		}
		return Lists.transform(models, new Function<T, E>() {
			@Override
			@Nullable
			public E apply(@Nullable final T input) {
				return toEntity(input);
			}
		});
	}

	protected static <T, E extends Entity> E toEntityForced(final Converter<T, E> converter, final T model, final E entity) {
		final E result = converter.toEntity(model, entity);
		return result != null ? result : converter.toEntity(model);
	}
}
