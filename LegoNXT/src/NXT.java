import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import lejos.nxt.Button;
import lejos.nxt.LCD;
import lejos.nxt.Sound;
import lejos.nxt.comm.BTConnection;
import lejos.nxt.comm.Bluetooth;

/**
 * Classe principal configura o robot e a ligacao bluetooth
 * 
 * @author Apocas
 * 
 */
public class NXT {
	private String connected = "Connected";
	private String waiting = "Waiting...";
	private String closing = "Closing...";
	private BTConnection btc = null;
	public DataInputStream input = null;
	public DataOutputStream output = null;
	private boolean running = true;

	public NXT() throws InterruptedException, IOException {

	}

	public void connect() throws InterruptedException, IOException {
		while (!Button.ESCAPE.isPressed()) {
			LCD.drawString(waiting, 0, 0);
			LCD.refresh();

			btc = Bluetooth.waitForConnection();

			LCD.clear();
			LCD.drawString(connected, 0, 0);
			LCD.refresh();

			Sound.beep();

			input = btc.openDataInputStream();
			output = btc.openDataOutputStream();

			Receiver recv = new Receiver(this);
			new Thread(recv).start();
			running = true;

			while (!Button.ESCAPE.isPressed() && running) {
				Thread.sleep(100);
			}
			close();
		}
	}

	public void disconnect() {
		running = false;
	}

	private void close() throws IOException, InterruptedException {
		input.close();
		output.close();
		Thread.sleep(100);
		LCD.clear();
		LCD.drawString(closing, 0, 0);
		LCD.refresh();
		btc.close();
		LCD.clear();
	}

}
