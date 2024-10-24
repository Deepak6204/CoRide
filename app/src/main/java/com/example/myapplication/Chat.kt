package com.example.myapplication.ui.chat

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import com.example.myapplication.R
import com.example.myapplication.models.Message
import com.example.myapplication.utils.FirebaseChatHelper.sendMessage
import com.example.myapplication.ui.components.MessageBubble
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.launch

@Composable
fun ChatScreen(chatId: String, currentUserId: String) {
    val database = FirebaseDatabase.getInstance().reference
    var messageText by remember { mutableStateOf("") }
    val messages = remember { mutableStateListOf<Message>() }

    // Create LazyListState for controlling scroll behavior
    val lazyListState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope() // Coroutine scope for scrolling

    // Fetch chat messages and listen for new ones
    LaunchedEffect(chatId) {
        database.child("chats").child(chatId).child("messages")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    messages.clear()
                    snapshot.children.forEach { data ->
                        val message = data.getValue(Message::class.java)
                        if (message != null) {
                            messages.add(message)
                        }
                    }
                    // Scroll to the last message whenever the messages list is updated
                    if (messages.isNotEmpty()) {
                        coroutineScope.launch {
                            lazyListState.animateScrollToItem(messages.size - 1)
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.e("ChatScreen", "Error fetching messages: ${error.message}")
                }
            })
    }

    Column(modifier = Modifier.fillMaxSize()) {
        // Chat Messages List with LazyColumn
        LazyColumn(
            state = lazyListState,
            modifier = Modifier.weight(1f),
            contentPadding = PaddingValues(16.dp)
        ) {
            items(messages) { message ->
                MessageBubble(message = message, isCurrentUser = message.senderId == currentUserId)
            }
        }

        // Message input and send button
        Row(modifier = Modifier.padding(16.dp)) {
            messageBox(messageText = messageText, onValueChange = {messageText = it})
            IconButton(onClick = {
                if (messageText.isNotEmpty()) {
                    sendMessage(chatId, currentUserId, messageText)
                    messageText = ""
                    // Scroll to the bottom when a new message is sent
                    coroutineScope.launch {
                        lazyListState.animateScrollToItem(messages.size - 1)
                    }
                }
            }) {
                Image(painter = painterResource(R.drawable.icon), contentDescription = "Send")

            }
        }
    }
}


@Composable
fun messageBox(messageText: String, onValueChange: (String) -> Unit) {


    TextField(
        value = messageText,
        onValueChange = onValueChange,
        placeholder = {
            Text("Message")
        },
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Color.White,
            focusedPlaceholderColor = colorResource(R.color.light_gray),
            unfocusedPlaceholderColor = Color.Black,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            focusedTextColor = colorResource(R.color.black),
            unfocusedTextColor = Color.Black,
            unfocusedContainerColor = Color.White
        ),
        shape = RoundedCornerShape(30.dp),
        modifier = Modifier
            .border(
                1.dp,
                color = colorResource(R.color.light_green),
                shape = RoundedCornerShape(30.dp)
            ),

        textStyle = TextStyle(color = colorResource(R.color.black)),
    )
}
