package com.android.javier.demo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;

import com.android.javier.demo.command.LoginCommand;
import com.javier.simplemvc.core.NotifyManager;
import com.javier.simplemvc.patterns.view.SimpleActivity;

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
                notifyManager.sendNotifyMessage(SimpleConstants.MSG_COMMIT_LOGIN, "13235687640", "123456");
            }
        });
    }

    @Override
    public void onInitComplete() {

    }
}