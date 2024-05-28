package at.hannibal2.skyhanni.utils

import at.hannibal2.skyhanni.test.command.ErrorManager
import net.minecraft.client.Minecraft
import java.awt.Desktop
import java.io.File
import java.io.IOException
import java.net.URI

object OSUtils {

    //     @Deprecated("Use openBrowserWithChat() instead.", ReplaceWith("openBrowserWithChat(url)"))
    @JvmStatic
    fun openBrowser(url: String) {
        openBrowserWithChat(url, false)
    }

    fun openBrowserWithChat(url: String, sendMessage: Boolean = false) {
        val desktopSupported = Desktop.isDesktopSupported()
        val supportedActionBrowse = Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)
        if (desktopSupported && supportedActionBrowse) {
            try {
                Desktop.getDesktop().browse(URI(url))
                if (sendMessage) ChatUtils.chat("Opened url: §b$url")
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

    fun openFile(path: String, absolute: Boolean = false, sendMessage: Boolean = false) {
        if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.OPEN)) {
            try {
                if (absolute && path.isNotEmpty()) Desktop.getDesktop().open(File(path))
                else Desktop.getDesktop().open(File("${Minecraft.getMinecraft().mcDataDir}${File.separatorChar}$path"))
                if (sendMessage) {
                    val displayPath = if (absolute) path
                    else ".minecraft${if (path.isNotEmpty()) File.separatorChar else ""}$path"
                    ChatUtils.chat("Opened file location: §b$displayPath")
                }
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

    fun openBrowserCommand(args: Array<String>) {
        if (args.isEmpty()) return
        val sendMessage = if (args.size > 1) args[1] == "true" else false
        openBrowserWithChat(args[0], sendMessage)
    }

    fun openFileCommand(args: Array<String>, absolute: Boolean = false) {
        val path = if (args.isNotEmpty()) args[0] else ""
        val sendMessage = if (args.size > 1) args[1] == "true" else false
        openFile(path, absolute, sendMessage)
    }

    fun copyToClipboard(text: String) {
        ClipboardUtils.copyToClipboard(text)
    }

    suspend fun readFromClipboard() = ClipboardUtils.readFromClipboard()
}
