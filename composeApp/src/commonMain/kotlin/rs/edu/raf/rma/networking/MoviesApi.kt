package rs.edu.raf.rma.networking

import de.jensklingenberg.ktorfit.http.Body
import de.jensklingenberg.ktorfit.http.DELETE
import de.jensklingenberg.ktorfit.http.GET
import de.jensklingenberg.ktorfit.http.POST
import de.jensklingenberg.ktorfit.http.Path
import de.jensklingenberg.ktorfit.http.Query
import rs.edu.raf.rma.networking.model.*

interface MoviesApi {

    // --- Auth ---

    @POST("auth/signup")
    suspend fun register(@Body body: RegisterRequestBody): AuthResponseApiModel

    @POST("auth/login")
    suspend fun login(@Body body: AuthRequestBody): AuthResponseApiModel

    // --- Movies ---

    @GET("movies")
    suspend fun getMovies(
        @Query("page") page: Int = 1,
        @Query("page_size") pageSize: Int = 20,
        @Query("query") query: String? = null,
        @Query("genre_id") genreId: Int? = null,
        @Query("min_year") minYear: Int? = null,
        @Query("max_year") maxYear: Int? = null,
        @Query("min_rating") minRating: Double? = null,
        @Query("sort_by") sortBy: String? = null,
        @Query("sort_order") sortOrder: String? = null,
    ): PaginatedResponse<MovieListItemApiModel>

    @GET("movies/{id}")
    suspend fun getMovie(@Path("id") id: String): MovieApiModel

    @GET("movies/{id}/cast")
    suspend fun getMovieCast(
        @Path("id") id: String,
        @Query("page") page: Int = 1,
        @Query("page_size") pageSize: Int = 20,
    ): PaginatedResponse<ActorApiModel>

    // --- Genres ---

    @GET("genres")
    suspend fun getGenres(): List<GenreApiModel>

    // --- Profile ---

    @GET("me")
    suspend fun getProfile(): UserApiModel

    // --- Favorites ---

    @GET("me/favorites")
    suspend fun getFavorites(): List<MovieListItemApiModel>

    @POST("me/favorites/{movie_id}")
    suspend fun addFavorite(@Path("movie_id") movieId: String)

    @DELETE("me/favorites/{movie_id}")
    suspend fun removeFavorite(@Path("movie_id") movieId: String)

    // --- Watchlist ---

    @GET("me/watchlist")
    suspend fun getWatchlist(): List<MovieListItemApiModel>

    @POST("me/watchlist/{movie_id}")
    suspend fun addToWatchlist(@Path("movie_id") movieId: String)

    @DELETE("me/watchlist/{movie_id}")
    suspend fun removeFromWatchlist(@Path("movie_id") movieId: String)

    // --- Quiz ---

    @POST("leaderboard")
    suspend fun submitQuizResult(@Body body: QuizSubmitBody): QuizSubmitResponse

    @GET("me/quiz-results")
    suspend fun getMyQuizResults(
        @Query("page") page: Int = 1,
        @Query("page_size") pageSize: Int = 20,
    ): PaginatedResponse<QuizResultApiModel>

    @GET("leaderboard")
    suspend fun getLeaderboard(
        @Query("category") category: Int = 1,
        @Query("page") page: Int = 1,
        @Query("page_size") pageSize: Int = 20,
    ): PaginatedResponse<LeaderboardEntryApiModel>
}
