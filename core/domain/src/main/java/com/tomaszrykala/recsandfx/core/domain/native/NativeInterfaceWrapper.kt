package com.tomaszrykala.recsandfx.core.domain.native

import android.util.Log
import com.tomaszrykala.recsandfx.core.datatype.EffectDescription
import com.tomaszrykala.recsandfx.core.domain.effect.Effect
import com.tomaszrykala.recsandfx.core.domain.effect.toNativeEffect

interface NativeInterfaceWrapper {
    fun startAudioRecorder()
    fun stopAudioRecorder()
    fun createAudioEngine()
    fun enable(enable: Boolean)
    fun destroyAudioEngine()

    fun addEffect(effect: Effect)
    fun removeEffect()
    fun updateParamsAt(effect: Effect, value: Float, index: Int)
    fun writeFile(pathFile: String)

    fun getAllEffectsMap(): Map<String, EffectDescription>
}

internal class NativeInterfaceWrapperImpl : NativeInterfaceWrapper {

    override fun startAudioRecorder() = NativeInterface.startAudioRecorder()

    override fun stopAudioRecorder() = NativeInterface.stopAudioRecorder()

    override fun createAudioEngine() = NativeInterface.createAudioEngine()

    override fun enable(enable: Boolean) = NativeInterface.enable(enable)

    override fun destroyAudioEngine() = NativeInterface.destroyAudioEngine()

    override fun addEffect(effect: Effect) {
        // NativeInterface.removeEffectAt(0) // ?
        NativeInterface.addEffect(effect.toNativeEffect())
    }

    override fun removeEffect() = NativeInterface.removeEffectAt(0)

    override fun updateParamsAt(effect: Effect, value: Float, index: Int) {
        val nativeEffect = effect.toNativeEffect().apply { paramValues[index] = value }
        Log.d(TAG, "nativeEffect update ${nativeEffect.paramValues[index]}.")
        NativeInterface.updateParamsAt(nativeEffect, 0) // index?
    }

    override fun writeFile(pathFile: String) {
        NativeInterface.writeFile(pathFile)
    }

    override fun getAllEffectsMap(): Map<String, EffectDescription> =
        NativeInterface.effectDescriptionMap
}