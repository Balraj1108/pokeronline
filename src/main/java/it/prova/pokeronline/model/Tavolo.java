package it.prova.pokeronline.model;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "tavolo")
public class Tavolo {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;
	@Column(name = "esperienzaminima")
	private Integer esperienzaMinima;
	@Column(name = "ciframinima")
	private Integer cifraMinima;
	@Column(name = "denominazione")
	private String denominazione;
	@Column(name = "datacreazione")
	private LocalDateTime dataCreazione;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "tavolo")
	private Set<Utente> giocatori = new HashSet<Utente>(0);

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "utentecreazione_id", referencedColumnName = "id", nullable = false)
	private Utente utenteCreazione;

	public Tavolo() {
		super();
	}

	public Tavolo(Long id, Integer esperienzaMinima, Integer cifraMinima, String denominazione, LocalDateTime dataCreazione,
			Set<Utente> giocatori, Utente utenteCreazione) {
		super();
		this.id = id;
		this.esperienzaMinima = esperienzaMinima;
		this.cifraMinima = cifraMinima;
		this.denominazione = denominazione;
		this.dataCreazione = dataCreazione;
		this.giocatori = giocatori;
		this.utenteCreazione = utenteCreazione;
	}

	public Tavolo(Long id, Integer esperienzaMinima, Integer cifraMinima, String denominazione, LocalDateTime dataCreazione) {
		super();
		this.id = id;
		this.esperienzaMinima = esperienzaMinima;
		this.cifraMinima = cifraMinima;
		this.denominazione = denominazione;
		this.dataCreazione = dataCreazione;
	}
	

	public Tavolo(String denominazione, Integer esperienzaMinima, Integer cifraMinima) {
		super();
		this.esperienzaMinima = esperienzaMinima;
		this.cifraMinima = cifraMinima;
		this.denominazione = denominazione;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getEsperienzaMinima() {
		return esperienzaMinima;
	}

	public void setEsperienzaMinima(Integer esperienzaMinima) {
		this.esperienzaMinima = esperienzaMinima;
	}

	public Integer getCifraMinima() {
		return cifraMinima;
	}

	public void setCifraMinima(Integer cifraMinima) {
		this.cifraMinima = cifraMinima;
	}

	public String getDenominazione() {
		return denominazione;
	}

	public void setDenominazione(String denominazione) {
		this.denominazione = denominazione;
	}

	public LocalDateTime getDataCreazione() {
		return dataCreazione;
	}

	public void setDataCreazione(LocalDateTime dataCreazione) {
		this.dataCreazione = dataCreazione;
	}

	public Set<Utente> getGiocatori() {
		return giocatori;
	}

	public void setGiocatori(Set<Utente> giocatori) {
		this.giocatori = giocatori;
	}

	public Utente getUtenteCreazione() {
		return utenteCreazione;
	}

	public void setUtenteCreazione(Utente utenteCreazione) {
		this.utenteCreazione = utenteCreazione;
	}

}
