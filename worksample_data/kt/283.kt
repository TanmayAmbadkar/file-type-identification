package org.w3c.dom.mediacapture

import kotlin.js.*
import org.khronos.webgl.*
import org.w3c.css.masking.*
import org.w3c.dom.*
import org.w3c.dom.clipboard.*
import org.w3c.dom.css.*
import org.w3c.dom.events.*
import org.w3c.dom.parsing.*
import org.w3c.dom.pointerevents.*
import org.w3c.dom.svg.*
import org.w3c.dom.url.*
import org.w3c.fetch.*
import org.w3c.files.*
import org.w3c.notifications.*
import org.w3c.performance.*
import org.w3c.workers.*
import org.w3c.xhr.*

external open class MediaStream() : EventTarget {
    constructor(stream: MediaStream)
    constructor(tracks: Array<MediaStreamTrack>)
    open val id: String
    open val active: Boolean
    var onaddtrack: ((MediaStreamTrackEvent) -> dynamic)?
    var onremovetrack: ((MediaStreamTrackEvent) -> dynamic)?
    fun getAudioTracks(): Array<MediaStreamTrack>
    fun getVideoTracks(): Array<MediaStreamTrack>
    fun getTracks(): Array<MediaStreamTrack>
    fun getTrackById(trackId: String): MediaStreamTrack?
    fun addTrack(track: MediaStreamTrack)
    fun removeTrack(track: MediaStreamTrack)
    fun clone(): MediaStream
}

external abstract class MediaStreamTrack : EventTarget {
    open val kind: String
    open val id: String
    open val label: String
    open var enabled: Boolean
    open val muted: Boolean
    open var onmute: ((Event) -> dynamic)?
    open var onunmute: ((Event) -> dynamic)?
    open val readyState: MediaStreamTrackState
    open var onended: ((Event) -> dynamic)?
    open var onoverconstrained: ((Event) -> dynamic)?
    fun clone(): MediaStreamTrack
    fun stop()
    fun getCapabilities(): MediaTrackCapabilities
    fun getConstraints(): MediaTrackConstraints
    fun getSettings(): MediaTrackSettings
    fun applyConstraints(constraints: MediaTrackConstraints = definedExternally): Promise<Unit>
}

external interface MediaTrackSupportedConstraints {
    var width: Boolean? /* = true */
        get() = definedExternally
        set(value) = definedExternally
    var height: Boolean? /* = true */
        get() = definedExternally
        set(value) = definedExternally
    var aspectRatio: Boolean? /* = true */
        get() = definedExternally
        set(value) = definedExternally
    var frameRate: Boolean? /* = true */
        get() = definedExternally
        set(value) = definedExternally
    var facingMode: Boolean? /* = true */
        get() = definedExternally
        set(value) = definedExternally
    var resizeMode: Boolean? /* = true */
        get() = definedExternally
        set(value) = definedExternally
    var volume: Boolean? /* = true */
        get() = definedExternally
        set(value) = definedExternally
    var sampleRate: Boolean? /* = true */
        get() = definedExternally
        set(value) = definedExternally
    var sampleSize: Boolean? /* = true */
        get() = definedExternally
        set(value) = definedExternally
    var echoCancellation: Boolean? /* = true */
        get() = definedExternally
        set(value) = definedExternally
    var autoGainControl: Boolean? /* = true */
        get() = definedExternally
        set(value) = definedExternally
    var noiseSuppression: Boolean? /* = true */
        get() = definedExternally
        set(value) = definedExternally
    var latency: Boolean? /* = true */
        get() = definedExternally
        set(value) = definedExternally
    var channelCount: Boolean? /* = true */
        get() = definedExternally
        set(value) = definedExternally
    var deviceId: Boolean? /* = true */
        get() = definedExternally
        set(value) = definedExternally
    var groupId: Boolean? /* = true */
        get() = definedExternally
        set(value) = definedExternally
}

@kotlin.internal.InlineOnly
inline fun MediaTrackSupportedConstraints(width: Boolean? = true, height: Boolean? = true, aspectRatio: Boolean? = true, frameRate: Boolean? = true, facingMode: Boolean? = true, resizeMode: Boolean? = true, volume: Boolean? = true, sampleRate: Boolean? = true, sampleSize: Boolean? = true, echoCancellation: Boolean? = true, autoGainControl: Boolean? = true, noiseSuppression: Boolean? = true, latency: Boolean? = true, channelCount: Boolean? = true, deviceId: Boolean? = true, groupId: Boolean? = true): MediaTrackSupportedConstraints {
    val o = js("({})")

    o["width"] = width
    o["height"] = height
    o["aspectRatio"] = aspectRatio
    o["frameRate"] = frameRate
    o["facingMode"] = facingMode
    o["resizeMode"] = resizeMode
    o["volume"] = volume
    o["sampleRate"] = sampleRate
    o["sampleSize"] = sampleSize
    o["echoCancellation"] = echoCancellation
    o["autoGainControl"] = autoGainControl
    o["noiseSuppression"] = noiseSuppression
    o["latency"] = latency
    o["channelCount"] = channelCount
    o["deviceId"] = deviceId
    o["groupId"] = groupId

    return o
}

external interface MediaTrackCapabilities {
    var width: ULongRange?
        get() = definedExternally
        set(value) = definedExternally
    var height: ULongRange?
        get() = definedExternally
        set(value) = definedExternally
    var aspectRatio: DoubleRange?
        get() = definedExternally
        set(value) = definedExternally
    var frameRate: DoubleRange?
        get() = definedExternally
        set(value) = definedExternally
    var facingMode: Array<String>?
        get() = definedExternally
        set(value) = definedExternally
    var resizeMode: Array<String>?
        get() = definedExternally
        set(value) = definedExternally
    var volume: DoubleRange?
        get() = definedExternally
        set(value) = definedExternally
    var sampleRate: ULongRange?
        get() = definedExternally
        set(value) = definedExternally
    var sampleSize: ULongRange?
        get() = definedExternally
        set(value) = definedExternally
    var echoCancellation: Array<Boolean>?
        get() = definedExternally
        set(value) = definedExternally
    var autoGainControl: Array<Boolean>?
        get() = definedExternally
        set(value) = definedExternally
    var noiseSuppression: Array<Boolean>?
        get() = definedExternally
        set(value) = definedExternally
    var latency: DoubleRange?
        get() = definedExternally
        set(value) = definedExternally
    var channelCount: ULongRange?
        get() = definedExternally
        set(value) = definedExternally
    var deviceId: String?
        get() = definedExternally
        set(value) = definedExternally
    var groupId: String?
        get() = definedExternally
        set(value) = definedExternally
}

@kotlin.internal.InlineOnly
inline fun MediaTrackCapabilities(width: ULongRange? = undefined, height: ULongRange? = undefined, aspectRatio: DoubleRange? = undefined, frameRate: DoubleRange? = undefined, facingMode: Array<String>? = undefined, resizeMode: Array<String>? = undefined, volume: DoubleRange? = undefined, sampleRate: ULongRange? = undefined, sampleSize: ULongRange? = undefined, echoCancellation: Array<Boolean>? = undefined, autoGainControl: Array<Boolean>? = undefined, noiseSuppression: Array<Boolean>? = undefined, latency: DoubleRange? = undefined, channelCount: ULongRange? = undefined, deviceId: String? = undefined, groupId: String? = undefined): MediaTrackCapabilities {
    val o = js("({})")

    o["width"] = width
    o["height"] = height
    o["aspectRatio"] = aspectRatio
    o["frameRate"] = frameRate
    o["facingMode"] = facingMode
    o["resizeMode"] = resizeMode
    o["volume"] = volume
    o["sampleRate"] = sampleRate
    o["sampleSize"] = sampleSize
    o["echoCancellation"] = echoCancellation
    o["autoGainControl"] = autoGainControl
    o["noiseSuppression"] = noiseSuppression
    o["latency"] = latency
    o["channelCount"] = channelCount
    o["deviceId"] = deviceId
    o["groupId"] = groupId

    return o
}

external interface MediaTrackConstraints : MediaTrackConstraintSet {
    var advanced: Array<MediaTrackConstraintSet>?
        get() = definedExternally
        set(value) = definedExternally
}

@kotlin.internal.InlineOnly
inline fun MediaTrackConstraints(advanced: Array<MediaTrackConstraintSet>? = undefined, width: dynamic = undefined, height: dynamic = undefined, aspectRatio: dynamic = undefined, frameRate: dynamic = undefined, facingMode: dynamic = undefined, resizeMode: dynamic = undefined, volume: dynamic = undefined, sampleRate: dynamic = undefined, sampleSize: dynamic = undefined, echoCancellation: dynamic = undefined, autoGainControl: dynamic = undefined, noiseSuppression: dynamic = undefined, latency: dynamic = undefined, channelCount: dynamic = undefined, deviceId: dynamic = undefined, groupId: dynamic = undefined): MediaTrackConstraints {
    val o = js("({})")

    o["advanced"] = advanced
    o["width"] = width
    o["height"] = height
    o["aspectRatio"] = aspectRatio
    o["frameRate"] = frameRate
    o["facingMode"] = facingMode
    o["resizeMode"] = resizeMode
    o["volume"] = volume
    o["sampleRate"] = sampleRate
    o["sampleSize"] = sampleSize
    o["echoCancellation"] = echoCancellation
    o["autoGainControl"] = autoGainControl
    o["noiseSuppression"] = noiseSuppression
    o["latency"] = latency
    o["channelCount"] = channelCount
    o["deviceId"] = deviceId
    o["groupId"] = groupId

    return o
}

external interface MediaTrackConstraintSet {
    var width: dynamic
        get() = definedExternally
        set(value) = definedExternally
    var height: dynamic
        get() = definedExternally
        set(value) = definedExternally
    var aspectRatio: dynamic
        get() = definedExternally
        set(value) = definedExternally
    var frameRate: dynamic
        get() = definedExternally
        set(value) = definedExternally
    var facingMode: dynamic
        get() = definedExternally
        set(value) = definedExternally
    var resizeMode: dynamic
        get() = definedExternally
        set(value) = definedExternally
    var volume: dynamic
        get() = definedExternally
        set(value) = definedExternally
    var sampleRate: dynamic
        get() = definedExternally
        set(value) = definedExternally
    var sampleSize: dynamic
        get() = definedExternally
        set(value) = definedExternally
    var echoCancellation: dynamic
        get() = definedExternally
        set(value) = definedExternally
    var autoGainControl: dynamic
        get() = definedExternally
        set(value) = definedExternally
    var noiseSuppression: dynamic
        get() = definedExternally
        set(value) = definedExternally
    var latency: dynamic
        get() = definedExternally
        set(value) = definedExternally
    var channelCount: dynamic
        get() = definedExternally
        set(value) = definedExternally
    var deviceId: dynamic
        get() = definedExternally
        set(value) = definedExternally
    var groupId: dynamic
        get() = definedExternally
        set(value) = definedExternally
}

@kotlin.internal.InlineOnly
inline fun MediaTrackConstraintSet(width: dynamic = undefined, height: dynamic = undefined, aspectRatio: dynamic = undefined, frameRate: dynamic = undefined, facingMode: dynamic = undefined, resizeMode: dynamic = undefined, volume: dynamic = undefined, sampleRate: dynamic = undefined, sampleSize: dynamic = undefined, echoCancellation: dynamic = undefined, autoGainControl: dynamic = undefined, noiseSuppression: dynamic = undefined, latency: dynamic = undefined, channelCount: dynamic = undefined, deviceId: dynamic = undefined, groupId: dynamic = undefined): MediaTrackConstraintSet {
    val o = js("({})")

    o["width"] = width
    o["height"] = height
    o["aspectRatio"] = aspectRatio
    o["frameRate"] = frameRate
    o["facingMode"] = facingMode
    o["resizeMode"] = resizeMode
    o["volume"] = volume
    o["sampleRate"] = sampleRate
    o["sampleSize"] = sampleSize
    o["echoCancellation"] = echoCancellation
    o["autoGainControl"] = autoGainControl
    o["noiseSuppression"] = noiseSuppression
    o["latency"] = latency
    o["channelCount"] = channelCount
    o["deviceId"] = deviceId
    o["groupId"] = groupId

    return o
}

external interface MediaTrackSettings {
    var width: Int?
        get() = definedExternally
        set(value) = definedExternally
    var height: Int?
        get() = definedExternally
        set(value) = definedExternally
    var aspectRatio: Double?
        get() = definedExternally
        set(value) = definedExternally
    var frameRate: Double?
        get() = definedExternally
        set(value) = definedExternally
    var facingMode: String?
        get() = definedExternally
        set(value) = definedExternally
    var resizeMode: String?
        get() = definedExternally
        set(value) = definedExternally
    var volume: Double?
        get() = definedExternally
        set(value) = definedExternally
    var sampleRate: Int?
        get() = definedExternally
        set(value) = definedExternally
    var sampleSize: Int?
        get() = definedExternally
        set(value) = definedExternally
    var echoCancellation: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var autoGainControl: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var noiseSuppression: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var latency: Double?
        get() = definedExternally
        set(value) = definedExternally
    var channelCount: Int?
        get() = definedExternally
        set(value) = definedExternally
    var deviceId: String?
        get() = definedExternally
        set(value) = definedExternally
    var groupId: String?
        get() = definedExternally
        set(value) = definedExternally
}

@kotlin.internal.InlineOnly
inline fun MediaTrackSettings(width: Int? = undefined, height: Int? = undefined, aspectRatio: Double? = undefined, frameRate: Double? = undefined, facingMode: String? = undefined, resizeMode: String? = undefined, volume: Double? = undefined, sampleRate: Int? = undefined, sampleSize: Int? = undefined, echoCancellation: Boolean? = undefined, autoGainControl: Boolean? = undefined, noiseSuppression: Boolean? = undefined, latency: Double? = undefined, channelCount: Int? = undefined, deviceId: String? = undefined, groupId: String? = undefined): MediaTrackSettings {
    val o = js("({})")

    o["width"] = width
    o["height"] = height
    o["aspectRatio"] = aspectRatio
    o["frameRate"] = frameRate
    o["facingMode"] = facingMode
    o["resizeMode"] = resizeMode
    o["volume"] = volume
    o["sampleRate"] = sampleRate
    o["sampleSize"] = sampleSize
    o["echoCancellation"] = echoCancellation
    o["autoGainControl"] = autoGainControl
    o["noiseSuppression"] = noiseSuppression
    o["latency"] = latency
    o["channelCount"] = channelCount
    o["deviceId"] = deviceId
    o["groupId"] = groupId

    return o
}

external open class MediaStreamTrackEvent(type: String, eventInitDict: MediaStreamTrackEventInit) : Event {
    open val track: MediaStreamTrack
}

external interface MediaStreamTrackEventInit : EventInit {
    var track: MediaStreamTrack?
        get() = definedExternally
        set(value) = definedExternally
}

@kotlin.internal.InlineOnly
inline fun MediaStreamTrackEventInit(track: MediaStreamTrack?, bubbles: Boolean? = false, cancelable: Boolean? = false, composed: Boolean? = false): MediaStreamTrackEventInit {
    val o = js("({})")

    o["track"] = track
    o["bubbles"] = bubbles
    o["cancelable"] = cancelable
    o["composed"] = composed

    return o
}

external open class OverconstrainedErrorEvent(type: String, eventInitDict: OverconstrainedErrorEventInit) : Event {
    open val error: dynamic
}

external interface OverconstrainedErrorEventInit : EventInit {
    var error: dynamic /* = null */
        get() = definedExternally
        set(value) = definedExternally
}

@kotlin.internal.InlineOnly
inline fun OverconstrainedErrorEventInit(error: dynamic = null, bubbles: Boolean? = false, cancelable: Boolean? = false, composed: Boolean? = false): OverconstrainedErrorEventInit {
    val o = js("({})")

    o["error"] = error
    o["bubbles"] = bubbles
    o["cancelable"] = cancelable
    o["composed"] = composed

    return o
}

external abstract class MediaDevices : EventTarget {
    open var ondevicechange: ((Event) -> dynamic)?
    fun enumerateDevices(): Promise<dynamic>
    fun getSupportedConstraints(): MediaTrackSupportedConstraints
    fun getUserMedia(constraints: MediaStreamConstraints = definedExternally): Promise<MediaStream>
}

external abstract class MediaDeviceInfo {
    open val deviceId: String
    open val kind: MediaDeviceKind
    open val label: String
    open val groupId: String
    fun toJSON(): dynamic
}

external abstract class InputDeviceInfo : MediaDeviceInfo {
    fun getCapabilities(): MediaTrackCapabilities
}

external interface MediaStreamConstraints {
    var video: dynamic /* = false */
        get() = definedExternally
        set(value) = definedExternally
    var audio: dynamic /* = false */
        get() = definedExternally
        set(value) = definedExternally
}

@kotlin.internal.InlineOnly
inline fun MediaStreamConstraints(video: dynamic = false, audio: dynamic = false): MediaStreamConstraints {
    val o = js("({})")

    o["video"] = video
    o["audio"] = audio

    return o
}

external interface ConstrainablePattern {
    var onoverconstrained: ((Event) -> dynamic)?
    fun getCapabilities(): Capabilities
    fun getConstraints(): Constraints
    fun getSettings(): Settings
    fun applyConstraints(constraints: Constraints = definedExternally): Promise<Unit>
}

external interface DoubleRange {
    var max: Double?
        get() = definedExternally
        set(value) = definedExternally
    var min: Double?
        get() = definedExternally
        set(value) = definedExternally
}

@kotlin.internal.InlineOnly
inline fun DoubleRange(max: Double? = undefined, min: Double? = undefined): DoubleRange {
    val o = js("({})")

    o["max"] = max
    o["min"] = min

    return o
}

external interface ConstrainDoubleRange : DoubleRange {
    var exact: Double?
        get() = definedExternally
        set(value) = definedExternally
    var ideal: Double?
        get() = definedExternally
        set(value) = definedExternally
}

@kotlin.internal.InlineOnly
inline fun ConstrainDoubleRange(exact: Double? = undefined, ideal: Double? = undefined, max: Double? = undefined, min: Double? = undefined): ConstrainDoubleRange {
    val o = js("({})")

    o["exact"] = exact
    o["ideal"] = ideal
    o["max"] = max
    o["min"] = min

    return o
}

external interface ULongRange {
    var max: Int?
        get() = definedExternally
        set(value) = definedExternally
    var min: Int?
        get() = definedExternally
        set(value) = definedExternally
}

@kotlin.internal.InlineOnly
inline fun ULongRange(max: Int? = undefined, min: Int? = undefined): ULongRange {
    val o = js("({})")

    o["max"] = max
    o["min"] = min

    return o
}

external interface ConstrainULongRange : ULongRange {
    var exact: Int?
        get() = definedExternally
        set(value) = definedExternally
    var ideal: Int?
        get() = definedExternally
        set(value) = definedExternally
}

@kotlin.internal.InlineOnly
inline fun ConstrainULongRange(exact: Int? = undefined, ideal: Int? = undefined, max: Int? = undefined, min: Int? = undefined): ConstrainULongRange {
    val o = js("({})")

    o["exact"] = exact
    o["ideal"] = ideal
    o["max"] = max
    o["min"] = min

    return o
}

external interface ConstrainBooleanParameters {
    var exact: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var ideal: Boolean?
        get() = definedExternally
        set(value) = definedExternally
}

@kotlin.internal.InlineOnly
inline fun ConstrainBooleanParameters(exact: Boolean? = undefined, ideal: Boolean? = undefined): ConstrainBooleanParameters {
    val o = js("({})")

    o["exact"] = exact
    o["ideal"] = ideal

    return o
}

external interface ConstrainDOMStringParameters {
    var exact: dynamic
        get() = definedExternally
        set(value) = definedExternally
    var ideal: dynamic
        get() = definedExternally
        set(value) = definedExternally
}

@kotlin.internal.InlineOnly
inline fun ConstrainDOMStringParameters(exact: dynamic = undefined, ideal: dynamic = undefined): ConstrainDOMStringParameters {
    val o = js("({})")

    o["exact"] = exact
    o["ideal"] = ideal

    return o
}

external interface Capabilities {
}

@kotlin.internal.InlineOnly
inline fun Capabilities(): Capabilities {
    val o = js("({})")


    return o
}

external interface Settings {
}

@kotlin.internal.InlineOnly
inline fun Settings(): Settings {
    val o = js("({})")


    return o
}

external interface ConstraintSet {
}

@kotlin.internal.InlineOnly
inline fun ConstraintSet(): ConstraintSet {
    val o = js("({})")


    return o
}

external interface Constraints : ConstraintSet {
    var advanced: Array<ConstraintSet>?
        get() = definedExternally
        set(value) = definedExternally
}

@kotlin.internal.InlineOnly
inline fun Constraints(advanced: Array<ConstraintSet>? = undefined): Constraints {
    val o = js("({})")

    o["advanced"] = advanced

    return o
}

/* please, don't implement this interface! */
@Suppress("NESTED_CLASS_IN_EXTERNAL_INTERFACE")
external interface MediaStreamTrackState {
    companion object
}
inline val MediaStreamTrackState.Companion.LIVE: MediaStreamTrackState get() = "live".asDynamic().unsafeCast<MediaStreamTrackState>()
inline val MediaStreamTrackState.Companion.ENDED: MediaStreamTrackState get() = "ended".asDynamic().unsafeCast<MediaStreamTrackState>()

/* please, don't implement this interface! */
@Suppress("NESTED_CLASS_IN_EXTERNAL_INTERFACE")
external interface VideoFacingModeEnum {
    companion object
}
inline val VideoFacingModeEnum.Companion.USER: VideoFacingModeEnum get() = "user".asDynamic().unsafeCast<VideoFacingModeEnum>()
inline val VideoFacingModeEnum.Companion.ENVIRONMENT: VideoFacingModeEnum get() = "environment".asDynamic().unsafeCast<VideoFacingModeEnum>()
inline val VideoFacingModeEnum.Companion.LEFT: VideoFacingModeEnum get() = "left".asDynamic().unsafeCast<VideoFacingModeEnum>()
inline val VideoFacingModeEnum.Companion.RIGHT: VideoFacingModeEnum get() = "right".asDynamic().unsafeCast<VideoFacingModeEnum>()

/* please, don't implement this interface! */
@Suppress("NESTED_CLASS_IN_EXTERNAL_INTERFACE")
external interface VideoResizeModeEnum {
    companion object
}
inline val VideoResizeModeEnum.Companion.NONE: VideoResizeModeEnum get() = "none".asDynamic().unsafeCast<VideoResizeModeEnum>()
inline val VideoResizeModeEnum.Companion.CROP_AND_SCALE: VideoResizeModeEnum get() = "crop-and-scale".asDynamic().unsafeCast<VideoResizeModeEnum>()

/* please, don't implement this interface! */
@Suppress("NESTED_CLASS_IN_EXTERNAL_INTERFACE")
external interface MediaDeviceKind {
    companion object
}
inline val MediaDeviceKind.Companion.AUDIOINPUT: MediaDeviceKind get() = "audioinput".asDynamic().unsafeCast<MediaDeviceKind>()
inline val MediaDeviceKind.Companion.AUDIOOUTPUT: MediaDeviceKind get() = "audiooutput".asDynamic().unsafeCast<MediaDeviceKind>()
inline val MediaDeviceKind.Companion.VIDEOINPUT: MediaDeviceKind get() = "videoinput".asDynamic().unsafeCast<MediaDeviceKind>()

