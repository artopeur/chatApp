package com.example.chatapp


// Use 'val' for properties that don't change, 'var' for those that do.
class ThisUser(var USERID: Int, var name: String) {
    // Properties can be initialized directly or in an init block.
    var thisUserMessage: String = "$name entered the chat"
}

class MessageHistory {
    private val _messages = mutableListOf<String>()
    val messages: List<String>
        get() = _messages

    fun add(sender: String, message: String) {
        _messages.add("$sender: $message")
    }
}

class DefaultRooms(
    val rooms: Array<String>,
    var selectedRoom: String
)

// Using a 'const' for compile-time constants is a good practice.
object conf {
    //const val SERVER_URL = "http://192.168.1.137:3112" // Use 10.0.2.2 to connect to localhost from Android emulator
    const val SERVER_URL = "https://chatapp.ydns.eu:3000" // the main chatapp url.
    const val ROOMS_ENDPOINT = "/rooms"
    const val MESSAGES_ENDPOINT = "/messages"
}
