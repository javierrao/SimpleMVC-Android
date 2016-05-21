package com.android.javier.demo;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;

import com.android.javier.demo.command.LoginCommand;
import com.android.javier.demo.fragment.Fragment1;
import com.android.javier.demo.fragment.Fragment2;
import com.android.javier.demo.fragment.Fragment3;
import com.javier.simplemvc.app.SimpleActivity;
import com.javier.simplemvc.app.SimpleFragment;
import com.javier.simplemvc.modules.notify.NotifyMessage;

public class DemoActivity extends SimpleActivity {

    public static final String FRAGMENT_1 = "FRAGMENT_1";
    public static final String FRAGMENT_2 = "FRAGMENT_2";
    public static final String FRAGMENT_3 = "FRAGMENT_3";

    private Button mLoginButton;
    private RadioGroup mRadioGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo);
    }

    @Override
    public void initView() {
        mLoginButton = (Button) findViewById(R.id.testbtn);
        mRadioGroup = (RadioGroup) findViewById(R.id.fragment_switch);
    }

    @Override
    public void addEventListener() {
        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendNotifyMessage(R.integer.msg_commit_login, "13235687640", "123456");
            }
        });

        mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.f1:
                        switchFragment(FRAGMENT_1, R.id.fragment_container);
                        break;
                    case R.id.f2:
                        switchFragment(FRAGMENT_2, R.id.fragment_container);
                        break;
                    case R.id.f3:
                        switchFragment(FRAGMENT_3, R.id.fragment_container);
                        break;
                }
            }
        });
    }

    @Override
    public void onInitComplete() {
        switchFragment(FRAGMENT_1, R.id.fragment_container);
    }

    @Override
    public void initCommand() {
        registerCommand(LoginCommand.class);
    }

    @Override
    public void removeCommand() {
        removeCommand(LoginCommand.class);
    }

    @Override
    public int[] listMessage() {
        return new int[0];
    }

    @Override
    public void handlerMessage(NotifyMessage message) {

    }

    @Override
    protected String[] getFragmentTags() {
        return new String[]{FRAGMENT_1, FRAGMENT_2, FRAGMENT_3};
    }

    @Override
    protected SimpleFragment getFragment(String tag) {
        SimpleFragment fragment = null;

        switch (tag) {
            case FRAGMENT_1:
                fragment = new Fragment1();
                break;
            case FRAGMENT_2:
                fragment = new Fragment2();
                break;
            case FRAGMENT_3:
                fragment = new Fragment3();
                break;
        }

        return fragment;
    }
}