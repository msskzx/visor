/*
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

package de.uniulm.omi.executionware.agent.monitoring.sensors.impl;

import com.google.common.base.Optional;
import com.sun.management.OperatingSystemMXBean;
import de.uniulm.omi.executionware.agent.monitoring.metric.api.MeasurementNotAvailableException;
import de.uniulm.omi.executionware.agent.monitoring.monitors.impl.MonitorContext;
import de.uniulm.omi.executionware.agent.monitoring.sensors.api.Measurement;
import de.uniulm.omi.executionware.agent.monitoring.sensors.api.Sensor;

import java.lang.management.ManagementFactory;

/**
 * A probe for measuring the CPU usage in % on the given machine.
 */
public class CpuUsageSensor implements Sensor {

    @Override
    public void init() {
        //intentionally left empty
    }

    @Override
    public void setMonitorContext(Optional<MonitorContext> monitorContext) {
        //intentionally left empty
    }

    @Override
    public Measurement getMeasurement() throws MeasurementNotAvailableException {
        OperatingSystemMXBean osBean = ManagementFactory.getPlatformMXBean(
                OperatingSystemMXBean.class);

        double systemCpuLoad = osBean.getSystemCpuLoad();
        double systemCpuLoadPercentage = systemCpuLoad * 100;

        if (systemCpuLoad < 0) {
            throw new MeasurementNotAvailableException("Received negative value");
        }

        return new MeasurementImpl(System.currentTimeMillis(), systemCpuLoadPercentage);
    }
}