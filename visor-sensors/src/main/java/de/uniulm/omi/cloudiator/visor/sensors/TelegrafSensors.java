package de.uniulm.omi.cloudiator.visor.sensors;

import de.uniulm.omi.cloudiator.visor.exceptions.SensorInitializationException;
import de.uniulm.omi.cloudiator.visor.monitoring.AbstractSensor;
import de.uniulm.omi.cloudiator.visor.monitoring.MonitorContext;
import de.uniulm.omi.cloudiator.visor.monitoring.SensorConfiguration;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class TelegrafSensors extends AbstractSensor {

	@Override
	protected void initialize(MonitorContext monitorContext, SensorConfiguration sensorConfiguration)
			throws SensorInitializationException {
		super.initialize(monitorContext, sensorConfiguration);
	}

	/*
	 * read the config file and add the required inputs or outputs plugins
	 * 
	 * sensorType: "inputs" / "outputs"
	 * 
	 * sensorConfiguration: <"cpu", "totalcpu = true,percpu = true">
	 */
	void writeConfig(String sensorType, Map<String, String> sensorConfiguration) throws IOException {
		String fileName = "telegraf.conf";
		String betweenParametersSeparator = ",";

		BufferedReader bufferedReader = new BufferedReader(new FileReader(fileName));
		StringBuilder stringBuilder = new StringBuilder("");

		String st = "";
		while ((st = bufferedReader.readLine()) != null) {
			stringBuilder.append(st + "\n");
		}
		bufferedReader.close();

		BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(fileName));

		for (Entry<String, String> entry : sensorConfiguration.entrySet()) {
			stringBuilder.append("[[" + sensorType + ":" + entry.getKey() + "]]\n");
			String[] parameters = entry.getValue().split(betweenParametersSeparator);
			for (int i = 0; i < parameters.length; i++) {
				stringBuilder.append("  " + parameters[i] + "\n");
			}

		}

		bufferedWriter.write(stringBuilder.toString());
		bufferedWriter.close();
	}

	void run() throws IOException {
		Runtime.getRuntime().exec("telegraf.exe");
	}

}
