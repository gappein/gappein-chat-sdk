package com.gappein.compose.component.channel

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gappein.coroutine.sdk.model.ChannelListData
import com.gappein.coroutine.sdk.model.Message
import com.gappein.coroutine.sdk.model.User

@Composable
fun ChannelItem(channel: ChannelListData) {

    Row(
        modifier = Modifier
            .padding(horizontal = 8.dp, vertical = 8.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        //Avatar
        Column(modifier = Modifier.padding(start = 8.dp)) {
            Text(
                text = channel.user.name,
                style = TextStyle(fontWeight = FontWeight.Bold),
                fontSize = 18.sp,
            )

            val lastMessageText = channel.lastMessage.message

            Text(
                text = lastMessageText,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        }
    }

}

@Preview
@Composable
fun setupPreview(){
    ChannelItem(ChannelListData("", User(), Message()))
}