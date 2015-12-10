package view;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JFrame;

import model.Cenario;
import model.Elemento;
import model.PecaGeometrica;
import model.Personagem;
import model.Principal;
import control.ControlCenter;
import control.InputManager;

public class Renderer extends JFrame {
	private static final long serialVersionUID = 1L;

	private BufferStrategy bufferStrategy;
	private BufferedImage tela;
	private Graphics2D g, g2d;
	private Hud hud;

	private HashMap<String, ArrayList<Elemento>> elementos;
	private HashMap<String, Cenario> cenarios;

	public Renderer(Elemento elemento, HashMap<String, ArrayList<Elemento>> elementos,
			HashMap<String, Cenario> cenarios) {
		this.setTitle("Desenvolvimento de Jogos Digitais");
		this.setSize(ControlCenter.width, ControlCenter.height);
		this.addKeyListener(InputManager.getInstance());
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setUndecorated(true);
		this.setIgnoreRepaint(true);
		this.setVisible(true);
		this.createBufferStrategy(2);
		this.setPreferredSize(new Dimension(ControlCenter.width,
				ControlCenter.height));
		this.setFocusable(true);
		this.requestFocus();

		this.elementos = elementos;
		this.cenarios = cenarios;
		this.hud = new Hud((Principal) elemento);

		bufferStrategy = this.getBufferStrategy();

		g2d = (Graphics2D) bufferStrategy.getDrawGraphics();
		g2d.setColor(Color.BLACK);
		g2d.fillRect(0, 0, ControlCenter.width, ControlCenter.height);
		g2d.dispose();
		bufferStrategy.show();
	}

	public void init() {
		try {
			ImageManager.getInstance().loadImage("tileset.png");
			ImageManager.getInstance().loadImage("Monstro.png");
			ImageManager.getInstance().loadImage("personagem.png");
			ImageManager.getInstance().loadImage("triangulo.png");
			ImageManager.getInstance().loadImage("quadrado.png");
			for (Cenario cenario : cenarios.values()) {
				desenhaCamadas(cenario);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Desenha as camadas que compõe o cenário
	 * 
	 * @return void
	 * @throws IOException
	 */

	private void desenhaCamadas(Cenario cenario) throws IOException {
		for (Map.Entry<String, int[][]> entry : cenario.getCamadas().entrySet()) {
			BufferedImage layer = new BufferedImage(
					(int) cenario.getPos().width,
					(int) cenario.getPos().height,
					BufferedImage.TYPE_4BYTE_ABGR);
			for (int i = 0; i < cenario.getHeight(); i++) {
				for (int j = 0; j < cenario.getWidth(); j++) {
					int tile = (entry.getValue()[i][j] != 0) ? (entry
							.getValue()[i][j] - 1) : 0;
					int tileRow = (tile / 8) | 0;
					int tileCol = (tile % 8) | 0;
					layer.getGraphics().drawImage(
							ImageManager.getInstance().loadImage(
									cenario.getSource()),
							(j * cenario.getTileWidth()),
							(i * cenario.getTileHeight()),
							(j * cenario.getTileWidth())
									+ cenario.getTileWidth(),
							(i * cenario.getTileHeight())
									+ cenario.getTileHeight(),
							(tileCol * cenario.getTileWidth()),
							(tileRow * cenario.getTileHeight()),
							(tileCol * cenario.getTileWidth())
									+ cenario.getTileWidth(),
							(tileRow * cenario.getTileHeight())
									+ cenario.getTileHeight(), null);
				}
			}
			cenario.getLayers().put(entry.getKey(), layer);
		}
	}

	public void update() {

	}

	public void paint() {
		try {
			g = (Graphics2D) tela.getGraphics();

			renderInferior(g);
			renderElementos(g);
			renderSuperior(g);

			g2d = (Graphics2D) bufferStrategy.getDrawGraphics();
			g2d.drawImage(
					tela,
					(int) cenarios.get(ControlCenter.currentCenario).getPos().x,
					(int) cenarios.get(ControlCenter.currentCenario).getPos().y,
					null);
			hud.pintaHud(g2d);
			g2d.setColor(Color.WHITE);
			g2d.setComposite(AlphaComposite.SrcOver.derive(ControlCenter.alpha));
			g2d.fillRect(0, 0, 800, 600);
			g2d.dispose();
			bufferStrategy.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void currentCenario() {
		tela = new BufferedImage((int) cenarios.get(
				ControlCenter.currentCenario).getPos().width, (int) cenarios
				.get(ControlCenter.currentCenario).getPos().height,
				BufferedImage.TYPE_4BYTE_ABGR);
	}

	protected void renderElementos(Graphics2D g) throws IOException {
		for (Elemento elemento : elementos.get(ControlCenter.currentCenario)) {
			if (elemento.isVisivel()) {
				if (elemento instanceof Personagem) {
					g.drawImage(
							ImageManager.getInstance().loadImage(
									elemento.getImage()),
							(int) (elemento.getPos().x),
							(int) (elemento.getPos().y),
							(int) (elemento.getPos().x + elemento.getPos().width),
							(int) (elemento.getPos().y + elemento.getPos().height),
							(int) ((elemento.getAnimates() % elemento
									.getNumFrames()) * elemento.getPos().width),
							(int) (elemento.getDirection() * elemento.getPos().height),
							(int) (((elemento.getAnimates() % elemento
									.getNumFrames()) * elemento.getPos().width) + elemento
									.getPos().width),
							(int) ((elemento.getDirection() * elemento.getPos().height) + elemento
									.getPos().height), null);
				} else if (elemento instanceof PecaGeometrica) {
					g.drawImage(
							ImageManager.getInstance().loadImage(
									elemento.getImage()),
							(int) elemento.getPos().x,
							(int) elemento.getPos().y, null);
				}
			}
		}
	}

	/**
	 * Metodo que desenha uma camada especificada por parametro
	 * 
	 * @param g
	 *            Graphics onde a camada será desenhado
	 * @param name
	 *            Nome da camada a ser desenhada
	 * 
	 * @return void
	 */
	public void render(Graphics2D g, String name) {
		g.drawImage(
				cenarios.get(ControlCenter.currentCenario).getLayers()
						.get(name), 0, 0, null);
	}

	/**
	 * Metodo que desenha todas as camadas de uma vez
	 * 
	 * @param g
	 *            Graphics onde todas as camadas seram desenhadas
	 * 
	 * @return void
	 */
	public void render(Graphics2D g) {
		for (BufferedImage img : cenarios.get(ControlCenter.currentCenario)
				.getLayers().values()) {
			g.drawImage(img, 0, 0, null);
		}
	}

	/**
	 * Metodo que desenha as camadas inferiores
	 * 
	 * @param g
	 *            Graphics onde as camadas bases seram desenhadas
	 * 
	 * @return void
	 */
	public void renderInferior(Graphics2D g) {
		for (String string : cenarios.get(ControlCenter.currentCenario)
				.getLayersInferiores()) {
			render(g, string);
		}
	}

	/**
	 * Metodo que desenha as camadas superiores
	 * 
	 * @param g
	 *            Graphics onde as camadas da superficie seram desenhadas
	 * 
	 * @return void
	 */
	public void renderSuperior(Graphics2D g) {
		for (String string : cenarios.get(ControlCenter.currentCenario)
				.getLayersSuperiores()) {
			render(g, string);
		}
	}
}