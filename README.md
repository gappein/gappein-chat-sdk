# Gappein
<img src=https://github.com/Gappein/Gappein-Chat-SDK/blob/development/art/banner-chat-sdk.png >

Gappein is a new Chat SDK in town!

A plug and play modular toolkit for integrating the Chat feature on top of Firebase!

# Table of contents

- [Installation](#installation)
- [Initialization](#initialization)
- [UI](#UI)
- [Developer Notes](#developer-notes)
- [Sample App](#sample-app)
- [Contributing](#contributing)
- [License](#license)

# Installation
[(Back to top)](#table-of-contents)

Add the dependencies to the `build.gradle`

```groovy
//TODO final dependency name will depend on bintray setup

implementation "com.gappein.sdk:gappein-sdk:1.0.0-alpha-1" 
implementation "com.gappein.sdk:gappein-ui:1.0.0-alpha1"
implementation "com.google.firebase:firebase-core:17.5.0"
implementation "com.google.firebase:firebase-storage:19.1.1"
implementation "com.google.firebase:firebase-firestore:21.5.0"
```

# Initialization
[(Back to top)](#table-of-contents)

Initialize the Gappein SDK with one line.

```kotlin
//TODO make this better
Gappein.Builder().build()
```

Set the `User` by passing information about the currently logged in user

```kotlin
Gappein.getInstance().setUser(
            User(
                token = "user_token",
                profileImageUrl = "link_to_image",
                name = "user_name" 
            ), token = "user_token", {
            //Handle onSuccess
            }, {
            //Handle onError
            }
        )
```


# UI
[(Back to top)](#table-of-contents)

You can either build your own UI or use our UI SDK to create a beautiful chat experience.

To open the Channel List Fragment add the following code -

```kotlin
//TODO make this better
private fun addChannelListFragment() {
        val fm = supportFragmentManager
        val ft = fm.beginTransaction()
        ft.add(R.id.container,ChannelListFragment.newInstance())
        ft.commit()
    }
```

To open the Chat between two users, use the following code -

```kotlin
startActivity(MessageListActivity.buildIntent(this,"channel_id",User))
```

where `User` is the recipient user of the device.


# Developer Notes
[(Back to top)](#table-of-contents)

//TODO add this for explaining nuances of the SDK or give the link to Wiki

# Sample App
[(Back to top)](#table-of-contents)

Find the [Demo App here](https://github.com/Gappein/Gappein-Chat-SDK/tree/development/app)

# Contributing
[(Back to top)](#table-of-contents)

Your contributions are always welcome! Please have a look at the [contribution guidelines](CONTRIBUTING.md) first. :tada:

# License
[(Back to top)](#table-of-contents)

The MIT License (MIT) 2020. Please have a look at the [LICENSE.md](LICENSE.md) for more details.
