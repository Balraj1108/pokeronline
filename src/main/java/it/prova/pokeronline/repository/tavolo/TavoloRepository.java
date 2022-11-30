package it.prova.pokeronline.repository.tavolo;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import it.prova.pokeronline.model.Tavolo;


public interface TavoloRepository extends CrudRepository<Tavolo, Long> {


	@Query("from Tavolo t join fetch t.utenteCreazione where t.id = ?1")
	Tavolo findSingleTavoloEager(Long id);
	
	//List<Agenda> findByTitoloAndGenere(String titolo, String genere);
	
	
	@Query("select t from Tavolo t join fetch t.utenteCreazione tu where tu.username = ?1")
	List<Tavolo> FindByUsername(String username);
	
	
	@Query("select t from Tavolo t join fetch t.utenteCreazione")
	List<Tavolo> findAllTavoloEager();
	
	List<Tavolo> findByEsperienzaMinimaLessThan(Integer esperienzaAccumulata);
	
	@Query("select t from Tavolo t join fetch t.giocatori tg where tg.id = ?1")
	Tavolo findLastGame(Long id);
	 
	
}
