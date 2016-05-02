package com.android.javier.demo.tasks;

import com.android.javier.demo.R;
import com.android.javier.demo.entities.UserLoginEntity;
import com.javier.simplemvc.modules.task.SimpleNetTask;
import com.javier.simplemvc.net.ResponseEntity;

import org.json.JSONObject;

/**
 * Created by javier
 */
public class LoginTask extends SimpleNetTask {
    @Override
    protected void handlerResponse(ResponseEntity responseEntity) {
        super.handlerResponse(responseEntity);

        try {
            JSONObject jsonObject = new JSONObject(responseEntity.getContent());

            UserLoginEntity entity = new UserLoginEntity();
            entity.setId(jsonObject.getInt("id"));
            entity.setAccount(jsonObject.getString("account"));

            sendNotifyMessage(R.integer.msg_login_success, entity);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void handlerError(ResponseEntity responseEntity) {
        super.handlerError(responseEntity);
        sendNotifyMessage(R.integer.msg_login_failed, responseEntity.getContent());
    }
}