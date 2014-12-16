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

package de.uniulm.omi.monitoring.reporting.impl.cli;

import de.uniulm.omi.monitoring.metric.impl.Metric;
import de.uniulm.omi.monitoring.reporting.api.ReportingInterface;
import de.uniulm.omi.monitoring.reporting.impl.ReportingException;

import java.util.Collection;

/**
 * Created by daniel on 27.11.14.
 */
public class CommandLineReporter implements ReportingInterface<Metric> {

    @Override
    public void report(Metric metric) throws ReportingException {
        System.out.println(metric.toString());
    }

    @Override
    public void report(Collection<Metric> metrics) throws ReportingException {
        for(Metric metric : metrics) {
            this.report(metric);
        }
    }
}