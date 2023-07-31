/*
 * Copyright  2019 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.tomaszrykala.recsandfx.core.domain.native

import android.util.Log
import com.tomaszrykala.recsandfx.core.datatype.EffectDescription
import com.tomaszrykala.recsandfx.core.datatype.NativeEffect

internal object NativeInterface {
    // Used to load the 'native-lib' library on application startup.
    val effectDescriptionMap: Map<String, EffectDescription>

    init {
        System.loadLibrary("native-lib")
        effectDescriptionMap = getEffects().associateBy { it.name }
        Log.d(TAG, effectDescriptionMap.toString())
    }

    // Functions/Members called by UI code
    // Adds effect at end of effect list
    fun addEffect(effect: NativeEffect) {
        val nativeId = convertEffectToId(effect)
        Log.d(TAG, "addEffect $nativeId.")
        addDefaultEffectNative(nativeId)
    }

    // Removes effect at index
    fun removeEffectAt(index: Int) {
        removeEffectNative(index)
    }

    // Signals params were updated at index
    fun updateParamsAt(effect: NativeEffect, index: Int) {
        val nativeId = convertEffectToId(effect)
        Log.d(TAG, "updateParamsAt $nativeId.")
        modifyEffectNative(nativeId, index, effect.paramValues)
    }

    // Rotates existing effect from index to another
    fun rotateEffectAt(from: Int, to: Int) {
        Log.d(TAG, String.format("Effect was rotated from %d to %d", from, to))
        rotateEffectNative(from, to)
    }

    fun enable(enable: Boolean) {
        Log.d(TAG, "Enabling effects: $enable")
        enablePassthroughNative(enable)
    }

    external fun startAudioRecorder()
    external fun stopAudioRecorder()
    external fun writeFile(pathFile: String)

    // State of audio engine
    external fun createAudioEngine()

    external fun destroyAudioEngine()

    // These functions populate effectDescriptionMap
    private external fun getEffects(): Array<EffectDescription>

    // These functions mutate the function list
    // Adds effect at index
    private external fun addDefaultEffectNative(id: Int)

    private external fun removeEffectNative(index: Int)

    private external fun rotateEffectNative(from: Int, to: Int)

    private external fun modifyEffectNative(id: Int, index: Int, params: FloatArray)

    private external fun enableEffectNative(index: Int, enable: Boolean)

    private external fun enablePassthroughNative(enable: Boolean)

    // These are utility functions
    private fun convertEffectToId(effect: NativeEffect): Int =
        effectDescriptionMap[effect.name]?.id ?: -1

}

internal const val TAG = "NativeInterface"
