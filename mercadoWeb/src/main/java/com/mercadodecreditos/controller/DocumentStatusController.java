package com.mercadodecreditos.controller;

import java.util.List;

import com.mercadodecreditos.dao.DocumentStatusDao;
import com.mercadodecreditos.dao.DocumentStatusDaoImpl;
import com.mercadodecreditos.model.DocumentStatus;

public class DocumentStatusController {
	
	public List<DocumentStatus> getAll(){
		List<DocumentStatus> docStatus = null;
		DocumentStatusDao docType = new DocumentStatusDaoImpl();
		
		docStatus = docType.listAll();
		return docStatus;
	}
	
	public DocumentStatus getDocumentStatusById(int id) {		
		DocumentStatusDao docType = new DocumentStatusDaoImpl();
		return docType.getDocumentStatusById(id);
	}
		
	public DocumentStatus getDocumentStatusByDesc(String desc) {		
		DocumentStatusDao docType = new DocumentStatusDaoImpl();
		return docType.getDocumentStatusByDesc(desc);
	}

}
