package arsw.threads;

/**
 * Un galgo que puede correr en un carril
 * 
 * @author rlopez
 * 
 */
public class Galgo extends Thread {
	private int paso;
	private Carril carril;
	RegistroLlegada regl;

	public Galgo(Carril carril, String name, RegistroLlegada reg) {
		super(name);
		this.carril = carril;
		paso = 0;
		this.regl=reg;
	}

	public void corra() throws InterruptedException {
		while (paso < carril.size()) {			
			Thread.sleep(100);
			carril.setPasoOn(paso++);
			carril.displayPasos(paso);
			synchronized (MainCanodromo.lock) {
            while (MainCanodromo.pausado) {
                MainCanodromo.lock.wait(); // 🔥 espera hasta que el botón continue haga notifyAll
            }
        }
			
			if (paso == carril.size()) {
				carril.finish();

				int posicion = regl.asignarPosicion(this.getName());
				System.out.println("El galgo " + this.getName() + " llegó en la posición " + posicion);
			}
		}
	}


	@Override
	public void run() {
		
		try {
			corra();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}

}
