package com.android.javier.demo.tasks;

import com.android.javier.demo.entities.UserLoginEntity;
import com.android.javier.simplemvc.entity.TaskEntity;
import com.android.javier.simplemvc.net.ErrorEntity;
import com.android.javier.simplemvc.net.ResponseEntity;
import com.android.javier.simplemvc.tasks.SimpleNetworkTask;

/**
 * Created by javier on 2016/3/27.
 */
public class LoginTask extends SimpleNetworkTask<UserLoginEntity> {
    public LoginTask(TaskEntity entity) {
        super(entity);
    }

    @Override
    protected UserLoginEntity onResponse(ResponseEntity responseEntity) {

        return null;
    }

    @Override
    protected ErrorEntity onResponseError(ResponseEntity responseEntity) {
        return null;
    }
}