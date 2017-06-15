package cn.vove7.pond_plug.utils;

import java.util.ArrayList;

/**
 * Created by Vove on 2017/6/14.
 * 路径
 */

public class ResponseMessage {
    private String message;
    private boolean isHaveResult;
    private int stepNum;
    private String useTime;
    private ArrayList<Step> steps=new ArrayList<>();

    private int[] lastFishCoor;

    public int[] getLastFishCoor() {
        return lastFishCoor;
    }

    public void setLastFishCoor(int[] lastFishCoor) {
        this.lastFishCoor = lastFishCoor;
    }

    public int getStepNum() {
        return stepNum;
    }

    public void setStepNum(int stepNum) {
        this.stepNum = stepNum;
    }

    public ArrayList<Step> getSteps() {
        return steps;
    }

    public boolean isHaveResult() {
        return isHaveResult;
    }

    public void setHaveResult(boolean haveResult) {
        isHaveResult = haveResult;
    }

    public void setSteps(ArrayList<Step> steps) {
        this.steps = steps;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getUseTime() {
        return useTime;
    }

    public void setUseTime(String useTime) {
        this.useTime = useTime;
    }
}
class Step{
    private int index;
    private int stepNum;
    private int bumpNo;//块序号
    private Integer[] bumpCoor=new Integer[2];
    private char direction;//方向

    public Integer[] getBumpCoor() {
        return bumpCoor;
    }

    public void setBumpCoor(Integer[] bumpCoor) {
        this.bumpCoor = bumpCoor;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public int getStepNum() {
        return stepNum;
    }

    public void setStepNum(int stepNum) {
        this.stepNum = stepNum;
    }

    public char getDirection() {
        return direction;
    }

    public void setDirection(char direction) {
        this.direction = direction;
    }

    public int getBumpNo() {
        return bumpNo;
    }

    public void setBumpNo(int bumpNo) {
        this.bumpNo = bumpNo;
    }
}