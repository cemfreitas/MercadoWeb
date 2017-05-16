package com.mercadodecreditos.web;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;

import org.primefaces.context.RequestContext;

import com.mercadodecreditos.controller.DocumentController;
import com.mercadodecreditos.controller.DocumentQuestionController;
import com.mercadodecreditos.model.Document;
import com.mercadodecreditos.model.DocumentFraction;
import com.mercadodecreditos.util.ApplicationException;
import com.mercadodecreditos.util.SystemDomain;

@ManagedBean(name = "documentQuestionBean")
@SessionScoped
public class DocumentQuestionBean implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5572021914059719726L;
	private Document selectedDocument;
	private BigDecimal bid, totalFraction = new BigDecimal(0);
	private List<SelectItem> fractionDocsList;
	private String[] selectedFractionDocs;
	private Hashtable<Long, BigDecimal> fractionTable = new Hashtable<Long, BigDecimal>();

	@ManagedProperty(value = "#{contextBean}")
	private ContextBean contextBean;

	public ContextBean getContextBean() {
		return contextBean;
	}

	public void setContextBean(ContextBean contextBean) {
		this.contextBean = contextBean;
	}

	public BigDecimal getBid() {
		return bid;
	}

	public void setBid(BigDecimal bid) {
		this.bid = bid;
	}

	public Document getSelectedDocument() {
		return selectedDocument;
	}

	public void setSelectedDocument(Document selectedDocument) {
		this.selectedDocument = selectedDocument;
	}

	public void placeBid() {
		Map<String, Object> options = new HashMap<String, Object>();
		selectedFractionDocs = null;
		totalFraction = new BigDecimal(0);
		this.bid = null;
		if (selectedDocument.isAllowFractionPrice()) {
			getFractionValues();
			options.put("modal", true);
			options.put("resizable", false);
			options.put("draggable", false);
			options.put("contentHeight", 300);
			options.put("contentWidth", 440 + (fractionDocsList.size() * 55));
			options.put("closable", false);
			RequestContext.getCurrentInstance().openDialog(
					"/restricted/userbidfraction", options, null);
		} else {
			options.put("modal", true);
			options.put("resizable", false);
			options.put("draggable", false);
			options.put("closable", false);
			if (selectedDocument.getDocumentType().getXidDocumentType() == SystemDomain.documentTypeCreditoICMS) {
				options.put("contentHeight", 255);
				options.put("contentWidth", 370);
			} else {
				options.put("contentHeight", 300);
				options.put("contentWidth", 420);
			}
			RequestContext.getCurrentInstance().openDialog(
					"/restricted/userbid", options, null);
		}
	}

	public void cancelBid() {
		RequestContext.getCurrentInstance().closeDialog(null);
	}

	public void saveBid() {
		if (this.bid.doubleValue() <= 0) {
			FacesContext context = FacesContext.getCurrentInstance();
			FacesMessage facesMessage = new FacesMessage(
					FacesMessage.SEVERITY_ERROR, "", "Valor do lance inválido");
			context.addMessage(null, facesMessage);
			RequestContext.getCurrentInstance()
					.execute("window.scrollTo(0,0);");
			return;
		}
		if (selectedDocument.isAllowFractionPrice()) {
			if (selectedFractionDocs == null
					|| selectedFractionDocs.length == 0) {
				FacesContext context = FacesContext.getCurrentInstance();
				FacesMessage facesMessage = new FacesMessage(
						FacesMessage.SEVERITY_ERROR, "",
						"Selecione ao menos um valor fracionado");
				context.addMessage(null, facesMessage);
				RequestContext.getCurrentInstance().execute(
						"window.scrollTo(0,0);");
				return;
			}
			if (this.totalFraction.doubleValue() < this.bid.doubleValue()) {
				FacesContext context = FacesContext.getCurrentInstance();
				FacesMessage facesMessage = new FacesMessage(
						FacesMessage.SEVERITY_ERROR, "",
						"Valor do lance maior que o valor total das frações");
				context.addMessage(null, facesMessage);
				RequestContext.getCurrentInstance().execute(
						"window.scrollTo(0,0);");
				return;
			}
		} else {			
			double bid = (selectedDocument.getNetValueToReceive() == null ? selectedDocument
					.getPrice().doubleValue() : selectedDocument
					.getNetValueToReceive().doubleValue());
			if (bid < this.bid
					.doubleValue()) {
				FacesContext context = FacesContext.getCurrentInstance();
				FacesMessage facesMessage = new FacesMessage(
						FacesMessage.SEVERITY_ERROR, "",
						"Valor do lance maior do que o valor do crédito");
				context.addMessage(null, facesMessage);
				RequestContext.getCurrentInstance().execute(
						"window.scrollTo(0,0);");
				return;
			}
		}
		DocumentQuestionController documentQuestionController = new DocumentQuestionController();
		try {
			documentQuestionController.saveDocumentQuestion(
					this.selectedDocument, contextBean.getLoggedUser(),
					this.bid, selectedFractionDocs);
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

	public List<SelectItem> getFractionValues() {
		DecimalFormat df = new java.text.DecimalFormat("#,##0.00");
		List<DocumentFraction> list = null;
		DocumentController documentController = new DocumentController();
		list = documentController.getFractionDocuments(selectedDocument);
		this.fractionDocsList = new ArrayList<SelectItem>();
		for (DocumentFraction documentFraction : list) {
			if ((documentFraction.getStatus().getXidDocumentStatus() == SystemDomain.documentStatusReady)
					|| (documentFraction.getStatus().getXidDocumentStatus() == SystemDomain.documentStatusInNegotiation)) {
				this.fractionDocsList.add(new SelectItem(documentFraction
						.getXidFractionDocument(), df.format(documentFraction
						.getPrice())));
				fractionTable.put(documentFraction.getXidFractionDocument(),
						documentFraction.getPrice());
			}
		}

		return fractionDocsList;
	}

	public String[] getSelectedFractionDocs() {
		int i = 0;
		List<DocumentFraction> docFracList = null;
		DocumentQuestionController docQC = null;

		if (this.fractionDocsList != null && this.fractionDocsList.size() == 1) {
			SelectItem item = this.fractionDocsList.get(0);

			return selectedFractionDocs = new String[] { item.getValue()
					.toString() };
		}

		docQC = new DocumentQuestionController();
		docFracList = docQC.getSelectedDocumentFraction(
				contextBean.getLoggedUser(), selectedDocument);
		if (docFracList != null) {
			selectedFractionDocs = new String[docFracList.size()];
			for (DocumentFraction docFrac : docFracList) {
				selectedFractionDocs[i] = String.valueOf(docFrac
						.getXidFractionDocument());
				i++;
			}
		}
		return selectedFractionDocs;
	}

	public void setSelectedFractionDocs(String[] selectedFractionDocs) {
		this.selectedFractionDocs = selectedFractionDocs;
	}

	public void AccTotalFraction() {
		totalFraction = new BigDecimal(0);
		if (selectedFractionDocs != null && selectedFractionDocs.length > 0) {
			for (int i = 0; i < selectedFractionDocs.length; i++) {
				totalFraction = totalFraction.add(fractionTable.get(Long
						.valueOf(selectedFractionDocs[i])));
			}
		}
	}

	public BigDecimal getTotalFraction() {
		if (selectedFractionDocs != null && selectedFractionDocs.length > 0) {
			totalFraction = new BigDecimal(0);
			for (int i = 0; i < selectedFractionDocs.length; i++) {
				totalFraction = totalFraction.add(fractionTable.get(Long
						.valueOf(selectedFractionDocs[i])));
			}
		}
		return totalFraction;
	}

	public void setTotalFraction(BigDecimal totalFraction) {
		this.totalFraction = totalFraction;
	}
}
