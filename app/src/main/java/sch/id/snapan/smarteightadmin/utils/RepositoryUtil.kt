package sch.id.snapan.smarteightadmin.other

inline fun <T> safeCallNetwork(action: () -> Resource<T>): Resource<T> {
    return try {
        action()
    } catch (e: Exception) {
        Resource.Error(e.message ?: "Terjadi masalah tak diketahui")
    }
}