package com.android.javier.simplemvc.app;

import android.app.Application;

import com.android.javier.simplemvc.ApplicationContext;
import com.android.javier.simplemvc.util.Logger;

/**
 * Created by javier.
 */
@SuppressWarnings("unused")
public class SimpleApplication extends Application {

    protected ApplicationContext applicationContext;

    private Logger logger = null;

    public SimpleApplication() {
        applicationContext = ApplicationContext.getApplicationContext(this);
    }

    protected void initSimpleMVC(int contextResId) {
        applicationContext.init(contextResId);
    }

    protected void initDatabase() {
        applicationContext.initDatabase();
    }

    protected void initLogger(String tag, String applicationName, String logfileName, boolean debug) {
        Logger.initLogger(tag, applicationName, logfileName, debug);

        logger = Logger.getLogger();
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