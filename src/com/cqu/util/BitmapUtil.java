package com.cqu.util;

import com.cqu.bean.ImageItem;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class BitmapUtil {
	
	public static void recycleBmp(Bitmap unusedBmp)
	{
		if(unusedBmp!=null)
		{
			unusedBmp.recycle();
			System.gc();
		}
	}
	
	public static Bitmap loadImageBitmap(ImageItem imageItem, int maxSampleSize)
	{
		Bitmap bmp=null;
		BitmapFactory.Options ops=new BitmapFactory.Options();
		ops.inSampleSize=1;
		while(true)
		{
			try{
				bmp=BitmapFactory.decodeFile(imageItem.getDir()+"/"+imageItem.getName(), ops);
				return bmp;
			}catch(OutOfMemoryError err)
			{
				if(ops.inSampleSize>=maxSampleSize)
				{
					return null;
				}else
				{
					ops.inSampleSize=ops.inSampleSize+1;
				}
			}
		}
	}
}
