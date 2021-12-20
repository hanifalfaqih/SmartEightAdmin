package sch.id.snapan.smarteightadmin.repositories

import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import sch.id.snapan.smarteightadmin.other.Resource
import sch.id.snapan.smarteightadmin.other.safeCallNetwork
import sch.id.snapan.smarteightadmin.repositories.base.AuthRepository

class DefaultAuthRepository : AuthRepository {

    private val auth = Firebase.auth

    override suspend fun loginUser(email: String, password: String): Resource<AuthResult> {
        return withContext(Dispatchers.IO) {
            safeCallNetwork {
                val result = auth.signInWithEmailAndPassword(email, password).await()
                Resource.Success(result)
            }
        }
    }

}