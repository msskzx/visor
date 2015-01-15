package de.uniulm.omi.executionware.agent;/*
 *
 *  * Copyright (c) 2014 University of Ulm
 *  *
 *  * See the NOTICE file distributed with this work for additional information
 *  * regarding copyright ownership.  Licensed under the Apache License, Version 2.0 (the
 *  * "License"); you may not use this file except in compliance
 *  * with the License.  You may obtain a copy of the License at
 *  *
 *  *   http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing,
 *  * software distributed under the License is distributed on an
 *  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  * KIND, either express or implied.  See the License for the
 *  * specific language governing permissions and limitations
 *  * under the License.
 *
 */

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import de.uniulm.omi.executionware.agent.execution.impl.ShutdownHook;
import de.uniulm.omi.executionware.agent.monitoring.Interval;
import de.uniulm.omi.executionware.agent.monitoring.management.api.MonitoringService;
import de.uniulm.omi.executionware.agent.monitoring.management.api.ProbeNotFoundException;
import de.uniulm.omi.executionware.agent.server.impl.SocketServer;

import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * Created by daniel on 17.12.14.
 */
public class MonitoringAgentService {

    private final Set<Module> modules;

    public MonitoringAgentService(Set<Module> modules) {
        this.modules = modules;
    }

    public void start() {
        final Injector injector = Guice.createInjector(this.modules);
        try {
            injector.getInstance(MonitoringService.class).startMonitoring("cpu_usage", "de.uniulm.omi.executionware.agent.monitoring.probes.impl.CpuUsageProbe", new Interval(1, TimeUnit.SECONDS));
        } catch (ProbeNotFoundException e) {
            throw new RuntimeException(e);
        }
        injector.getInstance(SocketServer.class);
        Runtime.getRuntime().addShutdownHook(injector.getInstance(ShutdownHook.class));
    }
}
