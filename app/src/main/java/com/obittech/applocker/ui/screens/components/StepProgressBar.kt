package com.obittech.applocker.ui.screens.components

import androidx.compose.animation.animateColor
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.WarningAmber
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.getValue
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import com.obittech.applocker.ui.theme.AppLockerTheme

@Composable
fun CircleStepProgressBar(
    currentStep: Int,
    stepStatuses: List<CircleStepStatus>,
    stepLabels: List<String>?, // Labels for each step if needed
    onStepClick: (Int) -> Unit, // Callback for when a step is clicked
    modifier: Modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 16.dp, vertical = 16.dp)
) {

//    if (stepLabels!=null){
        Column(modifier = modifier) {
            // Progress Circles + Connectors
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                stepStatuses.forEachIndexed { index, status ->
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        StepCircle(
                            status = status,
                            isCurrent = index == currentStep,
                            onClick = { onStepClick(index)})
                        if (stepLabels!=null){
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(text = stepLabels.getOrNull(index) ?: "", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f))
                        }
                    }

                    if (index != stepStatuses.lastIndex) {
                        StepConnector(
                            isCompleted = stepStatuses[index] == CircleStepStatus.COMPLETED,
                            isCurrent = index == currentStep || index + 1 == currentStep
                        )
                    }
                }
            }
        }
//    }else{
//        Row(
//            verticalAlignment = Alignment.CenterVertically,
//            horizontalArrangement = Arrangement.SpaceBetween,
//            modifier = modifier
//        ) {
//            stepStatuses.forEachIndexed { index, status ->
//                StepCircle(status = status, isCurrent = index == currentStep)
//
//                if (index != stepStatuses.lastIndex) {
//                    StepConnector(
//                        isCompleted = stepStatuses[index] == CircleStepStatus.COMPLETED,
//                        isCurrent = index == currentStep || index + 1 == currentStep
//                    )
//                }
//            }
//        }
//    }

}

//Animated Circle
@Composable
fun StepCircle(
    status: CircleStepStatus,
    isCurrent: Boolean,
    onClick: () -> Unit = {}
) {

    val haptics = LocalHapticFeedback.current

    val transition = updateTransition(targetState = status, label = "StepCircleTransition")

    val scale by transition.animateFloat(
        transitionSpec = { tween(durationMillis = 300) },
        label = "Scale"
    ) { target ->
        if (target == CircleStepStatus.COMPLETED) 1.2f else 1f
    }

    val backgroundColor by transition.animateColor(
        transitionSpec = { tween(durationMillis = 300) },
        label = "BackgroundColor"
    ) {
        when (it) {
            CircleStepStatus.COMPLETED -> Color(0xFF4CAF50)
            CircleStepStatus.SKIPPED -> Color(0xFFFFC107)
            else -> Color.White
        }
    }

    val borderColor = when {
        status == CircleStepStatus.COMPLETED -> Color(0xFF4CAF50)
        isCurrent -> Color(0xFF2196F3)
        status == CircleStepStatus.SKIPPED -> Color(0xFFFFC107)
        else -> Color.Gray
    }

    Box(
        modifier = Modifier
            .size(24.dp * scale)
            .border(2.dp, borderColor, CircleShape)
            .background(backgroundColor, shape = CircleShape)
            .padding(2.dp)
            .clickable {
                haptics.performHapticFeedback(HapticFeedbackType.LongPress)
                onClick() },
        contentAlignment = Alignment.Center
    ) {
        if (status == CircleStepStatus.COMPLETED) {
            Icon(
                imageVector = Icons.Default.Check,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(14.dp)
            )
        }else if (status == CircleStepStatus.SKIPPED) {
            Icon(
                imageVector = Icons.Default.WarningAmber,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(14.dp)
            )
        } else if (isCurrent) {
            val dotAlpha by animateFloatAsState(
                targetValue = if (isCurrent) 1f else 0f,
                animationSpec = tween(500),
                label = "DotFade"
            )

            Box(
                modifier = Modifier
                    .size(10.dp)
                    .graphicsLayer {
                        alpha = dotAlpha
                    }
                    .background(Color(0xFF2196F3), CircleShape)
            )
        }
    }
}

//@Composable
//fun StepCircle(
//    status: CircleStepStatus,
//    isCurrent: Boolean
//) {
//    val color = when (status) {
//        CircleStepStatus.COMPLETED -> Color(0xFF4CAF50) // green
//        CircleStepStatus.SKIPPED -> Color(0xFFFFC107)    // amber
//        else -> Color.White
//    }
//
//    val borderColor = when {
//        status == CircleStepStatus.COMPLETED -> Color(0xFF4CAF50)
//        isCurrent -> Color(0xFF2196F3)
//        status == CircleStepStatus.SKIPPED -> Color(0xFFFFC107)
//        else -> Color.Gray
//    }
//
//    Box(
//        modifier = Modifier
//            .size(24.dp)
//            .border(2.dp, borderColor, CircleShape)
//            .background(color, shape = CircleShape),
//        contentAlignment = Alignment.Center
//    ) {
//        if (status == CircleStepStatus.COMPLETED) {
//            Icon(
//                imageVector = Icons.Default.Check,
//                contentDescription = null,
//                tint = Color.White,
//                modifier = Modifier.size(14.dp)
//            )
//        }else if (status == CircleStepStatus.SKIPPED) {
//            Icon(
//                imageVector = Icons.Default.WarningAmber,
//                contentDescription = null,
//                tint = Color.White,
//                modifier = Modifier.size(14.dp)
//            )
//        } else if (isCurrent) {
//            Box(
//                modifier = Modifier
//                    .size(10.dp)
//                    .background(Color(0xFF2196F3), CircleShape)
//            )
//        }
//    }
//}

//Animated Connector
@Composable
fun RowScope.StepConnector(
    isCompleted: Boolean,
    isCurrent: Boolean
) {
    val color = when {
        isCompleted -> Color(0xFF4CAF50)
        isCurrent -> Color(0xFF2196F3)
        else -> Color.LightGray
    }

    val animatedWidth by animateFloatAsState(
        targetValue = if (isCompleted || isCurrent) 1f else 0.4f,
        animationSpec = tween(500),
        label = "LineGrow"
    )

    Box(
        modifier = Modifier
            .weight(1f)
            .height(2.dp)
            .fillMaxWidth(animatedWidth)
            .background(color)
    )
}

//@Composable
//fun RowScope.StepConnector(
//    isCompleted: Boolean,
//    isCurrent: Boolean
//) {
//    val color = when {
//        isCompleted -> Color(0xFF4CAF50)
//        isCurrent -> Color(0xFF2196F3)
//        else -> Color.LightGray
//    }
//
//    Box(
//        modifier = Modifier
//            .weight(1f)
//            .height(2.dp)
//            .background(color)
//    )
//}

enum class CircleStepStatus {
    PENDING,
    COMPLETED,
    SKIPPED
}



@Preview
@Composable
fun CircleStepProgressBarPreview(){
    AppLockerTheme(isDarkTheme = true) {
        CircleStepProgressBar(3, listOf(CircleStepStatus.COMPLETED, CircleStepStatus.SKIPPED, CircleStepStatus.PENDING, CircleStepStatus.PENDING, CircleStepStatus.PENDING),stepLabels = listOf("Permission", "Overlay", "Password", "Setting", "Finish"), onStepClick = {})
    }
}