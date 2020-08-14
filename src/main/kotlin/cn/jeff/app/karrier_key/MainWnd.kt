package cn.jeff.app.karrier_key

import cn.jeff.app.karrier_key.api.MyApi
import cn.jeff.app.karrier_key.event.TimeTick
import com.sun.jna.platform.KeyboardUtils
import com.sun.jna.platform.win32.User32
import com.sun.jna.platform.win32.WinDef
import com.sun.jna.platform.win32.WinUser
import javafx.fxml.FXMLLoader
import javafx.scene.layout.BorderPane
import tornadofx.*
import java.awt.event.KeyEvent

class MainWnd : View("鋼鉄の咆哮3 飛機自動出擊鍵盤助手") {

	override val root: BorderPane
	private val j: MainWndJ
	private val user = User32.INSTANCE
	private val myApi = MyApi.INSTANCE
	//	private val kernel = Kernel32.INSTANCE
	private val hWnd: WinDef.HWND by lazy(this::findSelfWindowHandle)

	init {
		val loader = FXMLLoader()
		root = loader.load(javaClass.getResourceAsStream(
				"/cn/jeff/app/karrier_key/MainWnd.fxml"
		))
		j = loader.getController()
		j.k = this

//		j.label01.text = "使用方法：\n当ScrollLock灯亮，每秒自动按小键盘减号键；\n当ScrollLock灯灭，停止。"
		println(j.label01.text)

		primaryStage.setOnCloseRequest {
			println("窗口关闭：$it")
		}

		subscribe<TimeTick> {
			onTimeTick()
		}
	}

	private fun findSelfWindowHandle(): WinDef.HWND {
		return user.FindWindow(null, title)
	}

	fun btn01Click() {
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

	fun btnCloseClick() {
		close()
	}

	private fun onTimeTick() {
		val scrollLockState = myApi.GetKeyState(KeyEvent.VK_SCROLL_LOCK)
		if (scrollLockState != 0.toShort()) {
			performKeyPress(KeyEvent.VK_SUBTRACT.toByte(), 0x2D, true)
		}
	}

	private fun performKeyPress(vk: Byte, sc: Byte, ext: Boolean) {
		val extFlag = if (ext) WinUser.KEYBDINPUT.KEYEVENTF_EXTENDEDKEY else 0

		myApi.keybd_event(vk, sc, extFlag, 0)
		Thread.sleep(100)
		myApi.keybd_event(vk, sc, extFlag or WinUser.KEYBDINPUT.KEYEVENTF_KEYUP, 0)
		Thread.sleep(100)
	}

}
