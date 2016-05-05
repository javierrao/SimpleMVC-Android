package com.android.javier.demo.tasks;

import com.android.javier.demo.entities.UserLoginEntity;
import com.javier.simplemvc.interfaces.ITaskCallback;
import com.javier.simplemvc.modules.task.SimpleDatabaseTask;

/**
 * author:Javier
 * time:2016/5/6.
 * mail:38244704@qq.com
 */
public class UserTask extends SimpleDatabaseTask<UserLoginEntity> {
    public UserTask(ITaskCallback callback) {
        super(callback);
    }

    @Override
    protected UserLoginEntity query(Object[] params) {
        return super.query(params);
    }

    @Override
    protected void onExecuteFinish(UserLoginEntity userLoginEntity) {
        if (userLoginEntity != null) {
            onResult(200, userLoginEntity);
        } else {

            onFailed(201, "读取用户信息失败");
        }
    }
}
