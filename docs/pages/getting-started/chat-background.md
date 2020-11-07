# Chat Background 🖼️

## Set the background

To set the chat background of a conversation which will be visible to both the user use,

```kotlin
ChatClient.getInstance().setChanBackground("channelId", "background_uri", {
    //Background set successfully
}, {
    // File upload percentage
}, { exception->
    //Handle exception
})
```

## Getting background

To get the chat background for that specific channel use,

```kotlin
ChatClient.getInstance().getChatBackground("channe_id") { urlOfBackground->
    // Set this to the background of the view
}
```
