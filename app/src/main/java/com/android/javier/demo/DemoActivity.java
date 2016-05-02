package com.android.javier.demo;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.android.javier.demo.command.LoginCommand;
import com.javier.simplemvc.app.SimpleActivity;

public class DemoActivity extends SimpleActivity {

    private Button testbtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo);
    }

    @Override
    public void initView() {
        testbtn = (Button) findViewById(R.id.testbtn);
    }

    @Override
    public void addEventListener() {
        testbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendNotifyMessage(R.integer.msg_commit_login, "13235687640", "123456");
            }
        });
    }

    @Override
    public void onInitComplete() {

    }

    @Override
    public void initCommand() {
        super.initCommand();

        registerCommand(LoginCommand.class);
    }

    @Override
    public void removeCommand() {
        super.removeCommand();

        removeCommand(LoginCommand.class);
    }
}