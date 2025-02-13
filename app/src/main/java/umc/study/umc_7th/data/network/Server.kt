package umc.study.umc_7th.data.network

import android.graphics.BitmapFactory
import android.util.Log
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.HttpException
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import umc.study.umc_7th.Album
import umc.study.umc_7th.AlbumContent
import umc.study.umc_7th.BuildConfig
import umc.study.umc_7th.LocalDateGsonAdapter
import umc.study.umc_7th.LocalDateSerializer
import umc.study.umc_7th.LocalDateTimeGsonAdapter
import umc.study.umc_7th.LocalDateTimeSerializer
import umc.study.umc_7th.MusicContent
import umc.study.umc_7th.PodcastContent
import umc.study.umc_7th.User
import umc.study.umc_7th.VideoContent
import java.time.LocalDate
import java.time.LocalDateTime
import javax.inject.Inject

private val retrofitInstance: ServerEndpoint by lazy {
    val loggingInterceptor = HttpLoggingInterceptor { message ->
        val threadName = Thread.currentThread().id
        Log.d("HttpLogger", "[$threadName] $message")
    }.apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    val client = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .build()

    val retrofit =
        Retrofit.Builder().baseUrl(BuildConfig.SERVER_URL).client(client).addConverterFactory(
            GsonConverterFactory.create(
                GsonBuilder().setLenient()
                    .registerTypeAdapter(
                        LocalDateTime::class.java,
                        LocalDateTimeGsonAdapter(LocalDateTimeSerializer)
                    )
                    .registerTypeAdapter(
                        LocalDate::class.java,
                        LocalDateGsonAdapter(LocalDateSerializer)
                    ).create()
            )
        ).build()

    retrofit.create(ServerEndpoint::class.java)
}

class Server @Inject constructor(
    private val authPreference: AuthPreference,
) {
    suspend fun getRandomMusics(size: Int): List<MusicContent> {
        val response = retrofitInstance.getRandomMusics(size)
        return response.map { it.toMusicContent() }
    }

    suspend fun getMusic(id: Long): MusicContent {
        val response = retrofitInstance.getMusics(id = id, albumId = null).first()
        return response.toMusicContent()
    }

    suspend fun getRandomAlbums(size: Int): List<Album> {
        val response = retrofitInstance.getRandomAlbums(size)
        return response.map {
            Album(
                id = it.id,
                title = it.title,
                author = it.author,
                imageId = it.imageId,
                releasedDate = it.releaseDate,
            )
        }
    }

    suspend fun getAlbum(id: Long): AlbumContent {
        val albumResponse = retrofitInstance.getAlbum(id)
        val authorResponse = retrofitInstance.getAuthor(albumResponse.authorId)
        val musicResponseList = retrofitInstance.getMusics(id = null, albumId = id)
        return AlbumContent(
            id = albumResponse.id,
            title = albumResponse.title,
            author = authorResponse.name,
            imageId = albumResponse.imageId,
            type = albumResponse.type,
            genre = albumResponse.genre,
            releasedDate = albumResponse.releaseDate,
            contentList = musicResponseList.map { it.toMusicContent() },
        )
    }

    suspend fun getRandomPodcasts(size: Int): List<PodcastContent> {
        val response = retrofitInstance.getRandomPodcasts(size)
        return response.map { it.toPodcastContent() }
    }

    suspend fun getPodcast(id: Long): PodcastContent {
        val response = retrofitInstance.getPodcast(id)
        return response.toPodcastContent()
    }

    suspend fun getRandomVideos(size: Int): List<VideoContent> {
        val response = retrofitInstance.getRandomVideos(size)
        return response.map { it.toVideoContent() }
    }

    suspend fun getVideo(id: Long): VideoContent {
        val response = retrofitInstance.getVideo(id)
        return response.toVideoContent()
    }

    suspend fun getImage(id: Long): ImageBitmap {
        val response = retrofitInstance.getImage(id)
        val bytes = response.bytes()
        val bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
        return bitmap.asImageBitmap()
    }

    suspend fun signUp(email: String, password: String): User? {
        try {
            val response = retrofitInstance.signup(SignUpRequest(email, password))
            authPreference.accessToken = response.accessToken
            authPreference.refreshToken = response.refreshToken
            val user = response.user.toUser()
            authPreference.user = user
            return user
        } catch (e: HttpException) {
            if (e.code() == 409) return null
            else throw e
        }
    }

    suspend fun login(email: String, password: String): User? {
        try {
            val response = retrofitInstance.login(LoginRequest(email, password))
            authPreference.accessToken = response.accessToken
            authPreference.refreshToken = response.refreshToken
            val user = response.user.toUser()
            authPreference.user = user
            return user
        } catch (e: HttpException) {
            if (e.code() == 401) return null
            else throw e
        }
    }

    fun logout() {
        authPreference.accessToken = null
        authPreference.refreshToken = null
        authPreference.user = null
    }

    suspend fun getMyProfile() = doWithJwtHandling routine@{ _, user ->
        return@routine user
    }

    suspend fun getAllLikeLogs() = doWithJwtHandling routine@{ token, user ->
        val response = retrofitInstance.getAllLikes("Bearer $token", user.id)
        return@routine response.map { it.contentId to it.date }
    }

    suspend fun getLikeLog(id: Long) = doWithJwtHandling routine@{ token, user ->
        try {
            val response = retrofitInstance.isLiked("Bearer $token", user.id, id)
            return@routine response.contentId to response.date
        } catch (e: HttpException) {
            if (e.code() == 404) return@routine null
            else throw e
        }
    }

    suspend fun setLike(
        id: Long,
        setTo: Boolean,
    ) = doWithJwtHandling routine@{ token, user ->
        retrofitInstance.setLike("Bearer $token", user.id, id, setTo)
        return@routine
    }

    private suspend fun <T> doWithJwtHandling(
        routine: suspend (token: String, user: User) -> T,
    ): T {
        try {
            val token = authPreference.accessToken
            val user = authPreference.user
            if (token == null || user == null) throw Exception("No login")
            return routine(token, user)
        } catch (e: HttpException) {
            if (e.code() != 401) throw e
            refresh()
            val token = authPreference.accessToken
            val user = authPreference.user
            if (token == null || user == null) throw Exception("No login")
            return routine(token, user)
        }
    }

    private suspend fun refresh() {
        val refreshToken = authPreference.refreshToken ?: throw Exception("No refresh token")
        val auth = retrofitInstance.refresh(refreshToken)
        authPreference.accessToken = auth.accessToken
        authPreference.refreshToken = auth.refreshToken
        authPreference.user = auth.user.toUser()
    }
}