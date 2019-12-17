package cash.z.ecc.android.lockbox

import android.content.Context
import de.adorsys.android.securestoragelibrary.SecurePreferences
import java.nio.ByteBuffer
import java.nio.CharBuffer
import java.nio.charset.StandardCharsets
import java.util.*


class LockBox(private val appContext: Context) : LockBoxProvider {

    override fun setBytes(key: String, value: ByteArray) {
        SecurePreferences.setValue(appContext, key, value.toHex())
    }

    override fun getBytes(key: String): ByteArray {
        return SecurePreferences.getStringValue(appContext, key, null)!!.fromHex()
    }

    override fun setCharsUtf8(key: String, value: CharArray) {
        setBytes(key, value.toBytes())
    }

    override fun getCharsUtf8(key: String): CharArray {
        return getBytes(key).fromBytes()
    }


    //
    // Extensions (TODO: find library that works better with arrays of bytes and chars)
    //

    private fun ByteArray.toHex(): String {
        val sb = StringBuilder(size * 2)
        for (b in this)
            sb.append(String.format("%02x", b))
        return sb.toString()
    }

    private fun String.fromHex(): ByteArray {
        val len = length
        val data = ByteArray(len / 2)
        var i = 0
        while (i < len) {
            data[i / 2] =
                ((Character.digit(this[i], 16) shl 4) + Character.digit(this[i + 1], 16)).toByte()
            i += 2
        }
        return data
    }

    private fun CharArray.toBytes(): ByteArray {
        val byteBuffer = StandardCharsets.UTF_8.encode(CharBuffer.wrap(this))
        return Arrays.copyOf(byteBuffer.array(), byteBuffer.limit());
    }

    private fun ByteArray.fromBytes(): CharArray {
        val charBuffer = StandardCharsets.UTF_8.decode(ByteBuffer.wrap(this))
        return Arrays.copyOf(charBuffer.array(), charBuffer.limit())
    }
}

