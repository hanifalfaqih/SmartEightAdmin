package sch.id.snapan.smarteightadmin.ui.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.AuthResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import sch.id.snapan.smarteightadmin.other.Event
import sch.id.snapan.smarteightadmin.other.Resource
import sch.id.snapan.smarteightadmin.repositories.base.AuthRepository
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val dispatcher: CoroutineDispatcher = Dispatchers.Main
): ViewModel() {

    private val _loginStatusUser = MutableLiveData<Event<Resource<AuthResult>>>()
    val loginStatusUser: LiveData<Event<Resource<AuthResult>>> = _loginStatusUser

    fun loginUser(email: String, password: String) {
        _loginStatusUser.postValue(Event(Resource.Loading()))
        viewModelScope.launch(dispatcher) {
            val result = authRepository.loginUser(email, password)
            _loginStatusUser.postValue(Event(result))
        }
    }
}