package sch.id.snapan.smarteightadmin.repositories.base

import sch.id.snapan.smarteightadmin.data.entity.Attendance
import sch.id.snapan.smarteightadmin.other.Resource

interface AttendanceRepository {

    suspend fun getListAttendance(): Resource<List<Attendance>>

}