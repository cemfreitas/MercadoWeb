package com.mercadodecreditos.dao;

import java.math.BigDecimal;
import java.util.List;

import com.mercadodecreditos.model.Document;
import com.mercadodecreditos.model.DocumentFraction;
import com.mercadodecreditos.model.DocumentFractionQuestion;
import com.mercadodecreditos.model.DocumentQuestion;
import com.mercadodecreditos.model.DocumentStatus;
import com.mercadodecreditos.model.Message;
import com.mercadodecreditos.model.User;

public interface DocumentQuestionDao {
	public void save(DocumentQuestion docQuestion);	
	public void save(DocumentQuestion docQuestion, DocumentFractionQuestion[] fractions);
	public void update(DocumentQuestion docQuestion);
	public DocumentQuestion getDocumentQuestionById(long id);
	public List<DocumentQuestion> getDocumentQuestionByUserBid(User user);
	public List<DocumentQuestion> getDocumentQuestionByUserDoc(Document doc);	
	public List<User> getUsers(User user,String msgType,Document document,DocumentStatus documentStatus);
	public List<Document> getDocuments(User user,String msgType,DocumentStatus documentStatus);
	public List<DocumentQuestion> searchMessages(User user,String msgType,List<Document> documents,User userMsg,Document document);
	public List<DocumentFraction> getDocumentFraction(DocumentQuestion documentQuestion);
	public boolean isAnyFractionSold(DocumentQuestion documentQuestion);
	public DocumentQuestion getLastDocumentQuestion(Document doc, User user);
	public DocumentQuestion getLastDocumentQuestionByUserBid(User userBid, Document document);	
	List<DocumentFraction> getSelectedDocumentFraction(
			DocumentQuestion documentQuestion);
	public List<Message> getMessagesReceived(Document document);
	public List<BigDecimal> getBidsReceived(Document document, User user);
}
