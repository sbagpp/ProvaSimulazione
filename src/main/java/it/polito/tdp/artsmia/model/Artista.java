package it.polito.tdp.artsmia.model;

public class Artista {
	private Integer id;
	private String nome;
	public Artista(Integer id, String nome) {
		super();
		this.id = id;
		this.nome = nome;
	}
	public Integer getId() {
		return id;
	}
	public String getNome() {
		return nome;
	}
	@Override
	public String toString() {
		return nome;
	}
	
	
}
