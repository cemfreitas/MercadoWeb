package com.mercadodecreditos.dao;

import java.util.List;

import com.mercadodecreditos.model.DocumentType;

public interface DocumentTypeDao {
	public List<DocumentType> listAll();
	public DocumentType getDocumentTypeById(int id);
	public DocumentType getDocumentTypeByDesc(String desc);
}
