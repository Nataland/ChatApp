package com.nataland.chatapp.meow

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nataland.chatapp.network.ChatRepository
import com.nataland.chatapp.network.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

enum class ChatArrangement {
    Start, End
}

sealed class Message(open val content: String, val arrangement: ChatArrangement) {
    data class Self(override val content: String) : Message(content, arrangement = ChatArrangement.End)
    data class Server(override val content: String) : Message(content, arrangement = ChatArrangement.Start)
    data class Error(override val content: String) : Message(content, arrangement = ChatArrangement.Start)
    // Todo: implement loading
}

data class HomeScreenState(
    val messages: List<Message> = emptyList(),
)

@HiltViewModel
class MeowGPTViewModel @Inject constructor(
    private val chatRepository: ChatRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(HomeScreenState())
    val uiState = _uiState.asStateFlow()

    fun sendMessage(input: String) {
        appendMessage(Message.Self(input))
        viewModelScope.launch {
            when (val response = chatRepository.message(input)) {
                is Result.Error -> appendMessage(
                    Message.Error(
                        response.exception.message.orEmpty()
                    )
                )

                is Result.Success -> appendMessage(
                    Message.Server(
                        response.data.message?.body.toString()
                    )
                )
            }
        }
    }

    private fun appendMessage(newMessage: Message) {
        _uiState.value = _uiState.value.copy(
            messages = _uiState.value.messages + newMessage
        )
    }
}