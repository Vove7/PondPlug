package cn.vove7.pond_plug.utils;

import java.util.ArrayList;

/**
 * Created by Vove on 2017/5/3.
 */

public class SnodeArrayList extends ArrayList<Snode> {
   private int q=0;
   boolean queueIsEmpty(){
      return q>=this.size();
   }
   Snode DeQueue(){
      return this.get(q++);
   }

   @Override
   public void clear() {
      q=0;
      super.clear();
   }
}
