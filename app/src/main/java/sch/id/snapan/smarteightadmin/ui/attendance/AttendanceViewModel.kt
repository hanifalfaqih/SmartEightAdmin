package sch.id.snapan.smarteightadmin.ui.attendance

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import sch.id.snapan.smarteightadmin.data.entity.Attendance
import sch.id.snapan.smarteightadmin.other.Event
import sch.id.snapan.smarteightadmin.other.Resource
import sch.id.snapan.smarteightadmin.repositories.base.AttendanceRepository
import javax.inject.Inject

@HiltViewModel
class AttendanceViewModel @Inject constructor(
    private val attendanceRepository: AttendanceRepository,
    private val dispatcher: CoroutineDispatcher = Dispatchers.Main
): ViewModel() {

    private val _getListAttendanceStatus = MutableLiveData<Event<Resource<List<Attendance>>>>()
    val getListAttendanceStatus: LiveData<Event<Resource<List<Attendance>>>> = _getListAttendanceStatus

    fun getListAnnouncement() {
        _getListAttendanceStatus.postValue(Event(Resource.Loading()))
        viewModelScope.launch(dispatcher) {
            val result = attendanceRepository.getListAttendance()
            _getListAttendanceStatus.postValue(Event(result))
        }
    }

}