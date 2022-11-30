package it.prova.pokeronline.web.api;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;


import it.prova.pokeronline.dto.TavoloDTO;
import it.prova.pokeronline.dto.UtenteDTO;
import it.prova.pokeronline.model.Tavolo;
import it.prova.pokeronline.model.Utente;
import it.prova.pokeronline.service.TavoloService;
import it.prova.pokeronline.service.UtenteService;
import it.prova.pokeronline.web.api.exception.IdNotNullForInsertException;
import it.prova.pokeronline.web.api.exception.PermessoNegatoExceptio;
import it.prova.pokeronline.web.api.exception.TavoloNotFoundException;

@RestController
@RequestMapping("api/tavolo")
public class TavoloController {

	@Autowired
	private TavoloService tavoloService;
	
	@Autowired
	private UtenteService utenteService;
	
	@GetMapping
	public List<TavoloDTO> getAllby() {
		
		return TavoloDTO.createTavoloDTOFromModelList(tavoloService.FindByUsername(), true);
	}
	
	
	// gli errori di validazione vengono mostrati con 400 Bad Request ma
		// elencandoli grazie al ControllerAdvice
	
		
		@PostMapping
		@ResponseStatus(HttpStatus.CREATED)
		public TavoloDTO createNew(@Valid @RequestBody TavoloDTO agendaInput) {
			// se mi viene inviato un id jpa lo interpreta come update ed a me (producer)
			// non sta bene
			if (agendaInput.getId() != null)
				throw new IdNotNullForInsertException("Non è ammesso fornire un id per la creazione");

			String username = SecurityContextHolder.getContext().getAuthentication().getName();
			
			agendaInput.setUtenteCreazione(UtenteDTO.buildUtenteDTOFromModel(utenteService.findByUsername(username)));
			Tavolo agendaIns = tavoloService.inserisciNuovo(agendaInput.buildTavoloModel());
			
			
			return TavoloDTO.buildTavoloDTOFromModel(agendaIns, true);
		}

		@GetMapping("/{id}")
		public TavoloDTO findById(@PathVariable(value = "id", required = true) long id) {
			String username = SecurityContextHolder.getContext().getAuthentication().getName();
			Utente utenteLoggato = utenteService.findByUsername(username);
			
			Tavolo agenda = tavoloService.caricaSingoloElementoEager(id);

			if (utenteLoggato.getRuoli().stream().filter(r -> r.getCodice().equals("ROLE_ADMIN")).findAny().
					orElse(null) != null || utenteLoggato.getId() == agenda.getUtenteCreazione().getId()) {
				if (agenda == null)
					throw new TavoloNotFoundException("Tavolo not found con id: " + id);

				return TavoloDTO.buildTavoloDTOFromModel(agenda, true);
			} else {
				throw new PermessoNegatoExceptio("non hai i permessi per visualizzare");
			}
			
		}
		
		@PutMapping("/{id}")
	    public TavoloDTO update(@Valid @RequestBody TavoloDTO agendaInput, @PathVariable(required = true) Long id) {
	        String username = SecurityContextHolder.getContext().getAuthentication().getName();
	        Utente utenteLoggato = utenteService.findByUsername(username);

	        Tavolo agenda = tavoloService.caricaSingoloElemento(id);

	        if (agenda == null)
	            throw new TavoloNotFoundException("Agenda not found con id: " + id);

	        if (utenteLoggato.getRuoli().stream().filter(r -> r.getCodice().equals("ROLE_ADMIN")).findAny()
	                .orElse(null) != null || utenteLoggato.getId() == agenda.getUtenteCreazione().getId()) {

	            agendaInput.setId(id);
	            agendaInput.setUtenteCreazione(UtenteDTO.buildUtenteDTOFromModel(utenteService.findByUsername(username)));
	            Tavolo agendaAggiornata = tavoloService.aggiorna(agendaInput.buildTavoloModel());
	            return TavoloDTO.buildTavoloDTOFromModel(agendaAggiornata, true);

	        } else {
	            throw new PermessoNegatoExceptio("Non hai i permessi per modificare questo elemento!");
	        }
	    }
		
		@DeleteMapping("/{id}")
	    @ResponseStatus(HttpStatus.NO_CONTENT)
	    public void delete(@PathVariable(required = true) Long id) {

	        Tavolo agenda = tavoloService.caricaSingoloElemento(id);
	        
	        if (agenda == null)
	            throw new TavoloNotFoundException("Agenda not found con id: " + id);
	        
	        String username = SecurityContextHolder.getContext().getAuthentication().getName();
	        Utente utenteLoggato = utenteService.findByUsername(username);
	        
	        

	        if (utenteLoggato.getRuoli().stream().filter(r -> r.getCodice().equals("ROLE_ADMIN")).findAny()
	                .orElse(null) != null || utenteLoggato.getId() == agenda.getUtenteCreazione().getId()) {

	        	tavoloService.rimuovi(id);

	        } else {
	            throw new PermessoNegatoExceptio("Non hai i permessi per Eliminare questo elemento!");
	        }


	    }
	
	
}
