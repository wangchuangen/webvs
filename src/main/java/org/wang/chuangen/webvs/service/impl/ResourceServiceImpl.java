package org.wang.chuangen.webvs.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.wang.chuangen.webvs.dao.ResourceDao;
import org.wang.chuangen.webvs.model.Resource;
import org.wang.chuangen.webvs.service.ResourceService;
import org.wang.chuangen.webvs.vo.ResourceRely;

@Service("resourceService")
public class ResourceServiceImpl implements ResourceService{
	
	@Autowired
	private ResourceDao resourceDao;

	@Override
	public int addResource(Resource resource) {
		return resourceDao.addResource(resource);
	}

	@Override
	public int addResources(List<Resource> resources) {
		return resourceDao.addResources(resources);
	}

	@Override
	public int addResourceRelies(List<ResourceRely> resourceRelies) {
		return resourceDao.addResourceRelies(resourceRelies);
	}

	@Override
	public Resource findResource(int id) {
		return resourceDao.findResource(id);
	}

	@Override
	public Resource findLabelVersion(String label) {
		return resourceDao.findLabelVersion(label);
	}

	
	
	@Override
	public void findRelyResource(int relyId, List<Resource> resources, List<Integer> ids) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("relyId", relyId);
		params.put("ids", ids);
		List<Resource> resourceList = resourceDao.findRelyResource(params);
		if(resourceList != null && resourceList.size() > 0){
			resources.addAll(resourceList);
			for (Resource resource : resourceList) {
				ids.add(resource.getId());
			}
			for (Resource resource : resourceList) {
				findRelyResource(resource.getId(), resources, ids);
			}
		}
	}

	@Override
	public List<Resource> findSonResourceList(int id) {
		return resourceDao.findSonResourceList(id);
	}

	@Override
	public List<Resource> findSonResource(int id) {
		return resourceDao.findSonResource(id);
	}

	@Override
	public List<Resource> findAllResource() {
		return resourceDao.findAllResource();
	}

	@Override
	public List<Resource> findResourceTree() {
		return resourceDao.findSonResource(0);
	}

	@Override
	public int isExistTable(String tablename) {
		return resourceDao.isExistTable(tablename);
	}

	@Override
	public int isExistLabel(String label) {
		return resourceDao.isExistLabel(label);
	}

	@Override
	public int updateResourceLabel(int id, String label) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("id", "" + id);
		params.put("label", label);
		return resourceDao.updateResourceLabel(params);
	}

	@Override
	public int updateResourceVersions(List<Resource> resources) {
		int s = 0;
		for (Resource resource : resources) {
			s += resourceDao.updateResourceVersions(resource);
		}
		return s;
	}

	@Override
	public int createResourceTable() {
		return resourceDao.createResourceTable();
	}

	@Override
	public int createUrlIndex() {
		return resourceDao.createUrlIndex();
	}

	@Override
	public int createResourceRelyTable() {
		return resourceDao.createResourceRelyTable();
	}

	@Override
	public int deleteInvalidResource(List<Integer> ids) {
		int s = resourceDao.deleteRelyByIds(ids);
		return s + resourceDao.deleteResourceByIds(ids);
	}

	@Override
	public int deleteResourceRelyByIds(List<Integer> ids) {
		return resourceDao.deleteResourceRelyByIds(ids);
	}

	@Override
	public int deleteTable(String tableName) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("tableName", tableName);
		return resourceDao.deleteTable(params);
	}

	@Override
	public int rebuildDatabase() {
		int s=0;
		Map<String, String> params = new HashMap<String, String>();
		params.put("tableName", "RESOURCE_RELY");
		s+=resourceDao.deleteTable(params);
		params.put("tableName", "RESOURCE");
		s+=resourceDao.deleteTable(params);
		s+=resourceDao.createResourceTable();
		s+=resourceDao.createUrlIndex();
		s+=resourceDao.createResourceRelyTable();
		return s;
	}
}
