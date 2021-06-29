package it.polito.tdp.artsmia.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.artsmia.db.ArtsmiaDAO;

public class Model {
	private ArtsmiaDAO dao;
	private SimpleWeightedGraph <Artista, DefaultWeightedEdge > grafo;
	private Map <Integer, Artista> idMap;
	private boolean flag_grafo;
	private List<Adiacenza> edge;
	private ArrayList<Artista> topPercorso;
	
	public Model() {
		this.dao = new ArtsmiaDAO();
	}

	public List<String> getRuoli() {
		// TODO Auto-generated method stub
		return this.dao.getRuoli();
	}

	public void creaGrafo(String ruolo) {
		this.grafo = new SimpleWeightedGraph<> (DefaultWeightedEdge.class);
		this.idMap = new HashMap<>();
		this.dao.loadIdMap(this.idMap, ruolo);
		Graphs.addAllVertices(this.grafo, this.idMap.values());
		this.edge = new ArrayList<Adiacenza>();
		this.edge = this.dao.getAdiacenze(this.idMap, ruolo);
		for(Adiacenza a : this.edge) {
			Graphs.addEdge(this.grafo, a.getA1(), a.getA2(), a.getPeso());
		}
		this.flag_grafo = true;		
	}
	public boolean isSetGrafo() {
		return this.flag_grafo;
	}
	public String infoGrafo() {
		if(this.flag_grafo){
			return "GRAFO CREATO\n#ARCHI = "+this.grafo.edgeSet().size()+"\n#VERTICI = "+ this.grafo.vertexSet().size()+"\n";
		}
		return "GRAFO NON CREATO\n";
	}
	public List<Adiacenza> getCongiutni(){
		Collections.sort(this.edge, new OrderPeso());
		return this.edge;
		
	}

	public boolean idValidator(Integer id) {
		// TODO Auto-generated method stub
		return (this.idMap.get("id")!=null);
	}

	public Artista getActor(Integer id) {
		// TODO Auto-generated method stub
		return this.idMap.get(id);
	}

	public List<Artista> getPercorso(Artista a) {
		this.topPercorso = new ArrayList<Artista> ();
		
		for(DefaultWeightedEdge ed : this.grafo.incomingEdgesOf(a)) {
			List<Artista> parziale = new ArrayList<>();
			cerca(Graphs.getOppositeVertex(this.grafo, ed, a), parziale, grafo.getEdgeWeight(ed));
		}
		return this.topPercorso;
		
	}

	private void cerca(Artista nuovo, List<Artista> parziale, double peso) {
		List<Artista> next = this.getNext(nuovo, parziale, peso );
		if(next.size()==0) {
			if(this.topPercorso.size()<=parziale.size()) {
				this.topPercorso = new ArrayList<>(parziale);
			}
			return;
		}
		for(Artista a : next) {
			parziale.add(a);
			this.cerca(nuovo, parziale, peso);
			parziale.remove(parziale.size()-1);
		}
	}

	private List<Artista> getNext(Artista nuovo, List<Artista> parziale, double peso) {
		List<Artista> result = new ArrayList<>();
		for(DefaultWeightedEdge ed : this.grafo.incomingEdgesOf(nuovo)) {
			Artista prova = Graphs.getOppositeVertex(this.grafo, ed, nuovo);
			if(peso ==  this.grafo.getEdgeWeight(ed) && !parziale.contains(prova)) {
				result.add(prova);
			}
		}
		return result;
	}

	

}
