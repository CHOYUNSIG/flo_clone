package umc.study.umc_7th.ui.song

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import umc.study.umc_7th.R
import umc.study.umc_7th.SuspendedImage
import umc.study.umc_7th.previewMusicContentList

@Composable
fun ContentFrame(
    title: String,
    author: String,
    imageId: Long,
    onAuthorNameClicked: () -> Unit,
) {
    var coverWidth by remember { mutableStateOf(0.dp) }
    val density = LocalDensity.current

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .padding(4.dp)
            .fillMaxWidth()
    ) {
        Text(
            text = title,
            style = TextStyle(
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
            ),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.widthIn(max = coverWidth)
        )
        Button(
            onClick = onAuthorNameClicked,
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Transparent,
                contentColor = Color.Black.copy(alpha = 0.5f)
            ),
            shape = RoundedCornerShape(4.dp),
            contentPadding = PaddingValues(4.dp),
            modifier = Modifier.widthIn(max = coverWidth)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = author,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Icon(
                    painter = painterResource(id = R.drawable.btn_arrow_more),
                    contentDescription = null,
                    modifier = Modifier.size(16.dp)
                )
            }
        }
        SuspendedImage(id = imageId) { bitmap ->
            if (bitmap != null) Image(
                bitmap = bitmap,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .height(coverWidth)
                    .fillMaxWidth(0.6f)
                    .onGloballyPositioned { with(density) { coverWidth = it.size.width.toDp() } }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewContentFrame() {
    val content = previewMusicContentList.random()

    ContentFrame(
        title = content.title,
        author = content.author,
        imageId = content.imageId,
        onAuthorNameClicked = {},
    )
}
