package it.prova.pokeronline.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.prova.pokeronline.model.Tavolo;
import it.prova.pokeronline.repository.tavolo.TavoloRepository;
import it.prova.pokeronline.web.api.exception.TavoloNotFoundException;

@Service
@Transactional(readOnly = true)
public class TavoloServiceImpl implements TavoloService {

	@Autowired
	private TavoloRepository repository;
	
	public List<Tavolo> listAllElements(boolean eager) {
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		System.out.println(username);
		if (eager)
			return (List<Tavolo>) repository.findAllAgendaEager();

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
		return repository.save(filmInstance);
	}

	@Transactional
	public void rimuovi(Long idToRemove) {
		repository.findById(idToRemove)
				.orElseThrow(() -> new TavoloNotFoundException("Tavolo not found con id: " + idToRemove));
		repository.deleteById(idToRemove);
	}
	
}
