package cn.vove7.pond_plug.handler;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

/**
 * Created by Vove on 2017/6/12.
 * MessageHandler
 */

public class MessageHandler extends Handler {
    private Context context;

    public MessageHandler(Context context) {
        this.context = context;
    }

    @Override
    public void handleMessage(Message msg) {
        Bundle bundle=msg.getData();
        String message=bundle.getString("message");
        System.out.print(message);
        Toast.makeText(context,message,Toast.LENGTH_SHORT).show();
        super.handleMessage(msg);
    }
}
