package com.mercadodecreditos.web;

import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;

import com.mercadodecreditos.controller.DocumentController;
import com.mercadodecreditos.model.Document;
import com.mercadodecreditos.util.ApplicationException;

@ManagedBean(name = "closeDealBean")
@RequestScoped
public class CloseDeal implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2954721559118046709L;
	@ManagedProperty(value = "#{messageBean}")
	private MessageBean messageBean;

	public Document getDocumentDeal() {
		return messageBean.getSelectedMessage().getDocument();
	}

	public MessageBean getMessageBean() {
		return messageBean;
	}

	public void setMessageBean(MessageBean messageBean) {
		this.messageBean = messageBean;
	}

	public String closeDeal() throws ApplicationException {
		DocumentController documentContrtoller = new DocumentController();
		if (messageBean.getSelectedMessage().getDocument()
				.isAllowFractionPrice()) {			
			documentContrtoller.documentSold(messageBean.getLastDocumentQuestion(),messageBean.getFractions());	
		} else {
			documentContrtoller.documentSold(messageBean.getLastDocumentQuestion());
		}
		return "/public/principal";
	}
}
