package com.android.javier.demo;

import com.android.javier.simplemvc.app.SimpleApplication;

/**
 * Created by javier on 2016/3/29.
 */
public class BaseApplication extends SimpleApplication {

    @Override
    public void onCreate() {
        super.onCreate();

        initSimpleMVC(R.raw.application_context);
        initLogger("SimpleMVC", "SimpleMVC", "SimpleMVC", true);
    }
}