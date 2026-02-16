package com.example.chatapp

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow

class SecondViewModel : ViewModel() {

    private val messageHistory = MessageHistory()

    private val _incomingMessage = MutableSharedFlow<String>(
        replay = 1,
        extraBufferCapacity = 64
    )
    val incomingMessage: SharedFlow<String> = _incomingMessage

    fun addMessage(sender: String, message: String) {
        val formatted = "$sender: $message"
        messageHistory.add(sender, message)
        _incomingMessage.tryEmit(formatted)
    }
}