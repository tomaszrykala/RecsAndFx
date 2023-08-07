package com.tomaszrykala.recsandfx.feature.effects_list

import com.tomaszrykala.recsandfx.core.domain.effect.Effect
import com.tomaszrykala.recsandfx.core.domain.effect.EffectCategory
import com.tomaszrykala.recsandfx.core.domain.repository.EffectsRepository
import io.mockk.coEvery
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class EffectsListViewModelTest {

    private val mockEffectsRepository: EffectsRepository = mockk(relaxUnitFun = true)
    private val dispatcher: CoroutineDispatcher = UnconfinedTestDispatcher()

    private val sut = EffectsListViewModel(mockEffectsRepository, dispatcher)

    @Test
    fun `on INIT the initial state is Empty`() = runTest {
        val listUiState = sut.uiStateFlow.value

        assertTrue(listUiState is EffectsListUiState.Empty)
    }

    @Test
    fun `GIVEN an Effect is emitted WHEN observeEffects THEN receive that Effect`() = runTest {
        val effect = Effect(1, "delay", emptyList(), EffectCategory.Delay, -1)
        coEvery { mockEffectsRepository.getAllEffects() } returns listOf(effect)

        sut.observeEffects()

        val listUiState = sut.uiStateFlow.value
        assertTrue(listUiState is EffectsListUiState.EffectsList)
        with(listUiState as EffectsListUiState.EffectsList) {
            assertEquals(1, this.effects.size)
            assertEquals(effect, effects[EffectCategory.Delay]!![0])
        }
    }
}