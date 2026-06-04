package rs.edu.raf.rma.posts.db

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation

data class PostWithCategories(
    @Embedded val post: PostEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "id",
        associateBy = Junction(
            value = PostCategoryCrossRef::class,
            parentColumn = "postId",
            entityColumn = "categoryId",
        ),
    )
    val categories: List<CategoryEntity>,
)
