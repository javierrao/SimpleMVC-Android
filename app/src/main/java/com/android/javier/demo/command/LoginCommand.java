package com.android.javier.demo.command;

import android.content.Context;
import android.widget.Toast;

import com.android.javier.demo.R;
import com.android.javier.demo.SimpleConstants;
import com.android.javier.demo.entities.UserLoginEntity;
import com.android.javier.demo.tasks.LoginTask;
import com.android.javier.demo.tasks.UserTask;
import com.javier.simplemvc.data.http.ErrorEntity;
import com.javier.simplemvc.interfaces.ITaskCallback;
import com.javier.simplemvc.patterns.command.SimpleCommand;
import com.javier.simplemvc.patterns.model.SimpleTask;
import com.javier.simplemvc.patterns.notify.NotifyMessage;

/**
 * Created by javier
 */
public class LoginCommand extends SimpleCommand implements ITaskCallback<UserLoginEntity> {

    public LoginCommand(Context context) {
        super(context);
    }

    @Override
    public void onRegister() {
        super.onRegister();

        registerTask(R.id.ids_task_query_user, UserTask.class, this);
        registerTask(R.id.ids_task_user_login, LoginTask.class, this);
    }

    @Override
    public void onRemove() {
        super.onRemove();

        removeTask(R.id.ids_task_query_user);
        removeTask(R.id.ids_task_user_login);
    }

    @Override
    public String[] listMessage() {
        return new String[]{SimpleConstants.MSG_COMMIT_LOGIN};
    }

    @Override
    public void handlerMessage(NotifyMessage message) {
        switch (message.getName()) {
            case SimpleConstants.MSG_COMMIT_LOGIN:
                Object[] objects = message.getParams();
                String account = String.valueOf(objects[0]);
                String password = String.valueOf(objects[1]);
                logger.i("execute login! " + account + " - " + password);
//                taskManager.post(R.id.ids_task_user_login,
//                        "https://192.168.0.132/user/login",
//                        "account=" + account + "&password=" + password);

                taskManager.query(R.id.ids_task_query_user, "1");
                break;
        }
    }

    @Override
    public void onResult(int code, UserLoginEntity result, SimpleTask target) {
    }

    @Override
    public void onFailed(int code, ErrorEntity error, SimpleTask target) {
        logger.i("code : " + code);
        logger.i("error : " + error.getMessage());
        logger.i("task : " + target);
    }
}