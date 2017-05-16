package com.mercadodecreditos.dao;

import java.util.List;

import com.mercadodecreditos.model.DocumentStatus;

public interface DocumentStatusDao {
	public List<DocumentStatus> listAll();
	public DocumentStatus getDocumentStatusById(int id);
	public DocumentStatus getDocumentStatusByDesc(String desc);
}
