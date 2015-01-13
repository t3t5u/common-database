package com.github.t3t5u.common.database;

import java.io.Serializable;
import java.util.Date;

public interface Entity extends Serializable {
	long UNDEFINED_ID = -1;
	String COLUMN_NAME_ID = "id";
	String COLUMN_NAME_CREATED_AT = "created_at";
	String COLUMN_NAME_UPDATED_AT = "updated_at";

	long getId();

	void setId(long id);

	Date getCreatedAt();

	void setCreatedAt(Date createdAt);

	Date getUpdatedAt();

	void setUpdatedAt(Date updatedAt);
}
