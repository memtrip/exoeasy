package com.memtrip.exoeasy.broadcast

import android.content.Context
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.memtrip.exoeasy.AudioResource
import rx.Observable
import rx.subjects.PublishSubject

/**
 * Copyright 2013-present memtrip LTD.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
class PlayBackStateUpdates<A : AudioResource>(
    private val audioResource: A
) {

    private val publishSubject: PublishSubject<PlayBackState> = PublishSubject.create<PlayBackState>()

    private val broadcastReceiver: PlayBackStateBroadcastReceiver = PlayBackStateBroadcastReceiver(publishSubject)

    fun playBackStateChanges(): Observable<PlayBackState> {
        return publishSubject.asObservable()
    }

    fun register(context: Context) {
        LocalBroadcastManager
                .getInstance(context)
                .registerReceiver(
                    broadcastReceiver,
                    PlayBackStateBroadcastReceiver.intentFilter(audioResource.url))
    }

    fun unregister(context: Context) {
        LocalBroadcastManager
                .getInstance(context)
                .unregisterReceiver(broadcastReceiver)
    }
}