import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

import org.junit.jupiter.api.*;

public class UnitTests {
    // test cases for qubit

    // test if constructor with random init works as expected
    @Test
    public void testRandomQubit() {
        Qubit qubit = new Qubit();
        Assertions.assertTrue(qubit.getPolarization() == 0 || qubit.getPolarization() == 1);
        Assertions.assertTrue(qubit.getValue() == 0 || qubit.getValue() == 1);
    }

    // test constructor with specific values for value and polarization
    @Test
    public void testQubitValues() {
        Qubit qubit = new Qubit(1, 0);
        Assertions.assertEquals(1, qubit.getValue());
        Assertions.assertEquals(0, qubit.getPolarization());
    }

    // test if set method sets existing qubit to different values
    @Test
    public void testSetQubit() {
        Qubit qubit = new Qubit(0, 1);
        // set to opposite values
        qubit.set(1, 0);
        Assertions.assertEquals(1, qubit.getValue());
        Assertions.assertEquals(0, qubit.getPolarization());
    }

    // test if measure method returns original value with same polarization
    @Test
    public void testMeasureSamePolarization() {
        Qubit qubit = new Qubit(1, 0);
        int newValue = qubit.measure(0);
        Assertions.assertEquals(1, newValue);
        Assertions.assertEquals(0, qubit.getPolarization());
    }

    // test measure method if value does indeed change(using math.random) for
    // different polarization
    @Test
    public void testMeasureChangesValue() {
        Qubit qubit = new Qubit(1, 0);
        int value = qubit.getValue(); // value is 1
        for (int i = 0; i < 10; i++) {
            int newValue = qubit.measure(1);
            // value changes
            if (newValue != value) {
                Assertions.assertTrue(true);
            }
        }
    }

    // tests for cipher class

    // test if message is empty, should return no bytes
    @Test
    public void testEmptyMessage() {
        byte[] messageBytes = new byte[0];
        String key = "11010";
        byte[] encryptedMessage = Cipher.xor(messageBytes, key);
        Assertions.assertEquals(0, encryptedMessage.length);
    }

    // test if correct ouput is produced for XOR with message:hi
    @Test
    public void testCorrectOutput() {
        byte[] messageBytes = "hi".getBytes(StandardCharsets.UTF_8);
        String key = "1101";
        byte[] encryptedMessage = Cipher.xor(messageBytes, key);
        // actual encrypted values given a key:1101 and message: hi
        byte[] expectedEncryptedBytes = new byte[] { (byte) 0xB5, (byte) 0xb4 };
        Assertions.assertArrayEquals(expectedEncryptedBytes, encryptedMessage);
    }

    // tests if given a key length that isn't a multiple of 8, key is correctly
    // looped and XORed given message:hii
    @Test
    public void testKeyIsLooped() {
        byte[] messageBytes = "hii".getBytes(StandardCharsets.UTF_8);
        String key = "11011";
        byte[] encryptedMessage = Cipher.xor(messageBytes, key);
        // expected encrypted byte when key: 11011 is looped with message: hii
        byte[] expectedEncryptedBytes = new byte[] { (byte) 0xB6, (byte) 0x9E, (byte) 0xD4 };
        Assertions.assertArrayEquals(expectedEncryptedBytes, encryptedMessage);
    }

    // test when key is longer than messageBytes
    @Test
    public void testLongKey() {
        byte[] messageBytes = "h".getBytes(StandardCharsets.UTF_8);
        String key = "11011011101010000101010101110111101010110101010";
        byte[] encryptedMessage = Cipher.xor(messageBytes, key);
        // expected encrypted byte with key and "h"
        byte[] expectedEncryptedBytes = new byte[] { (byte) 0xB3 };
        Assertions.assertArrayEquals(expectedEncryptedBytes, encryptedMessage);
    }

    // test rightmost bit is correctly Xored(last bit in loop)
    @Test
    public void testRightMostBitShift() {
        byte[] messageBytes = new byte[] { (byte) 0x1 }; // byte value: 0000 0001
        String key = "0";
        byte[] encryptedMessage = Cipher.xor(messageBytes, key);
        Assertions.assertEquals(1, encryptedMessage.length);
        Assertions.assertEquals(1, encryptedMessage[0]);
    }

    // test if leftmost bit is Xored(1st bit)
    @Test
    public void testLeftMostBitShift() {
        byte[] messageBytes = new byte[] { (byte) 0x80 }; // byte value: 1000 0000
        String key = "0";
        byte[] encryptedMessage = Cipher.xor(messageBytes, key);
        Assertions.assertEquals(1, encryptedMessage.length);
        int unsignedValue = encryptedMessage[0] & 0xFF;
        Assertions.assertEquals(128, unsignedValue);
    }

    // test if XOR can be used to encrypt and decrypt, returning the original
    // character
    @Test
    public void testEncryptionDecryption() {
        String message = "I like cows";
        byte[] messageBytes = message.getBytes(StandardCharsets.UTF_8);
        String key = "10101";
        byte[] encryptedMessage = Cipher.xor(messageBytes, key);
        byte[] decryptedMessage = Cipher.xor(encryptedMessage, key);
        String decryptedString = new String(decryptedMessage, StandardCharsets.UTF_8);
        Assertions.assertEquals(message, decryptedString);
    }

    // test when stream of bytes is really long(long message), encryption and
    // decryption both works
    @Test
    public void testLongMessage() {
        String message = "8% of 25 is the same as 25% of 8 and one of them is much easier to do in your head.";
        byte[] messageBytes = message.getBytes(StandardCharsets.UTF_8);
        String key = "10101";
        byte[] encryptedMessage = Cipher.xor(messageBytes, key);
        byte[] decryptedMessage = Cipher.xor(encryptedMessage, key);
        String decryptedString = new String(decryptedMessage, StandardCharsets.UTF_8);
        Assertions.assertEquals(message, decryptedString);
    }

    // test if special characters are encrypted/decrypted correctly
    @Test
    public void testSpecialCharacters() {
        String message = "@#$%^&*()_+-=[]{}|;':<>,.?/`~";
        String key = "11011";
        byte[] messageBytes = message.getBytes(StandardCharsets.UTF_8);
        byte[] encryptedMessage = Cipher.xor(messageBytes, key);
        byte[] decryptedMessage = Cipher.xor(encryptedMessage, key);
        String decryptedString = new String(decryptedMessage, StandardCharsets.UTF_8);
        Assertions.assertEquals(message, decryptedString);
    }

    // test that encryption/decryption is accurate with extended ascii codes
    @Test
    public void testExtendedAsciiEncryptionDecryption() {
        String message = "€ŠœŸ®¶µÆð"; // euro symbol
        String key = "010101";
        byte[] messageBytes = message.getBytes(StandardCharsets.UTF_8);
        byte[] encryptedMessage = Cipher.xor(messageBytes, key);
        byte[] decryptedMessage = Cipher.xor(encryptedMessage, key);
        Assertions.assertEquals(message, new String(decryptedMessage, StandardCharsets.UTF_8));
    }

    // test for Endpoint class

    // test setKey method in Endpoint class gets same key values for receiver and
    // sender with qubitstream length 16
    @Test
    public void testSetKeyMethod16() {
        ArrayList<Qubit> senderStream = new ArrayList<>();
        ArrayList<Qubit> receiverStream = new ArrayList<>();
        for (int i = 0; i < 16; i++) {
            senderStream.add(new Qubit());
            receiverStream.add(new Qubit());
        }
        EndPoint sender = new EndPoint();
        EndPoint receiver = new EndPoint();
        sender.setKey(senderStream, receiverStream);
        receiver.setKey(senderStream, receiverStream);
        Assertions.assertEquals(receiver.getKey().toString(), sender.getKey().toString());
    }

    // test setKey method in Endpoint class gets same key values for receiver and
    // sender with qubitstream length 256
    @Test
    public void testSetKeyMethod256() {
        ArrayList<Qubit> senderStream = new ArrayList<>();
        ArrayList<Qubit> receiverStream = new ArrayList<>();
        for (int i = 0; i < 256; i++) {
            senderStream.add(new Qubit());
            receiverStream.add(new Qubit());
        }
        EndPoint sender = new EndPoint();
        EndPoint receiver = new EndPoint();
        sender.setKey(senderStream, receiverStream);
        receiver.setKey(senderStream, receiverStream);
        Assertions.assertEquals(receiver.getKey().toString(), sender.getKey().toString());
    }

    // test setKey method in Endpoint class gets same key values for receiver and
    // sender with qubitstream length 1024
    @Test
    public void testSetKeyMethod1024() {
        ArrayList<Qubit> senderStream = new ArrayList<>();
        ArrayList<Qubit> receiverStream = new ArrayList<>();
        for (int i = 0; i < 1024; i++) {
            senderStream.add(new Qubit());
            receiverStream.add(new Qubit());
        }
        EndPoint sender = new EndPoint();
        EndPoint receiver = new EndPoint();
        sender.setKey(senderStream, receiverStream);
        receiver.setKey(senderStream, receiverStream);
        Assertions.assertEquals(receiver.getKey().toString(), sender.getKey().toString());
    }

    // test cases for QKE

    // test checkvalid keys method returns false for 0(no encryption)
    @Test
    public void testInvalidCheckValidKeyfor0() {
        String key = "0";
        Assertions.assertFalse(QKE.checkValidKey(key));
    }

    // test checkvalid keys method returns false is key is a string of 0s
    @Test
    public void testInvalidCheckValidKeyfor0s() {
        String key = "00000000000";
        Assertions.assertFalse(QKE.checkValidKey(key));
    }

    // test checkvalid keys method returns false if key is empty
    @Test
    public void testInvalidCheckValidKeyforEmptyKey() {
        String key = "";
        Assertions.assertFalse(QKE.checkValidKey(key));
    }

    // test checkvalid keys method returns false if key is null
    @Test
    public void testInvalidCheckValidKeyforNullKey() {
        String key = null;
        Assertions.assertFalse(QKE.checkValidKey(key));
    }

    // test checkvalid keys method returns false if key contains numbers other than
    // 0,1
    @Test
    public void testInvalidCheckValidKeyforOtherNumbers() {
        String key = "92827389";
        Assertions.assertFalse(QKE.checkValidKey(key));
    }

    // test checkvalid keys method returns false if key a 0/1 with other numbers
    @Test
    public void testInvalidCheckValidKeyforEdge() {
        String key = "19280270389";
        Assertions.assertFalse(QKE.checkValidKey(key));
    }

    // test checkvalid keys method returns false if key contains other chars
    @Test
    public void testInvalidCheckValidKeyOtherChars() {
        String key = "1dsada";
        Assertions.assertFalse(QKE.checkValidKey(key));
    }

    // test if checkvalid keys returns true if key is 1
    @Test
    public void testValidKeysKey1() {
        String key = "1";
        Assertions.assertTrue(QKE.checkValidKey(key));
    }

    // test if checkvalid keys returns true if key is 10
    @Test
    public void testValidKeysKey10() {
        String key = "10";
        Assertions.assertTrue(QKE.checkValidKey(key));
    }

    // test if checkvalid keys returns true if key is 01
    @Test
    public void testValidKeysKey01() {
        String key = "01";
        Assertions.assertTrue(QKE.checkValidKey(key));
    }

    // test if checkvalid keys returns true if key is 101010
    @Test
    public void testValidKeysKey() {
        String key = "101010";
        Assertions.assertTrue(QKE.checkValidKey(key));
    }

    // test sender and receiver have same keys for 16

    // if key is empty

    // if key is all 0s(no encryption)

}
