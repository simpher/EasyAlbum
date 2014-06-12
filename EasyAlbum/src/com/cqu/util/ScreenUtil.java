package com.cqu.util;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.WindowManager;

public class ScreenUtil {
	/**
	 * 获得屏幕大小，以pixel为单位
	 * @param context
	 * @return
	 */
	public static Screen getScreenPix(Context context) {
        DisplayMetrics dm = new DisplayMetrics();
        
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        windowManager.getDefaultDisplay().getMetrics(dm);
        
        return new Screen(dm.widthPixels,dm.heightPixels);
    }
	
	public static class Screen{
        public int widthPixels;
        public int heightPixels;
       
        public Screen(){
        }
       
        public Screen(int widthPixels,int heightPixels){
            this.widthPixels=widthPixels;
            this.heightPixels=heightPixels;
        }

        @Override
        public String toString() {
            return "("+widthPixels+","+heightPixels+")";
        }
    }
}
