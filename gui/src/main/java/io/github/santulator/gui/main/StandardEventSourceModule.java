/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.santulator.gui.main;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import io.github.santulator.core.ThreadPoolTool;
import io.github.santulator.gui.event.CommandLineEventSource;
import io.github.santulator.gui.event.ExternalEventBroker;
import io.github.santulator.gui.event.ExternalEventBrokerImpl;

import java.util.List;

public class StandardEventSourceModule extends AbstractModule {
    private final List<String> args;

    public StandardEventSourceModule(final String... args) {
        this.args = List.of(args);
    }

    @Override
    protected void configure() {
        bind(CommandLineEventSource.class).toInstance(new CommandLineEventSource(args));
    }

    @Provides
    public ExternalEventBroker provideExternalEventBroker(final CommandLineEventSource commandLineEventSource, final ThreadPoolTool threadPoolTool) {
        ExternalEventBrokerImpl externalEventBroker = new ExternalEventBrokerImpl(threadPoolTool);

        commandLineEventSource.setListener(externalEventBroker);

        return externalEventBroker;
    }
}
