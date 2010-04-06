package juegos.squareChess;

import juegos.base.Estado;
import juegos.base.Juego;
import juegos.base.Jugador;
import juegos.base.Movimiento;
import juegos.base._Juego;
import juegos.tateti.Tateti;

public class SquareChess extends _Juego {
	public static Juego JUEGO = new SquareChess(7,7);
	
	private final int ancho;
	
	private final int alto;
	
	public SquareChess(int alto, int ancho) {
		super("SquareChess", "Blancas", "Negras");
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
		return new EstadoSqChess(0, 0, tableroVacio(alto, ancho), false);
	}
	
	public class Posicion
	{
		public int x;
		public int y;
		
		public Posicion(int x, int y)
		{
			this.x = x;
			this.y = y;
		}
	}
	
	private class EstadoSqChess implements Estado {
		private final int turno;
		
		private final int[][] tablero;
		
		private final boolean removerPieza;
		
		private final int[] cuadradosIniciales;
		
		private final int jugador;
		
		public EstadoSqChess(int turno, int jugador, int[][] tablero, boolean removerPieza)
		{
			this.turno = turno;
			this.tablero = tablero;
			this.removerPieza = removerPieza;
			this.jugador = jugador;
			this.cuadradosIniciales = new int[jugadores.length];
		}
		
		@Override
		public Juego juego() {
			// TODO Auto-generated method stub
			return SquareChess.this;
		}

		@Override
		public Jugador[] jugadores() {
			return jugadores;
		}

		@Override
		public Movimiento[] movimientos(Jugador jugador) {
			// TODO Auto-generated method stub
			return null;
		}
		
		public class MovimientoSqChess implements Movimiento {
			
			//Casillas de accion
			public final Posicion pos1; //Origen o posicion
			public final Posicion pos2; //Destino
			
			
			public MovimientoSqChess(Posicion pos)
			{
				this.pos1 = pos;
				this.pos2 = new Posicion(-1, -1);
			}
			
			public MovimientoSqChess(Posicion origen, Posicion destino)
			{
				this.pos1 = origen;
				this.pos2 = destino;
			}
			
			@Override
			public Estado estado() {
				return EstadoSqChess.this;
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
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Estado siguiente(Movimiento movimiento) {
			
			MovimientoSqChess movSC = (MovimientoSqChess)movimiento;
			//Se estan colocando las fichas en el tablero
			if (this.turno < tablero.length)
			{
				int[][] tableroSig = this.tablero.clone();
				tableroSig[movSC.pos1.x][movSC.pos1.y] = movSC.indiceJugador();
				int sigJugador = (jugador + 1) % jugadores.length;
				return new EstadoSqChess(this.turno + 1, sigJugador, tableroSig, turnoRemover());
			}
			//Movimientos de solo remover
			else
			{
				
			}
			
			return null;
		}
		
		public int cuadrados(Jugador jugador)
		{
			return 0;
		}
		
		public boolean turnoRemover()
		{
			if (turno < tablero.length)
				return false;
			
			int turnosRemover = tablero.length;
			for (int i = 0; i < cuadradosIniciales.length; i++)
			{
				if (cuadradosIniciales[i] == 0)
					turnosRemover += 1;
				else
					turnosRemover += cuadradosIniciales[i];
			}
			
			if (turno < turnosRemover)
			{
				return true;
			}
			
			return false;
		}
		
		public boolean formaCuadrado(Posicion pos, int jugador)
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
			
			for (int i = 0; i < 8; i+= 2)
			{
				Posicion pos0 = rodeantes[i];
				Posicion pos1 = rodeantes[i+1];
				Posicion pos2 = rodeantes[(i+2) % 8];
				if (tablero[pos0.x][pos0.y] == jugador && tablero[pos1.x][pos1.y] == jugador && tablero[pos2.x][pos2.y] == jugador)
				{
					return true;
				}
			}
			
			return false;
		}
	
	}
}
