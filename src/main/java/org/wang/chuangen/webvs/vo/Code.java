package org.wang.chuangen.webvs.vo;

/**
 * 结果编码常量
 * @author 王传根
 * @date 2016-1-25 上午10:36:35
 */
public interface Code {
	/**
	 * 空值 0
	 */
	int NULL_NUMBER=0;//空值
	/**
	 * 正确的 有效的 200
	 */
	int VALID=200;//正确的
	/**
	 * 运行错误 500
	 */
	int RUN_ERROR=500;//运行错误
	/**
	 * 信息不存在 404
	 */
	int INFO_NOT_EXIST=404;//信息不存在
	/**
	 * 信息异常 600
	 */
	int INFO_ANOMALY=600;//信息异常
	/**
	 * 用户不存在 4041
	 */
	int USER_NOT_EXIST=4041;//用户不存在
	/**
	 * 用户信息异常 6001
	 */
	int USER_ANOMALY=6001;//用户信息异常
	/**
	 * 无权限的 401
	 */
	int NOT_ALLOW=401;//无权限的
	/**
	 * 禁止的 403
	 */
	int FORBID=403;//禁止的
	/**
	 * 无效的 406
	 */
	int INVALID=406;//无效的
	/**
	 * 失败的 501
	 */
	int FAILURE=501;//失败的
	/**
	 * 错误的 502
	 */
	int UNTRUE=502;//错误的
	/**
	 * 密码错误 602
	 */
	int PASS_UNTRUE=602;//密码错误
	/**
	 * 已经登录 610
	 */
	int ALREADY_LOGIN=610;//已经登录
	/**
	 * 尚未登录 611
	 */
	int NOT_LOGIN=611;//尚未登录
	/**
	 * 登录超时 612
	 */
	int LOGIN_OUT=612;//登录超时
	/**
	 * 异地登录 613
	 */
	int OFFSITE_LOGIN=613;//异地登录
	/**
	 * 已经注册 621
	 */
	int ALREADY_ENROLL=621;//已经注册
	/**
	 * 参数错误 631
	 */
	int PARAM_ERROR=631;
	/**
	 * 过期的,过时的 641
	 */
	int OVERDUE=641;
	/**
	 * 重复的 642
	 */
	int REPETITIVE=642;
	/**
	 * 余额不足 661
	 */
	int INSUFFICIENT=661;//
	/**
	 * 提示信息 208
	 */
	int MESSAGE_INFO=208;//提示信息
	/**
	 * 错误信息 504
	 */
	int MESSAGE_ERROR=504;//错误信息
	/**
	 * 其他 699
	 */
	int OTHER=699;//其他
	/**
	 * 关闭命令 702
	 */
	int CLOSE=702;
	/**
	 * 改变日志等级 716
	 */
	int CHANGE_LOG_LEVEL=716;
}
