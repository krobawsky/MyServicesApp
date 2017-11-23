package pe.tecsup.com.myservicesapp.broadcastreceivers;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.RingtoneManager;
import android.os.BatteryManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import java.text.DecimalFormat;

import pe.tecsup.com.myservicesapp.MainActivity;
import pe.tecsup.com.myservicesapp.R;

public class BatteryLevelReceiver extends BroadcastReceiver {

    private static final String TAG = BatteryLevelReceiver.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "Calling onReceive...");

        // https://developer.android.com/training/monitoring-device-state/battery-monitoring.html

        // Cómo determinar el nivel de batería actual
        Intent batteryStatus = new ContextWrapper(context).registerReceiver(null, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));

        int level = batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
        int scale = batteryStatus.getIntExtra(BatteryManager.EXTRA_SCALE, -1);

        float batteryPct = (level / (float) scale) * 100;
        Log.d(TAG, "Nivel de batería: " + batteryPct);

        String message = "";

        if(Intent.ACTION_BATTERY_LOW.equalsIgnoreCase(intent.getAction())) {
            message = "Batería muy bajo: " + new DecimalFormat("###,###,##0.0'%'").format(batteryPct);
        }else if(Intent.ACTION_POWER_CONNECTED.equalsIgnoreCase(intent.getAction())){
            message = "Cargador Conectado: " + new DecimalFormat("###,###,##0.0'%'").format(batteryPct);
        }else if(Intent.ACTION_POWER_DISCONNECTED.equalsIgnoreCase(intent.getAction())){
            message = "Cargador Desconectado: " + new DecimalFormat("###,###,##0.0'%'").format(batteryPct);
        }

        // Notification Builder
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);

        // Intent
        intent =  new Intent(context, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_ONE_SHOT);

        // Notification
        Notification notification = builder
                .setContentTitle("Nivel de batería")
                .setContentText(message)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setColor(ContextCompat.getColor(context, R.color.colorPrimary))
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .build();

        // Notification manager
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(1, notification);

        // Play sound
        RingtoneManager.getRingtone(context, RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)).play();
    }
}

