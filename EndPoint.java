import java.util.ArrayList;
//emulates sender/receiver
public class EndPoint {
    private ArrayList<Qubit> qubitStream;
    private StringBuilder key = new StringBuilder();
    private byte[] messageBytes;

    // constructor
    public EndPoint() {}

    public ArrayList<Qubit> getQubitStream() {
            return this.qubitStream;
    }

    public StringBuilder getKey() {
        return this.key;
    }

    public byte[] getEncryptedMessage() {
        return this.messageBytes;
    }

    // setter
    public void setKey(ArrayList<Qubit> senderStream, ArrayList<Qubit> receiverStream) {
        if(senderStream.size() != receiverStream.size()){
            throw new IllegalArgumentException("Qubit stream lengths are different!");
        }
        // compare polarisation types and append to key the value of qubits with matching polarizations
        key.setLength(0);
        for (int i = 0; i < senderStream.size(); i++) {
            if (senderStream.get(i).getPolarization() == receiverStream.get(i).getPolarization()) {
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
