package com.javier.simplemvc.core;

import android.util.SparseArray;

import com.javier.simplemvc.SimpleContext;
import com.javier.simplemvc.interfaces.ICommand;
import com.javier.simplemvc.interfaces.IDisplay;
import com.javier.simplemvc.interfaces.IObserverFunction;
import com.javier.simplemvc.modules.notify.NotifyMessage;
import com.javier.simplemvc.utils.Logger;

import java.lang.reflect.Constructor;
import java.util.ArrayList;

/**
 * author:Javier
 * time:2016/4/30.
 * mail:38244704@qq.com
 * <p/>
 * 管理command的类，用于注册command，注销command, 执行command.
 * 并且维护注册的command的序列
 */
@SuppressWarnings("unused")
public final class Controller implements IObserverFunction {
    private static Controller controller;

    private SparseArray<CommandObserverMap> commandSparseArray = new SparseArray<>();

    /**
     * 单例的方式获取Controller对象
     *
     * @return Controller对象
     */
    public synchronized static Controller getInstance() {
        if (controller == null)
            controller = new Controller();

        return controller;
    }

    /**
     * 注册command
     *
     * @param commandClass 与消息对于的command类的class对象
     * @param display      显示对象，用于command对显示的回调，也用户创建mediator对象
     */
    public void registerCommand(Class commandClass, IDisplay display) {
        Constructor<?> cons[] = commandClass.getConstructors();
        try {
            ICommand command = (ICommand) cons[0].newInstance(display);
            registerCommand(command);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 移除注册command
     *
     * @param what 消息ID
     */
    public void removeCommand(Class commandClass) {

        ArrayList<Integer> keys = new ArrayList<>();

        for (int i = 0; i < commandSparseArray.size(); i++) {
            int key = commandSparseArray.keyAt(i);

            CommandObserverMap map = commandSparseArray.get(key);

            if (map.command.getClass() == commandClass) {
                map.command.onRemove();

                SimpleContext.getInstance().removeObserver(key, map.observer);

                keys.add(key);
            }
        }

        for (int key : keys) {
            commandSparseArray.remove(key);
        }
    }


    @Override
    public void ObserverFunction(NotifyMessage message) {
        if (commandSparseArray.size() == 0) {
            return;
        }

        executeCommand(message);
    }


    private void registerCommand(ICommand command) {
        int[] commandListMessage = command.listMessage();

        if (commandListMessage == null) {
            return;
        }

        Observer observer = new Observer(this);

        CommandObserverMap map = new CommandObserverMap();
        map.command = command;
        map.observer = observer;

        for (int message : commandListMessage) {
            SimpleContext.getInstance().registerObserver(message, observer);
            commandSparseArray.put(message, map);
        }

        command.onRegister();
    }

    private void executeCommand(NotifyMessage message) {
        if (commandSparseArray.get(message.getWhat()) == null) {
            Logger.getLogger().e("message for " + message.getWhat() + " may not be registered");
            return;
        }

        commandSparseArray.get(message.getWhat()).command.execute(message);
    }

    private class CommandObserverMap {
        ICommand command;
        Observer observer;

        public void clean() {
            command = null;
            observer = null;
        }
    }
}