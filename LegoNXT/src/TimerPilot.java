import lejos.robotics.TachoMotor;
import lejos.robotics.navigation.TachoPilot;

/**
 * Classe responsavel pelo controlo
 * 
 * @author Apocas
 * 
 */
public class TimerPilot extends TachoPilot implements Runnable {
	private int timer = 0;
	private boolean running = true;

	public TimerPilot(float wheelDiameter, float trackWidth,
			TachoMotor leftMotor, TachoMotor rightMotor) {
		super(wheelDiameter, trackWidth, leftMotor, rightMotor);
	}

	/**
	 * Mover com base tempo
	 * 
	 * @param speed
	 * @param temp
	 */
	public void travelTimed(int speed, int temp) {
		this.setMoveSpeed(speed);
		this.timer = Math.abs(temp);
		// new Thread(this).start();

		running = true;
		if (temp < 0) {
			this.backward();
		} else {
			this.forward();
		}
		try {
			Thread.sleep(timer);
		} catch (InterruptedException e) {
		}
		this.stop();
		running = false;

		// while(this.isMoving())Thread.yield();
	}

	/**
	 * Rotacao com base tempo
	 * 
	 * @param speed
	 * @param temp
	 */
	public void rotateTimed(int speed, int temp) {
		this.setSpeed(speed);
		this.timer = Math.abs(temp);
		// new Thread(this).start();

		running = true;
		if (temp < 0) {
			this.getLeft().forward();
			this.getRight().backward();
		} else {
			this.getLeft().backward();
			this.getRight().forward();
		}
		try {
			Thread.sleep(timer);
		} catch (InterruptedException e) {
		}
		this.stop();
		running = false;

		// while(this.isMoving())Thread.yield();
	}

	@Override
	public void run() {
		running = true;
		this.forward();
		try {
			Thread.sleep(timer);
		} catch (InterruptedException e) {
		}
		this.stop();
		running = false;
	}

	public boolean isRunning() {
		return running;
	}
}
