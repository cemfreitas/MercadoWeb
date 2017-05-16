package com.mercadodecreditos.web;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;

import org.primefaces.context.RequestContext;

import com.mercadodecreditos.controller.DocumentController;
import com.mercadodecreditos.controller.DocumentTypeController;
import com.mercadodecreditos.controller.StateCityController;
import com.mercadodecreditos.dao.DocumentStatusDao;
import com.mercadodecreditos.dao.DocumentStatusDaoImpl;
import com.mercadodecreditos.model.City;
import com.mercadodecreditos.model.Document;
import com.mercadodecreditos.model.DocumentFraction;
import com.mercadodecreditos.model.DocumentStatus;
import com.mercadodecreditos.model.DocumentType;
import com.mercadodecreditos.model.State;
import com.mercadodecreditos.util.ApplicationException;
import com.mercadodecreditos.util.SystemDomain;

@ManagedBean(name = "documentBean")
@SessionScoped
public class DocumentBean implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2887202579632975930L;
	private Document document, selectedDocument;
	private List<Document> documents;
	private int idState, idCity, idDocumentType, status;
	private List<SelectItem> documentTypes, states, cities, statusList;
	private BigDecimal price,netValue,incomeTax;
	private boolean showTableDocs;
	private String issueDate;

	@ManagedProperty(value = "#{contextBean}")
	private ContextBean contextBean;

	public ContextBean getContextBean() {
		return contextBean;
	}

	public void setContextBean(ContextBean contextBean) {
		this.contextBean = contextBean;
	}

	public Document getSelectedDocument() {
		return selectedDocument;
	}

	public void setSelectedDocument(Document selectedDocument) {
		this.selectedDocument = selectedDocument;
	}

	public String getIssueDate() {
		return issueDate;
	}

	public void setIssueDate(String issueDate) {
		this.issueDate = issueDate;
	}

	public List<SelectItem> getStatusList() {
		return statusList;
	}

	public void setStatusList(List<SelectItem> statusList) {
		this.statusList = statusList;
	}

	public BigDecimal getPrice() {
		if (this.price != null && this.price.doubleValue() == 0) {
			this.price = null;
		}
		return price;
	}

	public void setPrice(BigDecimal price) {		
		this.price = price;
	}

	public boolean isShowTableDocs() {
		return showTableDocs;
	}

	public void setShowTableDocs(boolean showTableDocs) {
		this.showTableDocs = showTableDocs;
	}

	public List<Document> getDocuments() {
		return documents;
	}

	public void setDocuments(List<Document> documents) {
		this.documents = documents;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String newDocument() {
		this.document = new Document();
		documentTypes = null;
		this.states = null;
		this.cities = null;
		this.idDocumentType = 0;
		this.idCity = 0;
		this.idState = 0;
		this.price = null;
		this.incomeTax = null;
		this.netValue = null;
		this.issueDate = "";
		this.document.setAllowFractionPrice(false);
		this.document.setCheckedByState(true);
		return "/restricted/documentform";
	}

	public String viewDocuments() {
		this.selectedDocument = null;
		this.showTableDocs = false;
		this.status = -1;
		this.getDocumentStatusList();
		return "/restricted/managedocument";
	}

	public String save() {
		try {
			validateFields();
			DocumentController documentController = new DocumentController();
			getStateAndCityForSave();
			getDocumentTypeForSave();
			this.document.setPrice(this.price);
			this.document.setNetValueToReceive(this.netValue);
			this.document.setIncomeTaxValue(this.incomeTax);
			documentController.saveDocument(document);
			return "/restricted/docsavedmessage";
		} catch (ApplicationException e) {
			FacesContext context = FacesContext.getCurrentInstance();
			FacesMessage facesMessage = new FacesMessage(
					FacesMessage.SEVERITY_ERROR, "", e.getMessage());
			context.addMessage(null, facesMessage);
			RequestContext.getCurrentInstance()
					.execute("window.scrollTo(0,0);");
			return null;
		}
	}

	public void update() {
		try {
			validateFields();
			DocumentController documentController = new DocumentController();
			getStateAndCityForSave();
			getDocumentTypeForSave();
			documentController.updateDocument(document);
		} catch (ApplicationException e) {
			FacesContext context = FacesContext.getCurrentInstance();
			FacesMessage facesMessage = new FacesMessage(
					FacesMessage.SEVERITY_ERROR, "", e.getMessage());
			context.addMessage(null, facesMessage);
			RequestContext.getCurrentInstance()
					.execute("window.scrollTo(0,0);");
		}
	}

	private void getStateAndCityForSave() {
		StateCityController cityState = new StateCityController();
		City city = cityState.getCityById(this.idCity);
		State state = cityState.getStateById(this.idState);
		this.document.setCity(city);
		this.document.setState(state);
	}

	private void getDocumentTypeForSave() {
		DocumentTypeController documentTypeController = new DocumentTypeController();
		DocumentType documentType = documentTypeController
				.getDocumentTypeById(this.idDocumentType);
		this.document.setDocumentType(documentType);
	}

	public void fillCities(ValueChangeEvent e) {
		if (e.getNewValue() == null) {
			return;
		}
		List<City> list = null;
		StateCityController stateCityController = new StateCityController();
		list = stateCityController.getCitiesByState((int) e.getNewValue());
		this.cities = new ArrayList<SelectItem>();
		for (City city : list) {
			this.cities.add(new SelectItem(city.getXidCity(), city
					.getDescription()));
		}

	}

	public void changedDocumentType(ValueChangeEvent e) {
		if (e.getNewValue() == null) {
			return;
		}

		if ((int) e.getNewValue() == SystemDomain.documentTypeCreditoICMS) {
			this.idState = contextBean.getLoggedUser().getState().getXidState();
		}
	}

	public Document getDocument() {
		return document;
	}

	public void setDocument(Document document) {
		this.document = document;
	}

	public int getIdState() {
		return idState;
	}

	public void setIdState(int idState) {
		this.idState = idState;
	}

	public int getIdCity() {
		return idCity;
	}

	public void setIdCity(int idCity) {
		this.idCity = idCity;
	}

	public int getIdDocumentType() {
		return idDocumentType;
	}

	public void setIdDocumentType(int idDocumentType) {
		this.idDocumentType = idDocumentType;
	}

	public List<SelectItem> getDocumentTypes() {
		List<DocumentType> list = null;
		DocumentTypeController documentTypeController = new DocumentTypeController();
		list = documentTypeController.getAll();
		if (this.documentTypes == null) {
			this.documentTypes = new ArrayList<SelectItem>();
			for (DocumentType documentType : list) {

				if (documentType.getXidDocumentType() == SystemDomain.documentTypeCreditoICMS
						&& !contextBean.getLoggedUser().isAdmin()) {
					if (contextBean.getLoggedUser().getUserType()
							.getXidUserType() == SystemDomain.userTypePessoaJuridica) {
						this.documentTypes.add(new SelectItem(documentType
								.getXidDocumentType(), documentType
								.getDescription()));
					}
				} else {
					this.documentTypes.add(new SelectItem(documentType
							.getXidDocumentType(), documentType
							.getDescription()));
				}

			}
		}
		return documentTypes;
	}	

	public void setDocumentTypes(List<SelectItem> documentTypes) {
		this.documentTypes = documentTypes;
	}

	public List<SelectItem> getStates() {
		List<State> list = null;
		StateCityController stateCityController = new StateCityController();
		list = stateCityController.getAllStates();
		if (this.states == null) {
			this.states = new ArrayList<SelectItem>();
			for (State estate : list) {
				this.states.add(new SelectItem(estate.getXidState(), estate
						.getDescription()));
			}
		}
		return states;
	}

	public void setStates(List<SelectItem> states) {
		this.states = states;
	}

	public List<SelectItem> getCities() {
		return cities;
	}

	public void setCities(List<SelectItem> cities) {
		this.cities = cities;
	}

	public boolean showStateField() {
		if (this.idDocumentType == SystemDomain.documentTypePrecatorioUniao) {
			return false;
		} else {
			return true;
		}
	}

	public boolean disableStateField() {
		if (this.idDocumentType == SystemDomain.documentTypePrecatorioUniao
				|| this.idDocumentType == SystemDomain.documentTypeCreditoICMS) {
			return true;
		} else {
			return false;
		}
	}

	public boolean showCityField() {
		if (this.idDocumentType == SystemDomain.documentTypePrecatorioMunicipal) {
			return true;
		} else {
			return false;
		}
	}

	public void listDocuments(ValueChangeEvent e) {
		if (e.getNewValue() == null) {
			return;
		}

		if ((int) e.getNewValue() == -1) {
			return;
		}

		DocumentController documentController = new DocumentController();
		documents = documentController.listUserDocuments(
				contextBean.getLoggedUser(), (int) e.getNewValue());

		if (documents != null && documents.size() > 0) {
			this.showTableDocs = true;
		} else {
			this.showTableDocs = false;
		}

	}

	private void getDocumentStatusList() {
		List<DocumentStatus> list;
		DocumentStatusDao documentStatusDao = new DocumentStatusDaoImpl();
		list = documentStatusDao.listAll();

		this.statusList = new ArrayList<SelectItem>();
		for (DocumentStatus docStatus : list) {
			this.statusList.add(new SelectItem(
					docStatus.getXidDocumentStatus(), docStatus
							.getDescription()));
		}
	}

	public void remove() {

	}

	public void edit() {
		Map<String, Object> options = new HashMap<String, Object>();
		options.put("modal", true);
		options.put("resizable", false);
		options.put("draggable", false);
		options.put("contentHeight", 255);
		options.put("contentWidth", 370);
		options.put("closable", false);
		RequestContext.getCurrentInstance().openDialog("/restricted/userbid",
				options, null);
	}

	public void showFractionDocuments() {
		RequestContext.getCurrentInstance().openDialog(
				"/restricted/fractiondocs");
	}

	public List<DocumentFraction> getFractionDocs() {
		List<DocumentFraction> fracDocs = null;
		DocumentController documentController = new DocumentController();
		fracDocs = documentController
				.getFractionDocuments(this.selectedDocument);

		return fracDocs;
	}

	private void validateFields() throws ApplicationException {

		if (this.idDocumentType <= 0) {
			throw new ApplicationException("Escolha um tipo de crédito");
		}

		if (this.idDocumentType != SystemDomain.documentTypeCreditoICMS
				&& document.getDocumentNumber().trim().equals("")) {
			throw new ApplicationException("Número do precatório é obrigatório");
		}

		if (showStateField() && this.idState <= 0) {
			throw new ApplicationException("Escolha um estado");
		}

		if (showCityField() && this.idCity <= 0) {
			throw new ApplicationException("Escolha um município");
		}

		if (this.price.intValue() <= 0) {
			throw new ApplicationException("Valor do crédito inválido");
		}

		if (this.issueDate.trim().equals("")) {
			throw new ApplicationException("Data de emissão é obrigatória");
		} else {
			SimpleDateFormat fmt = new SimpleDateFormat("dd/MM/yyyy");
			try {
				fmt.setLenient(false);
				document.setIssueDate(fmt.parse(this.issueDate));
			} catch (ParseException e) {
				throw new ApplicationException("Data de emissão inválida");
			}
			if (document.getIssueDate().compareTo(new Date()) > 0) {
				throw new ApplicationException("Data de emissão inválida");
			}
		}
	}

	public BigDecimal getNetValue() {
		if (this.netValue != null && this.netValue.doubleValue() == 0) {
			this.netValue = null;
		}
		return netValue;
	}

	public void setNetValue(BigDecimal netValue) {
		this.netValue = netValue;
	}

	public BigDecimal getIncomeTax() {
		if (this.incomeTax != null && this.incomeTax.doubleValue() == 0) {
			this.incomeTax = null;
		}
		return incomeTax;
	}

	public void setIncomeTax(BigDecimal incomeTax) {
		this.incomeTax = incomeTax;
	}
}
