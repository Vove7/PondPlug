package cn.vove7.pond_plug.handler;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.Button;
import android.widget.Toast;


/**
 * Created by Vove on 2017/6/12.
 * MessageHandler
 */

public class MessageHandler extends Handler {
    private Context context;
    private Button button;

    public MessageHandler(Context context) {
        this.context = context;
    }

    public void setButton(Button button) {
        this.button = button;
    }

    @Override
    public void handleMessage(Message msg) {
        if(msg.arg1==1){
            button.setBackgroundResource(msg.arg2);
            super.handleMessage(msg);
            return;
        }
        Bundle bundle=msg.getData();
        String message=bundle.getString("message");
        System.out.print(message);
        Toast.makeText(context,message,Toast.LENGTH_SHORT).show();
        super.handleMessage(msg);
    }


}
