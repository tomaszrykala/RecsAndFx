/*
 * Copyright  2019 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

#ifndef ANDROID_FXLAB_EFFECTS_H
#define ANDROID_FXLAB_EFFECTS_H


// The Sample Rate for effects
static int SAMPLE_RATE = 48000;

// This header should include the various effect descriptions
// general
#include "descrip/EffectDescription.h"

// no parent group
#include "descrip/PassthroughDescription.h"
#include "descrip/GainDescription.h"
#include "descrip/TremoloDescription.h"

// filters
#include "descrip/AllPassDescription.h"
#include "descrip/FIRDescription.h"
#include "descrip/IIRDescription.h"

// delays
#include "descrip/DoublingDescription.h"
#include "descrip/EchoDescription.h"

// choruses
#include "descrip/FlangerDescription.h"
#include "descrip/WhiteChorusDescription.h"
#include "descrip/SlapbackDescription.h"
#include "descrip/VibratoDescription.h"

// distortion
#include "descrip/OverdriveDescription.h"
#include "descrip/DistortionDescription.h"

constexpr std::tuple<
        Effect::PassthroughDescription,
        Effect::GainDescription,
        Effect::TremoloDescription,

        Effect::AllPassDescription,
        Effect::FIRDescription,
        Effect::IIRDescription,

        Effect::DoublingDescription,
        Effect::EchoDescription,

        Effect::FlangerDescription,
        Effect::WhiteChorusDescription,
        Effect::SlapbackDescription,
        Effect::VibratoDescription,

        Effect::OverdriveDescription,
        Effect::DistortionDescription
> EffectsTuple{};

constexpr size_t numEffects = std::tuple_size<decltype(EffectsTuple)>::value;


#endif //ANDROID_FXLAB_EFFECTS_H
