package com.obittech.applocker.ui.screens.screenoverlay

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun OverlayPinDialog(
    onSubmit: (String) -> Unit,
    onCancel: () -> Unit
) {
    // Animate visibility with fade + scale
    AnimatedVisibility(
        visible = true,
        enter = fadeIn() + scaleIn(),
        exit = fadeOut() + scaleOut()
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    // Blur effect (Android 12+ only)
                    Brush.verticalGradient(
                        colors = listOf(
                            MaterialTheme.colorScheme.background.copy(alpha = 0.75f),
                            MaterialTheme.colorScheme.surface.copy(alpha = 0.85f)
                        )
                    )
                )
                .padding(32.dp),
            contentAlignment = Alignment.Center
        ) {
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = MaterialTheme.colorScheme.surface,
                tonalElevation = 6.dp,
                shadowElevation = 8.dp,
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("Enter PIN to Unlock", style = MaterialTheme.typography.titleMedium)
                    Spacer(modifier = Modifier.height(16.dp))

                    var pin by remember { mutableStateOf("") }

                    OutlinedTextField(
                        value = pin,
                        onValueChange = { pin = it },
                        label = { Text("PIN") },
                        visualTransformation = PasswordVisualTransformation()
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(horizontalArrangement = Arrangement.SpaceEvenly) {
                        OutlinedButton(onClick = { onCancel() }) {
                            Text("Cancel")
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Button(onClick = { onSubmit(pin) }) {
                            Text("Unlock")
                        }

                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun OverlayPinDialogPreview() {
    OverlayPinDialog({}) { }
}

//@Composable
//fun OverlayPinDialog(
//    onSubmit: (String) -> Unit,
//    onCancel: () -> Unit
//) {
//    Surface(
//        shape = MaterialTheme.shapes.medium,
//        color = MaterialTheme.colorScheme.surface,
//        modifier = Modifier
//            .fillMaxWidth()
//            .padding(32.dp)
//    ) {
//        Column(
//            modifier = Modifier.padding(24.dp),
//            verticalArrangement = Arrangement.Center,
//            horizontalAlignment = Alignment.CenterHorizontally
//        ) {
//            Text("Enter PIN to Unlock", style = MaterialTheme.typography.titleMedium)
//            Spacer(modifier = Modifier.height(16.dp))
//
//            var pin by remember { mutableStateOf("") }
//
//            OutlinedTextField(
//                value = pin,
//                onValueChange = { pin = it },
//                label = { Text("PIN") },
//                visualTransformation = PasswordVisualTransformation()
//            )
//
//            Spacer(modifier = Modifier.height(16.dp))
//
//            Row(horizontalArrangement = Arrangement.SpaceEvenly) {
//                Button(onClick = { onSubmit(pin) }) {
//                    Text("Unlock")
//                }
//                Spacer(modifier = Modifier.width(8.dp))
//                OutlinedButton(onClick = { onCancel() }) {
//                    Text("Cancel")
//                }
//            }
//        }
//    }
//}
