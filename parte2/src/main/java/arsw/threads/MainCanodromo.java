package arsw.threads;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;

public class MainCanodromo {

    private static Galgo[] galgos;
    private static Canodromo can;
    private static RegistroLlegada reg = new RegistroLlegada();

    public static final Object lock = new Object();
    public static volatile boolean pausado = false;

    public static void main(String[] args) {
        can = new Canodromo(17, 100);
        galgos = new Galgo[can.getNumCarriles()];
        can.setVisible(true);

        can.setStartAction(new ActionListener() {
    @Override
    public void actionPerformed(final ActionEvent e) {
        ((JButton) e.getSource()).setEnabled(false);
        new Thread() {
            public void run() {
                for (int i = 0; i < can.getNumCarriles(); i++) {
                    galgos[i] = new Galgo(can.getCarril(i), "" + i, reg);
                    galgos[i].start();
                }

                for (int i = 0; i < can.getNumCarriles(); i++) {
                    try {
                        galgos[i].join();
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                }

                can.winnerDialog(reg.getGanador(),
                        reg.getUltimaPosicionAlcanzada() - 1);
                System.out.println("El ganador fue: " + reg.getGanador());
            }
        }.start();
    }
});


        can.setStopAction(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                pausado = true;
                System.out.println("Carrera pausada!");
            }
        });

        can.setContinueAction(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                synchronized (lock) {
                    pausado = false;
                    lock.notifyAll();
                }
                System.out.println("Carrera reanudada!");
            }
        });
    }
}
