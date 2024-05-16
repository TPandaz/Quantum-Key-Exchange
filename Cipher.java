public class Cipher {
    //private constructor as an instance of this class is not required
    private Cipher(){}

    public static String xor(String message, String key){
        StringBuilder output = new StringBuilder();
        //loop through every message, XORing the bit with key
        for (int i =0; i < message.length(); i++){
            char keyChar = key.charAt(i % key.length());
            //XOR both chars from message and key, appending to output
            output.append((keyChar ^ message.charAt(i)));
        }
        return output.toString();
    }
    
}
