package umc.study.umc_7th.ui.main.locker

import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

data class TabItem(
    val label: String,
    val page: @Composable () -> Unit,
)

@Composable
fun TabLayout(
    tabs: List<TabItem>
) {
    val pageState = rememberPagerState { tabs.size }
    val scope = rememberCoroutineScope()

    Column {
        Row(
            modifier = Modifier.horizontalScroll(rememberScrollState())
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(32.dp),
                modifier = Modifier.padding(horizontal = 16.dp)
            ) {
                tabs.forEachIndexed { index, (label, _) ->
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .drawBehind {
                                if (index == pageState.currentPage) drawLine(
                                    color = Color.Blue,
                                    start = Offset(x = 0f, y = size.height),
                                    end = Offset(x = size.width, y = size.height),
                                    strokeWidth = 5f,
                                )
                            }
                            .heightIn(min = 48.dp)
                            .clickable {
                                scope.launch {
                                    pageState.animateScrollToPage(index)
                                }
                            }
                    ) {
                        Text(
                            text = label,
                            style = TextStyle(
                                color = if (index == pageState.currentPage)
                                    Color.Blue
                                else
                                    Color.Black.copy(alpha = 0.5f),
                            )
                        )
                    }
                }
            }
        }
        HorizontalPager(
            state = pageState,
            modifier = Modifier.fillMaxWidth()
        ) { index ->
            tabs[index].page()
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewTabLayout() {
    TabLayout(
        tabs = listOf(
            TabItem(
                label = "저장한 곡",
                page = {
                    Text(text = "이것은 저장한 곡")
                }
            ),
            TabItem(
                label = "음악파일",
                page = {
                    Text(text = "이것은 음악파일")
                }
            )
        )
    )
}