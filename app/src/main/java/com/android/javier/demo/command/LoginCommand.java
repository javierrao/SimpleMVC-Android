package com.android.javier.demo.command;

import com.android.javier.demo.R;
import com.android.javier.demo.mediator.LoginMediator;
import com.android.javier.demo.tasks.LoginTask;
import com.javier.simplemvc.interfaces.IDisplay;
import com.javier.simplemvc.modules.command.SimpleCommand;
import com.javier.simplemvc.modules.notify.NotifyMessage;

/**
 * Created by javier
 */
public class LoginCommand extends SimpleCommand {

    public LoginCommand(IDisplay display) {
        super(display);
    }

    @Override
    protected void initTask() {
        registerTask(R.id.ids_task_user_login, LoginTask.class);
    }

    @Override
    protected void initMediator() {
        registerMediator(new LoginMediator(R.id.ids_login_mediator, display));
    }

    @Override
    public void execute(NotifyMessage message) {
        switch (message.getWhat()) {
            case R.integer.msg_commit_login:
                Object[] objects = message.getParams();
                String account = String.valueOf(objects[0]);
                String password = String.valueOf(objects[1]);
                task.post(R.id.ids_task_user_login,
                        "https://192.168.0.132/user/login",
                        "account="+account+"&password="+password);
                break;
        }
    }

    @Override
    public int[] listMessage() {
        return new int[]{R.integer.msg_commit_login};
    }
}