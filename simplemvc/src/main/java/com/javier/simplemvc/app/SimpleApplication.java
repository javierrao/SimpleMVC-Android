package com.javier.simplemvc.app;

import android.app.Application;
import android.util.SparseArray;

import com.javier.simplemvc.SimpleContext;
import com.javier.simplemvc.utils.Logger;

/**
 * Created by javier.
 */
@SuppressWarnings("unused")
public abstract class SimpleApplication extends Application {

    protected SimpleContext simpleContext;

    private Logger logger = null;

    public SimpleApplication() {
        simpleContext = SimpleContext.getInstance();
    }

    protected void initDatabase(String dbName, int version) {
        SparseArray daoClass = getDaoClass();

        if (daoClass == null || daoClass.size() == 0) {
            logger.i("dao class is null!");
            return;
        }

        simpleContext.initDatabase(getApplicationContext(), dbName, version, daoClass);
    }

    protected void initLogger(String tag, String applicationName, String logfileName, boolean debug, boolean writeFile) {
        Logger.initLogger(tag, applicationName, logfileName, debug, writeFile);
        logger = Logger.getLogger();
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

    public abstract SparseArray<Class> getDaoClass();
}