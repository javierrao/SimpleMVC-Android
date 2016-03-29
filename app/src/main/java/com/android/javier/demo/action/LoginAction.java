package com.android.javier.demo.action;

import android.content.Context;

import com.android.javier.demo.R;
import com.android.javier.demo.entities.UserLoginEntity;
import com.android.javier.simplemvc.action.SimpleAction;
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
        if (notify.getResId() == R.string.ids_notify_user_login) {
            String[] body = (String[]) notify.getBody();

            Logger.getLogger().d("参数："+body[0] + " - " + body[1]);

            // 执行登陆的task
            SimpleTask task = getTaskById(R.string.ids_task_user_login);
            taskManager.executeTaskHttpPost(task, "https://192.168.0.111/login", "account=javierrao&password=javier24122", "");
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
}