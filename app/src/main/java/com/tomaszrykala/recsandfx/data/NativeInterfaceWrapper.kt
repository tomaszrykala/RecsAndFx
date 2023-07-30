package com.tomaszrykala.recsandfx.data

interface NativeInterfaceWrapper {
    fun startAudioRecorder()
    fun stopAudioRecorder()
    fun writeFile(pathFile: String)
}

class NativeInterfaceWrapperImpl : NativeInterfaceWrapper {

    override fun startAudioRecorder() {
        NativeInterface.startAudioRecorder()
    }

    override fun stopAudioRecorder() {
        NativeInterface.stopAudioRecorder()
    }

    override fun writeFile(pathFile: String) {
        NativeInterface.writeFile(pathFile)
    }
}