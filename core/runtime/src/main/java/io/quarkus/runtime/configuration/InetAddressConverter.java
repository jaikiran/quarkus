package io.quarkus.runtime.configuration;

import static io.quarkus.runtime.configuration.ConverterSupport.DEFAULT_QUARKUS_CONVERTER_PRIORITY;

import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.annotation.Priority;

import org.eclipse.microprofile.config.spi.Converter;
import org.wildfly.common.net.Inet;

/**
 * A converter which produces values of type {@link InetAddress}.
 */
@Priority(DEFAULT_QUARKUS_CONVERTER_PRIORITY)
public class InetAddressConverter implements Converter<InetAddress> {

    @Override
    public InetAddress convert(final String value) {
        if (value.isEmpty()) {
            return null;
        }
        final InetAddress parsed = Inet.parseInetAddress(value);
        if (parsed != null) {
            return parsed;
        }
        try {
            return InetAddress.getByName(value);
        } catch (UnknownHostException e) {
            throw new IllegalArgumentException("Unable to resolve \"" + value + "\"", e);
        }
    }
}
