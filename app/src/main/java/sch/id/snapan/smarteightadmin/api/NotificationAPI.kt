package sch.id.snapan.smarteight.api

import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST
import sch.id.snapan.smarteight.data.notification.PushNotification
import sch.id.snapan.smarteightadmin.utils.Constants.Companion.CONTENT_TYPE
import sch.id.snapan.smarteightadmin.utils.Constants.Companion.SERVER_KEY

interface NotificationAPI {
    @Headers("Authorization: key=$SERVER_KEY", "Content-Type:$CONTENT_TYPE")
    @POST("fcm/send")
    suspend fun postNotification(
        @Body notificaton: PushNotification
    ): Response<ResponseBody>

}