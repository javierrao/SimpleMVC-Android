package com.android.javier.simplemvc;

import android.content.Context;
import android.util.SparseArray;
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

/**
 * Created by javier on 2016/3/26.
 * <p>
 * 描述应用程序中所包含的 action、task、dataSource，与raw/ApplicationContext.xml相对应
 * 为需要用到该对象的地方提供对象的创建，并且将配置中的id属性，转换成用android id资源来代替，这样可以方便使用的时候存取
 * <p>
 * 例如： 配置的action id 为 ${ids_action_user_login} 那么就必须在values下定义ids.xml文件，并且配置如下：
 * <?xml version="1.0" encoding="utf-8"?>
 * <resources>
 * <item name="ids_action_user_login" type="id"/>
 * </resources>
 * <p>
 * 那么在使用的时候则可以使用R.id.ids_action_user_login来获取对应的对象
 */
public final class ApplicationContext {

    /**
     * application context对象，描述了 action、task、dataSource
     */
    private static ApplicationContext applicationContext;

    /**
     * 应用程序上下文
     */
    private Context context;

    /**
     * 是否初始化。
     * 确保application context被初始化过，且仅初始化一次。
     * 频繁的初始化application context会带来性能的损耗
     */
    public static boolean bInit = false;

    /**
     * 应用程序 action 描述的集合
     */
    private ArrayList<ActionEntity> actionlist = null;

    /**
     * 应用程序 task 描述的集合
     */
    private ArrayList<TaskEntity> taskList = null;

    /**
     * 描述应用程序数据源的对象
     */
    private DataSourceEntity dataSourceEntity = null;

    /**
     * 单例方法，创建或获取 ApplicationContext 对象
     *
     * @param context android 应用程序上下文
     * @return ApplicationContext对象
     */
    public static ApplicationContext getApplicationContext(Context context) {
        if (applicationContext == null) {
            applicationContext = new ApplicationContext(context);
        }

        return applicationContext;
    }

    /**
     * 重载的获取 ApplicationContext 对象的单例方法，调用该方法之前需要确保 ApplicationContext 已经被初始化，否则返回的对象为 null
     *
     * @return ApplicationContext对象
     */
    public static ApplicationContext getApplicationContext() {
        if (bInit) {
            return applicationContext;
        }

        Logger.getLogger().e("please init applicationContext first.");
        return null;
    }

    /**
     * 构造方法
     *
     * @param context android 应用程序上下文
     */
    protected ApplicationContext(Context context) {
        this.context = context;
    }

    /**
     * 通过配置文件文件名初始化
     *
     * @param filename 配置文件名
    public void init(String filename) {
    if (bInit) {
    Logger.getLogger().w("ApplicationContext already initialization");
    return;
    }
    int res = context.getResources().getIdentifier(context.getPackageName() + ":raw/" + filename, null, null);
    init(res);
    }*/

    /**
     * 初始化数据库，如果在应用程序中需要用到数据库，则调用该方法，否则不需要调用
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
     * @return SimpleTask 对象
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
     * @param daoId dao 配置的资源ID
     * @return IDao 对象
     */
    public IDao getDao(int daoId) {
        if (!bInit) {
            Logger.getLogger().e("get dao failed. ApplicationContext must be initialization first");
            return null;
        }

        String daoName = String.valueOf(dataSourceEntity.getDaoArray().get(daoId));

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
     * @param str 资源文件中ID的名称
     * @return 资源ID
     */
    private int getItemResourceId(String str) {
        String tmpStr = str.substring(2, str.length() - 1);
        return context.getResources().getIdentifier(context.getPackageName() + ":id/" + tmpStr, null, null);
    }

    /**
     * 根据action名，创建action对象
     *
     * @param actionName action名称
     * @return IAction对象
     */
    private IAction createAction(String actionName) {
        if (actionName.equals("")) {
            return null;
        }

        Class<?> clazz;
        try {
            clazz = Class.forName(actionName);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        Constructor<?> cons[] = clazz.getConstructors();
        try {
            return (SimpleAction) cons[0].newInstance(context);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * 根据消息ID查询处理该消息的action
     *
     * @param notifyId 消息ID
     * @return action描述对象
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
     * @return 返回消息描述对象
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

        if (taskEntity == null) {
            return null;
        }

        Class<?> clazz;
        try {
            clazz = Class.forName(taskEntity.getName());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        Constructor<?> cons[] = clazz.getConstructors();

        try {
            return (SimpleTask) cons[0].newInstance(taskEntity);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * 创建DAO对象
     *
     * @param daoName dao的全路径
     * @return IDao 对象
     */
    private IDao createDao(String daoName) {
        Class<?> clazz;
        try {
            clazz = Class.forName(daoName);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        Constructor<?> cons[] = clazz.getConstructors();

        try {
            return (IDao) cons[0].newInstance(context);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * 根据task id查找task
     *
     * @param taskId task id
     * @return task描述对象
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

    /**
     * 解析 raw/application_context.xml配置文件
     *
     * @param xml 配置文件输入流
     */
    private void xmlParser(InputStream xml) {
        try {
            XmlPullParser pullParser = Xml.newPullParser();
            pullParser.setInput(xml, "UTF-8"); //为Pull解释器设置要解析的XML数据
            int event = pullParser.getEventType();

            ActionEntity actionEntity = null;
            ArrayList<Notify> notifies = null;
            SparseArray<String> daoArray = null;

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

                            notifies = new ArrayList<>();
                        } else if ("notify".equalsIgnoreCase(startTagName) && notifies != null) {
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

                        } else if ("type".equalsIgnoreCase(startTagName) && taskEntity != null) {
                            taskEntity.setType(pullParser.nextText());

                        } else if ("meta-data".equalsIgnoreCase(startTagName) && taskEntity != null) {
                            taskEntity.setMetaData(pullParser.nextText());

                        } else if ("dataSource".equalsIgnoreCase(startTagName)) {
                            dataSourceEntity = new DataSourceEntity();

                        } else if ("name".equalsIgnoreCase(startTagName) && dataSourceEntity != null) {
                            dataSourceEntity.setDbName(pullParser.nextText());

                        } else if ("version".equalsIgnoreCase(startTagName) && dataSourceEntity != null) {
                            dataSourceEntity.setVersion(Integer.parseInt(pullParser.nextText()));

                        } else if ("backup_when_need_upgrade".equalsIgnoreCase(startTagName) && dataSourceEntity != null) {
                            dataSourceEntity.setBackUpWhenUpgrade(Boolean.parseBoolean(pullParser.nextText()));

                        } else if ("dao-config".equalsIgnoreCase(startTagName)) {
                            daoArray = new SparseArray<>();

                        } else if ("dao".equalsIgnoreCase(startTagName) && daoArray != null) {
                            int daoId = 0;
                            String daoIdName = pullParser.getAttributeValue(null, "id");

                            if (daoIdName.startsWith("$")) {
                                daoId = getItemResourceId(daoIdName);
                            }
                            daoArray.put(daoId, pullParser.getAttributeValue(null, "name"));
                        }
                        break;

                    case XmlPullParser.END_TAG:
                        String endTagName = pullParser.getName();
                        if ("action".equalsIgnoreCase(endTagName) && actionEntity != null) {
                            actionEntity.setNotifies(notifies);
                            actionlist.add(actionEntity);

                            actionEntity = null;
                        } else if ("task".equalsIgnoreCase(endTagName)) {
                            taskList.add(taskEntity);

                            taskEntity = null;
                        } else if ("dao-config".equalsIgnoreCase(endTagName)) {
                            dataSourceEntity.setDaoArray(daoArray);
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