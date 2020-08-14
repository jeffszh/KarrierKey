package cn.jeff.app.karrier_key

import cn.jeff.app.karrier_key.event.TimeTick
import tornadofx.*
import kotlin.concurrent.timer

class KarrierKey : App(MainWnd::class) {

	init {
		timer(name = "TickTimer", daemon = true, period = 1000) {
			fire(TimeTick)
		}
	}

}
