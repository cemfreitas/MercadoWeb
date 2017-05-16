package com.mercadodecreditos.web;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.application.ConfigurableNavigationHandler;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;

import org.primefaces.context.RequestContext;

import com.mercadodecreditos.controller.DocumentQuestionController;
import com.mercadodecreditos.model.Document;
import com.mercadodecreditos.model.DocumentFraction;
import com.mercadodecreditos.model.DocumentQuestion;
import com.mercadodecreditos.model.Message;
import com.mercadodecreditos.util.ApplicationException;
import com.mercadodecreditos.util.SystemDomain;

@ManagedBean(name = "messageBean")
@SessionScoped
public class MessageBean implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -7124550414599991687L;
	private String msgStatus, msgType;
	private List<SelectItem> documentsMsg, fractionDocs, bidsOffered;// ,usersMsg
	private boolean showTableMsg, closedDeal, disableMessageOpen;
	// private List<DocumentQuestion> messages;
	private List<Message> messages;
	private List<DocumentQuestion> messagesSent;
	private Message selectedMessage;
	private long selectedDocument;// ,selectedUser
	private BigDecimal newBid, totalFraction;
	private List<Document> documents = null;
	private List<DocumentFraction> fractions;
	private DocumentQuestion selectedMessageSent;
	public static final String MESSAGE_TYPE_RECEIVED = "received";
	public static final String MESSAGE_TYPE_SENT = "sent";
	public static final String MESSAGE_STATUS_ALL = "all";
	public static final String MESSAGE_STATUS_STILL = "still";
	public static final String MESSAGE_STATUS_CONCLUDED = "concluded";

	@ManagedProperty(value = "#{contextBean}")
	private ContextBean contextBean;

	public Message getSelectedMessage() {
		return selectedMessage;
	}

	public void setSelectedMessage(Message selectedMessage) {
		this.selectedMessage = selectedMessage;
	}

	public ContextBean getContextBean() {
		return contextBean;
	}

	public void setContextBean(ContextBean contextBean) {
		this.contextBean = contextBean;
	}

	public BigDecimal getNewBid() {
		return newBid;
	}

	public void setNewBid(BigDecimal newBid) {
		this.newBid = newBid;
	}

	/*
	 * public long getSelectedUser() { return selectedUser; }
	 * 
	 * public void setSelectedUser(long selectedUser) { this.selectedUser =
	 * selectedUser; }
	 */

	public boolean isClosedDeal() {
		return closedDeal;
	}

	public long getSelectedDocument() {
		return selectedDocument;
	}

	public void setSelectedDocument(long selectedDocument) {
		this.selectedDocument = selectedDocument;
	}

	/*
	 * public List<SelectItem> getUsersMsg() { return usersMsg; }
	 * 
	 * public void setUsersMsg(List<SelectItem> usersMsg) { this.usersMsg =
	 * usersMsg; }
	 */

	public List<SelectItem> getDocumentsMsg() {
		return documentsMsg;
	}

	public void setDocumentsMsg(List<SelectItem> documentsMsg) {
		this.documentsMsg = documentsMsg;
	}

	public List<Message> getMessages() {
		return messages;
	}

	public void setMessages(List<Message> messages) {
		this.messages = messages;
	}

	public String getMsgStatus() {
		return msgStatus;
	}

	public void setMsgStatus(String msgStatus) {
		this.msgStatus = msgStatus;
	}

	public boolean isShowTableMsg() {
		return showTableMsg;
	}

	public void setShowTableMsg(boolean showTableMsg) {
		this.showTableMsg = showTableMsg;
	}

	public String init(String mode) {
		messages = new ArrayList<Message>();
		showTableMsg = false;
		msgStatus = "all";
		selectedDocument = -1;
		documentsMsg = null;
		closedDeal = false;
		disableMessageOpen = false;
		if (mode.equals("sent")) {
			msgType = MESSAGE_TYPE_SENT;
			fillDocuments();
			return "/restricted/bidsent";
		} else {
			msgType = MESSAGE_TYPE_RECEIVED;
			fillDocuments();
			return "/restricted/bidreceived";
		}
	}

	public void searchMessages2() {
		if (closedDeal) {
			redirectDealPage();
		}
		
		DocumentQuestionController documentQuestionController = new DocumentQuestionController();
		this.messages = documentQuestionController
				.getMessageReceived(selectedDocument);
		
		

		if (messages == null || messages.size() == 0) {
			showTableMsg = false;
		} else {
			showTableMsg = true;
		}
	}

	public void searchMessages() {
		if (closedDeal) {
			redirectDealPage();
		}
		DocumentQuestionController documentQuestionController = new DocumentQuestionController();
		this.messagesSent = documentQuestionController.searchMessages(
				contextBean.getLoggedUser(), msgType, documents, -1,
				selectedDocument);

		if (messagesSent == null || messagesSent.size() == 0) {
			showTableMsg = false;
		} else {
			showTableMsg = true;
		}
	}

	/*
	 * public void fillUsers(ValueChangeEvent e) { if (e.getNewValue() == null)
	 * { return; }
	 * 
	 * if (msgType.equals("") || msgStatus.equals("")) { return; } else {
	 * this.selectedDocument = (long) e.getNewValue(); }
	 * 
	 * List<User> users = null; DocumentQuestionController
	 * documentQuestionController = new DocumentQuestionController(); users =
	 * documentQuestionController.getUsers( contextBean.getLoggedUser(),
	 * msgType, this.selectedDocument, this.msgStatus); this.usersMsg = new
	 * ArrayList<SelectItem>(); for (User user : users) { this.usersMsg .add(new
	 * SelectItem(user.getXidUser(), user.getLogin())); } }
	 */

	private void fillDocuments() {
		DocumentQuestionController documentQuestionController = new DocumentQuestionController();
		documents = documentQuestionController.getDocuments(
				contextBean.getLoggedUser(), this.msgType, this.msgStatus);
		this.documentsMsg = new ArrayList<SelectItem>();
		for (Document doc : documents) {
			this.documentsMsg.add(new SelectItem(doc.getXidDocument(), doc
					.getGeneratedDocNumber()));
		}
	}

	public void fillDocuments(ValueChangeEvent e) {
		if (e.getNewValue() == null) {
			return;
		}

		this.msgStatus = (String) e.getNewValue();

		if (msgType.equals("") || msgStatus.equals("")) {
			return;
		}

		DocumentQuestionController documentQuestionController = new DocumentQuestionController();
		documents = documentQuestionController.getDocuments(
				contextBean.getLoggedUser(), this.msgType, this.msgStatus);
		this.documentsMsg = new ArrayList<SelectItem>();
		for (Document doc : documents) {
			this.documentsMsg.add(new SelectItem(doc.getXidDocument(), doc
					.getGeneratedDocNumber()));
		}

		/*
		 * List<User> users = null; users = documentQuestionController.getUsers(
		 * contextBean.getLoggedUser(), this.msgType, -1, this.msgStatus);
		 * this.usersMsg = new ArrayList<SelectItem>(); for (User user : users)
		 * { this.usersMsg .add(new SelectItem(user.getXidUser(),
		 * user.getLogin())); }
		 */
		if (showTableMsg) {
			searchMessages2();
		}
	}

	public void openMessageReceived() {
		Map<String, Object> options = new HashMap<String, Object>();
		newBid = null;
		options.put("modal", true);
		options.put("resizable", false);
		options.put("draggable", false);
		options.put("closable", false);

		if (selectedMessage.getDocument().isAllowFractionPrice()) {
			options.put("contentHeight", 370);
			options.put("contentWidth", 450);

			RequestContext.getCurrentInstance().openDialog(
					"/restricted/userreplyfraction", options, null);
		} else {
			if (selectedMessage.getDocument().getDocumentType()
					.getXidDocumentType() == SystemDomain.documentTypeCreditoICMS) {
				options.put("contentHeight", 285);
				options.put("contentWidth", 430);
			} else {
				options.put("contentHeight", 320);
				options.put("contentWidth", 450);
			}

			RequestContext.getCurrentInstance().openDialog(
					"/restricted/bidreceivedreply", options, null);

		}
	}

	public void openMessageSent() {
		Map<String, Object> options = new HashMap<String, Object>();
		newBid = null;
		options.put("modal", true);
		options.put("resizable", false);
		options.put("draggable", false);
		options.put("closable", false);

		if (selectedMessageSent.getDocument().isAllowFractionPrice()) {
			options.put("contentHeight", 370);
			options.put("contentWidth", 450);

			RequestContext.getCurrentInstance().openDialog(
					"/restricted/userreplyfraction", options, null);
		} else {
			if (selectedMessageSent.getDocument().getDocumentType()
					.getXidDocumentType() == SystemDomain.documentTypeCreditoICMS) {
				options.put("contentHeight", 285);
				options.put("contentWidth", 430);
			} else {
				options.put("contentHeight", 320);
				options.put("contentWidth", 450);
			}

			RequestContext.getCurrentInstance().openDialog(
					"/restricted/bidsentreply", options, null);

		}
	}

	public void saveBid() {
		String[] fractionsStr = null;
		int i = 0;
		if (this.newBid.doubleValue() <= 0) {
			FacesContext context = FacesContext.getCurrentInstance();
			FacesMessage facesMessage = new FacesMessage(
					FacesMessage.SEVERITY_ERROR, "",
					"Valor do novo lance inválido");
			context.addMessage(null, facesMessage);
			RequestContext.getCurrentInstance()
					.execute("window.scrollTo(0,0);");
			return;
		}

		if (fractions != null && fractions.size() > 0) {
			fractionsStr = new String[fractions.size()];
			for (DocumentFraction docFrac : fractions) {
				fractionsStr[i] = String.valueOf(docFrac
						.getXidFractionDocument());
				i++;
			}
		}

		DocumentQuestionController documentQuestionController = new DocumentQuestionController();
		try {
			documentQuestionController.saveDocumentQuestion(
					this.selectedMessageSent.getDocument(),
					contextBean.getLoggedUser(), this.newBid, fractionsStr);
		} catch (ApplicationException e) {
			FacesContext context = FacesContext.getCurrentInstance();
			FacesMessage facesMessage = new FacesMessage(
					FacesMessage.SEVERITY_ERROR, "",
					"Houve um problema ao registrar sua oferta. Tenta mais tarde");
			context.addMessage(null, facesMessage);
			RequestContext.getCurrentInstance()
					.execute("window.scrollTo(0,0);");
			return;
		}
		RequestContext.getCurrentInstance().closeDialog(null);
	}

	public void saveBidSent() {
		String[] fractionsStr = null;
		int i = 0;
		if (this.newBid.doubleValue() <= 0) {
			FacesContext context = FacesContext.getCurrentInstance();
			FacesMessage facesMessage = new FacesMessage(
					FacesMessage.SEVERITY_ERROR, "",
					"Valor do novo lance inválido");
			context.addMessage(null, facesMessage);
			RequestContext.getCurrentInstance()
					.execute("window.scrollTo(0,0);");
			return;
		}

		if (fractions != null && fractions.size() > 0) {
			fractionsStr = new String[fractions.size()];
			for (DocumentFraction docFrac : fractions) {
				fractionsStr[i] = String.valueOf(docFrac
						.getXidFractionDocument());
				i++;
			}
		}

		DocumentQuestionController documentQuestionController = new DocumentQuestionController();
		try {
			documentQuestionController.saveDocumentQuestion(
					this.selectedMessageSent.getDocument(),
					contextBean.getLoggedUser(), this.newBid, fractionsStr);
		} catch (ApplicationException e) {
			FacesContext context = FacesContext.getCurrentInstance();
			FacesMessage facesMessage = new FacesMessage(
					FacesMessage.SEVERITY_ERROR, "",
					"Houve um problema ao registrar sua oferta. Tenta mais tarde");
			context.addMessage(null, facesMessage);
			RequestContext.getCurrentInstance()
					.execute("window.scrollTo(0,0);");
			return;
		}
		RequestContext.getCurrentInstance().closeDialog(null);
	}

	public void requestBetterBid() {
		DocumentQuestion docQ = null;
		DocumentQuestionController documentQuestionController = new DocumentQuestionController();
		docQ = documentQuestionController.getLastDocumentQuestion(
				selectedMessage.getDocument(), selectedMessage.getUserBid());
		documentQuestionController.setRaiseBidFieldFlag(docQ);
		RequestContext.getCurrentInstance().closeDialog(null);
	}

	public void cancelBid() {
		RequestContext.getCurrentInstance().closeDialog(null);
	}

	public String msgConfirm() {
		String msg;
		msg = "Uma mensagem será enviada para o usuário "
				+ selectedMessage.getUserBid().getLogin();
		msg += new String(new byte[] { 0x0D, 0x0A });
		msg += "Confirma ?";
		return msg;
	}

	public void closeDeal() {
		closedDeal = true;
		RequestContext.getCurrentInstance().closeDialog(null);
	}

	private void redirectDealPage() {
		FacesContext fc = FacesContext.getCurrentInstance();
		ConfigurableNavigationHandler nav = (ConfigurableNavigationHandler) fc
				.getApplication().getNavigationHandler();
		nav.performNavigation("/restricted/closedeal");

	}

	public List<SelectItem> getFractionDocs() {
		DecimalFormat df = new java.text.DecimalFormat("#,##0.00");
		totalFraction = new BigDecimal(0);

		DocumentQuestionController documentQuestionController = new DocumentQuestionController();

		if (selectedMessageSent == null) {
			fractions = documentQuestionController
					.getDocumentFraction(getLastDocumentQuestion());
		} else {
			fractions = documentQuestionController
					.getDocumentFraction(selectedMessageSent);
		}
		

		this.fractionDocs = new ArrayList<SelectItem>();
		for (DocumentFraction fraction : fractions) {
			this.fractionDocs.add(new SelectItem(fraction
					.getXidFractionDocument(), df.format(fraction.getPrice())));
			totalFraction = totalFraction.add(fraction.getPrice());
		}

		return fractionDocs;
	}

	public BigDecimal getTotalFraction() {
		return totalFraction;
	}

	public DocumentQuestion getLastDocumentQuestion() {
		DocumentQuestion docQ = null;

		DocumentQuestionController documentQuestionController = new DocumentQuestionController();
		docQ = documentQuestionController.getLastDocumentQuestion(
				selectedMessage.getDocument(), selectedMessage.getUserBid());

		return docQ;
	}

	public List<DocumentFraction> getFractions() {
		return fractions;
	}

	public String getTitleText(DocumentQuestion message) {
		Document document = message.getDocument();
		DocumentQuestionController documentQuestionController;

		if (document.getLastDocumentStatus().getXidDocumentStatus() == SystemDomain.documentStatusSold) {
			disableMessageOpen = true;
			return "Crédito vendido";
		}

		if (message.getRaiseBidRequest().equals("S")) {
			disableMessageOpen = true;
			if (document.isAllowFractionPrice()) {
				return "Fração vendida";
			}
			return "Crédito vendido";
		}

		if (document.isAllowFractionPrice()) {
			documentQuestionController = new DocumentQuestionController();
			if (documentQuestionController.isAnyFractionSold(message)) {
				disableMessageOpen = true;
				return "Fração vendida";
			}

		}

		if (msgType.equals(MESSAGE_TYPE_SENT)) {
			documentQuestionController = new DocumentQuestionController();

			if (documentQuestionController
					.getLastDocumentQuestion(document,
							contextBean.getLoggedUser()).getRaiseBidRequest()
					.equals("N")) {
				disableMessageOpen = true;
				return "Aguardar resposta do vendedor";
			}

			if (documentQuestionController
					.getLastDocumentQuestion(document,
							contextBean.getLoggedUser()).getRaiseBidRequest()
					.equals("Y")
					&& documentQuestionController.getLastDocumentQuestion(
							document, contextBean.getLoggedUser()).equals(
							message)) {
				disableMessageOpen = false;
				return "Fazer nova oferta";
			} else {
				disableMessageOpen = true;
				return "Aguardar resposta do vendedor";
			}
		}

		if (msgType.equals(MESSAGE_TYPE_RECEIVED)) {
			documentQuestionController = new DocumentQuestionController();

			if (message.getRaiseBidRequest().equals("N")) {
				disableMessageOpen = false;
				return "Ver lance";
			}

			if (message.getRaiseBidRequest().equals("Y")) {
				disableMessageOpen = true;
				return "Aguardar resposta do comprador";
			}
		}

		disableMessageOpen = false;
		return "Ver lance";
	}

	public String getTitleText2(Message message) {
		Document document = message.getDocument();
		// DocumentQuestionController documentQuestionController;

		if (document.getLastDocumentStatus().getXidDocumentStatus() == SystemDomain.documentStatusSold) {
			disableMessageOpen = true;
			return "Crédito vendido";
		}

		if (message.getRaiseBidRequest().equals("S")) {
			disableMessageOpen = true;
			if (document.isAllowFractionPrice()) {
				return "Fração vendida";
			}
			return "Crédito vendido";
		}

		/*
		 * if (document.isAllowFractionPrice()) { documentQuestionController =
		 * new DocumentQuestionController(); if
		 * (documentQuestionController.isAnyFractionSold(message)) {
		 * disableMessageOpen = true; return "Fração vendida"; } }
		 */

		if (msgType.equals(MESSAGE_TYPE_SENT)) {

			if (getLastDocumentQuestion().getRaiseBidRequest().equals("N")) {
				disableMessageOpen = true;
				return "Aguardar resposta do vendedor";
			}

			if (getLastDocumentQuestion().getRaiseBidRequest().equals("Y")
					&& getLastDocumentQuestion().equals(message)) {
				disableMessageOpen = false;
				return "Fazer nova oferta";
			} else {
				disableMessageOpen = true;
				return "Aguardar resposta do vendedor";
			}
		}

		if (msgType.equals(MESSAGE_TYPE_RECEIVED)) {

			if (message.getRaiseBidRequest().equals("N")) {
				disableMessageOpen = false;
				return "Ver lance";
			}

			if (message.getRaiseBidRequest().equals("Y")) {
				disableMessageOpen = true;
				return "Aguardar resposta do comprador";
			}
		}

		disableMessageOpen = false;
		return "Ver lance";
	}

	public boolean isDisableMessageOpen() {

		return disableMessageOpen;
	}

	public String getMsgType() {
		return msgType;
	}

	public void setMsgType(String msgType) {
		this.msgType = msgType;
	}

	public List<SelectItem> getBidsOffered() {
		DecimalFormat df = new java.text.DecimalFormat("#,##0.00");
		DocumentQuestionController documentQuestionController;
		List<BigDecimal> bids = null;

		documentQuestionController = new DocumentQuestionController();

		bids = documentQuestionController.getBids(
				selectedMessage.getDocument(), selectedMessage.getUserBid());

		bidsOffered = new ArrayList<SelectItem>();

		for (BigDecimal bid : bids) {
			this.bidsOffered
					.add(new SelectItem(1, df.format(bid.doubleValue())));

		}

		return bidsOffered;
	}

	public List<DocumentQuestion> getMessagesSent() {
		return messagesSent;
	}

	public void setMessagesSent(List<DocumentQuestion> messagesSent) {
		this.messagesSent = messagesSent;
	}

	public DocumentQuestion getSelectedMessageSent() {
		return selectedMessageSent;
	}

	public void setSelectedMessageSent(DocumentQuestion selectedMessageSent) {
		this.selectedMessageSent = selectedMessageSent;
	}

}
