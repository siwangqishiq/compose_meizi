package xyz.panyi.meizi.page

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import kotlinx.coroutines.flow.distinctUntilChanged
import xyz.panyi.meizi.model.Album
import xyz.panyi.meizi.ui.theme.Compose_meiziTheme
import xyz.panyi.meizi.vm.AlbumsViewModel

@Composable
fun AblumsPage(modifier: Modifier = Modifier,
               viewModel: AlbumsViewModel = AlbumsViewModel()) {
    val albums by viewModel.albums.collectAsState()
    val gridState = rememberLazyGridState()
    val refreshState = rememberSwipeRefreshState(false)
    
    // 滑动到底部加载更多
    LaunchedEffect(gridState) {
        snapshotFlow {
            val layoutInfo = gridState.layoutInfo
            val lastVisible = layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0
            lastVisible >= layoutInfo.totalItemsCount - 4
        }.distinctUntilChanged()
            .collect { shouldLoad ->
                if(shouldLoad){
                    viewModel.loadMore()
                }
            }
    }
    
    SwipeRefresh(
        state = refreshState,
        onRefresh = { viewModel.refresh() }
    ){
        LazyVerticalGrid(columns = GridCells.Fixed(2),
            state = gridState,
            modifier = modifier.fillMaxSize()
        ) {
            items(
                items = albums,
                key = { it.id }
            ) { album ->
                CardItem(album)
            }
        }
    }
}

@Composable
fun CardItem(album: Album, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier
            .padding(8.dp)
            .fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(album.cover)
                    .crossfade(true)
                    .crossfade(300)
                    .build(),
                contentDescription = album.title,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(240.dp)
            )
            
            Text(
                text = album.title?:"",
                modifier = Modifier.padding(8.dp),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                fontSize = 14.sp
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
fun AblumsPagePreview() {
    Compose_meiziTheme {
        AblumsPage()
    }
}