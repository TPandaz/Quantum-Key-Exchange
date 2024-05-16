import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;

public class Bob {
    public static void main(String[] args){
        if(args.length != 1){
            System.err.println("Usage: java Alice <QubitStreamLength>");
            System.exit(1);
        }
        try{
            int qubitLength = Integer.parseInt(args[0]);
            Socket aliceSocket = new Socket("localhost", 54321);
            
            InputStream inputStream = aliceSocket.getInputStream();
            OutputStream outputStream = aliceSocket.getOutputStream();

            //receive stream of qubits and place into arraylist
            byte[] receivedBytes = new byte[qubitLength];
            int bytesRead = inputStream.read(receivedBytes);
              
            ArrayList<Qubit> receivedQubits = new ArrayList<>();
            for (int i = 0; i < qubitLength; i++) {
                byte combinedByte = receivedBytes[i];
                // Extract polarization (MSB)
                int polarization = (int)(combinedByte & 2) >> 1; // AND with 2 to get MSB, then right shift
                // Extract value (LSB)
                int value = combinedByte & 1;
                System.out.println("combined byte: " + Integer.toBinaryString(combinedByte));
                System.out.println("received polarization: " + polarization + " value: " + value);
                receivedQubits.add(new Qubit(value, polarization));
                System.out.println("receivedqubits actual polarisation: " + receivedQubits.get(i).getPolarization());
            }
            

            //measure qubits with random polarization and place into arraylist
            ArrayList<Qubit> qubitStream = new ArrayList<>();
            for(int i = 0; i < qubitLength; i++){
                // int polarization = (int)Math.round(Math.random());//random number of 0 or 1
                // int value = receivedQubits.get(i).measure(polarization);
                qubitStream.add(new Qubit(1, 0));
            }

            //send own qubitStream to Alice
            ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
            for (Qubit qubit : qubitStream) {
                // Combine polarization and value into a single byte
                byte combinedByte = (byte) ((qubit.getPolarization() << 1) | qubit.getValue());
                byteStream.write(combinedByte);
                System.out.println("Combined byte: " + Integer.toBinaryString(combinedByte));
              }
            outputStream.write(byteStream.toByteArray());
            outputStream.flush();

            //debugger
            for(Qubit qubit : receivedQubits){
                System.out.println("received qubits:");
                System.out.println("polarization: " + qubit.getPolarization() + " value: " + qubit.getValue());
            }
            System.out.println();
            for(Qubit qubit : qubitStream){
                System.out.println("qubitStream:");
                System.out.println("polarization: " + qubit.getPolarization() + " value: " + qubit.getValue());
            }
            //generate key
            StringBuilder key = getSecretKey(qubitStream, receivedQubits);
            System.out.println(key);

            inputStream.close();
            outputStream.close();
            aliceSocket.close();
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    private static StringBuilder getSecretKey(ArrayList<Qubit> qubitStream, ArrayList<Qubit> receivedQubits){
        //compare polarisation types and append to key the value of qubits with matching polarizations
        StringBuilder key = new StringBuilder();
        
        for(int i =0; i < qubitStream.size(); i++){
            if(qubitStream.get(i).getPolarization() == receivedQubits.get(i).getPolarization()){
                key.append(qubitStream.get(i).getValue());
            }
        }
        return key;
    }
}
