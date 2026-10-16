package com.wireless.audiostreamer.network

import android.os.Environment
import java.io.File

object FsBrowser {
    private val root: File by lazy { Environment.getExternalStorageDirectory() }

    data class Entry(val name: String, val isDir: Boolean, val size: Long)

    fun resolve(rel: String): File? {
        val clean = rel.trim('/', ' ')
        val f = if (clean.isEmpty()) root else File(root, clean)
        val rootCanon = root.canonicalPath
        val fCanon = try { f.canonicalPath } catch (e: Exception) { return null }
        if (fCanon != rootCanon && !fCanon.startsWith(rootCanon + File.separator)) return null
        return f
    }

    fun list(rel: String): List<Entry>? {
        val dir = resolve(rel) ?: return null
        if (!dir.isDirectory) return null
        val files = dir.listFiles() ?: return emptyList()
        return files
            .filter { !it.name.startsWith(".") }
            .sortedWith(compareBy({ !it.isDirectory }, { it.name.lowercase() }))
            .map { Entry(it.name, it.isDirectory, if (it.isDirectory) 0L else it.length()) }
    }

    fun encodeList(entries: List<Entry>): ByteArray =
        entries.joinToString("\n") { "${if (it.isDir) "D" else "F"}|${it.size}|${it.name}" }
            .toByteArray(Charsets.UTF_8)

    fun decodeList(b: ByteArray): List<Entry> {
        val s = String(b, Charsets.UTF_8)
        if (s.isEmpty()) return emptyList()
        return s.split("\n").mapNotNull {
            val p = it.split("|", limit = 3)
            if (p.size != 3) null else Entry(p[2], p[0] == "D", p[1].toLongOrNull() ?: 0L)
        }
    }
}
