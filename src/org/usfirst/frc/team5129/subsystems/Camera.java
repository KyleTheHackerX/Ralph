package org.usfirst.frc.team5129.subsystems;

import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

import edu.wpi.cscore.CvSink;
import edu.wpi.cscore.CvSource;
import edu.wpi.cscore.UsbCamera;
import edu.wpi.first.wpilibj.CameraServer;

public class Camera extends Thread {
	
	private String streamname;
	
	public Camera(String streamname) {
		this.streamname = streamname;
	}
	
	public void run() {
		UsbCamera camera = CameraServer.getInstance().startAutomaticCapture();
		camera.setResolution(640, 480);

		CvSink cvSink = CameraServer.getInstance().getVideo();
		CvSource outputStream = CameraServer.getInstance().putVideo(streamname, 640, 480);

		Mat mat = new Mat();

		while (!Thread.interrupted()) {
			if (cvSink.grabFrame(mat) == 0) {
				outputStream.notifyError(cvSink.getError());
				continue;
			}
			Imgproc.rectangle(mat, new Point(100, 100), new Point(400, 400),
					new Scalar(255, 255, 255), 5);
			outputStream.putFrame(mat);
		}
		this.setDaemon(true);
	}
}
