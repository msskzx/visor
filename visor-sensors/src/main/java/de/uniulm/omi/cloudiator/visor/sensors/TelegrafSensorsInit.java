package de.uniulm.omi.cloudiator.visor.sensors;

import de.uniulm.omi.cloudiator.visor.exceptions.SensorInitializationException;
import de.uniulm.omi.cloudiator.visor.monitoring.AbstractSensor;
import de.uniulm.omi.cloudiator.visor.monitoring.MonitorContext;
import de.uniulm.omi.cloudiator.visor.monitoring.SensorConfiguration;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class TelegrafSensorsInit {

	static String betweenParametersSeparator = ",";
	static String fileName = "Telegraf/telegraf.conf";

	static void initServer() throws IOException {

	}

	static void setConfigSettings(String global_tags, String agent) throws IOException {
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

		stringBuilder.append("[outputs.http]\n");
		stringBuilder.append(addHTTPPlugin(""));
		stringBuilder.append("[outputs.http.headers]\n");
		stringBuilder.append("  Content-Type = \"text/plain; charset=utf-8\"");

		bufferedWriter.write(stringBuilder.toString());
		bufferedWriter.close();
	}

	static void createConfig(String global_tags, String agentParameters) throws IOException {
		StringBuilder agent = new StringBuilder(agentParameters);
		if (agentParameters.equals("")) {
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

	static String addHTTPPlugin(String HTTPParameters) {
		StringBuilder parameters = new StringBuilder(HTTPParameters);

		if (HTTPParameters.equals("")) {
			parameters.append("  url = \"http://localhost:31415/monitors/uuid\"\n");
			parameters.append("  timeout = \"5s\"\n");
			parameters.append("  method = \"POST\"\n");
			parameters.append("  timeout = \"5s\"\n");
			parameters.append("  data_format = \"json\"\n");
		}

		return parameters.toString();
	}

	static void run() throws IOException {
		Runtime.getRuntime().exec("\".\\Telegraf\\telegraf.exe\" --config \".\\Telegraf\\telegraf.conf\"");
	}

	public static void main(String[] args) throws IOException {
		createConfig("", "");
		addHTTPPlugin("");

		TelegrafSensors tel = new TelegrafSensors();
		Map<String, String> map = new HashMap<>();
		map.put("cpu", "totalcpu = true,percpu = true");

		tel.addPlugin("inputs", map);
//		run();
	}

}

