package com.android.javier.demo;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.android.javier.simplemvc.app.SimpleActivity;
import com.android.javier.simplemvc.interfaces.IAction;
import com.android.javier.simplemvc.interfaces.INotify;

public class DemoActivity extends SimpleActivity {

    private Button testbtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo);
    }

    @Override
    protected void initView() {
        testbtn = (Button) findViewById(R.id.testbtn);
    }

    @Override
    protected void setEventListener() {
        testbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doActionNotify(R.id.ids_notify_user_login, "username", "password");
            }
        });
    }

    @Override
    protected void prepareComplete() {

    }

    @Override
    public void handleNotify(INotify notify, IAction action) {

    }
}