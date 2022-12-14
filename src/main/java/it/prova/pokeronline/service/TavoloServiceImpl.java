package it.prova.pokeronline.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.prova.pokeronline.dto.UtenteDTO;
import it.prova.pokeronline.model.Tavolo;
import it.prova.pokeronline.model.Utente;
import it.prova.pokeronline.repository.tavolo.TavoloRepository;
import it.prova.pokeronline.web.api.exception.TavoloNotFoundException;

@Service
@Transactional(readOnly = true)
public class TavoloServiceImpl implements TavoloService {

	@Autowired
	private TavoloRepository repository;
	@Autowired
	private UtenteService utenteService;
	
	public List<Tavolo> listAllElements(boolean eager) {
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		System.out.println(username);
		if (eager)
			return (List<Tavolo>) repository.findAllTavoloEager();

		return (List<Tavolo>) repository.findAll();
	}

	public Tavolo caricaSingoloElemento(Long id) {
		return repository.findById(id).orElse(null);
	}

	public Tavolo caricaSingoloElementoEager(Long id) {
		return repository.findSingleTavoloEager(id);
	}

	@Transactional
	public Tavolo aggiorna(Tavolo filmInstance) {
		return repository.save(filmInstance);
	}

	@Transactional
	public Tavolo inserisciNuovo(Tavolo filmInstance) {
		String usernameInSessione = SecurityContextHolder.getContext().getAuthentication().getName();
		
		filmInstance.setUtenteCreazione(utenteService.findByUsername(usernameInSessione));
		filmInstance.setDataCreazione(LocalDateTime.now());
		return repository.save(filmInstance);
	}

	@Transactional
	public void rimuovi(Long idToRemove) {
		repository.findById(idToRemove)
				.orElseThrow(() -> new TavoloNotFoundException("Tavolo not found con id: " + idToRemove));
		repository.deleteById(idToRemove);
	}

	@Override
	public List<Tavolo> FindByUsername() {
		String usernameInSessione = SecurityContextHolder.getContext().getAuthentication().getName();
		
		return repository.FindByUsername(usernameInSessione);
	}

	@Override
	public List<Tavolo> findByEsperienzaMinimaLessThan() {
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		Utente utenteLoggato = utenteService.findByUsername(username);
		Integer esperienzaUtente = utenteLoggato.getEsperienzaAccumulata();
		
		return repository.findByEsperienzaMinimaLessThan(esperienzaUtente);
	}

	@Override
	@Transactional
	public Tavolo findLastGame() {
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		Utente utenteLoggato = utenteService.findByUsername(username);
		Long idUtente = utenteLoggato.getId();
		Tavolo tavoloACuiSto = repository.findByGiocatoriId(idUtente);
		System.out.println(tavoloACuiSto.getGiocatori().size());
		
		return repository.findByGiocatoriId(idUtente);
		
	}

	@Override
	@Transactional
	public void exitGame() {
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		Utente utenteLoggato = utenteService.findByUsername(username);
		Long idUtente = utenteLoggato.getId();
		
		Tavolo tavoloACuiSto = repository.findByGiocatoriId(idUtente);
		Long idTavolo = tavoloACuiSto.getId();
		Tavolo tavolo = repository.findSingleTavoloEager(idTavolo);
		
		
		tavolo.getGiocatori().remove(utenteLoggato);
		
		repository.save(tavolo);
		

		
		
		
	}

	@Override
	@Transactional
	public Tavolo addGiocatoreTavolo(Long id) {
		
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		Utente utenteLoggato = utenteService.findByUsername(username);
		//Set<Utente> giocatoriDaAggiungere = new HashSet<>();
		//giocatoriDaAggiungere.add(utenteLoggato);
		
		
		Tavolo tavoloInInput  = repository.findSingleTavoloEager(id);
		tavoloInInput.getGiocatori().add(utenteLoggato);
		//tavoloService.aggiorna(tavoloInInput);
		
		return repository.save(tavoloInInput);
	}

	@Override
	@Transactional
	public void eliminaRecord() {
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		Utente utenteLoggato = utenteService.findByUsername(username);
		Long idDa = utenteLoggato.getId();
		
		//repository.eliminaRecord(idDa);
	}
	
}
