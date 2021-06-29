package it.polito.tdp.artsmia.model;

import java.util.Comparator;

public class OrderPeso implements Comparator<Adiacenza> {

	@Override
	public int compare(Adiacenza o1, Adiacenza o2) {
		// TODO Auto-generated method stub
		return -(o1.getPeso().compareTo(o2.getPeso()));
	}

}
