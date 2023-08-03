package com.tomaszrykala.recsandfx.core.domain.effect

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.tomaszrykala.recsandfx.core.domain.R

enum class EffectCategory(@StringRes val fxName: Int, @DrawableRes val fxIcon: Int) {
    Delay(R.string.effect_category_delays, R.drawable.ic_fx_delays_24),
    Chorus(R.string.effect_category_choruses, R.drawable.ic_fx_choruses_24),
    Distortion(R.string.effect_category_distortions, R.drawable.ic_fx_distortions_24),
    Comb(R.string.effect_category_comb_filters, R.drawable.ic_fx_filters_24),
    None(R.string.effect_category_no_category, R.drawable.ic_fx_no_category_24)
}