package com.alekseeva.smallapp.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alekseeva.smallapp.model.UserProfile
import com.alekseeva.smallapp.repository.IUserRepository
import com.alekseeva.smallapp.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val repository: IUserRepository) : ViewModel() {
    private val _uiState = MutableStateFlow<State>(State.Loading)
    val uiState: StateFlow<State> = _uiState.asStateFlow()

    fun getUser() {
        viewModelScope.launch {
            try {
                repository.getUser().collect {
                    _uiState.value = if (it == null) State.NoUser else State.Success(it)
                }
            } catch (e: Exception) {
                _uiState.value = State.Error(e.message ?: "Error")
            }
        }
    }
}

sealed class State {
    data object Loading : State()
    data object NoUser : State()
    data class Success(val profile: UserProfile) : State()
    data class Error(val message: String) : State()
}