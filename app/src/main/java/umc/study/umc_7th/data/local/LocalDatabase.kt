package umc.study.umc_7th.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import umc.study.umc_7th.MusicContent
import umc.study.umc_7th.data.local.dao.LikedContentDao
import umc.study.umc_7th.data.local.dao.SavedMusicDao
import umc.study.umc_7th.data.local.entity.LikedContentEntity
import umc.study.umc_7th.data.local.entity.SavedMusicEntity
import java.time.LocalDateTime
import java.time.ZoneOffset

@Database(
    entities = [
        LikedContentEntity::class,
        SavedMusicEntity::class,
    ],
    version = 1,
    exportSchema = false,
)
@TypeConverters(Converter::class)
abstract class LocalDatabase: RoomDatabase() {
    abstract fun getSavedMusicDao(): SavedMusicDao
    abstract fun getLikedContentDao(): LikedContentDao

    fun getAllSavedMusicsFlow(): Flow<List<MusicContent>> {
        return getSavedMusicDao().getFlow().map { it.map { entity -> entity.toContent() } }
    }

    fun getAllLikedContentsFlow(): Flow<List<Pair<Long, LocalDateTime>>> {
        return getLikedContentDao().getFlow().map { it.map { entity -> entity.id to entity.date } }
    }

    suspend fun getSavedMusic(id: Long): MusicContent? {
        return getSavedMusicDao().get(id).firstOrNull()?.toContent()
    }

    suspend fun addSavedMusic(vararg musics: MusicContent) {
        getSavedMusicDao().insert(*musics.map { SavedMusicEntity.fromContent(it) }.toTypedArray())
    }

    suspend fun deleteSavedMusic(music: MusicContent) {
        getSavedMusicDao().delete(SavedMusicEntity.fromContent(music))
    }

    suspend fun clearSavedMusic() {
        getSavedMusicDao().clear()
    }

    suspend fun isLiked(id: Long): Boolean {
        return getLikedContentDao().get(id).isNotEmpty()
    }

    suspend fun like(vararg ids: Long) {
        ids.forEach { id ->
            getLikedContentDao().insert(LikedContentEntity(id = id, date = LocalDateTime.now()))
        }
    }

    suspend fun unlike(vararg ids: Long) {
        ids.forEach { id ->
            getLikedContentDao().delete(id)
        }
    }
}

class Converter {
    @TypeConverter
    fun fromLocalDateTime(date: LocalDateTime?) = date?.toEpochSecond(ZoneOffset.UTC)

    @TypeConverter
    fun toLocalDateTime(epochSecond: Long?) = epochSecond?.let {
        LocalDateTime.ofEpochSecond(it, 0, ZoneOffset.UTC)
    }
}
