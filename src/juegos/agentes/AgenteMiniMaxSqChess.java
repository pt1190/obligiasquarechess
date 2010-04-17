package juegos.agentes;

import juegos.base.Agente;
import juegos.base.Estado;
import juegos.base.Jugador;
import juegos.base.Movimiento;
import juegos.squareChess.SquareChess.EstadoSqChess;
import juegos.squareChess.SquareChess.EstadoInitSqChess;

public class AgenteMiniMaxSqChess implements Agente {
	
	private Jugador jugador;

	private int profundidadMax = 3;
	
	public AgenteMiniMaxSqChess() {
	}

	@Override
	public void comienzo(Jugador jugador, Estado estado) {
		this.jugador = jugador;		
	}

	@Override
	public Movimiento decision(Estado estado) {
		AlfaBeta alfaBeta = minimax(estado, new AlfaBeta(-100.0), new AlfaBeta(100.0));
		System.out.println("AB = " + alfaBeta.valor);
		return alfaBeta.getMov();
	}
	
	private AlfaBeta minimax(Estado estado, AlfaBeta alfa, AlfaBeta beta)	
	{
		EstadoSqChess estSqChess = (EstadoSqChess)estado;
		Double res = estado.resultado(jugador);
		if (res != null)
		{
			return new AlfaBeta(res);
		}
		if (alfa.profundidad >= profundidadMax)
		{
			int idxJugador = estado.jugadores()[0].equals(jugador) ? 0 : 1;
			int idxOponente = idxJugador == 0 ? 1 : 0;
			double val = 0.0;
			val = (heurística(estado, idxJugador) - heurística(estado, idxOponente)) / 18.0;
			return new AlfaBeta(val);
		}
		
		Jugador jugadorEstado = estSqChess.getJugador();
		
		if (jugador.equals(jugadorEstado))
		{
			Movimiento[] movs = estado.movimientos(jugador);
			AlfaBeta ab;
			for (Movimiento mov : movs)
			{
				ab = minimax(estado.siguiente(mov), alfa.sig(), beta.sig());
				ab.mov = mov;
				if (ab.valor > alfa.valor  || (ab.valor == alfa.valor && ab.profundidad < alfa.profundidad))
					alfa = ab;
				if (alfa.valor >= beta.valor)
					return alfa;
			}
			return alfa;
		}
		else
		{
			Movimiento[] movs = estado.movimientos(jugadorEstado);
			AlfaBeta ab;
			for (Movimiento mov : movs)
			{
				ab = minimax(estado.siguiente(mov), alfa.sig(), beta.sig());
				ab.mov = mov;
				if (ab.valor < beta.valor  || (ab.valor == beta.valor && ab.profundidad < beta.profundidad))
					beta = ab;
				if (alfa.valor > beta.valor)
					return beta;
			}
			
			return beta;
		}
	}
	
	private double heurística(Estado estado, int idxJugador)
	{
		double val = 0.0;
		EstadoSqChess estSqChess = (EstadoSqChess)estado;
		
		int idxOponente = idxJugador == 0 ? 1 : 0;
		
		int[][] tablero = estSqChess.getTablero();
		int ancho = tablero.length;
		int alto = tablero[0].length;
		
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

	@Override
	public void fin(Estado estado) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Jugador jugador() {
		return jugador;
	}

	@Override
	public void movimiento(Movimiento movimiento, Estado estado) {
		// TODO Auto-generated method stub
		
	}
	
	public class AlfaBeta {
		
		private Movimiento mov;
		private Double valor; // Valor correspondiente al estado final contenido en el movimiento
		private int profundidad;
		
		public Movimiento getMov() {
			return mov;
		}

		public void setMov(Movimiento mov) {
			this.mov = mov;
		}

		public Double getValor() {
			return valor;
		}

		public void setValor(Double valor) {
			this.valor = valor;
		}
		
		public AlfaBeta(Double v, Movimiento m, int prof){
			valor = v;
			mov = m;
			profundidad = prof;
		}
		
		public AlfaBeta(Double v){
			valor = v;
			mov = null;
			profundidad = 1;
		}
		
		public AlfaBeta sig()
		{
			return new AlfaBeta(this.valor, this.mov, this.profundidad+1);
		}

	}
}
