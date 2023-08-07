package com.tomaszrykala.recsandfx

import android.content.Context
import com.tomaszrykala.recsandfx.core.domain.native.NativeInterfaceWrapper
import com.tomaszrykala.recsandfx.feature.permissions.PermissionsWrapper
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Assert.assertEquals
import org.junit.Test

class RecsAndFxViewModelTest {

    private val contextMock: Context = mockk()
    private val mockNativeInterface: NativeInterfaceWrapper = mockk(relaxUnitFun = true)
    private val mockPermissions: PermissionsWrapper = mockk()

    private val sut = RecsAndFxViewModel(mockNativeInterface, mockPermissions)

    @Test
    fun `GIVEN recording is granted WHEN onCreated THEN create and enable Audio Engine`() {
        every { mockPermissions.isRecordingAudioGranted(contextMock) } returns true

        sut.onCreated(contextMock)

        verify { mockNativeInterface.createAudioEngine() }
        verify { mockNativeInterface.enable(false) }
    }

    @Test
    fun `GIVEN recording is not granted WHEN onCreated THEN create and enable Audio Engine`() {
        every { mockPermissions.isRecordingAudioGranted(contextMock) } returns false

        sut.onCreated(contextMock)

        verify(exactly = 0) { mockNativeInterface.createAudioEngine() }
        verify(exactly = 0) { mockNativeInterface.enable(false) }
    }

    @Test
    fun `WHEN onDestroyed THEN destroy Audio Engine`() {
        sut.onDestroyed()

        verify { mockNativeInterface.destroyAudioEngine() }
    }

    @Test
    fun `WHEN enableAudio THEN pass the flag to the Audio Engine`() {
        sut.enableAudio(true)

        verify { mockNativeInterface.enable(true) }
    }

    @Test
    fun `WHEN getPermissions THEN return all required Permissions`() {
        val permissions = listOf("one", "two")
        every { mockPermissions.getAllPermissions() } returns permissions

        val actual = sut.getPermissions()

        assertEquals(permissions, actual)
    }
}