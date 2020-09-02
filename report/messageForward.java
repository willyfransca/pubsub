/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package report;

import core.DTNHost;
import core.Message;
import core.MessageListener;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author asus
 */
public class messageForward extends Report implements MessageListener {

    public static Map<DTNHost, Integer> catat = new HashMap<DTNHost, Integer>();

    @Override
    public void newMessage(Message m) {
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
        if (catat.containsKey(from)) {
            catat.put(from, catat.get(from) + 1);
        }
        else{
            catat.put(from, 1);
        }
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
