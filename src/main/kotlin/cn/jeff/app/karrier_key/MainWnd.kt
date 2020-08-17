package cn.jeff.app.karrier_key

import cn.jeff.app.karrier_key.api.MyApi
import cn.jeff.app.karrier_key.event.TimeTick
import com.sun.jna.platform.KeyboardUtils
import com.sun.jna.platform.win32.User32
import com.sun.jna.platform.win32.WinDef
import com.sun.jna.platform.win32.WinUser
import javafx.beans.property.SimpleIntegerProperty
import javafx.fxml.FXMLLoader
import javafx.scene.layout.BorderPane
import javafx.scene.media.Media
import javafx.scene.media.MediaPlayer
import javafx.scene.paint.Color
import tornadofx.*
import java.awt.event.KeyEvent

class MainWnd : View("鋼鉄の咆哮3 飛機自動出擊鍵盤助手") {

	override val root: BorderPane
	private val j: MainWndJ
	private val user = User32.INSTANCE
	private val myApi = MyApi.INSTANCE
	//	private val kernel = Kernel32.INSTANCE
	private val hWnd: WinDef.HWND by lazy(this::findSelfWindowHandle)
	private val stickCount = SimpleIntegerProperty(0)
	private val maxStickCount = 3
	private val audio01 = Media(javaClass.getResource("/wav/On.wav").toString())
	private val audio02 = Media(javaClass.getResource("/wav/Off.wav").toString())
	private val player01 = MediaPlayer(audio01)
	private val player02 = MediaPlayer(audio02)

	init {
		val loader = FXMLLoader()
		root = loader.load(javaClass.getResourceAsStream(
				"/cn/jeff/app/karrier_key/MainWnd.fxml"
		))
		j = loader.getController()
		j.k = this

		println(j.label01.text)

		j.circle01.fillProperty().bind(stickCount.objectBinding {
			if (it == maxStickCount) {
				Color.GOLD
			} else {
				Color.GRAY
			}
		})

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
		val subtractKeyState = myApi.GetAsyncKeyState(KeyEvent.VK_SUBTRACT)
		val addKeyState = myApi.GetAsyncKeyState(KeyEvent.VK_ADD)
		println((subtractKeyState.toInt() and 0xFFFF).toString(16))
		when {
			// 若按下过加号键，取消自动连发。
			addKeyState != 0.toShort() -> {
				if (stickCount.value == maxStickCount) {
					player02.stop()
					player02.play()
				}
				stickCount.value = 0
			}
			// 若长按没达到时间就松开，取消自动连发。
			(subtractKeyState.toInt() and 0x8000) == 0 -> {
				if (stickCount.value < maxStickCount) {
					stickCount.value = 0
				}
			}
			// 若长按达到时间，激活自动连发。
			else -> {
				if (stickCount.value < maxStickCount) {
					stickCount.value++
					if (stickCount.value == maxStickCount) {
						player01.stop()
						player01.play()
//						Toolkit.getDefaultToolkit().beep()
					}
				}
			}
		}
		if (stickCount.value >= maxStickCount) {
			performKeyPress(KeyEvent.VK_SUBTRACT.toByte(), 0x2D, true)
		}
	}

	@Suppress("SameParameterValue")
	private fun performKeyPress(vk: Byte, sc: Byte, ext: Boolean) {
		val extFlag = if (ext) WinUser.KEYBDINPUT.KEYEVENTF_EXTENDEDKEY else 0

		myApi.keybd_event(vk, sc, extFlag, 0)
		Thread.sleep(100)
		myApi.keybd_event(vk, sc, extFlag or WinUser.KEYBDINPUT.KEYEVENTF_KEYUP, 0)
		Thread.sleep(100)
	}

}
