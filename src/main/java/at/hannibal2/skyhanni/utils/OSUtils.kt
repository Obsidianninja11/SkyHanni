package at.hannibal2.skyhanni.utils

import at.hannibal2.skyhanni.SkyHanniMod.Companion.minecraftDirectory
import at.hannibal2.skyhanni.test.command.ErrorManager
import java.awt.Desktop
import java.io.File
import java.io.IOException
import java.net.URI

object OSUtils {

    @JvmStatic
    fun openBrowser(url: String) {
        if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
            try {
                Desktop.getDesktop().browse(URI(url))
            } catch (e: IOException) {
                ErrorManager.logErrorWithData(e, "Error opening website: $url")
            }
        } else {
            copyToClipboard(url)
            ErrorManager.logErrorStateWithData(
                "Web browser is not supported! Copied url to clipboard.",
                "Web browser not supported."
            )
        }
    }

    fun openBrowserCommand(array: Array<String>) {
        if (array.size != 1) return
        openBrowser(array[0])
    }

    fun openFile(path: String, absolute: Boolean = false) {
        if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.OPEN)) {
            try {
                if (absolute) Desktop.getDesktop().open(File(path))
                else Desktop.getDesktop().open(File("${minecraftDirectory.path}${File.separatorChar}$path"))
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

    fun copyToClipboard(text: String) {
        ClipboardUtils.copyToClipboard(text)
    }

    suspend fun readFromClipboard() = ClipboardUtils.readFromClipboard()
}
