package rs.edu.raf.rma.passwords.domain

import kotlinx.serialization.Serializable

@Serializable
data class Password(
    val id: Long,
//    @SerialName("_id") val id2: Long? = null,
    val name: String,
    val url: String,
    val password: String,
)
