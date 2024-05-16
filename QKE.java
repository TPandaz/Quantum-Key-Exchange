import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.SecureRandom;
import java.util.ArrayList;

public class QKE {
    public static void main(String[] args) {
        if (args.length != 1) {
            System.err.println("Usage: java QKE <QubitStreamLength>");
            System.exit(1);
        }
        int qubitLength = Integer.parseInt(args[0]);

        // create 2 endpoint machines
        endPoint sender = new endPoint();
        endPoint receiver = new endPoint();

        // generate stream of random qubits for sender
        ArrayList<Qubit> qubitArrayList = new ArrayList<>(qubitLength);
        for (int i = 0; i < qubitLength; i++) {
            qubitArrayList.add(new Qubit()); // add random qubits
        }
        sender.setQubitStream(qubitArrayList);

        // receiver receives stream of qubits from sender (simulated via
        // getters/setters)

        // create deep copy of the sender qubit stream and set it to receiver qubitstream
        ArrayList<Qubit> copiedQubitStream = new ArrayList<>();
        for (Qubit qubit : sender.getQubitStream()) {
            copiedQubitStream.add(new Qubit(qubit.getValue(), qubit.getPolarization()));
        }
        receiver.setQubitStream(copiedQubitStream);
        // currently does not know polarisations & values for qubit stream so it
        // measures it, changing the recorded values in the receivers qubitstream
        for (Qubit qubit : receiver.getQubitStream()) {
            int randomPolarization = (int) Math.round(Math.random()); // random number of 0 or 1
            int value = qubit.measure(randomPolarization);
        }

        // // debug
        // System.out.println("sender qubits:");
        // for (Qubit qubit : sender.getQubitStream()) {
        //     System.out.println("polarization: " + qubit.getPolarization() + " value: " + qubit.getValue());
        // }
        // System.out.println();
        // System.out.println("receiver qubits:");
        // for (Qubit qubit : receiver.getQubitStream()) {
        //     System.out.println("polarization: " + qubit.getPolarization() + " value: " + qubit.getValue());
        // }

        //simulate exchanging polarizations between sender and receiver
        sender.setKey(sender.getQubitStream(), receiver.getQubitStream());
        receiver.setKey(sender.getQubitStream(), receiver.getQubitStream());
        System.out.println(receiver.getKey() + " " + sender.getKey());
        
        //sender encrypts chars to console and send to receiver, receiver decrypts message and prints to console
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        try{
            while(true){
                String messageToSend = reader.readLine();
                //quit program if message is quit
                if(messageToSend.equals("quit")){
                    break;
                }
                String encryptedMessage = Cipher.xor(messageToSend, sender.getKey().toString());
                System.out.println(encryptedMessage);
            }
        }catch(IOException e){
            e.printStackTrace();
        }
       
    }

}

// this class emulates the sender/receiver machines
class endPoint {
    private ArrayList<Qubit> qubitStream;
    private StringBuilder key = new StringBuilder();
    private String encryptedMessage;
    private String ReceivedMessage;
    private SecureRandom numGenerator = new SecureRandom();

    // constructor
    endPoint() {
    }

    public int randomPolarization() {
        return numGenerator.nextInt(2);
    }

    // getter
    public ArrayList<Qubit> getQubitStream() {
        if (this.qubitStream != null) {
            return this.qubitStream;
        } else {
            return null;
        }
    }

    public StringBuilder getKey(){
        return this.key;
    }

    // setter
    public void setKey(ArrayList<Qubit> senderStream, ArrayList<Qubit> receiverStream){
         //compare polarisation types and append to key the value of qubits with matching polarizations
         StringBuilder key = new StringBuilder();
         for(int i =0; i < qubitStream.size(); i++){
             if(senderStream.get(i).getPolarization() == receiverStream.get(i).getPolarization()){
                 this.key.append(senderStream.get(i).getValue());
                 //System.out.println(senderStream.get(i).getValue());
             }
         }
    }

    public void setQubitStream(ArrayList<Qubit> qubitStream) {
        this.qubitStream = qubitStream;
    }

    public void setMessage(String message) {
        this.encryptedMessage = message;
    }

    public void setReceivedMessage(String message) {
        this.ReceivedMessage = message;
    }
}