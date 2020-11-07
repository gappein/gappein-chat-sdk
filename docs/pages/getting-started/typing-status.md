# Typing Status ⌨️

## Set Typing status

If you want that participant gets to see your typing status, you also need to set your typing status in the `TextWatcher` of you `EditText` like,

```kotlin
ChatClient.getInstance().setTypingStatus(
    "channelId",
    "your_user_token",
    is_user_typing = true // Set this to `false` if you don't want to show if user is typing or not.
) {
    // Handle Success
}
```

## Getting Typing status

If you want to get the status of the participating user in the channel use,

```kotlin
ChatClient.getInstance().getTypingStatus("channelId", "participant_user_token") {
    // Handle the typing status
    // If user is not typing it will return "-"
}
```
