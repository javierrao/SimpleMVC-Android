package com.javier.simplemvc.modules.mediator;

import com.javier.simplemvc.interfaces.IDisplay;
import com.javier.simplemvc.interfaces.IMediator;

/**
 * author:Javier
 * time:2016/4/30.
 * mail:38244704@qq.com
 */
public abstract class SimpleMediator implements IMediator {
    protected IDisplay display;
    protected int id;

    protected SimpleMediator(int id, IDisplay display) {
        this.display = display;
        this.id = id;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public void onRemove() {

    }

    @Override
    public void onRegister() {

    }
}