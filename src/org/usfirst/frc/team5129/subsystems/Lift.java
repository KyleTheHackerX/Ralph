package org.usfirst.frc.team5129.subsystems;

import org.usfirst.frc.team5129.subsystems.stages.LState;

import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.wpilibj.SpeedController;

public class Lift {
	
	private Spark lift;
	
	private LState lState;
	
	/**
	 * Controls the Lift mechanism. Only drive negative!
	 * 
	 * @param lift
	 * @see SpeedController
	 */
	public Lift(Spark lift) {
		this.lift = lift;
		this.lState = LState.STOPPED;
	}
	
	public LState getState() {
		return this.lState;
	}
	
	public void setState(LState lState) {
		this.lState = lState;
	}
	
	/**
	 * Decides the motors actions based on the state.
	 * @param fState
	 */
	public void call(LState lState) {
		switch (lState) {
		case RUNNING:
			liftUp();
			break;
		case STOPPED:
			stop();
			break;
		}
	}
	
	private void liftUp() {
		lift.set(-1);
		if (lState != LState.RUNNING) {
			lState = LState.RUNNING;
		}
	}
	
	private void stop() {
		if (lState != LState.STOPPED) {
			lState = LState.STOPPED;
		}
		if (lift.getSpeed() != 0) {
			lift.stopMotor();
		}
	}
//	
//	private void slowMotor() {
//		for (double i = 0.0; i < -0.1; i--) {
//			if (i == -1) {
//				break;
//			}
//			lift.set(i);
//		}
//	}
//	
//	public void arcade(GenericHID joystick) {
//		lift.arcadeDrive(joystick);
//	}
//	
//	public void arcadeY(GenericHID joystick) {
//		lift.drive(joystick.getY(), 0);
//	}
//	
//	public void arcadeSide(XboxController joystick, Hand hand) {
//		lift.drive(joystick.getY(hand), 0);
//	}
//	
}
