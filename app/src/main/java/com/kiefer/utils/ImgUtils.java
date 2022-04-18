package com.kiefer.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.kiefer.LLPPDRUMS;
import com.kiefer.R;
import com.kiefer.ui.tabs.TabManager;

import java.util.ArrayList;
import java.util.Random;

public class ImgUtils {

    public static Bitmap getTabImg(LLPPDRUMS llppdrums, int imgId, int tabNo, int nOfTabs, int orientation){
        if(imgId > 0) {

            Bitmap bitmap = BitmapFactory.decodeResource(llppdrums.getResources(), imgId); //create a bitmap from the drawable id

            int tabSize = (int) llppdrums.getResources().getDimension(R.dimen.tabsSize);

            if (orientation == TabManager.VERTICAL) {

                bitmap = Bitmap.createBitmap(
                        bitmap,
                        0,
                        (bitmap.getHeight() / nOfTabs) * tabNo, //tabs start at 0
                        tabSize - 1,
                        (bitmap.getHeight() / nOfTabs) - 1
                );
            } else {
                bitmap = Bitmap.createBitmap(
                        bitmap,
                        (bitmap.getWidth() / nOfTabs) * tabNo,
                        0,
                        bitmap.getWidth() / nOfTabs,
                        tabSize
                );
            }
            return bitmap;
        }
        return null;
    }

    public static Bitmap getBgImg(LLPPDRUMS llppdrums, int imgId, int orientation){

        if(imgId > 0) {

            Bitmap bitmap = BitmapFactory.decodeResource(llppdrums.getResources(), imgId);

            int tabSize = (int) llppdrums.getResources().getDimension(R.dimen.tabsSize);

            if (orientation == TabManager.VERTICAL) {
                bitmap = Bitmap.createBitmap(
                        bitmap,
                        tabSize,
                        0,
                        bitmap.getWidth() - tabSize,
                        bitmap.getHeight()
                );
            } else {
                bitmap = Bitmap.createBitmap(
                        bitmap,
                        0,
                        tabSize,
                        bitmap.getWidth(),
                        bitmap.getHeight() - tabSize
                );
            }

            return bitmap;
        }
        return null;
    }

    public static int getRandomImageId(){
        ArrayList<Integer> list = new ArrayList<>();
        list.add(R.drawable.bg_beach_small);
        list.add(R.drawable.bg_cat_small);
        list.add(R.drawable.bg_gtr_small);
        list.add(R.drawable.bg_ko_small);
        list.add(R.drawable.bg_rat_small);
        list.add(R.drawable.bg_dog_small);
        list.add(R.drawable.bg_tango_small);

        Random r = new Random();
        return list.get(r.nextInt(list.size()));
        //return -1;
    }
}
