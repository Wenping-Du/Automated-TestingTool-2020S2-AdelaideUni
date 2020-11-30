package com.adelaide.autotest;
/**
 * @author Wenping(Deb) Du
 */
import java.io.IOException;
import java.io.OutputStream;

public class RootShellCmd {

	private static class RootShellCmdHolder {  
		private static final RootShellCmd INSTANCE = new RootShellCmd();  
	} 
	/**
	 * 单例
	 * @return
	 */
	public static RootShellCmd getInstance(){
		return RootShellCmdHolder.INSTANCE;
	}
	
	private OutputStream os;

	/**
	 * 执行shell指令
	 * 
	 * @param cmd
	 *            指令
	 */
	public final void exec(String cmd) {
		try {
//			if(os == null){
				os = Runtime.getRuntime().exec("su").getOutputStream();
//			}
			os.write(cmd.getBytes());
		} catch (Exception e) {
			e.printStackTrace();
		} finally{
			if (os != null) {
				try {
					os.flush();
					os.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * 后台模拟全局按键
	 */
	public final void simulateSomeKeys(int [] keyCodes) {
		String cmd = "";
		for (int i : keyCodes) {
			cmd += "input keyevent " + i + "\n";
		}
		exec(cmd);
	}

	/**
	 * 后台模拟全局按键
	 * 
	 * @param strs
	 */
	public final void simulateSomeStrs(String strs) {
		exec("input text " + strs + "\n");
	}
	

	/**
	 * 输入下划线
	 */
	public final void inputTapXY(int x, int y) {
		String cmd = "input tap " + x + " " + y;
		try {
			os = Runtime.getRuntime().exec("su").getOutputStream();
			os.write(cmd.getBytes());
		} catch (Exception e) {
			e.printStackTrace();
		} finally{
			if (os != null) {
				try {
					os.flush();
					os.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
}