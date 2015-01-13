package com.github.t3t5u.common.database;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public interface CancellableConverter<T, E extends Entity> extends Converter<T, E> {
	List<T> toModels(AtomicBoolean cancelled, List<E> entities);

	List<E> toEntities(AtomicBoolean cancelled, List<T> models);
}
