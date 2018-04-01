package org.usfirst.frc.team5129.annot;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.CLASS;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Marks anything related to the FRC TeleOperated state.
 * @author Driver
 *
 */
@Retention(CLASS)
@Target({ TYPE, FIELD, METHOD })
public @interface TeleOperated {

}
