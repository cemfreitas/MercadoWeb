package com.mercadodecreditos.controller;

import java.util.List;

import com.mercadodecreditos.dao.DocumentTypeDao;
import com.mercadodecreditos.dao.DocumentTypeDaoImpl;
import com.mercadodecreditos.model.DocumentType;

public class DocumentTypeController {
	
	public List<DocumentType> getAll(){
		List<DocumentType> docTypes = null;
		DocumentTypeDao docType = new DocumentTypeDaoImpl();
		
		docTypes = docType.listAll();
		return docTypes;
	}
	
	public DocumentType getDocumentTypeById(int id) {		
		DocumentTypeDao docType = new DocumentTypeDaoImpl();
		return docType.getDocumentTypeById(id);
	}
		
	public DocumentType getDocumentTypeByDesc(String desc) {		
		DocumentTypeDao docType = new DocumentTypeDaoImpl();
		return docType.getDocumentTypeByDesc(desc);
	}

}
