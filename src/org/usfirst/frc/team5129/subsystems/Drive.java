package org.usfirst.frc.team5129.subsystems;

import edu.wpi.first.wpilibj.RobotDrive;

public class Drive extends RobotDrive {

	public Drive(int frontLeftMotor, int rearLeftMotor, int frontRightMotor, int rearRightMotor) {
		super(frontLeftMotor, rearLeftMotor, frontRightMotor, rearRightMotor);
		// TODO Auto-generated constructor stub
	}
	
	public void driveForward(boolean poweredLeft) {
		if (poweredLeft) {
			this.m_frontLeftMotor.set(-0.2);
			this.m_frontRightMotor.set(-0.3);
			this.m_rearLeftMotor.set(-0.2);
			this.m_rearRightMotor.set(-0.3);
		} else {
			this.drive(-0.3, 0);
		}
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
