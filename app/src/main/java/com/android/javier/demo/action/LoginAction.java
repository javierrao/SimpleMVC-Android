package com.android.javier.demo.action;

import android.content.Context;

import com.android.javier.demo.R;
import com.android.javier.demo.dao.UserDao;
import com.android.javier.demo.entities.UserLoginEntity;
import com.android.javier.simplemvc.action.SimpleAction;
import com.android.javier.simplemvc.tasks.SimpleDatabaseTask;
import com.android.javier.simplemvc.tasks.SimpleNetworkTask;
import com.android.javier.simplemvc.util.Logger;
import com.android.javier.simplemvc.interfaces.INotify;
import com.android.javier.simplemvc.net.ErrorEntity;
import com.android.javier.simplemvc.tasks.SimpleTask;

/**
 * Created by javier on 2016/3/27.
 */
public class LoginAction extends SimpleAction<UserLoginEntity> {

    public LoginAction(Context context) {
        super(context);
    }

    @Override
    public void doAction(INotify notify) {
        if (notify.getId() == R.id.ids_notify_user_login) {
            String[] body = (String[]) notify.getBody();

            Logger.getLogger().d("参数：" + body[0] + " - " + body[1]);

            // 异步执行http登录请求
            SimpleNetworkTask task = getAsyncHttpTask(R.id.ids_task_user_login);
            taskManager.executeTaskHttpPost(task, "https://192.168.0.111/redpacket/user/login", "account=18129938032&password=abc123", "");

            // 同步执行数据库操作
            UserDao userDao = (UserDao) getDao(R.id.ids_dao_user);
            userDao.createUser();

            // 异步执行数据库操作
            SimpleDatabaseTask setUserTask = getAsyncDatabaseTask(R.id.ids_task_set_user);
            taskManager.executeAsyncDatabaseTask(setUserTask, null);
        }
    }

    @Override
    public void onResult(int code, UserLoginEntity result, SimpleTask target) {
        // super.onResult 必须调用
        super.onResult(code, result, target);

    }

    @Override
    public void onFailed(int code, ErrorEntity message, SimpleTask target) {
        // super.onFailed 必须调用
        super.onFailed(code, message, target);
    }

    @Override
    public void onDatabaseResult(UserLoginEntity result, SimpleTask target) {

    }
}