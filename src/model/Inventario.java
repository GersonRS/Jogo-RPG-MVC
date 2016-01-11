package model;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

public class Inventario {

	private final int NUM_MAX = 6;
	private ArrayList<PecaGeometrica> pecasgeometricas;
	private BufferedImage image;

	public Inventario() {
		pecasgeometricas = new ArrayList<PecaGeometrica>();
		try {
			this.image = ImageManager.getInstance().loadImage("inventario.png");
		} catch (IOException e) {
			e.printStackTrace();
		}
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

	public BufferedImage getImage() {
		return image;
	}
	
}
