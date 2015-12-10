package model;

import control.InputManager;

/**
 * 
 * Interface provedora de um Movimento para um Elemento. 
 * 
 */

public interface Movimento {

	/**
	 * 
	 * Metodo de movimento de um elemento
	 * 
	 * @param inputManager
	 * 		objeto controlador das intera��es
	 * 
	 * @return void
	 */
	void mover(InputManager inputManager);
}
