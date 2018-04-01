package org.usfirst.frc.team5129.subsystems;

import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.Victor;

public class Drive extends RobotDrive {

	public Drive(int frontLeftMotor, int rearLeftMotor, int frontRightMotor, int rearRightMotor) {
		super(frontLeftMotor, rearLeftMotor, frontRightMotor, rearRightMotor);
		// TODO Auto-generated constructor stub
	}
	
	public Drive(Victor frontLeftMotor, Victor rearLeftMotor, Victor frontRightMotor, Victor rearRightMotor) {
		super(frontLeftMotor, rearLeftMotor, frontRightMotor, rearRightMotor);
		// TODO Auto-generated constructor stub
	}
	
	public void driveForward() {
		this.drive(-0.3, 0);
	}
	
	public void driveBackward() {
		this.drive(0.3, 0);
	}
	
	public void driveLeft() {
		this.drive(0.05, -1);
	}
	
	public void driveRight() {
		this.drive(0.05, 1);
	}

}
