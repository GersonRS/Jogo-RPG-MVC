package model;

import control.InputManager;

public class Quadrado extends PecaGeometrica {

	public Quadrado(int x, int y, int id) {
		super(x, y, id);
		carregaImage("quadrado.png");
	}

	@Override
	public double calculaArea() {
		return Math.pow(tamanho, 2);
	}

	@Override
	public void update(int currentTick) {

	}

	@Override
	public void mover(InputManager inputManager) {
		// TODO Auto-generated method stub

	}

}
