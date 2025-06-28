package com.obittech.applocker.ui.screens.lockScreen

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.obittech.applocker.data.LockedAppRepository
import com.obittech.applocker.data.source.LockedAppEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LockScreenViewModel @Inject constructor(private val lockedAppRepository: LockedAppRepository):ViewModel() {


}