package juegos.torneos;

import juegos.Partida;
import juegos.agentes.AgenteAleatorio;
import juegos.agentes.AgenteMiniMaxSqChess;
import juegos.base.*;
import juegos.tateti.Tateti;
import juegos.squareChess.SquareChess;;

/** Torneo que hace jugar a todos los agentes contra todos los agentes, jugando
 *  con todos los jugadores, repetido una cierta cantidad de veces.  
 */
public class TorneoTodosContraTodos extends _Torneo {
	private final int veces;

	public TorneoTodosContraTodos(int veces, Juego juego, Agente... agentes) {
		super(juego, agentes);
		this.veces = veces;
	}
	
	public TorneoTodosContraTodos(Juego juego, Agente... agentes) {
		this(1, juego, agentes);
	}

	@Override public int completar() {
		int cantidad = 0;
		Agente[] participantes = new Agente[juego.jugadores().length];
		for (int vez = 0; vez < veces; vez++) {
			cantidad += completar(participantes, 0);
		}
		return cantidad;
	}
	
	protected int completar(Agente[] participantes, int pos) {
		if (pos < participantes.length) {
			int cantidad = 0;
			bucleAgente: for (int i = 0; i < agentes.length; i++) {
				// Verifica que el agente no haya sido elegido a�n.
				for (int j = 0; j < pos; j++) {
					if (participantes[j].equals(agentes[i])) {
						continue bucleAgente;
					}						
				}
				
				participantes[pos] = agentes[i];
				cantidad += completar(participantes, pos+1);
			}
			return cantidad;
		} else {
			Partida partida = Partida.completa(juego, participantes);
			acumular(partida.actual(), participantes);
			return 1;
		}
	}

	/** Imprime una tabla con las estad�sticas para cada agente. 
	 */
	@Override public String toString() {
		StringBuilder buffer = new StringBuilder();
		buffer.append("Todos contra todos.\n")
			.append("\t#partidas\t#ganadas\t#perdidas\tpromedio\n");
		for (int i = 0; i < agentes.length; i++) {
			buffer.append(String.format("\t%s\n\t%8d\t%8d\t%8d\t%8.4f\n",
				agentes[i], (int)estadisticas[i][0], (int)estadisticas[i][1],
				(int)estadisticas[i][2], estadisticas[i][3]));
		}
		return buffer.toString();
	}
	
////////////////////////////////////////////////////////////////////////////////
	
	/** Prueba r�pida con un torneo de Tateti entre dos jugadores aleatorios. 
	 */
	public static void main(String[] args) throws Exception {
		Torneo torneo = new TorneoTodosContraTodos(50, SquareChess.JUEGO, 
			new AgenteAleatorio(), new AgenteMiniMaxSqChess());
		int cantidadPartidas = torneo.completar();
		System.out.println(torneo);
		System.out.println("Total de partidas = "+ cantidadPartidas);
	}
}
