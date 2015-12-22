package model;

import java.awt.event.KeyEvent;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.HashMap;

import control.GameControlCenter;
import control.InputManager;

public class GameLogic {

	private final int ABOVE = 0;
	private final int RIGHT = 1;
	private final int BELOW = 2;
	private final int LEFT = 3;
	protected Elemento elemento;
	protected HashMap<String, ArrayList<Elemento>> elementos;
	protected HashMap<String, Cenario> cenarios;
	protected boolean transition, alert;
	private String nextCenerio;
	private Point2D.Double nextPos;

	public GameLogic(Elemento elemento,
			HashMap<String, ArrayList<Elemento>> elementos,
			HashMap<String, Cenario> cenarios) {
		this.elemento = elemento;
		this.elementos = elementos;
		this.cenarios = cenarios;
		this.nextPos = new Point2D.Double();
	}

	/**
	 * O método logica atualiza todos os estados jogo de objetos. O jogo irá
	 * processá-lo do modelo de dados e definir a próxima situação de jogo que
	 * irá ser pintado na tela.
	 */
	public void logica(int tick) {

		if (transition) {
			if (GameControlCenter.alpha > 0.9) {
				GameControlCenter.add *= -1;
				currentCenario(nextCenerio);
				elemento.getPos().x = nextPos.x;
				elemento.getPos().y = nextPos.y;
				cenarios.get(nextCenerio).getPos().y = (elemento.getPos().y - GameControlCenter.height / 3)
						* -1;
				cenarios.get(nextCenerio).getPos().x = (elemento.getPos().x - GameControlCenter.width / 3)
						* -1;
				// ajeitar o cenario
				if (cenarios.get(GameControlCenter.currentCenario).getPos().x > 0) {
					cenarios.get(GameControlCenter.currentCenario).getPos().x = 0;
				}
				if (cenarios.get(GameControlCenter.currentCenario).getPos().y > 0) {
					cenarios.get(GameControlCenter.currentCenario).getPos().y = 0;
				}
				if (cenarios.get(GameControlCenter.currentCenario).getPos().x < (cenarios
						.get(GameControlCenter.currentCenario).getPos().width
						- GameControlCenter.width / 3 - 14 - GameControlCenter.width / 3)
						* -1) {
					cenarios.get(GameControlCenter.currentCenario).getPos().x = (cenarios
							.get(GameControlCenter.currentCenario).getPos().width
							- GameControlCenter.width / 3 - 14 - GameControlCenter.width / 3)
							* -1;
				}
				if (cenarios.get(GameControlCenter.currentCenario).getPos().y < (cenarios
						.get(GameControlCenter.currentCenario).getPos().height
						- GameControlCenter.height / 3 - 23 - GameControlCenter.height / 3)
						* -1) {
					cenarios.get(GameControlCenter.currentCenario).getPos().y = (cenarios
							.get(GameControlCenter.currentCenario).getPos().height
							- GameControlCenter.height / 3 - 23 - GameControlCenter.height / 3)
							* -1;
				}
				nextCenerio = "";
				updateElementos(tick);
				alert = true;
			}
			GameControlCenter.alpha += GameControlCenter.add;
			if (GameControlCenter.alpha < 0.0) {
				GameControlCenter.add = 0.01f;
				GameControlCenter.alpha = 0.0f;
				transition = false;
			}
		} else {
			double x = elemento.getPos().x;
			double y = elemento.getPos().y;

			updateElementos(tick);

			if (elemento.getPos().x >= GameControlCenter.width / 3
					&& elemento.getPos().x <= cenarios.get(
							GameControlCenter.currentCenario).getPos().width
							- GameControlCenter.width / 3 - 14) {
				cenarios.get(GameControlCenter.currentCenario).getPos().x -= elemento
						.getPos().x - x;
			}
			if (elemento.getPos().y >= GameControlCenter.height / 3
					&& elemento.getPos().y <= cenarios.get(
							GameControlCenter.currentCenario).getPos().height
							- GameControlCenter.height / 3 - 23) {
				cenarios.get(GameControlCenter.currentCenario).getPos().y -= elemento
						.getPos().y - y;
			}

			// sair do mapa
			if (elemento.getPos().x < 0
					|| elemento.getPos().x + elemento.getPos().width > cenarios
							.get(GameControlCenter.currentCenario).getPos().width) {
				elemento.getPos().x = x;
			}
			if (elemento.getPos().y < 0
					|| elemento.getPos().y + elemento.getPos().height > cenarios
							.get(GameControlCenter.currentCenario).getPos().height) {
				elemento.getPos().y = y;
			}

			if (InputManager.getInstance().isJustPressed(KeyEvent.VK_ENTER)) {
				for (int j = 0; j < elemento.getCollidingEntities().length; j++) {
					if (elemento.getCollidingEntities()[j] != null)
						if (elemento.getCollidingEntities()[j] instanceof NPC) {

						} else if (elemento.getCollidingEntities()[j] instanceof PecaGeometrica) {
							if (((Principal) elemento).getInventario()
									.getPecasgeometricas().size() < ((Principal) elemento)
									.getInventario().getNUM_MAX()) {
								((Principal) elemento).getInventario().add(
										(PecaGeometrica) elemento
												.getCollidingEntities()[j]);
								elemento.getCollidingEntities()[j]
										.setAtivo(false);
							}
						}
				}
			}
		}
	}

	public void currentCenario(String current) {
		if (cenarios.containsKey(current)) {
			Cenario scenery = cenarios.get(current);
			elementos.put("obstaculos", scenery.getObstaculos());
			elementos.put("out", scenery.getOut());
			for (ArrayList<Elemento> elements : elementos.values()) {
				elements.remove(elemento);
			}
			elementos.get(current).add(elemento);
			GameControlCenter.currentCenario = current;
		}
	}

	public void removerObstaculos(int id) {
		ArrayList<Elemento> o = elementos.get("obstaculos");
		ArrayList<Elemento> r = new ArrayList<Elemento>();
		for (int i = 0; i < o.size(); i++) {
			if (o.get(i).id == id) {
				r.add(o.get(i));
			}
		}
		for (int i = 0; i < r.size(); i++) {
			o.remove(r.get(i));
		}
	}

	protected void updateElementos(int tick) {
		for (int i = 0; i < elementos.get(GameControlCenter.currentCenario)
				.size(); i++) {
			Elemento e = elementos.get(GameControlCenter.currentCenario).get(i);
			if (e.isAtivo()) {
				e.mover(InputManager.getInstance());
				e.update(tick);
			} else {
				elementos.get(GameControlCenter.currentCenario).remove(e);
			}
		}
		testaColisao();
	}

	public void testaColisao() {
		ArrayList<Elemento> element = this.elementos
				.get(GameControlCenter.currentCenario);
		ArrayList<Elemento> obstaculo = this.elementos.get("obstaculos");
		ArrayList<Elemento> out = this.elementos.get("out");

		for (Elemento o : element) {
			for (int i = 0; i < o.getCollidingEntities().length; i++) {
				o.getCollidingEntities()[i] = null;
			}
		}
		for (int i1 = 0; i1 < element.size() - 1; i1++) {
			Elemento o1 = element.get(i1);
			for (int i2 = i1 + 1; i2 < element.size(); i2++) {
				Elemento o2 = element.get(i2);
				double difX = (o1.getColisao().getX() + (o1.getColisao()
						.getWidth() / 2))
						- (o2.getColisao().getX() + (o1.getColisao().getWidth() / 2));
				double difY = (o1.getColisao().getY() + (o1.getColisao()
						.getHeight() / 2))
						- (o2.getColisao().getY() + (o1.getColisao()
								.getHeight() / 2));
				double distancia = Math.sqrt((difX * difX) + (difY * difY));
				if (distancia < 64)
					if (o1.getColisao().intersects(o2.getColisao())) {
						Rectangle2D rect = o1.getColisao().createIntersection(
								o2.getColisao());
						if (rect.getWidth() > rect.getHeight()) {
							if (o1.getColisao().getCenterY() < o2.getColisao()
									.getCenterY()) {
								o1.getCollidingEntities()[BELOW] = o2;
								o2.getCollidingEntities()[ABOVE] = o1;
							} else {
								o1.getCollidingEntities()[ABOVE] = o2;
								o2.getCollidingEntities()[BELOW] = o1;
							}
						} else {
							if (o1.getColisao().getCenterX() < o2.getColisao()
									.getCenterX()) {
								o1.getCollidingEntities()[RIGHT] = o2;
								o2.getCollidingEntities()[LEFT] = o1;
							} else {
								o1.getCollidingEntities()[LEFT] = o2;
								o2.getCollidingEntities()[RIGHT] = o1;
							}
						}
					}
			}
		}
		for (int i1 = 0; i1 < element.size(); i1++) {
			Elemento o1 = element.get(i1);
			for (int i2 = 0; i2 < obstaculo.size(); i2++) {
				Elemento o2 = obstaculo.get(i2);
				if (o2.isAtivo()) {
					double difX = (o1.getColisao().getX() + (o1.getColisao()
							.getWidth() / 2))
							- (o2.getColisao().getX() + (o1.getColisao()
									.getWidth() / 2));
					double difY = (o1.getColisao().getY() + (o1.getColisao()
							.getHeight() / 2))
							- (o2.getColisao().getY() + (o1.getColisao()
									.getHeight() / 2));
					double distancia = Math.sqrt((difX * difX) + (difY * difY));
					if (distancia < 64)
						if (o1.getColisao().intersects(o2.getColisao())) {
							Rectangle2D rect = o1.getColisao()
									.createIntersection(o2.getColisao());
							if (rect.getWidth() > rect.getHeight()) {
								if (o1.getColisao().getCenterY() < o2
										.getColisao().getCenterY()) {
									o1.getCollidingEntities()[BELOW] = o2;
									o2.getCollidingEntities()[ABOVE] = o1;
								} else {
									o1.getCollidingEntities()[ABOVE] = o2;
									o2.getCollidingEntities()[BELOW] = o1;
								}
							} else {
								if (o1.getColisao().getCenterX() < o2
										.getColisao().getCenterX()) {
									o1.getCollidingEntities()[RIGHT] = o2;
									o2.getCollidingEntities()[LEFT] = o1;
								} else {
									o1.getCollidingEntities()[LEFT] = o2;
									o2.getCollidingEntities()[RIGHT] = o1;
								}
							}
						}

				}
			}
		}
		for (int i = 0; i < out.size(); i++) {
			if (out.get(i).isAtivo()) {
				double difX = (out.get(i).getColisao().getX() + (out.get(i)
						.getColisao().getWidth() / 2))
						- (elemento.getColisao().getX() + (out.get(i)
								.getColisao().getWidth() / 2));
				double difY = (out.get(i).getColisao().getY() + (out.get(i)
						.getColisao().getHeight() / 2))
						- (elemento.getColisao().getY() + (out.get(i)
								.getColisao().getHeight() / 2));
				double distancia = Math.sqrt((difX * difX) + (difY * difY));
				if (distancia < 64)
					if (out.get(i).getColisao()
							.intersects(elemento.getColisao())) {
						String destino = cenarios.get(
								GameControlCenter.currentCenario).getDestino(
								out.get(i).id);
						if (destino != "")
							for (Elemento in : cenarios.get(destino).getIn()) {
								if (in.id == out.get(i).id) {
									nextPos.y = in.getPos().y
											- in.getPos().height - 5;
									nextPos.x = in.getPos().x + 5;
									nextCenerio = destino;
									transition = true;
								}
							}
					}
			}
		}
	}

	public boolean isAlert() {
		return alert;
	}

	public void setAlert(boolean alert) {
		this.alert = alert;
	}

}
