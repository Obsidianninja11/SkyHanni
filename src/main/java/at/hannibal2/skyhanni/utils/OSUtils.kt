package at.hannibal2.skyhanni.utils

import at.hannibal2.skyhanni.test.command.ErrorManager
import net.minecraft.client.Minecraft
import java.awt.Desktop
import java.io.File
import java.io.IOException
import java.net.URI

object OSUtils {

    @JvmStatic
    fun openBrowser(url: String) {
        val desktopSupported = Desktop.isDesktopSupported()
        val supportedActionBrowse = Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)
        if (desktopSupported && supportedActionBrowse) {
            try {
                Desktop.getDesktop().browse(URI(url))
            } catch (e: IOException) {
                ErrorManager.logErrorWithData(
                    e, "Error while opening website",
                    "url" to url
                )
            }
        } else {
            copyToClipboard(url)
            ErrorManager.logErrorStateWithData(
                "Cannot open website! Copied url to clipboard instead", "Web browser is not supported",
                "url" to url,
                "desktopSupported" to desktopSupported,
                "supportedActionBrowse" to supportedActionBrowse,
            )
        }
    }

    fun openBrowserCommand(array: Array<String>) {
        if (array.isNotEmpty()) {
            openBrowser(array.joinToString(""))
            ChatUtils.chat("Opening url: ${array.joinToString("")}")
        }
    }

    fun openFile(path: String, absolute: Boolean = false, sendMessage: Boolean = false) {
        if (sendMessage) {
            val displayPath = if (absolute) path
            else ".minecraft${if (path.isNotEmpty()) File.separatorChar else ""}$path"
            ChatUtils.chat("Opening file: $displayPath")
        }
        if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.OPEN)) {
            try {
                if (absolute) Desktop.getDesktop().open(File(path))
                else Desktop.getDesktop().open(File("${Minecraft.getMinecraft().mcDataDir}${File.separatorChar}$path"))
            } catch (e: IOException) {
                ErrorManager.logErrorWithData(e, "Error opening file: $path")
            }
        } else {
            copyToClipboard(path)
            ErrorManager.logErrorStateWithData(
                "File opening is not supported! Copied path to clipboard.",
                "File opening not supported."
            )
        }
    }

    fun openFileCommand(array: Array<String>) {
        if (array.isNotEmpty()) openFile(array.joinToString(""), absolute = true, sendMessage = true)
    }

    fun copyToClipboard(text: String) {
        ClipboardUtils.copyToClipboard(text)
    }

    suspend fun readFromClipboard() = ClipboardUtils.readFromClipboard()
}
