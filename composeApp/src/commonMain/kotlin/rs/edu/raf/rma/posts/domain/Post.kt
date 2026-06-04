package rs.edu.raf.rma.posts.domain

import kotlinx.datetime.LocalDate

data class Post(
    val id: Int,
    val title: String,
    val mediaType: String,
    val imageUrl: String?,
    val thumbnailUrl: String?,
    val date: LocalDate,
    val likes: Int,
    val dislikes: Int,
    val commentsCount: Int,
    val categories: List<Category>,
)
