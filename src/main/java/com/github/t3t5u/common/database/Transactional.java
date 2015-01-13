package com.github.t3t5u.common.database;

public interface Transactional {
	void begin();

	void commit();

	void rollback();
}
