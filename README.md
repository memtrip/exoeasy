exoeasy
======================
An abstraction layer over an instance of ExoPlayer running on a background service. Exoeasy provides a 
convenient mechanism for observing on playback updates and publishing playback commands. 
The service can be configured to provide `RemoteViews` for controlling playback from a Notification.

## Gradle dependency
```groovy
dependencies {
    implementation 'com.memtrip.exoeasy:exoeasy:1.0'
}
```

## Sample project
https://github.com/memtrip/ExoEasy/tree/master/sample

## Define your Audio resource
An AudioResource contains the `url` that will be consumed by ExoPlayer, the device `userAgent`, 
whether playback progress should be tracked periodically and any other extra properties 
that you decide to associate with the stream. 

```kotlin
data class HttpAudioResource(
    override val url: String,
    override val userAgent: String = "${BuildConfig.VERSION_NAME}/${BuildConfig.VERSION_CODE}",
    override val trackProgress: Boolean = true
) : AudioResource
```

## Provide extras via AudioResourceIntent
The `AudioResource` you define must have an associated `AudioResourceIntent`. 
The background service will provide ExoPlayer with; `url`, `userAgent` and `trackProgress`, these 
extras and any you define are provided by `into` and retrieved by `get`.

### Note:
If you choose to use notifications to control audio events, when the notifications is selected,
the extra properties you define are passed to the `onNewIntent(intent: Intent)` lifecycle method 
of the destination activity.

```kotlin
class HttpAudioResourceIntent : AudioResourceIntent<HttpAudioResource>() {

    override fun into(data: HttpAudioResource, notificationInfo: NotificationInfo, intent: Intent): Intent {
        intent.putExtra(EXTRA_PROPERTY, data.extraProperty)
        return super.into(data, notificationInfo, intent)
    }

    override fun get(intent: Intent): HttpAudioResource {
        return HttpAudioResource(
            intent.getStringExtra(HTTP_AUDIO_STREAM_URL),
            intent.getStringExtra(HTTP_AUDIO_STREAM_USER_AGENT),
            intent.getBooleanExtra(HTTP_AUDIO_STREAM_TRACK_PROGRESS, true),
            intent.getStringExtra(EXTRA_PROPERTY)
        )
    }

    companion object {
        const val EXTRA_PROPERTY = "EXTRA_PROPERTY"
    }
}
```

## Configure the streaming service
The underlying streaming service is configured by creating a concrete implementation of `StreamingService`.
A concrete implementation of `AudioResourceIntent` is required and `NotificationConfig`, `AudioStateRemoteView`,
and `KClass<out Activity>` can be provided to configure the playback notification.

```kotlin
class AudioStreamingService : StreamingService<HttpAudioResource>() {

    override fun audioResourceIntent(): HttpAudioResourceIntent = HttpAudioResourceIntent()

    override fun notificationConfig(): NotificationConfig {
        return NotificationConfig(
            false)
    }

    override fun playBackStateRemoteView(
        destination: Destination<HttpAudioResource>
    ): PlayBackStateRemoteView<HttpAudioResource> {
        return PlayPauseStopRemoteView(this, destination)
    }

    override fun activityDestination(): KClass<out Activity> {
        return AudioPlayingActivity::class
    }
}
```

## Send playback events to exoplayer with `AudioStreamController`

```kotlin
val audioResource = HttpAudioResource(
    "https://s3.eu-west-2.amazonaws.com/rewindit-audio/Rewind+It+Really+Nice+Trips+%2311+by+Jason+Hogan+%2819-07-18%29.mp3",
    "extra property!")

val audioStreamController = AudioStreamController(
    audioResource,
    HttpAudioResourceIntent(),
    NotificationInfo("Jason Hogan", "This is it!", null),
    AudioStreamingService::class,
    this)
```

```kotlin
audioStreamController.play()
audioStreamController.pause()
audioStreamController.stop()
audioStreamController.seek(progress)

// request an event for the latest playback state
audioStreamController.tickle()
```

## Receive playback events with PlayBackStateUpdates

```kotlin
private val playBackStateUpdates = PlayBackStateUpdates()
```

The playback state updates are propagated via a LocalBroadcastReceiver, you must register for updates
in `onStart` and unregister in `onStop`
```kotlin
playBackStateUpdates = PlayBackStateUpdates(audioResource)

override fun onStart() {
    super.onStart()
    playBackStateUpdates.register(this, audioResource.url)
    audioStreamController.tickle()
}

override fun onStop() {
    super.onStop()
    playBackStateUpdates.unregister(this)
}
```

Once the PlayBackStateUpdates is registered, it is possible to observe on events. Events are emitted
as the `PlayBackState` sealed class.

```kotlin
playBackStateUpdates.playBackStateChanges().subscribe {
    when (playBackState) {
        PlayBackState.Buffering -> {
            // buffering new data
        }
        PlayBackState.Play -> {
            // started playing
        }
        PlayBackState.Pause -> {
            // paused
        }
        PlayBackState.Stop -> {
            // playback has stopped
        }
        PlayBackState.Completed -> {
            // playback has completed
        }
        is PlayBackState.Progress -> {
            // playBackState.percentage => the playback percentage complete
            // playBackState.duration => the total duration of the playback
            // playBackState.currentPosition => the current position in the playback
        }
        is PlayBackState.BufferingError -> {
            // playBackState.throwable => the exception that caused the error
            // a playback error occurred
        }
    }
}
```