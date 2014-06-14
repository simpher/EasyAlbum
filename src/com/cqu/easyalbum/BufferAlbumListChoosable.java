package com.cqu.easyalbum;

import android.content.Context;

import com.cqu.util.ArrayUtil;
import com.cqu.util.SharedPrefUtil;

public class BufferAlbumListChoosable {
	
	private static final String KEY_ALBUM_LIST_ITEMS="ALBUM_LIST_ITEM";
	private static final int MAX_BUFFERED_ALBUM_ITEM_COUNT=6;
	
	public static String[] getAvailAlbums(Context context)
	{
		String[] values=SharedPrefUtil.readoutValues(context, new String[]{KEY_ALBUM_LIST_ITEMS});
		if(values.length>0&&values[0]!=null&&values[0].length()>0)
		{
			return values[0].split(":");
		}
		return null;
	}
	
	public static void saveAvailAlbums(Context context, String latestAlbumName)
	{
		String itemsStringNew=latestAlbumName;
		String[] bufferedItemsList=getAvailAlbums(context);
		if(bufferedItemsList!=null)
		{
			if(ArrayUtil.inArray(bufferedItemsList, latestAlbumName)==-1)
			{
				int len=Math.min(bufferedItemsList.length, MAX_BUFFERED_ALBUM_ITEM_COUNT-1);
				for(int i=0;i<len;i++)
				{
					itemsStringNew+=":"+bufferedItemsList[i];
				}
			}
		}
		SharedPrefUtil.writeValues(context, new String[]{KEY_ALBUM_LIST_ITEMS}, new String[]{itemsStringNew});
	}
	
	public static void removeInvalidAlbums(Context context, String invalidAlbumName)
	{
		
		String[] bufferedItemsList=getAvailAlbums(context);
		if(bufferedItemsList!=null)
		{
			int index=ArrayUtil.inArray(bufferedItemsList, invalidAlbumName);
			if(index!=-1)
			{
				String itemsStringNew="";
				int len=Math.min(bufferedItemsList.length, MAX_BUFFERED_ALBUM_ITEM_COUNT-1);
				for(int i=0;i<len;i++)
				{
					if(i==index)
					{
						continue;
					}
					itemsStringNew+=bufferedItemsList[i]+":";
				}
				if(itemsStringNew.length()>0)
				{
					itemsStringNew=itemsStringNew.substring(0, itemsStringNew.length()-1);
				}
				SharedPrefUtil.writeValues(context, new String[]{KEY_ALBUM_LIST_ITEMS}, new String[]{itemsStringNew});
			}
		}
	}
}
