import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class QKE {
    public static void main(String[] args) {
        if (args.length != 1) {
            System.err.println("Usage: java QKE <QubitStreamLength(int)>");
            System.exit(1);
        }
        if(Integer.parseInt(args[0]) <= 0){
            System.err.println("qubitStreamLength should be a positive number");
            System.exit(1);
        }
        int qubitLength = Integer.parseInt(args[0]);

        // create 2 endpoint machines
        EndPoint sender = new EndPoint();
        EndPoint receiver = new EndPoint();
        boolean validKey = false;;

        while(!validKey){
            generateKey(qubitLength, sender, receiver);
            String key = sender.getKey().toString();
            validKey = isKeyValid(key);
        }

        System.out.println("receiver's keys: " + receiver.getKey());
        System.out.println("sender's keys: " + sender.getKey());
        System.out.println("Type Message below(type quit to exit program): ");

        // sender encrypts chars typed in console and sends to receiver, receiver
        // decrypts message and prints to console
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        try {
            while (true) {
                String messageToSend = reader.readLine();
                // quit program if "quit" is typed into console
                if (messageToSend.equals("quit")) {
                    break;
                }
                // convert message to byte array
                byte[] messageBytes = messageToSend.getBytes(StandardCharsets.UTF_8);
                // encrypt
                byte[] encryptedMessage = Cipher.xor(messageBytes, sender.getKey().toString());
                // set sender's encrypted msg
                sender.setEncryptedMessage(encryptedMessage);
                System.out.println("encrypted msg: " + encryptedMessage);
                // send to receiver
                receiver.setEncryptedMessage(sender.getEncryptedMessage());
                System.out.println("receiver's encrypted message: " + receiver.getEncryptedMessage());
                // receiver decrypts message and prints to console
                byte[] decryptedMessage = Cipher.xor(receiver.getEncryptedMessage(), receiver.getKey().toString());
                String decryptedMessageString = new String(decryptedMessage, StandardCharsets.UTF_8);
                System.out.println("receiver's decrypted message: " + decryptedMessageString);

            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void generateKey(int qubitLength, EndPoint sender, EndPoint receiver){
         // generate stream of random qubits for sender
         ArrayList<Qubit> qubitArrayList = new ArrayList<>(qubitLength);
         for (int i = 0; i < qubitLength; i++) {
             // add qubits with random polarisations and values
             qubitArrayList.add(new Qubit());
         }
         sender.setQubitStream(qubitArrayList);
         // copy qubit stream from sender to receiver
         ArrayList<Qubit> copiedQubitStream = new ArrayList<>();
         for (Qubit qubit : sender.getQubitStream()) {
             copiedQubitStream.add(new Qubit(qubit.getValue(), qubit.getPolarization()));
         }
         receiver.setQubitStream(copiedQubitStream);
         // receiver measures received qubit stream, changing the recorded polarizations
         // & values in the qubitstream
         for (Qubit qubit : receiver.getQubitStream()) {
             int randomPolarization = (int) Math.round(Math.random()); // random number of 0 or 1
             qubit.measure(randomPolarization);
         }
         sender.setKey(sender.getQubitStream(), receiver.getQubitStream());
         receiver.setKey(sender.getQubitStream(), receiver.getQubitStream());
    }

    public static boolean isKeyValid(String key){
        //checks if key is not null, not empty, not "0", not a number other than 0 or 1, not a string of only 0s
        if(key == null || key.isEmpty() || key.equals("0") || !key.matches("^[01]+$") || key.matches("^0*$")){
            System.out.println("Key not valid...reestablishing keys");
            return false;
        }else{
            return true;
        }
    }

}
