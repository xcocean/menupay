package com.njz.menupay.helper;

import android.content.Context;
import android.view.Gravity;
import android.widget.Toast;

public final class MessageHelper {
    private static MessageHelper getInstance;

    //构造方法设置为私有的
    private MessageHelper() {
    }

    //对外开放
    public static MessageHelper getInstance() {
        if (getInstance == null) {
            //synchronized 对线程枷锁
            synchronized (MessageHelper.class) {
                if (getInstance == null)//如果还为空，实例化
                    getInstance = new MessageHelper();
            }
        }
        return getInstance;
    }

    /**
     * 默认下方弹出
     */
    public void toast(Context context, String content){
        Toast.makeText(context,content,Toast.LENGTH_SHORT).show();//短时间
    }

    /**
     * 居中弹出
     */
    public void toastCenter(Context context, String content){
        Toast toast = Toast.makeText(context, content, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }


}
