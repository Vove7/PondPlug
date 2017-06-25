package cn.vove7.pond_plug.handler;

import android.content.Context;
import android.os.Bundle;
import android.os.Message;

import cn.vove7.pond_plug.R;

/**
 * Created by Administrator on 2017/6/25.
 * 子线程消息通知
 */

public class ToastHelper {
    private MessageHandler handleMessage;
    private Context context;

    public ToastHelper(Context context) {
        this.context=context;
        handleMessage=new MessageHandler(context);
    }

    public void showNotify(String text){
        Message msg = new Message();
        Bundle bundle = new Bundle();
        bundle.putString("message",text);
        msg.setData(bundle);
        handleMessage.sendMessage(msg);
    }
    public void showNotify(int resId){
        Message msg = new Message();
        Bundle bundle = new Bundle();
        bundle.putString("message",context.getString(resId));
        msg.setData(bundle);
        handleMessage.sendMessage(msg);
    }
}
