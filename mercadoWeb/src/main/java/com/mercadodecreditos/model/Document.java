package com.mercadodecreditos.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Type;

import com.mercadodecreditos.util.SystemDomain;

@Entity
@Table(name = "Document")
public class Document implements Serializable {
	private static final long serialVersionUID = 1325065070029336332L;
	@Id
	@GeneratedValue
	@Column(name = "xidDocument")
	private long xidDocument;
	private String documentNumber;
	@ManyToOne
	@JoinColumn(name = "idUser")
	private User user;
	private String description;
	private BigDecimal price;
	@Type(type = "yes_no")
	private boolean allowFractionPrice;
	private BigDecimal residualPrice;
	private Date validationDate;
	@ManyToOne
	@JoinColumn(name = "idDocumentType")
	private DocumentType documentType;
	@ManyToOne
	@JoinColumn(name = "idState")
	private State state;
	@ManyToOne
	@JoinColumn(name = "idCity")
	private City city;
	private Date issueDate;
	@ManyToOne
	@JoinColumn(name = "lastDocumentStatus")
	private DocumentStatus lastDocumentStatus;
	
	@OneToMany(mappedBy = "document")
	private List<DocumentQuestion> documentQuestion;
	
	@Type(type = "yes_no")
	private boolean checkedByState;
	private BigDecimal incomeTaxValue;
	private BigDecimal netValueToReceive;
	private Date documentTimeStamp;
	private transient boolean enableBidButton;
	private transient String labelBidButton;

	public Document() {

	}

	public Document(Document document) {
		this.allowFractionPrice = document.allowFractionPrice;
		this.checkedByState = document.checkedByState;
		this.city = document.city;
		this.description = document.description;
		this.documentNumber = document.documentNumber;
		this.documentQuestion = document.documentQuestion;
		this.documentType = document.documentType;
		this.issueDate = document.issueDate;
		this.lastDocumentStatus = document.lastDocumentStatus;
		this.state = document.state;
		this.user = document.user;
		this.validationDate = document.validationDate;
	}

	public long getXidDocument() {
		return xidDocument;
	}

	public void setXidDocument(long xidDocument) {
		this.xidDocument = xidDocument;
	}

	public String getDocumentNumber() {
		return documentNumber;
	}

	public void setDocumentNumber(String documentNumber) {
		this.documentNumber = documentNumber;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		if (price == null) {
			return;
		}
		this.price = price.setScale(2, BigDecimal.ROUND_HALF_EVEN);
	}

	public Date getValidationDate() {
		return validationDate;
	}

	public void setValidationDate(Date validationDate) {
		this.validationDate = validationDate;
	}

	public DocumentType getDocumentType() {
		return documentType;
	}

	public void setDocumentType(DocumentType documentType) {
		this.documentType = documentType;
	}

	public State getState() {
		return state;
	}

	public void setState(State state) {
		this.state = state;
	}

	public City getCity() {
		return city;
	}

	public void setCity(City city) {
		this.city = city;
	}

	public Date getIssueDate() {
		return issueDate;
	}

	public void setIssueDate(Date issueDate) {
		this.issueDate = issueDate;
	}

	public DocumentStatus getLastDocumentStatus() {
		return lastDocumentStatus;
	}

	public void setLastDocumentStatus(DocumentStatus lastDocumentStatus) {
		this.lastDocumentStatus = lastDocumentStatus;
	}

	public boolean isCheckedByState() {
		return checkedByState;
	}

	public void setCheckedByState(boolean checkedByState) {
		this.checkedByState = checkedByState;
	}	
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (xidDocument ^ (xidDocument >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Document other = (Document) obj;
		if (xidDocument != other.xidDocument)
			return false;
		return true;
	}

	@Transient
	public boolean isEnableBidButton() {
		return enableBidButton;
	}

	@Transient
	public void setEnableBidButton(boolean enableBidButton) {
		this.enableBidButton = enableBidButton;
	}

	@Transient
	public String getLabelBidButton() {
		return labelBidButton;
	}

	@Transient
	public void setLabelBidButton(String labelBidButton) {
		this.labelBidButton = labelBidButton;
	}

	/*
	 * public String getMaskedDocNumber() { return
	 * documentNumber.replace(documentNumber.subSequence(0, 6), "******"); }
	 */

	public boolean isAllowFractionPrice() {
		return allowFractionPrice;
	}

	public void setAllowFractionPrice(boolean allowFractionPrice) {
		this.allowFractionPrice = allowFractionPrice;
	}

	public BigDecimal getResidualPrice() {
		return residualPrice;
	}

	public void setResidualPrice(BigDecimal residualPrice) {
		if (residualPrice != null) {
			this.residualPrice = residualPrice.setScale(2,
					BigDecimal.ROUND_HALF_EVEN);
		} else {
			this.residualPrice = null;
		}
	}

	public Date getDocumentTimeStamp() {
		return documentTimeStamp;
	}
	
	public BigDecimal getIncomeTaxValue() {
		return incomeTaxValue;
	}

	public void setIncomeTaxValue(BigDecimal incomeTaxValue) {
		this.incomeTaxValue = incomeTaxValue;
	}

	public BigDecimal getNetValueToReceive() {
		return netValueToReceive;
	}

	public void setNetValueToReceive(BigDecimal netValueToReceive) {
		this.netValueToReceive = netValueToReceive;
	}

	public String getGeneratedDocNumber() {
		String docType, sequential, year;

		if (getDocumentType().getXidDocumentType() == SystemDomain.documentTypeCreditoICMS) {
			docType = "ICMS";
		} else {
			docType = "PREC";
		}
		int seqLen = String.valueOf(getXidDocument()).length();
		sequential = replicate("0", (7 - seqLen))
				+ String.valueOf(getXidDocument());

		Calendar cal = Calendar.getInstance();
		
		if (getDocumentTimeStamp() != null) {
			cal.setTime(getDocumentTimeStamp());
		}
		
		year = String.valueOf(cal.get(Calendar.YEAR));

		return year + "-" + sequential + "-" + docType;
	}

	private String replicate(String strRepl, long qtd) {
		StringBuffer str = new StringBuffer();
		
		if (qtd < 0) {
			qtd = qtd * -1;
		}
		
		for (int i = 0; i < qtd; i++) {
			
			str.append(strRepl);
		}
		
		return str.toString();
	}

	@Override
	public String toString() {
		return "Document [xidDocument="
				+ xidDocument
				+ ", documentNumber="
				+ documentNumber
				+ ", user="
				+ user.getName()
				+ ", description="
				+ description
				+ ", price="
				+ (price == null ? "Nulo" : price.toString())
				+ ", allowFractionPrice="
				+ allowFractionPrice
				+ ", residualPrice="
				+ (residualPrice == null ? "Nulo" : residualPrice.toString())
				+ ", validationDate="
				+ validationDate.toString()
				+ ", documentType="
				+ documentType.getDescription()
				+ ", state="
				+ (state == null ? "Nulo" : state.getDescription())
				+ ", city="
				+ (city == null ? "Nulo" : city.getDescription())
				+ ", issueDate="
				+ issueDate.toString()
				+ ", lastDocumentStatus="
				+ lastDocumentStatus.toString()
				+ ", documentQuestion="
				+ (documentQuestion == null ? "Nulo" : documentQuestion.size())
				+ "]";
	}
		
}
