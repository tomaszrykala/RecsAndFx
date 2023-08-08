package com.tomaszrykala.recsandfx.feature.effect_detail

import android.content.Context
import android.net.Uri
import com.tomaszrykala.recsandfx.core.domain.effect.Effect
import com.tomaszrykala.recsandfx.core.domain.effect.EffectCategory
import com.tomaszrykala.recsandfx.core.domain.native.NativeInterfaceWrapper
import com.tomaszrykala.recsandfx.core.domain.repository.EffectResult
import com.tomaszrykala.recsandfx.core.domain.repository.EffectsRepository
import com.tomaszrykala.recsandfx.core.storage.FileStorage
import com.tomaszrykala.recsandfx.feature.media_player.RecordingsPlayer
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.coVerifyOrder
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class EffectDetailViewModelTest {

    private val mockFileStorage: FileStorage = mockk()
    private val mockRecordingsPlayer: RecordingsPlayer = mockk(relaxed = true)
    private val mockEffectsRepository: EffectsRepository = mockk()
    private val mockNativeInterface: NativeInterfaceWrapper = mockk(relaxed = true)
    private val dispatcher: CoroutineDispatcher = UnconfinedTestDispatcher()

    private val sut = EffectDetailViewModel(
        fileStorage = mockFileStorage,
        recordingsPlayer = mockRecordingsPlayer,
        effectsRepository = mockEffectsRepository,
        nativeInterface = mockNativeInterface,
        ioDispatcher = dispatcher,
        defaultDispatcher = dispatcher
    )

    @Test
    fun `on INIT the initial state is Empty`() = runTest {
        val listUiState = sut.uiStateFlow.value

        assertTrue(listUiState is EffectDetailUiState.Empty)
    }

    @Test
    fun `GIVEN an Effect is matched WHEN observeEffect THEN emit its Recordings`() = runTest {
        val (_, allRecordings, effect) = setUpEffect()

        coVerify { mockNativeInterface.addEffect(effect) }
        coVerify(exactly = 0) { mockNativeInterface.removeEffect() }
        val listUiState = sut.uiStateFlow.value
        assertTrue(listUiState is EffectDetailUiState.EffectDetail)
        with(listUiState as EffectDetailUiState.EffectDetail) {
            assertEquals(effect, this.effect)
            assertEquals(allRecordings, this.recordings)
        }
    }

    @Test
    fun `GIVEN cached present WHEN observeEffect THEN clear cached effect before setting new one`() = runTest {
        val (_, _, effect) = setUpEffect(hasCached = true)

        coVerifyOrder {
            mockNativeInterface.removeEffect()
            mockNativeInterface.addEffect(effect)
        }
    }

    @Test
    fun `GIVEN no Effect is matched WHEN observeEffect THEN emit an Error`() = runTest {
        val effectName = "none"
        coEvery { mockEffectsRepository.getEffect(effectName) } returns EffectResult(null, false)

        sut.observeEffect(effectName)

        val listUiState = sut.uiStateFlow.value
        assertTrue(listUiState is EffectDetailUiState.Error)
        with(listUiState as EffectDetailUiState.Error) {
            assertEquals(R.string.recordings_load_error, this.errorResId)
        }
    }

    @Test
    fun `WHEN startAudioRecorder THEN start Audio Recording`() = runTest {
        sut.startAudioRecorder()

        coVerify { mockNativeInterface.startAudioRecorder() }
    }

    @Test
    fun `WHEN stopAudioRecorder THEN stop the Recording and save File`() = runTest {
        val (effectName, _, _) = setUpEffect()
        val filePath = "filePath"
        coEvery { mockFileStorage.getRecordingFilePath(effectName) } returns filePath

        sut.stopAudioRecorder()

        with(mockNativeInterface) {
            coVerify { stopAudioRecorder() }
            coVerify { writeFile(filePath) }
        }
        coVerify(exactly = 2) { mockFileStorage.getAllRecordings(effectName) }
    }

    @Test
    fun `WHEN onSelectedRecording THEN play the Audio Recording`() = runTest {
        val mockContext: Context = mockk()
        val mockUri: Uri = mockk()
        val selectedRecording = "recording"
        coEvery { mockFileStorage.getRecordingUri(selectedRecording) } returns mockUri

        sut.onSelectedRecording(mockContext, selectedRecording)

        coVerify { mockRecordingsPlayer.play(mockContext, mockUri) }
    }

    @Test
    fun `WHEN onRecordingStop THEN stop Audio Recording`() = runTest {
        sut.onRecordingStop()

        coVerify { mockRecordingsPlayer.stop() }
    }

    @Test
    fun `WHEN deleteRecording THEN delete the Audio Recording`() = runTest {
        val (effectName, allRecordings, effect) = setUpEffect()
        val selectedRecording = "recording"
        coEvery { mockFileStorage.deleteRecording(selectedRecording) } returns true

        sut.deleteRecording(selectedRecording)

        val listUiState = sut.uiStateFlow.value
        assertTrue(listUiState is EffectDetailUiState.EffectDetail)
        with(listUiState as EffectDetailUiState.EffectDetail) {
            assertEquals(effect, this.effect)
            assertEquals(allRecordings, this.recordings)
        }
        coVerify(exactly = 2) { mockFileStorage.getAllRecordings(effectName) }
    }

    @Test
    fun `GIVEN the file can't be deleted WHEN deleteRecording THEN log the error`() = runTest {
        val (effectName, _, _) = setUpEffect()
        val selectedRecording = "recording"
        coEvery { mockFileStorage.deleteRecording(selectedRecording) } returns false

        sut.deleteRecording(selectedRecording)

        coVerify(exactly = 1) { mockFileStorage.getAllRecordings(effectName) }
        val listUiState = sut.uiStateFlow.value
        assertTrue(listUiState is EffectDetailUiState.Error)
        with(listUiState as EffectDetailUiState.Error) {
            assertEquals(R.string.recordings_delete_error, this.errorResId)
        }
    }

    @Test
    fun `WHEN onParamChange THEN pass the params to the Native Interface`() = runTest {
        val (_, _, effect) = setUpEffect()
        val value = 1.0f
        val index = 4

        sut.onParamChange(value, index)

        coVerify { mockNativeInterface.updateParamsAt(effect, value, index) }
    }

    private suspend fun setUpEffect(hasCached: Boolean = false): Triple<String, List<String>, Effect> {
        val effectName = "Delay"
        val allRecordings = listOf("Recording 1", "Recording 2")
        val effect = Effect(1, effectName, emptyList(), EffectCategory.Delay, -1)
        coEvery { mockEffectsRepository.getEffect(effectName) } returns EffectResult(effect, hasCached)
        coEvery { mockFileStorage.getAllRecordings(effectName) } returns allRecordings

        sut.observeEffect(effectName)

        return Triple(effectName, allRecordings, effect)
    }
}