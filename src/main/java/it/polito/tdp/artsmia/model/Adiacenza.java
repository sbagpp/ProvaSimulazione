package it.polito.tdp.artsmia.model;

public class Adiacenza {
	private Artista a1;
	private Artista a2;
	private Double peso;
	public Adiacenza(Artista a1, Artista a2, Double peso) {
		super();
		this.a1 = a1;
		this.a2 = a2;
		this.peso = peso;
	}
	public Artista getA1() {
		return a1;
	}
	public Artista getA2() {
		return a2;
	}
	public Double getPeso() {
		return peso;
	}
	@Override
	public String toString() {
		return ""+a1.toString()+ " é connnesso a "+a2.toString()+" il loro peso é di : "+this.peso+"\n";
	}
	
}
