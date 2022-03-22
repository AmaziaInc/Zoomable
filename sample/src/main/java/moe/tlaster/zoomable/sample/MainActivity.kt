package moe.tlaster.zoomable.sample

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Checkbox
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import moe.tlaster.zoomable.Zoomable
import moe.tlaster.zoomable.rememberZoomableState

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Sample()
        }
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
private fun Sample() {
    val pagerState = rememberPagerState(pageCount = 10)
    HorizontalPager(state = pagerState) { page ->
        val state = rememberZoomableState(
            minScale = 1f
        )
        var enable by remember {
            mutableStateOf(true)
        }
        var showBox by remember {
            mutableStateOf(false)
        }
        Box(modifier = Modifier.fillMaxSize()) {
            Zoomable(
                state = state,
                enable = enable,
                onTap = {
                    showBox = !showBox
                },
                doubleTapScale = {
                    if (state.scale > 2f) {
                        state.minScale
                    } else {
                        state.scale * 2
                    }
                },
                shouldConsumePositionChange = { _, dragAmount ->
                    if (dragAmount.y >= 0.1f) {
                        true
                    } else {
                        val scaledHalfWidth = size.width * (state.scale - 1) / 2
                        if (dragAmount.x > 0 && state.translateX.toInt() == scaledHalfWidth.toInt()) {
                            false
                        } else {
                            !(dragAmount.x < 0 && (-state.translateX).toInt() == scaledHalfWidth.toInt())
                        }
                    }
                }
            ) {
                // Our page content
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(2.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        modifier = Modifier
                            .fillMaxSize()
                            .clipToBounds(),
                        painter = painterResource(id = R.drawable.dog),
                        contentScale = ContentScale.Crop,
                        contentDescription = null
                    )
                    Text(
                        text = "Page: $page",
                    )
                }
            }
            Checkbox(checked = enable, onCheckedChange = { enable = it })
            if (showBox) {
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(8.dp)
                        .background(color = Color.Red)
                        .size(32.dp)
                )
            }
        }
    }
}
