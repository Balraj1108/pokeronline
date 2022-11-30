package it.prova.pokeronline.web.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import it.prova.pokeronline.dto.TavoloDTO;
import it.prova.pokeronline.dto.UtenteDTO;
import it.prova.pokeronline.model.Utente;
import it.prova.pokeronline.service.TavoloService;
import it.prova.pokeronline.service.UtenteService;

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
	
	
}
