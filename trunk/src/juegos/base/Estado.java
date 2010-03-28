package juegos.base;


/** Un estado representa la situaci�n del tablero, piezas y cualquier otro 
 *  elemento del juego en el momento en que uno o m�s jugadores deben ejecutar
 *  una de varias acciones posibles.
 */
public interface Estado {
	
	/** Juego al que corresponde el estado.
	 *  @see Juego 
	 */
	public Juego juego();
	
	/** Jugadores que est�n participando de la partida.
	 *  @see Jugador 
	 */
	public Jugador[] jugadores();
	
	/* La lista de posibles movimientos en el estado para el jugador. Si el
	 * jugador no est� habilitado a mover, se puede retornar un vector vac�o o
	 * null.
	 */
	public Movimiento[] movimientos(Jugador jugador);
	
	/** Resultado en el estado para el jugador dado. Debe retornar 0 para 
	 * 	indicar empate, un n�mero positivo para victoria y un n�mero negativo 
	 *  para derrota. Debe retornar null si el estado de juego no es final.
	 */
	public Double resultado(Jugador jugador);
	
	/** Obtiene el siguiente estado, resultante de ejecutar el movimiento. 
	 */
	public Estado siguiente(Movimiento movimiento);
}
