package org.usfirst.frc.team5129.robot;

import java.sql.Timestamp;
import java.util.Timer;
import java.util.TimerTask;

import org.usfirst.frc.team5129.annot.Autonomous;
import org.usfirst.frc.team5129.annot.ForField;
import org.usfirst.frc.team5129.annot.Init;
import org.usfirst.frc.team5129.annot.TeleOperated;
import org.usfirst.frc.team5129.subsystems.Camera;
import org.usfirst.frc.team5129.subsystems.Drive;
import org.usfirst.frc.team5129.subsystems.FuelCollection;
import org.usfirst.frc.team5129.subsystems.Lift;
import org.usfirst.frc.team5129.subsystems.stages.FState;
import org.usfirst.frc.team5129.subsystems.stages.LState;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.GenericHID.Hand;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.RobotState;
import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

@ForField
public class Robot extends IterativeRobot {

	private Victor frontLeft, frontRight, rearLeft, rearRight;
	
	@TeleOperated
	private Drive drive;
	private Joystick stick;
	private XboxController controller;
	private Lift lift;
	private FuelCollection collect;

	private Boolean hasAuto = false;
	private Integer seconds = 0;
	
	private Timer time = new Timer();
	private Camera cam;

	@Init
	@Override
	public void robotInit() {
		frontLeft = new Victor (1);
		rearLeft = new Victor  (0);
		frontRight = new Victor(3);
		rearRight = new Victor (2);
		//2301, 0 and 3 inverted
		//1032 (WORKING), all is inverted
		drive = new Drive(frontLeft, rearLeft, frontRight, rearRight);
		invert();
		lift = new Lift(new Spark(8));
		collect = new FuelCollection(new Spark(9));
		cam = new Camera("USB Camera 0");
		cam.start();
		stick = new Joystick(0);
		if (stick.getPort() != 0 || stick == null) {
			reportToDS("Joystick is in wront port (Expected 0). Check the DRIVER STATION.", false);
		}
		controller = new XboxController(1);
		if (controller.getPort() != 1 || controller == null) {
			reportToDS("Xbox Controller is in wront port (Expected 1). Check the DRIVER STATION.",
					false);
		}
		drive.setSafetyEnabled(false);
		updateDashboard();
	}

	@Init
	@Autonomous
	@Override
	public void autonomousInit() {
		seconds = 0;
		hasAuto = false;
	}

	@Autonomous
	@Override
	public void autonomousPeriodic() {
		updateDashboard();
		if (!hasAuto) {
			time.schedule(new TimerTask() {
				@Override
				public void run() {
					seconds++;
					drive.driveBackward();
//					if (seconds < 5 && !(seconds > 5)) {
//						drive.driveForward();
//					} else if (seconds < 8 && !(seconds > 8)) {
//						frontRight.set(-0.2);
//						frontLeft.set(0.3);
//						rearLeft.set(0.3);
//						rearRight.set(-0.3);
//					}
					if (seconds == 4) {
						drive.stopMotor();
						this.cancel();
					}
				}
				
			}, 0, 1000);
			hasAuto = true;
		}
	}

	@Init
	@TeleOperated
	@Override
	public void teleopInit() {

	}

	@Override
	public void teleopPeriodic() {
		while (RobotState.isOperatorControl() && RobotState.isEnabled()) {
			drive.arcadeDrive(stick, true);
			updateCollect();
			collect.call(collect.getState());
			updateLift();
			lift.call(lift.getState());
			updateDashboard();
		}
	}

	@Init
	@Override
	public void testInit() {

	}

	@Override
	public void testPeriodic() {
		while (stick.getTrigger() && RobotState.isEnabled()) {
			drive.arcadeDrive(stick, true);
			updateCollect();
			collect.call(collect.getState());
			updateLift();
			lift.call(lift.getState());
			updateDashboard();
		}
	}

	@Init
	@Override
	public void disabledInit() {
		seconds = 0;
		hasAuto = false;
		stopAllMotors();
	}

	/**
	 * Controls: [LBUMPER - RUNNING_IN] [RBUMPER - RUNNING_OUT] [X - STOP]
	 */
	private void updateCollect() {
		if (controller.getBumper(Hand.kLeft)) {
			collect.setState(FState.RUNNINGIN);
		} else if (controller.getBumper(Hand.kRight)) {
			collect.setState(FState.RUNNINGOUT);
		}
		if (controller.getXButton()) {
			collect.setState(FState.STOPPED);
		}
	}

	/**
	 * Controls: [Y - RUNNING Lift] [B - STOP Lift]
	 */
	private void updateLift() {
		if (controller.getYButton()) {
			lift.setState(LState.RUNNING);
		} else if (controller.getBButton()) {
			lift.setState(LState.STOPPED);
		}
	}

	private void updateDashboard() {
		SmartDashboard.putString("Lift State ", lift.getState().toString());
		SmartDashboard.putString("Collection State ", collect.getState().toString());
		SmartDashboard.putString("Camera State ", cam.getState().toString());
	}

	private void invert() {
		drive.setInvertedMotor(RobotDrive.MotorType.kFrontRight, true);
		drive.setInvertedMotor(RobotDrive.MotorType.kFrontLeft, true);
		drive.setInvertedMotor(RobotDrive.MotorType.kRearRight, true);
		drive.setInvertedMotor(RobotDrive.MotorType.kRearLeft, true);
	}

	private void stopAllMotors() {
		drive.stopMotor();
		collect.setState(FState.STOPPED);
		collect.call(collect.getState());
		lift.setState(LState.STOPPED);
		lift.call(lift.getState());
	}
	
	private void reportToDS(String message, boolean isError) {
		Timestamp t = new Timestamp(System.currentTimeMillis());
		if (isError) {
			System.out.println(("[" + t + "] " + message));
		} else {
			DriverStation.reportWarning(("[" + t + "] " + message), isError);
		}
	}

}
