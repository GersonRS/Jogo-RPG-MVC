package model;

import java.awt.geom.Rectangle2D;

public abstract class PecaGeometrica extends Elemento {

	protected int tamanho = 1;

	public PecaGeometrica(int x, int y, int id) {
		super(x, y, 32, 32);
		this.numFrames = 1;
	}

	public abstract double calculaArea();

	public int getTamanho() {
		return tamanho;
	}

	public int getId() {
		return id;
	}

	@Override
	public Rectangle2D.Double getColisao() {
		return getPos();
	}
}