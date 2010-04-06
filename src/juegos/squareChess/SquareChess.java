package juegos.squareChess;

import juegos.base.Estado;
import juegos.base.Juego;
import juegos.base.Jugador;
import juegos.base.Movimiento;
import juegos.base._Juego;

public class SquareChess extends _Juego {
	public static Juego JUEGO = new SquareChess(7,7, "Blancas", "Negras");
	
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
			// TODO Auto-generated method stub
			return null;
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
				return new EstadoRemoverSqChess(this.turno + 1, tableroSig);
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
		
		public EstadoRemoverSqChess(int turno, int[][] tablero)
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
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Estado siguiente(Movimiento movimiento) {
			// TODO Auto-generated method stub
			return null;
		}
	}
}
