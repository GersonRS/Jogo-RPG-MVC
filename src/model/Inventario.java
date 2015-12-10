package model;

import java.util.ArrayList;

public class Inventario {

	private final int NUM_MAX = 6;
	private ArrayList<PecaGeometrica> pecasgeometricas;
	private String image;

	public Inventario() {
		pecasgeometricas = new ArrayList<PecaGeometrica>();
		this.image = "inventario.png";
	}

	public void add(PecaGeometrica peca) {
		if (pecasgeometricas.size() < NUM_MAX) {
			pecasgeometricas.add(peca);
		}
	}
	
	public void remove(int id){
		for (int i = 0; i < pecasgeometricas.size(); i++) {
			PecaGeometrica p = pecasgeometricas.get(i);
			if(p.getId()==id){
				pecasgeometricas.remove(i);
			}
		}
	}

	public ArrayList<PecaGeometrica> getPecasgeometricas() {
		return pecasgeometricas;
	}

	public int getNUM_MAX() {
		return NUM_MAX;
	}

	public String getImage() {
		return image;
	}
	
}
