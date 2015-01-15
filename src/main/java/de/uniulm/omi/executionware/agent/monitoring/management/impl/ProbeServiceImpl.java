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

package de.uniulm.omi.executionware.agent.monitoring.management.impl;

import de.uniulm.omi.executionware.agent.monitoring.management.api.ProbeNotFoundException;
import de.uniulm.omi.executionware.agent.monitoring.management.api.ProbeService;
import de.uniulm.omi.executionware.agent.monitoring.probes.api.Probe;

/**
 * Created by daniel on 15.01.15.
 */
public class ProbeServiceImpl implements ProbeService {

    @Override
    public Probe findProbe(String className) throws ProbeNotFoundException {
        try {
            return (Probe) Class.forName(className).newInstance();
        } catch (ClassCastException | ClassNotFoundException | InstantiationException | IllegalAccessException e) {
            throw new ProbeNotFoundException("Could not load probe with name " + className, e);
        }
    }
}