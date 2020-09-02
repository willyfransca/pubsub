package routing;

import core.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import routing.DecisionEngineRouter;
import routing.MessageRouter;
import report.interestEngine;

/**
 * @author willy Sanata Dharma University
 */
public class PubSub implements RoutingDecisionEngine {

    public static final String CONTENT = "popularity";
    private static int waktu = 604800;
    //untuk menyimpan cahnnel apa yang lagi popular
    public Map<String, Integer> popularity ;
    //menyimpan pesan request subscriber
    public Map<DTNHost, Message> subscriptionList;
    private String status = "relay";
    private String interest;
    
    public PubSub(Settings s) {
    }

    public PubSub(PubSub proto) {
        super();
        this.popularity = proto.popularity;
        popularity= new HashMap<String, Integer>();
        subscriptionList = new HashMap<DTNHost, Message>();
        interest = "";
    }

    @Override
    public void connectionUp(DTNHost thisHost, DTNHost peer) {
    }

    @Override
    public void connectionDown(DTNHost thisHost, DTNHost peer) {
    }

    @Override
    public void doExchangeForNewConnection(Connection con, DTNHost peer) {

        DTNHost ahost = con.getOtherNode(peer);
        DecisionEngineRouter other = (DecisionEngineRouter) (peer.getRouter());
        DecisionEngineRouter thisHost = (DecisionEngineRouter) (ahost.getRouter());

        //pertukaran interest
        if (!status.equals("publiser") && !getOtherDecisionEngine(peer).status.equalsIgnoreCase("publiser")) {

            for (Map.Entry<DTNHost, Message> entry : getOtherDecisionEngine(peer).subscriptionList.entrySet()) {
                DTNHost keyPeer = entry.getKey();
                Message valuePeer = entry.getValue();
                //cek sudah ada host itu belum
                if (!subscriptionList.containsKey(keyPeer)) {
                    subscriptionList.put(keyPeer, valuePeer);
                } else {
                    // jika sudah ada cek velue nya  untuk perbaharui pesan yang baru
                    if (subscriptionList.get(keyPeer).getCreationTime() < valuePeer.getCreationTime()) {
                        subscriptionList.put(keyPeer, valuePeer);
                    }
                }
            }
        }
    }

    @Override
    public boolean newMessage(Message m,DTNHost h) {
        //untuk pubisher
        if (!m.getContentType().equalsIgnoreCase("SUBS")) {
            interest=m.getId();
//            h.getRouter().resetInterest(m.getId());
            this.status= "publisher";
        } //untuk subscriber
        else {
            this.status = "subscriber";
//memasukkan perubahan interest buat subscriber
            interest=m.getId();

//===============================
// update subscription list
            if (!subscriptionList.containsKey(h)) {
                subscriptionList.put(h, m);
            } else {
                // jika sudah ada cek velue nya  untuk perbaharui pesan yang baru
                if (subscriptionList.get(h).getCreationTime() < m.getCreationTime()) {
                    subscriptionList.put(h, m);
                }
            }
//================================

//menghitung jumlah node yang subscribe pada channel tertentu
            if (popularity.containsKey(m.getId())) {
                popularity.put(m.getId(), popularity.get(m.getId()) + 1);
            } else {

                popularity.put(m.getId(), 1);
            }
        }
        return true;
    }

    @Override
    public boolean isFinalDest(Message m, DTNHost aHost) {
        if (status.equalsIgnoreCase("subscriber")) {
                if (interest.equals(m.getContentType())) {
                    return true;
//                }
            }
        }
        return false;
    }

    @Override
    public boolean shouldSaveReceivedMessage(Message m, DTNHost thisHost) {
     //kalau publisher tidak menyimpan pesan 
        if (status.equalsIgnoreCase("publisher")) {
            return false;
        } else {
            //pesan yg di terima request dari subscriber
            //algoritma hopCount Filtering
            if (!hopCountFilter(thisHost, m)) {
                return false;
            }
            return true;
        }
    }

    @Override
    public boolean shouldSendMessageToHost(Message m, DTNHost otherHost) {
        //pesan broadcast subscriber tidak di kirimkan
        if (m.getContentType().equalsIgnoreCase("SUBS")) {
            //queue dengan popularity
            return false;
        } else {
            //pengiriman berdasarkan subscription list peer
            for (Map.Entry<DTNHost, Message> entry : getOtherDecisionEngine(otherHost).subscriptionList.entrySet()) {
                DTNHost key = entry.getKey();
                Message value = entry.getValue();
                if (value.getId().equals(m.getContentType())) {
                    popularity(m);
                    return true;
                }
            }
            return false;
        }

    }

    @Override
    public boolean shouldDeleteSentMessage(Message m, DTNHost otherHost) {
        return false;
    }

    @Override
    public boolean shouldDeleteOldMessage(Message m, DTNHost hostReportingOld) {
        return true;
    }

    private PubSub getOtherDecisionEngine(DTNHost h) {
        MessageRouter otherRouter = h.getRouter();
        assert otherRouter instanceof DecisionEngineRouter : "This router only works "
                + " with other routers of same type";

        return (PubSub) ((DecisionEngineRouter) otherRouter).getDecisionEngine();
    }

    @Override
    public RoutingDecisionEngine replicate() {
        return new PubSub(this);
    }

    //sisi penerima
    public boolean hopCountFilter(DTNHost thisHost, Message m) {
        //mengecek ada pesan tidak di buffernya
        if (thisHost.getRouter().hasMessage(m.getId())) {
            //kalau ada ambil pesan tersebut
            int msgMeHost = thisHost.getRouter().getMessage(m.getId()).getHopCount();
            int msgPeer = m.getHopCount();

            if (msgMeHost > msgPeer) {//bandingkan hop count m1 & m2
//                System.out.println("hapus");
                thisHost.getRouter().removeFromMessages(m.getId());
            } else {
                //kalau m1 lebih kecil dari m2 tidak usah di simpan
                return false;
            }
        }
        return true;
    }

    public void popularity(Message m) {
        m.updateProperty(CONTENT, popularity.get(m.getContentType()));

    }
    @Override
    public void update(DTNHost host) {
        if (SimClock.getIntTime() - waktu == 0) {
            popularity.clear();
            waktu = waktu + 604800;
        }
    }
}
