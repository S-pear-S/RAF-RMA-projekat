package rs.edu.raf.rma.posts.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface PostDao {

    @Transaction
    @Query("SELECT * FROM posts WHERE mediaType = :mediaType ORDER BY date DESC")
    fun observePostsByMediaType(mediaType: String): Flow<List<PostWithCategories>>

    @Transaction
    @Query("SELECT * FROM posts WHERE id = :id")
    fun observePostDetail(id: Int): Flow<PostDetailWithCategories?>

    @Upsert
    suspend fun upsertPosts(posts: List<PostEntity>)

    @Upsert
    suspend fun upsertPostDetails(details: PostDetailsEntity)

    @Upsert
    suspend fun upsertCategories(categories: List<CategoryEntity>)

    @Query("DELETE FROM post_categories WHERE postId = :postId")
    suspend fun deleteCategoryLinksForPost(postId: Int)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertCategoryLinks(links: List<PostCategoryCrossRef>)

    @Transaction
    suspend fun replacePostCategoryLinks(postId: Int, categoryIds: List<Int>) {
        deleteCategoryLinksForPost(postId)
        insertCategoryLinks(categoryIds.map { PostCategoryCrossRef(postId, it) })
    }

    @Transaction
    suspend fun refreshListTransaction(posts: List<PostEntity>) {
        upsertPosts(posts)
    }

    @Transaction
    suspend fun refreshDetailTransaction(
        post: PostEntity,
        details: PostDetailsEntity,
        categories: List<CategoryEntity>,
        categoryIds: List<Int>,
    ) {
        upsertPosts(listOf(post))
        upsertPostDetails(details)
        upsertCategories(categories)
        replacePostCategoryLinks(post.id, categoryIds)
    }
}
