package com.mercadodecreditos.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.hibernate.HibernateException;

import com.mercadodecreditos.dao.DocumentDao;
import com.mercadodecreditos.dao.DocumentDaoImpl;
import com.mercadodecreditos.dao.DocumentQuestionDao;
import com.mercadodecreditos.dao.DocumentQuestionDaoImpl;
import com.mercadodecreditos.dao.DocumentStatusDao;
import com.mercadodecreditos.dao.DocumentStatusDaoImpl;
import com.mercadodecreditos.dao.UserDao;
import com.mercadodecreditos.dao.UserDaoImpl;
import com.mercadodecreditos.model.Document;
import com.mercadodecreditos.model.DocumentFraction;
import com.mercadodecreditos.model.DocumentFractionQuestion;
import com.mercadodecreditos.model.DocumentQuestion;
import com.mercadodecreditos.model.DocumentSituation;
import com.mercadodecreditos.model.DocumentStatus;
import com.mercadodecreditos.model.Message;
import com.mercadodecreditos.model.User;
import com.mercadodecreditos.util.ApplicationException;
import com.mercadodecreditos.util.MailUtils;
import com.mercadodecreditos.util.SystemDomain;
import com.mercadodecreditos.web.MessageBean;

public class DocumentQuestionController {

	public void saveDocumentQuestion(Document doc, User userBid,
			BigDecimal bid, String[] fractionDocs) throws ApplicationException {
		Calendar cal = Calendar.getInstance();
		DocumentQuestion docQuestion = new DocumentQuestion();
		MailUtils mailUtils = new MailUtils();
		DocumentStatus inNegotiation = getDocumentStatus(SystemDomain.documentStatusInNegotiation), lastDocumentStatus = doc
				.getLastDocumentStatus();
		DocumentDao document;
		DocumentSituation docs;

		doc.setLastDocumentStatus(inNegotiation);
		document = new DocumentDaoImpl();
		docs = new DocumentSituation();
		docs.setDocument(doc);
		docs.setDocumentSituationDate(new Date());
		docs.setDocumentStatus(inNegotiation);

		docQuestion.setDocument(doc);
		docQuestion.setUserBid(userBid);
		docQuestion.setDescription("");
		docQuestion.setDocumentQuestionDate(cal.getTime());
		docQuestion.setRaiseBidRequest("N");
		if (bid != null && bid.doubleValue() == 0) {
			bid = null;
		}
		docQuestion.setBid(bid);
		try {
			if (fractionDocs == null || fractionDocs.length == 0) {
				new DocumentQuestionDaoImpl().save(docQuestion);
				if (!lastDocumentStatus.equals(inNegotiation)) {
					// Update status to in negotiation
					document.updateDocumentStatus(doc, docs);
				}
				// Send email
				mailUtils.sendEmailNewBidOffered(doc, bid, doc.getUser()
						.getEmail());
			} else {
				DocumentFractionQuestion[] docFractionQuestionLst = new DocumentFractionQuestion[fractionDocs.length];
				List<DocumentFraction> documentFractionLst = new ArrayList<DocumentFraction>();
				DocumentFraction documentFraction;
				for (int i = 0; i < fractionDocs.length; i++) {
					DocumentFractionQuestion documentFractionQuestion = new DocumentFractionQuestion();
					documentFraction = getFraction(Long
							.valueOf(fractionDocs[i]));
					documentFractionQuestion
							.setDocumentFraction(documentFraction);
					documentFractionQuestion.setDocumentQuestion(docQuestion);
					documentFractionLst.add(documentFraction);
					docFractionQuestionLst[i] = documentFractionQuestion;
				}
				new DocumentQuestionDaoImpl().save(docQuestion,
						docFractionQuestionLst);

				if (!lastDocumentStatus.equals(inNegotiation)) {
					// Update status to in negotiation
					document.updateDocumentStatus(doc, docs,
							documentFractionLst);
				}
				// Send e-mail
				mailUtils.sendEmailNewBidOffered(doc, bid,
						docFractionQuestionLst, doc.getUser().getEmail());
			}
		} catch (HibernateException e) {
			throw new ApplicationException(e.getMessage());
		}
	}

	public DocumentQuestion getDocumentQuestionById(long id) {
		DocumentQuestion docQ = null;

		docQ = new DocumentQuestionDaoImpl().getDocumentQuestionById(id);
		return docQ;
	}

	private DocumentFraction getFraction(long id) {
		DocumentDao documentDao = new DocumentDaoImpl();
		return documentDao.getDocumentFractionById(id);
	}

	public List<DocumentQuestion> getDocumentQuestionByUserBid(User user) {
		List<DocumentQuestion> docsQ = null;

		docsQ = new DocumentQuestionDaoImpl()
				.getDocumentQuestionByUserBid(user);
		return docsQ;
	}

	public List<DocumentQuestion> getDocumentQuestionByUserDoc(Document doc) {
		List<DocumentQuestion> docsQ = null;

		docsQ = new DocumentQuestionDaoImpl().getDocumentQuestionByUserDoc(doc);
		return docsQ;
	}
	
	public List<User> getUsers(User user, String msgType, long idDocument,
			String status) {
		List<User> users = null;

		DocumentQuestionDaoImpl documentQuestionDaoImpl = new DocumentQuestionDaoImpl();
		users = documentQuestionDaoImpl.getUsers(user, msgType,
				getDocumentById(idDocument), this.getDocumentStatus(status));

		return users;
	}

	public List<Document> getDocuments(User user, String msgType, String status) {
		List<Document> docs = null;

		DocumentQuestionDaoImpl documentQuestionDaoImpl = new DocumentQuestionDaoImpl();
		docs = documentQuestionDaoImpl.getDocuments(user, msgType,
				getDocumentStatus(status));

		return docs;
	}
	
	public List<Message> getMessageReceived(long document) {
		List<Message> bids=null;
		DocumentQuestionDao documentQuestionDao = new DocumentQuestionDaoImpl();
		
		bids = documentQuestionDao.getMessagesReceived(getDocumentById(document));
		
		return bids;
	}	
	

	public List<DocumentQuestion> searchMessages(User user, String msgType,
			List<Document> documents, long userMsg, long document) {
		List<DocumentQuestion> messages = null;

		DocumentQuestionDaoImpl documentQuestionDaoImpl = new DocumentQuestionDaoImpl();
		messages = documentQuestionDaoImpl.searchMessages(user, msgType,
				documents, getUserById(userMsg), getDocumentById(document));
		/*
		 * int i =0; for (DocumentQuestion docQ : messages) { String docNumber;
		 * Document doc; doc = docQ.getDocument(); docNumber =
		 * doc.getDocumentNumber(); docNumber =
		 * docNumber.replace(docNumber.subSequence(0, 6),"******");
		 * doc.setDocumentNumber(docNumber); docQ.setDocument(doc);
		 * messages.set(i, docQ); i++; }
		 */
		return messages;
	}

	private User getUserById(long id) {
		if (id == -1) {
			return null;
		}
		User user = null;
		UserDao userDao = new UserDaoImpl();
		user = userDao.getUserById(id);
		return user;

	}

	private Document getDocumentById(long id) {
		if (id == -1) {
			return null;
		}
		Document doc;
		DocumentDao docDao = new DocumentDaoImpl();
		doc = docDao.getDocumentById(id);
		return doc;

	}

	private DocumentStatus getDocumentStatus(String status) {
		DocumentStatus docStatus = null;
		DocumentStatusDao docDao = null;
		if (status.equals(MessageBean.MESSAGE_STATUS_ALL)) {
			return null;
		} else if (status.equals(MessageBean.MESSAGE_STATUS_CONCLUDED)) {
			docDao = new DocumentStatusDaoImpl();
			docStatus = docDao.getDocumentStatusById(SystemDomain.documentStatusSold);
		} else if (status.equals(MessageBean.MESSAGE_STATUS_STILL)) {
			docDao = new DocumentStatusDaoImpl();
			docStatus = docDao.getDocumentStatusById(SystemDomain.documentStatusInNegotiation);
		}
		return docStatus;
	}

	public void setRaiseBidFieldFlag(DocumentQuestion documentQuestion) {
		MailUtils mailUtils;
		documentQuestion.setRaiseBidRequest("Y");
		new DocumentQuestionDaoImpl().update(documentQuestion);
		mailUtils = new MailUtils();
		mailUtils.sendEmailRequestRaiseBid(documentQuestion, documentQuestion
				.getUserBid().getEmail());
	}

	public List<DocumentFraction> getDocumentFraction(
			DocumentQuestion documentQuestion) {
		List<DocumentFraction> fractions = null;
		DocumentQuestionDao documentQuestionDao = new DocumentQuestionDaoImpl();

		fractions = documentQuestionDao.getDocumentFraction(documentQuestion);

		return fractions;
	}
	
	public List<DocumentFraction> getDocumentFraction(
			User user, Document document) {
		List<DocumentFraction> fractions = null;
		DocumentQuestionDao documentQuestionDao = new DocumentQuestionDaoImpl();

		fractions = documentQuestionDao.getDocumentFraction(getLastDocumentQuestionByUser(user,document));

		return fractions;
	}
	
	public List<DocumentFraction> getSelectedDocumentFraction(
			User user, Document document) {
		List<DocumentFraction> fractions = null;
		DocumentQuestionDao documentQuestionDao = new DocumentQuestionDaoImpl();

		fractions = documentQuestionDao.getSelectedDocumentFraction(getLastDocumentQuestionByUser(user,document));

		return fractions;
	}

	public boolean isAnyFractionSold(DocumentQuestion documentQuestion) {
		boolean result;
		DocumentQuestionDao documentQuestionDao = new DocumentQuestionDaoImpl();

		result = documentQuestionDao.isAnyFractionSold(documentQuestion);

		return result;
	}

	private DocumentStatus getDocumentStatus(int id) {
		DocumentStatus documentStatus = null;
		DocumentStatusDao documentStatusDao = new DocumentStatusDaoImpl();

		documentStatus = documentStatusDao.getDocumentStatusById(id);

		return documentStatus;
	}
	
	private DocumentQuestion getLastDocumentQuestionByUser (User user, Document document) {
		DocumentQuestion documentQuestion = null;
		DocumentQuestionDao documentQuestionDao = new DocumentQuestionDaoImpl();
		
		documentQuestion = documentQuestionDao.getLastDocumentQuestionByUserBid(user,document);
		
		return documentQuestion;
	}
	
	public DocumentQuestion getLastDocumentQuestion(Document doc, User user) {
		DocumentQuestionDao documentQuestion = new DocumentQuestionDaoImpl();

		return documentQuestion.getLastDocumentQuestion(doc, user);
	}
	
	public List<BigDecimal> getBids(Document document, User user) {
		List<BigDecimal> bids = null;
		
		DocumentQuestionDao documentQuestion = new DocumentQuestionDaoImpl();
		bids=documentQuestion.getBidsReceived(document, user);
				
		return bids;
	}
}
