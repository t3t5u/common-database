package com.github.t3t5u.common.database;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.annotation.Nullable;

import com.github.t3t5u.common.util.ConcurrentUtils;
import com.google.common.base.Function;
import com.google.common.collect.Lists;

public abstract class AbstractCancellableConverter<T, E extends Entity> extends AbstractConverter<T, E> implements CancellableConverter<T, E> {
	@Override
	public final List<T> toModels(final AtomicBoolean cancelled, final List<E> entities) {
		if (entities == null) {
			return null;
		}
		if (ConcurrentUtils.isInterrupted(cancelled)) {
			return Collections.emptyList();
		}
		return Lists.transform(entities, new Function<E, T>() {
			@Override
			@Nullable
			public T apply(@Nullable final E input) {
				return toModel(cancelled, input);
			}
		});
	}

	private T toModel(final AtomicBoolean cancelled, final E entity) {
		return ConcurrentUtils.isInterrupted(cancelled) ? null : toModel(entity);
	}

	@Override
	public final List<E> toEntities(final AtomicBoolean cancelled, final List<T> models) {
		if (models == null) {
			return null;
		}
		if (ConcurrentUtils.isInterrupted(cancelled)) {
			return Collections.emptyList();
		}
		return Lists.transform(models, new Function<T, E>() {
			@Override
			@Nullable
			public E apply(@Nullable final T input) {
				return toEntity(cancelled, input);
			}
		});
	}

	private E toEntity(final AtomicBoolean cancelled, final T model) {
		return ConcurrentUtils.isInterrupted(cancelled) ? null : toEntity(model);
	}
}
