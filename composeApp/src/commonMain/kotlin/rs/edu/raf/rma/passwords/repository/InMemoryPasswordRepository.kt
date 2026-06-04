package rs.edu.raf.rma.passwords.repository

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import rs.edu.raf.rma.passwords.domain.Password
import rs.edu.raf.rma.passwords.domain.PasswordRepository
import kotlin.time.Duration.Companion.seconds

class InMemoryPasswordRepository : PasswordRepository {

    private val _passwords = MutableStateFlow(seedData())

    override fun observePasswords(): Flow<List<Password>> = _passwords.asStateFlow()

    override suspend  fun getById(id: Long): Password? {
        return _passwords.value.find { it.id == id }
    }

    override suspend  fun add(name: String, url: String, password: String): Password {
        val new = Password(
            id = (_passwords.value.maxOfOrNull { it.id } ?: 0) + 1,
            name = name,
            url = url,
            password = password,
        )
        _passwords.update { it + new }
        return new
    }

    override suspend  fun update(id: Long, name: String, url: String, password: String) {
        _passwords.update { list ->
            list.map {
                if (it.id == id) it.copy(name = name, url = url, password = password) else it
            }
        }
    }

    override suspend fun delete(id: Long) {
        _passwords.update { list -> list.filter { it.id != id } }
    }

    override suspend fun delete2(id: Long) {
        delay(2.seconds)
    }

    override suspend fun delete3(id: Long) {
        delay(1.seconds)
    }

    override suspend fun delete4(id: Long) {
        delay(1.seconds)
    }
}

private fun seedData(): List<Password> = listOf(
    Password(id = 1, name = "Google", url = "https://google.com", password = "g00gl3"),
    Password(id = 2, name = "GitHub", url = "https://github.com", password = "g1thub"),
    Password(id = 3, name = "Twitter (X)", url = "https://x.com", password = "tw1tt3r"),
)
