# User Status

## Set status

If you want to set text status of a user use,

```kotlin
ChatClient.getInstance().setUserStatus("text_Status", {
    // User Status set successfully
}, { exception ->
    // Handle the exception
})
```

## Retrieving status of a user

If you want to get the status of a user,

```kotlin
ChatClient.getInstance().getUserStatus("user_token", {
    // User Status in String
}, { exception ->
    // Handle the exception
})
```

!> Tip: If you don't pass `userToken` then the status would be returned for the _**current user**_.