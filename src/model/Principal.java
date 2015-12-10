package model;

import java.awt.event.KeyEvent;
import java.awt.geom.Rectangle2D.Double;

import control.InputManager;

public class Principal extends Personagem {

	private Inventario inventario;
	protected int strength;
	protected int intelligence;
	protected int agility;
	protected int dexterity;
	protected int hp, hpMax;
	protected int mp, mpMax;
	protected int level;
	protected int exp, expMax;
	

	public Principal(int x, int y, int width, int height, int numFrames,
			String img){
		super(x, y, width, height, numFrames, img);
		this.hpMax = 250;
		this.hp = 250;
		this.mpMax = 50;
		this.mp = 50;
		this.level = 1;
		this.expMax = 100;
		this.exp = 0;
		inventario = new Inventario();
	}

	@Override
	public void mover(InputManager i) {
		if (i.isPressed(KeyEvent.VK_RIGHT))
			acceleration.x = 0.4;
		else if (i.isPressed(KeyEvent.VK_LEFT))
			acceleration.x = -0.4;
		else if (i.isPressed(KeyEvent.VK_DOWN))
			acceleration.y = 0.4;
		else if (i.isPressed(KeyEvent.VK_UP))
			acceleration.y = -0.4;
	}

	public void maisExp(int mais) {
		exp += mais;
		if(exp > expMax){
			expMax *= 2;
		}
	}

	public Inventario getInventario() {
		return inventario;
	}

	@Override
	public void update(int currentTick) {
		super.update(currentTick);
	}

	@Override
	public Double getColisao() {
		return super.getColisao();
	}

	
}
