package com.alekseeva.smallapp.editprofile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alekseeva.smallapp.model.UserProfile
import com.alekseeva.smallapp.repository.IUserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditProfileViewModel @Inject constructor(private val repository: IUserRepository) : ViewModel() {
    private val _uiState = MutableStateFlow<EditProfileState>(EditProfileState.Loading)
    val uiState: StateFlow<EditProfileState> = _uiState.asStateFlow()

    fun saveUser(user: UserProfile) {
        viewModelScope.launch {
            val saved = repository.saveUser(user)
            _uiState.value = EditProfileState.Saved(saved)
        }
    }
    fun setToLoading(){
        _uiState.value = EditProfileState.Loading
    }
}

sealed class EditProfileState {
    data object Loading : EditProfileState()
    data class Saved(val saved: Boolean) : EditProfileState()
}