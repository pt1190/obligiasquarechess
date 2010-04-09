package juegos.tateti;

import juegos.base.Movimiento;

public class AlfaBeta {
	
	private Movimiento mov;
	private Double valor; // Valor correspondiente al estado final contenido en el movimiento
	
	public AlfaBeta(Double v, Movimiento m){
		valor = v;
		mov = m;
	}
	
	public Movimiento getMovimiento(){
		return mov;
	}
	
	public Double getValor(){
		return valor;
	}

}
