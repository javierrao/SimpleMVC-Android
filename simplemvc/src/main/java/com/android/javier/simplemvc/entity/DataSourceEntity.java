package com.android.javier.simplemvc.entity;

import java.util.HashMap;

/**
 * Created by javier on 2016/4/5.
 */
public final class DataSourceEntity {
    private String dbName;
    private int version;
    private boolean backUpWhenUpgrade;
    private HashMap<Integer, String> daos;

    public String getDbName() {
        return dbName;
    }

    public void setDbName(String dbName) {
        this.dbName = dbName;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public boolean isBackUpWhenUpgrade() {
        return backUpWhenUpgrade;
    }

    public void setBackUpWhenUpgrade(boolean backUpWhenUpgrade) {
        this.backUpWhenUpgrade = backUpWhenUpgrade;
    }

    public HashMap<Integer, String> getDaos() {
        return daos;
    }

    public void setDaos(HashMap<Integer, String> daos) {
        this.daos = daos;
    }
}
