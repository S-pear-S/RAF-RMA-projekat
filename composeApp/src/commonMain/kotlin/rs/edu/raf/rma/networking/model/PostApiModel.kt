package rs.edu.raf.rma.networking.model

import kotlinx.serialization.Serializable

@Serializable
data class PostApiModel(
    val id: Int,
    val title: String,
    val description: String,
    val mediaType: String,
    val imageUrl: String? = null,
    val imageHdUrl: String? = null,
    val videoUrl: String? = null,
    val thumbnailUrl: String? = null,
    val mediaAuthor: String? = null,
    val descriptionAuthor: String? = null,
    val copyright: String? = null,
    val altText: String? = null,
    val date: String,
    val likes: Int = 0,
    val dislikes: Int = 0,
    val commentsCount: Int = 0,
    val categories: List<CategoryApiModel> = emptyList(),
    val references: List<String> = emptyList(),
)

@Serializable
data class PostListItemApiModel(
    val id: Int,
    val title: String,
    val mediaType: String,
    val imageUrl: String? = null,
    val thumbnailUrl: String? = null,
    val date: String,
    val likes: Int = 0,
    val dislikes: Int = 0,
    val commentsCount: Int = 0,
)

@Serializable
data class CategoryApiModel(
    val id: Int,
    val name: String,
)

@Serializable
data class CategoryWithCountApiModel(
    val id: Int,
    val name: String,
    val postCount: Int,
)

@Serializable
data class CommentApiModel(
    val id: Int,
    val postId: Int,
    val nickname: String,
    val text: String,
    val createdAt: String,
)

@Serializable
data class BeskarStatsApiModel(
    val totalPosts: Int,
    val totalImages: Int,
    val totalVideos: Int,
    val totalLikes: Int,
    val totalComments: Int,
    val dateRange: DateRangeApiModel,
    val totalCategories: Int,
)

@Serializable
data class DateRangeApiModel(
    val from: String,
    val to: String,
)

@Serializable
data class PostCollectionApiModel(
    val id: Int,
    val name: String,
    val nickname: String,
    val createdAt: String,
    val posts: List<PostApiModel> = emptyList(),
)
