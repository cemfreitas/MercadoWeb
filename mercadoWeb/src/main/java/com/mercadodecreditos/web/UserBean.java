package com.mercadodecreditos.web;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;

import org.primefaces.context.RequestContext;
import org.springframework.util.DigestUtils;

import com.mercadodecreditos.controller.StateCityController;
import com.mercadodecreditos.controller.UserController;
import com.mercadodecreditos.controller.UserTypeController;
import com.mercadodecreditos.model.City;
import com.mercadodecreditos.model.State;
import com.mercadodecreditos.model.User;
import com.mercadodecreditos.model.UserType;
import com.mercadodecreditos.util.ApplicationException;
import com.mercadodecreditos.util.CNPJValidator;
import com.mercadodecreditos.util.CPFValidator;
import com.mercadodecreditos.util.DDDValidList;
import com.mercadodecreditos.util.DateUtils;
import com.mercadodecreditos.util.SystemDomain;

@ManagedBean(name = "userBean")
@SessionScoped
public class UserBean implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6266418526981252089L;
	private User user;
	private int idState, idCity, idUserType;
	private String passwordConfirmed, birthDate, password, labelName;
	private List<SelectItem> userTypes, states, cities;

	@ManagedProperty(value = "#{contextBean}")
	private ContextBean contextBean;

	public String newUser() {
		this.user = new User();
		this.idCity = 0;
		this.idState = 0;
		this.idUserType = SystemDomain.userTypePessoaFisica;
		this.labelName = "Nome:";
		states = null;
		cities = null;
		passwordConfirmed = "";
		password = "";
		birthDate = "";
		return "/public/userform";
	}

	public String alterUser() {
		SimpleDateFormat fmt = new SimpleDateFormat("dd/MM/yyyy");
		this.user = contextBean.getLoggedUser();
		this.idCity = user.getCity().getXidCity();
		this.idState = user.getState().getXidState();
		this.idUserType = user.getUserType().getXidUserType();
		fillCities(user.getState().getXidState());
		passwordConfirmed = "";
		password = "";
		birthDate = fmt.format(user.getBirthDate());
		return "/restricted/alteruser";
	}

	public String save(String mode) {
		try {
			validateFields(mode);

			UserController userController = new UserController();
			getStateAndCityForSave();
			getUserTypeForSave();
			if (mode.equals("insert")
					|| (mode.equals("alter") && !password.equals(""))) {
				this.user.setPassword(DigestUtils.md5DigestAsHex(this.password
						.getBytes()));
			}
			if (mode.equals("alter")) {
				userController.updateUser(this.user);
				return "/public/principal";
			} else {
				userController.saveUser(this.user);
				return "/public/welcome";
			}
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

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getPasswordConfirmed() {
		return passwordConfirmed;
	}

	public void setPasswordConfirmed(String passwordConfirmed) {
		this.passwordConfirmed = passwordConfirmed;
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

	public String getBithDate() {
		return birthDate;
	}

	public void setBithDate(String bithDate) {
		this.birthDate = bithDate;
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

	public List<SelectItem> getUserTypes() {
		List<UserType> list = null;
		UserTypeController userTypeController = new UserTypeController();
		list = userTypeController.getAll();
		if (this.userTypes == null) {
			this.userTypes = new ArrayList<SelectItem>();
			for (UserType userType : list) {
				this.userTypes.add(new SelectItem(userType.getXidUserType(),
						userType.getDescription()));
			}
		}
		return userTypes;
	}

	public List<SelectItem> getStates() {
		List<State> list = null;
		StateCityController stateCityController = new StateCityController();
		list = stateCityController.getAllStates();
		// if (this.states == null) {
		this.states = new ArrayList<SelectItem>();
		// this.states.add(new SelectItem(0,"Selecione um estado"));
		for (State state : list) {
			this.states.add(new SelectItem(state.getXidState(), state
					.getDescription()));
		}
		// }
		return states;
	}

	public List<SelectItem> getCities() {
		return cities;
	}

	private void fillCities(int state) {
		List<City> list = null;
		StateCityController stateCityController = new StateCityController();
		list = stateCityController.getCitiesByState(state);

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

	public boolean isPessoaFisica() {
		if (this.idUserType == SystemDomain.userTypePessoaJuridica) {
			user.setCNPJ(null);
			this.labelName = "Nome do representante da empresa:";
			return false;
		} else {
			this.labelName = "Nome:";
			return true;
		}

	}

	public ContextBean getContextBean() {
		return contextBean;
	}

	public void setContextBean(ContextBean contextBean) {
		this.contextBean = contextBean;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	private void validateFields(String mode) throws ApplicationException {

		if (user.getName().trim().equals("")) {
			throw new ApplicationException("Nome não pode ser vazio");
		}

		if (user.getLogin().trim().equals("")) {
			throw new ApplicationException("Login não pode ser vazio");
		}

		if (mode.equals("insert")
				|| (mode.equals("alter") && !password.trim().equals(""))) {
			String PASSWORD_PATTERN = "((?=.*\\d)(?=.*[A-Z]).{6,20})";

			Pattern pattern = Pattern.compile(PASSWORD_PATTERN);
			if (!pattern.matcher(password.trim()).matches()) {
				throw new ApplicationException(
						"A senha deve ter pelo menos uma letra maiúscula, um número e de 6 a 20 caracteres");
			}
		}

		if (!password.trim().equals(passwordConfirmed.trim())) {
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
}
