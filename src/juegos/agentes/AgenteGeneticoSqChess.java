package juegos.agentes;

import juegos.base.Agente;
import juegos.base.Estado;
import juegos.base.Jugador;
import juegos.base.Movimiento;
import juegos.squareChess.SquareChess.EstadoSqChess;
import juegos.squareChess.SquareChess.EstadoInitSqChess;;

public class AgenteGeneticoSqChess implements Agente {
	
	// Jugador que representa este agente
	private Jugador jugador;

	// Profundidad máxima del árbol minimax
	private int profundidadMax = 3;
	
	// Factor de la función heurística que representa la 
	// cantidad de fichas del jugador y del oponente (op)
	private double fFichas;
	private double fFichasOp;
	
	// Factor de la función heurística que representa la cantidad
	// de cuadrados parciales del jugador y del oponente (op)
	private double fCuadradosParciales;
	private double fCuadradosParcialesOp;
	
	// Factor de la función heurística que representa la 
	// cantidad de cuadrados del jugador y del oponente (op)
	private double fCuadrados;
	private double fCuadradosOp;
	
	public AgenteGeneticoSqChess() {
		fFichas = 1;
		fFichasOp = -1;
		fCuadradosParciales = 1;
		fCuadradosParcialesOp = -1;
		fCuadrados = 1;
		fCuadradosOp = -1;
	}
	
	public AgenteGeneticoSqChess(double[] factores) {
		fFichas = factores[0];
		fFichasOp = factores[1];
		fCuadradosParciales = factores[2];
		fCuadradosParcialesOp = factores[3];
		fCuadrados = factores[4];
		fCuadradosOp = factores[5];
	}

	@Override
	public void comienzo(Jugador jugador, Estado estado) {
		this.jugador = jugador;		
	}

	@Override
	public Movimiento decision(Estado estado) {
		//long init = System.currentTimeMillis();
		AlfaBeta alfaBeta = minimax(estado, new AlfaBeta(-100.0), new AlfaBeta(100.0));
		//System.out.println("AB = " + alfaBeta.valor);  // Descomentar para verificar el valor de alfa/beta del mov elegido
		//long fin = System.currentTimeMillis();
		//double deltaSecs = (fin - init) / 100.0;
		//System.out.println("Tiempo decision = " + deltaSecs + " secs.");
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
			double val = 0.0;
			val = heurística(estado, idxJugador);
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
		int idxOponente = idxJugador == 0 ? 1 : 0;
		EstadoSqChess estSqChess = (EstadoSqChess)estado;
		
		double valHeuristica = 0;
		
		//En el estado inicial no importa la cantidad de fichas, así que se ignora
		if (!estSqChess.getClass().equals(EstadoInitSqChess.class))
		{
			valHeuristica += fFichas * estSqChess.contarFichas(idxJugador);
			valHeuristica += fFichasOp * estSqChess.contarFichas(idxOponente);
		}
		valHeuristica += fCuadrados * estSqChess.contarCuadrados(idxJugador);
		valHeuristica += fCuadradosOp * estSqChess.contarCuadrados(idxOponente);
		valHeuristica += fCuadradosParciales * estSqChess.cuadradosParciales(idxJugador);
		valHeuristica += fCuadradosParcialesOp * estSqChess.cuadradosParciales(idxOponente);
		
		return valHeuristica;
	}
	
	public String toString()
	{
		return "[" + fFichas + "/" + fFichasOp + ", " + 
			   fCuadradosParciales + "/" + fCuadradosParcialesOp + ", " +
			   fCuadrados + "/" + fCuadradosOp + "]";
	}

	@Override
	public void fin(Estado estado){}

	@Override
	public Jugador jugador() 
	{
		return jugador;
	}

	@Override
	public void movimiento(Movimiento movimiento, Estado estado){}
	
	// Representa un nodo del arbol Minimax con poda Alfa-Beta
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
