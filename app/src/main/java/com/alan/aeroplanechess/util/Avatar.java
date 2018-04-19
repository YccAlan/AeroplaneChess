package com.alan.aeroplanechess.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.LruCache;


/**
 * Created by yccal on 2017/4/26.
 */

public class Avatar {
    static int dp2px;
    static int colors[]={0x64b5f6,0xff8a65,0x81c784,0x4db6ac,0x90a4ae,0xba68c8,0xf06292, 0xa1887f};
    static int colorsDark[]={0x42a5f5};

    static LruCache<String,Bitmap> cache=new LruCache<String,Bitmap>((int)Runtime.getRuntime().maxMemory()/8){
        @Override
        protected int sizeOf(String key, Bitmap value) {
            return value.getByteCount();
        }
    };

    public static void init(Context context){
        dp2px=(int)(context.getResources().getDisplayMetrics().density+0.5f);
    }

    public static Bitmap getAvatar(String c){
        return getAvatar(c,false);
    }

    public static Bitmap getAvatar(String c,boolean darkColor){
        Bitmap avatar=cache.get(c);
        if (avatar==null){
            int w=55*dp2px;
            float r=w*0.707f;
            int textSize=30*dp2px;
            avatar=Bitmap.createBitmap(w,w, Bitmap.Config.ARGB_8888);
            Canvas canvas=new Canvas(avatar);
            Paint paint=new Paint(Paint.ANTI_ALIAS_FLAG);

            paint.setColor(darkColor? colorsDark[c.charAt(0)%colorsDark.length]:colors[c.charAt(0)%colors.length]);
            canvas.drawRoundRect(0,0,w,w,r,r,paint);
            paint.setColor(Color.WHITE);
            paint.setTextSize(textSize);
            paint.setTextAlign(Paint.Align.CENTER);
            canvas.drawText(String.valueOf(c),w/2f,w/2f+11*dp2px,paint);
            cache.put(c,avatar);
        }
        return avatar;
    }
}
