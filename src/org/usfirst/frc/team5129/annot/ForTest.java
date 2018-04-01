package org.usfirst.frc.team5129.annot;

import static java.lang.annotation.RetentionPolicy.CLASS;

import java.lang.annotation.Retention;

/**
 * Marks the Robot class for testing purposes. Typically, you wouldn't
 * use this for the Field, as this class isn't updated to field standards.
 * @author Driver
 *
 * @deprecated This is not used for Field-play.
 */
@Retention(CLASS)
@Deprecated
public @interface ForTest {

}
