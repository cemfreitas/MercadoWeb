package com.mercadodecreditos.controller;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.mercadodecreditos.dao.DocumentDao;
import com.mercadodecreditos.dao.DocumentDaoImpl;
import com.mercadodecreditos.dao.DocumentQuestionDao;
import com.mercadodecreditos.dao.DocumentQuestionDaoImpl;
import com.mercadodecreditos.dao.DocumentStatusDao;
import com.mercadodecreditos.dao.DocumentStatusDaoImpl;
import com.mercadodecreditos.dao.DocumentTypeDao;
import com.mercadodecreditos.dao.DocumentTypeDaoImpl;
import com.mercadodecreditos.dao.UserDao;
import com.mercadodecreditos.dao.UserDaoImpl;
import com.mercadodecreditos.model.City;
import com.mercadodecreditos.model.Document;
import com.mercadodecreditos.model.DocumentFraction;
import com.mercadodecreditos.model.DocumentQuestion;
import com.mercadodecreditos.model.DocumentSituation;
import com.mercadodecreditos.model.DocumentStatus;
import com.mercadodecreditos.model.DocumentType;
import com.mercadodecreditos.model.State;
import com.mercadodecreditos.model.User;
import com.mercadodecreditos.util.ApplicationException;
import com.mercadodecreditos.util.MailUtils;
import com.mercadodecreditos.util.SystemDomain;
import com.mercadodecreditos.web.ContextBean;

public class DocumentController {
	private SimpleDateFormat fmt = new SimpleDateFormat("dd/MM/yyyy");
	private Date validationDate;
	private DocumentStatus lastDocumentStatus;
	private BigDecimal residualPrice;
	private BigDecimal totalPrice;

	private void generateValidationDate() {
		try {
			validationDate = fmt.parse("01/01/2999");
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}

	private void generateLastDocumentStatus() {
		DocumentStatusController docStatus = new DocumentStatusController();
		lastDocumentStatus = docStatus
				.getDocumentStatusById(SystemDomain.documentStatusPending);
	}

	public void saveDocument(Document document) throws ApplicationException {
		try {
			generateValidationDate();
			generateLastDocumentStatus();
			document.setValidationDate(validationDate);
			document.setLastDocumentStatus(lastDocumentStatus);
			document.setUser(new ContextBean().getLoggedUser());
			if (document.isAllowFractionPrice()) {
				List<DocumentFraction> fractionDocs = null;
				fractionDocs = generateFractionDocument(document);
				document.setResidualPrice(residualPrice);
				new DocumentDaoImpl().save(document, fractionDocs);
			} else {
				new DocumentDaoImpl().save(document);
			}
			MailUtils mailUtils = new MailUtils();
			mailUtils.sendEmailNewDocSaved(document);
		} catch (ApplicationException e) {
			throw new ApplicationException(e);
		}
	}

	private List<DocumentFraction> generateFractionDocument(Document doc) {
		ArrayList<DocumentFraction> fractionDocs = new ArrayList<DocumentFraction>();
		BigDecimal price = doc.getPrice();
		BigDecimal fraction;
		totalPrice = new BigDecimal(0);
		while (price.intValue() > 5000) {
			fraction = price.divide(new BigDecimal(2));
			if (fraction.intValue() % 5000 != 0) {
				int res = fraction.divide(new BigDecimal(5000)).intValue();
				if (res > 0) {
					fraction = new BigDecimal(res * 5000);
				} else {
					fraction = new BigDecimal(5000);
				}
			}
			totalPrice = totalPrice.add(fraction);
			DocumentFraction doc_ = new DocumentFraction();
			doc_.setStatus(doc.getLastDocumentStatus());
			doc_.setPrice(fraction);
			doc_.setMainDocument(doc);
			fractionDocs.add(doc_);
			price = price.subtract(fraction);
		}
		this.residualPrice = doc.getPrice().subtract(totalPrice);
		return fractionDocs;
	}

	public void updateDocument(Document document) {
		new DocumentDaoImpl().update(document);
	}

	public Document loadDocument(Document document) {
		Document doc = null;
		doc = new DocumentDaoImpl().getDocumentById(document.getXidDocument());

		return doc;
	}

	public List<Document> getDocumentByUser(User user) {
		List<Document> docs = null;
		docs = new DocumentDaoImpl().getDocumentByUser(user);

		return docs;
	}

	public List<Document> getDocumentByState(State state) {
		List<Document> docs = null;
		docs = new DocumentDaoImpl().getDocumentByState(state);

		return docs;
	}

	public List<Document> getDocumentByCity(City city) {
		List<Document> docs = null;
		docs = new DocumentDaoImpl().getDocumentByCity(city);

		return docs;
	}

	private String raiseRequested(Document doc, User user) {
		DocumentQuestionDao documentQuestionDao = new DocumentQuestionDaoImpl();
		DocumentQuestion documentQuestion;
		
		documentQuestion = documentQuestionDao.getLastDocumentQuestion(doc, user);
		
		if (documentQuestion != null) {
			return documentQuestion.getRaiseBidRequest();
		}		 

		return null;
	}

	public List<Document> searchDocument(User loggedUser, String[] docTypes,
			int idState, int idCity, BigDecimal valIni, BigDecimal valFim,
			Date dateIni, Date dateFinal) {
		List<Document> docs = null;

		DocumentType[] doctype;
		State state;
		City city;

		if (valIni != null && valIni.doubleValue() == 0) {
			valIni = null;
		}
		if (valFim != null && valFim.doubleValue() == 0) {
			valFim = null;
		}

		doctype = getDocumentTypes(docTypes);
		state = getStateById(idState);
		city = getCityById(idCity);

		DocumentDao doc = new DocumentDaoImpl();
		docs = doc.searchDocument(doctype, state, city, valIni, valFim,
				dateIni, dateFinal);

		// String docNumber;
		for (Document doc_ : docs) {
			if (doc_.getDocumentType().getXidDocumentType() == SystemDomain.documentTypeCreditoICMS) {
				if (!doc_.getState().equals(loggedUser.getState())) {
					doc_.setEnableBidButton(false);
					doc_.setLabelBidButton("Estado diferente");
				} else {
					if (doc_.getUser().equals(loggedUser)) {
						doc_.setEnableBidButton(false);
						doc_.setLabelBidButton("Seu crédito");
					} else {
						if (this.raiseRequested(doc_, loggedUser) == null) {
							doc_.setEnableBidButton(true);
							doc_.setLabelBidButton("Ofertar");
						} else if (this.raiseRequested(doc_, loggedUser)
								.equals("Y")) {
							doc_.setEnableBidButton(true);
							doc_.setLabelBidButton("Fazer nova oferta");
						} else if (this.raiseRequested(doc_, loggedUser)
								.equals("N")) {
							doc_.setEnableBidButton(false);
							doc_.setLabelBidButton("Aguardar resposta do vendedor");
						} else if (this.raiseRequested(doc_, loggedUser).equals("S")) {
							doc_.setEnableBidButton(true);
							doc_.setLabelBidButton("Ofertar");
						}
					}
				}
			} else {
				if (doc_.getUser().equals(loggedUser)) {
					doc_.setEnableBidButton(false);
					doc_.setLabelBidButton("Seu crédito");
				} else {
					if (this.raiseRequested(doc_, loggedUser) == null) {
						doc_.setEnableBidButton(true);
						doc_.setLabelBidButton("Ofertar");
					} else if (this.raiseRequested(doc_, loggedUser)
							.equals("Y")) {
						doc_.setEnableBidButton(true);
						doc_.setLabelBidButton("Fazer nova oferta");
					} else if (this.raiseRequested(doc_, loggedUser)
							.equals("N")) {
						doc_.setEnableBidButton(false);
						doc_.setLabelBidButton("Aguardar resposta do vendedor");
					} else {
						doc_.setEnableBidButton(true);
						doc_.setLabelBidButton("Ofertar");
					}
				}
			}
		}

		return docs;
	}

	private State getStateById(int id) {
		if (id < 0) {
			return null;
		}
		StateCityController cityState = new StateCityController();
		State state;
		state = cityState.getStateById(id);

		return state;
	}

	private City getCityById(int id) {
		if (id < 0) {
			return null;
		}
		StateCityController cityState = new StateCityController();
		City city;
		city = cityState.getCityById(id);

		return city;
	}

	private DocumentType[] getDocumentTypes(String[] docTypesStr) {
		if (docTypesStr == null || docTypesStr.length == 0) {
			return null;
		}
		DocumentType[] docTypes = new DocumentType[docTypesStr.length];
		DocumentTypeDao docType = new DocumentTypeDaoImpl();

		for (int i = 0; i < docTypesStr.length; i++) {
			docTypes[i] = docType.getDocumentTypeById(Integer
					.parseInt(docTypesStr[i]));
		}
		return docTypes;
	}

	public void documentSold(DocumentQuestion docmentSold)
			throws ApplicationException {
		try {
			Document document = docmentSold.getDocument();
			// User seller = docmentSold.getDocument().getUser();
			// User buyer = docmentSold.getUserBid();
			// BigDecimal bidDealt = docmentSold.getBid();

			DocumentStatus lastDocumentStatus;
			DocumentStatusDao soldDocStatus = new DocumentStatusDaoImpl();
			lastDocumentStatus = soldDocStatus
					.getDocumentStatusById(SystemDomain.documentStatusSold);
			document.setLastDocumentStatus(lastDocumentStatus);

			DocumentSituation docSituation = new DocumentSituation();
			docSituation.setDocument(document);
			docSituation.setDocumentSituationDate(new Date());
			docSituation.setDocumentStatus(lastDocumentStatus);

			DocumentDao documentDao = new DocumentDaoImpl();
			documentDao.updateDocumentStatus(document, docSituation);
			// TODO tratar pagamento de comissão

			new MailUtils().sendEmailDealClosed(docmentSold, docmentSold
					.getUserBid().getEmail());

		} catch (ApplicationException e) {
			throw new ApplicationException(e);
		}
	}

	public void documentSold(DocumentQuestion docmentSold,
			List<DocumentFraction> fracs) throws ApplicationException {
		try {
			DocumentDao documentDao = new DocumentDaoImpl();
			Document document = docmentSold.getDocument();
			// User seller = docmentSold.getDocument().getUser();
			// User buyer = docmentSold.getUserBid();
			// BigDecimal bidDealt = docmentSold.getBid();

			List<DocumentFraction> docsAvailable = this
					.getFractionDocumentsAvailable(document);

			DocumentStatus lastDocumentStatus;
			DocumentStatusDao soldDocStatus = new DocumentStatusDaoImpl();
			lastDocumentStatus = soldDocStatus
					.getDocumentStatusById(SystemDomain.documentStatusSold);			
			
			DocumentSituation docSituation = new DocumentSituation();
			docSituation.setDocument(document);
			docSituation.setDocumentSituationDate(new Date());
			docSituation.setDocumentStatus(lastDocumentStatus);

			if (docsAvailable.equals(fracs)) {// All Sold
				document.setLastDocumentStatus(lastDocumentStatus);				
				documentDao.updateDocumentStatus(document, docSituation, fracs);				
			} else {				
				documentDao.updateDocumentStatus(null, docSituation, fracs);
			}

			// TODO tratar pagamento de comissão
			new MailUtils().sendEmailDealClosed(docmentSold, fracs, docmentSold
					.getUserBid().getEmail());

		} catch (ApplicationException e) {
			throw new ApplicationException(e);
		}
	}

	public List<Document> listUserDocuments(User user, int documentStatus) {
		List<Document> document = null;
		DocumentDao documentDao = new DocumentDaoImpl();

		document = documentDao.getUserDocuments(user,
				getDocumentStatusById(documentStatus));

		return document;
	}

	public List<Document> searchDocumentAdmin(String login,
			String[] documentType, int documentStatus) {
		List<Document> document = null;
		DocumentDao documentDao = new DocumentDaoImpl();
		document = documentDao.searchDocumentAdmin(getUserByLogin(login),
				getDocumentTypes(documentType),
				getDocumentStatusById(documentStatus));

		return document;
	}

	public void changeDocumentStatus(Document document, int newStatus,
			String explanation) throws ApplicationException {

		try {
			DocumentStatus documentStatus;
			DocumentStatusDao documentStatusDao = new DocumentStatusDaoImpl();
			documentStatus = documentStatusDao.getDocumentStatusById(newStatus);
			document.setLastDocumentStatus(documentStatus);

			DocumentSituation docSituation = new DocumentSituation();
			docSituation.setDocument(document);
			docSituation.setDocumentSituationDate(new Date());
			docSituation.setDocumentStatus(documentStatus);
			docSituation.setDescription(explanation);

			DocumentDao documentDao = new DocumentDaoImpl();
			documentDao.updateDocumentStatus(document, docSituation);

			MailUtils mailUtils = new MailUtils();
			mailUtils.sendEmailDocStatusChanged(document, explanation);
		} catch (ApplicationException e) {
			throw new ApplicationException(e);
		}
	}

	private User getUserByLogin(String login) {
		if (login.equals("Todos")) {
			return null;
		}
		User user = null;
		UserDao userDao = new UserDaoImpl();
		user = userDao.getUserByLogin(login);
		return user;
	}

	private DocumentStatus getDocumentStatusById(int id) {
		if (id <= 0) {
			return null;
		}
		DocumentStatus documentStatus = null;
		DocumentStatusDao documentStatusDao = new DocumentStatusDaoImpl();
		documentStatus = documentStatusDao.getDocumentStatusById(id);

		return documentStatus;
	}

	public List<DocumentFraction> getFractionDocuments(Document mainDocument) {
		List<DocumentFraction> docs = null;

		DocumentDao documentDao = new DocumentDaoImpl();

		docs = documentDao.getFractionDocs(mainDocument);

		return docs;
	}

	public List<DocumentFraction> getFractionDocumentsAvailable(
			Document mainDocument) {
		List<DocumentFraction> docs = null;

		DocumentDao documentDao = new DocumentDaoImpl();

		docs = documentDao.getFractionDocsAvailable(mainDocument);

		return docs;
	}
}
