package com.example.chatapp

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import androidx.navigation.fragment.findNavController
import com.example.chatapp.databinding.FragmentFirstBinding

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
/**
 * Global Variables
 */

class FirstFragment : Fragment() {

    private var _binding: FragmentFirstBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentFirstBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val usernameInput = view.findViewById<EditText>(R.id.usernameInput)
        val roomSpinner = view.findViewById<Spinner>(R.id.roomSpinner)
        val roomsArray = arrayOf("General", "Tech", "Class")
        val adapter =
            ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, roomsArray)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        roomSpinner.adapter = adapter

        Log.d("CHAT", conf.SERVER_URL +
                " " +
                conf.ROOMS_ENDPOINT +
                " " +
                conf.MESSAGES_ENDPOINT)
        val rooms=DefaultRooms(roomsArray, "General")
        Log.d("CHAT", rooms.selectedRoom)
        val data= ThisUser(1, "Generic_User")
        Log.d("CHAT ","onViewCreated")
        Log.d("CHAT", roomSpinner.selectedItem.toString())


        Log.d("CHAT", usernameInput.text.toString() + " " + roomSpinner.selectedItem.toString())

        binding.joinButton.setOnClickListener {
            val username = binding.usernameInput.text.toString()
            data.USERID = 1
            data.name = usernameInput.text.toString()
            Log.d("CHAT", data.name)
            Log.d("CHAT", data.USERID.toString())
            val action = FirstFragmentDirections.actionFirstFragmentToSecondFragment(username, roomSpinner.selectedItem.toString())

            findNavController().navigate(action)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}