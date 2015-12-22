package view;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.Point2D;
import java.io.IOException;
import java.util.ArrayList;

import model.PecaGeometrica;
import model.Principal;

public class Hud {

	private Principal principal;
	private ArrayList<Point2D> posicoes;

	public Hud(Principal principal) {
		this.principal = principal;
		posicoes = new ArrayList<Point2D>();
		posicoes.add(new Point(621, 235));
		posicoes.add(new Point(621, 235 + 85));
		posicoes.add(new Point(621, 235 + 85 + 83));
		posicoes.add(new Point(621 + 75, 235));
		posicoes.add(new Point(621 + 75, 235 + 85));
		posicoes.add(new Point(621 + 75, 235 + 85 + 83));
	}
	
	private void renderInventario(Graphics2D g) throws IOException {
		ArrayList<PecaGeometrica> pecas = principal.getInventario().getPecasgeometricas();
		for (int i = 0; i < pecas.size(); i++) {
			g.drawImage(ImageManager.getInstance().loadImage(pecas.get(i).getImage()), (int) posicoes.get(i)
					.getX(), (int) posicoes.get(i).getY(), null);
		}
		g.drawImage(ImageManager.getInstance().loadImage(principal.getInventario().getImage()), 596, 211, null);
	}

	public void pintaHud(Graphics2D g) throws IOException {
		g.drawImage(ImageManager.getInstance().loadImage("hud inventario.png"), 0, 0, null);
		renderInventario(g);
		g.setColor(Color.LIGHT_GRAY);
		g.fillRect(579, 529, 181, 31);
		g.setColor(Color.BLUE);
		g.drawRect(579, 529, 181, 31);
		
	}
}
