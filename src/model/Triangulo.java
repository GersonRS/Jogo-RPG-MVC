package model;

import control.InputManager;

public class Triangulo extends PecaGeometrica {

	public Triangulo(int x, int y, int id) {
		super(x, y, id);
		carregaImage("triangulo.png");
	}

	@Override
	public double calculaArea() {
		return Math.pow(tamanho, 2) / 2;
	}

	@Override
	public void update(int currentTick) {

	}

	@Override
	public void mover(InputManager inputManager) {
		// TODO Auto-generated method stub

	}

}
