package com.github.t3t5u.common.database;

import java.util.List;

public interface Converter<T, E extends Entity> {
	T toModel(E entity);

	@Deprecated
	T toModel(E entity, T model);

	List<T> toModels(List<E> entities);

	E toEntity(T model);

	E toEntity(T model, E entity);

	List<E> toEntities(List<T> models);
}
