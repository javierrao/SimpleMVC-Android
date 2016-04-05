package com.android.javier.simplemvc.app;

import android.app.Application;

import com.android.javier.simplemvc.ApplicationContext;
import com.android.javier.simplemvc.db.SimpleDatabase;
import com.android.javier.simplemvc.util.Logger;

/**
 * Created by javier on 2016/3/29.
 */
public class SimpleApplication extends Application {

    protected ApplicationContext applicationContext;

    public SimpleApplication() {
        applicationContext = ApplicationContext.getApplicationContext(this);
    }

    protected void initSimpleMVC(int contextResId) {
        applicationContext.init(contextResId);
    }

    protected void initSimpleMVC(String contextFileName) {
        applicationContext.init(contextFileName);
    }

    protected void initDatabase() {
        applicationContext.initDatabase();
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

        applicationContext.destroy();
    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
    }
}