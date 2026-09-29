package com.example.fontcraftpro.ui.editor

import com.example.fontcraftpro.data.model.TextAlignment
import com.example.fontcraftpro.data.model.TextEffect
import com.example.fontcraftpro.data.model.TextLayer
import org.junit.Assert.assertTrue
import org.junit.Test

class TextRenderEffectTest {
    @Test
    fun glowAnd3dEffectsCreateMultipleRealRenderPasses() {
        val layer = TextLayer(
            id = "test-1",
            x = 0f,
            y = 0f,
            text = "字效",
            fontSize = 80f,
            textColor = 0xFFFFFFFF.toInt(),
            effect = TextEffect.OUTER_GLOW,
            shadowRadius = 18f,
            is3D = true,
            extrudeDepth = 6,
            textAlignment = TextAlignment.LEFT
        )

        val passes = buildTextRenderPasses(layer)
        assertTrue("Glow and 3D should create multiple actual render passes", passes.size >= 3)
    }
}
