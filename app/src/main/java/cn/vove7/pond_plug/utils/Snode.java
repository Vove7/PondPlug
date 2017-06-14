package cn.vove7.pond_plug.utils;

/**
 * Created by Vove on 2017/4/16.
 * Snode
 */

public class Snode {
    final static int N = 6;
    private int bnum;
    private int s[][] = new int[N][N];//
    private Bump bump[] = new Bump[20];


    public int getBnum() {
        return bnum;
    }

    public void setBnum(int bnum) {
        this.bnum = bnum;
    }

    public static int getN() {
        return N;
    }

    public int[][] getS() {
        return s;
    }

    public Bump[] getBump() {
        return bump;
    }

    public void setBump(Bump[] bump) {
        this.bump = bump;
    }

    void setS(int[][] s) {
        this.s = s;
    }

    public Snode() {
        for (int i = 0; i < bump.length; i++) {
            bump[i] = new Bump();
        }
    }

    Bump getBumpByIndex(int index) {
        return bump[index];
    }
}