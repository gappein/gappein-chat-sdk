# User Channel Operation

## Get all user channels

To get all the channels of current user,

```kotlin
ChatClient.getInstance().getUserChannels { channels ->
    // Handles the channel like displaying in the RecyclerView
}
```

## Get the participant user of channel

To get the participant user of channel use,

```kotlin
ChatClient.getInstance().getChannelRecipientUser(channelId) { user ->
    // Get the user object
}
```
