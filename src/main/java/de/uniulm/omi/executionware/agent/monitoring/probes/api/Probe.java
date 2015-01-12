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

package de.uniulm.omi.executionware.agent.monitoring.probes.api;

import de.uniulm.omi.executionware.agent.monitoring.metric.api.MeasurementNotAvailableException;

/**
 * Interface which needs to be implemented to create a Probe.
 * <p>
 * A probe is run with the given interval measuring the metric with the provided name.
 */
public interface Probe {

    /**
     * The name of the measurement this probe does.
     *
     * @return the name of the measurement this probe measures.
     */
    public String getMeasurementName();

    /**
     * The value of metric this probe measures at the time it is called.
     *
     * @return the measured value at this point of time.
     * @throws de.uniulm.omi.executionware.agent.monitoring.metric.api.MeasurementNotAvailableException
     */
    public Measurement getMeasurementValue() throws MeasurementNotAvailableException;
}