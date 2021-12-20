package sch.id.snapan.smarteightadmin.repositories.base

import com.google.firebase.auth.AuthResult
import sch.id.snapan.smarteightadmin.other.Resource

interface AuthRepository {

    suspend fun loginUser(email: String, password: String): Resource<AuthResult>

}