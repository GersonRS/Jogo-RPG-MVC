package control;

import java.util.ArrayList;

import model.Cenario;
import model.Elemento;
import model.GameLogic;
import model.NPC;
import model.Principal;
import model.Quadrado;
import model.Triangulo;
import view.GameRenderer;

public class GameController implements LoopSteps {

	private MainLoop main;
	private GameRenderer render;
	private GameLogic logic;
	public static final int width = 800;
	public static final int height = 600;
	public static String currentCenario;
	public static volatile float alpha = 0.0f;
	public static float add = 0.01f;

	public GameController() {
		main = new MainLoop(this, 60);
		this.logic = new GameLogic();
		new Thread(main).start();
	}

	@Override
	public void setup() {
		loadCenario("cidade");
		loadCenario("floresta 1");
		loadCenario("floresta 2");
		loadCenario("floresta 3");
		loadCenario("floresta 4");
		loadCenario("floresta 5");
		loadCenario("caverna 1");
		addTeleport("cidade", "floresta 1", 6);
		addTeleport("floresta 1", "cidade", 6);
		addTeleport("floresta 1", "floresta 5", 5);
		addTeleport("floresta 1", "floresta 2", 3);
		addTeleport("floresta 2", "floresta 1", 3);
		addTeleport("floresta 2", "floresta 4", 4);
		addTeleport("floresta 2", "floresta 3", 5);
		addTeleport("floresta 3", "floresta 2", 5);
		addTeleport("floresta 3", "floresta 4", 2);
		addTeleport("floresta 4", "floresta 3", 2);
		addTeleport("floresta 4", "floresta 2", 4);
		addTeleport("floresta 4", "floresta 5", 7);
		addTeleport("floresta 4", "caverna 1", 3);
		addTeleport("floresta 4", "caverna 1", 6);
		addTeleport("floresta 5", "floresta 1", 5);
		addTeleport("floresta 5", "floresta 4", 7);
		addTeleport("caverna 1", "floresta 4", 3);
		addTeleport("caverna 1", "floresta 4", 6);
		configLayers();
		addElementoPrincipal(new Principal(196, 100, 23, 55, 6,
				"personagem.png"));
		this.render = new GameRenderer(logic.getElemento(), logic.getElementos(), logic.getCenarios());
		logic.currentCenario("cidade");
		render.currentCenario();

		render.init();
		render.addKeyListener(InputManager.getInstance());
		render.addMouseListener(InputManager.getInstance());
		addPecasGeometricas();
		addNPCs();
	}

	@Override
	public void processLogics(int tick) {
		InputManager.getInstance().update();
		logic.logica(tick);
		if (logic.isAlert()) {
			render.currentCenario();
			logic.setAlert(false);
		}
	}

	@Override
	public void renderGraphics() {
		render.paint();
	}

	@Override
	public void tearDown() {

	}

	public void loadCenario(String cenario) {
		if (!logic.getCenarios().containsKey(cenario)) {
			Cenario scenery = new Cenario(cenario);
			logic.getCenarios().put(cenario, scenery);
			ArrayList<Elemento> elements = new ArrayList<Elemento>();
			logic.getElementos().put(cenario, elements);
		}
	}

	protected void configLayerInferior(String cenario, String layer) {
		logic.getCenarios().get(cenario).configLayerInferior(layer);
	}

	protected void configLayerSuperior(String cenario, String layer) {
		logic.getCenarios().get(cenario).configLayerSuperior(layer);
	}

	public void addElemento(String cenario, Elemento e) {
		logic.getElementos().get(cenario).add(e);
	}

	public void addElementoPrincipal(Elemento e) {
		logic.setElemento(e);
	}

	public void addTeleport(String cenariOrigem, String cenarioDestino,
			int local) {
		if (logic.getCenarios().containsKey(cenariOrigem)
				&& logic.getCenarios().containsKey(cenarioDestino)) {
			logic.getCenarios().get(cenariOrigem).addTeleport(cenarioDestino, local);
		}
	}

	public void configLayers() {
		// configuração do cenario caverna 1
		configLayerInferior("caverna 1", "vacuo");
		configLayerInferior("caverna 1", "chao");
		configLayerInferior("caverna 1", "pedras");
		configLayerInferior("caverna 1", "paredes");
		configLayerSuperior("caverna 1", "folhas");
//		 configuração do cenario cidade
		configLayerInferior("cidade", "grama");
		configLayerInferior("cidade", "areia");
		configLayerInferior("cidade", "casas");
		configLayerInferior("cidade", "troncos");
		configLayerInferior("cidade", "muros");
		configLayerSuperior("cidade", "telhado");
		configLayerSuperior("cidade", "folhas");
		// configuração do cenario floresta 1
		configLayerInferior("floresta 1", "grama");
		configLayerInferior("floresta 1", "areia");
		configLayerInferior("floresta 1", "penhasco 1");
		configLayerInferior("floresta 1", "penhasco 2");
		configLayerInferior("floresta 1", "pedras");
		configLayerInferior("floresta 1", "morros 1");
		configLayerInferior("floresta 1", "troncos");
		configLayerInferior("floresta 1", "ponte");
		configLayerSuperior("floresta 1", "morros 2");
		configLayerSuperior("floresta 1", "folhas 1");
		configLayerSuperior("floresta 1", "folhas 2");
		// configuração do cenario floresta 2
		configLayerInferior("floresta 2", "grama");
		configLayerInferior("floresta 2", "penhasco 1");
		configLayerInferior("floresta 2", "penhasco 2");
		configLayerInferior("floresta 2", "areia");
		configLayerInferior("floresta 2", "morros 1");
		configLayerInferior("floresta 2", "troncos");
		configLayerSuperior("floresta 2", "folhas 1");
		configLayerSuperior("floresta 2", "folhas 2");
		// configuração do cenario floresta 3
		configLayerInferior("floresta 3", "grama");
		configLayerInferior("floresta 3", "areia");
		configLayerInferior("floresta 3", "penhasco 1");
		configLayerInferior("floresta 3", "penhasco 2");
		configLayerInferior("floresta 3", "ponte");
		configLayerInferior("floresta 3", "troncos");
		configLayerInferior("floresta 3", "pedras");
		configLayerInferior("floresta 3", "morros 1");
		configLayerSuperior("floresta 3", "folhas 1");
		configLayerSuperior("floresta 3", "folhas 2");
		// configuração do cenario floresta 4
		configLayerInferior("floresta 4", "grama");
		configLayerInferior("floresta 4", "penhasco 1");
		configLayerInferior("floresta 4", "penhasco 2");
		configLayerInferior("floresta 4", "areia");
		configLayerInferior("floresta 4", "morros 1");
		configLayerInferior("floresta 4", "morros 2");
		configLayerInferior("floresta 4", "ponte");
		configLayerInferior("floresta 4", "troncos");
		configLayerInferior("floresta 4", "pedras");
		configLayerSuperior("floresta 4", "folhas 1");
		configLayerSuperior("floresta 4", "folhas 2");
		// configuração do cenario floresta 1
		configLayerInferior("floresta 5", "grama");
		configLayerInferior("floresta 5", "areia");
		configLayerInferior("floresta 5", "morros");
		configLayerInferior("floresta 5", "troncos");
		configLayerInferior("floresta 5", "pedras");
		configLayerSuperior("floresta 5", "folhas 1");
		configLayerSuperior("floresta 5", "folhas 2");

	}

	public void addPecasGeometricas() {
		addElemento("floresta 1", new Triangulo(10, 233, 1));
		addElemento("cidade", new Quadrado(1100, 1138, 2));
		addElemento("cidade", new Triangulo(1056, 170, 3));
		addElemento("cidade", new Quadrado(50, 832, 4));
		addElemento("floresta 1", new Triangulo(32, 470, 5));
		addElemento("floresta 1", new Quadrado(766, 490, 6));
		addElemento("floresta 1", new Triangulo(10, 1024, 7));
		addElemento("floresta 2", new Quadrado(180, 360, 8));
		addElemento("floresta 2", new Triangulo(570, 128, 9));
		addElemento("floresta 2", new Quadrado(200, 660, 10));
		addElemento("floresta 2", new Triangulo(232, 660, 11));
		addElemento("floresta 3", new Quadrado(320, 192, 12));
		addElemento("floresta 3", new Triangulo(1216, 256, 13));
		addElemento("floresta 3", new Quadrado(1216, 736, 14));
		addElemento("floresta 3", new Triangulo(928, 576, 15));
		addElemento("floresta 4", new Quadrado(128, 960, 16));
		addElemento("floresta 4", new Triangulo(512, 320, 17));
		addElemento("floresta 4", new Quadrado(960, 800, 18));
		addElemento("floresta 5", new Triangulo(928, 224, 19));
		addElemento("floresta 5", new Quadrado(160, 480, 20));
		addElemento("caverna 1", new Triangulo(540, 864, 21));
		addElemento("caverna 1", new Quadrado(160, 704, 22));
		addElemento("caverna 1", new Triangulo(448, 96, 23));
		addElemento("cidade", new Quadrado(768, 0, 24));
	}

	public void addNPCs() {
		addElemento("cidade",
				new NPC(508, 180, 27, 57, 2, 4, render.getDialogo(),
						"Monstro.png"));
		addElemento("cidade",
				new NPC(256, 736, 27, 57, 2, 4, render.getDialogo(),
						"Monstro.png"));
		addElemento("cidade",
				new NPC(896, 544, 27, 57, 2, 4, render.getDialogo(),
						"Monstro.png"));
		addElemento("floresta 1",
				new NPC(396, 233, 27, 57, 0, 4, render.getDialogo(),
						"Monstro.png"));
		addElemento("floresta 1",
				new NPC(660, 233, 27, 57, 0, 4, render.getDialogo(),
						"Monstro.png"));
	}
}
