/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package report;

import core.ConnectionListener;
import core.DTNHost;
import core.Message;
import core.MessageListener;
import core.SimClock;
import core.UpdateListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Jarkom
 */
public class latencyReport5 extends Report implements MessageListener, UpdateListener {

    //map simpan channel, list latencynya
    private Map<String, List<Double>> latencies = new HashMap<String, List<Double>>();
    private int totalContact = 0;
    private int lastRecord = 0;
    private int interval = 7200;
    private Map<Integer, Double> nrofLatency = new HashMap<Integer, Double>();

    @Override
    public void newMessage(Message m) {
        //mencatat kapan pesan di buat
        if (!m.getContentType().equalsIgnoreCase("SUBS") && latencies.size()<5) {
            if (!latencies.containsKey(m.getContentType())) {
                List<Double> a = new ArrayList<>();
                latencies.put(m.getContentType(), a);
                System.out.println(latencies);
                System.out.println(latencies.size());
            }
        }
    }

    @Override
    public void messageTransferStarted(Message m, DTNHost from, DTNHost to) {

    }

    @Override
    public void messageDeleted(Message m, DTNHost where, boolean dropped) {
    }

    @Override
    public void messageTransferAborted(Message m, DTNHost from, DTNHost to) {
    }

    @Override
    public void messageTransferred(Message m, DTNHost from, DTNHost to, boolean firstDelivery) {
        if (firstDelivery && !m.getContentType().equalsIgnoreCase("SUBS")) {
            double latenciesValue = SimClock.getIntTime() - m.getCreationTime();
            List<Double> temp = latencies.get(m.getContentType());
            temp.add(latenciesValue);
            latencies.put(m.getContentType(), temp);
        }
    }

    @Override
    public void done() {
        String statsText = "Contact\tLatencies\n";
        for (Map.Entry<Integer, Double> entry : nrofLatency.entrySet()) {
            Integer key = entry.getKey();
            Double value = entry.getValue();
            statsText += key + "\t" + value + "\n";
        }
        write(statsText);
        super.done();
    }

    @Override
    public void updated(List<DTNHost> hosts) {

        if (SimClock.getIntTime() - lastRecord >= interval) {
            double temp = 0,total=0;
            for (Map.Entry<String, List<Double>> entry : latencies.entrySet()) {
                String key = entry.getKey();
                List<Double> value = entry.getValue();
               double temp1=0;
               if(!value.isEmpty()){
                   temp1=Double.parseDouble(getAverage(value));
                   System.out.println(temp1);
               }
                temp=temp+temp1;
            }
            total = temp/5;
            nrofLatency.put(lastRecord, temp);
            lastRecord = SimClock.getIntTime() - SimClock.getIntTime() % interval;
        }
    }
}
