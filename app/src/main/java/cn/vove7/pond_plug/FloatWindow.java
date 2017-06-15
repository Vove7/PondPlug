package cn.vove7.pond_plug;

import android.content.Context;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.view.*;
import android.widget.ImageView;
import android.widget.Toast;
import cn.vove7.pond_plug.handler.MessageHandler;
import cn.vove7.pond_plug.handler.InternetHandler;
import cn.vove7.pond_plug.utils.*;

import java.util.Date;


/**
 * Created by Vove on 2017/4/9.
 * 悬浮窗
 */

public class FloatWindow {
    private static int position;

    static {
        position = 40;
    }

    private static long lastClickTime = new Date().getTime();
    private static boolean isRunning = false;
    private Context context;
    private View view;
    private WindowManager windowManager = null;
    private WindowManager.LayoutParams mParams = null;
    private Snode startNode;
    private MessageHandler handler;
    private CaptureScreen captureScreen = null;
    private InternetHandler internetHandler;
    private SimulateScreen simulateScreen;

    public FloatWindow(final Context context, final View view) {
        this.context = context;
        this.view = view;

        handler = new MessageHandler(context);
        internetHandler = new InternetHandler(context);
        captureScreen = new CaptureScreen(context);

        simulateScreen=new SimulateScreen(this);
        showFloatWindow();
    }

    public Context getContext() {
        return context;
    }

    @RequiresApi(api = Build.VERSION_CODES.HONEYCOMB_MR2)
    private void initView() {
        if (windowManager == null) {
            windowManager = (WindowManager) context.getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
        }
        final Point size = new Point();
        windowManager.getDefaultDisplay().getSize(size);
        mParams = new WindowManager.LayoutParams();
        mParams.packageName = context.getPackageName();
        mParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
        mParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        mParams.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                | WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN
                | WindowManager.LayoutParams.FLAG_LAYOUT_INSET_DECOR;

        mParams.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
        mParams.format = PixelFormat.RGBA_8888;
        mParams.gravity = Gravity.TOP | Gravity.START;

        mParams.x = dp2px(position);
        mParams.y = dp2px(position);

        view = LayoutInflater.from(context).inflate(R.layout.float_layout, null);

        final ImageView imageView = (ImageView) view.findViewById(R.id.float_icon);

        imageView.setOnClickListener(new View.OnClickListener() {//点击事件，否则move事件不触发？
            @Override
            public void onClick(View v) {
                //captureScreen_su();//截屏
            }
        });
        imageView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                //simulateOperate();
                return false;
            }
        });
        imageView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_MOVE: {
                        //event.getRawX()  获取手指位置
                        mParams.x = (int) event.getRawX() - imageView.getWidth() / 2;
                        mParams.y = (int) event.getRawY() - imageView.getHeight() / 2;
                        windowManager.updateViewLayout(view, mParams);
                    }
                    break;
                    case MotionEvent.ACTION_DOWN: {
                        //双击
                        long currentClickTime = new Date().getTime();
                        if ((currentClickTime - lastClickTime) < 190) {
                            if (!isRunning) {
                                isRunning = true;
                                begin();
                            } else {
                                Toast.makeText(view.getContext(), "正在运行中，请稍后", Toast.LENGTH_SHORT).show();
                            }
                        } else lastClickTime = currentClickTime;
                    }
                    break;
                }
                return false;
            }
        });
        view.findViewById(R.id.close_float).
                setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        hideFloat();
                    }
                });
    }


    private void begin() {

        view.findViewById(R.id.scrollView).setVisibility(View.GONE);
//        Toast.makeText(context, "开始运行.", Toast.LENGTH_SHORT).show();
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                if (!captureScreen.captureScreen_su()) {//截屏
                    isRunning = false;
                    return;
                }

                startNode = new Snode();

                HandleScreen.scanPic(startNode);//处理截图

                ResponseMessage responseMessage = internetHandler.postData(startNode);
                if(responseMessage!=null&&responseMessage.isHaveResult()){
                    //模拟屏幕操作
                    simulateScreen.simulateOperate(responseMessage);
                }
                //

                isRunning = false;//运行结束
            }
        });
        thread.setPriority(Thread.MAX_PRIORITY);
        thread.start();

        //
    }

    void showFloatWindow() {
        if (view == null) {
            initView();
            windowManager.addView(view, mParams);
            //windowManager.addView(imageView,mParams);
        }
    }

    void hideFloat() {
        if (view != null) {
            windowManager.removeView(view);
            view = null;
        }
    }

    public int dp2px(float dp) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.1f);
    }

}