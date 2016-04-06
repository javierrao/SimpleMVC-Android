package com.android.javier.demo.tasks;

import android.database.Cursor;

import com.android.javier.simplemvc.entity.TaskEntity;
import com.android.javier.simplemvc.tasks.SimpleDatabaseTask;

/**
 * Created by javier
 */
public class SetUserInfoTask extends SimpleDatabaseTask<Cursor> {
    public SetUserInfoTask(TaskEntity entity) {
        super(entity);
    }

    @Override
    protected Cursor doDatabaseOptions(Object[] params) {
        return null;
    }
}