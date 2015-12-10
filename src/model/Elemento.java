package model;

import java.awt.geom.Rectangle2D;

/**
 * 
 * Elemento base do jogo. Esta classe prove os principais atributos 
 * para os demais elementos especializados deste elemento.
 * 
 */
public abstract class Elemento implements Acoes {

	protected Rectangle2D.Double pos;
	protected Elemento[] collidingEntities;
	protected int animates;
	protected int numFrames;
	protected int direction;
	protected boolean ativo;
	protected boolean visivel;
	protected int id;
	protected String image;

	/**
	 * 
	 * Crie um novo Elemento.
	 * 
	 * @param x
	 *            posição x do elemento
	 * @param y
	 *            posição y do elemento
	 * @param width
	 *            largura do elemento
	 * @param height
	 *            altura do elemento
	 *            
	 */
	public Elemento(int x, int y, int width, int height) {
		this.pos = new Rectangle2D.Double(x, y, width, height);
		this.collidingEntities = new Elemento[4];
		this.ativo = true;
	}

	/**
	 * 
	 * metodo de atualização do elemento.
	 * 
	 * @param currentTick
	 *            numero de vezes que o jogo fez iterações.
	 * 
	 */
	public abstract void update(int currentTick);

	/**
	 * 
	 * retorna a posição atual do elemento.
	 * 
	 * @return Rectangle2D.Double
	 */
	public Rectangle2D.Double getPos() {
		return pos;
	}

	/**
	 * 
	 * retorna outros elementos com os quais este elemento esta colidindo.
	 * 
	 * @return Elemento[]
	 */
	public Elemento[] getCollidingEntities() {
		return collidingEntities;
	}

	/**
	 * 
	 * retorna o estado ativo do elemento.
	 * 
	 * @return boolean
	 */
	public boolean isAtivo() {
		return ativo;
	}

	/**
	 * 
	 * altera o estado ativo do elemento.
	 * 
	 * @param ativo
	 *            valor do novo estado ativo do elemento.
	 * 
	 */
	public void setAtivo(boolean ativo) {
		this.ativo = ativo;
	}

	/**
	 * 
	 * retorna o estado visivel do elemento.
	 * 
	 * @return boolean
	 */
	public boolean isVisivel() {
		return visivel;
	}

	/**
	 * 
	 * altera o estado visivel do elemento.
	 * 
	 * @param ativo
	 *            valor do novo estado visivel do elemento.
	 * 
	 */
	public void setVisivel(boolean visivel) {
		this.visivel = visivel;
	}

	public String getImage() {
		return image;
	}

	public int getAnimates() {
		return animates;
	}

	public int getNumFrames() {
		return numFrames;
	}

	public int getDirection() {
		return direction;
	}

	
}