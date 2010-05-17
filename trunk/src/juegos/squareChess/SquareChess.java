package juegos.squareChess;

import java.util.ArrayList;

import juegos.Partida;
import juegos.agentes.AgenteConsola;
import juegos.agentes.AgenteMiniMaxSqChess;
import juegos.base.Estado;
import juegos.base.Juego;
import juegos.base.Jugador;
import juegos.base.Movimiento;
import juegos.base._Juego;

/**
 * Lógica del juego Square Chess (Fang Qí)<br><br> 
 * El mismo tiene las siguientes etapas:
 * <li> Etapa 1: Rellenar el tablero intentando formar cuadrados
 * 		bloqueando cuadrados del oponente. 
 * <li> Etapa 2: Quitar tantas fichas del oponente que no formen 
 * 		cuadrado como cantidad de cuadrados formados en la etapa 
 * 		anterior. De no haber formado cuadrados se quita una 
 * 		ficha del oponente.
 * <li> Etapa 3: Cada jugador debe mover una de sus fichas para
 * 		formar cuadrados y proceder a quitarle tantas fichas al 
 * 		oponente como cantidad de nuevos cuadrados formados.<br>
 * <br>El juego finaliza cuando el jugador 1 le quitó todas las
 * fichas al jugador 2. El jugador 1 se declara como ganador. 
 */
public class SquareChess extends _Juego {
	
	/**
	 * Instancia del juego
	 */
	public static Juego JUEGO = new SquareChess(7,8, "Blancas", "Negras");
	
	/**
	 * Ancho del tablero
	 */
	private final int ancho;
	
	/**
	 * Alto del tablero
	 */
	private final int alto;
	
	/**
	 * Determina la cantidad de jugadas seguidas en las cuales
	 * ningún jugador come una ficha de su contrincante.
	 */
	private final int maxMovidasSinComer = 20;
	
	/**
	 * Alfabeto para hacer la conversión de las posiciones
	 * a modo standard.
	 */
	public static final String alfabeto = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	
	/**
	 * Constructor del juego. 
	 * @param alto		Ancho del tablero
	 * @param ancho		Alto del tablero
	 * @param primero	Nombre representativo del primer jugador
	 * @param segundo	Nombre representativo del segundo jugador
	 */
	public SquareChess(int alto, int ancho, String primero, String segundo) 
	{
		super("SquareChess", primero, segundo);
		this.alto = alto;
		this.ancho = ancho;
	}

	/**
	 *  Tablero de prueba para la prueba de las siguientes etapas del juego
	 */
	@SuppressWarnings("unused")
	private final int[][] TABLERO_PRUEBA = {
			{ 0, -1,  0, -1, -1, -1, -1},
			{-1,  0,  0, -1, -1, -1, -1},
			{-1, -1, -1, -1,  1,  1, -1},
			{-1, -1, -1, -1,  1, -1,  1},
			{-1, -1, -1, -1, -1, -1, -1},
			{-1, -1, -1, -1, -1, -1, -1},
			{-1, -1, -1, -1, -1, -1, -1},
			{-1, -1, -1, -1, -1, -1, -1}
			};
	
	/**
	 * Inicialización del tablero 
	 * @param alto	Alto del tablero
	 * @param ancho	Ancho del tablero
	 * @return		Matriz de alto x ancho inicializado (celdas en -1)
	 */
	private static int[][] tableroVacio(int alto, int ancho)
	{
		int[][] tablero = new int[ancho][alto];
		for (int x = 0; x < ancho; x++)
		{
			for (int y = 0; y < alto; y++)
			{
				tablero[x][y] = -1;
			}
		}		
		return tablero;
	}

	@Override
	public Estado inicio(Jugador... jugadores) 
	{
		return new EstadoInitSqChess(0, tableroVacio(alto, ancho), 0);
		//return new EstadoMoverSqChess(300, TABLERO_PRUEBA, 1, 7);
	}
	
	/**
	 * Representación de un estado general del juego Square Chess
	 */
	public class EstadoSqChess implements Estado {

		/**
		 * Número que representa el turno actual
		 */
		protected final int turno;
		
		/**
		 * El tablero del juego
		 */
		protected final int[][] tablero;
		
		/**
		 * Cantidad de movidas sin comer
		 * TODO Acordarse de resetear esta variable en el momento que
		 * 		se sale del bucle
		 */
		protected final int movidasSinComer;
		
		public EstadoSqChess(int turno, int[][] tablero, int movidasSinComer)
		{
			this.turno = turno;
			this.tablero = tablero;
			this.movidasSinComer = movidasSinComer;
		}
		
		public int[][] getTablero()
		{
			return tablero;
		}
		
		@Override
		public Juego juego() 
		{
			return SquareChess.this;
		}

		@Override
		public Jugador[] jugadores() 
		{
			return jugadores;
		}

		@Override
		public Movimiento[] movimientos(Jugador jugador) 
		{
			return new Movimiento[0];
		}

		@Override
		public Double resultado(Jugador jugador) 
		{
			if (turno <= alto*ancho)
				return null;
			
			int idxOponente = jugadores[0].equals(jugador) ? 1 : 0;
			int idxJugador = (idxOponente + 1) % 2;
			
			if (contarFichas(idxOponente) < 4)
			{
				return 1.0;
			}
			else if (contarFichas(idxJugador) < 4)
			{
				return -1.0;
			}
			else if (movidasSinComer > maxMovidasSinComer)
			{
				return 0.0;
			}
			else
				return null;
		}

		@Override
		public Estado siguiente(Movimiento movimiento)
		{
			return null;
		}
		
		protected int celdasVacias()
		{	
			return contarFichas(-1);
		}
		
		/**
		 * Contador de cuadrados completos para un jugador 
		 * @param jugador	índice del jugador
		 * @return			Número de cuadrados que tiene el 
		 * 					jugador con el índice determinado  
		 */
		public int contarCuadrados(int jugador)
		{
			int cuadrados = 0;
			for (int y = 0; y < alto-1; y++) {
				for (int x = 0; x < ancho-1; x++) {
					if (tablero[x][y] == jugador && tablero[x+1][y] == jugador && tablero[x][y+1] == jugador && tablero[x+1][y+1] == jugador)
						cuadrados++;
				}		
			}			
			return cuadrados;
		}
		
		/**
		 * Obtiene la cantidad de cuadrados parciales de un jugador.
	     * Los cuadrados parciales se definen como 3 fichas de un jugador
	     * organizadas de tal manera que solo falta colocar una sola ficha
	     * para formar un cuadrado.
		 * 
		 * @param idxJugador 	índice del jugador a verificar (0 = blanco, 1 = negro)
		 * @return 				Número de cuadrados parciales.
		 */
		public double cuadradosParciales(int idxJugador)
		{
			int idxOponente = idxJugador == 0 ? 1 : 0;			
			int[][] tablero = this.getTablero();
			int ancho = tablero.length;
			int alto = tablero[0].length;
			double val = 0;
			for (int x = 0; x < ancho-1; x++)
			{
				for (int y = 0; y < alto-1; y++)
				{
					if (tablero[x][y] == idxOponente || tablero[x+1][y] == idxOponente || tablero[x][y+1] == idxOponente || tablero[x+1][y+1] == idxOponente)
						continue;
					
					if (tablero[x][y] == idxJugador && tablero[x+1][y] == idxJugador && tablero[x][y+1] == idxJugador && tablero[x+1][y+1] == idxJugador)
					{
						val += 1.0;
						continue;
					}
					
					if (tablero[x][y] == idxJugador)
						val += 0.25;
					
					if (tablero[x+1][y] == idxJugador)
						val += 0.25;
					
					if (tablero[x+1][y+1] == idxJugador)
						val += 0.25;
					
					if (tablero[x][y+1] == idxJugador)
						val += 0.25;
				}
			}			
			return val;
		}

		/**
		 * Obtiene la cantidad de fichas en el tablero según
		 * el tipo de ficha que se desea buscar.
		 * @param tipoFicha	tipo de ficha a buscar 
		 * @return 			Cantidad de fichas correspondiente al tipo de ficha buscado
		 */
		public int contarFichas(int tipoFicha)
		{
			int fichas = 0;
			for (int y = 0; y < alto; y++) {
				for (int x = 0; x < ancho; x++) {
					if (tablero[x][y] == tipoFicha)
						fichas++;
				}		
			}			
			return fichas;
		}
		
		/**
		 * Chequea que se puede formar un cuadrado
		 * @param pos		Posición desde la cuál chequear
		 * @param jugador	Jugador que se desea chequear
		 * @param tablero	Tablero a consultar
		 * @return			True si puede formar un cuadrado, de lo contrario false
		 */
		protected boolean formaCuadrado(Posicion pos, int jugador, int[][] tablero)
		{
			Posicion[] rodeantes = new Posicion[8];
			rodeantes[0] = new Posicion(pos.x - 1, pos.y);
			rodeantes[1] = new Posicion(pos.x - 1, pos.y - 1);
			rodeantes[2] = new Posicion(pos.x, pos.y - 1);
			rodeantes[3] = new Posicion(pos.x + 1, pos.y - 1);
			rodeantes[4] = new Posicion(pos.x + 1, pos.y);
			rodeantes[5] = new Posicion(pos.x + 1, pos.y + 1);
			rodeantes[6] = new Posicion(pos.x, pos.y + 1);
			rodeantes[7] = new Posicion(pos.x - 1, pos.y + 1);			
			for (int i = 0; i < 8; i+= 2) {
				Posicion pos0 = rodeantes[i];
				if (pos0.x < 0 || pos0.x >= ancho || pos0.y < 0 || pos0.y >= alto)
					continue;
				
				Posicion pos1 = rodeantes[i+1];
				if (pos1.x < 0 || pos1.x >= ancho || pos1.y < 0 || pos1.y >= alto)
					continue;
				
				Posicion pos2 = rodeantes[(i+2) % 8];
				if (pos2.x < 0 || pos2.x > ancho || pos2.y < 0 || pos2.y >= alto)
					continue;
				if (tablero[pos0.x][pos0.y] == jugador && tablero[pos1.x][pos1.y] == jugador && tablero[pos2.x][pos2.y] == jugador) {
					return true;
				}
			}			
			return false;
		}
		
		/**
		 * Se obtiene una copia del tablero de esta instancia del juego. 
		 * @return Matriz representando el tablero actual.
		 */
		protected int[][] copiarTablero()
		{
			int[][] copia = new int[ancho][alto];
			for (int x = 0; x < ancho; x++)
			{
				for (int y = 0; y < alto; y++)
				{
					copia[x][y] = tablero[x][y];
				}
			}
			return copia;
		}
		
		@Override
		public String toString()
		{
			String salida = "  ";			
			for (int x = 0; x < alto; x++)
			{
				salida += (x+1) + " ";
			}
			salida += "\n";			
			for (int x = 0; x < ancho; x++) {
				salida += alfabeto.charAt(x) + " ";
				for (int y = 0; y < alto; y++) {
				
					if (tablero[x][y] == -1)
						salida += "-" + " ";
					else
						salida += jugadores[tablero[x][y]].toString().substring(0,1) + " ";
				}
				salida += "\n";
			}			
			if (this.getClass().equals(EstadoInitSqChess.class))
				salida += "\nColocar ficha";
			else if (this.getClass().equals(EstadoMoverSqChess.class))
				salida += "\nMover ficha";
			else if (this.getClass().equals(EstadoRemoverSqChess.class))
				salida += "\nRemover ficha";
			return salida;
		}
		
		public Jugador getJugador()
		{
			return null;
		}
	}
	
	/**
	 * Representación de un estado inicial del juego.
	 * Los estados iniciales pertenecen a la primer etapa del juego.
	 * @see SquareChess
	 */
	public class EstadoInitSqChess extends EstadoSqChess {
		
		public EstadoInitSqChess(int turno, int[][] tablero, int movidasSinComer)
		{
			super(turno, tablero, movidasSinComer);
		}

		@Override
		public Movimiento[] movimientos(Jugador jugador)
		{
			if (!jugador.equals(getJugador()) || resultado(jugador) != null)
				return new Movimiento[0];
			Movimiento[] movs = new Movimiento[celdasVacias()];
			int i = 0;
			for (int y = 0; y < alto; y++) {
				for (int x = 0; x < ancho; x++) {
					if (tablero[x][y] == -1)
					{
						movs[i] = new MovimientoInitSqChess(new Posicion(x, y));
						i++;
					}
				}
					
			}
			return movs;
		}
		
		/**
		 * Representación de un movimiento posible en la primer etapa del juego.
		 */
		public class MovimientoInitSqChess implements Movimiento 
		{
			/**
			 * Posición actual
			 * @see Posicion
			 */
			public final Posicion posicion;
			
			public MovimientoInitSqChess(Posicion pos)
			{
				this.posicion = pos;
			}
			
			@Override
			public Estado estado()
			{
				return EstadoInitSqChess.this;
			}

			@Override
			public Jugador jugador()
			{
				return getJugador();
			}
			
			/**
			 * Índice del jugador actual
			 * @return	Número que representa al jugador actual
			 */
			public int indiceJugador()
			{
				return turno % jugadores.length;
			}
			
			/**
			 * Representación de la posición en String.
			 * Se realiza la conversión de por ej. (0,0) a A1 para
			 * mantener la coordenada en valores standards.
			 */
			@Override
			public String toString()
			{
				int posy = posicion.y + 1;
				return "" + alfabeto.charAt(posicion.x) + posy;
			}			
		}

		@Override
		public Estado siguiente(Movimiento movimiento)
		{
			MovimientoInitSqChess movSC = (MovimientoInitSqChess)movimiento;
			if (this.tablero[movSC.posicion.x][movSC.posicion.y] != -1)
				return this;
				
			int[][] tableroSig = copiarTablero();
			tableroSig[movSC.posicion.x][movSC.posicion.y] = movSC.indiceJugador();
			EstadoSqChess estadoSig = new EstadoInitSqChess(this.turno + 1, tableroSig, 0);
			if (estadoSig.celdasVacias() != 0)
				return estadoSig;
			else
			{
				Estado sig = new EstadoRemoverSqChess(this.turno + 1, tableroSig, (turno + 1) % 2);
				Movimiento[] movs = sig.movimientos(jugadores[0]);
				if (movs == null || movs.length == 0)
				{
					sig = new EstadoRemoverSqChess(this.turno + 1, tableroSig, turno % 2);
				}
				
				return sig;
			}
		}
		
		@Override
		public Jugador getJugador()
		{
			return jugadores[turno % 2];
		}
	}
	
	/**
	 * Representación de un estado de remover fichas en el juego.
	 * Estos estados pertenecen a la segunda etapa del juego. 
	 * @see SquareChess
	 */
	private class EstadoRemoverSqChess extends EstadoSqChess {
		
		/**
		 * Cantidad de fichas a remover inicialmente.
		 * Necesario para cuando se comienza con la segunda etapa.
		 */
		private final int[] removerIniciales;
		
		/**
		 * Turno del jugador actual
		 */
		private final int jugadorTurno;
		
		public EstadoRemoverSqChess(int turno, int[][] tablero, int jugadorTurno)
		{
			super(turno, tablero, 0);
			this.removerIniciales = new int[jugadores.length];
			for (int i = 0; i < jugadores.length; i++)
			{
				int cuads = contarCuadrados(i);
				if (cuads == 0)
					cuads++;
				removerIniciales[i] = cuads;
			}
			
			this.jugadorTurno = jugadorTurno;
		}		

		@Override
		public Movimiento[] movimientos(Jugador jugador)
		{
			if (!jugador.equals(getJugador()) || resultado(jugador) != null)
				return new Movimiento[0];
			
			int idxJugador = 0;
			while (idxJugador < jugadores.length && !jugadores[idxJugador].equals(jugador))
				idxJugador++;
			
			int idxOponente = (idxJugador + 1) % 2;
			
			int fichasOponente = contarFichas(idxOponente);
			Movimiento[] movimientos = new Movimiento[fichasOponente];
			int idxMov = 0;
			for (int y = 0; y < alto; y++) {
				for (int x = 0; x < ancho; x++) {
					if (tablero[x][y] == idxOponente)
					{
						if (!formaCuadrado(new Posicion(x, y), idxOponente, tablero))
						{
							movimientos[idxMov] = new MovimientoRemoverSqChess(new Posicion(x, y));
							idxMov++;
						}
					}
				}
			}
			
			if (idxMov < movimientos.length)
			{
				if (idxMov == 0)
					return new Movimiento[0];
				
				Movimiento[] movsReales = new Movimiento[idxMov];
				for (int i = 0; i < idxMov; i++)
				{
					movsReales[i] = movimientos[i];
				}
				
				return movsReales;
			}
			
			return movimientos;
		}
		
		@Override
		public Double resultado(Jugador jugador)
		{
			if (celdasVacias() == 0)
			{
				for (int x = 0; x < ancho; x++)
				{
					for (int y = 0; y < alto; y++)
					{
						if (!formaCuadrado(new Posicion(x,y), tablero[x][y], tablero))
							return null;
					}
				}
				
				return 0.0;
			}
			
			return super.resultado(jugador);
		}
		
		/**
		 * Representación de un movimiento posible en la segunda etapa del juego.
		 */
		public class MovimientoRemoverSqChess implements Movimiento {
			
			/**
			 * Posición actual
			 * @see Posicion
			 */
			private final Posicion posicion;
			
			public MovimientoRemoverSqChess(Posicion posFicha)
			{
				this.posicion = posFicha;
			}
			
			@Override
			public Estado estado()
			{
				return EstadoRemoverSqChess.this;
			}

			@Override
			public Jugador jugador()
			{
				return jugadores[jugadorTurno];
			}
			
			/**
			 * Representación de la posición en String.
			 * Se realiza la conversión de por ej. (0,0) a A1 para
			 * mantener la coordenada en valores standards.
			 */
			@Override
			public String toString()
			{
				int posy = posicion.y + 1;
				return "" + alfabeto.charAt(posicion.x) + posy;
			}
		}
		
		@Override
		public Jugador getJugador()
		{
			return jugadores[jugadorTurno];
		}
		
		@Override
		public Estado siguiente(Movimiento movimiento)
		{
			MovimientoRemoverSqChess mov = (MovimientoRemoverSqChess)movimiento;
			if (mov == null)
				return this;
			
			int idxOponente = (jugadorTurno+1) % jugadores.length;
			if (tablero[mov.posicion.x][mov.posicion.y] != idxOponente)
				return this;
			
			int[][] tableroSig = copiarTablero();
			tableroSig[mov.posicion.x][mov.posicion.y] = -1;
			
			int turnoFinRemoverInit = alto*ancho + removerIniciales[0] + removerIniciales[1];
			int turnoSig = turno + 1;
			int jugadorSig = 0;
			if (turnoSig < turnoFinRemoverInit)
			{
				if (turnoSig < alto*ancho + removerIniciales[0])
					jugadorSig = 0;
				else
					jugadorSig = 1;
				
				EstadoRemoverSqChess sig = new EstadoRemoverSqChess(turnoSig, tableroSig, jugadorSig);
				Movimiento[] movs = sig.movimientos(jugadores[jugadorSig]);
				
				if (movs == null || movs.length == 0)
				{
					if (jugadorSig == 0)
					{
						sig = new EstadoRemoverSqChess(alto*ancho + removerIniciales[0], tableroSig, 1);
						movs = sig.movimientos(jugadores[1]);
						if (movs == null || movs.length <= 0)
						{
							return new EstadoMoverSqChess(turnoSig, tableroSig, 0, 0);
						}
						return sig;
					}
					else
					{
						return new EstadoMoverSqChess(turnoSig, tableroSig, 0, 0);
					}
				}
				
				return sig;
			}
			else
			{
				jugadorSig = idxOponente;
				EstadoMoverSqChess estadoSig = new EstadoMoverSqChess(turno + 1, tableroSig, jugadorSig, movidasSinComer+1);
				Movimiento[] movs = estadoSig.movimientos(jugadores[idxOponente]);
				if (movs == null || movs.length == 0)
				{
					return new EstadoMoverSqChess(turno + 1, tableroSig, jugadorTurno, 0);
				}
				else
				{
					return estadoSig;
				}
			}
		}
	}
	
	/**
	 * Representación de un estado de mover fichas en el juego.
	 * Estos estados pertenecen a la tercer etapa del juego. 
	 * @see SquareChess
	 */
	private class EstadoMoverSqChess extends EstadoSqChess {
		
		/**
		 * Índice del jugador actual
		 */
		private final int idxJugador;
		
		public EstadoMoverSqChess(int turno, int[][] tablero, int idxJugador, int movidasSinComer)
		{
			super(turno, tablero, movidasSinComer);
			this.idxJugador = idxJugador;
		}
				
		/**
		 * Representación de un movimiento posible en la tercer etapa del juego.
		 */
		public class MovimientoMoverSqChess implements Movimiento {
			
			/**
			 * Posición actual de la ficha
			 * @see Posicion
			 */
			private final Posicion posFicha;
			
			/**
			 * Posición destino de la ficha
			 * @see Posicion
			 */
			private final Posicion destinoFicha;
			
			public MovimientoMoverSqChess(Posicion posFicha, Posicion destinoFicha)
			{
				this.posFicha = posFicha;
				this.destinoFicha = destinoFicha;
			}
			
			@Override
			public Estado estado()
			{
				return EstadoMoverSqChess.this;
			}

			@Override
			public Jugador jugador()
			{
				return jugadores[idxJugador];
			}
			
			@Override
			public String toString()
			{
				return "" + alfabeto.charAt(posFicha.x) + posFicha.y + " a " + alfabeto.charAt(destinoFicha.x) + destinoFicha.y;
			}
		}

		@Override
		public Movimiento[] movimientos(Jugador jugador)
		{
			if (jugador != getJugador() || resultado(jugador) != null)
				return new Movimiento[0];
			
			ArrayList<MovimientoMoverSqChess> movs = new ArrayList<MovimientoMoverSqChess>();
			int idxJugador = 0;
			while (!jugadores[idxJugador].equals(jugador)) {
				idxJugador++;
			}
			
			for (int y = 0; y < alto; y++) {
				for (int x = 0; x < ancho; x++) {
					if (tablero[x][y] == idxJugador) {
						movs.addAll(movimientosFicha(new Posicion(x, y)));
					}
				}
					
			}
			return movs.toArray(new Movimiento[movs.size()]);
		}
		
		/**
		 * Obtener movimientos posibles para mover una ficha. 
		 * @param pos	Posición desde la cual chequear los posibles movimientos a realizar
		 * @return		Lista con todos los movimientos posibles
		 */
		private ArrayList<MovimientoMoverSqChess> movimientosFicha(Posicion pos)
		{
			ArrayList<MovimientoMoverSqChess> aMovs = new ArrayList<MovimientoMoverSqChess>();
			//Derecha
			Posicion sigPos = pos.mover(1, 0);
			while (sigPos.x < ancho && tablero[sigPos.x][sigPos.y] == -1)
			{
				MovimientoMoverSqChess mov = new MovimientoMoverSqChess(pos, sigPos);
				aMovs.add(mov);
				sigPos = sigPos.mover(1, 0);
			}
			
			//Izquierda
			sigPos = pos.mover(-1, 0);
			while (sigPos.x >= 0 && tablero[sigPos.x][sigPos.y] == -1)
			{
				MovimientoMoverSqChess mov = new MovimientoMoverSqChess(pos, sigPos);
				aMovs.add(mov);
				sigPos = sigPos.mover(-1, 0);
			}
			
			//Abajo
			sigPos = pos.mover(0, 1);
			while (sigPos.y < alto && tablero[sigPos.x][sigPos.y] == -1)
			{
				MovimientoMoverSqChess mov = new MovimientoMoverSqChess(pos, sigPos);
				aMovs.add(mov);
				sigPos = sigPos.mover(0, 1);
			}
			
			//Arriba
			sigPos = pos.mover(0, -1);
			while (sigPos.y >= 0 && tablero[sigPos.x][sigPos.y] == -1)
			{
				MovimientoMoverSqChess mov = new MovimientoMoverSqChess(pos, sigPos);
				aMovs.add(mov);
				sigPos = sigPos.mover(0, -1);
			}
			
			return aMovs;
		}

		@Override
		public Estado siguiente(Movimiento movimiento)
		{
			MovimientoMoverSqChess mov = (MovimientoMoverSqChess)movimiento;
			
			if (tablero[mov.posFicha.x][mov.posFicha.y] != idxJugador)
				return this;
			
			Posicion[] trayectoria = Posicion.linea(mov.posFicha, mov.destinoFicha);
			for (int i = 1; i < trayectoria.length; i++)
			{
				Posicion temp = trayectoria[i];
				if (tablero[temp.x][temp.y] != -1)
					return this;
			}
			
			int[][] tableroSig = copiarTablero();
			tableroSig[mov.posFicha.x][mov.posFicha.y] = -1;
			tableroSig[mov.destinoFicha.x][mov.destinoFicha.y] = idxJugador;
			if (formaCuadrado(mov.destinoFicha, idxJugador, tableroSig))
			{
				
				EstadoRemoverSqChess estadoSig = new EstadoRemoverSqChess(turno + 1, tableroSig, idxJugador);
				Movimiento[] movs = estadoSig.movimientos(jugadores[idxJugador]);
				if (movs == null || movs.length == 0)
				{
					EstadoMoverSqChess otroEstSig = new EstadoMoverSqChess(turno + 1, tableroSig, (idxJugador+1) % 2, movidasSinComer+1);
					movs = otroEstSig.movimientos(jugadores[(idxJugador+1) % 2]);
					if (movs == null || movs.length == 0)
					{
						return new EstadoMoverSqChess(turno + 1, tableroSig, idxJugador, movidasSinComer+1);
					}
					return otroEstSig;	
				}
				else return estadoSig;
			}
			else
			{
				int jugadorSig = (idxJugador+1) % 2;
				EstadoSqChess estadoSig = new EstadoMoverSqChess(turno + 1, tableroSig, jugadorSig, movidasSinComer+1);
				Movimiento[] movs = estadoSig.movimientos(jugadores[jugadorSig]);
				if (movs == null || movs.length == 0)
				{
					return new EstadoMoverSqChess(turno + 1, tableroSig, idxJugador, movidasSinComer+1);
				}
				else
					return estadoSig;
			}
		}
		
		@Override
		public Jugador getJugador()
		{
			return jugadores[idxJugador];
		}
	}
	
	public static void main(String[] args) throws Exception 
	{
		System.out.println(Partida.completa(SquareChess.JUEGO, 
				new AgenteConsola(), 
				new AgenteMiniMaxSqChess()
				//new AgenteAleatorio()
			).toString());
	}
}
