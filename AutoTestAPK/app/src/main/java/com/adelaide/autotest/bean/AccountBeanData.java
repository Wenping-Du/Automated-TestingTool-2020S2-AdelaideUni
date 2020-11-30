package com.adelaide.autotest.bean;

import com.linique.cloudpsd.json.annotations.Expose;

/**
 * @author Wenping(Deb) Du
 */
public class AccountBeanData {
	@Expose
	public String to_account = "";
	@Expose
	public String to_user_name = "";
	@Expose
	public int status_code;
	@Expose
	public String status_message;
	@Expose
	public int transaction_id;
	@Expose
	public double to_amount;
	
	private int local_status = 0;//0.初始化状态； 1.已完成汇款操作，操作成功；2.账号错误；3.支付密码错误；4.程序异常崩溃

	public int getLocal_status() {
		return local_status;
	}

	public void setLocal_status(int local_status) {
		this.local_status = local_status;
	}
	
	
}
