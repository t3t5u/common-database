package com.github.t3t5u.common.database;

import java.util.List;

public interface Dao<E extends Entity> {
	E find(long id);

	List<E> findAll();

	boolean insert(E entity);

	boolean insertOrUpdate(E entity);

	boolean update(E entity);

	boolean delete(E entity);

	long deleteAll();

	long count();
}