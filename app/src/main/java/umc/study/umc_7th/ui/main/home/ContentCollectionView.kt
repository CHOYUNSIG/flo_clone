package umc.study.umc_7th.ui.main.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import umc.study.umc_7th.Album
import umc.study.umc_7th.Authorized
import umc.study.umc_7th.Content
import umc.study.umc_7th.PodcastContent
import umc.study.umc_7th.R
import umc.study.umc_7th.SuspendedImage
import umc.study.umc_7th.VideoContent
import umc.study.umc_7th.Viewable
import umc.study.umc_7th.previewAlbumContent
import umc.study.umc_7th.previewPodcastContentList
import umc.study.umc_7th.previewVideoContentList

enum class GlobeCategory(
    val expression: String,
) {
    GLOBAL(expression = "종합"),
    DOMESTIC(expression = "국내"),
    FOREIGN(expression = "해외"),
}

@Composable
fun GlobeCategorizedAlbumCollectionView(
    title: String,
    contentList: List<Album>,
    globeCategory: GlobeCategory,
    onViewTitleClicked: () -> Unit,
    onAlbumClicked: (Album) -> Unit,
    onAlbumPlayClicked: (Album) -> Unit,
    onCategoryClicked: (GlobeCategory) -> Unit,
) {
    ContentCollectionView(
        contentList = contentList,
        titleBar = {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .fillMaxWidth()
            ) {
                Button(
                    colors = ButtonColors(
                        containerColor = Color.Transparent,
                        disabledContainerColor = Color.Transparent,
                        contentColor = Color.Unspecified,
                        disabledContentColor = Color.Unspecified,
                    ),
                    onClick = onViewTitleClicked,
                    shape = RoundedCornerShape(4.dp),
                    contentPadding = PaddingValues(4.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                    ) {
                        Text(
                            text = title,
                            style = TextStyle(
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                            )
                        )
                        Icon(
                            painter = painterResource(id = R.drawable.btn_arrow_more),
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
                Row {
                    GlobeCategory.entries.forEach { category ->
                        TextButton(
                            onClick = { onCategoryClicked(category) },
                        ) {
                            Text(
                                text = AnnotatedString(category.expression),
                                style = TextStyle(
                                    color = if (category == globeCategory)
                                        Color.Blue
                                    else
                                        Color.Gray
                                )
                            )
                        }
                    }
                }
            }
        },
        thumbnail = { content ->
            Box(
                contentAlignment = Alignment.BottomEnd,
            ) {
                SuspendedImage(id = content.imageId) { bitmap ->
                    if (bitmap != null) Image(
                        bitmap = bitmap,
                        contentScale = ContentScale.Crop,
                        contentDescription = null,
                        modifier = Modifier
                            .size(128.dp)
                            .clip(RoundedCornerShape(8.dp))
                    )
                }
                IconButton(onClick = { onAlbumPlayClicked(content) }) {
                    Icon(
                        painter = painterResource(id = R.drawable.btn_miniplayer_play),
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(48.dp)
                    )
                }
            }
        },
        onContentClicked = onAlbumClicked,
    )
}

@Composable
fun PodcastCollectionView(
    title: String,
    contentList: List<PodcastContent>,
    onContentClicked: (PodcastContent) -> Unit,
) {
    ContentCollectionView(
        contentList = contentList,
        titleBar = {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 4.dp)
            ) {
                Text(
                    text = title,
                    style = TextStyle(
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                    )
                )
            }
        },
        thumbnail = { content ->
            SuspendedImage(id = content.imageId) { bitmap ->
                if (bitmap != null) Image(
                    bitmap = bitmap,
                    contentScale = ContentScale.Crop,
                    contentDescription = null,
                    modifier = Modifier
                        .size(128.dp)
                        .clip(RoundedCornerShape(8.dp))
                )
            }
        },
        onContentClicked = onContentClicked,
    )
}

@Composable
fun VideoCollectionView(
    title: String,
    contentList: List<VideoContent>,
    onContentClicked: (Content) -> Unit,
) {
    ContentCollectionView(
        contentList = contentList,
        titleBar = {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 4.dp)
            ) {
                Text(
                    text = title,
                    style = TextStyle(
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                    )
                )
            }
        },
        thumbnail = { content ->
            Box(contentAlignment = Alignment.BottomEnd) {
                SuspendedImage(id = content.imageId) { bitmap ->
                    if (bitmap != null) Image(
                            bitmap = bitmap,
                            contentScale = ContentScale.FillHeight,
                            contentDescription = null,
                            modifier = Modifier
                                .width(228.dp)
                                .height(128.dp)
                                .clip(RoundedCornerShape(8.dp))
                    )
                }
                Box(
                    modifier = Modifier
                        .padding(8.dp)
                        .background(color = Color.Black.copy(alpha = 0.5f))
                ) {
                    Text(
                        text = "${"%02d".format(content.length / 60)}:${content.length % 60}",
                        style = TextStyle(
                            fontSize = 10.sp,
                            color = Color.White,
                        ),
                        modifier = Modifier.padding(4.dp),
                    )
                }
            }
        },
        onContentClicked = onContentClicked,
    )
}

@Composable
private fun <T> ContentCollectionView(
    contentList: List<T>,
    titleBar: @Composable () -> Unit, // 제목 바에 그려질 컴포저블
    thumbnail: @Composable (T) -> Unit, // 컨텐츠 미리보기 사진 컴포저블
    onContentClicked: (T) -> Unit
) where T: Viewable, T: Authorized {
    val density = LocalDensity.current
    var contentWidth by remember { mutableStateOf(0.dp) }

    Column(
        verticalArrangement = Arrangement.spacedBy(4.dp),
        modifier = Modifier.padding(vertical = 16.dp)
    ) {
        titleBar()
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            item { Spacer(modifier = Modifier.width(4.dp)) }
            items(count = contentList.size) { index ->
                val content = contentList[index]
                Button(
                    colors = ButtonColors(
                        containerColor = Color.Transparent,
                        disabledContainerColor = Color.Transparent,
                        contentColor = Color.Unspecified,
                        disabledContentColor = Color.Unspecified,
                    ),
                    onClick = { onContentClicked(content) },
                    shape = RoundedCornerShape(4.dp),
                    contentPadding = PaddingValues(4.dp)
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Box(
                            modifier = Modifier.onGloballyPositioned {
                                with(density) { contentWidth = it.size.width.toDp() }
                            }
                        ) {
                            thumbnail(content)
                        }
                        Column(
                            verticalArrangement = Arrangement.spacedBy(4.dp),
                            modifier = Modifier.width(contentWidth)
                        ) {
                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier.height(24.dp)
                            ) {
                                Text(
                                    text = content.title,
                                    style = TextStyle(
                                        fontSize = 16.sp,
                                    ),
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis,
                                )
                            }
                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier.height(24.dp)
                            ) {
                                Text(
                                    text = content.author,
                                    style = TextStyle(
                                        color = TextStyle.Default.color.copy(alpha = 0.5f)
                                    ),
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis,
                                )
                            }
                        }
                    }
                }
            }
            item { Spacer(modifier = Modifier.width(4.dp)) }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewGlobeCategorizedMusicCollectionView() {
    GlobeCategorizedAlbumCollectionView(
        title = "오늘 발매 음악",
        contentList = List(10) {
            val album = previewAlbumContent
            Album(
                id = album.id,
                title = album.title,
                author = album.author,
                imageId = album.imageId,
                releasedDate = album.releasedDate,
            )
        },
        globeCategory = GlobeCategory.GLOBAL,
        onViewTitleClicked = {},
        onAlbumClicked = {},
        onAlbumPlayClicked = {},
        onCategoryClicked = {},
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewPodcastCollectionView() {
    PodcastCollectionView(
        title = "매일 들어도 좋은 팟캐스트",
        contentList = previewPodcastContentList,
        onContentClicked = {},
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewVideoCollectionView() {
    VideoCollectionView(
        title = "비디오 콜렉션",
        contentList = previewVideoContentList,
        onContentClicked = {},
    )
}