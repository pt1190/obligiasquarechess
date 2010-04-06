package juegos.squareChess;

public class Posicion
{
	public int x;
	public int y;
	
	public Posicion(int x, int y)
	{
		this.x = x;
		this.y = y;
	}
	
	public Posicion clone()
	{
		return new Posicion(this.x, this.y);
	}
	
	public Posicion mover(int x, int y)
	{
		return new Posicion(this.x + x, this.y + y);
	}
	
	public String toString()
	{
		return "(" + this.x + "," + this.y + ")";
	}
}