package rs.edu.raf.rma.posts.domain

import kotlinx.coroutines.flow.Flow

interface PostRepository {
    fun observePosts(mediaType: String = "image"): Flow<List<Post>>
    fun observePost(id: Int): Flow<PostDetails?>
    suspend fun refreshPosts()
    suspend fun refreshPost(id: Int)
}
