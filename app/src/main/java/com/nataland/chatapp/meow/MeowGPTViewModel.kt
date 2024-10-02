package com.nataland.chatapp.meow

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nataland.chatapp.network.ChatRepository
import com.nataland.chatapp.network.Result
import com.nataland.chatapp.picker.Cat
import com.nataland.chatapp.picker.CatInfo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


enum class ChatArrangement {
    Start, End
}

sealed class Message(open val content: String, val arrangement: ChatArrangement) {
    data class Self(override val content: String) :
        Message(content, arrangement = ChatArrangement.End)

    data class Server(override val content: String) :
        Message(content, arrangement = ChatArrangement.Start)

    data class Error(override val content: String) :
        Message(content, arrangement = ChatArrangement.Start)
    // Todo: implement loading
}

data class MeowGPTState(
    val messages: List<Message> = emptyList(),
    val cat: Cat = Cat.Bobby,
    val catAction: String = ""
)

@HiltViewModel
class MeowGPTViewModel @Inject constructor(
    private val chatRepository: ChatRepository,
) : ViewModel() {
    private val _uiState = MutableStateFlow(MeowGPTState())
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

    fun setCat(cat: Cat) {
        if (cat != _uiState.value.cat) {
            _uiState.value = _uiState.value.copy(
                cat = cat,
                catAction = CatInfo.getRandomAction(cat.name),
                messages = emptyList()
            )
        }
    }

    private fun appendMessage(newMessage: Message) {
        _uiState.value = _uiState.value.copy(
            messages = _uiState.value.messages + newMessage
        )
    }
}