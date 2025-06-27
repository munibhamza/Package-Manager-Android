package com.obittech.applocker.utils

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.android.material.color.MaterialColors


@Composable
fun AccessibilityPromptDialog(
    showDialog: Boolean,
    onDismiss: () -> Unit,
    onOpenSettings: () -> Unit
) {
    if (showDialog) {
        AlertDialog(
            onDismissRequest = onDismiss,
            title = {
                Text("Enable Accessibility")
            },
            text = {
                Text("To use the app lock feature, please enable Accessibility permission for this app.")
            },
            confirmButton = {
                Button(onClick = onOpenSettings) {
                    Text("Open Settings")
                }
            },
            dismissButton = {
                TextButton(onClick = onDismiss) {
                    Text("Cancel")
                }
            }
        )
    }
}


@Composable
fun Keypad(
    onDigitPress: (String) -> Unit,
    onBackspace: () -> Unit
) {
    val buttons = listOf(
        listOf("1", "2", "3"),
        listOf("4", "5", "6"),
        listOf("7", "8", "9"),
        listOf("", "0", "←")
    )
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        buttons.forEach { row ->
            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier.fillMaxWidth()
            ) {

                row.forEach { label ->
                    KeypadAnimatedButton(label, onDigit = onDigitPress, onBackspace = onBackspace)
                }
            }
            Spacer(modifier = Modifier.height(12.dp))
        }
    }
}


@Composable
fun KeypadButton(
    label: String,
    onDigit: (String) -> Unit,
    onBackspace: () -> Unit
) {
    val isBackspace = label == "←"
    Box(
        modifier = Modifier
            .size(80.dp)
            .clickable(
                enabled = label.isNotEmpty(),
                onClick = {
                    if (isBackspace) onBackspace() else onDigit(label)
                }
            ),
        contentAlignment = Alignment.Center
    ) {
        if (label.isNotEmpty()) {
            Text(
                text = label,
                fontSize = 28.sp,
                color = Color.White
            )
        }
    }
}


@Composable
fun KeypadAnimatedButton(
    label: String,
    onDigit: (String) -> Unit,
    onBackspace: () -> Unit
) {
    val isBackspace = label == "←"
    var pressed by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(
        targetValue = if (pressed) 0.9f else 1f,
        animationSpec = tween(durationMillis = 100)
    )
    Box(
        modifier = Modifier
            .size(80.dp)
            .graphicsLayer(scaleX = scale, scaleY = scale)
            .clickable(
                onClick = {
                    pressed = true
                    if (isBackspace) onBackspace() else onDigit(label)
                },
                onClickLabel = "Keypad button $label"
            )
            .pointerInput(Unit) {
                detectTapGestures(
                    onPress = {
                        pressed = true
                        tryAwaitRelease()
                        pressed = false
                    }
                )
            },
        contentAlignment = Alignment.Center
    ) {
        if (label.isNotEmpty()) {
            Text(
                text = label,
                fontSize = 28.sp,
                color = Color.White
            )
        }
    }
}

@Preview
@Composable
fun KeypadPreview() {
    Keypad(onDigitPress = {}, onBackspace = {})
}


@Composable
fun PinDots(pinLength: Int, filledCount: Int) {
    Row(horizontalArrangement = Arrangement.Center) {
        repeat(pinLength) { index ->
            val isFilled = index < filledCount
            AnimatedVisibility(
                visible = true,
                enter = fadeIn(animationSpec = tween(300)) + scaleIn(initialScale = 0.6f),
                exit = fadeOut()
            ) {
                Box(
                    modifier = Modifier
                        .size(16.dp)
                        .padding(8.dp)
                        .background(
                            color = if (isFilled) Color.White else Color.Gray,
                            shape = CircleShape
                        )
                )
            }
        }
    }
}
@Preview
@Composable
fun PinDotsPreview(){
    PinDots(4,3)
}

@Composable
fun NumericKeypad(onKeyPress: (String) -> Unit) {
    val keys = listOf(
        listOf("1","2","3"),
        listOf("4","5","6"),
        listOf("7","8","9"),
        listOf("","0","←")
    )
    Column {
        keys.forEach { row ->
            Row(horizontalArrangement = Arrangement.Center) {
                row.forEach { key ->
                    if (key.isNotEmpty()) {
                        Button(
                            onClick = { onKeyPress(key) },
                            modifier = Modifier
                                .size(72.dp)
                                .padding(8.dp),
                            shape = CircleShape
                        ) {
                            Text(key)
                        }
                    } else {
                        Spacer(modifier = Modifier.size(72.dp).padding(8.dp))
                    }
                }
            }
        }
    }
}
@Preview
@Composable
fun NumericKepadPreview(){
    NumericKeypad(onKeyPress = {})
}
