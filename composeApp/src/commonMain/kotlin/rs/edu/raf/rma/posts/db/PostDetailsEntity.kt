package rs.edu.raf.rma.posts.db

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "post_details",
    foreignKeys = [
        ForeignKey(
            entity = PostEntity::class,
            parentColumns = ["id"],
            childColumns = ["postId"],
            onDelete = ForeignKey.CASCADE,
        ),
    ],
)
data class PostDetailsEntity(
    @PrimaryKey val postId: Int,
    val description: String,
    val imageHdUrl: String?,
    val videoUrl: String?,
    val mediaAuthor: String?,
    val descriptionAuthor: String?,
    val copyright: String?,
    val altText: String?,
)
