# User Presence (Online/Offline) Status

## Set user as online/offline

To set user online status use,

```kotlin
ChatClient.getInstance().setUserOnline("my_user_token", status = true/false)
```

## Get user presence status

```kotlin
ChatClient.getInstance().isUserOnline("user_token") { isOnline, lastOnlineAt ->
    if (isOnline) {
        // lastOnlineAt will be empty
    } else {
        // lastOnlineAt will return time object
    }
}
```
