package cn.vove7.pond_plug.handler;

import android.content.Context;
import android.os.Bundle;
import android.os.Message;

import cn.vove7.pond_plug.R;
import cn.vove7.pond_plug.utils.ResponseMessage;
import cn.vove7.pond_plug.utils.Snode;

import com.google.gson.Gson;

import okhttp3.*;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

/**
 * Created by Vove on 2017/6/14.
 * 响应处理
 */

public class InternetHandler {
    private Context context;
    private Gson gson = new Gson();
    private static URL executeUrl = null;
    private static URL testUrl = null;
    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private MessageHandler handleMessage;

    public InternetHandler(Context context) {
        this.context=context;
        handleMessage = new MessageHandler(context);
        try {
            executeUrl = new URL("http://115.159.155.25/PondPlugServer/handlePond");
            testUrl = new URL("http://115.159.155.25/PondPlugServer");
//            executeUrl=new URL("http://172.20.53.240:8080/handlePond");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    public boolean testInternet() {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(testUrl)
                .addHeader("Connection", "close")
                .build();
        try {
            Response response = client.newCall(request).execute();

            return response.isSuccessful();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public ResponseMessage postData(Snode startNode) {

        Message msg = new Message();
        Bundle bundle = new Bundle();

        String jsonData = gson.toJson(startNode);//转换json

        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(15, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .build();
        RequestBody body = RequestBody.create(JSON, jsonData);
        Request request = new Request.Builder()
                .url(executeUrl)
                .post(body)
                .build();
        try {
            Response response = client.newCall(request).execute();

            if (response.isSuccessful()) {
                String responseJson = response.body().string();
//                Log.d("responseJson",responseJson);
                ResponseMessage responseMessage = gson.fromJson(responseJson, ResponseMessage.class);

                bundle.putString("message", responseMessage.getMessage()
                        + (responseMessage.isHaveResult() ? "--" + responseMessage.getStepNum() + "步" : "111"));
                msg.setData(bundle);
                handleMessage.sendMessage(msg);
                return responseMessage;
            } else {
                bundle.putString("message",context.getString(R.string.internet_error));
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
