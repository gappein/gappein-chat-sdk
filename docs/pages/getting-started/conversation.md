# Conversation with the users 💬

## Create conversation (Two users)

To create conversation between two users use,

```kotlin
ChatClient.getInstance().openOrCreateChannel("participant_user_token") { channelId ->
    // This is channel id of the conversation channel between two users
}
```

## Getting messages of a channel

Get messages of a current channel,

```kotlin
ChatClient.getInstance().getMessages(channelId) { messages ->
    // This is list of messages of the channel between two users
}
```

## Get the last message

To get last message of a current channel,

```kotlin
ChatClient.getInstance().getLastMessageFromChannel(channelId) { message, senderUser ->
    // This is the last message of the channel
}
```
