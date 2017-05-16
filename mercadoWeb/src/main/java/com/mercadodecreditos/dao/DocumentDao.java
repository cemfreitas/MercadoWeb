package com.mercadodecreditos.dao;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.mercadodecreditos.model.City;
import com.mercadodecreditos.model.Document;
import com.mercadodecreditos.model.DocumentFraction;
import com.mercadodecreditos.model.DocumentSituation;
import com.mercadodecreditos.model.DocumentStatus;
import com.mercadodecreditos.model.DocumentType;
import com.mercadodecreditos.model.State;
import com.mercadodecreditos.model.User;
import com.mercadodecreditos.util.ApplicationException;

public interface DocumentDao {
	public void save(Document document) throws ApplicationException;

	public void update(Document document);

	public Document getDocumentById(long id);

	public List<Document> getDocumentByUser(User user);

	public List<Document> getDocumentByState(State state);

	public List<Document> getDocumentByCity(City city);

	public void updateDocumentStatus(Document document,
			DocumentSituation documentSituation) throws ApplicationException;
	
	public void updateDocumentStatus(Document document,
			DocumentSituation documentSituation, List<DocumentFraction> fractions) throws ApplicationException;

	public List<Document> searchDocument(DocumentType[] documentType,
			State state, City city, BigDecimal valIni, BigDecimal valEnd,
			Date deadLineIni, Date deadLineEnd);

	public List<Document> searchDocumentAdmin(User user,
			DocumentType[] documentType, DocumentStatus documentStatus);

	public List<Document> getUserDocuments(User user,DocumentStatus statusDocument);
	
	public List<DocumentFraction> getFractionDocs(Document mainDocument);
	
	public List<DocumentFraction> getFractionDocsAvailable(Document mainDocument);
	
	public DocumentFraction getDocumentFractionById(long id);
}
