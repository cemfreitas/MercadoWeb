package com.mercadodecreditos.web;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import org.primefaces.context.RequestContext;

import com.mercadodecreditos.controller.DocumentController;
import com.mercadodecreditos.model.Document;
import com.mercadodecreditos.util.ApplicationException;
import com.mercadodecreditos.util.SystemDomain;

@ManagedBean(name = "searchDocumentBean")
@SessionScoped
public class SearchDocumentBean implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -2085007845471840273L;
	private String[] documentTypes;
	private int idState, idCity;
	private BigDecimal initValue, finalValue;
	private String initDateStr, finalDateStr;
	private Date initDate, finalDate;
	private String searchDocType;

	private boolean showTableDocs, firstTime = true;
	private List<Document> documents;

	@ManagedProperty(value = "#{contextBean}")
	private ContextBean contextBean;

	public ContextBean getContextBean() {
		return contextBean;
	}

	public void setContextBean(ContextBean contextBean) {
		this.contextBean = contextBean;
	}

	public String init(String docType) {
		documents = new ArrayList<Document>();
		idState = 0;
		idCity = 0;
		initValue = null;
		finalValue = null;
		initDateStr = "";
		finalDateStr = "";
		showTableDocs = false;
		searchDocType = docType;
		if (docType.equals("all")) {
			documentTypes = new String[4];
			for (int i = 0; i < 4; i++) {
				documentTypes[i] = String.valueOf(i + 1);
			}
		} else if (docType.equals("precatorio")) {
			documentTypes = new String[3];
			documentTypes[0] = String
					.valueOf(SystemDomain.documentTypePrecatorioMunicipal);
			documentTypes[1] = String
					.valueOf(SystemDomain.documentTypePrecatorioEstadual);
			documentTypes[2] = String
					.valueOf(SystemDomain.documentTypePrecatorioUniao);
		} else if (docType.equals("icms")) {
			documentTypes = new String[1];
			documentTypes[0] = String
					.valueOf(SystemDomain.documentTypeCreditoICMS);
		}
		if (firstTime) {
			firstTime = false;
			return "/restricted/searchdocument";
		} else {
			return "/restricted/searchdocument?faces-redirect=true";
		}
	}

	public List<Document> getDocuments() {
		return documents;
	}

	public void setDocuments(List<Document> documents) {
		this.documents = documents;
	}

	public boolean isShowTableDocs() {
		return showTableDocs;
	}

	public void setShowTableDocs(boolean showTableDocs) {
		this.showTableDocs = showTableDocs;
	}

	public String getFinalDateStr() {
		return finalDateStr;
	}

	public void setFinalDateStr(String finalDateStr) {
		this.finalDateStr = finalDateStr;
	}

	public String getInitDateStr() {
		return initDateStr;
	}

	public void setInitDateStr(String initDateStr) {
		this.initDateStr = initDateStr;
	}

	public BigDecimal getFinalValue() {
		return finalValue;
	}

	public void setFinalValue(BigDecimal finalValue) {
		this.finalValue = finalValue;
	}

	public BigDecimal getInitValue() {
		return initValue;
	}

	public void setInitValue(BigDecimal initValue) {
		this.initValue = initValue;
	}

	public int getIdCity() {
		return idCity;
	}

	public void setIdCity(int idCity) {
		this.idCity = idCity;
	}

	public int getIdState() {
		return idState;
	}

	public void setIdState(int idState) {
		this.idState = idState;
	}

	public String[] getDocumentTypes() {
		return documentTypes;
	}

	public void setDocumentTypes(String[] documentTypes) {
		this.documentTypes = documentTypes;
	}

	public boolean showDocumentTypes() {
		if (searchDocType.equals("all")) {
			return true;
		} else {
			return false;
		}

	}

	public void searchDocuments() {
		if (documentTypes == null || documentTypes.length == 0) {
			showTableDocs = false;
			return;
		}
		DocumentController documentController = new DocumentController();

		try {
			getRangeDate();
			documents = documentController.searchDocument(
					contextBean.getLoggedUser(), documentTypes, idState,
					idCity, initValue, finalValue, initDate, finalDate);
		} catch (ApplicationException e) {
			FacesContext context = FacesContext.getCurrentInstance();
			FacesMessage facesMessage = new FacesMessage(
					FacesMessage.SEVERITY_ERROR, "", e.getMessage());
			context.addMessage(null, facesMessage);
			RequestContext.getCurrentInstance()
					.execute("window.scrollTo(0,0);");
			return;
		}
		if (documents == null || documents.size() == 0) {
			showTableDocs = false;
		} else {
			showTableDocs = true;
		}

		// return documents;
	}

	private void getRangeDate() throws ApplicationException {
		SimpleDateFormat fmt = new SimpleDateFormat("dd/MM/yyyy");

		if (!initDateStr.equals("")) {
			try {
				fmt.setLenient(false);
				initDate = fmt.parse(initDateStr);
			} catch (ParseException e) {
				throw new ApplicationException("Data inicial inválida");
			}
		} else {
			initDate = null;
		}
		if (!finalDateStr.equals("")) {
			try {
				fmt.setLenient(false);
				finalDate = fmt.parse(finalDateStr);
			} catch (ParseException e) {
				throw new ApplicationException("Data final inválida");
			}
		} else {
			finalDate = null;
		}
		if (initDate != null && finalDate != null) {
			if (initDate.compareTo(finalDate) > 0) {
				throw new ApplicationException(
						"Data inicial maior que data final");
			}
		}
	}

	public String getSearchDocType() {
		if (searchDocType.equals("all")) {
			return "créditos";
		} else if (searchDocType.equals("precatorio")) {
			return "precatórios";
		} else {
			return "crédito de ICMS";
		}
	}

	public String getDocumentTypeDesc(Document document) {

		if (document.isAllowFractionPrice()) {
			return document.getDocumentType().getDescription()
					+ " (Crédito fracionado)";
		} else {
			return document.getDocumentType().getDescription();
		}
	}		
}
