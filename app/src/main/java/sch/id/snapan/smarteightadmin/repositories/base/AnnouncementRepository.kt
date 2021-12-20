package sch.id.snapan.smarteightadmin.repositories.base

import android.net.Uri
import sch.id.snapan.smarteight.data.notification.PushNotification
import sch.id.snapan.smarteightadmin.data.entity.Announcement
import sch.id.snapan.smarteightadmin.other.Resource

interface AnnouncementRepository {

    suspend fun getListAnnouncement(): Resource<List<Announcement>>

    suspend fun getDetailAnnouncement(announcementId: String): Resource<Announcement?>

    suspend fun deleteAnnouncement(announcement: Announcement): Resource<Any>

    suspend fun editAnnouncement(oldAnnouncement: Announcement, newAnnouncement: Map<String, Any>): Resource<Any>

    suspend fun addAnnouncement(imageUri: Uri?, title: String, message: String): Resource<Any>

    suspend fun sendNotification(notification: PushNotification)

}