package juegos.squareChess;

import java.util.ArrayList;

import juegos.base.Estado;
import juegos.base.Juego;
import juegos.base.Jugador;
import juegos.base.Movimiento;
import juegos.base._Juego;

public class SquareChess extends _Juego {
	public static Juego JUEGO = new SquareChess(7,8, "Blancas", "Negras");
	
	private final int ancho;
	
	private final int alto;
	
	public SquareChess(int alto, int ancho, String primero, String segundo) {
		super("SquareChess", primero, segundo);
		this.alto = alto;
		this.ancho = ancho;
	}

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
		return new EstadoInitSqChess(0, tableroVacio(alto, ancho));
	}
	
	private class EstadoInitSqChess implements Estado {
		private final int turno;
		
		private final int[][] tablero;
		
		public EstadoInitSqChess(int turno, int[][] tablero) {
			this.turno = turno;
			this.tablero = tablero;
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
			return null;
		}
		
		private int celdasVacias()
		{
			int vacias = 0;
		
			for (int y = 0; y < alto; y++) {
				for (int x = 0; x < ancho; x++) {
					if (tablero[x][y] == -1)
						vacias++;
				}
					
			}
			
			return vacias;
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
				return jugadores[turno % jugadores.length];
			}
			
			public int indiceJugador() {
				return turno % jugadores.length;
			}
			
		}
		@Override
		public Double resultado(Jugador jugador) {
			return 0.0;
		}

		@Override
		public Estado siguiente(Movimiento movimiento) {
			MovimientoInitSqChess movSC = (MovimientoInitSqChess)movimiento;
			if (this.tablero[movSC.posicion.x][movSC.posicion.y] != -1)
				return this;
				
			int[][] tableroSig = this.tablero.clone();
			tableroSig[movSC.posicion.x][movSC.posicion.y] = movSC.indiceJugador();
			if (celdasVacias() != 0)
				return new EstadoInitSqChess(this.turno + 1, tableroSig);
			else
				return new EstadoRemoverSqChess(this.turno + 1, tableroSig, 0);
		}
		
		public int cuadrados(Jugador jugador) {
			return 0;
		}
		
		public String toString() {
			String salida = "";
			for (int y = 0; y < alto; y++) {
				for (int x = 0; x < ancho; x++) {
					salida += tablero[x][y] + " ";
				}
				if (y < alto -1)
					salida += "\n";
			}
			return salida;
		}
	
	}
	
	private class EstadoRemoverSqChess implements Estado {

		private final int turno;
		
		private final int[][] tablero;
		
		private final int[] removerIniciales;
		
		private final int jugadorTurno;
		
		public EstadoRemoverSqChess(int turno, int[][] tablero, int jugadorTurno)
		{
			this.turno = turno;
			this.tablero = tablero;
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
		
		private int contarCuadrados(int jugador)
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
		
		private int contarFichas(int ficha)
		{
			int fichas = 0;
			for (int y = 0; y < alto; y++) {
				for (int x = 0; x < ancho; x++) {
					if (tablero[x][y] == ficha)
						fichas++;
				}		
			}
			
			return fichas;
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
						movimientos[idxMov] = new MovimientoRemoverSqChess(new Posicion(x, y));
					}
				}
					
			}
			
			return null;
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
		}
		
		@Override
		public Double resultado(Jugador jugador) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Estado siguiente(Movimiento movimiento) {
			MovimientoRemoverSqChess mov = (MovimientoRemoverSqChess)movimiento;
			if (mov == null)
				return this;
			
			int idxOponente = (jugadorTurno+1) % jugadores.length;
			if (tablero[mov.posicion.x][mov.posicion.y] != idxOponente)
				return this;
			
			int[][] tableroSig = tablero.clone();
			tableroSig[mov.posicion.x][mov.posicion.y] = jugadorTurno;
			
			int turnoFinRemoverInit = alto*ancho + removerIniciales[0] + removerIniciales[1];
			int turnoSig = turno + 1;
			int jugadorSig = 0;
			if (turnoSig < turnoFinRemoverInit)
			{
				if (turnoSig < alto*ancho + removerIniciales[0])
					jugadorSig = 0;
				else
					jugadorSig = 1;
				
				return new EstadoRemoverSqChess(turnoSig, tableroSig, jugadorSig);
			}
			else
			{
				jugadorSig = idxOponente;
				// TODO return new EstadoMoverSqChess;
			}
			return null;
		}
	}
	private class EstadoMoverSqChess implements Estado {
		
		private final int turno;
		
		private final int[][] tablero;
		
		private final int idxJugador;
		
		public EstadoMoverSqChess(int turno, int[][] tablero, int idxJugador)
		{
			this.turno = turno;
			this.tablero = tablero;
			this.idxJugador = idxJugador;
		}
		
		@Override
		public Juego juego() {
			return SquareChess.this;
		}

		@Override
		public Jugador[] jugadores() {
			return jugadores;
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
			
		}

		@Override
		public Movimiento[] movimientos(Jugador jugador) {
			
			ArrayList movs = new ArrayList();
			
			for (int y = 0; y < alto; y++) {
				for (int x = 0; x < ancho; x++) {
					
				}
					
			}
			return null;
		}
		
		private Movimiento[] movimientosFicha(Posicion pos)
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
			while (sigPos.x > 0 && tablero[sigPos.x][sigPos.y] == -1)
			{
				MovimientoMoverSqChess mov = new MovimientoMoverSqChess(pos, sigPos);
				aMovs.add(mov);
				sigPos = sigPos.mover(-1, 0);
			}
			
			//Arriba
			sigPos = pos.mover(0, 1);
			while (sigPos.y < alto && tablero[sigPos.x][sigPos.y] == -1)
			{
				MovimientoMoverSqChess mov = new MovimientoMoverSqChess(pos, sigPos);
				aMovs.add(mov);
				sigPos = sigPos.mover(0, 1);
			}
			
			//Abajo
			sigPos = pos.mover(0, -1);
			while (sigPos.y > 0 && tablero[sigPos.x][sigPos.y] == -1)
			{
				MovimientoMoverSqChess mov = new MovimientoMoverSqChess(pos, sigPos);
				aMovs.add(mov);
				sigPos = sigPos.mover(0, -1);
			}
			
			MovimientoMoverSqChess[] movimientos = aMovs.toArray(new MovimientoMoverSqChess[aMovs.size()]);
			return movimientos;
		}

		@Override
		public Double resultado(Jugador jugador) {
			// Solo termina luego de remover la ultima ficha
			return null;
		}

		@Override
		public Estado siguiente(Movimiento movimiento) {
			// TODO Auto-generated method stub
			return null;
		}
		
	}
}
