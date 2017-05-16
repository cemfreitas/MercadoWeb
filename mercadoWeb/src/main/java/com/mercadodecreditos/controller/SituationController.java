package com.mercadodecreditos.controller;

import java.util.List;

import com.mercadodecreditos.dao.SituationDao;
import com.mercadodecreditos.dao.SituationDaoImpl;
import com.mercadodecreditos.model.Situation;

public class SituationController {
	
	public List<Situation> getAll(){
		List<Situation> list= null;
		SituationDao situationDao = new SituationDaoImpl();
		list = situationDao.listAll();
		
		return list;
	}
	
	public Situation getSituationById(int id) {
		Situation situation=null;
		SituationDao situationDao = new SituationDaoImpl();
		situation = situationDao.getSituationById(id);
		
		return situation;
	}

}
