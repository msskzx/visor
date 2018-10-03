package de.uniulm.omi.cloudiator.visor.sensors;

import de.uniulm.omi.cloudiator.visor.exceptions.MeasurementNotAvailableException;
import de.uniulm.omi.cloudiator.visor.exceptions.SensorInitializationException;
import de.uniulm.omi.cloudiator.visor.monitoring.AbstractSensor;
import de.uniulm.omi.cloudiator.visor.monitoring.Measurement;
import de.uniulm.omi.cloudiator.visor.monitoring.MonitorContext;
import de.uniulm.omi.cloudiator.visor.monitoring.SensorConfiguration;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Set;

public class TelegrafSensor extends AbstractSensor implements Runnable{

	String betweenParametersSeparator = ",";
	String fileName = "./Telegraf/telegraf.conf";

	Thread thread;
	String threadName;

	String sensorType;
	Map<String, String> sensorConfiguration;
	String sensorMetrics;
	String sensorName;

	public TelegrafSensor(String sensorType, SensorConfiguration sensorConfiguration) {
		this.sensorType = sensorType;
		this.sensorConfiguration = sensorConfiguration.getConfiguration();
		run();
	}

	@Override
	protected void initialize(MonitorContext monitorContext, SensorConfiguration sensorConfiguration)
			throws SensorInitializationException {
		super.initialize(monitorContext, sensorConfiguration);

		this.sensorType = sensorType;
		this.sensorConfiguration = sensorConfiguration.getConfiguration();
		// run();
	}

	/**
	 * read the config file and add the required inputs or outputs plugins
	 *
	 * sensorType: "inputs" / "outputs"
	 *
	 * sensorConfiguration: <"plugin:cpu", "parameters:totalcpu = true,percpu = true">
	 *
	 * @throws IOException
	 */
	synchronized void addPlugin() throws IOException {

		BufferedReader bufferedReader = new BufferedReader(new FileReader(fileName));
		StringBuilder stringBuilder = new StringBuilder("");

		String st = "";
		while ((st = bufferedReader.readLine()) != null) {
			stringBuilder.append(st + "\n");
		}
		bufferedReader.close();

		BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(fileName));

		for (Entry<String, String> entry : sensorConfiguration.entrySet()) {
			sensorName = entry.getKey();
			stringBuilder.append("[[" + sensorType + ":" + sensorName + "]]\n");
			String[] parameters = entry.getValue().split(betweenParametersSeparator);
			for (int i = 0; i < parameters.length; i++) {
				stringBuilder.append("  " + parameters[i] + "\n");
			}

		}

		stringBuilder.append("\n");
		bufferedWriter.write(stringBuilder.toString());

		bufferedWriter.close();
	}


	@Override
	protected Set<Measurement> measureSet() {
		Set<Measurement> set = new HashSet<>();
		//TODO
		return set;
	}

	public void setSensorMetrics(String metrics) {
		this.sensorMetrics = metrics;
	}

	public String getSensorName() {
		return sensorName;
	}


	/**
	 * called to add a plugin to the config file
	 */
	@Override
	public void run() {
		try {
			addPlugin();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void start() {
		threadName = "thread"+ new Random().nextInt(100000);
		thread = new Thread(this, threadName);
		thread.start();
	}

}
