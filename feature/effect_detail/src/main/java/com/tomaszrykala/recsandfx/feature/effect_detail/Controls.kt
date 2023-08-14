package com.tomaszrykala.recsandfx.feature.effect_detail

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import com.tomaszrykala.recsandfx.core.domain.effect.Effect
import kotlinx.coroutines.launch
import kotlin.math.pow
import kotlin.math.roundToInt

@Composable
internal fun Controls(
    effect: Effect,
    slidersState: Map<Int, MutableState<Float>>,
    onSliderChange: suspend (value: Float, index: Int) -> Unit,
) {
    effect.params.forEachIndexed { index, param ->
        val coroutineScope = rememberCoroutineScope()
        val sliderPosition: MutableState<Float> = slidersState[index]!!

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = param.name,
                modifier = Modifier.padding(end = paddingSmall),
                style = TextStyle(fontSize = spTextUnit(18.0f))
            )
            Text(
                text = sliderPosition.value.roundToTwoDecimals().toString(),
                modifier = Modifier.padding(end = paddingSmall),
                style = TextStyle(fontWeight = FontWeight.Bold, fontSize = spTextUnit(18.0f))
            )
            Text(
                text = param.minValue.toString(), modifier = Modifier
                    .alpha(0.6f)
                    .padding(end = paddingSmall)
            )
            Slider(
                value = sliderPosition.value,
                onValueChange = { value -> sliderPosition.value = value },
                onValueChangeFinished = {
                    coroutineScope.launch { onSliderChange(sliderPosition.value, index) }
                },
                valueRange = object : ClosedFloatingPointRange<Float> {
                    override fun lessThanOrEquals(a: Float, b: Float): Boolean = a <= b
                    override val endInclusive: Float = param.maxValue
                    override val start: Float = param.minValue
                },
                modifier = Modifier
                    .weight(0.5f, false)
                    .padding(end = paddingSmall),
            )
            Text(text = param.maxValue.toString(), modifier = Modifier.alpha(0.6f))
        }
    }
}

private fun Float.roundToTwoDecimals(): Double {
    val factor = 10.0.pow(2.toDouble())
    return (this * factor).roundToInt() / factor
}