package umc.study.umc_7th

import androidx.compose.runtime.Composable
import kotlinx.serialization.Serializable
import java.time.LocalDate


sealed interface Identifiable {
    val id: Long
}

sealed interface Playable {
    val length: Int
}

sealed interface Viewable {
    val imageId: Long
}

sealed interface Authorized {
    val title: String
    val author: String
}

@Serializable
data class Album(
    override val id: Long,
    override val title: String,
    override val author: String,
    override val imageId: Long,
    @Serializable(with = LocalDateSerializer::class) val releasedDate: LocalDate,
): Identifiable, Authorized, Viewable

sealed interface Content: Identifiable, Playable, Viewable, Authorized {
    override val id: Long
    override val title: String
    override val author: String
    override val imageId: Long
    override val length: Int
}

@Serializable
data class AlbumContent(
    override val id: Long,
    override val title: String,
    override val author: String,
    override val imageId: Long,
    val type: String,
    val genre: String,
    @Serializable(with = LocalDateSerializer::class) val releasedDate: LocalDate,
    val contentList: List<MusicContent>,
): Content {
    override val length: Int = contentList.sumOf { it.length }

    fun toAlbum() = Album(
        id = id,
        title = title,
        author = author,
        imageId = imageId,
        releasedDate = releasedDate,
    )
}

@Serializable
data class MusicContent(
    override val id: Long,
    override val title: String,
    override val author: String,
    override val imageId: Long,
    override val length: Int,
    val albumId: Long,
    val label: String? = null,
): Content

@Serializable
data class PodcastContent(
    override val id: Long,
    override val title: String,
    override val author: String,
    override val imageId: Long,
    override val length: Int,
    val description: String?,
): Content

@Serializable
data class VideoContent(
    override val id: Long,
    override val title: String,
    override val author: String,
    override val imageId: Long,
    override val length: Int,
): Content


val previewAlbumContent @Composable get() = AlbumContent(
    id = 100,
    title = "IU 5th Album \"LILAC\"",
    author = "아이유(IU)",
    imageId = -1,
    type = "정규",
    genre = "댄스 팝",
    releasedDate = LocalDate.of(2023, 1, 1),
    contentList = previewMusicContentList,
)

val previewMusicContentList @Composable get() = listOf(
    "라일락",
    "Flu",
    "Coin",
    "봄 안녕 봄",
    "Celebrity",
    "돌림노래 (feat. DEAN)",
    "빈 컵 (Empty Cup)",
    "아이와 나의 바다",
    "어푸 (Ah puh)",
    "에필로그",
).mapIndexed { index, title ->
    MusicContent(
        id = 100,
        title = title,
        author = "아이유(IU)",
        imageId = -1,
        length = 210,
        albumId = 100,
        label = if (index == 0 || index == 2) "TITLE" else null
    )
}

val previewPodcastContentList @Composable get() = List(15) {
    PodcastContent(
        id = 100,
        title = "김시선의 귀책사유 FLO X 윌라",
        author = "김시선",
        imageId = -2,
        length = 200,
        description = null,
    )
}

val previewVideoContentList @Composable get() = List(15) {
    VideoContent(
        id = 100,
        title = "제목",
        author = "지은이",
        imageId = -3,
        length = 200,
    )
}