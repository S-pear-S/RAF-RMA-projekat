package rs.edu.raf.rma.posts.db

import androidx.room.Entity
import androidx.room.Index

@Entity(
    tableName = "post_categories",
    primaryKeys = ["postId", "categoryId"],
    indices = [Index("categoryId")],
)
data class PostCategoryCrossRef(
    val postId: Int,
    val categoryId: Int,
)
