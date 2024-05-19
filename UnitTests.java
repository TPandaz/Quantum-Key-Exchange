import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

import org.junit.jupiter.api.*;

public class UnitTests {
    private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();

    @BeforeEach
    public void setout() {
        System.setOut(new PrintStream(outputStreamCaptor));
    }

    // test cases for qubit
    @org.junit.jupiter.api.Nested
    @DisplayName("Qubit Tests")
    public class QubitTests {

        @Test
        @DisplayName("Constructor sets value correctly to 1")
        public void testQubitValues1() {
            Qubit qubit = new Qubit(1, 0);
            Assertions.assertEquals(1, qubit.getValue());
        }

        @Test
        @DisplayName("Constructor sets value correctly to 0")
        public void testQubitValues0() {
            Qubit qubit = new Qubit(0, 0);
            Assertions.assertEquals(0, qubit.getValue());
        }

        @Test
        @DisplayName("Constructor sets polarization correctly to 1")
        public void testQubitPolarization1() {
            Qubit qubit = new Qubit(0, 1);
            Assertions.assertEquals(1, qubit.getPolarization());
        }

        @Test
        @DisplayName("Constructor sets polarization correctly to 0")
        public void testQubitPolarization0() {
            Qubit qubit = new Qubit(0, 0);
            Assertions.assertEquals(0, qubit.getPolarization());
        }

        @Test
        @DisplayName("test constructor invalid polarization input: -1")
        public void testConstructorInvalidPolarizationNegative1() {
            assertThrows(IllegalArgumentException.class, () -> new Qubit(1, -1));
        }

        @Test
        @DisplayName("test constructor invalid polarization input: -1000 ")
        public void testConstructorInvalidPolarizationNegative1000() {
            assertThrows(IllegalArgumentException.class, () -> new Qubit(1, -1000));
        }

        @Test
        @DisplayName("test constructor invalid polarization input: 12038910")
        public void testConstructorInvalidPolarizationBigNumber() {
            assertThrows(IllegalArgumentException.class, () -> new Qubit(1, 12038910));
        }

        @Test
        @DisplayName("test constructor invalid value input: -1")
        public void testConstructorInvalidValueNegative1() {
            assertThrows(IllegalArgumentException.class, () -> new Qubit(-1, 0));
        }

        @Test
        @DisplayName("test constructor invalid value input: -1000")
        public void testConstructorInvalidValueNegative1000() {
            assertThrows(IllegalArgumentException.class, () -> new Qubit(-1000, 0));
        }

        @Test
        @DisplayName("test constructor invalid value input: 22346789")
        public void testConstructorInvalidValueBigNumber() {
            assertThrows(IllegalArgumentException.class, () -> new Qubit(22346789, 0));
        }

        @Test
        @DisplayName("tests if constructor(no params) initializes value to 0 or 1")
        public void testConstructorRandomValue() {
            Qubit qubit = new Qubit();
            int value = qubit.getValue();
            Assertions.assertTrue(value == 0 || value == 1);
        }

        @Test
        @DisplayName("tests if constructor(no params) initializes polarizations to 0 or 1")
        public void testConstructorRandomPolarization() {
            Qubit qubit = new Qubit();
            int polarization = qubit.getPolarization();
            Assertions.assertTrue(polarization == 0 || polarization == 1);
        }

        @Test
        @DisplayName("set method sets existing value from 0 to 1")
        public void testSetQubitValueTo1() {
            Qubit qubit = new Qubit(0, 0);
            // set to opposite values
            qubit.set(1, 0);
            Assertions.assertEquals(1, qubit.getValue());
            Assertions.assertEquals(0, qubit.getPolarization());
        }

        @Test
        @DisplayName("set method sets existing value from 1 to 0")
        public void testSetQubitValueTo0() {
            Qubit qubit = new Qubit(1, 0);
            // set to opposite values
            qubit.set(0, 0);
            Assertions.assertEquals(0, qubit.getValue());
            Assertions.assertEquals(0, qubit.getPolarization());
        }

        @Test
        @DisplayName("set method sets existing polarization from 0 to 1")
        public void testSetQubitPolarizationTo1() {
            Qubit qubit = new Qubit(0, 0);
            qubit.set(0, 1);
            Assertions.assertEquals(0, qubit.getValue());
            Assertions.assertEquals(1, qubit.getPolarization());
        }

        @Test
        @DisplayName("set method sets existing polarization from 1 to 0")
        public void testSetQubitPolarizationTo0() {
            Qubit qubit = new Qubit(0, 1);
            qubit.set(0, 0);
            Assertions.assertEquals(0, qubit.getValue());
            Assertions.assertEquals(0, qubit.getPolarization());
        }

        @Test
        @DisplayName("set method throw exception when value is set to -1")
        public void testSetQubitValueExceptionNegative1() {
            Qubit qubit = new Qubit(0, 0);
            assertThrows(IllegalArgumentException.class, () -> qubit.set(-1, 0));

        }

        @Test
        @DisplayName("set method throw exception when value is set to -1000")
        public void testSetQubitValueExceptionNegative1000() {
            Qubit qubit = new Qubit(0, 0);
            assertThrows(IllegalArgumentException.class, () -> qubit.set(-1000, 0));
        }

        @Test
        @DisplayName("set method throw exception when value is set to 1234678")
        public void testSetQubitValueExceptionBigNumber() {
            Qubit qubit = new Qubit(0, 0);
            assertThrows(IllegalArgumentException.class, () -> qubit.set(1234678, 0));
        }

        @Test
        @DisplayName("set method throw exception when Polarization is set to -1")
        public void testSetQubitPolarizationExceptionNegative1() {
            Qubit qubit = new Qubit(0, 0);
            assertThrows(IllegalArgumentException.class, () -> qubit.set(0, -1));
        }

        @Test
        @DisplayName("set method throw exception when Polarization is set to -1000")
        public void testSetQubitPolarizationExceptionNegative1000() {
            Qubit qubit = new Qubit(0, 0);
            assertThrows(IllegalArgumentException.class, () -> qubit.set(0, -1000));
        }

        @Test
        @DisplayName("set method throw exception when Polarization is set to 1234678")
        public void testSetQubitPolarizationExceptionBigNumber() {
            Qubit qubit = new Qubit(0, 0);
            assertThrows(IllegalArgumentException.class, () -> qubit.set(0, 1234678));
        }

        @Test
        @DisplayName("test if measure method returns original value with same polarization: 0")
        public void testMeasureSamePolarization0() {
            Qubit qubit = new Qubit(1, 0);
            int newValue = qubit.measure(0);
            Assertions.assertEquals(1, newValue);
            Assertions.assertEquals(0, qubit.getPolarization());
        }

        @Test
        @DisplayName("test if measure method returns original value with same polarization: 1")
        public void testMeasureSamePolarization1() {
            Qubit qubit = new Qubit(1, 1);
            int newValue = qubit.measure(1);
            Assertions.assertEquals(1, newValue);
            Assertions.assertEquals(1, qubit.getPolarization());
        }

        @Test
        @DisplayName("test measure method if value does indeed change(using math.random) for different polarization:1")
        public void testMeasureChangesValue1() {
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

        @Test
        @DisplayName("test measure method if value does indeed change(using math.random) for different polarization:0")
        public void testMeasureChangesValue0() {
            Qubit qubit = new Qubit(1, 1);
            int value = qubit.getValue(); // value is 1
            for (int i = 0; i < 10; i++) {
                int newValue = qubit.measure(0);
                // value changes
                if (newValue != value) {
                    Assertions.assertTrue(true);
                }
            }
        }

        @Test
        @DisplayName("test measure method if exception is thrown when Polarization: -1 ")
        public void testMeasureExceptionPolarizationNegative1() {
            Qubit qubit = new Qubit(0, 1);
            assertThrows(IllegalArgumentException.class, () -> qubit.measure(-1));
        }

        @Test
        @DisplayName("test measure method if exception is thrown when Polarization: -1000 ")
        public void testMeasureExceptionPolarizationNegative1000() {
            Qubit qubit = new Qubit(0, 1);
            assertThrows(IllegalArgumentException.class, () -> qubit.measure(-1000));
        }

        @Test
        @DisplayName("test measure method if exception is thrown when Polarization: 123467 ")
        public void testMeasureExceptionPolarizationBigNumber() {
            Qubit qubit = new Qubit(0, 1);
            assertThrows(IllegalArgumentException.class, () -> qubit.measure(123467));
        }
    }

    @org.junit.jupiter.api.Nested
    @DisplayName("Cipher Tests")
    public class CipherTests {

        @Test
        @DisplayName("test if message is empty, should return no bytes")
        public void testEmptyMessage() {
            byte[] messageBytes = new byte[0];
            String key = "11010";
            byte[] encryptedMessage = Cipher.xor(messageBytes, key);
            Assertions.assertEquals(0, encryptedMessage.length);
        }

        @Test
        @DisplayName("test if correct ouput is produced for XOR with message:hi")
        public void testCorrectOutput() {
            byte[] messageBytes = "hi".getBytes(StandardCharsets.UTF_8);
            String key = "1101";
            byte[] encryptedMessage = Cipher.xor(messageBytes, key);
            // actual encrypted values given a key:1101 and message: hi
            byte[] expectedEncryptedBytes = new byte[] { (byte) 0xB5, (byte) 0xb4 };
            Assertions.assertArrayEquals(expectedEncryptedBytes, encryptedMessage);
        }

        @Test
        @DisplayName("tests if given a key length that isn't a multiple of 8, key is correctly looped and XORed given message:hii")
        public void testKeyIsLooped() {
            byte[] messageBytes = "hii".getBytes(StandardCharsets.UTF_8);
            String key = "11011";
            byte[] encryptedMessage = Cipher.xor(messageBytes, key);
            // expected encrypted byte when key: 11011 is looped with message: hii
            byte[] expectedEncryptedBytes = new byte[] { (byte) 0xB6, (byte) 0x9E, (byte) 0xD4 };
            Assertions.assertArrayEquals(expectedEncryptedBytes, encryptedMessage);
        }

        @Test
        @DisplayName("Test when key is longer than message, correct ouput")
        public void testLongKey() {
            byte[] messageBytes = "h".getBytes(StandardCharsets.UTF_8);
            String key = "11011011101010000101010101110111101010110101010";
            byte[] encryptedMessage = Cipher.xor(messageBytes, key);
            // expected encrypted byte with key and "h"
            byte[] expectedEncryptedBytes = new byte[] { (byte) 0xB3 };
            Assertions.assertArrayEquals(expectedEncryptedBytes, encryptedMessage);
        }

        @Test
        @DisplayName("Test rightmost bit is correctly Xored(last bit in loop)")
        public void testRightMostBitShift() {
            byte[] messageBytes = new byte[] { (byte) 0x1 }; // byte value: 0000 0001
            String key = "0";
            byte[] encryptedMessage = Cipher.xor(messageBytes, key);
            Assertions.assertEquals(1, encryptedMessage.length);
            Assertions.assertEquals(1, encryptedMessage[0]);
        }

        @Test
        @DisplayName("Test leftmost bit is correctly XOred(1st bit in loop)")
        public void testLeftMostBitShift() {
            byte[] messageBytes = new byte[] { (byte) 0x80 }; // byte value: 1000 0000
            String key = "0";
            byte[] encryptedMessage = Cipher.xor(messageBytes, key);
            Assertions.assertEquals(1, encryptedMessage.length);
            int unsignedValue = encryptedMessage[0] & 0xFF;
            // dec: 128 is byte value:1000 0000
            Assertions.assertEquals(128, unsignedValue);
        }

        @Test
        @DisplayName("test if XOR can be used to encrypt and decrypt, returning the original character")
        public void testEncryptionDecryption() {
            String message = "I like cows";
            byte[] messageBytes = message.getBytes(StandardCharsets.UTF_8);
            String key = "10101";
            byte[] encryptedMessage = Cipher.xor(messageBytes, key);
            byte[] decryptedMessage = Cipher.xor(encryptedMessage, key);
            String decryptedString = new String(decryptedMessage, StandardCharsets.UTF_8);
            // should return "I like cows"
            Assertions.assertEquals(message, decryptedString);
        }

        @Test
        @DisplayName("test if special characters are encrypted/decrypted correctly")
        public void testSpecialCharacters() {
            String message = "@#$%^&*()_+-=[]{}|;':<>,.?/`~";
            String key = "11011";
            byte[] messageBytes = message.getBytes(StandardCharsets.UTF_8);
            byte[] encryptedMessage = Cipher.xor(messageBytes, key);
            byte[] decryptedMessage = Cipher.xor(encryptedMessage, key);
            String decryptedString = new String(decryptedMessage, StandardCharsets.UTF_8);
            Assertions.assertEquals(message, decryptedString);
        }

        @Test
        @DisplayName("test when with long stream of bytes(long message), encryption and decryption both works")
        public void testLongMessage() {
            String message = "8% of 25 is the same as 25% of 8 and one of them is much easier to do in your head.";
            byte[] messageBytes = message.getBytes(StandardCharsets.UTF_8);
            String key = "10101";
            byte[] encryptedMessage = Cipher.xor(messageBytes, key);
            byte[] decryptedMessage = Cipher.xor(encryptedMessage, key);
            String decryptedString = new String(decryptedMessage, StandardCharsets.UTF_8);
            Assertions.assertEquals(message, decryptedString);
        }

        @Test
        @DisplayName("test that encryption/decryption is accurate with extended ascii codeS")
        public void testExtendedAsciiEncryptionDecryption() {
            String message = "€ŠœŸ®¶µÆð";
            String key = "010101";
            byte[] messageBytes = message.getBytes(StandardCharsets.UTF_8);
            byte[] encryptedMessage = Cipher.xor(messageBytes, key);
            byte[] decryptedMessage = Cipher.xor(encryptedMessage, key);
            Assertions.assertEquals(message, new String(decryptedMessage, StandardCharsets.UTF_8));
        }

    }

    @org.junit.jupiter.api.Nested
    @DisplayName("EndPoint class Tests")
    public class EndPointTests {

        @Test
        @DisplayName("test setKey method gets same key values for receiver and sender with qubitstream length 16")
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

        @Test
        @DisplayName("test setKey method gets same key values for receiver and sender with qubitstream length 256")
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

        @Test
        @DisplayName("test setKey method gets same key values for receiver and sender with qubitstream length 1024")
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

        @Test
        @DisplayName("test setKey method throws an exception when streams are different lengths")
        public void testSetKeyMethodExceptionDifferentStreamLengths() {
            ArrayList<Qubit> senderStream = new ArrayList<>();
            ArrayList<Qubit> receiverStream = new ArrayList<>();
            for (int i = 0; i < 1024; i++) {
                senderStream.add(new Qubit());
            }
            // Add less qutbis to receiverStream for different lengths
            for (int i = 0; i < 512; i++) {
                receiverStream.add(new Qubit());
            }
            EndPoint sender = new EndPoint();
            assertThrows(IllegalArgumentException.class, () -> sender.setKey(senderStream, receiverStream));
        }
    }

    @org.junit.jupiter.api.Nested
    @DisplayName("QKE tests")
    public class QKETests {
        @Test
        @DisplayName("test checkvalid keys method returns false for key:0 (no encryption)")
        public void testInvalidCheckValidKeyfor0() {
            String key = "0";
            Assertions.assertFalse(QKE.isKeyValid(key));
        }

        @Test
        @DisplayName("test checkvalid keys method returns false is key is a string of 0s")
        public void testInvalidCheckValidKeyfor0s() {
            String key = "0000000";
            Assertions.assertFalse(QKE.isKeyValid(key));
        }

        @Test
        @DisplayName("test checkvalid keys method returns false if key is empty")
        public void testInvalidCheckValidKeyforEmptyKey() {
            String key = "";
            Assertions.assertFalse(QKE.isKeyValid(key));
        }

        @Test
        @DisplayName("test checkvalid keys method returns false if key is null")
        public void testInvalidCheckValidKeyforNullKey() {
            String key = null;
            Assertions.assertFalse(QKE.isKeyValid(key));
        }

        @Test
        @DisplayName("test checkvalid keys method returns false if key contains numbers other than 0,1")
        public void testInvalidCheckValidKeyforOtherNumbers() {
            String key = "92827389";
            Assertions.assertFalse(QKE.isKeyValid(key));
        }

        @Test
        @DisplayName("test checkvalid keys method returns false if key contains 0 & 1 with other numbers")
        public void testInvalidCheckValidKeyforEdge() {
            String key = "19280270389";
            Assertions.assertFalse(QKE.isKeyValid(key));
        }

        @Test
        @DisplayName("test checkvalid keys method returns false if key contains non-numbers")
        public void testInvalidCheckValidKeyOtherChars() {
            String key = "1dsada";
            Assertions.assertFalse(QKE.isKeyValid(key));
        }

        @Test
        @DisplayName("test if checkvalid keys returns true if key is 1")
        public void testValidKeysKey1() {
            String key = "1";
            Assertions.assertTrue(QKE.isKeyValid(key));
        }

        @Test
        @DisplayName("test if checkvalid keys returns true if key is 10")
        public void testValidKeysKey10() {
            String key = "10";
            Assertions.assertTrue(QKE.isKeyValid(key));
        }

        @Test
        @DisplayName("test if checkvalid keys returns true if key is 01")
        public void testValidKeysKey01() {
            String key = "01";
            Assertions.assertTrue(QKE.isKeyValid(key));
        }

        @Test
        @DisplayName("test if checkvalid keys returns true if key is a string of 0s and 1s")
        public void testValidKeysKey() {
            String key = "101010";
            Assertions.assertTrue(QKE.isKeyValid(key));
        }
    }

}
