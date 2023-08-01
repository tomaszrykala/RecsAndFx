package com.tomaszrykala.recsandfx.core.domain.effect

import com.tomaszrykala.recsandfx.core.datatype.ParamDescription

data class Param(
    val name: String,
    val minValue: Float,
    val maxValue: Float,
    val defaultValue: Float
)

fun Param.toParamDescription() = ParamDescription(
    paramName = name,
    minValue = minValue,
    maxValue = maxValue,
    defaultValue = defaultValue
)

fun ParamDescription.toParam() = Param(
    name = paramName,
    minValue = minValue,
    maxValue = maxValue,
    defaultValue = defaultValue
)