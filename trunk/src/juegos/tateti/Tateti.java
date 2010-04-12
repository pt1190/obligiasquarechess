package juegos.tateti;

import juegos.Partida;
import juegos.agentes.*;
import juegos.base.*;

/** Tatetí (o tres en línea), implementado con las interfaces del framework, a 
 *  modo de ejemplo.  
 */
public class Tateti extends _Juego {
	public static Juego JUEGO = new Tateti();
	
	private Tateti() {
		super("Tateti", "Xs", "Os");
	}

	@Override public Estado inicio(Jugador... jugadores) {
		return new EstadoTateti(0);
	}
	
	/*  Todo el estado de juego del tateti se puede codificar en un entero de 
	 *  32 bits, siguiendo el siguiente esquema. Las casillas del tablero se 
	 *  enumeran y luego se utilizan bits del int para indicar la ocupación de 
	 *  cada casilla por cada jugador.
	 *  
	 *  Los 9 bits menos significativos se asignan a las ocupaciones de casillas 
	 *  de Xs, y los siguientes 9 bits a las ocupaciones de Os. El bit número 19 
	 *  está en 0 si juegan las Xs y en 1 si juegan las Os.
	 *  
	 *  La verificación de si algún bando ha obtenido tres en línea se resume a
	 *  verificar 8 máscaras de bits.
	 */
	
	private static final boolean empate(int tablero) {
		return (tablero & 0x1FF | (tablero >> 9) & 0x1FF) == 0x1FF;
	}
	
	private static final int[] LINEAS = { 
		0x7, 0x38, 0x1C0, 0x49, 0x92, 0x124, 0x54, 0x111 
	};
	private static final boolean gana(int tablero) {
		for (int mascara : LINEAS) {
			if ((tablero & mascara) == mascara) {
				return true;
			}
		}
		return false;
	}

	private static final char casilla(int tablero, int casilla) {
		casilla = 1 << casilla;
		return (tablero & casilla) != 0 ? 'X' : 
			((tablero >> 9) & casilla) != 0 ? 'O' : ' '; 
	}
	
	// Coordenadas de las casillas, para usar como nombres de movimientos.
	private final String[] CASILLAS = {"A1","B1","C1","A2","B2","C2","A3","B3","C3"};
	
	private class EstadoTateti implements Estado {
		public final int tablero;
		
		public EstadoTateti(int tablero) {
			this.tablero = tablero;
		}
		
		@Override public Juego juego() {
			return Tateti.this;
		}
		
		@Override public Jugador[] jugadores() {
			return jugadores;
		}
		
		@Override public Estado siguiente(Movimiento movimiento) {
			int mascara = ((MovimientoTateti)movimiento).mascara;
			return new EstadoTateti((tablero | mascara) ^ 0x40000);
		}

		@Override public Movimiento[] movimientos(Jugador jugador) {
			// Si el jugador no es el que mueve, o ha terminado la partida, retorna null.
			int jugadorHabilitado = tablero >> 18;
			if (!jugador.equals(jugadores[jugadorHabilitado]) 
					|| resultado(jugadorHabilitado) != null) {
				return null;
			}
			int ocupadas = (tablero & 0x1FF) | ((tablero >> 9) & 0x1FF);
			boolean jueganXs = (tablero & 0x40000) == 0;

			// Cuenta la cantidad de movimientos.
			int length = 0;
			int mascara = 1;
			for (int i = 0; i < 9; i++) {
				if ((mascara & ocupadas) == 0) {
					length++;
				}
				mascara = mascara << 1;
			}
			
			// Construye el vector de movimientos.
			Movimiento[] movs = new Movimiento[length];
			mascara = 1;
			for (int i = 0, j = 0; i < 9; i++) {
				if ((mascara & ocupadas) == 0) {
					movs[j++] = new MovimientoTateti(jueganXs ? mascara : (mascara << 9));
				}
				mascara = mascara << 1;
			}
			return movs;
		}

		private Double resultado(int jugador) {
			if (gana(tablero & 0x1FF)) { // ganan las Xs
				return jugador == 0 ? 1.0 : -1.0;
			}
			if (gana((tablero >> 9) & 0x1FF)) { // ganan las Os
				return jugador == 1 ? 1.0 : -1.0;
			}
			if (empate(tablero)) {
				return 0.0;
			}
			return null;
		}
		
		@Override public Double resultado(Jugador jugador) {
			return 	resultado(jugador.equals(jugadores[0]) ? 0 : 1);
		}
		
		public class MovimientoTateti implements Movimiento {
			public final int mascara;
			
			public MovimientoTateti(int mascara) {
				this.mascara = mascara;
			}

			@Override public Estado estado() {
				return EstadoTateti.this;
			}
		
			@Override public Jugador jugador() {
				return jugadores[tablero >> 18];
			}

			@Override public String toString() {
				int m = mascara;
				if (m > 0x1FF) {
					m = m >> 9;
				}
				int i = 0;
				for (; m > 1; i++) {
					m = m >> 1;
				}
				return CASILLAS[i];
			}
		}
		
		@Override public String toString() {
			StringBuilder buffer = new StringBuilder();
			buffer.append(casilla(tablero, 0)).append('|')
				.append(casilla(tablero, 1)).append('|')
				.append(casilla(tablero, 2)).append("\n-+-+-\n")
				.append(casilla(tablero, 3)).append('|')
				.append(casilla(tablero, 4)).append('|')
				.append(casilla(tablero, 5)).append("\n-+-+-\n")
				.append(casilla(tablero, 6)).append('|')
				.append(casilla(tablero, 7)).append('|')
				.append(casilla(tablero, 8));
			return buffer.toString();
		}
	}
	
	public static void main(String[] args) throws Exception {
		System.out.println(Partida.completa(Tateti.JUEGO, 
				new AgenteConsola(), 
				new AgenteMiniMaxTateti()
				//new AgenteAleatorio()
			).toString());
	}
}
