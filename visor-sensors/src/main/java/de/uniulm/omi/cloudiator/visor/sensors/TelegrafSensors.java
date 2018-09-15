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
		Runtime.getRuntime().exec("\".\\Telegraf\\telegraf.exe\" --config \".\\Telegraf\\telegraf.conf\"");
	}

	void addHTTPPlugin(String HTTPParameters) throws IOException {
		StringBuilder parameters = new StringBuilder(HTTPParameters);
		Map<String, String> sensorConfiguration = new HashMap<>();

		if(HTTPParameters.equals("")) {
			parameters.append("url = \"http://localhost:31415/monitors/uuid\"\n");
			parameters.append("timeout = \"5s\"\n");
			parameters.append("method = \"POST\"\n");
			parameters.append("timeout = \"5s\"\n");
			parameters.append("data_format = \"json\"\n");
		}

		sensorConfiguration.put("http", parameters.toString());	
		sensorConfiguration.put("http.headers", "Content-Type = \"text/plain; charset=utf-8\"");
		
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

	void createConfig(String global_tags, String agentParameters) throws IOException {
		StringBuilder agent = new StringBuilder(agentParameters);
		if (agentParameters.toString().equals("")) {
			agent.append("interval = \"10s\"");
			agent.append(",round_interval = true\n");
			agent.append(",metric_buffer_limit = 1000\n");
			agent.append(",flush_buffer_when_full = true\n");
			agent.append(",collection_jitter = \"0s\"\n");
			agent.append(",flush_interval = \"10s\"\n");
			agent.append(",flush_jitter = \"0s\"\n");
			agent.append(",debug = false\n");
			agent.append(",quiet = false\n");
			agent.append(",logfile = \"Telegraf/telegraf.log\",hostname = \"\"\n");
		}
		setConfigSettings(global_tags, agent.toString());
	}

}
