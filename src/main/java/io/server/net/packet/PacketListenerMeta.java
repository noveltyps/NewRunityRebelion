package io.server.net.packet;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * An annotation that describes what client -> server packets a
 * {@link PacketListener} can listen to.
 *
 * @author nshusa
 **/
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Inherited
public @interface PacketListenerMeta {

	/**
	 * The client-server packet identifiers that this annotated listener can listen
	 * to.
	 */
	int[] value() default {};
}