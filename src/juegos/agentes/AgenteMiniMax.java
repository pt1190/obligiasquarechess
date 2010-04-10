package juegos.agentes;

import juegos.base.Agente;
import juegos.base.Estado;
import juegos.base.Jugador;
import juegos.base.Movimiento;

public class AgenteMiniMax implements Agente {
	
	private Jugador jugador;

	public AgenteMiniMax() {
	}

	@Override
	public void comienzo(Jugador jugador, Estado estado) {
		this.jugador = jugador;		
	}

	@Override
	public Movimiento decision(Estado estado) {
		AlfaBeta alfaBeta = alfaBeta(jugador, estado, new AlfaBeta(-15.0), new AlfaBeta(15.0));
		return alfaBeta.getMov();
	}

	private AlfaBeta alfaBeta(Jugador jug, Estado estado, AlfaBeta alfa, AlfaBeta beta) {
		Double r = estado.resultado(jug);
		if(r != null)
			return new AlfaBeta(r, null);
		else
		{
			Movimiento[] movs = estado.movimientos(jug);
			for(Movimiento m : movs)
			{
				Jugador nextJug = getOther(m.jugador(), estado);
				if(jugador.equals(jug))
				{
					alfa = maximo(alfa,alfaBeta(nextJug, estado.siguiente(m), alfa, beta));
					if(alfa.getValor() >= beta.getValor())
					{
						beta.setMov(m);
						return beta;
					}
					alfa.setMov(m);
					return alfa;
				}
				else
				{
					beta = minimo(beta,alfaBeta(nextJug, estado.siguiente(m), alfa, beta));
					if(alfa.getValor() >= beta.getValor())
					{
						alfa.setMov(m);
						return alfa;
					}
					beta.setMov(m);
					return beta;
				}
			}
			return null;
		}
	}
	
	private Jugador getOther(Jugador j, Estado e){
		Jugador[] jugs = e.jugadores();
		return jugs[0].equals(j) ? jugs[1] : jugs[0];
	}

	private AlfaBeta minimo(AlfaBeta alfa, AlfaBeta beta) {
		return alfa.getValor() <= beta.getValor() ? alfa : beta;
	}

	private AlfaBeta maximo(AlfaBeta alfa, AlfaBeta beta) {
		return alfa.getValor() >= beta.getValor() ? alfa : beta;
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
		
		public AlfaBeta(Double v, Movimiento m){
			valor = v;
			mov = m;
		}
		
		public AlfaBeta(Double v){
			valor = v;
			mov = null;
		}

	}

}
