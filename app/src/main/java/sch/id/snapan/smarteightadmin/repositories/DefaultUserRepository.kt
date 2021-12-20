package sch.id.snapan.smarteightadmin.repositories

import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import sch.id.snapan.smarteightadmin.data.entity.User
import sch.id.snapan.smarteightadmin.other.Resource
import sch.id.snapan.smarteightadmin.other.safeCallNetwork
import sch.id.snapan.smarteightadmin.repositories.base.UserRepository

class DefaultUserRepository: UserRepository {

    private val users = Firebase.firestore.collection("Users")

    override suspend fun getListUser(): Resource<List<User>> {
        return withContext(Dispatchers.IO) {
            safeCallNetwork {
                val allUsers = users.get().await().toObjects(User::class.java)
                Resource.Success(allUsers)
            }
        }
    }
}