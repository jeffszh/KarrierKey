package cn.jeff.app.karrier_key.api;

import com.sun.jna.Native;
import com.sun.jna.platform.win32.WinNT;
import com.sun.jna.platform.win32.WinUser;
import com.sun.jna.win32.StdCallLibrary;
import com.sun.jna.win32.W32APIOptions;

public interface MyApi extends StdCallLibrary, WinUser, WinNT {

	/**
	 * The instance.
	 */
	MyApi INSTANCE = Native.load("user32", MyApi.class, W32APIOptions.DEFAULT_OPTIONS);

	/**
	 * This function determines whether a key is up or down at the time the
	 * function is called, and whether the key was pressed after a previous call
	 * to GetAsyncKeyState.
	 *
	 * @param vKey Specifies one of 256 possible virtual-key codes.
	 * @return If the function succeeds, the return value specifies whether the
	 * key was pressed since the last call to GetAsyncKeyState, and
	 * whether the key is currently up or down. If the most significant
	 * bit is set, the key is down.
	 */
	short GetAsyncKeyState(int vKey);

	short GetKeyState(int vKey);

}
