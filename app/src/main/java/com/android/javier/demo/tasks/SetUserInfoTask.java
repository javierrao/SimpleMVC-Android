package com.android.javier.demo.tasks;

import com.android.javier.demo.R;
import com.android.javier.demo.dao.UserDao;
import com.android.javier.simplemvc.entity.TaskEntity;
import com.android.javier.simplemvc.tasks.SimpleDatabaseTask;

/**
 * Created by javier on 2016/4/4.
 */
public class SetUserInfoTask extends SimpleDatabaseTask {
    public SetUserInfoTask(TaskEntity entity) {
        super(entity);
    }

    @Override
    protected Object doDatabaseOptions(Object[] params) {
        // 执行异步数据库操作
        UserDao userDao = (UserDao) getDao(R.id.ids_task_set_user);
        userDao.createUser();
        return null;
    }
}