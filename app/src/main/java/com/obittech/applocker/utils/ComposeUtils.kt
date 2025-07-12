package com.obittech.applocker.utils

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
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
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.obittech.applocker.ui.screens.onboarding.OnboardingStepStatus


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


@Composable
fun AnimatedPagerHost(
    currentPage: Int,
    modifier: Modifier = Modifier,
    content: @Composable (Int) -> Unit
) {
    var previousPage by remember { mutableStateOf(currentPage) }

    AnimatedContent(
        targetState = currentPage,
        modifier = modifier,
        transitionSpec = {
            val isForward = targetState > initialState
            val direction = if (isForward) 1 else -1

            slideInHorizontally(
                animationSpec = tween(300),
                initialOffsetX = { it * direction }
            ) + fadeIn() togetherWith
                    slideOutHorizontally(
                        animationSpec = tween(300),
                        targetOffsetX = { it * -direction }
                    ) + fadeOut() using SizeTransform(clip = false)
        },
        label = "AnimatedPager"
    ) { pageIndex ->
        previousPage = pageIndex
        content(pageIndex)
    }
}

@Composable
fun StepAnimProgressBar(
    currentStep: Int,
    stepStatuses: List<OnboardingStepStatus>,
    modifier: Modifier = Modifier
        .fillMaxWidth()
        .height(8.dp)
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        stepStatuses.forEachIndexed { index, status ->
            val targetColor = when {
                index < currentStep && status == OnboardingStepStatus.COMPLETED -> Color(0xFF4CAF50) // green
                index == currentStep -> Color(0xFF2196F3) // blue
                status == OnboardingStepStatus.SKIPPED -> Color(0xFFFFC107) // amber
                else -> Color(0xFFBDBDBD) // gray
            }

            val animatedColor by animateColorAsState(targetColor, label = "progressColor")

            val fillFraction by animateFloatAsState(
                targetValue = when {
                    index < currentStep && status == OnboardingStepStatus.COMPLETED -> 1f
                    index == currentStep -> 0.8f
                    else -> 0.4f
                },
                label = "fillFraction"
            )

            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(8.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .background(animatedColor)
                    .fillMaxWidth(fraction = fillFraction)
            )
        }
    }
}

@Composable
fun StepProgressBar(
    currentStep: Int,
    stepStatuses: List<OnboardingStepStatus>,
    modifier: Modifier = Modifier
        .fillMaxWidth()
        .height(8.dp)
        .padding(horizontal = 24.dp)
) {

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        stepStatuses.forEachIndexed { index, status ->
            val color = when {
                index < currentStep && status == OnboardingStepStatus.COMPLETED -> Color(0xFF4CAF50) // Green
                index == currentStep -> Color(0xFF2196F3) // Blue
                status == OnboardingStepStatus.SKIPPED -> Color(0xFFFFC107) // Amber
                else -> Color(0xFFBDBDBD) // Grey
            }

            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(8.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .background(color)
            )
        }
    }
}

@Preview
@Composable
fun StepProgressBarPreview(){
    StepProgressBar(1, listOf(OnboardingStepStatus.COMPLETED, OnboardingStepStatus.COMPLETED, OnboardingStepStatus.COMPLETED, OnboardingStepStatus.COMPLETED, OnboardingStepStatus.COMPLETED))
}


@Composable
fun ErrorBanner(
    message: String,
    isVisible: Boolean,
    actionText: String? = null,
    onActionClicked: (() -> Unit)? = null
) {
    AnimatedVisibility(
        visible = isVisible,
        enter = fadeIn(tween(300)) + slideInVertically(initialOffsetY = {40}),
        exit = fadeOut(tween(300)) + slideOutVertically(targetOffsetY = { -40 })
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
            color = MaterialTheme.colorScheme.errorContainer,
            tonalElevation = 4.dp,
            shape = RoundedCornerShape(8.dp)
        ) {
            Row(
                modifier = Modifier
                    .padding(12.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = message,
                    color = MaterialTheme.colorScheme.onErrorContainer,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.weight(1f)
                )

                if (actionText != null && onActionClicked != null) {
                    Spacer(modifier = Modifier.width(12.dp))
                    TextButton(onClick = onActionClicked) {
                        Text(actionText.uppercase())
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun ErrorBannerPreview() {
    ErrorBanner("This is an error message", isVisible = true, actionText = "Close")
}