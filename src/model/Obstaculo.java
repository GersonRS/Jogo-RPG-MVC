package model;

import java.awt.geom.Rectangle2D;

import control.InputManager;

/**
 * 
 * Classe que representa um obstaculo para algum elemento.
 * 
 */
public class Obstaculo extends Elemento {

	/**
	 * Crie um novo Obstaculo.
	 */
	public Obstaculo(int x, int y, int width, int height, int id) {
		super(x, y, width, height);
		this.id = id;
		this.setAtivo(true);
		this.visivel = true;
	}

	@Override
	public Rectangle2D.Double getColisao() {
		return getPos();
	}

	@Override
	public void update(int currentTick) {

	}

	@Override
	public void mover(InputManager inputManager) {
		// TODO Auto-generated method stub
		
	}

}
