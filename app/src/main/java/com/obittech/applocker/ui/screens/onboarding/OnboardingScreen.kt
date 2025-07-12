package com.obittech.applocker.ui.screens.onboarding


import android.app.Activity
import android.content.Intent
import android.provider.Settings
import android.util.Log
import androidx.activity.compose.LocalActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.core.VisibilityThreshold
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.RocketLaunch
import androidx.compose.material.icons.filled.Security
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.obittech.applocker.services.AppLockAccessibilityService
import kotlinx.coroutines.launch
import kotlin.jvm.java
import androidx.core.net.toUri
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.obittech.applocker.ui.screens.components.CircleStepProgressBar
import com.obittech.applocker.ui.screens.components.CircleStepStatus
import com.obittech.applocker.utils.AccessibilityChecker.allowAccessibilityServiceIntent
import com.obittech.applocker.utils.AccessibilityChecker.isAccessibilityServiceEnabled
import com.obittech.applocker.utils.AnimatedPagerHost
import com.obittech.applocker.utils.ErrorBanner
import com.obittech.applocker.utils.OverlayPermissionChecker.isOverlayPermissionGranted
import com.obittech.applocker.utils.OverlayPermissionChecker.requestOverlayPermission
import com.obittech.applocker.utils.StepAnimProgressBar
import dagger.hilt.android.lifecycle.HiltViewModel

@Composable
fun OnboardingScreen(
    onFinished: () -> Unit,
    viewModel: OnboardingViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val stepIndex by viewModel.stepIndex
    val shouldShowAllow by viewModel.shouldShowAllow
    val showError by viewModel.permissionError
    val errorText by viewModel.errorMessage
    val isNextEnabled by viewModel.isNextButtonEnabled
    // Launch permission evaluator whenever step changes

    ErrorBanner(
        message = errorText,
        isVisible = showError,
        actionText = null,
        onActionClicked = {

        }
    )

    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(Unit) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                viewModel.evaluateStepPermissions()
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    val steps = listOf<@Composable () -> Unit>(
        {
            PermissionStep(
                title = "Accessibility Service",
                description = "Accessibility Control Permission is required to monitor which app is opened.",
//                checkGranted = {
//                    isAccessibilityServiceEnabled(context, AppLockAccessibilityService::class.java)
//                },
//                openSettingsIntent = {
//                    allowAccessibilityServiceIntent(context)
//                },
//                onGranted = {
//                    viewModel.markStepCompleted(viewModel.stepIndex.value)
//                    viewModel.goToNextStep() },
            )
        },  //Accessibility Permission
        {
            PermissionStep(
                title = "Draw Over Other Apps",
                description = "Draw Over other Apps Permission is required to show the lock screen over other apps.",
//                checkGranted = { isOverlayPermissionGranted(context) },
//                openSettingsIntent = {
//                    requestOverlayPermission(context)
//                },
//                onGranted = {
//                    viewModel.markStepCompleted(viewModel.stepIndex.value)
//                    viewModel.goToNextStep()},
            )
        },  //Overlay Permission
        {
            SetPasswordStep(
                viewModel,
                onComplete = {
                    viewModel.markStepCompleted(viewModel.stepIndex.value)
                    viewModel.goToNextStep()},
                onSkip = {
                    viewModel.markStepSkipped(viewModel.stepIndex.value)
                    viewModel.goToNextStep()},
                onBack = { viewModel.goToPreviousStep() }
            )
        },  //Set Password
        {
            PreferenceSettingsStep(
                onBack = { viewModel.goToPreviousStep() },
                onComplete = {
                    viewModel.markStepCompleted(viewModel.stepIndex.value)
                    viewModel.goToNextStep()},
                onSkip = {
                    viewModel.markStepSkipped(viewModel.stepIndex.value)
                    viewModel.goToNextStep()},
                setOverlay = { viewModel.setOverlayUsage(it) },
                setLockOnUnlock = { viewModel.setLockOnUnlock(it) }
            )
        },  //Settings
        {
            FinalStep (onFinish = {
                viewModel.onNextClicked (onFinished = onFinished)
            })

        }   //Finish and go to home
    )
    ErrorBanner(
        message = errorText,
        isVisible = showError
    )

    OnboardingStepContainer(
        viewModel = viewModel,
        stepContent = {
            AnimatedPagerHost(currentPage = viewModel.stepIndex.value) { index ->
                steps.getOrNull(index)?.invoke()
            }
        },
        nextLabel = if (stepIndex in 0..1 && shouldShowAllow) "Allow" else "Next",
        onNextClick = {

            when (stepIndex) {
                0 -> viewModel.onNextClicked(
                    requestPermissionLauncher = {
                        allowAccessibilityServiceIntent(context)
                    },
                    onFinished = onFinished
                )

                1 -> viewModel.onNextClicked(
                    requestPermissionLauncher = {
                        requestOverlayPermission(context)
                    },
                    onFinished = onFinished
                )

                else -> viewModel.onNextClicked(onFinished = onFinished)
            }
        }
    )
}
@Preview
@Composable
fun OnboardingScreenPreview() {
    OnboardingScreen({})
}

@Composable
fun OnboardingStepContainer(
    viewModel: OnboardingViewModel,
    stepContent: @Composable () -> Unit,
    nextLabel: String? = null,
    onNextClick: (() -> Unit)
) {
    val stepIndex by viewModel.stepIndex
    val statuses = viewModel.stepStatuses
    val labels = viewModel.stepLabels

    val isLastStep = stepIndex == labels.lastIndex
    val showSkip = stepIndex != labels.lastIndex

    Scaffold (modifier = Modifier
        .fillMaxSize()){ paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(paddingValues)
        ) {
            Spacer(modifier = Modifier.height(24.dp))

            CircleStepProgressBar(
                currentStep = stepIndex,
                stepStatuses = statuses,
//                stepLabels = labels
                stepLabels = null,
                onStepClick = { index ->
                    val allowed = statuses[index] != CircleStepStatus.PENDING || index == stepIndex
                    if (allowed) {
                        viewModel.jumpToStep(index)
                    }
                }
            )

            Spacer(modifier = Modifier.height(24.dp))

            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                stepContent()
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (showSkip) {
                    TextButton(onClick = {
                        when(stepIndex){
                            0-> viewModel.triggerPermissionError(ErrorType.Accessibility)
                            1-> viewModel.triggerPermissionError(ErrorType.Overlay)
                            else -> {}
                        }
                        viewModel.markStepSkipped(stepIndex)
                        viewModel.goToNextStep()
                    }) {
                        Text("Skip")
                    }
                } else {
                    Spacer(modifier = Modifier.width(64.dp))
                }

                if (!isLastStep){
                    Button(
                        onClick = onNextClick) {
                        Text(if (isLastStep) "Let's Go 🚀" else nextLabel?: "Next") //pass stepActionButtonText in else case if using dynamic button labels
                    }
                }

            }
        }
    }
}


@Composable
fun PermissionStep(
    title: String,
    description: String,
//    checkGranted: () -> Boolean,
//    openSettingsIntent: () -> Unit,
//    onGranted: () -> Unit,
    modifier: Modifier = Modifier
) {
//    val isGranted = remember { mutableStateOf(checkGranted()) }
//    val currentOnGranted by rememberUpdatedState(onGranted)
//
//    val lifecycleOwner = LocalLifecycleOwner.current
//    DisposableEffect(Unit) {
//        val observer = LifecycleEventObserver { _, event ->
//            if (event == Lifecycle.Event.ON_RESUME) {
//                val granted = checkGranted()
//                isGranted.value = granted
//                if (granted) currentOnGranted()
//            }
//        }
//        lifecycleOwner.lifecycle.addObserver(observer)
//        onDispose { lifecycleOwner.lifecycle.removeObserver(observer) }
//    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {

        Column(
            modifier = Modifier
                .align(Alignment.Center)
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = Icons.Default.Security,
                contentDescription = null,
                modifier = Modifier.size(80.dp),
                tint = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(title, style = MaterialTheme.typography.headlineSmall)
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                description,
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(32.dp))

            /*Button(onClick = openSettingsIntent, enabled = !isGranted.value) {
                Text("Allow")
            }*/
        }
    }
}


@Composable
fun SetPasswordStep(
    viewModel: OnboardingViewModel,
    onComplete: () -> Unit,
    onSkip: () -> Unit,
    onBack: () -> Unit
) {
    val pin by viewModel.appPin

    Box(Modifier.fillMaxSize().padding(24.dp)) {

        Column(
            Modifier.align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Set App Password", style = MaterialTheme.typography.headlineSmall)
            Spacer(Modifier.height(8.dp))
            Text("Enter a PIN to secure your locked apps.")

            Spacer(Modifier.height(24.dp))
            OutlinedTextField(
                value = pin,
                onValueChange = {
                    if (it.length <= 6) viewModel.onAppPinEntered(it) },
                label = { Text("Enter PIN") },
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword)
            )
        }
    }
}

@Composable
fun PreferenceSettingsStep(
    onComplete: () -> Unit,
    onSkip: () -> Unit,
    onBack: () -> Unit,
    setOverlay: (Boolean) -> Unit,
    setLockOnUnlock: (Boolean) -> Unit
) {
    val useOverlay = remember { mutableStateOf(true) }
    val lockOnScreenOff = remember { mutableStateOf(true) }

    Box(Modifier.fillMaxSize().padding(24.dp)) {

        Column(
            Modifier.align(Alignment.Center),
            horizontalAlignment = Alignment.Start
        ) {
            Text("Choose PIN Entry Method", style = MaterialTheme.typography.headlineSmall)
            Spacer(Modifier.height(12.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                RadioButton(
                    selected = useOverlay.value,
                    onClick = {
                        useOverlay.value = !useOverlay.value
                        setOverlay(useOverlay.value)
                    }
                )
                Text("Use Overlay")
                Spacer(Modifier.width(16.dp))
                RadioButton(
                    selected = !useOverlay.value,
                    onClick = {
                        useOverlay.value = !useOverlay.value
                        setOverlay(useOverlay.value)
                    }
                )
                Text("Use Activity")
            }

            Spacer(Modifier.height(24.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(
                    checked = lockOnScreenOff.value,
                    onCheckedChange = {
                        lockOnScreenOff.value = it
                        setLockOnUnlock(lockOnScreenOff.value)
                    }
                )
                Text("Relock app on screen off")
            }

            Spacer(Modifier.height(32.dp))
//            Button(onClick = {
//                setOverlay(useOverlay.value)
//                setLockOnUnlock(lockOnScreenOff.value)
//                onComplete()
//            }) {
//                Text("Continue")
//            }
        }
    }
}

@Composable
fun FinalStep(onFinish: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = Icons.Default.CheckCircle,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(80.dp)
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Setup Complete!",
            style = MaterialTheme.typography.headlineSmall,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "You're all set to use App Locker.\nEverything's configured.",
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = onFinish,
            shape = RoundedCornerShape(50),
            contentPadding = PaddingValues(horizontal = 24.dp, vertical = 12.dp)
        ) {
            Icon(Icons.Default.RocketLaunch, contentDescription = "Go")
            Spacer(modifier = Modifier.width(8.dp))
            Text("Let's Go")
        }
    }
}

@Composable
fun FinalOnboardingStep(
    onFinish: () -> Unit
) {
    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = Icons.Default.CheckCircle,
            contentDescription = null,
            modifier = Modifier.size(80.dp),
            tint = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text("Setup Completed!", style = MaterialTheme.typography.headlineSmall)
        Spacer(modifier = Modifier.height(8.dp))
        Text("You're all set! Let’s dive into the app.", style = MaterialTheme.typography.bodyMedium)

        Spacer(modifier = Modifier.height(32.dp))

        Button(onClick = {
            scope.launch {
                onFinish()
            }
        }) {
            Icon(Icons.Default.RocketLaunch, contentDescription = null, modifier = Modifier.padding(end = 8.dp))
            Text("Let's Go")
        }
    }
}


@Composable
fun OnboardingTopSection(
    currentStep: Int,
    totalSteps: Int,
    stepStatuses: List<OnboardingStepStatus>,
    showBack: Boolean,
    onBack: () -> Unit,
    onSkip: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 24.dp, start = 16.dp, end = 16.dp)
    ) {
        if (showBack) {
            IconButton(
                onClick = onBack,
                modifier = Modifier.align(Alignment.CenterStart)
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBackIosNew,
                    contentDescription = "Back"
                )
            }
        }

        Log.d("OnboardingTopSection", "currentStep: $currentStep, totalSteps: $totalSteps")
        if (currentStep != (totalSteps-1)){
            TextButton(
                onClick = onSkip,
                modifier = Modifier.align(Alignment.CenterEnd)
            ) {
                Text("Skip")
            }
        }


        StepAnimProgressBar(
            currentStep = currentStep,
            stepStatuses = stepStatuses,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(top = 40.dp)
        )
    }
}


@Preview
@Composable
fun PermissionStepPreview(){
    PermissionStep("test",
        "test",
//        checkGranted = {false},
//        openSettingsIntent = {},
//        onGranted = {}
    )
}


