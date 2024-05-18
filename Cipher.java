
public final class Cipher {
    // private constructor as an instance of this class is not required
    private Cipher() {}

    public static byte[] xor(byte[] messageBytes, String key) {
        // create new byte array filled with 0s
        byte[] encryptedMessage = new byte[messageBytes.length];

        // for every bit in byte array
        for (int i = 0; i < (messageBytes.length * 8); i++) {
            // get the current char in key and make it a bit value
            char keyChar = key.charAt(i % key.length());
            int keyBit = keyChar - '0'; // convert character to bit value by subtracting ascii value of 48 from it
            // get current bit from byte array
            int messageBytesArrayIndex = i / 8; //which byte loop is currently at
            int bitShiftPosition = 7 - (i % 8); // position of bit to rightshift by
            byte messageByte = messageBytes[messageBytesArrayIndex]; // current byte value we are xoring
            int messageBit = (messageByte >>> bitShiftPosition) & 1; // shift and mask to get leftmost bit

            // xor the bit with key
            int encryptedBit = messageBit ^ keyBit;
            // left shift by 1 and OR with encryptedbit(appends to the right)
            encryptedMessage[messageBytesArrayIndex] = (byte) ((encryptedMessage[messageBytesArrayIndex] << 1)
                    | encryptedBit);
        }

        System.out.println("XORed message in binary:");
        for (byte b : encryptedMessage) {
            System.out.println(Integer.toBinaryString(b & 0xFF)); // Ensure byte is treated as unsigned
        }

        return encryptedMessage;

    }

}
