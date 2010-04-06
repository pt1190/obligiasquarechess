package juegos.squareChess;

import juegos.base.Estado;
import juegos.base.Juego;
import juegos.base.Jugador;
import juegos.base.Movimiento;
import juegos.base._Juego;
import juegos.tateti.Tateti;

public class SquareChess extends _Juego {
	public static Juego JUEGO = new SquareChess();
	
	public SquareChess() {
		super("SquareChess", "Blancas", "Negras");
	}

	private static char[] tableroVacio = { 
		'0','0','0','0','0','0','0',
		'0','0','0','0','0','0','0',
		'0','0','0','0','0','0','0',
		'0','0','0','0','0','0','0',
		'0','0','0','0','0','0','0',
		'0','0','0','0','0','0','0',
		'0','0','0','0','0','0','0',
	};

	@Override
	public Estado inicio(Jugador... jugadores) {
		return new EstadoInitSqChess(0, tableroVacio);
	}
	
	private final String[] CASILLAS = {
		"A1","B1","C1","D1","E1","F1","G1",
		"A2","B2","C2","D2","E2","F2","G2",
		"A3","B3","C3","D3","E3","F3","G3",
		"A4","B4","C4","D4","E4","F4","G4",
		"A5","B5","C5","D5","E5","F5","G5",
		"A6","B6","C6","D6","E6","F6","G6",
		"A7","B7","C7","D7","E7","F7","G7",
		"A8","B8","C8","D8","E8","F8","G8"
	};
	
	// Representa los estados que constituyen la etapa inicial del juego
	// donde se debe rellenar el tablero
	private class EstadoInitSqChess implements Estado {
		private final int turno;
		
		private final char[] tablero;
		
		public EstadoInitSqChess(int turno, char[] tablero)
		{
			this.turno = turno;
			this.tablero = tablero;
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

			@Override
			public Estado estado() {
				return EstadoInitSqChess.this;
			}

			@Override
			public Jugador jugador() {
				return jugadores[turno % 2];
			}
			
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
	
	// Representa los estados que constituyen la etapa inicial del juego
	// donde se debe remover fichas
	private class EstadoRemoveSqChess implements Estado {
		private final int turno;
		
		private final char[] tablero;
		
		public EstadoRemoveSqChess(int turno, char[] tablero)
		{
			this.turno = turno;
			this.tablero = tablero;
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

			@Override
			public Estado estado() {
				return EstadoRemoveSqChess.this;
			}

			@Override
			public Jugador jugador() {
				return jugadores[turno % 2];
			}
			
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
	
	// Representa los estados que constituyen la etapa inicial del juego
	// donde se debe mover fichas
	private class EstadoMoveSqChess implements Estado {
		private final int turno;
		
		private final char[] tablero;
		
		public EstadoMoveSqChess(int turno, char[] tablero)
		{
			this.turno = turno;
			this.tablero = tablero;
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

			@Override
			public Estado estado() {
				return EstadoMoveSqChess.this;
			}

			@Override
			public Jugador jugador() {
				return jugadores[turno % 2];
			}
			
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
