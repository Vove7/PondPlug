package cn.vove7.pond_plug.threads;

import android.content.Context;
import android.os.Bundle;
import android.os.Message;
import android.os.Vibrator;

import cn.vove7.pond_plug.FloatWindow;
import cn.vove7.pond_plug.R;
import cn.vove7.pond_plug.handler.InternetHandler;
import cn.vove7.pond_plug.handler.MessageHandler;
import cn.vove7.pond_plug.utils.CaptureScreen;
import cn.vove7.pond_plug.utils.HandleScreen;
import cn.vove7.pond_plug.utils.ResponseMessage;
import cn.vove7.pond_plug.utils.SimulateScreen;
import cn.vove7.pond_plug.utils.Snode;

import static cn.vove7.pond_plug.FloatWindow.isOpenVibrator;

/**
 * Created by Vove on 2017/6/22.
 * 搜索线程
 */

public class SearchThread extends Thread {
    private Context context;
    private CaptureScreen captureScreen = null;
    private SimulateScreen simulateScreen;
    private InternetHandler internetHandler;
    private MessageHandler handleMessage;
    private Vibrator vibrator;

    public SearchThread(Context context) {
        this.context=context;
        internetHandler = new InternetHandler(context);//网络处理
        captureScreen = new CaptureScreen(context);//截屏
        handleMessage=new MessageHandler(context);//多线程通知处理
        simulateScreen = new SimulateScreen(context);//模拟操作
        vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);//震动
        setPriority(Thread.MAX_PRIORITY);
    }

    @Override
    public void run() {
        Message msg = new Message();
        Bundle bundle = new Bundle();
        if (!internetHandler.testInternet()) {//测试网络
            bundle.putString("message",context.getString(R.string.internet_error));
            msg.setData(bundle);
            handleMessage.sendMessage(msg);

            finish();
            return;
        }

        bundle.putString("message",context.getString(R.string.begin_run));
        msg.setData(bundle);
        handleMessage.sendMessage(msg);

        if (isOpenVibrator) {
            long[] pattern = {100, 200};   //停止 开启
            vibrator.vibrate(pattern, -1); //震动一次
        }
        if (!captureScreen.captureScreen_su()) {//截屏
            finish();
            return;
        }
        Snode startNode = new Snode();
        HandleScreen.scanPic(startNode);//处理截图

        ResponseMessage responseMessage = internetHandler.postData(startNode);//发送数据
        if (responseMessage != null && responseMessage.isHaveResult()) {//返回结果
            //模拟屏幕操作
            simulateScreen.simulateOperate(responseMessage);
        }
        finish();
    }

    private void finish(){
        FloatWindow.isRunning = false;//运行结束
        FloatWindow.changeBtnStatus(R.drawable.begin);
        interrupt();
    }

}
