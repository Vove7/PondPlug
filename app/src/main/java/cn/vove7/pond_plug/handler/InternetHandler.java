package cn.vove7.pond_plug.handler;

import android.content.Context;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import cn.vove7.pond_plug.utils.ResponseMessage;
import cn.vove7.pond_plug.utils.Snode;
import com.google.gson.Gson;
import okhttp3.*;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Vove on 2017/6/14.
 * 响应处理
 */

public class InternetHandler {
    private Gson gson=new Gson();
    private static URL url = null;
    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private MessageHandler handleMessage;

    public InternetHandler(Context context) {
        handleMessage=new MessageHandler(context);
        try {
            url=new URL("http://172.20.53.229:8080/handlePond");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }


    public ResponseMessage postData(Snode startNode) {

        String jsonData=gson.toJson(startNode);//转换json

        OkHttpClient client = new OkHttpClient();
        RequestBody body = RequestBody.create(JSON, jsonData);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        try {
            Response response = client.newCall(request).execute();

            Message msg = new Message();
            Bundle bundle = new Bundle();
            if (response.isSuccessful()) {
                bundle.putString("message", "请求发送成功");
                msg.setData(bundle);
                handleMessage.sendMessage(msg);
                String responseJson=response.body().string();
                Log.d("responseJson",responseJson);
                return gson.fromJson(responseJson,ResponseMessage.class);
            } else {
                bundle.putString("message", "请求发送失败");
                msg.setData(bundle);
                handleMessage.sendMessage(msg);
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

}
