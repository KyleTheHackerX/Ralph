package org.usfirst.frc.team5129.subsystems;

import org.usfirst.frc.team5129.subsystems.stages.FState;

import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.wpilibj.SpeedController;

public class FuelCollection {
	
	private Spark collect;
	
	private FState fState;
	
	/**
	 * Controls the fuel collection mechanism.
	 * 
	 * @param collect
	 * @see SpeedController
	 */
	public FuelCollection(Spark collect) {
		this.collect = collect;
		this.fState = FState.STOPPED;
	}
	
	public FState getState() {
		return this.fState;
	}
	
	public void setState(FState fState) {
		this.fState = fState;
	}
	
	/**
	 * Decides the motors actions based on the state.
	 * @param fState
	 */
	public void call(FState fState) {
		switch (fState) {
		case RUNNINGIN:
			collectIn();
			break;
		case RUNNINGOUT:
			collectOut();
			break;
		case STOPPED:
			stop();
			break;
		}
	}
	
	private void collectIn() {
		if (fState != FState.RUNNINGIN) {
			fState = FState.RUNNINGIN;
		}
		collect.set(1);
	}
	
	private void collectOut() {
		if (fState != FState.RUNNINGOUT) {
			fState = FState.RUNNINGOUT;
		}
		collect.set(-1);
	}
	
	private void stop() {
		if (collect.getSpeed() != 0) {
			collect.stopMotor();
		}
		if (fState != FState.STOPPED) {
			fState = FState.STOPPED;
		}
	}
	
//	
//	public void pour() {
//		collect.drive(1.0, 0);
//	}
//	
//	public void arcade(GenericHID joystick) {
//		collect.arcadeDrive(joystick);
//	}
//	
//	public void arcadeY(GenericHID joystick) {
//		collect.drive(joystick.getY(), 0);
//	}
//	
//	public void arcadeSide(XboxController joystick, Hand hand) {
//		collect.drive(joystick.getY(hand), 0);
//	}
//	
}
