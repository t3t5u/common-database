package com.github.t3t5u.common.database;

import java.util.List;

public interface CascadableDao<E extends Entity> extends Dao<E> {
	E find(long id, boolean cascade);

	List<E> findAll(boolean cascade);

	boolean insert(E entity, boolean cascade);

	boolean insertOrUpdate(E entity, boolean cascade);

	boolean update(E entity, boolean cascade);

	boolean delete(E entity, boolean cascade);

	long deleteAll(boolean cascade);
}
