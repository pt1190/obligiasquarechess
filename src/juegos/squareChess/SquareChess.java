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

public class SquareChess extends _Juego {
	public static Juego JUEGO = new SquareChess(7,8, "Blancas", "Negras");
	
	private final int ancho;
	
	private final int alto;
	
	private final int maxMovidasSinComer = 20;
	
	public final String alfabeto = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	
	public SquareChess(int alto, int ancho, String primero, String segundo) {
		super("SquareChess", primero, segundo);
		this.alto = alto;
		this.ancho = ancho;
	}

	// Tablero de prueba para la prueba de las siguientes etapas del juego
	private final int[][] TABLERO_PRUEBA = {
			{0, -1,  0, -1, -1, -1, -1},
			{-1,  0,  0, -1, -1, -1, -1},
			{-1, -1, -1, -1,  1,  1, -1},
			{-1, -1, -1, -1,  1, -1, 1},
			{-1, -1, -1, -1, -1, -1, -1},
			{-1, -1, -1, -1, -1, -1, -1},
			{-1, -1, -1, -1, -1, -1, -1},
			{-1, -1, -1, -1, -1, -1, -1},
			};
	
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
	public Estado inicio(Jugador... jugadores) {
		return new EstadoInitSqChess(0, tableroVacio(alto, ancho), 0);
		//return new EstadoMoverSqChess(300, TABLERO_PRUEBA, 1, 7);
	}
	
	public class EstadoSqChess implements Estado {

		protected final int turno;
		
		protected final int[][] tablero;
		
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
		public Juego juego() {
			return SquareChess.this;
		}

		@Override
		public Jugador[] jugadores() {
			return jugadores;
		}

		@Override
		public Movimiento[] movimientos(Jugador jugador) {
			return null;
		}

		@Override
		public Double resultado(Jugador jugador) {
			if (turno < alto*ancho)
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
		public Estado siguiente(Movimiento movimiento) {
			return null;
		}
		
		protected int celdasVacias()
		{	
			return contarFichas(-1);
		}
		
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
		
		protected boolean formaCuadrado(Posicion pos, int jugador, int[][] tablero) {
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
		public String toString() {
			String salida = "";
			for (int x = 0; x < ancho; x++) {
				for (int y = 0; y < alto; y++) {
				
					if (tablero[x][y] == -1)
						salida += "-" + " ";
					else
						salida += jugadores[tablero[x][y]].toString().substring(0,1) + " ";
				}
				if (x < ancho -1)
					salida += "\n";
			}
			return salida;
		}
		
		public Jugador getJugador()
		{
			return null;
		}
	}
	
	public class EstadoInitSqChess extends EstadoSqChess {
		
		public EstadoInitSqChess(int turno, int[][] tablero, int movidasSinComer) {
			super(turno, tablero, movidasSinComer);
		}

		@Override
		public Movimiento[] movimientos(Jugador jugador) {
			if (!jugador.equals(getJugador()) || resultado(jugador) != null)
				return null;
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
		
		public class MovimientoInitSqChess implements Movimiento {
			
			//Casillas de accion
			public final Posicion posicion; //Origen o posicion
			
			public MovimientoInitSqChess(Posicion pos) {
				this.posicion = pos;
			}
			
			@Override
			public Estado estado() {
				return EstadoInitSqChess.this;
			}

			@Override
			public Jugador jugador() {
				return getJugador();
			}
			
			public int indiceJugador() {
				return turno % jugadores.length;
			}
			
			@Override
			public String toString()
			{
				int posy = posicion.y + 1;
				return "" + alfabeto.charAt(posicion.x) + posy;
			}			
		}

		@Override
		public Estado siguiente(Movimiento movimiento) {
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
				if (sig.movimientos(jugadores[0]) == null)
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
	
	private class EstadoRemoverSqChess extends EstadoSqChess {
		
		private final int[] removerIniciales;
		
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
		public Movimiento[] movimientos(Jugador jugador) {
			if (!jugador.equals(getJugador()) || resultado(jugador) != null)
				return null;
			
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
					return null;
				
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
		
		public class MovimientoRemoverSqChess implements Movimiento {
			
			private final Posicion posicion;
			
			public MovimientoRemoverSqChess(Posicion posFicha)
			{
				this.posicion = posFicha;
			}
			
			@Override
			public Estado estado() {
				return EstadoRemoverSqChess.this;
			}

			@Override
			public Jugador jugador() {
				return jugadores[jugadorTurno];
			}
			
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
		public Estado siguiente(Movimiento movimiento) {
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
				if (sig.movimientos(jugadores[jugadorSig]) == null)
				{
					if (jugadorSig == 0)
					{
						sig = new EstadoRemoverSqChess(alto*ancho + removerIniciales[0], tableroSig, 1);
						if (sig.movimientos(jugadores[1]) == null)
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
				if (estadoSig.movimientos(jugadores[idxOponente]) == null)
					return new EstadoMoverSqChess(turno + 1, tableroSig, jugadorTurno, 0);
				else
					return estadoSig;
			}
		}
	}
	
	private class EstadoMoverSqChess extends EstadoSqChess {
		
		private final int idxJugador;
		
		public EstadoMoverSqChess(int turno, int[][] tablero, int idxJugador, int movidasSinComer)
		{
			super(turno, tablero, movidasSinComer);
			this.idxJugador = idxJugador;
		}
				
		public class MovimientoMoverSqChess implements Movimiento {
			
			private final Posicion posFicha;
			
			private final Posicion destinoFicha;
			
			public MovimientoMoverSqChess(Posicion posFicha, Posicion destinoFicha)
			{
				this.posFicha = posFicha;
				this.destinoFicha = destinoFicha;
			}
			
			@Override
			public Estado estado() {
				return EstadoMoverSqChess.this;
			}

			@Override
			public Jugador jugador() {
				return jugadores[idxJugador];
			}
			
			@Override
			public String toString()
			{
				return "mover " + alfabeto.charAt(posFicha.x) + (int)posFicha.y + " a " + alfabeto.charAt(destinoFicha.x) + (int)destinoFicha.y;
			}
		}

		@Override
		public Movimiento[] movimientos(Jugador jugador) {
			if (jugador != getJugador() || resultado(jugador) != null)
				return null;
			
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
		public Estado siguiente(Movimiento movimiento) {
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
				if (estadoSig.movimientos(jugadores[idxJugador]) == null)
				{
					return new EstadoMoverSqChess(turno + 1, tableroSig, (idxJugador+1) % 2, movidasSinComer+1);
				}
				else return estadoSig;
			}
			else
			{
				int jugadorSig = (idxJugador+1) % 2;
				EstadoSqChess estadoSig = new EstadoMoverSqChess(turno + 1, tableroSig, jugadorSig, movidasSinComer+1);
				if (estadoSig.movimientos(jugadores[jugadorSig]) == null)
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
	
	public static void main(String[] args) throws Exception {
		System.out.println(Partida.completa(SquareChess.JUEGO, 
				new AgenteConsola(), 
				new AgenteMiniMaxSqChess()
				//new AgenteAleatorio()
			).toString());
	}
}
