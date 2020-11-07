# Send Messages 📨

## Send a text message 📃

Send a text message to a user in the channel using,

```kotlin
ChatClient.getInstance().sendMessage(message, "participant_user_token_of_the_Channel", {
    // Message sent successfully
}, {exception->
    // Handle the exception here
})
```

## Send a Media 📷

Send a image from _Camera/Gallery_ or even _GIFs_ from _Giphy_ like,

```kotlin
ChatClient.getInstance().sendMessage("file_uri", "participant_user_token_of_the_Channel", {
    // Message sent successfully
}, { progress ->
    // Handle the progress of file being upload
}, { exception ->
    // Handle the exception here
})
```

?> **Tip :** Before uploading the File, make sure you compress it.
