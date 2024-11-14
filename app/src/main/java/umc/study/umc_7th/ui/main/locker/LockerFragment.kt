package umc.study.umc_7th.ui.main.locker

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import umc.study.umc_7th.MusicContent
import umc.study.umc_7th.databinding.FragmentLockerBinding
import umc.study.umc_7th.previewMusicContentList
import umc.study.umc_7th.ui.main.BottomNavigationBar
import umc.study.umc_7th.ui.main.MainViewModel
import umc.study.umc_7th.ui.main.NavigationDestination
import umc.study.umc_7th.ui.main.album.TabItem

class LockerFragment : Fragment() {
    private val viewModel: MainViewModel by activityViewModels()
    private lateinit var binding: FragmentLockerBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLockerBinding.inflate(inflater, container, false)
        binding.composeViewLocker.setContent { Screen() }
        return binding.root
    }

    @Composable
    private fun Screen() {
        LockerScreen(
            savedMusics = viewModel.savedMusics.toList(),
            onMusicContentClicked = { viewModel.play(it) },
            onDeleteContentClicked = {
                viewModel.deleteMusic(
                    music = it,
                    onFailed = { /* TODO */ }
                )
            },
        )
    }
}

@Composable
fun LockerScreen(
    savedMusics: List<MusicContent>,
    onMusicContentClicked: (MusicContent) -> Unit,
    onDeleteContentClicked: (MusicContent) -> Unit,
) {
    Column {
        TitleBar(
            title = "보관함",
            isLoginDone = false,
            onLoginClicked = { /* TODO */ }
        )
        TabLayout(
            tabs = listOf(
                TabItem(
                    label = "저장한 곡",
                    page = {
                        SavedMusicPage(
                            musics = savedMusics,
                            onEditButtonClicked = { /* TODO */ },
                            onPlayAllButtonClicked = { /* TODO */ },
                            onPlayButtonClicked = onMusicContentClicked,
                            onDeleteClicked = onDeleteContentClicked,
                            onDetailsClicked = { /* TODO */ },
                        )
                    }
                ),
                TabItem(
                    label = "음악파일",
                    page = { StorageFilePage() }
                )
            )
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewLockerScreen() {
    Scaffold(
        bottomBar = {
            BottomNavigationBar(
                onPlaylistButtonClicked = {},
                onDestinationClicked = {},
                onPreviousButtonClicked = {},
                onNextButtonClicked = {},
                onPlayButtonClicked = {},
                onContentClicked = {},
                currentDestination = NavigationDestination.HOME,
                currentContent = previewMusicContentList.random(),
                isPlaying = false,
                playingPoint = 50,
            )
        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            LockerScreen(
                savedMusics = previewMusicContentList,
                onMusicContentClicked = {},
                onDeleteContentClicked = {},
            )
        }
    }
}
