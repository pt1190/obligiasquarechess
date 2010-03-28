package juegos.torneos;

import juegos.base.*;

/** Interfaz de todos aquellos controladores de torneos. Un torneo se define 
 *  como un conjunto de partidas entre varios agentes siguiendo una determinada
 *  organizaci�n. Puede ser simplemente "todos contra todos", "eliminatoria" o
 *  esquemas m�s elaborados.  
 */
public interface Torneo {

	/** Juego del torneo.
	 */
	public Juego juego();
	
	/** Agentes que participan en el torneo. 
	 */
	public Agente[] agentes();
	
	/** Correr todas las partidas acumulando las estad�sticas. Retorna la 
	 *  cantidad total de partidas jugadas.  
	 */
	public int completar();
	
	/** Cantidad de partidas que jug� el agente. Retorna null si el agente no
	 *  participa del torneo.
	 */
	public Double partidasJugadas(Agente agente);
	
	/** Cantidad de partidas ganadas por el agente. Retorna null si el agente no
	 *  participa del torneo.
	 */
	public Double partidasGanadas(Agente agente);
	
	/** Cantidad de partidas perdidas por el agente. Retorna null si el agente 
	 *  no participa del torneo.
	 */
	public Double partidasPerdidas(Agente agente);
	
	/** Resultado promedio del agente en todas las partidas que jug�. Retorna 
	 *  null si el agente no participa del torneo.
	 */
	public Double resultadoPromedio(Agente agente);
	
	/** Todas las estad�sticas antes mencionadas en un vector. Las posiciones
	 *  ser�an:
	 *  [0] = partidasJugadas
	 *  [1] = partidasGanadas
	 *  [2] = partidasPerdidas
	 *  [3] = resultadoPromedio
	 *  Posiciones superiores se pueden usar como extensi�n. Si el agente no 
	 *  forma parte del torneo retorna null.
	 */
	public double[] estadisticas(Agente agente);
}
