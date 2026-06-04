package rs.edu.raf.rma.posts.data

import kotlinx.datetime.LocalDate
import rs.edu.raf.rma.networking.model.CategoryApiModel
import rs.edu.raf.rma.networking.model.PostApiModel
import rs.edu.raf.rma.networking.model.PostListItemApiModel
import rs.edu.raf.rma.posts.db.CategoryEntity
import rs.edu.raf.rma.posts.db.PostDetailWithCategories
import rs.edu.raf.rma.posts.db.PostDetailsEntity
import rs.edu.raf.rma.posts.db.PostEntity
import rs.edu.raf.rma.posts.db.PostWithCategories
import rs.edu.raf.rma.posts.domain.Category
import rs.edu.raf.rma.posts.domain.Post
import rs.edu.raf.rma.posts.domain.PostDetails

fun PostListItemApiModel.toPostEntity(): PostEntity = PostEntity(
    id = id,
    title = title,
    mediaType = mediaType,
    imageUrl = imageUrl,
    thumbnailUrl = thumbnailUrl,
    date = LocalDate.parse(date),
    likes = likes,
    dislikes = dislikes,
    commentsCount = commentsCount,
)

fun PostApiModel.toPostEntity(): PostEntity = PostEntity(
    id = id,
    title = title,
    mediaType = mediaType,
    imageUrl = imageUrl,
    thumbnailUrl = thumbnailUrl,
    date = LocalDate.parse(date),
    likes = likes,
    dislikes = dislikes,
    commentsCount = commentsCount,
)

fun PostApiModel.toPostDetailsEntity(): PostDetailsEntity = PostDetailsEntity(
    postId = id,
    description = description,
    imageHdUrl = imageHdUrl,
    videoUrl = videoUrl,
    mediaAuthor = mediaAuthor,
    descriptionAuthor = descriptionAuthor,
    copyright = copyright,
    altText = altText,
)

fun CategoryApiModel.toCategoryEntity(): CategoryEntity = CategoryEntity(
    id = id,
    name = name,
)

fun CategoryEntity.toDomain(): Category = Category(id = id, name = name)

fun PostEntity.toDomain(categories: List<Category>): Post = Post(
    id = id,
    title = title,
    mediaType = mediaType,
    imageUrl = imageUrl,
    thumbnailUrl = thumbnailUrl,
    date = date,
    likes = likes,
    dislikes = dislikes,
    commentsCount = commentsCount,
    categories = categories,
)

fun PostWithCategories.toDomain(): Post =
    post.toDomain(categories = categories.map { it.toDomain() })

fun PostDetailWithCategories.toDomain(): PostDetails {
    val domainPost = post.toDomain(categories = categories.map { it.toDomain() })
    return PostDetails(
        post = domainPost,
        description = details?.description,
        imageHdUrl = details?.imageHdUrl,
        videoUrl = details?.videoUrl,
        mediaAuthor = details?.mediaAuthor,
        descriptionAuthor = details?.descriptionAuthor,
        copyright = details?.copyright,
        altText = details?.altText,
    )
}
