package control;

import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashMap;
import javax.swing.event.MouseInputListener;

public class InputManager implements KeyListener, MouseInputListener {

	static protected int KEY_RELEASED = 0;
	static protected int KEY_JUST_PRESSED = 1;
	static protected int KEY_PRESSED = 2;
	static private InputManager instance;
	private HashMap<Integer, Integer> keyCache;
	private ArrayList<Integer> pressedKeys;
	private ArrayList<Integer> releasedKeys;
	private boolean mouseButton1;
	private boolean mouseButton2;
	private Point mousePos;

	private InputManager() {
		keyCache = new HashMap<Integer, Integer>();
		pressedKeys = new ArrayList<Integer>();
		releasedKeys = new ArrayList<Integer>();
		mousePos = new Point();
	}

	static public InputManager getInstance() {
		if (instance == null) {
			instance = new InputManager();
		}
		return instance;
	}

	public boolean isPressed(int keyId) {
		return keyCache.containsKey(keyId)
				&& !keyCache.get(keyId).equals(KEY_RELEASED);
	}

	public boolean isJustPressed(int keyId) {
		return keyCache.containsKey(keyId)
				&& keyCache.get(keyId).equals(KEY_JUST_PRESSED);
	}

	public boolean isReleased(int keyId) {
		return !keyCache.containsKey(keyId)
				|| keyCache.get(keyId).equals(KEY_RELEASED);
	}

	public boolean isMousePressed(int buttonId) {
		if (buttonId == MouseEvent.BUTTON1) {
			return mouseButton1;
		}
		if (buttonId == MouseEvent.BUTTON2) {
			return mouseButton2;
		}
		return false;
	}

	public int getMouseX() {
		return (int) mousePos.getX();
	}

	public int getMouseY() {
		return (int) mousePos.getY();
	}

	public Point getMousePos() {
		return mousePos;
	}

	public void update() {
		for (Integer keyCode : keyCache.keySet()) {
			if (isJustPressed(keyCode)) {
				keyCache.put(keyCode, KEY_PRESSED);
			}
		}
		for (Integer keyCode : releasedKeys) {
			keyCache.put(keyCode, KEY_RELEASED);
		}
		for (Integer keyCode : pressedKeys) {
			if (isReleased(keyCode)) {
				keyCache.put(keyCode, KEY_JUST_PRESSED);
			} else {
				keyCache.put(keyCode, KEY_PRESSED);
			}
		}
		pressedKeys.clear();
		releasedKeys.clear();
	}

	public void keyTyped(KeyEvent e) {
		// Rotina não utilizada. Evento de tecla teclada.
	}

	public void keyPressed(KeyEvent e) {
		pressedKeys.add(e.getKeyCode());
	}

	public void keyReleased(KeyEvent e) {
		releasedKeys.add(e.getKeyCode());
	}

	public void mouseClicked(MouseEvent e) {
	}

	public void mousePressed(MouseEvent e) {
		if (e.getButton() == MouseEvent.BUTTON1) {
			mouseButton1 = true;
		}
		if (e.getButton() == MouseEvent.BUTTON2) {
			mouseButton2 = true;
		}
	}

	public void mouseReleased(MouseEvent e) {
		if (e.getButton() == MouseEvent.BUTTON1) {
			mouseButton1 = false;
		}
		if (e.getButton() == MouseEvent.BUTTON2) {
			mouseButton2 = false;
		}
	}

	public void mouseEntered(MouseEvent e) {
	}

	public void mouseExited(MouseEvent e) {
	}

	public void mouseDragged(MouseEvent e) {
		mousePos.setLocation(e.getPoint());
	}

	public void mouseMoved(MouseEvent e) {
		mousePos.setLocation(e.getPoint());
	}
}
