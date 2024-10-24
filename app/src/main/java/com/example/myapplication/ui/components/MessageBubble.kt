package com.example.myapplication.ui.components


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.R
import com.example.myapplication.models.Message

@Composable
fun MessageBubble(message: Message, isCurrentUser: Boolean) {
    val alignment = if (isCurrentUser) Alignment.End else Alignment.Start
    Box(
        contentAlignment = alignment(isCurrentUser),
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Text(
            text = message.text,
            color = if (isCurrentUser) Color.White else Color.Black,
            modifier = Modifier
                .clip(RoundedCornerShape(8.dp))
                .background(if (isCurrentUser) colorResource(R.color.light_green) else Color.LightGray)
                .padding(8.dp),
            fontSize = 17.sp
        )
    }
}

fun alignment(isCurrentUser: Boolean): Alignment {
    if(isCurrentUser)return Alignment.BottomEnd
    else return Alignment.BottomStart
}
