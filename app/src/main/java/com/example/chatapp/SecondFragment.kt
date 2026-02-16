package com.example.chatapp

import android.R.attr.data
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.example.chatapp.databinding.FragmentSecondBinding



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

        adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_list_item_1,
            mutableListOf()
        )

        // 3. Find the ListView and set its adapter.
        // It's better to use the binding object if your ListView has an id in the layout.
        // Assuming your ListView has the id `messageList` in fragment_second.xml:
        binding.messageList.adapter = adapter

        SocketManager.setUsername(args.name)
        SocketManager.joinRoom("room1")

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.incomingMessage.collect { messages ->
                //adapter.clear()
                adapter.addAll(messages)
                adapter.notifyDataSetChanged()
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

    /*fun addMessageToView(msg: String) {
        Log.d("CHAT", "adding: $msg" )
        adapter.add(msg)
        adapter.notifyDataSetChanged()
    }
    */

    fun sendMessage(msg: String) {
        Log.d("CHAT", "sending: $msg")
        SocketManager.sendMessage("room1", msg)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        SocketManager.disconnect()
        _binding = null
    }
}
