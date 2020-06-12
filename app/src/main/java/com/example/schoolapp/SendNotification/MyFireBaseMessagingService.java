package com.example.schoolapp.SendNotification;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.example.schoolapp.R;
import com.example.schoolapp.Views.Activities.FindFriendsActivity;
import com.example.schoolapp.Views.Fragments.RequestFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFireBaseMessagingService extends FirebaseMessagingService {


    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            sendOreoNotification(remoteMessage);

        else
            sendNotification(remoteMessage);


    }


    private void sendOreoNotification(RemoteMessage remoteMessage) {
        String Title = remoteMessage.getData().get("Title");
        String Message = remoteMessage.getData().get("Message");


        oreoNotification oreoNotification = new oreoNotification(getApplicationContext());

        Notification.Builder builder = oreoNotification.getOreoNotification(Title, Message);


        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(0, builder.build());

    }

    private void sendNotification(RemoteMessage remoteMessage) {
        String Title = remoteMessage.getData().get("Title");
        String Message = remoteMessage.getData().get("Message");


        NotificationCompat.Builder builder
                = new NotificationCompat.Builder(getApplicationContext())
                .setSmallIcon(R.drawable.ic_aws_notification)
                .setContentTitle(Title)
                .setContentText(Message);
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(0, builder.build());

    }
}


