package com.cqu.util;

import android.graphics.Bitmap;

public class BitmapUtil {
	
	public static void recycleBmp(Bitmap unusedBmp)
	{
		if(unusedBmp!=null)
		{
			unusedBmp.recycle();
			System.gc();
		}
	}
}
