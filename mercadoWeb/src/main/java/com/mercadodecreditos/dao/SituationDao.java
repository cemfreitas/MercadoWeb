package com.mercadodecreditos.dao;

import java.util.List;

import com.mercadodecreditos.model.Situation;

public interface SituationDao {
	public List<Situation> listAll();
	public Situation getSituationById(int id);
}
