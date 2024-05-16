import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

/* socket code referenced from:
 * https://www.geeksforgeeks.org/how-to-implement-peer-to-peer-communication-in-java/
 */

public class Alice {

    public static void main(String[] args) {
        if (args.length != 1) {
            System.err.println("Usage: java Alice <QubitStreamLength>");
            System.exit(1);
        }
        try {
            ServerSocket ss = new ServerSocket(54321);
            System.out.println("Alice is waiting...");

            // wait for bob to connect to socket
            Socket bobSocket = ss.accept();
            System.out.println("Bob is connected!");

            InputStream inputStream = bobSocket.getInputStream();
            OutputStream outputStream = bobSocket.getOutputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

            // send stream of random qubits to bob
            int qubitLength = Integer.parseInt(args[0]);
            ArrayList<Qubit> qubitStream = new ArrayList<>(qubitLength);
            for (int i = 0; i < qubitLength; i++) {
                qubitStream.add(new Qubit()); //add random qubits
            }
            ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
         
            for (Qubit qubit : qubitStream) {
                // pack polarization and value into a single byte
                byte combinedByte = (byte) ((qubit.getPolarization() << 1) | qubit.getValue());
                byteStream.write(combinedByte);
                System.out.println("combined byte: " + Integer.toBinaryString(combinedByte));
              }
            outputStream.write(byteStream.toByteArray());
            outputStream.flush();


            // receive byte stream from Bob and place into arraylist of qubits
            //polarization type is already known???
            byte[] receivedBytes = new byte[qubitLength ];
            int bytesRead = inputStream.read(receivedBytes);
            ArrayList<Qubit> receivedQubits = new ArrayList<>();
            for (int i = 0; i < qubitLength; i++) {
                byte combinedByte = receivedBytes[i];
                // get polarization (MSB)
                int polarization = (combinedByte & 2) >> 1; // AND with 2 to get MSB, then right shift
                // get value (LSB)
                int value = combinedByte & 1;
                System.out.println("received polarization: " + polarization + " value: " + value);
                receivedQubits.add(new Qubit(value, polarization));
            }

            //  //debugger
            //  System.out.println("received qubits:");
            //  for(Qubit qubit : receivedQubits){
            //     System.out.println("polarization: " + qubit.getPolarization() + " value: " + qubit.getValue());
            // }
            // System.out.println();
            // System.out.println("qubitStream:");
            // for(Qubit qubit : qubitStream){
            //     System.out.println("polarization: " + qubit.getPolarization() + " value: " + qubit.getValue());
            // }

            //generate key
            StringBuilder key = getSecretKey(qubitStream, receivedQubits);
            System.out.println(key);

            //communication loop
            while(true){
                String messageToSend = reader.readLine();
                //quit program if message is quit
                if(messageToSend.equals("quit")){
                    break;
                }
                String encryptedMessage = Cipher.xor(messageToSend, key.toString());
                System.out.println(encryptedMessage);
            }

            inputStream.close();
            outputStream.close();
            bobSocket.close();
            ss.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static StringBuilder getSecretKey(ArrayList<Qubit> qubitStream, ArrayList<Qubit> receivedQubits) {
        // compare polarisation types and append to key the value of qubits with
        // matching polarizations
        StringBuilder key = new StringBuilder();
        for (int i = 0; i < qubitStream.size(); i++) {
            if (qubitStream.get(i).getPolarization() == receivedQubits.get(i).getPolarization()) {
                key.append(qubitStream.get(i).getValue());
            }
        }
        return key;
    }

}