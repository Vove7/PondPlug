package cn.vove7.pond_plug.utils;

import android.util.DisplayMetrics;

import cn.vove7.pond_plug.FloatWindow;

import java.util.ArrayList;

import static cn.vove7.pond_plug.utils.Snode.N;

/**
 * Created by Vove on 2017/6/15.
 * 模拟操作
 */

public class SimulateScreen {
    private FloatWindow floatWindow;
    private int maBeginX;
    private int maBeginY;
    private int bumpHeight;
    private int bumpWidth;
    private static int speed = 280;

    public static void setSpeed(int speed) {
        SimulateScreen.speed = speed;
    }

    public SimulateScreen(FloatWindow floatWindow) {
        this.floatWindow = floatWindow;

        DisplayMetrics metrics = floatWindow.getContext().getResources().getDisplayMetrics();

        int height = metrics.heightPixels;//屏幕高
        int width = metrics.widthPixels;//屏幕宽
        maBeginX = (int) (height * 0.2242);
        maBeginY = (int) (width * 0.0486);
        int matrixHeight = (int) (height * 0.5109);
        int matrixWidth = (int) (width * 0.9048);
        bumpHeight = (matrixHeight / (2 * N));
        bumpWidth = (matrixWidth / (2 * N));

    }

    public void simulateOperate(ResponseMessage responseMessage) {
        ArrayList<Step> steps = responseMessage.getSteps();
        StringBuilder builder = new StringBuilder();

        int bumpCoorY, bumpCoorX, stepNum, stepRate, beginX, beginY, endX, endY, plus,moveTime;
        int deltX, deltY;//移动距离
        String swipeCmd;

        for (Step step : steps) {
            bumpCoorY = step.getBumpCoor()[0];
            bumpCoorX = step.getBumpCoor()[1];//移动块坐标
            stepNum = step.getStepNum();//步数
            stepRate = (2 * (stepNum - 1) + 1);

            beginX = maBeginY + (2 * bumpCoorX + 1) * bumpWidth;
            beginY = maBeginX + (2 * bumpCoorY + 1) * bumpHeight;

            //速度距离控制
            plus = speed < 270&&stepNum!=1 ? (-20 - stepNum * 5) : speed > 330 ? (5 - stepNum) * 13 : 10;

            deltX = bumpWidth * stepRate + plus;
            deltY = bumpHeight * stepRate + plus;

            switch (step.getDirection()) {
                case 'U':
                    endX = beginX;
                    endY = beginY - deltY;
                    break;
                case 'D':
                    endX = beginX;
                    endY = beginY + deltY;
                    break;
                case 'R':
                    endX = beginX + deltX;
                    endY = beginY;
                    break;
                case 'L':
                    endX = beginX - deltX;
                    endY = beginY;
                    break;
                default:
                    return;
            }
            moveTime = stepNum * speed;
            swipeCmd = "input swipe " + beginX + " " + beginY + " " + endX + " " + endY + " " + moveTime + " \n";
            builder.append(swipeCmd);
        }
        beginX = maBeginY + (2 * responseMessage.getLastFishCoor()[1] + 1) * bumpWidth;
        beginY = maBeginX + (2 * responseMessage.getLastFishCoor()[0] + 1) * bumpHeight;
        endX = maBeginX + 13 * bumpWidth;
        String lastCmd = "input swipe " + beginX + " " + beginY + " " + endX + " " + beginY + " 300";
        builder.append(lastCmd);
//        Log.d("swipeCmd",builder.toString());
        executeCmd(builder.toString());
    }

    private void executeCmd(String cmd) {
        RootCmd.rootCmd(cmd);
    }
}
