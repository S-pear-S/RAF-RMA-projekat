package rs.edu.raf.rma.networking

import de.jensklingenberg.ktorfit.http.Body
import de.jensklingenberg.ktorfit.http.DELETE
import de.jensklingenberg.ktorfit.http.GET
import de.jensklingenberg.ktorfit.http.Header
import de.jensklingenberg.ktorfit.http.Headers
import de.jensklingenberg.ktorfit.http.POST
import de.jensklingenberg.ktorfit.http.PUT
import de.jensklingenberg.ktorfit.http.Path
import de.jensklingenberg.ktorfit.http.Query
import rs.edu.raf.rma.networking.model.BeskarStatsApiModel
import rs.edu.raf.rma.networking.model.CategoryWithCountApiModel
import rs.edu.raf.rma.networking.model.CommentApiModel
import rs.edu.raf.rma.networking.model.CommentBody
import rs.edu.raf.rma.networking.model.CreateCollectionBody
import rs.edu.raf.rma.networking.model.NicknameBody
import rs.edu.raf.rma.networking.model.PaginatedResponse
import rs.edu.raf.rma.networking.model.PostApiModel
import rs.edu.raf.rma.networking.model.PostCollectionApiModel
import rs.edu.raf.rma.networking.model.PostListItemApiModel
import rs.edu.raf.rma.networking.model.UpdateCollectionBody

interface BeskarApi {

    // Posts
    @GET("posts")
    suspend fun getPosts(
        @Query("page") page: Int = 1,
        @Query("page_size") pageSize: Int = 20,
        @Query("query") query: String? = null,
        @Query("media_type") mediaType: String? = null,
        @Query("category_id") categoryId: Int? = null,
        @Query("start_date") startDate: String? = null,
        @Query("end_date") endDate: String? = null,
        @Query("sort_by") sortBy: String? = null,
        @Query("sort_order") sortOrder: String? = null,
    ): PaginatedResponse<PostListItemApiModel>

    @GET("posts/{id}")
    suspend fun getPost(
        @Path("id") id: Int,
//        @Header(value = "accessToken") accessToken: String,
    ): PostApiModel

    @GET("posts/{id}/related")
    suspend fun getRelatedPosts(@Path("id") id: Int): List<PostListItemApiModel>

    @GET("posts/random")
    suspend fun getRandomPost(): PostApiModel

    @GET("posts/apod")
    suspend fun getPostByApodDate(@Query("date") date: String): PostApiModel

    @GET("posts/on-this-day")
    suspend fun getPostsOnThisDay(
        @Query("month") month: Int,
        @Query("day") day: Int,
    ): List<PostListItemApiModel>

    @GET("posts/popular")
    suspend fun getPopularPosts(
        @Query("sort_by") sortBy: String = "likes",
        @Query("page") page: Int = 1,
        @Query("page_size") pageSize: Int = 20,
    ): PaginatedResponse<PostListItemApiModel>

    // Reactions
    @POST("posts/{id}/like")
    suspend fun likePost(@Path("id") id: Int, @Body body: NicknameBody)

    @DELETE("posts/{id}/like")
    suspend fun removeLike(@Path("id") id: Int, @Body body: NicknameBody)

    @POST("posts/{id}/dislike")
    suspend fun dislikePost(@Path("id") id: Int, @Body body: NicknameBody)

    @DELETE("posts/{id}/dislike")
    suspend fun removeDislike(@Path("id") id: Int, @Body body: NicknameBody)

    // Comments
    @GET("posts/{id}/comments")
    suspend fun getComments(
        @Path("id") postId: Int,
        @Query("page") page: Int = 1,
        @Query("page_size") pageSize: Int = 20,
    ): PaginatedResponse<CommentApiModel>

    @POST("posts/{id}/comments")
    suspend fun addComment(@Path("id") postId: Int, @Body body: CommentBody): CommentApiModel

    // Collections
    @POST("collections")
    suspend fun createCollection(@Body body: CreateCollectionBody): PostCollectionApiModel

    @GET("collections/{id}")
    suspend fun getCollection(@Path("id") id: Int): PostCollectionApiModel

    @PUT("collections/{id}")
    suspend fun updateCollection(
        @Path("id") id: Int,
        @Body body: UpdateCollectionBody,
    ): PostCollectionApiModel

    // Categories
    @GET("categories")
    suspend fun getCategories(
        @Query("page") page: Int = 1,
        @Query("page_size") pageSize: Int = 20,
    ): PaginatedResponse<CategoryWithCountApiModel>

    @GET("categories/popular")
    suspend fun getPopularCategories(
        @Query("limit") limit: Int = 10,
    ): List<CategoryWithCountApiModel>

    // Stats
    @GET("stats")
    suspend fun getStats(): BeskarStatsApiModel
}
