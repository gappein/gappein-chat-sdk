# Gappein

Gappein is a new Chat SDK in town, a plug and play toolkit for integrating Chat feature on top of Firebase!

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
implementation "com.gappein.sdk:gappein-sdk:1.0.0" 
implementation "com.gappein.sdk:gappein-ui:1.0.0"
implementation "com.google.firebase:firebase-core:17.5.0"
implementation "com.google.firebase:firebase-storage:19.1.1"
implementation "com.google.firebase:firebase-firestore:21.5.0"
```

# Initialization
[(Back to top)](#table-of-contents)

Initialize the Gappein SDK with our builder object and set the `User` which would be the user of the current device.

```kotlin
//TODO make this better
Gappein.Builder().build()

Gappein.getInstance().setUser(
            User(
                token = "1234567890",
                createdAt = Date(),
                profileImageUrl = "1234567890",
                name = "Himanshu"
            ), token = "1234567890", {}, {}
        )
```

# UI
[(Back to top)](#table-of-contents)

You can either build your own UI or use our UI SDK to create a beautiful chat experience.
To open the Chat List Fragment add the following code -

```kotlin
//TODO make this better
private fun addFragment() {
        val fm = supportFragmentManager
        val ft = fm.beginTransaction()
        ft.add(R.id.container,ChannelListFragment.newInstance())
        ft.commit()
    }
```

To open the Chat between two users, use the following code -

```kotlin
startActivity(MessageListActivity.buildIntent(this,"[0987654321, 1234567890]",User))
```

where `User` is the current user of the device.

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
