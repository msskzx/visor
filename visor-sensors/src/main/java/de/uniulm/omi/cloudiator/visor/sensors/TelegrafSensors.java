package de.uniulm.omi.cloudiator.visor.sensors;

import de.uniulm.omi.cloudiator.visor.exceptions.SensorInitializationException;
import de.uniulm.omi.cloudiator.visor.monitoring.AbstractSensor;
import de.uniulm.omi.cloudiator.visor.monitoring.MonitorContext;
import de.uniulm.omi.cloudiator.visor.monitoring.SensorConfiguration;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;


public class TelegrafSensors extends AbstractSensor {

  @Override
  protected void initialize(MonitorContext monitorContext,
      SensorConfiguration sensorConfiguration) throws SensorInitializationException {
    super.initialize(monitorContext, sensorConfiguration);
    
    /*
	 * <String, String>
	 * 
	 * <"cpu", "totalcpu = true,percpu = true">
	 */
    
    StringBuilder sb = new StringBuilder("");
    String betweenParametersSeparator = ",";		
    String fileName = "telegraf.conf";
	BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(fileName));
			
	for (Entry<String, String> entry : sensorConfiguration.entrySet()) 
	{
		sb.append("[[inputs." + entry.getKey() + "]]\n");
		String[] parameters = entry.getValue().split(betweenParametersSeparator);
		for(int i = 0;i<parameters.length;i++){
			sb.append("  " + parameters[i]+"\n");
		}
		
	}
		
	bufferedWriter.write(sb.toString());
	bufferedWriter.close();
  }
  
  void run() throws IOException {
		Runtime.getRuntime().exec("telegraf.exe");
	}
  
}
