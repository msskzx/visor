package de.uniulm.omi.cloudiator.visor.sensors;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.Map;

public class TelegrafSensorsInit {


    static String betweenParametersSeparator = ",";
    static String fileName = "Telegraf/telegraf.conf";
    static Map<String, Boolean> runningSensors;
    static int PORT = 9000;
    static String route = "/monitors";
    static String url = "http://localhost:" + PORT + route;


    static void initServer() throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(PORT), 0);
        server.createContext(route, new TelegrafHTTPHandler());
        server.setExecutor(null);
        server.start();
    }


    /**
     * creates the config file and adds some global settings to it
     *
     * @param global_tags
     * @param agentParameters
     * @throws IOException
     */
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


    /**
     * HTTP plugin responsible for sending the output metrics
     *
     * @param HTTPParameters
     * @return
     */
    static String addHTTPPlugin(String HTTPParameters) {
        StringBuilder parameters = new StringBuilder(HTTPParameters);

        if (HTTPParameters.equals("")) {
            parameters.append("  url = \"" + url + "\"\n");
            parameters.append("  timeout = \"5s\"\n");
            parameters.append("  method = \"POST\"\n");
            parameters.append("  timeout = \"5s\"\n");
            parameters.append("  data_format = \"json\"\n");
        }

        return parameters.toString();
    }


    /**
     * run the telegraf sensors using this specific config file
     *
     * @throws IOException
     */
    static void run() throws IOException {
        Runtime.getRuntime().exec("\".\\Telegraf\\telegraf.exe\" --config \".\\Telegraf\\telegraf.conf\"");
    }


    /**
     * check if the sensor the user wants to run, is already running
     * <p>
     * if not add it to the list of running sensors
     *
     * @param sensor
     * @return boolean
     */
    boolean alreadyRunningSensor(String sensor) {
        if (runningSensors.containsKey(sensor))
            return true;
        runningSensors.put(sensor, true);
        return false;
    }


    static class TelegrafHTTPHandler implements HttpHandler {


        /**
         * @param exchange
         * @throws IOException
         */
        public void handle(HttpExchange exchange) throws IOException {
            exchange.sendResponseHeaders(200, 0);
            OutputStream responseBody = exchange.getResponseBody();
        }
    }
}
