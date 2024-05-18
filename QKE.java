import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
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
            //add qubits with random polarisations and values
            qubitArrayList.add(new Qubit()); 
        }
        sender.setQubitStream(qubitArrayList);
     

        // receiver receives stream of qubits from sender (simulated via getters/setters)

        //copy qubit stream from sender to receiver
        ArrayList<Qubit> copiedQubitStream = new ArrayList<>();
        for (Qubit qubit : sender.getQubitStream()) {
            copiedQubitStream.add(new Qubit(qubit.getValue(), qubit.getPolarization()));
        }
        receiver.setQubitStream(copiedQubitStream);

        // receiver measures received qubit stream, changing the recorded polarizations & values in the qubitstream
        for (Qubit qubit : receiver.getQubitStream()) {
            int randomPolarization = (int) Math.round(Math.random()); // random number of 0 or 1
            qubit.measure(randomPolarization);
        }

        //simulate exchanging polarizations between sender and receiver using getter/setter
        //does this by comparing the polarizations of sneder and receivers qubit streams
        sender.setKey(sender.getQubitStream(), receiver.getQubitStream());
        receiver.setKey(sender.getQubitStream(), receiver.getQubitStream());
        System.out.println("receiver's keys: " + receiver.getKey());
        System.out.println("sender's keys: " + sender.getKey());

        //sender encrypts chars typed in console and sends to receiver, receiver decrypts message and prints to console
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        try{
            while(true){
                String messageToSend = reader.readLine();
                //quit program if "quit" is typed into console
                if(messageToSend.equals("quit")){
                    break;
                }

                //convert message to byte array
                byte[] messageBytes = messageToSend.getBytes(StandardCharsets.UTF_8);
                
                //encrypt
                byte[] encryptedMessage = Cipher.xor(messageBytes, sender.getKey().toString());
                //set sender's encrypted msg
                sender.setEncryptedMessage(encryptedMessage); 
                System.out.println("encrypted msg: " + encryptedMessage);

                //send to receiver
                receiver.setEncryptedMessage(sender.getEncryptedMessage());
                System.out.println("receiver's encrypted message: " + receiver.getEncryptedMessage());

                //receiver decrypts message and prints to console
                byte[] decryptedMessage = Cipher.xor(receiver.getEncryptedMessage(), receiver.getKey().toString());
                String decryptedMessageString = new String(decryptedMessage, StandardCharsets.UTF_8);
                System.out.println("receiver's decrypted message: " + decryptedMessageString);


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
    private byte[] messageBytes;

    // constructor
    endPoint() {
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

    public byte[] getEncryptedMessage(){
        return this.messageBytes;
    }

    // setter
    public void setKey(ArrayList<Qubit> senderStream, ArrayList<Qubit> receiverStream){
         //compare polarisation types and append to key the value of qubits with matching polarizations
         StringBuilder key = new StringBuilder();
         for(int i =0; i < qubitStream.size(); i++){
             if(senderStream.get(i).getPolarization() == receiverStream.get(i).getPolarization()){
                 this.key.append(senderStream.get(i).getValue());
              }
         }
    }

    public void setQubitStream(ArrayList<Qubit> qubitStream) {
        this.qubitStream = qubitStream;
    }

    public void setEncryptedMessage(byte[] message) {
        this.messageBytes = message;
    }

  
}