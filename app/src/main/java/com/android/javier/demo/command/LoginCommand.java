package com.android.javier.demo.command;

import com.android.javier.demo.R;
import com.android.javier.demo.entities.UserLoginEntity;
import com.android.javier.demo.tasks.LoginTask;
import com.android.javier.demo.tasks.UserTask;
import com.javier.simplemvc.interfaces.ITaskCallback;
import com.javier.simplemvc.modules.command.SimpleCommand;
import com.javier.simplemvc.modules.notify.NotifyMessage;
import com.javier.simplemvc.modules.task.SimpleTask;
import com.javier.simplemvc.net.ErrorEntity;

/**
 * Created by javier
 */
public class LoginCommand extends SimpleCommand implements ITaskCallback<UserLoginEntity> {

    @Override
    public void initTask() {
        registerTask(R.id.ids_task_user_login, LoginTask.class, this);
        registerTask(R.id.ids_task_query_user, UserTask.class, this);
    }

    @Override
    public int[] listMessage() {
        return new int[]{R.integer.msg_commit_login};
    }

    @Override
    public void handlerMessage(NotifyMessage message) {
        switch (message.getWhat()) {
            case R.integer.msg_commit_login:
                Object[] objects = message.getParams();
                String account = String.valueOf(objects[0]);
                String password = String.valueOf(objects[1]);
                taskManager.post(R.id.ids_task_user_login,
                        "https://192.168.0.132/user/login",
                        "account=" + account + "&password=" + password);

                taskManager.query(R.id.ids_task_query_user, "1");
                break;
        }
    }

    @Override
    public void onResult(int code, UserLoginEntity result, SimpleTask target) {

    }

    @Override
    public void onFailed(int code, ErrorEntity error, SimpleTask target) {

    }
}