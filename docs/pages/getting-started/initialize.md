# Initialize the SDK

To initialize the Chat SDK, you have two different ways depending upon the features you want.

## With GIF Support

If you want to have an integrated Gif support, initialize Gappein in you application class like,

```kotlin
Gappein.initialize(context, "GiphyApiKey")
```

?> You can get API key of Giphy from [here](https://developers.giphy.com/).

## Without GIF Support

If you don't want to have Gif support, initialize Gappein in you application class like,

```kotlin
Gappein.initialize(context)
```
