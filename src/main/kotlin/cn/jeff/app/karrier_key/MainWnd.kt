package cn.jeff.app.karrier_key

import com.sun.jna.platform.win32.*
import javafx.fxml.FXMLLoader
import javafx.scene.layout.BorderPane
import tornadofx.*
import java.awt.event.KeyEvent

class MainWnd : View("试试WinAPI") {

	override val root: BorderPane
	private val j: MainWndJ
	private val user = User32.INSTANCE
	private val kernel = Kernel32.INSTANCE
	private val hWnd: WinDef.HWND
	private val atom01 = 1001
	private val atom02 = 1002

	init {
		val loader = FXMLLoader()
		root = loader.load(javaClass.getResourceAsStream(
				"/cn/jeff/app/karrier_key/MainWnd.fxml"
		))
		j = loader.getController()
		j.k = this

		hWnd = user.FindWindow(null, "试试WinAPI")
		registerKeys()

		primaryStage.setOnCloseRequest {
			println("窗口关闭：$it")
			unregisterKeys()
		}
	}

	private fun registerKeys() {
		user.RegisterHotKey(hWnd, atom01, WinUser.MOD_CONTROL, KeyEvent.VK_SUBTRACT)
		user.RegisterHotKey(hWnd, atom01, WinUser.MOD_CONTROL, KeyEvent.VK_ADD)
	}

	private fun unregisterKeys() {
		user.UnregisterHotKey(hWnd.pointer, atom01)
		user.UnregisterHotKey(hWnd.pointer, atom01)
	}

	fun btn01Click() {
		val hWnd = user.FindWindow(null, "试试WinAPI")
		information("hWnd = $hWnd")
	}

}
