package rs.edu.raf.rma.posts.db

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.datetime.LocalDate

@Entity(tableName = "posts")
data class PostEntity(
    @PrimaryKey val id: Int,
    val title: String,
    val mediaType: String,
    val imageUrl: String?,
    val thumbnailUrl: String?,
    val date: LocalDate,
    val likes: Int,
    val dislikes: Int,
    val commentsCount: Int,
)
