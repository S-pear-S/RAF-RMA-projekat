package rs.edu.raf.rma.posts.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import rs.edu.raf.rma.networking.BeskarApi
import rs.edu.raf.rma.posts.domain.Post
import rs.edu.raf.rma.posts.domain.PostDetails
import rs.edu.raf.rma.posts.domain.PostRepository

class PostRepositoryImpl(
    private val beskarApi: BeskarApi,
) : PostRepository {

    override fun observePosts(mediaType: String): Flow<List<Post>> = flowOf(emptyList())

    override fun observePost(id: Int): Flow<PostDetails?> = flowOf(null)

    override suspend fun refreshPosts() = Unit

    override suspend fun refreshPost(id: Int) = Unit
}
