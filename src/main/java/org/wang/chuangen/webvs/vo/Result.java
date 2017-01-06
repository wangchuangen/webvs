package org.wang.chuangen.webvs.vo;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.serializer.SimplePropertyPreFilter;

/**
 * 通用结果对象
 * @author 王传根
 * @date 2016-1-25 上午10:54:28
 */
public class Result{
	/**
	 * 状态
	 */
	private int status=0;
	/**
	 * 信息
	 */
	private String message="";
	/**
	 * 总数
	 */
	private int total=0;
	/**
	 * 数据集合
	 */
	private Object data;
	
	public Result(){}
	
	public Result(Object data){
		this.status=Code.VALID;
		this.data=data;
	}
	public Result(int total){
		this.status=Code.VALID;
		this.total=total;
	}
	public Result(int status,Object data) {
		this.status = status;
		this.data = data;
	}
	public Result(Object data,int total) {
		this.status = Code.VALID;
		this.total = total;
		this.data = data;
	}
	public Result(Object data,String message){
		status=Code.VALID;
		this.data=data;
		this.message=message;
	}
	public Result(int status,String message){
		this.status=status;
		this.message=message;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public int getTotal() {
		return total;
	}
	public void setTotal(int total) {
		this.total = total;
	}
	public Object getData() {
		return data;
	}
	public void setData(Object data) {
		this.data = data;
	}
	@Override
	public String toString() {
		return "Result [status=" + status + ", message=" + message + ", total="	+ total + ", data=" + data + "]";
	}
	
	public String toJSON(){
		return JSON.toJSONString(this,SerializerFeature.DisableCircularReferenceDetect);
	}
	
	public String toJSON(String callback,String... filterText){
		SimplePropertyPreFilter filter=new SimplePropertyPreFilter(filterText);
		String jsonText=JSON.toJSONString(this,filter,SerializerFeature.DisableCircularReferenceDetect);
		if(callback==null||"".equals(callback))return jsonText;
		return callback+"("+jsonText+")";
	}
}
