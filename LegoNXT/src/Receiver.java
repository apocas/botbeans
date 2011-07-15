import java.io.IOException;

import lejos.nxt.LCD;
import lejos.nxt.Motor;
import lejos.nxt.SensorPort;
import lejos.nxt.Sound;
import lejos.nxt.SoundSensor;
import lejos.nxt.UltrasonicSensor;
import lejos.nxt.addon.CompassSensor;
import lejos.robotics.navigation.Pilot;

/**
 * Classe responsável por receber mensagens do BotbeansControl
 * 
 * @author Apocas
 * 
 */
public class Receiver implements Runnable {
	private UltrasonicSensor ultrasonic;
	private CompassSensor compass;
	private SoundSensor sound;
	private boolean running = true;
	private Pilot pilot;
	private NXT modem = null;
	private float roda = 5.6f;
	private float eixo = 17.1f;
	private Motor motore = Motor.B;
	private Motor motord = Motor.A;
	private Motor motora = Motor.C;
	private SensorPort porta_us = SensorPort.S3;
	private SensorPort porta_comp = SensorPort.S1;
	private SensorPort porta_som = SensorPort.S2;

	public Receiver(NXT modem) {
		this.modem = modem;
		ultrasonic = new UltrasonicSensor(porta_us);
		compass = new CompassSensor(porta_comp);
		sound = new SoundSensor(porta_som);
		pilot = new TimerPilot(roda, eixo, motore, motord);
	}

	/**
	 * Executa accao de acordo com a mensagem recebida
	 */
	@Override
	public void run() {
		int[] data = new int[255];
		while (running && data.length >= 1) {
			try {
				data = receive();
				if (validate(data)) {
					LCD.drawString("R: " + data.length + " " + data[0], 0, 0);
					LCD.refresh();
				}

				if (data.length >= 1 && data[0] > 0 && validate(data)) {
					switch (data[0]) {
					case 1:
						pilot.setMoveSpeed(data[1]);
						pilot.travel(data[2]);
						modem.output.writeInt(1);
						modem.output.flush();
						break;
					case 2:
						((TimerPilot) pilot).travelTimed(data[1], data[2]);
						modem.output.writeInt(1);
						modem.output.flush();
						break;
					case 3:
						pilot.setTurnSpeed(data[1]);
						pilot.rotate(data[2]);
						modem.output.writeInt(1);
						modem.output.flush();
						break;
					case 4:
						((TimerPilot) pilot).rotateTimed(data[1], data[2]);
						modem.output.writeInt(1);
						modem.output.flush();
						break;
					case 15:
						roda = modem.input.readFloat();
						eixo = modem.input.readFloat();
						motore = translateMotor(modem.input.readInt());
						motord = translateMotor(modem.input.readInt());
						motora = lastMotor();
						porta_us = translateSensor(modem.input.readInt());
						porta_comp = translateSensor(modem.input.readInt());
						porta_som = translateSensor(modem.input.readInt());
						pilot = new TimerPilot(roda, eixo, motore, motord);
						modem.output.writeInt(1);
						modem.output.flush();
						break;
					case 16:
						try {
							Thread.sleep(150);
						} catch (InterruptedException e) {
						}
						ultrasonic.getDistance();
						try {
							Thread.sleep(100);
						} catch (InterruptedException e1) {
						}
						modem.output.writeInt((int) ultrasonic.getDistance());
						modem.output.flush();
						break;
					case 17:
						try {
							Thread.sleep(250);
						} catch (InterruptedException e) {
						}
						modem.output.writeInt((int) compass.getDegrees());
						modem.output.flush();
						break;
					case 18:
						Sound.playTone(data[1], data[2]);
						try {
							Thread.sleep(data[2]);
						} catch (InterruptedException e) {
						}
						modem.output.writeInt(1);
						modem.output.flush();
						break;
					case 19:
						try {
							Thread.sleep(250);
						} catch (InterruptedException e) {
						}
						modem.output.writeInt(sound.readValue());
						modem.output.flush();
						break;
					case 20:
						motora.setSpeed(data[1]);
						motora.rotate(data[2]);
						modem.output.writeInt(1);
						modem.output.flush();
						break;
					}
				}
			} catch (IOException e) {
				running = false;
				modem.disconnect();
			}
		}
		modem.disconnect();
	}

	private Motor lastMotor() {
		if ((motore == Motor.A || motord == Motor.A)
				&& (motore == Motor.B || motord == Motor.B)) {
			return Motor.C;
		} else if ((motore == Motor.A || motord == Motor.A)
				&& (motore == Motor.C || motord == Motor.C)) {
			return Motor.B;
		} else {
			return Motor.A;
		}
	}

	private SensorPort translateSensor(int m) {
		switch (m) {
			case 1:
				return SensorPort.S1;
			case 2:
				return SensorPort.S2;
			case 3:
				return SensorPort.S3;
			case 4:
				return SensorPort.S4;
			default:
				return null;
		}
	}

	/**
	 * Traduz o motor
	 * 
	 * @param m
	 * @return
	 */
	private Motor translateMotor(int m) {
		switch (m) {
		case 1:
			return Motor.A;
		case 2:
			return Motor.B;
		case 3:
			return Motor.C;
		default:
			return Motor.A;
		}
	}

	/**
	 * Valida o tamanho da mensagem
	 * 
	 * @param data
	 * @return
	 */
	private boolean validate(int[] data) {
		switch (data[0]) {
		case 1:
		case 2:
		case 3:
		case 4:
		case 18:
		case 20:
			if (data.length == 3) {
				return true;
			}
			break;
		case 15:
		case 16:
		case 17:
		case 19:
			if (data.length == 1) {
				return true;
			}
			break;
		}
		return false;
	}

	public int[] receive() throws IOException {
		int size = modem.input.readInt();
		int dd[] = new int[size];
		for (int i = 0; i < size; i++) {
			dd[i] = modem.input.readInt();
		}
		return dd;
	}

}
