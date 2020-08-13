package cn.jeff.app.karrier_key

import com.sun.jna.platform.win32.User32
import javafx.fxml.FXMLLoader
import javafx.scene.layout.BorderPane
import tornadofx.*

class MainWnd : View("试试WinAPI") {

	override val root: BorderPane
	private val j: MainWndJ
	private val user = User32.INSTANCE
//	private val kernel = Kernel32.INSTANCE

	init {
		val loader = FXMLLoader()
		root = loader.load(javaClass.getResourceAsStream(
				"/cn/jeff/app/karrier_key/MainWnd.fxml"
		))
		j = loader.getController()
		j.k = this
	}

	fun btn01Click() {
		val hWnd = user.FindWindow(null, "试试WinAPI")
		information("hWnd = $hWnd")
	}

}
