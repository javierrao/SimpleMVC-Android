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

import java.util.HashMap;

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
    }

    @Override
    public void onRemove() {
        super.onRemove();

        unBindTask(1);
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

                bindTask(1, this);
                taskManager.post(1, "http://192.168.100.4:8080/lotteryhome/api/user/login",
                        "account=" + account + "&password=" + password, this);
                break;
        }
    }

    @Override
    public void onResult(int code, UserLoginEntity result, SimpleTask target) {
        HashMap map = new HashMap();
        map.put("code", 200);

        notifyManager.sendNotifyMessage("login_result", map);
    }

    @Override
    public void onFailed(int code, ErrorEntity error, SimpleTask target) {
        logger.i("code : " + code);
        logger.i("error : " + error.getMessage());
        logger.i("task : " + target);
    }
}