public class CeldaValor implements Celda {
	
	int valor;

	public CeldaValor(int valor) {
		this.valor=valor;
	}
	
	@Override
	public int getValue() {
		return valor;
	}
	
	@Override
	public String toString() {
		return Integer.toString(valor);
	}
	
}