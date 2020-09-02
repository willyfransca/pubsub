/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package report;

import core.ConnectionListener;
import core.DTNHost;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author asus
 */
public class totalKontak extends Report implements ConnectionListener{

    public Map<DTNHost, Integer> catat =  new HashMap<DTNHost,Integer>();
    @Override
    public void hostsConnected(DTNHost host1, DTNHost host2) {
        if(catat.containsKey(host1)){
            catat.put(host1, catat.get(host1)+1);
        }else{
            catat.put(host1, 1);
        }
    }

    @Override
    public void hostsDisconnected(DTNHost host1, DTNHost host2) {
    }
    @Override
    public void done() {
        for (Map.Entry<DTNHost, Integer> entry : catat.entrySet()) {
            DTNHost key = entry.getKey();
            Integer value = entry.getValue();
            write(key+"\t"+value);
        }
        super.done();
    }
}
