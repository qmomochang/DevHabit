package com.habit.devhabit;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by qmo-i7 on 2016/3/29.
 */
public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO Auto-generated method stub
        System.out.println("收到广播");
        Intent it = new Intent(context, MainActivity.class);
        it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(it);
        //收到广播后启动Activity,简单起见，直接就跳到了设置alarm的Activity
        //intent必须加上Intent.FLAG_ACTIVITY_NEW_TASK flag
    }
}

