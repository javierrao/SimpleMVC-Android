package com.android.javier.demo.tasks;

import com.android.javier.demo.entities.UserLoginEntity;
import com.android.javier.simplemvc.entity.TaskEntity;
import com.android.javier.simplemvc.net.ErrorEntity;
import com.android.javier.simplemvc.net.ResponseEntity;
import com.android.javier.simplemvc.tasks.SimpleNetworkTask;

import org.json.JSONObject;

/**
 * Created by javier
 */
public class LoginTask extends SimpleNetworkTask<UserLoginEntity> {
    public LoginTask(TaskEntity entity) {
        super(entity);
    }

    @Override
    protected UserLoginEntity onResponse(ResponseEntity responseEntity) throws Exception {
        UserLoginEntity entity = new UserLoginEntity();
        JSONObject jsonObject = responseEntity.getJsonContent();
        entity.setAccount(jsonObject.getString("account"));
        entity.setPassword("abc123");

        return entity;
    }

    @Override
    protected ErrorEntity onResponseError(ResponseEntity responseEntity) {
        return null;
    }
}