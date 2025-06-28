package com.obittech.applocker.ui.screens.lockScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.obittech.applocker.utils.NumericKeypad

@Composable
fun LockPinOverlayView(correctPin: String, onPinSuccess: () -> Unit) {
    var input by remember { mutableStateOf("") }
    var showError by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.9f))
            .padding(32.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text("Enter PIN", color = Color.White, fontSize = 24.sp)
            Spacer(Modifier.height(16.dp))

            Row {
                repeat(4) { index ->
                    Box(
                        modifier = Modifier
                            .padding(8.dp)
                            .size(20.dp)
                            .clip(CircleShape)
                            .background(
                                if (index < input.length) Color.White else Color.Gray
                            )
                    )
                }
            }

            Spacer(Modifier.height(16.dp))

            NumericKeypad(
                onKeyPress = { key ->
                    if (key == "<") {
                        input = input.dropLast(1)
                    } else {
                        if (input.length < 4) {
                            input += key
                            if (input.length == 4) {
                                if (input == correctPin) {
                                    onPinSuccess()
                                } else {
                                    showError = true
                                    input = ""
                                }
                            }
                        }
                    }
                }
            )

            if (showError) {
                Spacer(Modifier.height(12.dp))
                Text("Incorrect PIN", color = Color.Red)
            }
        }
    }
}

@Preview
@Composable
fun LockPinOverlayViewPreview() {
    LockPinOverlayView("1234",{})
}