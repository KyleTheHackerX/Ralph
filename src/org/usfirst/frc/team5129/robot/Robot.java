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
import edu.wpi.first.wpilibj.livewindow.LiveWindow;

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
	
	private RobotDrive drive;
	private Timer time;
	private boolean hasAuto = false;

	private Joystick stick;
	private XboxController controller;
 
	private Lift lift;
	private FuelCollection collect; 
	private Camera cam;
	
	@Override
	public void robotInit() {
		drive = new RobotDrive(1,0,3,2);
		invert();
		time = new Timer();
		lift = new Lift(new Spark(8));
		collect = new FuelCollection(new Spark(9));
		cam = new Camera("ROBOCAM");
		cam.start();
		stick = new Joystick(0);
		if (stick.getPort() != 0 || stick == null) {
			debug(MsgType.WARNING, "Joystick is [on different port / null]. Check the driver station. Disabling...");
			disabledInit();
		}
		controller = new XboxController(1);
		if (controller.getPort() != 1 || controller == null) {
			debug(MsgType.WARNING, "Xbox Controller is [on different port / null]. Check the driver station. Disabling...");
			disabledInit();
		}
		drive.setSafetyEnabled(false);
		debug(MsgType.WARNING, "Safety is disabled.");
		updateDashboard();
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

	@Override
	public void teleopPeriodic() {
		while (RobotState.isOperatorControl() && RobotState.isEnabled()) {
			drive.arcadeDrive(stick, true);
			updateCollect();
			collect.call(collect.getState());
			updateLift();
			lift.call(lift.getState());
			if (controller.getStartButton()) {
				updateStates();
			}
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
		debug(MsgType.CONTROLS, "Test Mode has control safety enabled. Hold the trigger to use controls.");
	}
	
	@Override
	public void testPeriodic() {
		while (stick.getTrigger() && RobotState.isEnabled()) {
			drive.arcadeDrive(stick, true);
			updateCollect();
			collect.call(collect.getState());
			updateLift();
			lift.call(lift.getState());
		}
	}
	
	@Override
	public void disabledInit() {
		debug(MsgType.DEBUG, "Disabled Mode. Freeing Drivers...");
		hasAuto = false;
		debug(MsgType.WARNING, "'hasAuto' set to false.");
		
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
		LiveWindow.run();
	}

	private void updateStates() {
		debug(MsgType.CONTROLS, "Collection State: " + collect.getState());
		debug(MsgType.CONTROLS, "Lift State: " + lift.getState());
	}
	
	private void invert() {
		drive.setInvertedMotor(RobotDrive.MotorType.kFrontRight, true);
		drive.setInvertedMotor(RobotDrive.MotorType.kFrontLeft, true);
		drive.setInvertedMotor(RobotDrive.MotorType.kRearRight, true);
		drive.setInvertedMotor(RobotDrive.MotorType.kRearLeft, true);
	}
}
