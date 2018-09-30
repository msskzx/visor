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

	String betweenParametersSeparator = ",";
	String fileName = "./Telegraf/telegraf.conf";

	@Override
	protected void initialize(MonitorContext monitorContext, SensorConfiguration sensorConfiguration)
			throws SensorInitializationException {
		super.initialize(monitorContext, sensorConfiguration);

		try {
			addPlugins("input", sensorConfiguration.getConfiguration());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/*
	 * read the config file and add the required inputs or outputs plugins
	 *
	 * sensorType: "inputs" / "outputs"
	 *
	 * sensorConfiguration: <"cpu", "totalcpu = true,percpu = true">
	 */
	void addPlugins(String sensorType, Map<String, String> sensorConfiguration) throws IOException {

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

		stringBuilder.append("\n");
		bufferedWriter.write(stringBuilder.toString());

		bufferedWriter.close();
	}

}
