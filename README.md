# Roblox Studio Mobile

Independent Android-first 3D editor prototype built with Kotlin and Jetpack Compose.

## Foundation
- Native Android app module and Compose editor shell.
- Toolbar placeholders: Select, Move, Rotate, Scale.
- Workspace placeholder, Explorer, and Properties panels.
- Minimum Android API 26; Java target 17.

## Renderer plan
Google Filament is the planned real-time 3D renderer for the viewport milestone. It is not integrated yet. The next milestone must add the Android Filament dependency, renderer lifecycle, scene, and touch camera.

## Build requirements
- Android Studio with Android Gradle Plugin 8.7.3 support
- JDK 17
- Android SDK Platform 35

Open the project in Android Studio, sync Gradle, and run the app configuration on an Android device or emulator.

## Roadmap
1. Kotlin/Android foundation and editor shell.
2. Filament viewport and touch camera.
3. Scene graph and object selection.
4. Part creation and transform tools.
5. Properties editing and project serialization.
6. Luau editor and supported import/export workflows.

## Scope
This is an independent editor project, not Roblox Studio itself. It does not include Roblox proprietary source code or cloud services, and does not promise full RBXL/RBXLX compatibility.
