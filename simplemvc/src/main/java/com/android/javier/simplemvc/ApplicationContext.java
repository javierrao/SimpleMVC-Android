package com.android.javier.simplemvc;

import android.content.Context;
import android.util.Xml;

import com.android.javier.simplemvc.db.SimpleDatabase;
import com.android.javier.simplemvc.entity.DataSourceEntity;
import com.android.javier.simplemvc.interfaces.IAction;
import com.android.javier.simplemvc.action.SimpleAction;
import com.android.javier.simplemvc.interfaces.IApplicationWidget;
import com.android.javier.simplemvc.entity.ActionEntity;
import com.android.javier.simplemvc.entity.TaskEntity;
import com.android.javier.simplemvc.interfaces.IDao;
import com.android.javier.simplemvc.tasks.TaskManager;
import com.android.javier.simplemvc.util.Logger;
import com.android.javier.simplemvc.notify.Notify;
import com.android.javier.simplemvc.tasks.SimpleTask;

import org.xmlpull.v1.XmlPullParser;

import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by javier on 2016/3/26.
 */
public final class ApplicationContext {
    private static ApplicationContext applicationContext;

    private Context context;

    public static boolean bInit = false;

    private ArrayList<ActionEntity> actionlist = null;
    private ArrayList<TaskEntity> taskList = null;
    private DataSourceEntity dataSourceEntity = null;

    public static ApplicationContext getApplicationContext(Context context) {
        if (applicationContext == null) {
            applicationContext = new ApplicationContext(context);
        }

        return applicationContext;
    }

    public static ApplicationContext getApplicationContext() {
        if (bInit) {
            return applicationContext;
        }

        Logger.getLogger().e("please init applicationContext first.");
        return null;
    }

    protected ApplicationContext(Context context) {
        this.context = context;
    }

    /**
     * 通过配置文件文件名初始化
     *
     * @param filename 配置文件名称
     */
    public void init(String filename) {
        if (bInit) {
            Logger.getLogger().w("ApplicationContext already initialization");
            return;
        }
        int res = context.getResources().getIdentifier(context.getPackageName() + ":raw/" + filename, null, null);
        init(res);
    }

    /**
     * 初始化数据库
     */
    public void initDatabase() {
        if (!bInit) {
            Logger.getLogger().e("init database failed. ApplicationContext must be initialization first");
            return;
        }

        SimpleDatabase database = SimpleDatabase.getSimpleDatabase();
        database.initDatabase(context, dataSourceEntity);
    }


    /**
     * 初始化ApplicationContext
     *
     * @param configId 存放在res/raw下的配置文件的ID，可以使用R.raw.*来获取资源
     */
    public void init(int configId) {
        if (bInit) {
            Logger.getLogger().w("ApplicationContext already init");
            return;
        }

        if (context != null) {
            InputStream input = context.getResources().openRawResource(configId);
            xmlParser(input);
            bInit = true;
        }
    }

    /**
     * 根据消息ID发送消息
     *
     * @param notifyId 消息ID,对应配置在context.xml中的notify的id字段
     * @param body     消息体
     * @param target   消息的发起者
     */
    public void sendNotify(int notifyId, Object body, IApplicationWidget target) {
        if (!bInit) {
            Logger.getLogger().e("send notify failed. ApplicationContext must be initialization first");
            return;
        }

        ActionEntity action = findActionByNotifyId(notifyId);

        if (action == null) {
            Logger.getLogger().e("send notify failed. can not find action by notify name '" + notifyId + "', please check applicationContext xml file.");
            return;
        }

        Notify notify = findNotifyById(notifyId);

        if (notify != null) {
            notify.setBody(body);
        }

        IAction targetAction = createAction(action.getName());

        if (targetAction != null) {
            targetAction.setApplicationWidget(target);
            targetAction.doAction(notify);
        }
    }

    /**
     * 根据taskID获取task
     *
     * @param taskId task id
     * @return
     */
    public SimpleTask getTask(int taskId) {
        if (!bInit) {
            Logger.getLogger().e("ApplicationContext must be initialization first");
            return null;
        }
        return createTask(taskId);
    }

    /**
     * 根据dao配置的ID查找对应的DAO对象
     *
     * @param daoId
     * @return
     */
    public IDao getDao(int daoId) {
        if (!bInit) {
            Logger.getLogger().e("get dao failed. ApplicationContext must be initialization first");
            return null;
        }

        String daoName = dataSourceEntity.getDaos().get(daoId);

        return createDao(daoName);
    }

    /**
     * 销毁ApplicationContext
     */
    public void destroy() {
        TaskManager.getInstance().destroy();
        SimpleDatabase.getSimpleDatabase().destroy();

        actionlist.clear();
        taskList.clear();

        taskList = null;
        dataSourceEntity = null;
    }

    /**
     * 获取资源ID
     *
     * @param str
     * @return
     */
    private int getItemResourceId(String str) {
        String tmpStr = str.substring(2, str.length() - 1);
        int res = context.getResources().getIdentifier(context.getPackageName() + ":id/" + tmpStr, null, null);

        return res;
    }

    /**
     * 根据action名，创建action对象
     *
     * @param actionName action名称
     * @return IAction对象
     */
    private IAction createAction(String actionName) {
        if (actionName == "") {
            return null;
        }

        Class<?> clazz = null;
        try {
            clazz = Class.forName(actionName);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        Constructor<?> cons[] = clazz.getConstructors();
        try {
            SimpleAction simpleAction = (SimpleAction) cons[0].newInstance(context);
            return simpleAction;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * 根据消息ID查询处理该消息的action
     *
     * @param notifyId 消息ID
     * @return
     */
    private ActionEntity findActionByNotifyId(int notifyId) {
        for (int i = 0; i < actionlist.size(); i++) {
            ActionEntity actionEntity = actionlist.get(i);
            ArrayList<Notify> notifies = actionEntity.getNotifies();

            for (int j = 0; j < notifies.size(); j++) {
                if (notifyId == notifies.get(i).getId()) {
                    return actionEntity;
                }
            }
        }

        return null;
    }

    /**
     * 根据消息名称查询需要发送的消息
     *
     * @param notifyId 消息ID
     * @return
     */
    private Notify findNotifyById(int notifyId) {
        for (int i = 0; i < actionlist.size(); i++) {
            ActionEntity actionEntity = actionlist.get(i);
            ArrayList<Notify> notifies = actionEntity.getNotifies();

            for (int j = 0; j < notifies.size(); j++) {
                if (notifyId == notifies.get(i).getId()) {
                    return notifies.get(i);
                }
            }
        }

        return null;
    }

    /**
     * 创建task对象
     *
     * @param taskId task的ID
     * @return SimpleTask的子类的对象
     */
    private SimpleTask createTask(int taskId) {
        TaskEntity taskEntity = findTask(taskId);

        Class<?> clazz = null;
        try {
            clazz = Class.forName(taskEntity.getName());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        Constructor<?> cons[] = clazz.getConstructors();

        try {
            SimpleTask task = (SimpleTask) cons[0].newInstance(taskEntity);
            return task;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * 创建DAO对象
     *
     * @param daoName dao的全路径
     * @return
     */
    private IDao createDao(String daoName) {
        Class<?> clazz = null;
        try {
            clazz = Class.forName(daoName);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        Constructor<?> cons[] = clazz.getConstructors();

        try {
            IDao dao = (IDao) cons[0].newInstance(context);
            return dao;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * 根据task id查找task
     *
     * @param taskId task id
     * @return
     */
    private TaskEntity findTask(int taskId) {
        for (int i = 0; i < taskList.size(); i++) {
            TaskEntity taskEntity = taskList.get(i);
            if (taskId == taskEntity.getId()) {
                return taskEntity;
            }
        }

        return null;
    }

    private void xmlParser(InputStream xml) {
        try {
            XmlPullParser pullParser = Xml.newPullParser();
            pullParser.setInput(xml, "UTF-8"); //为Pull解释器设置要解析的XML数据
            int event = pullParser.getEventType();

            ActionEntity actionEntity = null;
            ArrayList<Notify> notifies = null;
            HashMap<Integer, String> daos = null;

            TaskEntity taskEntity = null;

            while (event != XmlPullParser.END_DOCUMENT) {
                switch (event) {
                    case XmlPullParser.START_DOCUMENT:
                        break;
                    case XmlPullParser.START_TAG:
                        String startTagName = pullParser.getName();

                        if ("action-config".equalsIgnoreCase(startTagName)) {
                            actionlist = new ArrayList<>();
                        } else if ("action".equalsIgnoreCase(startTagName)) {
                            actionEntity = new ActionEntity();

                            String actionIdName = pullParser.getAttributeValue(null, "id");

                            int actionId = 0;
                            if (actionIdName.startsWith("$")) {
                                actionId = getItemResourceId(actionIdName);
                            }

                            actionEntity.setId(actionId);
                            actionEntity.setName(pullParser.getAttributeValue(null, "name"));

                            notifies = new ArrayList<Notify>();
                        } else if ("notify".equalsIgnoreCase(startTagName)) {
                            Notify notify = new Notify();

                            String notifyIdName = pullParser.getAttributeValue(null, "id");
                            int notifyId = 0;

                            if (notifyIdName.startsWith("$")) {
                                notifyId = getItemResourceId(notifyIdName);
                            }

                            notify.setId(notifyId);
                            notify.setName(pullParser.getAttributeValue(null, "name"));
                            notifies.add(notify);

                        } else if ("task-config".equalsIgnoreCase(startTagName)) {
                            taskList = new ArrayList<>();

                        } else if ("task".equalsIgnoreCase(startTagName)) {
                            taskEntity = new TaskEntity();

                            String taskIdName = pullParser.getAttributeValue(null, "id");
                            int taskId = 0;

                            if (taskIdName.startsWith("$")) {
                                taskId = getItemResourceId(taskIdName);
                            }

                            taskEntity.setId(taskId);
                            taskEntity.setName(pullParser.getAttributeValue(null, "name"));

                        } else if ("type".equalsIgnoreCase(startTagName)) {
                            taskEntity.setType(pullParser.nextText());

                        } else if ("meta-data".equalsIgnoreCase(startTagName)) {
                            taskEntity.setMetaData(pullParser.nextText());
                        } else if ("dataSource".equalsIgnoreCase(startTagName)) {
                            dataSourceEntity = new DataSourceEntity();
                        } else if ("name".equalsIgnoreCase(startTagName)) {
                            dataSourceEntity.setDbName(pullParser.nextText());
                        } else if ("version".equalsIgnoreCase(startTagName)) {
                            dataSourceEntity.setVersion(Integer.parseInt(pullParser.nextText()));
                        } else if ("backup_when_need_upgrade".equalsIgnoreCase(startTagName)) {
                            dataSourceEntity.setBackUpWhenUpgrade(Boolean.parseBoolean(pullParser.nextText()));
                        } else if ("daos".equalsIgnoreCase(startTagName)) {
                            daos = new HashMap<Integer, String>();
                        } else if ("dao".equalsIgnoreCase(startTagName)) {
                            int daoId = 0;
                            String daoIdName = pullParser.getAttributeValue(null, "id");

                            if (daoIdName.startsWith("$")) {
                                daoId = getItemResourceId(daoIdName);
                            }
                            daos.put(daoId, pullParser.getAttributeValue(null, "name"));
                        }
                        break;

                    case XmlPullParser.END_TAG:
                        String endTagName = pullParser.getName();
                        if ("action".equalsIgnoreCase(endTagName)) {
                            actionEntity.setNotifies(notifies);
                            actionlist.add(actionEntity);
                        } else if ("task".equalsIgnoreCase(endTagName)) {
                            taskList.add(taskEntity);
                        } else if ("daos".equalsIgnoreCase(endTagName)) {
                            dataSourceEntity.setDaos(daos);
                        }
                        break;
                }

                event = pullParser.next();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}