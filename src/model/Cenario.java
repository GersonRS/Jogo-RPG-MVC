package model;

import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * informa��es da classe Scenery
 * 
 * @author Gerson
 */
public class Cenario {

	private Rectangle2D.Double pos;
	private HashMap<Integer, String> destino;
	private int width, height;
	private int tileWidth;
	private int tileHeight;
	private String source;
	private HashMap<String, ArrayList<Elemento>> obstaculos;
	private HashMap<String, int[][]> camadas;
	private HashMap<String, String> datas;
	private HashMap<String, BufferedImage> layers;
	private ArrayList<String> layersInferiores;
	private ArrayList<String> layersSuperiores;

	/**
	 * 
	 * Metodo Construtor da classe Scenery
	 * 
	 * @param diretorio
	 *            Diretorio de onde esta o cenario a ser carregado
	 * 
	 */

	public Cenario(String diretorio) {
		this.datas = new HashMap<String, String>();
		this.camadas = new HashMap<String, int[][]>();
		this.layers = new HashMap<String, BufferedImage>();
		this.obstaculos = new HashMap<String, ArrayList<Elemento>>();
		this.layersInferiores = new ArrayList<String>();
		this.layersSuperiores = new ArrayList<String>();
		this.destino = new HashMap<Integer, String>();
		pos = new Rectangle2D.Double();
		carregaCenario(diretorio);
		montarMatriz();
		criaObstaculosInOut();
	}

	/**
	 * Metodo que constroi toda a matriz carregada apartir do arquivo do
	 * diretorio informado
	 * 
	 * @return void
	 */

	private void montarMatriz() {
		try {
			for (Map.Entry<String, String> entry : datas.entrySet()) {
				if (entry.getValue() != null && entry.getValue().length() > 0) {
					int camada[][] = new int[height][width];
					StringTokenizer linhas = new StringTokenizer(
							entry.getValue(), "\n");
					int i = 0;
					while (linhas.hasMoreTokens()) {
						StringTokenizer colunas = new StringTokenizer(
								linhas.nextToken(), ",");
						int j = 0;
						while (colunas.hasMoreTokens()) {
							camada[i][j] = Integer
									.parseInt(colunas.nextToken());
							j++;
						}
						i++;
					}
					camadas.put(entry.getKey(), camada);
				}
			}
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
	}

	/**
	 * cria os obstaculos e os in out.
	 * 
	 * @return void
	 */

	private void criaObstaculosInOut() {
		for (Map.Entry<String, int[][]> entry : camadas.entrySet()) {
			ArrayList<Elemento> obs = new ArrayList<Elemento>();
			for (int i = 0; i < height; i++) {
				for (int j = 0; j < width; j++) {
					int tile = (entry.getValue()[i][j] != 0) ? (entry
							.getValue()[i][j] - 1) : 0;
					if (entry.getKey().equalsIgnoreCase("obstaculos")
							&& tile != 0) {
						Obstaculo o = new Obstaculo((j * tileWidth),
								(i * tileHeight), tileWidth, tileHeight, tile);
						obs.add(o);
					} else if (entry.getKey().equalsIgnoreCase("in")
							&& tile != 0) {
						Obstaculo o = new Obstaculo((j * tileWidth),
								(i * tileHeight), tileWidth, tileHeight, tile);
						obs.add(o);
					} else if (entry.getKey().equalsIgnoreCase("out")
							&& tile != 0) {
						Obstaculo o = new Obstaculo((j * tileWidth),
								(i * tileHeight), tileWidth, tileHeight, tile);
						obs.add(o);
					}

				}
			}
			if (entry.getKey().equalsIgnoreCase("obstaculos"))
				obstaculos.put(entry.getKey(), obs);
			else if (entry.getKey().equalsIgnoreCase("in"))
				obstaculos.put(entry.getKey(), obs);
			else if (entry.getKey().equalsIgnoreCase("out"))
				obstaculos.put(entry.getKey(), obs);
		}
		System.gc();
	}

	/**
	 * Carrega o cen�rio a partir de um diretorio e inicializa todos os
	 * atributos do cen�rio.
	 * 
	 * @param diretorio
	 *            Diretorio de onde esta o cenario
	 * 
	 * @return void
	 */

	private void carregaCenario(String diretorio) {
		InputStream is = getClass().getClassLoader().getResourceAsStream(
				"scenerys/" + diretorio + ".tmx");
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory
					.newInstance();
			factory.setValidating(false);
			DocumentBuilder builder = factory.newDocumentBuilder();
			builder.setEntityResolver(new EntityResolver() {
				public InputSource resolveEntity(String publicId,
						String systemId) throws SAXException, IOException {
					return new InputSource(
							new ByteArrayInputStream(new byte[0]));
				}
			});

			Document doc = builder.parse(is);
			Element docElement = doc.getDocumentElement();

			this.width = Integer.parseInt(docElement.getAttribute("width"));
			this.height = Integer.parseInt(docElement.getAttribute("height"));
			this.tileWidth = Integer.parseInt(docElement
					.getAttribute("tilewidth"));
			this.tileHeight = Integer.parseInt(docElement
					.getAttribute("tileheight"));

			NodeList tileNodes = docElement.getElementsByTagName("tileset");
			Element currente = (Element) tileNodes.item(0);
			Element imageNode = (Element) currente
					.getElementsByTagName("image").item(0);
			this.source = imageNode.getAttribute("source");

			NodeList layerNodes = docElement.getElementsByTagName("layer");
			for (int i = 0; i < layerNodes.getLength(); i++) {
				Element current = (Element) layerNodes.item(i);
				String name = current.getAttribute("name");
				Element dataNode = (Element) current.getElementsByTagName(
						"data").item(0);
				Node cdata = dataNode.getFirstChild();
				String data = cdata.getNodeValue().trim();
				this.datas.put(name, data);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (width * tileWidth < 800 && height * tileHeight < 576) {
				System.out.println("erro");
			}
			pos.x = 0;
			pos.y = 0;
			pos.width = width * tileWidth;
			pos.height = height * tileHeight;
		}

	}

	/**
	 * Metodo para a a defini��o das camadas bases
	 * 
	 * @param s
	 *            Nome da camada a ser definida como camada inferior
	 * 
	 * @return void
	 */
	public void configLayerInferior(String s) {
		layersInferiores.add(s);
	}

	/**
	 * Metodo para a defini��o das camadas superiores
	 * 
	 * @param s
	 *            Nome da camada a ser definida como camada superior
	 * 
	 * @return void
	 */
	public void configLayerSuperior(String s) {
		layersSuperiores.add(s);
	}

	/**
	 * get do posicionamento do cen�rio
	 * 
	 * @return Rectangle2D.Double
	 */
	public Rectangle2D.Double getPos() {
		return pos;
	}

	/**
	 * get dos obstaculos que tem no cenr�rio
	 * 
	 * @return ArrayList<Obstaculo>
	 */
	public ArrayList<Elemento> getObstaculos() {
		return obstaculos.get("obstaculos");
	}

	/**
	 * get dos lugares onde o personagem se teleporta
	 * 
	 * @return ArrayList<Obstaculo>
	 */
	public ArrayList<Elemento> getIn() {
		return obstaculos.get("in");
	}

	/**
	 * get dos lugares onde o personagem se teleporta para outros cenarios
	 * 
	 * @return ArrayList<Obstaculo>
	 */
	public ArrayList<Elemento> getOut() {
		return obstaculos.get("out");
	}

	/**
	 * Metodo que adiciona um cenario destino
	 * 
	 * @param cenario
	 *            Nome do cenario destino
	 * @param local
	 *            numero do local para o qual o personagem ira se teleportar
	 * 
	 * @return void
	 */
	public void addTeleport(String cenario, int local) {
		destino.put(local, cenario);
	}

	/**
	 * Metodo que retorna o local de destino para o qual o personagem se
	 * teleporta.
	 * 
	 * @param local
	 *            numero do local para o qual o personagem ira se teleportar
	 * 
	 * @return String
	 */
	public String getDestino(int local) {
		if (destino.containsKey(local)) {
			return destino.get(local);
		}
		return "";
	}

	public String getSource() {
		return source;
	}

	public HashMap<String, int[][]> getCamadas() {
		return camadas;
	}

	public HashMap<String, BufferedImage> getLayers() {
		return layers;
	}

	public HashMap<Integer, String> getDestino() {
		return destino;
	}

	public void setDestino(HashMap<Integer, String> destino) {
		this.destino = destino;
	}

	public ArrayList<String> getLayersInferiores() {
		return layersInferiores;
	}

	public ArrayList<String> getLayersSuperiores() {
		return layersSuperiores;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public int getTileWidth() {
		return tileWidth;
	}

	public int getTileHeight() {
		return tileHeight;
	}

}
