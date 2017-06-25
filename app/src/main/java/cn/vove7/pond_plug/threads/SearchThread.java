package cn.vove7.pond_plug.threads;

import android.content.Context;
import android.os.Vibrator;

import cn.vove7.pond_plug.FloatWindow;
import cn.vove7.pond_plug.R;
import cn.vove7.pond_plug.handler.InternetHandler;
import cn.vove7.pond_plug.handler.ToastHelper;
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
    private CaptureScreen captureScreen = null;
    private SimulateScreen simulateScreen;
    private InternetHandler internetHandler;
    private ToastHelper toastHelper;
    private Vibrator vibrator;
    private FloatWindow floatWindow;

    public SearchThread(Context context,FloatWindow floatWindow) {
        this.floatWindow=floatWindow;
        internetHandler = new InternetHandler(context);//网络处理
        captureScreen = new CaptureScreen(context);//截屏
//        handleMessage=new MessageHandler(context);//多线程通知处理
        toastHelper=new ToastHelper(context);
        simulateScreen = new SimulateScreen(context);//模拟操作
        vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);//震动
        setPriority(Thread.MAX_PRIORITY);
    }

    @Override
    public void run() {
        if (!internetHandler.testInternet()) {//测试网络
            toastHelper.showNotify(R.string.internet_error);
            finish();
            return;
        }

        toastHelper.showNotify(R.string.begin_run);

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

        if(startNode.getBnum()<=3){
            toastHelper.showNotify(R.string.operate_hint);
            finish();
            return;
        }

        ResponseMessage responseMessage = internetHandler.postData(startNode);//发送数据
        if (responseMessage != null && responseMessage.isHaveResult()) {//返回结果
            //模拟屏幕操作
            simulateScreen.simulateOperate(responseMessage);
        }
        toastHelper.showNotify(R.string.finish_run);
        finish();
    }

    private void finish(){
        floatWindow.isRunning = false;//运行结束
        floatWindow.changeBtnStatus(R.drawable.begin);
        interrupt();
    }

}
