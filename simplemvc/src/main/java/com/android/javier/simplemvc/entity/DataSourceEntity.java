package com.android.javier.simplemvc.entity;

import android.util.SparseArray;

/**
 * Created by javier on 2016/4/5.
 * <p>
 * 描述数据库的实体
 */
@SuppressWarnings("unused")
public final class DataSourceEntity {
    private String dbName;
    private int version;
    private boolean backUpWhenUpgrade;
    private SparseArray<String> daoArray;

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

    public SparseArray<String> getDaoArray() {
        return daoArray;
    }

    public void setDaoArray(SparseArray<String> daoArray) {
        this.daoArray = daoArray;
    }
}
