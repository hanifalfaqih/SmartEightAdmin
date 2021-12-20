package sch.id.snapan.smarteightadmin.repositories

import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import sch.id.snapan.smarteight.api.RetrofitInstance
import sch.id.snapan.smarteight.data.notification.PushNotification
import sch.id.snapan.smarteight.utils.DateHelper
import sch.id.snapan.smarteightadmin.data.entity.Announcement
import sch.id.snapan.smarteightadmin.other.Resource
import sch.id.snapan.smarteightadmin.other.safeCallNetwork
import sch.id.snapan.smarteightadmin.repositories.base.AnnouncementRepository
import java.util.*

class DefaultAnnouncementRepository: AnnouncementRepository {

    private val authUid = Firebase.auth.uid!!
    private val pictures = Firebase.storage.getReference("Pictures")
    private val announcements = Firebase.firestore.collection("Announcements")

    override suspend fun getListAnnouncement(): Resource<List<Announcement>> {
        return withContext(Dispatchers.IO) {
            safeCallNetwork {
                val allAnnouncements = announcements.orderBy("date", Query.Direction.DESCENDING)
                    .get()
                    .await()
                    .toObjects(Announcement::class.java)
                Resource.Success(allAnnouncements)
            }
        }
    }

    override suspend fun getDetailAnnouncement(announcementId: String): Resource<Announcement?> {
        return withContext(Dispatchers.IO) {
            safeCallNetwork {
                val announcement = announcements.document(announcementId).get().await().toObject(Announcement::class.java)
                Resource.Success(announcement)
            }
        }
    }

    override suspend fun deleteAnnouncement(announcement: Announcement): Resource<Any> {
        return withContext(Dispatchers.IO) {
            safeCallNetwork {
                announcements.document(announcement.id).delete().await()
                Resource.Success(announcement)
            }
        }
    }

    override suspend fun editAnnouncement(
        oldAnnouncement: Announcement,
        newAnnouncement: Map<String, Any>
    ): Resource<Any> {
        return withContext(Dispatchers.IO) {
            safeCallNetwork {
                val announcementQuery = announcements
                    .whereEqualTo("id", oldAnnouncement.id)
                    .get()
                    .await()
                if (announcementQuery.documents.isNotEmpty()) {
                    for (document in announcementQuery) {
                        try {
                            announcements.document(oldAnnouncement.id).set(
                                newAnnouncement,
                                SetOptions.merge()
                            ).await()
                        } catch (e: Exception) {
                            Resource.Error(e.toString(), null)
                        }
                    }
                }
                Resource.Success(Any())
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun addAnnouncement(
        imageUri: Uri?,
        title: String,
        message: String
    ): Resource<Any> {
        return withContext(Dispatchers.IO) {
            safeCallNetwork {
                val announcementId = UUID.randomUUID().toString()
                val uid = authUid
                val imageUrl: String?

                val imageUploadResult = imageUri?.let { pictures.child(authUid).putFile(it).await() }
                if (imageUri != null) {
                    imageUrl = imageUploadResult?.metadata?.reference?.downloadUrl?.await().toString()
                } else {
                    imageUrl = null
                }

                val addAnnouncement = Announcement(
                    uid = uid,
                    id = announcementId,
                    imageUrl = imageUrl,
                    title = title,
                    message = message,
                    date = DateHelper.getCurrentDate()
                )
                announcements.document(announcementId).set(addAnnouncement).await()
                Resource.Success(Any())
            }
        }
    }

    override suspend fun sendNotification(notification: PushNotification) {
        return withContext(Dispatchers.IO) {
            safeCallNetwork {
                val response = RetrofitInstance.api.postNotification(notification)
                if (response.isSuccessful) {
                    Log.d("SMART EIGHT", "Response: Berhasil mengirim notifikasi")
                } else {
                    Log.e("SMART EIGHT", "Response: Gagal mengirim notifikasi")
                }
                Resource.Success(response)
            }
        }
    }
}