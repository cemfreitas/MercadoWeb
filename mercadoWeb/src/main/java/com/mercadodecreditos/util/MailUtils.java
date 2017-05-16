package com.mercadodecreditos.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.SimpleEmail;

import com.mercadodecreditos.model.Document;
import com.mercadodecreditos.model.DocumentFraction;
import com.mercadodecreditos.model.DocumentFractionQuestion;
import com.mercadodecreditos.model.DocumentQuestion;

public class MailUtils extends Properties {
	/**
	 * 
	 */
	private static final long serialVersionUID = -365118960346740126L;
	private String newLine = new String(new byte[] { 0x0D, 0x0A });
	private static String senderName, senderEmail, smtpHost, smtpUser,
			smtpPassword;
	private static int smtpPort;
	private static boolean smtpAuth;

	public void initializeMailParams() {
		InputStream inputFile;
		try {
			ClassLoader classLoader = getClass().getClassLoader();
			inputFile = new FileInputStream(classLoader.getResource(
					"mail.properties").getFile());
			this.load(inputFile);
			senderName = getProperty("mail.sender.name");
			smtpHost = getProperty("mail.smtp.host");
			smtpUser = getProperty("mail.smtp.user");
			smtpPassword = getProperty("mail.smtp.password");
			smtpPort = Integer.parseInt(getProperty("mail.smtp.port"));
			smtpAuth = getProperty("mail.smtp.auth").equals("true") ? true
					: false;
			senderEmail = getProperty("mail.sender.email");
			inputFile.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void sendEmail(String to, String subject, String message) {
		try {
			SimpleEmail email;

			email = new SimpleEmail();
			email.setHostName(smtpHost);
			email.setSmtpPort(smtpPort);
			if (smtpAuth) {
				email.setAuthenticator(new DefaultAuthenticator(smtpUser,
						smtpPassword));
			}
			email.setFrom(smtpUser, senderName);
			email.addTo(to);
			email.setSubject(subject);
			email.setMsg(message);
			email.setSentDate(new Date());

			email.send();
			System.out.println("Email enviado !");
		} catch (EmailException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void sendEmailNewDocSaved(Document document) {
		String message = "Voc� tem um novo cr�dito para aprovar :" + newLine
				+ "Tipo de cr�dito :"
				+ document.getDocumentType().getDescription() + newLine
				+ "N�mero do documento:" + document.getDocumentNumber()
				+ newLine + "N�mero gerado pelo site:"
				+ document.getGeneratedDocNumber() + newLine + "Usu�rio : "
				+ document.getUser().getName() + newLine + "Login : "
				+ document.getUser().getLogin() + newLine + "CPF : "
				+ document.getUser().getCPF() + newLine;
		if (document.getUser().getUserType().getXidUserType() == SystemDomain.userTypePessoaJuridica) {
			message += "CNPJ : " + document.getUser().getCNPJ();
		}

		String subject = "Novo cr�dito cadastrado para an�lise";

		sendEmail(senderEmail, subject, message);
	}

	public void sendEmailDocStatusChanged(Document document, String adminMessage) {
		String message = "Prezado usu�rio seu cr�dito teve o status alterado para "
				+ document.getLastDocumentStatus().getDescription()
				+ newLine
				+ adminMessage
				+ newLine
				+ "Tipo de cr�dito :"
				+ document.getDocumentType().getDescription()
				+ newLine
				+ "N�mero :"
				+ document.getGeneratedDocNumber()
				+ newLine
				+ "Usu�rio : "
				+ document.getUser().getName()
				+ newLine
				+ "Login : "
				+ document.getUser().getLogin()
				+ newLine
				+ "CPF : " + document.getUser().getCPF() + newLine;
		if (document.getUser().getUserType().getXidUserType() == SystemDomain.userTypePessoaJuridica) {
			message += "CNPJ : " + document.getUser().getCNPJ();
		}
		String subject = "Mercado de cr�ditos - Seu cr�dito teve o status alterado";

		sendEmail(document.getUser().getEmail(), subject, message);
	}

	public void sendEmailDealClosed(DocumentQuestion docmentSold, String email) {
		String message = "Prezado usu�rio, foi confirmado o fechamento do neg�cio."
				+ newLine
				+ "Segue abaixo os dados referente a sua compra:"
				+ newLine
				+ newLine
				+ "Cr�dito: "
				+ docmentSold.getDocument().getGeneratedDocNumber()
				+ newLine
				+ "Tipo: "
				+ docmentSold.getDocument().getDocumentType().getDescription()
				+ newLine
				+ "Data da compra: "
				+ getCurrentDate()
				+ newLine
				+ "Valor de face do cr�dito: R$ "
				+ formatCurrency(docmentSold.getDocument().getPrice())
				+ newLine
				+ "Imposto de renda incidente: R$ "
				+ formatCurrency(docmentSold.getDocument().getIncomeTaxValue())
				+ newLine
				+ "Valor l�quido a receber: R$ "
				+ formatCurrency(docmentSold.getDocument()
						.getNetValueToReceive())
				+ newLine
				+ "Valor do seu lance: R$ "
				+ formatCurrency(docmentSold.getBid()) + newLine;
		String subject = "Mercado de cr�ditos - Parab�ns, neg�cio fechado";
		sendEmail(email, subject, message);
	}

	public void sendEmailDealClosed(DocumentQuestion docmentSold,
			List<DocumentFraction> fracs, String email) {
		String message = "Prezado usu�rio, foi confirmado o fechamento do neg�cio."
				+ newLine
				+ "Segue abaixo os dados referente a sua compra:"
				+ newLine
				+ newLine
				+ "Cr�dito: "
				+ docmentSold.getDocument().getGeneratedDocNumber()
				+ newLine
				+ "Tipo: "
				+ docmentSold.getDocument().getDocumentType().getDescription()
				+ newLine
				+ "Data da compra: "
				+ this.getCurrentDate()
				+ newLine
				+ "Valor de face do cr�dito: "
				+ this.getFractions(fracs)
				+ newLine
				+ "Fra��es compradas: R$ "
				+ formatCurrency(docmentSold.getDocument().getPrice())
				+ newLine
				+ "Imposto de renda incidente: R$ "
				+ formatCurrency(docmentSold.getDocument().getIncomeTaxValue())
				+ newLine
				+ "Valor l�quido a receber: R$ "
				+ formatCurrency(docmentSold.getDocument()
						.getNetValueToReceive())
				+ newLine
				+ "Valor do seu lance: R$ "
				+ formatCurrency(docmentSold.getBid()) + newLine;
		String subject = "Mercado de cr�ditos - Parab�ns, neg�cio fechado";
		sendEmail(email, subject, message);
	}

	public void sendEmailRequestRaiseBid(DocumentQuestion messageBid,
			String email) {
		String message = "Prezado usu�rio, foi requisitado um aumento em um lance dado por voc�."
				+ newLine
				+ "Cr�dito: "
				+ messageBid.getDocument().getGeneratedDocNumber()
				+ newLine
				// + "Login do propriet�rio: "
				// + messageBid.getDocument().getUser().getLogin()
				// + newLine
				+ "Data do lance: "
				+ messageBid.getFormatedDate()
				+ newLine
				+ "Valor do lance: R$ "
				+ formatCurrency(messageBid.getBid())
				+ newLine;
		String subject = "Mercado de cr�ditos - Pedido de aumento de lance";
		sendEmail(email, subject, message);
	}

	public void sendEmailNewBidOffered(Document document, BigDecimal bid,
			String email) {
		String message = "Prezado usu�rio, foi oferecido um lance em um dos seus pap�is."
				+ newLine
				+ "Cr�dito: "
				+ document.getGeneratedDocNumber()
				+ newLine				
				+ "Valor de face do cr�dito: "
				+ formatCurrency(document.getPrice())
				+ newLine				
				+ "Data do lance: "
				+ getCurrentDate()
				+ newLine
				+ "Valor do lance: R$ " + formatCurrency(bid) + newLine;
		String subject = "Mercado de cr�ditos - Lance oferecido";
		sendEmail(email, subject, message);
	}

	public void sendEmailNewBidOffered(Document document, BigDecimal bid,
			DocumentFractionQuestion[] docFractionQuestionLst, String email) {
		String message = "Prezado usu�rio, foi oferecido um lance em um dos seus pap�is."
				+ newLine
				+ "Cr�dito: "
				+ document.getGeneratedDocNumber()
				+ newLine				
				+ "Fra��es ofertadas: "
				+ getFractions(docFractionQuestionLst)
				+ newLine
				+ "Data do lance: " 
				+ getCurrentDate()
				+ newLine
				+ "Valor do lance: R$ " + formatCurrency(bid) + newLine;
		String subject = "Mercado de cr�ditos - Lance oferecido";
		sendEmail(email, subject, message);
	}

	private String getCurrentDate() {
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		String formatedDate = sdf.format(new Date());

		return formatedDate;
	}

	private String getFractions(List<DocumentFraction> fracs) {
		String fracsStr = "";

		for (DocumentFraction fraction : fracs) {
			fracsStr += "R$ " + formatCurrency(fraction.getPrice()) + " ";
		}

		return fracsStr;
	}
	
	private String getFractions(DocumentFractionQuestion[] fracs) {
		String fracsStr = "";

		for (DocumentFractionQuestion fraction : fracs) {
			fracsStr += "R$ " + formatCurrency(fraction.getDocumentFraction().getPrice()) + " ";
		}

		return fracsStr;
	}

	private String formatCurrency(BigDecimal val) {
		String valStr = "";

		DecimalFormat formatter = new DecimalFormat("###,###,###.00");
		if (val == null) {
			valStr = "---";
		} else {
			valStr = formatter.format(val.doubleValue());
		}

		return valStr;
	}
}
