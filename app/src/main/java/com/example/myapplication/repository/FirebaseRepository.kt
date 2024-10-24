package com.example.myapplication.repository

import android.util.Log
import com.example.myapplication.models.Message
import com.google.firebase.database.*

object FirebaseRepository {

    private val database = FirebaseDatabase.getInstance().reference

    // Post chat message to Firebase
    fun sendMessage(chatId: String, senderId: String, messageText: String, onSuccess: () -> Unit, onFailure: (String) -> Unit) {
        val messageId = database.push().key ?: return
        val message = Message(senderId, messageText, System.currentTimeMillis())

        database.child("chats").child(chatId).child("messages").child(messageId).setValue(message)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { onFailure(it.message ?: "Error sending message") }
    }

    // Create chat if it doesn't exist
    fun createChatIfNotExists(userId1: String, userId2: String, onChatCreated: (String) -> Unit, onError: (String) -> Unit) {
        val query = database.child("chats").orderByChild("users/$userId1").equalTo(true)

        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                var chatId: String? = null
                for (child in snapshot.children) {
                    if (child.child("users/$userId2").exists()) {
                        chatId = child.key
                        break
                    }
                }

                if (chatId == null) {
                    chatId = database.push().key ?: return
                    val users = mapOf(userId1 to true, userId2 to true)
                    database.child("chats").child(chatId).child("users").setValue(users)
                }

                onChatCreated(chatId ?: "")
            }

            override fun onCancelled(error: DatabaseError) {
                onError(error.message)
            }
        })
    }
}
