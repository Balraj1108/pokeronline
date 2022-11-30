package it.prova.pokeronline.web.api;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import it.prova.pokeronline.dto.TavoloDTO;
import it.prova.pokeronline.dto.UtenteDTO;
import it.prova.pokeronline.model.Tavolo;
import it.prova.pokeronline.model.Utente;
import it.prova.pokeronline.service.TavoloService;
import it.prova.pokeronline.service.UtenteService;
import it.prova.pokeronline.web.api.exception.TavoloNotFoundException;

@RestController
@RequestMapping("api/game")
public class GameController {

	@Autowired
	private UtenteService utenteService;
	@Autowired
	private TavoloService tavoloService;
	
	@GetMapping("/credito{credito}")
	public UtenteDTO listaUtenteById(@PathVariable(value = "credito", required = true) Integer credito) {
		
		
		
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		Utente utenteLoggato = utenteService.findByUsername(username);
		
		Integer nuovoCredito = utenteLoggato.getCreditoAccumulato() + credito;
		
		utenteLoggato.setCreditoAccumulato(nuovoCredito);
		utenteService.aggiorna(utenteLoggato);
		
		return UtenteDTO.buildUtenteDTOFromModel(utenteLoggato);
	}
	
	@GetMapping
	public List<TavoloDTO> getAllTavoliByEsperienza() {
		
		return TavoloDTO.createTavoloDTOFromModelList(tavoloService.findByEsperienzaMinimaLessThan(), true);
	}
	
	@GetMapping("/tavolo{id}")
	public TavoloDTO inserireGiocatoreInTavolo(@PathVariable(value = "id", required = true) Long id) {
		
		
		
		/*String username = SecurityContextHolder.getContext().getAuthentication().getName();
		Utente utenteLoggato = utenteService.findByUsername(username);
		//Set<Utente> giocatoriDaAggiungere = new HashSet<>();
		//giocatoriDaAggiungere.add(utenteLoggato);
		
		
		Tavolo tavoloInInput  = tavoloService.caricaSingoloElementoEager(id);
		Set<Utente> giocatoriDaAggiungere = tavoloInInput.getGiocatori();
		giocatoriDaAggiungere.add(utenteLoggato);
		tavoloInInput.setGiocatori(giocatoriDaAggiungere);
		tavoloService.aggiorna(tavoloInInput);*/
		
		
		return TavoloDTO.buildTavoloDTOFromModel(tavoloService.addGiocatoreTavolo(id), true);
	}
	
	@GetMapping("/lastGame")
	public TavoloDTO ritornaUltimoTavolo() {
		
		if (tavoloService.findLastGame() == null) {
			throw new TavoloNotFoundException(" Non stai giocando a nessun tavolo");
		}
		return TavoloDTO.buildTavoloDTOFromModel(tavoloService.findLastGame(), true);
	}
	
	@GetMapping("/exitGame")
	@ResponseStatus(HttpStatus.OK)
	public void abbandondaTavoloACuiStavoGiocando() {
	
		if (tavoloService.findLastGame() == null) {
			throw new TavoloNotFoundException(" Non stai giocando a nessun tavolo");
		}
	
		tavoloService.exitGame();
		
		
	}
	
	@GetMapping("/exitGameNuovo")
	@ResponseStatus(HttpStatus.OK)
	public void abbandondaTavoloACuiStavoGiocao() {
	
		if (tavoloService.findLastGame() == null) {
			throw new TavoloNotFoundException(" Non stai giocando a nessun tavolo");
		}
	
		tavoloService.eliminaRecord();
		
		
	}

	
	
	
	
	
}
