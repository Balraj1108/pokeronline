package it.prova.pokeronline.dto;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import it.prova.pokeronline.model.Ruolo;
import it.prova.pokeronline.model.Utente;

public class UtenteDTOAggiornamento {

	private Long id;

	@NotBlank(message = "{username.notblank}")
	@Size(min = 3, max = 15, message = "Il valore inserito '${validatedValue}' deve essere lungo tra {min} e {max} caratteri")
	private String username;

	private String confermaPassword;

	@NotBlank(message = "{nome.notblank}")
	private String nome;

	@NotBlank(message = "{cognome.notblank}")
	private String cognome;

	private Long[] ruoliIds;
	
	//private Set<Ruolo> ruoli = new HashSet<>(0);
	
	

	public UtenteDTOAggiornamento() {
	}

	public UtenteDTOAggiornamento(Long id, String username, String nome, String cognome) {
		super();
		this.id = id;
		this.username = username;
		this.nome = nome;
		this.cognome = cognome;
	}
	
	

	public UtenteDTOAggiornamento(
		  String username,
			 String nome,
			 String cognome) {
		super();
		
		this.username = username;
		this.nome = nome;
		this.cognome = cognome;
		
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}


	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getCognome() {
		return cognome;
	}

	public void setCognome(String cognome) {
		this.cognome = cognome;
	}


	public String getConfermaPassword() {
		return confermaPassword;
	}

	public void setConfermaPassword(String confermaPassword) {
		this.confermaPassword = confermaPassword;
	}


	public Long[] getRuoliIds() {
		return ruoliIds;
	}

	public void setRuoliIds(Long[] ruoliIds) {
		this.ruoliIds = ruoliIds;
	}
	
	
	/*
	public Set<Ruolo> getRuoli() {
		return ruoli;
	}

	public void setRuoli(Set<Ruolo> ruoli) {
		this.ruoli = ruoli;
	}*/

	public Utente buildUtenteModel(boolean includeIdRoles) {
		Utente result = new Utente(this.id, this.username, this.nome, this.cognome);
		
		if (includeIdRoles && ruoliIds != null)
			result.setRuoli(Arrays.asList(ruoliIds).stream().map(id -> new Ruolo(id)).collect(Collectors.toSet()));

		return result;
	}

	
	// niente password...
	public static UtenteDTOAggiornamento buildUtenteDTOFromModel(Utente utenteModel) {
		UtenteDTOAggiornamento result = new UtenteDTOAggiornamento(utenteModel.getId(), utenteModel.getUsername(), utenteModel.getNome(),
				utenteModel.getCognome());

		if (!utenteModel.getRuoli().isEmpty())
			result.ruoliIds = utenteModel.getRuoli().stream().map(r -> r.getId()).collect(Collectors.toList())
					.toArray(new Long[] {});
		
		

		return result;
	}
	
	
	public static List<UtenteDTOAggiornamento> createUtenteDTOListFromModelList(List<Utente> modelListInput) {
		return modelListInput.stream().map(registaEntity -> {
			UtenteDTOAggiornamento result = UtenteDTOAggiornamento.buildUtenteDTOFromModel(registaEntity);
			
			return result;
		}).collect(Collectors.toList());
	}
	
	
}
