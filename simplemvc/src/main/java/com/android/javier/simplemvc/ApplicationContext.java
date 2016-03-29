package com.android.javier.simplemvc;

import android.content.Context;
import android.util.Xml;

import com.android.javier.simplemvc.interfaces.IAction;
import com.android.javier.simplemvc.action.SimpleAction;
import com.android.javier.simplemvc.app.IApplicationWidget;
import com.android.javier.simplemvc.entity.ActionEntity;
import com.android.javier.simplemvc.entity.TaskEntity;
import com.android.javier.simplemvc.util.Logger;
import com.android.javier.simplemvc.notify.Notify;
import com.android.javier.simplemvc.tasks.SimpleTask;

import org.xmlpull.v1.XmlPullParser;

import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.ArrayList;

/**
 * Created by javie on 2016/3/26.
 */
public final class ApplicationContext {
    private static ApplicationContext applicationContext;

    private Context context;

    private boolean bInit = false;

    private ArrayList<ActionEntity> actionlist = null;
    private ArrayList<TaskEntity> taskList = null;

    public static ApplicationContext getApplicationContext(Context context) {
        if (applicationContext == null) {
            applicationContext = new ApplicationContext(context);
        }

        return applicationContext;
    }

    protected ApplicationContext(Context context) {
        this.context = context;
    }


    /**
     * 初始化ApplicationContext
     *
     * @param configId 存放在res/raw下的配置文件的ID，可以使用R.raw.*来获取资源
     */
    public void init(int configId) {
        if (bInit) {
            return;
        }

        if (context != null) {
            InputStream input = context.getResources().openRawResource(configId);
            xmlParser(input);
            bInit = true;
        }
    }

    /**
     * 根据消息名称发送消息
     * @param notifyId    消息ID,对应配置在context.xml中的notify的id字段
     * @param body  消息体
     * @param target    消息的发起者
     */
    public void sendNotify(String notifyId, Object body, IApplicationWidget target) {
        ActionEntity action = findActionByNotifyId(notifyId);

        if (action == null) {
            Logger.getLogger().e("can not find action by notify name '"+notifyId+"', please check applicationContext xml file.");
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
     * 根据消息资源ID发送消息
     * @param notifyResId  需要发送的消息引用的资源ID
     * @param body      消息体
     * @param target    消息的发起者
     */
    public void sendNotify(int notifyResId, Object body, IApplicationWidget target) {
        String notifyId = context.getString(notifyResId);

        sendNotify(notifyId, body, target);
    }

    /**
     * 根据taskID获取task
     * @param taskId    task id
     * @return
     */
    public SimpleTask getTask(String taskId) {
        return createTask(taskId, 0);
    }

    public SimpleTask getTask(int resId) {
        return null;
    }

    /**
     * 销毁ApplicationContext
     */
    public void destroy() {

    }



    private int getItemResourceValue(String str) {
        String tmpStr = str.substring(2, str.length() - 1);
        int res = context.getResources().getIdentifier(tmpStr, "string", context.getPackageName());

        return res;
    }

    /**
     * 根据action名，创建action对象
     * @param actionName    action名称
     * @return  IAction对象
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
     * 根据消息名称查询处理该消息的action
     * @param notifyId      消息ID
     * @return
     */
    private ActionEntity findActionByNotifyId(String notifyId) {
        for (int i = 0; i < actionlist.size(); i++) {
            ActionEntity actionEntity = actionlist.get(i);
            ArrayList<Notify> notifies = actionEntity.getNotifies();

            for (int j = 0; j < notifies.size(); j++) {
                if (notifyId.equalsIgnoreCase(notifies.get(i).getId())) {
                    return actionEntity;
                }
            }
        }

        return null;
    }

    /**
     * 根据消息名称查询需要发送的消息
     * @param notifyId      消息ID
     * @return
     */
    private Notify findNotifyById(String notifyId) {
        for (int i = 0; i < actionlist.size(); i++) {
            ActionEntity actionEntity = actionlist.get(i);
            ArrayList<Notify> notifies = actionEntity.getNotifies();

            for (int j = 0; j < notifies.size(); j++) {
                if (notifyId.equalsIgnoreCase(notifies.get(i).getId())) {
                    return notifies.get(i);
                }
            }
        }

        return null;
    }

    /**
     * 根据task 的资源ID来创建task
     * @param resId
     * @return
     */
    private SimpleTask createTask(int resId) {
        String taskId = context.getString(resId);

        return createTask(taskId, resId);
    }

    /**
     * 创建task对象
     * @param taskId    task的ID
     * @param resId     task引用的资源ID
     * @return
     */
    private SimpleTask createTask(String taskId, int resId) {
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
     * 根据task id查找task
     * @param taskId    task id
     * @return
     */
    private TaskEntity findTask(String taskId) {
        for (int i = 0; i < taskList.size(); i++) {
            TaskEntity taskEntity = taskList.get(i);
            if (taskId.equalsIgnoreCase(taskEntity.getId())) {
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

                            String actionId = pullParser.getAttributeValue(null, "id");

                            if (actionId.startsWith("$")) {
                                actionId = context.getString(getItemResourceValue(actionId));
                            }

                            Logger.getLogger().d("actionId : " + actionId);

                            actionEntity.setId(actionId);
                            actionEntity.setName(pullParser.getAttributeValue(null, "name"));

                            notifies = new ArrayList<Notify>();
                        } else if ("notify".equalsIgnoreCase(startTagName)) {
                            Notify notify = new Notify();

                            String notifyId = pullParser.getAttributeValue(null, "id");

                            if (notifyId.startsWith("$")) {
                                int notifyResId = getItemResourceValue(notifyId);
                                notifyId = context.getString(notifyResId);
                                notify.setResId(notifyResId);
                            }

                            notify.setId(notifyId);
                            notify.setName(pullParser.getAttributeValue(null, "name"));
                            notifies.add(notify);

                        } else if ("task-config".equalsIgnoreCase(startTagName)) {
                            taskList = new ArrayList<>();

                        } else if ("task".equalsIgnoreCase(startTagName)) {
                            taskEntity = new TaskEntity();

                            String taskId = pullParser.getAttributeValue(null, "id");

                            if (taskId.startsWith("$")) {
                                taskEntity.setResId(getItemResourceValue(taskId));
                                taskId = context.getString(getItemResourceValue(taskId));
                            }

                            taskEntity.setId(taskId);
                            taskEntity.setName(pullParser.getAttributeValue(null, "name"));

                        } else if ("type".equalsIgnoreCase(startTagName)) {
                            taskEntity.setType(pullParser.nextText());

                        } else if ("meta-data".equalsIgnoreCase(startTagName)) {
                            taskEntity.setMetaData(pullParser.nextText());
                        }
                        break;

                    case XmlPullParser.END_TAG:
                        String endTagName = pullParser.getName();
                        if ("action".equalsIgnoreCase(endTagName)) {
                            actionEntity.setNotifies(notifies);
                            actionlist.add(actionEntity);
                        } else if ("task".equalsIgnoreCase(endTagName)) {
                            taskList.add(taskEntity);
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