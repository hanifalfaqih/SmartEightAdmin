package sch.id.snapan.smarteightadmin.ui.announcement

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import sch.id.snapan.smarteight.data.notification.NotificationData
import sch.id.snapan.smarteight.data.notification.PushNotification
import sch.id.snapan.smarteightadmin.data.entity.Announcement
import sch.id.snapan.smarteightadmin.other.Event
import sch.id.snapan.smarteightadmin.other.Resource
import sch.id.snapan.smarteightadmin.repositories.base.AnnouncementRepository
import sch.id.snapan.smarteightadmin.utils.Constants.Companion.TOPIC
import javax.inject.Inject

@HiltViewModel
class AnnouncementViewModel @Inject constructor(
    private val announcementRepository: AnnouncementRepository,
    private val dispatcher: CoroutineDispatcher = Dispatchers.Main
): ViewModel() {

    private val _getListAnnouncementStatus = MutableLiveData<Event<Resource<List<Announcement>>>>()
    val getListAnnouncementStatus: LiveData<Event<Resource<List<Announcement>>>> = _getListAnnouncementStatus

    private val _getDetailAnnouncementStatus = MutableLiveData<Event<Resource<Announcement?>>>()
    val getDetailAnnouncementStatus: LiveData<Event<Resource<Announcement?>>> = _getDetailAnnouncementStatus

    private val _deleteAnnouncementStatus = MutableLiveData<Event<Resource<Any>>>()
    val deleteAnnouncementStatus: LiveData<Event<Resource<Any>>> = _deleteAnnouncementStatus

    private val _editAnnouncementStatus = MutableLiveData<Event<Resource<Any>>>()
    val editAnnouncementStatus: LiveData<Event<Resource<Any>>> = _editAnnouncementStatus

    private val _addAnnouncementStatus = MutableLiveData<Event<Resource<Any>>>()
    val addAnnouncementStatus: LiveData<Event<Resource<Any>>> = _addAnnouncementStatus

    fun getListAnnouncement() {
        _getListAnnouncementStatus.postValue(Event(Resource.Loading()))
        viewModelScope.launch(dispatcher) {
            val result = announcementRepository.getListAnnouncement()
            _getListAnnouncementStatus.postValue(Event(result))
        }
    }

    fun getDetailAnnouncement(announcementId: String) {
        _getDetailAnnouncementStatus.postValue(Event(Resource.Loading()))
        viewModelScope.launch(dispatcher) {
            val result = announcementRepository.getDetailAnnouncement(announcementId)
            _getDetailAnnouncementStatus.postValue(Event(result))
        }
    }

    fun deleteAnnouncement(announcement: Announcement) {
        viewModelScope.launch(dispatcher) {
            val result = announcementRepository.deleteAnnouncement(announcement)
            _deleteAnnouncementStatus.postValue(Event(result))
        }
    }

    fun editAnnouncement(oldAnnouncement: Announcement, newAnnouncement: Map<String, Any>) {
        _editAnnouncementStatus.postValue(Event(Resource.Loading()))
        viewModelScope.launch(dispatcher) {
            val result = announcementRepository.editAnnouncement(oldAnnouncement, newAnnouncement)
            _editAnnouncementStatus.postValue(Event(result))
        }
    }

    fun addAnnouncement(imageUri: Uri?, title: String, message: String) {
        _addAnnouncementStatus.postValue(Event(Resource.Loading()))
        viewModelScope.launch(dispatcher) {
            val result = announcementRepository.addAnnouncement(imageUri, title, message)
            PushNotification(NotificationData(message = title), TOPIC).also {
                announcementRepository.sendNotification(it)
            }
            _addAnnouncementStatus.postValue(Event(result))
        }
    }
}