# Roblox Studio Mobile

Independent Android-first 3D editor prototype built with Kotlin and Jetpack Compose.

## Stage 1 — Android foundation
- Native Android app module and Compose editor shell.
- Toolbar placeholders: Select, Move, Rotate, Scale.
- Explorer and Properties panels.
- Minimum Android API 26; Java target 17.

## Stage 2 — Filament viewport
- Google Filament Android runtime and runtime material compiler.
- Native SurfaceView hosted inside the Compose editor.
- Real-time render loop with pause/resume and surface recreation handling.
- Procedural unlit cube rendered in a 3D scene.
- Drag on the viewport to rotate the cube.

The viewport uses Filament 1.81.0. Its Android integration follows the official Filament Android samples and lifecycle guidance. The renderer is a first viewport milestone, not yet a full scene editor.

## Build requirements
- Android Studio with Android Gradle Plugin 8.7.3 support
- JDK 17
- Android SDK Platform 35

Open the project in Android Studio, sync Gradle, and run the app configuration on an Android device or emulator. The first Gradle sync downloads the Filament Android libraries from Maven Central.

## Roadmap
1. Kotlin/Android foundation and editor shell.
2. Filament viewport and touch rotation.
3. Orbit camera, grid, and scene graph.
4. Part creation and transform tools.
5. Properties editing and project serialization.
6. Luau editor and supported import/export workflows.

## Scope
This is an independent editor project, not Roblox Studio itself. It does not include Roblox proprietary source code or cloud services, and does not promise full RBXL/RBXLX compatibility.
