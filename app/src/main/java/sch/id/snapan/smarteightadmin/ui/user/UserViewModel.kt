package sch.id.snapan.smarteightadmin.ui.user

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import sch.id.snapan.smarteightadmin.data.entity.User
import sch.id.snapan.smarteightadmin.other.Event
import sch.id.snapan.smarteightadmin.other.Resource
import sch.id.snapan.smarteightadmin.repositories.DefaultUserRepository
import sch.id.snapan.smarteightadmin.repositories.base.UserRepository
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val dispatcher: CoroutineDispatcher = Dispatchers.Main
): ViewModel() {

    private val _getListUserStatus = MutableLiveData<Event<Resource<List<User>>>>()
    val getListUserStatus: LiveData<Event<Resource<List<User>>>> = _getListUserStatus

    fun getListAnnouncement() {
        _getListUserStatus.postValue(Event(Resource.Loading()))
        viewModelScope.launch(dispatcher) {
            val result = userRepository.getListUser()
            _getListUserStatus.postValue(Event(result))
        }
    }

}