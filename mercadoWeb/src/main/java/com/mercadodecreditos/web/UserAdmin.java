package com.mercadodecreditos.web;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;

import com.mercadodecreditos.util.DateUtils;

import org.primefaces.context.RequestContext;
import org.springframework.util.DigestUtils;

import com.mercadodecreditos.controller.SituationController;
import com.mercadodecreditos.controller.StateCityController;
import com.mercadodecreditos.controller.UserController;
import com.mercadodecreditos.controller.UserTypeController;
import com.mercadodecreditos.model.City;
import com.mercadodecreditos.model.Situation;
import com.mercadodecreditos.model.State;
import com.mercadodecreditos.model.User;
import com.mercadodecreditos.model.UserType;
import com.mercadodecreditos.util.ApplicationException;
import com.mercadodecreditos.util.CNPJValidator;
import com.mercadodecreditos.util.CPFValidator;
import com.mercadodecreditos.util.DDDValidList;
import com.mercadodecreditos.util.SystemDomain;

@ManagedBean(name = "userAdminBean")
@SessionScoped
public class UserAdmin implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7467896651681273111L;
	private User user;
	private String login, passwordConfirmed, email, cpf, cnpj, birthDate,
			explanation, password, labelName;
	private List<SelectItem> cities, statusList;
	private int idState, idCity, idUserType, statusChanged;
	private boolean showUserPanel, showUserNotFound;

	public String userAdmin() {
		user = null;
		login = "";
		email = "";
		cpf = "";
		cnpj = "";
		showUserPanel = false;
		showUserNotFound = false;
		password = "";
		passwordConfirmed = "";
		if (idUserType == SystemDomain.userTypePessoaFisica) {
			labelName = "Nome do representante da empresa:";
		} else {
			labelName = "Nome:";
		}
		return "/admin/useradmin";
	}

	public String getIsAdmin() {
		if (user == null) {
			return "";
		}
		if (user.isAdmin()) {
			return "SIM";
		} else {
			return "NÃO";
		}
	}

	public String getPassword() {		
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getExplanation() {
		return explanation;
	}

	public void setExplanation(String explanation) {
		this.explanation = explanation;
	}

	public int getStatusChanged() {
		return statusChanged;
	}

	public void setStatusChanged(int statusChanged) {
		this.statusChanged = statusChanged;
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

	public int getIdUserType() {
		return idUserType;
	}

	public void setIdUserType(int idUserType) {
		this.idUserType = idUserType;
	}

	public List<SelectItem> getCities() {
		return cities;
	}

	public void setCities(List<SelectItem> cities) {
		this.cities = cities;
	}

	public String getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(String birthDate) {
		this.birthDate = birthDate;
	}

	public String getPasswordConfirmed() {
		return passwordConfirmed;
	}

	public void setPasswordConfirmed(String passwordConfirmed) {
		this.passwordConfirmed = passwordConfirmed;
	}

	public boolean isShowUserPanel() {
		return showUserPanel;
	}

	public boolean isShowUserNotFound() {
		return showUserNotFound;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getCpf() {
		return cpf;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
	}

	public void searchUser() {
		if (login.equals("") && email.equals("") && cpf.equals("")
				&& cnpj.equals("")) {
			FacesContext context = FacesContext.getCurrentInstance();
			FacesMessage facesMessage = new FacesMessage(
					FacesMessage.SEVERITY_ERROR, "",
					"Forneça um dos campos de busca");
			context.addMessage(null, facesMessage);
			RequestContext.getCurrentInstance()
					.execute("window.scrollTo(0,0);");
			return;
		}
		UserController userController = new UserController();

		this.user = userController.searchUserAdmin(login, email, cpf, cnpj);

		if (this.user == null) {
			showUserPanel = false;
			showUserNotFound = true;
		} else {
			showUserPanel = true;
			showUserNotFound = false;
		}

	}

	public String alterUser() {
		SimpleDateFormat fmt = new SimpleDateFormat("dd/MM/yyyy");
		birthDate = fmt.format(user.getBirthDate());
		fillCities(user.getState().getXidState());
		idUserType = user.getUserType().getXidUserType();
		idState = user.getState().getXidState();
		idCity = user.getCity().getXidCity();		
		return "/admin/alteruseradmin";
	}

	public void changeAdminStatus(boolean admin) {
		UserController userController = new UserController();
		user.setAdmin(admin);
		try {
			userController.updateUser(user);
		} catch (ApplicationException e) {
			FacesContext context = FacesContext.getCurrentInstance();
			FacesMessage facesMessage = new FacesMessage(
					FacesMessage.SEVERITY_ERROR, "", e.getMessage());
			context.addMessage(null, facesMessage);
			RequestContext.getCurrentInstance()
					.execute("window.scrollTo(0,0);");
		}
	}

	public void changeUserStatus() {
		Map<String, Object> options = new HashMap<String, Object>();
		options.put("modal", true);
		options.put("resizable", false);
		options.put("draggable", false);
		options.put("contentHeight", 260);
		options.put("contentWidth", 425);
		options.put("closable", false);
		statusList = null;
		statusChanged = 0;
		explanation = "";
		RequestContext.getCurrentInstance().openDialog(
				"/admin/changeuserstatus", options, null);
	}

	public void fillCities(int id) {

		List<City> list = null;
		StateCityController stateCityController = new StateCityController();
		list = stateCityController.getCitiesByState(id);

		this.cities = new ArrayList<SelectItem>();
		// this.cities.add(new SelectItem(0,"Selecione uma cidade"));
		for (City city : list) {
			this.cities.add(new SelectItem(city.getXidCity(), city
					.getDescription()));
		}
	}

	public void fillCities(ValueChangeEvent e) {
		if (e.getNewValue() == null) {
			return;
		}

		List<City> list = null;
		StateCityController stateCityController = new StateCityController();
		list = stateCityController.getCitiesByState((int) e.getNewValue());

		this.cities = new ArrayList<SelectItem>();
		// this.cities.add(new SelectItem(0,"Selecione uma cidade"));
		for (City city : list) {
			this.cities.add(new SelectItem(city.getXidCity(), city
					.getDescription()));
		}
	}

	public String save() {
		try {
			validateFields();
			UserController userController = new UserController();
			getStateAndCityForSave();
			getUserTypeForSave();
			if (!password.equals("")) {
				this.user.setPassword(DigestUtils.md5DigestAsHex(this.password
						.getBytes()));
			}
			userController.updateUser(this.user);
			return "/public/principal";
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

	private void getStateAndCityForSave() {
		StateCityController cityState = new StateCityController();
		City city = cityState.getCityById(this.idCity);
		State state = cityState.getStateById(this.idState);
		this.user.setCity(city);
		this.user.setState(state);
	}

	private void getUserTypeForSave() {
		UserTypeController userTypeController = new UserTypeController();
		UserType userType = userTypeController.getUserTypeById(this.idUserType);
		this.user.setUserType(userType);
	}

	public boolean isPessoaFisica() {
		if (this.idUserType == SystemDomain.userTypePessoaFisica) {
			user.setCNPJ(null);
			this.labelName = "Nome do representante da empresa:";
			return true;
		} else {
			this.labelName = "Nome:";
			return false;
		}

	}

	public List<SelectItem> getStatusList() {
		List<Situation> list = null;
		if (this.statusList == null) {
			SituationController situationController = new SituationController();
			list = situationController.getAll();
			this.statusList = new ArrayList<SelectItem>();
			for (Situation situation : list) {
				if (user == null || !user.getLastSituation().equals(situation)) {
					this.statusList.add(new SelectItem(situation
							.getXidSituation(), situation.getDescription()));
				}
			}
		}
		return statusList;
	}

	public void changeStatus() {
		UserController userController = new UserController();
		userController.changeUserStatus(user, statusChanged, explanation);

		RequestContext.getCurrentInstance().closeDialog(null);
	}

	public void cancelChangeUserStatus() {
		RequestContext.getCurrentInstance().closeDialog(null);
	}

	private void validateFields() throws ApplicationException {

		if (user.getName().trim().equals("")) {
			throw new ApplicationException("Nome não pode ser vazio");
		}

		if (user.getLogin().trim().equals("")) {
			throw new ApplicationException("Login não pode ser vazio");
		}

		if (!password.trim().equals("")) {
			String PASSWORD_PATTERN = "((?=.*\\d)(?=.*[A-Z]).{6,20})";

			Pattern pattern = Pattern.compile(PASSWORD_PATTERN);
			if (!pattern.matcher(password.trim()).matches()) {
				throw new ApplicationException(
						"A senha deve ter pelo menos uma letra maiúscula, um número e de 6 a 20 caracteres.");
			}
		}

		if (!password.trim().equals(passwordConfirmed)) {			
			throw new ApplicationException("Senha confirmada não confere");
		}

		if (user.getEmail().trim().equals("")) {
			throw new ApplicationException("Email é obrigatório");
		} else {
			String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
					+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
			Pattern pattern = Pattern.compile(EMAIL_PATTERN);
			if (!pattern.matcher(user.getEmail()).matches()) {
				throw new ApplicationException("Email inválido");
			}
		}

		if (user.getAddress().trim().equals("")) {
			throw new ApplicationException("Logradouro é obrigatório");
		}
		
		if (user.getAddressNumber().trim().equals("")) {
			throw new ApplicationException("Número do logradouro é obrigatório");
		}

		if (this.idState <= 0) {
			throw new ApplicationException("Escolha um estado");
		}

		if (this.idCity <= 0) {
			throw new ApplicationException("Escolha um município");
		}

		if (user.getZipCode().trim().equals("")) {
			throw new ApplicationException("CEP é obrigatório");
		}

		if (this.birthDate.trim().equals("")) {
			throw new ApplicationException("Data de nascimento é obrigatória");
		} else {
			SimpleDateFormat fmt = new SimpleDateFormat("dd/MM/yyyy");
			try {
				fmt.setLenient(false);
				user.setBirthDate(fmt.parse(this.birthDate));
			} catch (ParseException e) {
				throw new ApplicationException("Data de nascimento inválida");
			}
			if (user.getBirthDate().compareTo(new Date()) > 0) {
				throw new ApplicationException("Data de nascimento inválida");
			}
			if (DateUtils.getDiffYears(user.getBirthDate(), new Date()) < 18) {
				throw new ApplicationException("Usuário menor que 18 anos");
			}
		}

		if (user.getResPhone().trim().equals("")) {
			throw new ApplicationException("Telefone residencial é obrigatório");
		} else {
			List<String> ddds = DDDValidList.getValidDDDs();

			if (!ddds.contains(user.getResPhone().substring(1, 3))) {
				throw new ApplicationException(
						"Tel. residencial com DDD inválido");
			}

			if ((user.getCommPhone() != null)
					&& (!user.getCommPhone().trim().equals(""))) {
				if (!ddds.contains(user.getCommPhone().substring(1, 3))) {
					throw new ApplicationException(
							"Tel. comercial com DDD inválido");
				}
			}

			if ((user.getCelPhone() != null)
					&& (!user.getCelPhone().trim().equals(""))) {
				if (!ddds.contains(user.getCelPhone().substring(1, 3))) {
					throw new ApplicationException(
							"Tel. celular com DDD inválido");
				}
			}
		}

		if (user.getCPF().trim().equals("")) {
			throw new ApplicationException("CPF é obrigatório");
		} else {
			if (!CPFValidator.isValid(user.getCPF())) {
				throw new ApplicationException("CPF inválido");
			}
		}

		if ((idUserType == SystemDomain.userTypePessoaJuridica)
				&& (user.getCNPJ().trim().equals(""))) {
			throw new ApplicationException(
					"CNPJ é obrigatório para pessoa jurídica");
		} else {
			if ((user.getCNPJ() != null) && (!user.getCNPJ().trim().equals(""))) {
				if (!CNPJValidator.isValid(user.getCNPJ())) {
					throw new ApplicationException("CNPJ inválido");
				}
			}
		}
	}

	public String getLabelName() {
		return labelName;
	}

	public void setLabelName(String labelName) {
		this.labelName = labelName;
	}

	public String getCnpj() {
		return cnpj;
	}

	public void setCnpj(String cnpj) {
		this.cnpj = cnpj;
	}
}
