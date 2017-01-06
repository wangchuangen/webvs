package org.wang.chuangen.webvs.mvc;

import java.sql.Timestamp;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.wang.chuangen.webvs.listener.WebContextListener;
import org.wang.chuangen.webvs.model.Resource;
import org.wang.chuangen.webvs.service.ResourceService;
import org.wang.chuangen.webvs.util.RefreshDatabase;
import org.wang.chuangen.webvs.util.RefreshResource;
import org.wang.chuangen.webvs.util.Tool;
import org.wang.chuangen.webvs.vo.Code;
import org.wang.chuangen.webvs.vo.Result;

@Controller
public class ResourceAction {
	
	@Autowired
	private ResourceService resourceService;
	
	@ResponseBody
	@RequestMapping("rebuildDatabase")
	public String rebuildDatabase(String admin){
		if(StringUtils.isBlank(admin) || !admin.equals(WebContextListener.getAdmin())){
			return new Result(Code.PASS_UNTRUE, "管理员密码错误").toJSON();
		}
		resourceService.rebuildDatabase();
		Thread refresh = new Thread(new RefreshDatabase(resourceService));
		refresh.setDaemon(false);
		refresh.start();
		return new Result(Code.VALID, "已启动重建数据任务").toJSON();
	}
	
	@ResponseBody
	@RequestMapping("refreshAllResource")
	public String refreshAllResource(){
		long nowTime = System.currentTimeMillis();
		long refreshTime = RefreshDatabase.getRefreshTime();
		if((nowTime - RefreshDatabase.MIN_REFRESH_TIME) < refreshTime){
			return new Result(Code.MESSAGE_INFO, "系统与 " + Tool.getDateTime(new Timestamp(refreshTime)) + "更新的数据,请勿频繁更新数据").toJSON();
		}
		Thread refresh = new Thread(new RefreshDatabase(resourceService));
		refresh.setDaemon(false);
		refresh.start();
		return new Result(Code.VALID, "已启动更新数据任务").toJSON();
	}
	
	@ResponseBody
	@RequestMapping("refreshResource")
	public String refreshResource(int id){
		new RefreshResource(resourceService, id).run();
		return new Result(Code.VALID, "更新完成").toJSON();
	}
	
	@ResponseBody
	@RequestMapping("findResourceTree")
	public String findResourceTree(){
		List<Resource> resources=resourceService.findResourceTree();
		return Tool.toJSON(resources);
	}
	
	@ResponseBody
	@RequestMapping("updateResourceLabel")
	public String updateResourceLabel(int id, String label){
		int s = resourceService.isExistLabel(label);
		if(s > 0){
			return new Result(Code.VALID, "以存在重复的标签").toJSON();
		}
		s = resourceService.updateResourceLabel(id, label);
		if(s > 0){
			return new Result(Code.VALID, "修改成功").toJSON();
		}
		return new Result(Code.VALID, "修改失败").toJSON();
	}
}
