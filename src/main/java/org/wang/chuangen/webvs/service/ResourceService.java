package org.wang.chuangen.webvs.service;

import java.util.List;

import org.wang.chuangen.webvs.model.Resource;
import org.wang.chuangen.webvs.vo.ResourceRely;

public interface ResourceService {
	
	/**
	 * 添加一条资源数据
	 * @param resource
	 * @return
	 * @author 王传根
	 * @date 2016-8-19 下午9:28:25
	 */
	public int addResource(Resource resource);
	
	/**
	 * 添加多条资源数据
	 * @param resources
	 * @return
	 * @author 王传根
	 * @date 2016-8-19 下午9:29:05
	 */
	public int addResources(List<Resource> resources);
	
	/**
	 * 添加依赖关系
	 * @param resourceRelies
	 * @return
	 * @author 王传根
	 * @date 2016-8-20 下午8:53:53
	 */
	public int addResourceRelies(List<ResourceRely> resourceRelies);
	
	/**
	 * 根据ID查询资源
	 * @param id
	 * @return
	 * @author 王传根
	 * @date 2016-8-20 下午3:46:14
	 */
	public Resource findResource(int id);
	
	/**
	 * 查询标签版本
	 * @param label
	 * @return
	 * @author 王传根
	 * @date 2016-8-20 下午7:59:40
	 */
	public Resource findLabelVersion(String label);
	
	/**
	 * 查询依赖资源
	 * @param params
	 * @return
	 * @author 王传根
	 * @date 2016-8-20 下午6:57:43
	 */
	public void findRelyResource(int relyId, List<Resource> resources, List<Integer> ids);
	
	/**
	 * 查询目录列表
	 * @param id
	 * @return
	 * @author 王传根
	 * @date 2016-8-20 下午4:53:34
	 */
	public List<Resource> findSonResourceList(int id);
	
	/**
	 * 查询子项
	 * @param ID
	 * @return
	 * @author 王传根
	 * @date 2016-8-19 下午7:50:54
	 */
	public List<Resource> findSonResource(int id);
	
	/**
	 * 查询全部资源
	 * @return
	 * @author 王传根
	 * @date 2016-8-19 下午8:08:22
	 */
	public List<Resource> findAllResource();
	
	/**
	 * 查询资源树
	 * @return
	 * @author 王传根
	 * @date 2016-8-19 下午7:52:02
	 */
	public List<Resource> findResourceTree();

	/**
	 * 查询数据库表
	 * @return
	 * @author 王传根
	 * @date 2016-8-19 下午1:15:26
	 */
	public int isExistTable(String tablename);
	
	/**
	 * 查询标签
	 * @param label
	 * @return
	 * @author 王传根
	 * @date 2016-8-20 下午3:14:01
	 */
	public int isExistLabel(String label);
	
	/**
	 * 修改资源标签
	 * @param params
	 * @return
	 * @author 王传根
	 * @date 2016-8-20 下午2:46:27
	 */
	public int updateResourceLabel(int id, String label);
	
	/**
	 * 更新资源版本
	 * @param resources
	 * @return
	 * @author 王传根
	 * @date 2016-8-20 下午12:33:07
	 */
	public int updateResourceVersions(List<Resource> resources);
	
	/**
	 * 创建Resource表
	 * @return
	 * @author 王传根
	 * @date 2016-8-19 下午2:28:35
	 */
	public int createResourceTable();
	
	/**
	 * 创建url索引
	 * @return
	 * @author 王传根
	 * @date 2016-8-19 下午2:43:09
	 */
	public int createUrlIndex();
	
	/**
	 * 创建RESOURCE_RELY表
	 * @return
	 * @author 王传根
	 * @date 2016-8-19 下午3:01:13
	 */
	public int createResourceRelyTable();
	
	/**
	 * 删除无效的资源
	 * @param ids
	 * @return
	 * @author 王传根
	 * @date 2016-8-20 下午12:12:49
	 */
	public int deleteInvalidResource(List<Integer> ids);
	
	/**
	 * 根据ID删除资源依赖
	 * @param ids
	 * @return
	 * @author 王传根
	 * @date 2016-8-20 下午12:08:22
	 */
	public int deleteResourceRelyByIds(List<Integer> ids);
	
	/**
	 * 删除表
	 * @param tableName
	 * @return
	 * @author 王传根
	 * @date 2016-8-20 上午10:48:25
	 */
	public int deleteTable(String tableName);
	
	/**
	 * 重建数据
	 * @return
	 * @author 王传根
	 * @date 2016-8-19 下午3:29:59
	 */
	public int rebuildDatabase();
}
