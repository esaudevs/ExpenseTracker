package com.esaudev.expensetracker.ext

import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed

const val NOT_SWIPE = -1
const val SWIPE_TO_RIGHT = 0
const val SWIPE_TO_LEFT = 1

fun Modifier.swappableHorizontally(
    onSwipeRight: () -> Unit,
    onSwipeLeft: () -> Unit
): Modifier = composed {
    val swipeDirection = remember {
        mutableIntStateOf(NOT_SWIPE)
    }

    draggable(
        state = rememberDraggableState(
            onDelta = {
                when {
                    it > 0 -> swipeDirection.intValue = SWIPE_TO_RIGHT
                    it < 0 -> swipeDirection.intValue = SWIPE_TO_LEFT
                }
            }
        ),
        onDragStopped = {
            when (swipeDirection.intValue) {
                SWIPE_TO_RIGHT -> onSwipeRight()
                SWIPE_TO_LEFT -> onSwipeLeft()
            }
        },
        orientation = Orientation.Horizontal
    )
}
