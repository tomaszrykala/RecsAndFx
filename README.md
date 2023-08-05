RecsAndFx
==========

App demonstrating various DSP Audio Effects, by applying them to source audio, eg. a connected microphone and storing the recordings on the device for further playback.

Usage
---------------

The app consists of the List of all Effects available.

<img src="/readme/app_effects_list.png" width="40%" />

Upon selection the **Effect** may offer various controls for modulating its characteristics.
The **Record** button arms the device into recording mode, in which it captures the sound coming from the default source - eg. microphone and applies the Effect.
When using the device's built-in microphone, it's important to enable '**Pass-through**' by clicking the Icon in the AppBar, while ensuring that the output volume is as low as possible without introducing harsh feedback.

<img src="/readme/app_effect_detail.png" width="40%" />

Implementation
--------------

The app is also a sandbox of latest Android development practices and it incorporates:
- UI, State Management and Navigation using [Jetpack Compose](https://developer.android.com/jetpack/compose) and related libraries
- Dependency Injection using [Koin](https://insert-koin.io/)
- [App Modularisation](https://developer.android.com/topic/modularization) by feature
- ??... (TODO)

Acknowledgments
---------------

The implementation borrows heavily from the following open-source projects:
- [Google Oboe](https://github.com/google/oboe) - for the C++ implementations of DSP Audio Effects
- [oboe_fx_recorder](https://github.com/HeroSony/oboe_fx_recorder) - for the app idea and documentation
- [Oboe Recorder](https://github.com/sheraz-nadeem/oboe_recorder_sample) - for the recording capability

Pre-build
---------
Link Oboe path directory in CmakeLists.txt
```
set (OBOE_DIR <path_to_oboe>)
```

Add [libsndfile](https://github.com/libsndfile/libsndfile) library
```
set(LIB_SND_FILE_DIR <path_to_libsndfile>)
add_subdirectory(${LIB_SND_FILE_DIR} ./sndfile)
include_directories(${LIB_SND_FILE_DIR}/src)
```

Build and deploy.

Contributions
-------------
Encouraged and appreciated.

# License

    Copyright 2023 Tomasz Rykała

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
