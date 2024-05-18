import java.util.ArrayList;
//emulates sender/receiver
public class EndPoint {
    private ArrayList<Qubit> qubitStream;
    private StringBuilder key = new StringBuilder();
    private byte[] messageBytes;

    // constructor
    public EndPoint() {}

    public ArrayList<Qubit> getQubitStream() {
        if (this.qubitStream != null) {
            return this.qubitStream;
        } else {
            return null;
        }
    }

    public StringBuilder getKey() {
        return this.key;
    }

    public byte[] getEncryptedMessage() {
        return this.messageBytes;
    }

    // setter
    public void setKey(ArrayList<Qubit> senderStream, ArrayList<Qubit> receiverStream) {
        // check if streams are null, throw exception if so
        if (senderStream == null || receiverStream == null) {
            throw new IllegalArgumentException("Qubit streams cannot be null");
        }
        // compare polarisation types and append to key the value of qubits with
        // matching polarizations
        for (int i = 0; i < senderStream.size(); i++) {
            key.setLength(0);
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
