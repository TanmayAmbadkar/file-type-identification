/*
 * Copyright (c) 2018-present, lmyooyo@gmail.com.
 *
 * This source code is licensed under the GPL license found in the
 * LICENSE file in the root directory of this source tree.
 */
package com.lmy.codec.texture.impl.filter

import android.opengl.GLES20
import android.opengl.GLUtils
import com.lmy.codec.helper.Resources
import javax.microedition.khronos.opengles.GL10

abstract class BaseMultipleSamplerFilter(width: Int = 0,
                                         height: Int = 0,
                                         textureId: IntArray = IntArray(1)) : BaseFilter(width, height, textureId) {
    private var textures: IntArray? = null
    private var textureLocations: IntArray? = null
    override fun init() {
        super.init()
        if (null == getSamplers()) return
        textures = IntArray(getSamplers()!!.size)
        textureLocations = IntArray(getSamplers()!!.size)
        getSamplers()!!.forEachIndexed { index, sampler ->
            textures!![index] = loadTexture(sampler.path)
            textureLocations!![index] = getUniformLocation(sampler.name)
        }
    }

    private fun loadTexture(path: String): Int {
        val bitmap = Resources.instance.readAssetsAsBitmap(path)
        if (null == bitmap || bitmap.isRecycled) return 0
        val texture = IntArray(1)
        GLES20.glGenTextures(texture.size, texture, 0)
        GLES20.glBindTexture(GL10.GL_TEXTURE_2D, texture[0])
        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D,
                GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR.toFloat())
        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D,
                GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR.toFloat())
        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D,
                GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_CLAMP_TO_EDGE.toFloat())
        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D,
                GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_CLAMP_TO_EDGE.toFloat())
        GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bitmap, 0)
        GLES20.glBindTexture(GL10.GL_TEXTURE_2D, GLES20.GL_NONE)
        bitmap.recycle()
        return texture[0]
    }

    override fun active(samplerLocation: Int) {
        super.active(samplerLocation)
        if (null == textures || null == textureLocations || null == getSamplers()) {
            return
        }
        textures!!.forEachIndexed { index, it ->
            GLES20.glActiveTexture(GLES20.GL_TEXTURE0 + index + 1)
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, it)
            setUniform1i(textureLocations!![index], index + 1)
        }
    }

    abstract fun getSamplers(): Array<Sampler>?
    class Sampler(var name: String, var path: String)

    override fun release() {
        super.release()
        if (null != textures)
            GLES20.glDeleteTextures(textures!!.size, textures!!, 0)
    }
}