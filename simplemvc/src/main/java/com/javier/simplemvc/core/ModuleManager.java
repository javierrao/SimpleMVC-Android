package com.javier.simplemvc.core;

import android.util.SparseArray;

import com.javier.simplemvc.SimpleContext;
import com.javier.simplemvc.interfaces.IModule;
import com.javier.simplemvc.interfaces.IObserverFunction;
import com.javier.simplemvc.modules.notify.NotifyMessage;
import com.javier.simplemvc.modules.notify.Observer;

import java.util.ArrayList;

/**
 * author:Javier
 * time:2016/5/5.
 * mail:38244704@qq.com
 */
public final class ModuleManager {

    private SparseArray<IModule> moduleSparseArray = new SparseArray<>();

    private static ModuleManager moduleManager;


    public static ModuleManager getModuleManager() {
        if (moduleManager == null) {
            moduleManager = new ModuleManager();
        }

        return moduleManager;
    }

    public void registerModule(IModule module) {
        int[] moduleListMessage = module.listMessage();

        if (moduleListMessage == null || moduleListMessage.length == 0) {
            return;
        }

        for (int message : moduleListMessage) {
            SimpleContext.getInstance().registerObserver(message, observer);
            moduleSparseArray.put(message, module);
        }

        module.onRegister();
    }

    public void registerModule(Class moduleClass) {
        try {
            IModule module = (IModule) moduleClass.newInstance();
            registerModule(module);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void removeModule(Class moduleClass) {
        for (int i = 0; i < moduleSparseArray.size(); i++) {
            int what = moduleSparseArray.keyAt(i);

            IModule module = moduleSparseArray.get(what);

            if (module.getClass() == moduleClass) {
                SimpleContext.getInstance().removeObserver(what, observer);
                moduleSparseArray.removeAt(i);
                break;
            }
        }
    }

    public void removeModule(IModule moduleObject) {
        for (int i = 0; i < moduleSparseArray.size(); i++) {
            int what = moduleSparseArray.keyAt(i);

            IModule module = moduleSparseArray.get(what);

            if (module == moduleObject) {
                SimpleContext.getInstance().removeObserver(what, observer);
                moduleSparseArray.removeAt(i);
                break;
            }
        }
    }

    public void removeAllModule() {
        for (int i = 0; i < moduleSparseArray.size(); i++) {
            int what = moduleSparseArray.keyAt(i);
            SimpleContext.getInstance().removeObserver(what, observer);
        }
    }

    private Observer observer = new Observer(new IObserverFunction() {
        @Override
        public void ObserverFunction(NotifyMessage message) {
            IModule module = moduleSparseArray.get(message.getWhat());

            if (module != null) {
                module.handlerMessage(message);
            }
        }
    });
}