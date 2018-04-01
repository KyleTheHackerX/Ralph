package org.usfirst.frc.team5129.annot;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.CLASS;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Marks any FRC Initialization in the robot.
 * @author Driver
 *
 */
@Retention(CLASS)
@Target(METHOD)
public @interface Init {

}
