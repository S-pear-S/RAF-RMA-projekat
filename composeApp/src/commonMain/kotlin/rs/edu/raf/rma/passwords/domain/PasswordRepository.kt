package rs.edu.raf.rma.passwords.domain

import kotlinx.coroutines.flow.Flow

interface PasswordRepository {
    fun observePasswords(): Flow<List<Password>>
    suspend fun getById(id: Long): Password?
    suspend fun add(name: String, url: String, password: String): Password
    suspend fun update(id: Long, name: String, url: String, password: String)
    suspend fun delete(id: Long)
    suspend fun delete2(id: Long)
    suspend fun delete3(id: Long)
    suspend fun delete4(id: Long)
}
