# Gappein
<img src=https://github.com/Gappein/Gappein-Chat-SDK/blob/main/art/banner-chat-sdk.png >

[ ![Download Chat](https://api.bintray.com/packages/gappein/Gappein/Gappein-Chat-SDK/images/download.svg) ](https://bintray.com/gappein/Gappein/Gappein-Chat-SDK/_latestVersion)
[ ![Download UI](https://api.bintray.com/packages/gappein/Gappein/Gappein-UI-SDK/images/download.svg) ](https://bintray.com/gappein/Gappein/Gappein-UI-SDK/_latestVersion)

Gappein is a new Chat SDK in town!

A plug and play modular toolkit for integrating the Chat feature on top of Firebase!

# Table of contents

- [Get Started](#installation)
- [Installation](#installation)
- [Initialization](#initialization)
- [UI](#ui)
- [Developer Notes](#developer-notes)
- [Sample App](#sample-app)
- [Contributing](#contributing)
- [License](#license)

# Get Started
[(Back to top)](#table-of-contents)
To get started you need to first create a Firebase project for your app and add the `google-service.json` file in your project.

# Installation
[(Back to top)](#table-of-contents)

Add the dependencies to the `build.gradle`

```groovy
implementation "com.gappein.sdk:gappein-sdk:1.0.0-beta1"
```

# Initialization
[(Back to top)](#table-of-contents)

Initialize the Gappein SDK with one line.

```kotlin
Gappein.initialize(context)
```
If you want to Gif integration Initialize the Gappein SDK with one line. Get the API Key from [Giphy](https://developers.giphy.com/) and use it like,

```kotlin
Gappein.initialize(context,"API_KEY")
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
To use our UI SDK add the following to your app's `build.gradle` -

```groovy
implementation "com.gappein.sdk:gappein-ui:1.0.0-beta1"
```

To open the Channel List Fragment add the following code -

```kotlin
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

This SDK is in alpha release, we would love to hear your feedback. If you face any issues please let us know [here](https://github.com/Gappein/Gappein-Chat-SDK/issues)

# Sample App
[(Back to top)](#table-of-contents)

Find the [Demo App here](https://github.com/Gappein/Gappein-Chat-SDK/tree/main/app)

# Contributing
[(Back to top)](#table-of-contents)

Your contributions are always welcome! Please have a look at the [contribution guidelines](CONTRIBUTING.md) first. :tada:

# License
[(Back to top)](#table-of-contents)

The MIT License (MIT) 2020. Please have a look at the [LICENSE.md](LICENSE.md) for more details.
