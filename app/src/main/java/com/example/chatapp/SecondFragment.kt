package com.example.chatapp

import android.R.attr.data
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.navArgs
import com.example.chatapp.databinding.FragmentSecondBinding

var room = "";
class SecondFragment : Fragment() {

    private var _binding: FragmentSecondBinding? = null
    private val binding get() = _binding!!
    private val args: SecondFragmentArgs by navArgs()
    //private val messages = mutableListOf<String>()
    private val viewModel: SecondViewModel by viewModels()
    private lateinit var adapter: ArrayAdapter<String>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSecondBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        SocketManager.attachViewModel(viewModel)
        SocketManager.connect(conf.SERVER_URL)
        Log.d("Chat", "On view creater ${args.name}, ${args.room}")
        adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_list_item_1,
            mutableListOf()
        )


        when(args.room) {
            "General" -> room = "room1"
            "Tech" -> room = "room2"
            "Class" -> room = "room3"
        }
        binding.roomNameText.text = "Room: ${args.room}"
        binding.usernameText.text = "User: ${args.name}"
        // 3. Find the ListView and set its adapter.
        // It's better to use the binding object if your ListView has an id in the layout.
        // Assuming your ListView has the id `messageList` in fragment_second.xml:
        binding.messageList.adapter = adapter

        SocketManager.setUsername(args.name)
        SocketManager.joinRoom(room)

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.incomingMessage.collect { messages ->
                //adapter.clear()
                adapter.addAll(messages)
                adapter.notifyDataSetChanged()
                binding.messageList.smoothScrollToPosition(adapter.count - 1)
            }
        }

        binding.sendButton.setOnClickListener {
            val messageInput = view.findViewById<EditText>(R.id.messageInput)
            Log.d("CHAT", messageInput.text.toString())
            //addMessageToView(messageInput.text.toString())
            sendMessage(messageInput.text.toString())
            messageInput.setText("")
        }
    }
    fun sendMessage(msg: String) {
        Log.d("CHAT", "sending: $msg")
        SocketManager.sendMessage(room, msg)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        SocketManager.disconnect()
        _binding = null
    }
}
