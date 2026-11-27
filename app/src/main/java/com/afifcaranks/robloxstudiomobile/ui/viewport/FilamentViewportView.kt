package com.afifcaranks.robloxstudiomobile.ui.viewport

import android.content.Context
import android.opengl.Matrix
import android.util.AttributeSet
import android.util.Log
import android.view.Choreographer
import android.view.MotionEvent
import android.view.SurfaceHolder
import android.view.SurfaceView
import com.google.android.filament.Box
import com.google.android.filament.Camera
import com.google.android.filament.Engine
import com.google.android.filament.EntityManager
import com.google.android.filament.Filament
import com.google.android.filament.IndexBuffer
import com.google.android.filament.Material
import com.google.android.filament.RenderableManager
import com.google.android.filament.Renderer
import com.google.android.filament.Scene
import com.google.android.filament.SwapChain
import com.google.android.filament.VertexBuffer
import com.google.android.filament.View
import com.google.android.filament.Viewport
import com.google.android.filament.filamat.MaterialBuilder
import java.nio.ByteBuffer
import java.nio.ByteOrder

class FilamentViewportView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : SurfaceView(context, attrs), SurfaceHolder.Callback, Choreographer.FrameCallback {

    companion object {
        private const val TAG = "StudioFilament"
        init {
            Filament.init()
        }
    }

    private var engine: Engine? = null
    private var renderer: Renderer? = null
    private var scene: Scene? = null
    private var filamentView: View? = null
    private var camera: Camera? = null
    private var swapChain: SwapChain? = null
    private var material: Material? = null
    private var vertexBuffer: VertexBuffer? = null
    private var indexBuffer: IndexBuffer? = null

    private var cameraEntity = 0
    private var cubeEntity = 0
    private var surfaceReady = false
    private var paused = false
    private var framePosted = false
    private var released = false

    private var previousTouchX = 0f
    private var previousTouchY = 0f
    private var rotationX = -18f
    private var rotationY = 28f
    private val rotationMatrix = FloatArray(16)

    init {
        holder.addCallback(this)
        isFocusable = true
        isFocusableInTouchMode = true
    }

    override fun surfaceCreated(holder: SurfaceHolder) {
        surfaceReady = true
        released = false
        try {
            createFilamentScene()
            swapChain = engine?.createSwapChain(holder.surface)
            updateViewport(width, height)
            scheduleFrame()
        } catch (error: Throwable) {
            Log.e(TAG, "Unable to initialize the 3D viewport", error)
            releaseFilament()
        }
    }

    override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {
        updateViewport(width, height)
    }

    override fun surfaceDestroyed(holder: SurfaceHolder) {
        surfaceReady = false
        stopFrameLoop()
        destroySwapChain()
        releaseFilament()
    }

    private fun createFilamentScene() {
        val newEngine = Engine.create()
        engine = newEngine
        renderer = newEngine.createRenderer()
        scene = newEngine.createScene()
        filamentView = newEngine.createView()

        cameraEntity = EntityManager.get().create()
        camera = newEngine.createCamera(cameraEntity)
        camera?.lookAt(
            0.0, 0.0, 4.2,
            0.0, 0.0, 0.0,
            0.0, 1.0, 0.0
        )

        filamentView?.camera = camera
        filamentView?.scene = scene

        createMaterial(newEngine)
        createCube(newEngine)
        scene?.addEntity(cubeEntity)

        Matrix.setIdentityM(rotationMatrix, 0)
        applyCubeRotation()
    }

    private fun createMaterial(targetEngine: Engine) {
        MaterialBuilder.init()
        try {
            val packageData = MaterialBuilder()
                .platform(MaterialBuilder.Platform.MOBILE)
                .name("StudioMobileUnlit")
                .shading(MaterialBuilder.Shading.UNLIT)
                .require(MaterialBuilder.VertexAttribute.POSITION)
                .material(
                    """
                    void material(inout MaterialInputs material) {
                        prepareMaterial(material);
                        material.baseColor = vec4(0.12, 0.52, 0.96, 1.0);
                    }
                    """.trimIndent()
                )
                .build(targetEngine)

            check(packageData.isValid) { "Filament could not compile the viewport material." }
            val buffer = packageData.buffer
            material = Material.Builder()
                .payload(buffer, buffer.remaining())
                .build(targetEngine)
        } finally {
            MaterialBuilder.shutdown()
        }
    }

    private fun createCube(targetEngine: Engine) {
        val vertices = floatArrayOf(
            -1f, -1f, -1f,
             1f, -1f, -1f,
             1f,  1f, -1f,
            -1f,  1f, -1f,
            -1f, -1f,  1f,
             1f, -1f,  1f,
             1f,  1f,  1f,
            -1f,  1f,  1f
        )

        val indices = shortArrayOf(
            0, 2, 1, 0, 3, 2,
            4, 5, 6, 4, 6, 7,
            0, 1, 5, 0, 5, 4,
            3, 7, 6, 3, 6, 2,
            0, 4, 7, 0, 7, 3,
            1, 2, 6, 1, 6, 5
        )

        val vertexData = ByteBuffer.allocateDirect(vertices.size * 4)
            .order(ByteOrder.nativeOrder())
        vertices.forEach { vertexData.putFloat(it) }
        vertexData.flip()

        val indexData = ByteBuffer.allocateDirect(indices.size * 2)
            .order(ByteOrder.nativeOrder())
        indices.forEach { indexData.putShort(it) }
        indexData.flip()

        vertexBuffer = VertexBuffer.Builder()
            .bufferCount(1)
            .vertexCount(vertices.size / 3)
            .attribute(
                VertexBuffer.VertexAttribute.POSITION,
                0,
                VertexBuffer.AttributeType.FLOAT3,
                0,
                3 * 4
            )
            .build(targetEngine)
        vertexBuffer?.setBufferAt(targetEngine, 0, vertexData)

        indexBuffer = IndexBuffer.Builder()
            .indexCount(indices.size)
            .bufferType(IndexBuffer.Builder.IndexType.USHORT)
            .build(targetEngine)
        indexBuffer?.setBuffer(targetEngine, indexData)

        cubeEntity = EntityManager.get().create()
        RenderableManager.Builder(1)
            .boundingBox(Box(0f, 0f, 0f, 1f, 1f, 1f))
            .geometry(
                0,
                RenderableManager.PrimitiveType.TRIANGLES,
                vertexBuffer,
                indexBuffer,
                0,
                indices.size
            )
            .material(0, material!!.defaultInstance)
            .culling(false)
            .build(targetEngine, cubeEntity)
    }

    private fun updateViewport(viewWidth: Int, viewHeight: Int) {
        if (viewWidth <= 0 || viewHeight <= 0) return
        camera?.setProjection(45.0, viewWidth.toDouble() / viewHeight, 0.1, 100.0)
        filamentView?.viewport = Viewport(0, 0, viewWidth, viewHeight)
    }

    private fun scheduleFrame() {
        if (!surfaceReady || paused || framePosted || renderer == null || swapChain == null) return
        framePosted = true
        Choreographer.getInstance().postFrameCallback(this)
    }

    private fun stopFrameLoop() {
        if (framePosted) {
            Choreographer.getInstance().removeFrameCallback(this)
            framePosted = false
        }
    }

    override fun doFrame(frameTimeNanos: Long) {
        framePosted = false
        val activeRenderer = renderer
        val activeSwapChain = swapChain
        val activeView = filamentView

        if (!surfaceReady || paused || activeRenderer == null ||
            activeSwapChain == null || activeView == null
        ) {
            return
        }

        if (activeRenderer.beginFrame(activeSwapChain, frameTimeNanos)) {
            activeRenderer.render(activeView)
            activeRenderer.endFrame()
        }
        scheduleFrame()
    }

    fun pauseRendering() {
        paused = true
        stopFrameLoop()
    }

    fun resumeRendering() {
        paused = false
        scheduleFrame()
    }

    @Suppress("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.actionMasked) {
            MotionEvent.ACTION_DOWN -> {
                previousTouchX = event.x
                previousTouchY = event.y
                return true
            }
            MotionEvent.ACTION_MOVE -> {
                val deltaX = event.x - previousTouchX
                val deltaY = event.y - previousTouchY
                previousTouchX = event.x
                previousTouchY = event.y
                rotationY += deltaX * 0.6f
                rotationX = (rotationX + deltaY * 0.6f).coerceIn(-85f, 85f)
                applyCubeRotation()
                return true
            }
            MotionEvent.ACTION_UP -> {
                performClick()
                return true
            }
            MotionEvent.ACTION_CANCEL -> return true
        }
        return true
    }

    override fun performClick(): Boolean {
        super.performClick()
        return true
    }

    private fun applyCubeRotation() {
        val activeEngine = engine ?: return
        if (cubeEntity == 0) return
        Matrix.setIdentityM(rotationMatrix, 0)
        Matrix.rotateM(rotationMatrix, 0, rotationY, 0f, 1f, 0f)
        Matrix.rotateM(rotationMatrix, 0, rotationX, 1f, 0f, 0f)
        val transformManager = activeEngine.transformManager
        val instance = transformManager.getInstance(cubeEntity)
        if (instance != 0) {
            transformManager.setTransform(instance, rotationMatrix)
        }
    }

    private fun destroySwapChain() {
        val activeEngine = engine ?: return
        swapChain?.let {
            runCatching {
                activeEngine.destroySwapChain(it)
                activeEngine.flushAndWait()
            }.onFailure { error -> Log.w(TAG, "SwapChain cleanup warning", error) }
        }
        swapChain = null
    }

    fun release() {
        if (released) return
        released = true
        surfaceReady = false
        stopFrameLoop()
        holder.removeCallback(this)
        destroySwapChain()
        releaseFilament()
    }

    private fun releaseFilament() {
        val activeEngine = engine ?: return

        runCatching {
            if (cubeEntity != 0) {
                activeEngine.destroyEntity(cubeEntity)
                EntityManager.get().destroy(cubeEntity)
                cubeEntity = 0
            }
            if (cameraEntity != 0) {
                activeEngine.destroyCameraComponent(cameraEntity)
                EntityManager.get().destroy(cameraEntity)
                cameraEntity = 0
            }
            vertexBuffer?.let(activeEngine::destroyVertexBuffer)
            indexBuffer?.let(activeEngine::destroyIndexBuffer)
            material?.let(activeEngine::destroyMaterial)
            filamentView?.let(activeEngine::destroyView)
            scene?.let(activeEngine::destroyScene)
            renderer?.let(activeEngine::destroyRenderer)
            activeEngine.destroy()
        }.onFailure { error ->
            Log.e(TAG, "Filament cleanup warning", error)
        }

        engine = null
        renderer = null
        scene = null
        filamentView = null
        camera = null
        material = null
        vertexBuffer = null
        indexBuffer = null
    }

    override fun onDetachedFromWindow() {
        release()
        super.onDetachedFromWindow()
    }
}
