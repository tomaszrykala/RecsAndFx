package com.tomaszrykala.recsandfx.data

interface NativeInterfaceWrapper {
    fun startAudioRecorder()
    fun stopAudioRecorder()
    fun createAudioEngine()
    fun enable(enable: Boolean)
    fun destroyAudioEngine()
    fun writeFile(pathFile: String)
}

class NativeInterfaceWrapperImpl : NativeInterfaceWrapper {

    override fun startAudioRecorder() {
        NativeInterface.startAudioRecorder()
    }

    override fun stopAudioRecorder() {
        NativeInterface.stopAudioRecorder()
    }

    override fun createAudioEngine() {
        NativeInterface.createAudioEngine()
    }

    override fun enable(enable: Boolean) {
        NativeInterface.enable(enable)
    }

    override fun destroyAudioEngine() {
        NativeInterface.destroyAudioEngine()
    }

    override fun writeFile(pathFile: String) {
        NativeInterface.writeFile(pathFile)
    }
}