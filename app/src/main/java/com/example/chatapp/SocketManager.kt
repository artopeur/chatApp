package com.example.chatapp
import android.util.Log
import io.socket.client.IO
import io.socket.client.Socket
import org.json.JSONObject
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
object SocketManager {

    private lateinit var socket: Socket
    private var viewModel: SecondViewModel? = null

    fun attachViewModel(vm: SecondViewModel) {
        viewModel = vm
    }

    fun connect(url: String) {
        socket = IO.socket(url)

        socket.on("message") { args ->
            val json = args[0] as JSONObject
            val sender = json.getString("from")
            val text = json.getString("text")

            viewModel?.addMessage(sender, text)
        }

        socket.connect()
    }

    fun sendMessage(roomId: String, text: String) {
        val payload = JSONObject()
        payload.put("roomId", roomId)
        payload.put("text", text)
        socket.emit("message", payload)
    }

    fun setUsername(name: String) {
        socket.emit("set-username", name)
    }

    fun joinRoom(roomId: String) {
        socket.emit("join-room", roomId)
    }

    fun disconnect() {
        socket.disconnect()
        socket.off()
    }
}
