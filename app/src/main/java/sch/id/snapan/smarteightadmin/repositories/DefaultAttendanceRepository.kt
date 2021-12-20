package sch.id.snapan.smarteightadmin.repositories

import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import sch.id.snapan.smarteightadmin.data.entity.Attendance
import sch.id.snapan.smarteightadmin.other.Resource
import sch.id.snapan.smarteightadmin.other.safeCallNetwork
import sch.id.snapan.smarteightadmin.repositories.base.AttendanceRepository

class DefaultAttendanceRepository: AttendanceRepository {

    private val attendances = Firebase.firestore.collection("Attendances")

    override suspend fun getListAttendance(): Resource<List<Attendance>> {
        return withContext(Dispatchers.IO) {
            safeCallNetwork {
                val allAttendances = attendances.orderBy("date", Query.Direction.DESCENDING)
                    .get()
                    .await()
                    .toObjects(Attendance::class.java)
                Resource.Success(allAttendances)
            }
        }
    }

}