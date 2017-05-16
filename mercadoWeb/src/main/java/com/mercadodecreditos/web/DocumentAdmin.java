package com.mercadodecreditos.web;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;

import org.primefaces.context.RequestContext;

import com.mercadodecreditos.controller.DocumentController;
import com.mercadodecreditos.controller.DocumentStatusController;
import com.mercadodecreditos.model.Document;
import com.mercadodecreditos.model.DocumentStatus;
import com.mercadodecreditos.util.ApplicationException;

@ManagedBean(name = "documentAdminBean")
@SessionScoped
public class DocumentAdmin implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -2139591176894421570L;
	private Document document, selectedDocument;
	private List<Document> documents;
	private List<SelectItem> statusList;
	private String login,explanation;
	private int selectedStatus,statusChanged;
	private String[] documentTypes;

	public String documentAdmin() {
		statusList = null;
		documentTypes = null;
		document = null;
		documents = null;
		selectedDocument = null;
		explanation = "";
		selectedStatus = -1;
		login = "";
		documentTypes = new String[4];
		for (int i = 0; i < 4; i++) {
			documentTypes[i] = String.valueOf(i + 1);
		}
		return "/admin/documentadmin";
	}

	public Document getDocument() {
		return document;
	}

	public void setDocument(Document document) {
		this.document = document;
	}

	public Document getSelectedDocument() {
		return selectedDocument;
	}

	public void setSelectedDocument(Document selectedDocument) {
		this.selectedDocument = selectedDocument;
	}

	public List<Document> getDocuments() {
		return documents;
	}

	public void setDocuments(List<Document> documents) {
		this.documents = documents;
	}

	public List<SelectItem> getStatusList() {
		List<DocumentStatus> list = null;
		if (this.statusList == null) {
			DocumentStatusController documentStatusController = new DocumentStatusController();
			list = documentStatusController.getAll();
			this.statusList = new ArrayList<SelectItem>();
			for (DocumentStatus documentStatus : list) {
				if (selectedDocument == null
						|| !selectedDocument.getLastDocumentStatus().equals(
								documentStatus)) {
					this.statusList.add(new SelectItem(documentStatus
							.getXidDocumentStatus(), documentStatus
							.getDescription()));
				}
			}
		}
		return statusList;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public void searchDocuments() {
		DocumentController documentController = new DocumentController();
		documents = documentController.searchDocumentAdmin(this.login,
				this.documentTypes, this.selectedStatus);
	}

	public int getSelectedStatus() {
		return selectedStatus;
	}

	public void setSelectedStatus(int selectedStatus) {
		this.selectedStatus = selectedStatus;
	}

	public String[] getDocumentTypes() {
		return documentTypes;
	}

	public void setDocumentTypes(String[] documentTypes) {
		this.documentTypes = documentTypes;
	}
	
	public int getStatusChanged() {
		return statusChanged;
	}

	public void setStatusChanged(int statusChanged) {
		this.statusChanged = statusChanged;
	}
	
	public String getExplanation() {
		return explanation;
	}

	public void setExplanation(String explanation) {
		this.explanation = explanation;
	}

	public void manageDocument() {
		Map<String, Object> options = new HashMap<String, Object>();
		options.put("modal", true);
		options.put("resizable", false);
		options.put("draggable", false);
		options.put("contentHeight", 400);
		options.put("contentWidth", 460);
		options.put("closable", false);
		this.statusList = null;
		this.explanation = "";
		RequestContext.getCurrentInstance().openDialog(
				"/admin/changedocstatus", options, null);
	}

	public void changeDocStatus() {
		try {
		DocumentController documentController = new DocumentController();
		documentController.changeDocumentStatus(selectedDocument, statusChanged, explanation);
		selectedDocument = null; 
		statusList = null;
		} catch (ApplicationException e) {
			FacesContext context = FacesContext.getCurrentInstance();
			FacesMessage facesMessage = new FacesMessage(
					FacesMessage.SEVERITY_ERROR, "", e.getMessage());
			context.addMessage(null, facesMessage);
			RequestContext.getCurrentInstance()
					.execute("window.scrollTo(0,0);");			
		} finally {
			RequestContext.getCurrentInstance().closeDialog(null);
		}		
	}

	public void cancelChangeDocStatus() {
		selectedDocument = null;
		statusList = null;
		RequestContext.getCurrentInstance().closeDialog(null);
	}
}
