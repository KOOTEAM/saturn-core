package oops.saturn.manager.impl;

import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import oops.saturn.dao.EntityDao;
import oops.saturn.manager.TestManager;
import oops.saturn.model.Position;
import oops.saturn.model.Sharon;

@Service("testManager")
public class TestManagerImpl implements TestManager{
	private EntityDao entityDao;
	
	@Resource(name="entityDao")
	public void setEntityDao(EntityDao entityDao) {
		this.entityDao = entityDao;
	}



	public String saveSharon(Map<String, Object> map) {
		// TODO Auto-generated method stub
		Sharon sharon = new Sharon();
		
		sharon.setSharonName(map.get("sname").toString());
		sharon.setSharonAge(Integer.parseInt(map.get("sage").toString()));
		sharon.setSharonHeight(Long.parseLong(map.get("sheight").toString()));
		sharon.setSharonWeight(Long.parseLong(map.get("sweight").toString()));
		sharon.setPosition(Position.BOSS);
		
		Long id = this.entityDao.saveOrUpdate(sharon).getId();
		String name = this.entityDao.get(Sharon.class, id).getSharonName();
		
		return "id : " + id.toString() + " name : " + name;
	}
	
}
