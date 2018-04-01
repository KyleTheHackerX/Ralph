package org.usfirst.frc.team5129.robot;

import org.usfirst.frc.team5129.subsystems.Camera;
import org.usfirst.frc.team5129.subsystems.FuelCollection;
import org.usfirst.frc.team5129.subsystems.FuelCollection.FState;
import org.usfirst.frc.team5129.subsystems.Lift;
import org.usfirst.frc.team5129.subsystems.Lift.LState;

import edu.wpi.first.wpilibj.GenericHID.Hand;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.RobotState;
import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.XboxController;

public class Robot extends IterativeRobot {
	
	enum MsgType {
		
		DEBUG("[System] [Debug]: "),
		OK("[System]: "),
		WARNING("[System] [WARNING]: "),
		ERROR("[System] [ERROR]: "),
		CONTROLS("[Controls]"),
		REG("");
		
		private String value;
		
		MsgType(String value) {
			this.value = value;
		}
		
		public String getValue() {
			return value;
		}
	}
	
	private RobotDrive drive; // Although a subsystem, there is no need for a user-made object.
	private Timer time; // Driver Station Timer
	private boolean hasAuto = false; // Has Autonomous run once?

	private Joystick stick;
	private XboxController controller;
 
	private Lift lift; // Drive should go in the negative direction. Do NOT drive positive!
	private FuelCollection collect; 
	private Camera cam;
	
	@Override
	public void robotInit() {
		drive = new RobotDrive(1,0,3,2);
		drive.setInvertedMotor(RobotDrive.MotorType.kFrontRight, true);
		drive.setInvertedMotor(RobotDrive.MotorType.kFrontLeft, true);
		drive.setInvertedMotor(RobotDrive.MotorType.kRearRight, true);
		drive.setInvertedMotor(RobotDrive.MotorType.kRearLeft, true);
		time = new Timer();
		lift = new Lift(new Spark(8));
		collect = new FuelCollection(new Spark(9));
		cam = new Camera("ROBOCAM");
		cam.start();
		stick = new Joystick(0); // Logitech Extreme should go here.
		if (stick.getPort() != 0) {
			debug(MsgType.WARNING, "Joystick is on different port. Check the driver station.");
		}
		controller = new XboxController(1); // XBCTRLR should go here.
		if (controller.getPort() != 1) {
			debug(MsgType.WARNING, "Xbox Controller is on different port. Check the driver station.");
		}
		drive.setSafetyEnabled(false);
		debug(MsgType.WARNING, "Safety is disabled.");
		debug(MsgType.OK, "Robot Initialized! Driver station is ready.");
	}

	@Override
	public void autonomousInit() {
		time.start();
		debug(MsgType.DEBUG, "Entered Autonomous Mode.");
		hasAuto = true;
		debug(MsgType.WARNING, "'hasAuto' set to true. Timer will reset on mode switch.");
	}

	@Override
	public void autonomousPeriodic() {
		
	}
	
	@Override
	public void teleopInit() {
		debug(MsgType.DEBUG, "Entered Tele-operated Mode.");
		if (hasAuto) {
			time.reset();
			time.start();
			debug(MsgType.WARNING, "'hasAuto' set to true. It is reccomended to disable the robot "
					+ "before entering autonomous again.");
		} else {
			time.start();
		}
	}

	/*
	 * WARNING: While loop is called @ updateLift(); Make sure you don't activate the lift 
	 * unless you need to, as the controls wont be updated until the loop exits. If Taylor wishes, she
	 * can allow the toggle again. To prevent accidental use, updateLift(); will check the state of the
	 * collection mechanism to see if it's running.
	 */
	@Override
	public void teleopPeriodic() {
		while (RobotState.isOperatorControl() && RobotState.isEnabled()) {
			drive.arcadeDrive(stick, true);
			updateCollect();
			collect.call(collect.getState());
			lift.call(lift.getState());
			updateLift();
		}
	}

	@Override
	public void testInit() {
		debug(MsgType.DEBUG, "Entered Test Mode.");
		if (hasAuto) {
			time.reset();
			time.start();
			debug(MsgType.WARNING, "'hasAuto' set to true. It is reccomended to disable the robot "
					+ "before entering autonomous again.");
		} else {
			time.start();
		}
		debug(MsgType.WARNING, "This is not for the competition, obviously!");
	}
	
	@Override
	public void testPeriodic() {
	}
	
	@Override
	public void disabledInit() {
		debug(MsgType.DEBUG, "Disabled Mode. Freeing Drivers...");
		hasAuto = false;
		debug(MsgType.WARNING, "'hasAuto' set to false.");
		
	}
	
	@Override
	public void disabledPeriodic() {
	}
	
	private void debug(MsgType type, String msg) {
		System.out.println(type.getValue() + msg);
	}
	
	/**
	 * Controls: [LBUMPER - RUNNING_IN] [RBUMPER - RUNNING_OUT] [X - STOP]
	 */
	private void updateCollect() {
		if (controller.getBumper(Hand.kLeft)) {
			collect.setState(FState.RUNNINGIN);
			debug(MsgType.CONTROLS, "Collection > RUNNING_IN");
		} else if (controller.getBumper(Hand.kRight)) {
			collect.setState(FState.RUNNINGOUT);
			debug(MsgType.CONTROLS, "Collection > RUNNING_OUT");
		}
		if (controller.getXButton()) {
			collect.setState(FState.STOPPED);
			debug(MsgType.CONTROLS, "Collection > STOPPED");
		}
	}
	
	/**
	 * Controls: [Y - RUNNING Lift] [B - STOP Lift]
	 */
	private void updateLift() {
		if (controller.getYButton()) {
			debug(MsgType.CONTROLS, "Lift > RUNNING");
			lift.setState(LState.RUNNING);
		} else if (controller.getBButton()) {
			debug(MsgType.CONTROLS, "Lift > STOPPED");
			lift.setState(LState.STOPPED);
		}
	}
}
