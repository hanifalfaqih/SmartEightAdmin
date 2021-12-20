package sch.id.snapan.smarteightadmin.repositories.base

import sch.id.snapan.smarteightadmin.data.entity.Attendance
import sch.id.snapan.smarteightadmin.data.entity.User
import sch.id.snapan.smarteightadmin.other.Resource

interface UserRepository {

    suspend fun getListUser(): Resource<List<User>>
}