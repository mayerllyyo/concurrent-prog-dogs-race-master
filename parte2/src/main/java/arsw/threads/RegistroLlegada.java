package arsw.threads;

public class RegistroLlegada {

	private int ultimaPosicionAlcanzada=1;

	private String ganador=null;
	
	public String getGanador() {

		return ganador;
	}

	public synchronized int asignarPosicion(String galgo){
		int ultimaPosionActual = ultimaPosicionAlcanzada;
		 
		if (ultimaPosicionAlcanzada == 1){
			ganador = galgo;
		}

		ultimaPosicionAlcanzada ++;

		return ultimaPosionActual;

	}

	public void setGanador(String ganador) {
		this.ganador = ganador;
	}

	public int getUltimaPosicionAlcanzada() {
		return ultimaPosicionAlcanzada;
	}

	public void setUltimaPosicionAlcanzada(int ultimaPosicionAlcanzada) {
		this.ultimaPosicionAlcanzada = ultimaPosicionAlcanzada;
	}

	
	
}
