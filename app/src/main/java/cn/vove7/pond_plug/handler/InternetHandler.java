package cn.vove7.pond_plug.handler;

import android.content.Context;

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
    private Gson gson = new Gson();
    private static URL executeUrl = null;
//    private static URL testUrl = null;
    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private ToastHelper toastHelper;

    public InternetHandler(Context context) {
        toastHelper=new ToastHelper(context);
        try {
            executeUrl = new URL("http://115.159.155.25/PondPlugServer/handlePond");
//            testUrl = new URL("http://115.159.155.25/PondPlugServer");
//            executeUrl=new URL("http://172.20.53.247:8080/handlePond");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    public boolean testInternet() {
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(3, TimeUnit.SECONDS)
                .build();
        Request request = new Request.Builder()
                .url(executeUrl)
                .get()
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

                toastHelper.showNotify((responseMessage.isHaveResult() ? "搜索成功--" + responseMessage.getStepNum() + "步" : "搜索失败"));
                return responseMessage;
            } else {
                toastHelper.showNotify(R.string.internet_error);
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

}
