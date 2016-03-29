package com.android.javier.simplemvc.app;

import android.app.Application;

import com.android.javier.simplemvc.ApplicationContext;
import com.android.javier.simplemvc.util.Logger;

/**
 * Created by javier on 2016/3/29.
 */
public class SimpleApplication extends Application {

    protected void initSimpleMVC(int contextResId) {
        ApplicationContext appContext = ApplicationContext.getApplicationContext(this);
        appContext.init(contextResId);
    }

    protected void initLogger(String tag, String appname, String logfileName, boolean debug) {
       Logger.initLogger(tag, appname, logfileName, debug);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
    }
}