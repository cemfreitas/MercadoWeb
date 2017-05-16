package com.mercadodecreditos.dao;

import java.util.List;

import com.mercadodecreditos.model.Document;
import com.mercadodecreditos.model.DocumentSituation;

public interface DocumentSituationDao {
	public void save(DocumentSituation documentSituation);
	public List<DocumentSituation> getAllDocSituationByDoc(Document document);
	public DocumentSituation getLastDocSituationByDoc(Document document);
	public List<DocumentSituation> getDocSituationByFractionDoc(Document document);
}
