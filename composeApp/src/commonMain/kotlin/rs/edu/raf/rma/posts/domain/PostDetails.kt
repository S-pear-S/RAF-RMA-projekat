package rs.edu.raf.rma.posts.domain

data class PostDetails(
    val post: Post,
    val description: String?,
    val imageHdUrl: String?,
    val videoUrl: String?,
    val mediaAuthor: String?,
    val descriptionAuthor: String?,
    val copyright: String?,
    val altText: String?,
)
