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
	String fileName = "Telegraf/telegraf.conf";

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
	void addPlugin(String sensorType, Map<String, String> sensorConfiguration) throws IOException {

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

	void run() throws IOException {
		Runtime.getRuntime().exec("telegraf.exe");
	}

	void addHTTPPlugin() throws IOException {
		String parameters = "url = \"http://localhost/monitors\"\n";
		Map<String, String> sensorConfiguration = new HashMap<>();
		sensorConfiguration.put("http", parameters);

		addPlugin("outputs", sensorConfiguration);
	}

	void setConfigSettings(String global_tags, String agent) throws IOException {
		BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(fileName));
		StringBuilder stringBuilder = new StringBuilder("");

		stringBuilder.append("[global_tags]\n");
		String[] global_tagsParameters = global_tags.split(betweenParametersSeparator);
		for (int i = 0; i < global_tagsParameters.length; i++)
			stringBuilder.append("  " + global_tagsParameters[i] + "\n");
		stringBuilder.append("\n");

		stringBuilder.append("[agent]\n");
		String[] agentParameters = agent.split(betweenParametersSeparator);
		for (int i = 0; i < agentParameters.length; i++)
			stringBuilder.append("  " + agentParameters[i] + "\n");
		stringBuilder.append("\n");

		bufferedWriter.write(stringBuilder.toString());
		bufferedWriter.close();
	}

	void createConfig() throws IOException {
		String global_tags = "";
		String agent = "interval = \"10s\",round_interval = true,flush_buffer_when_full = true,collection_jitter = \"0s\",flush_interval = \"10s\",flush_jitter = \"0s\",debug = false,quiet = false,logfile = \"Telegraf/telegraf.log\",hostname = \"\"";
		setConfigSettings(global_tags, agent);
	}

}
