package com.afifcaranks.robloxstudiomobile.ui.viewport

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner

@Composable
fun FilamentViewport(modifier: Modifier = Modifier) {
    val lifecycleOwner = LocalLifecycleOwner.current
    val viewportState = remember { mutableStateOf<FilamentViewportView?>(null) }

    AndroidView(
        modifier = modifier,
        factory = { context ->
            FilamentViewportView(context).also { viewportState.value = it }
        }
    )

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_RESUME -> viewportState.value?.resumeRendering()
                Lifecycle.Event.ON_PAUSE -> viewportState.value?.pauseRendering()
                else -> Unit
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)

        if (lifecycleOwner.lifecycle.currentState.isAtLeast(Lifecycle.State.RESUMED)) {
            viewportState.value?.resumeRendering()
        }

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
            viewportState.value?.release()
            viewportState.value = null
        }
    }
}
