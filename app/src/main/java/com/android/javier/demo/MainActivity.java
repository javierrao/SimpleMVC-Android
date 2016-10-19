package com.android.javier.demo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.javier.simplemvc.patterns.notify.NotifyMessage;
import com.javier.simplemvc.patterns.view.SimpleActivity;

import java.util.HashMap;

/**
 * author:Javier
 * time:2016/5/24.
 * mail:38244704@qq.com
 */
public class MainActivity extends SimpleActivity {

    private Button mainLogin;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public void onInitView() {
        mainLogin = (Button) findViewById(R.id.main_login);
    }

    @Override
    public void onSetEventListener() {
        mainLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                notifyManager.sendNotifyMessage(SimpleConstants.MSG_COMMIT_LOGIN, "15815595810", "888888");
            }
        });
    }

    @Override
    public void onInitComplete() {

    }

    @Override
    public String[] listMessage() {
        return new String[]{
                "login_result"
        };
    }

    @Override
    public void handlerMessage(NotifyMessage message) {
        super.handlerMessage(message);

        switch (message.getName()) {
            case "login_result":
                HashMap m = message.getMap();
                int code = (int) m.get("code");

                if (code == 200) {
                    Toast.makeText(this, "登陆成功", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
}