package com.tomaszrykala.recsandfx.core.domain.native

import android.util.Log
import com.tomaszrykala.recsandfx.core.datatype.EffectDescription
import com.tomaszrykala.recsandfx.core.domain.effect.Effect
import com.tomaszrykala.recsandfx.core.domain.effect.toNativeEffect

interface NativeInterfaceWrapper {
    suspend fun startAudioRecorder()
    suspend fun stopAudioRecorder()
    fun createAudioEngine()
    fun enable(enable: Boolean)
    fun destroyAudioEngine()

    suspend fun addEffect(effect: Effect)
    suspend fun removeEffect()
    suspend fun updateParamsAt(effect: Effect, value: Float, index: Int)
    suspend fun writeFile(pathFile: String)

    fun getAllEffectsMap(): Map<String, EffectDescription>
}

internal class NativeInterfaceWrapperImpl : NativeInterfaceWrapper {

    override suspend fun startAudioRecorder() = NativeInterface.startAudioRecorder()

    override suspend fun stopAudioRecorder() = NativeInterface.stopAudioRecorder()

    override fun createAudioEngine() = NativeInterface.createAudioEngine()

    override fun enable(enable: Boolean) = NativeInterface.enable(enable)

    override fun destroyAudioEngine() = NativeInterface.destroyAudioEngine()

    override suspend fun addEffect(effect: Effect) {
        // NativeInterface.removeEffectAt(0) // ?
        NativeInterface.addEffect(effect.toNativeEffect())
    }

    override suspend fun removeEffect() = NativeInterface.removeEffectAt(0)

    override suspend fun updateParamsAt(effect: Effect, value: Float, index: Int) {
        val nativeEffect = effect.toNativeEffect().apply { paramValues[index] = value }
        Log.d(TAG, "nativeEffect update ${nativeEffect.paramValues[index]}.")
        NativeInterface.updateParamsAt(nativeEffect, 0) // index?
    }

    override suspend fun writeFile(pathFile: String) {
        NativeInterface.writeFile(pathFile)
    }

    override fun getAllEffectsMap(): Map<String, EffectDescription> =
        NativeInterface.effectDescriptionMap
}