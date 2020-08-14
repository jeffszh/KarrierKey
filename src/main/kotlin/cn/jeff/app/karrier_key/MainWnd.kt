package cn.jeff.app.karrier_key

import cn.jeff.app.karrier_key.api.MyApi
import com.sun.jna.platform.KeyboardUtils
import com.sun.jna.platform.win32.Kernel32
import com.sun.jna.platform.win32.User32
import com.sun.jna.platform.win32.WinDef
import com.sun.jna.platform.win32.WinUser
import javafx.fxml.FXMLLoader
import javafx.scene.layout.BorderPane
import tornadofx.*
import java.awt.event.KeyEvent

class MainWnd : View("试试WinAPI") {

	override val root: BorderPane
	private val j: MainWndJ
	private val user = User32.INSTANCE
	private val myApi = MyApi.INSTANCE
	private val kernel = Kernel32.INSTANCE
	private val hWnd: WinDef.HWND by lazy(this::findSelfWindowHandle)
	private val atom01 = 1001
	private val atom02 = 1002

	init {
		val loader = FXMLLoader()
		root = loader.load(javaClass.getResourceAsStream(
				"/cn/jeff/app/karrier_key/MainWnd.fxml"
		))
		j = loader.getController()
		j.k = this

		runLater {
			registerKeys()
		}

		primaryStage.setOnCloseRequest {
			println("窗口关闭：$it")
			unregisterKeys()
		}
	}

	private fun findSelfWindowHandle(): WinDef.HWND {
		return user.FindWindow(null, title)
	}

	private fun registerKeys() {
		user.RegisterHotKey(hWnd, atom01, WinUser.MOD_CONTROL, KeyEvent.VK_SUBTRACT)
		user.RegisterHotKey(hWnd, atom02, WinUser.MOD_CONTROL, KeyEvent.VK_ADD)
	}

	private fun unregisterKeys() {
		user.UnregisterHotKey(hWnd.pointer, atom01)
		user.UnregisterHotKey(hWnd.pointer, atom02)
	}

	fun btn01Click() {
//		val hWnd = user.FindWindow(null, "试试WinAPI")
		information("hWnd = $hWnd")
	}

	fun btn02Click() {
		val scrollLock = myApi.GetAsyncKeyState(KeyEvent.VK_SCROLL_LOCK)
		val keys = ByteArray(256)
		user.GetKeyboardState(keys)
		val key2 = KeyboardUtils.isPressed(KeyEvent.VK_SUBTRACT)
		information("scrollLock = $scrollLock, key2 = $key2")
		keys.forEach {
			print("$it, ")
		}
		println("\n${keys[KeyEvent.VK_SCROLL_LOCK]}")
		val sc = myApi.GetKeyState(KeyEvent.VK_SCROLL_LOCK)
		println("sc = $sc")
	}

}
