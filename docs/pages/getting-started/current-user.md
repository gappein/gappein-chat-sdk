# Current User 👤

## Setting a user

Now to set the user in your application, we do store the User in the memory itself. To set the User we use,

```kotlin
Gappein.getInstance().setUser(
    currentUser,
    token = currentUser.token,
    onSuccess = {
        // Handle the Success
    },
    onError = {
        // Handle the exception
    }
)
```

and `currentUser` here is,

```kotlin
val currentUser = User(
    token = userToken,
    profileImageUrl = userUrl,
    name = userName,
    createdAt = currentDate
)
```

## Getting current user

Get instance of Current User
To get the current user we can use,

```kotlin
Gappein.getInstance.getUser()
```