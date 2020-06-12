package com.example.schoolapp.SendNotification;

import com.example.schoolapp.Models.Entities.Contact;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APIService {

    @Headers(
            {
                    "Content-Type:application/json",
                    "Authorization:key=AAAA_Nc2E5Q:APA91bGCrPVmFYN0OXMSMNaLZy0GXBybEv3sxGSQPjYGKBFUrejx-vHJGqF9kvq3Od2mH5jkLbwuz7J0qlKe7vZx8ZSL8RRWgNejqRsBzj-iLCLKa85DPp-rYjJIOIvq2Bx2iXnD6xC2"
            }

    )

    @POST("fcm/send")
    Call<MyResponse> sendNotification(@Body NotificationSender body);
}
