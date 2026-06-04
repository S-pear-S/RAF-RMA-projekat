package rs.edu.raf.rma.networking.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MovieListItemApiModel(
    val imdbId: String,
    val title: String,
    val year: Int,
    val imdbRating: Double? = null,
    val imdbVotes: Int? = null,
    val posterPath: String? = null,
    val genres: List<GenreApiModel> = emptyList(),
)

@Serializable
data class MovieApiModel(
    val imdbId: String,
    val tmdbId: Int? = null,
    val title: String,
    val originalTitle: String? = null,
    val overview: String? = null,
    val tagline: String? = null,
    val releaseDate: String? = null,
    val year: Int,
    val runtime: Int? = null,
    val budget: Long? = null,
    val revenue: Long? = null,
    val languageCode: String? = null,
    val popularity: Double? = null,
    val imdbRating: Double? = null,
    val imdbVotes: Int? = null,
    val tmdbRating: Double? = null,
    val tmdbVotes: Int? = null,
    val posterPath: String? = null,
    val backdropPath: String? = null,
    val homepage: String? = null,
    val genres: List<GenreApiModel> = emptyList(),
    val collection: CollectionApiModel? = null,
)

@Serializable
data class GenreApiModel(
    val id: Int,
    val name: String,
)

@Serializable
data class CollectionApiModel(
    val id: Int,
    val name: String,
    val posterPath: String? = null,
    val backdropPath: String? = null,
)

@Serializable
data class ActorApiModel(
    val imdbId: String,
    val name: String,
    val professions: List<String> = emptyList(),
    val department: String? = null,
    val profilePath: String? = null,
)

@Serializable
data class AuthRequestBody(
    @SerialName("username") val username: String,
    @SerialName("password") val password: String,
)

@Serializable
data class RegisterRequestBody(
    @SerialName("full_name") val fullName: String,
    @SerialName("username") val username: String,
    @SerialName("password") val password: String,
)

@Serializable
data class AuthResponseApiModel(
    @SerialName("access_token") val accessToken: String,
    @SerialName("expires_in") val expiresIn: Long,
    @SerialName("user") val user: UserApiModel,
)

@Serializable
data class UserApiModel(
    val id: Int,
    val username: String,
    @SerialName("full_name") val fullName: String,
)

@Serializable
data class QuizSubmitBody(
    val score: Double,
    val category: Int = 1,
)

@Serializable
data class QuizResultApiModel(
    val id: String? = null,
    val category: Int = 1,
    val score: Double,
    @SerialName("played_at") val playedAt: Long? = null,
)

@Serializable
data class QuizSubmitResponse(
    val result: QuizResultApiModel,
    val ranking: Int? = null,
)

@Serializable
data class LeaderboardEntryApiModel(
    val rank: Int,
    @SerialName("user_id") val userId: String,
    val username: String,
    @SerialName("full_name") val fullName: String,
    val score: Double,
    @SerialName("played_at") val playedAt: Long,
    @SerialName("total_plays") val totalPlays: Int,
)
