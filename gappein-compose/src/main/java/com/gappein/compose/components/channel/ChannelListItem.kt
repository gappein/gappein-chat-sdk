package com.gappein.compose.components.channel

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gappein.sdk.model.ChannelListData

@Composable
fun ChannelListItem(channel: ChannelListData) {
    Row(modifier = Modifier.padding(16.dp)) {
        Column(modifier = Modifier.padding(8.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                UserName(channel.user.name)
                LastMessage(lastMessage = channel.lastMessage.message)
            }
        }
    }
}

@Composable
private fun UserName(userName: String) {
    BasicText(
        text = userName,
        style = TextStyle(
            fontSize = 14.sp,
            color = Color.Black,
            fontWeight = FontWeight.Black
        )
    )
}

@Composable
private fun LastMessage(lastMessage: String) {
    BasicText(
        text = lastMessage,
        style = TextStyle(
            fontSize = 12.sp,
            color = Color.LightGray,
            fontWeight = FontWeight.Normal
        )
    )
}