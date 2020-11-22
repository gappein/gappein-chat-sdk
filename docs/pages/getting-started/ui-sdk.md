# UI SDK

You can either build your own UI or use our UI SDK to create a beautiful chat experience.
To use our UI SDK add the following to your app's `build.gradle` -

```groovy
implementation "com.gappein.sdk:gappein-ui:1.0.0-beta3"
```

To open the Channel List Fragment add the following code -

```kotlin
private fun addChannelListFragment() {
    val fragmentTransaction = supportFragmentManager.beginTransaction().apply {
        add(R.id.container, ChannelListFragment.newInstance())
    }
    fragmentTransaction.commit()
}
```

To open the Chat between two users, use the following code -

```kotlin
startActivity(MessageListActivity.buildIntent(this, "channel_id", User))
```

?> Here `User` is the recipient user of the device.
