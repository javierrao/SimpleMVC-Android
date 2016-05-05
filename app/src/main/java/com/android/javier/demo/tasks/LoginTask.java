package com.android.javier.demo.tasks;

import com.android.javier.demo.R;
import com.android.javier.demo.entities.UserLoginEntity;
import com.javier.simplemvc.interfaces.ITaskCallback;
import com.javier.simplemvc.modules.task.SimpleNetTask;
import com.javier.simplemvc.net.ErrorEntity;
import com.javier.simplemvc.net.ResponseEntity;

import org.json.JSONObject;

/**
 * Created by javier
 */
public class LoginTask extends SimpleNetTask<UserLoginEntity> {
    public LoginTask(ITaskCallback callback) {
        super(callback);
    }

    @Override
    protected UserLoginEntity onResponse(ResponseEntity responseEntity) throws Exception {
        try {
            JSONObject jsonObject = new JSONObject(responseEntity.getContent());

            UserLoginEntity entity = new UserLoginEntity();
            entity.setId(jsonObject.getInt("id"));
            entity.setAccount(jsonObject.getString("account"));

            return entity;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return super.onResponse(responseEntity);
    }

    @Override
    protected ErrorEntity onResponseError(ResponseEntity responseEntity) {
        return super.onResponseError(responseEntity);
    }
}