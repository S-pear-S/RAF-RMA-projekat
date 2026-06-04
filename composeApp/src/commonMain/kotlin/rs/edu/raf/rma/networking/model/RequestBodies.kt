package rs.edu.raf.rma.networking.model

import kotlinx.serialization.Serializable

@Serializable
data class NicknameBody(
    val nickname: String,
)

@Serializable
data class CommentBody(
    val nickname: String,
    val text: String,
)

@Serializable
data class CreateCollectionBody(
    val nickname: String,
    val name: String,
    val postIds: List<Int>,
)

@Serializable
data class UpdateCollectionBody(
    val name: String,
    val postIds: List<Int>,
)
