package com.example.android.nfccardemu;

import android.nfc.cardemulation.HostApduService;
import android.os.Bundle;

import java.io.FileInputStream;
import java.util.Arrays;

/**
 * Created by Mark.Lin on 2/17/2016.
 */
public class CardService extends HostApduService {
    //AID for host card emulation service
    private static final String KEYCARD_AID = "F222222222";
    //APDU header
    private static final String APDU_HEADER = "00A40400";
    //build app specific APDU key
    private static final byte[] APP_APDU = BuildSelectApdu(KEYCARD_AID);
    //byte array to send back as response APDU if commandAPDU not recognized
    private static final byte[] UNKNOWN_APDU = HexStringToByteArray("0000");
    //byte array to send back as response APDU if commandAPDU recognized
    private static final byte[] ACCEPT_APDU = HexStringToByteArray("9000");

    /** GOTO function for when NFC link is either interrupted
       by call to another HCE service or link is physically
       broken.
     */
    public void onDeactivated(int reason){
        if (reason==0)
            System.out.println("Unrecognized APDU. Ensure AID is correct");
        else
            System.out.println("Connection terminated prematurely");
    }

    public byte[] processCommandApdu(byte[] commandApdu, Bundle extras){
        // If the APDU_COMMAND matches app APDU, return valid byte array

        if (Arrays.equals(APP_APDU, commandApdu)){
            String userInfo = readUserInfo();
            System.out.println(userInfo);

            byte[] userInfoBytes = userInfo.getBytes();
            return ConcatArrays(userInfoBytes, ACCEPT_APDU);
        }else{
            onDeactivated(0);
            return UNKNOWN_APDU;
        }
    }

    public static byte[] BuildSelectApdu(String aid) {
        // Format: [CLASS | INSTRUCTION | PARAMETER 1 | PARAMETER 2 | LENGTH | DATA]
        return HexStringToByteArray(APDU_HEADER + String.format("%02X",
                aid.length() / 2) + aid);
    }

    /**
     * Utility method to convert a hexadecimal string to a byte string.
     *
     * <p>Behavior with input strings containing non-hexadecimal characters is undefined.
     *
     * @param s String containing hexadecimal characters to convert
     * @return Byte array generated from input
     * @throws java.lang.IllegalArgumentException if input length is incorrect
     */
    public static byte[] HexStringToByteArray(String s) throws IllegalArgumentException {
        int len = s.length();
        if (len % 2 == 1) {
            throw new IllegalArgumentException("Hex string must have even number of characters");
        }
        byte[] data = new byte[len / 2]; // Allocate 1 byte per 2 hex characters
        for (int i = 0; i < len; i += 2) {
            // Convert each character into a integer (base-16), then bit-shift into place
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i+1), 16));
        }
        return data;
    }

    /**
     * Utility method to concatenate two byte arrays.
     * @param first First array
     * @param rest Any remaining arrays
     * @return Concatenated copy of input arrays
     */
    public static byte[] ConcatArrays(byte[] first, byte[]... rest) {
        int totalLength = first.length;
        for (byte[] array : rest) {
            totalLength += array.length;
        }
        byte[] result = Arrays.copyOf(first, totalLength);
        int offset = first.length;
        for (byte[] array : rest) {
            System.arraycopy(array, 0, result, offset, array.length);
            offset += array.length;
        }
        return result;
    }

    public String readUserInfo(){
        String filename = "userCreds";
        int i;
        String userInfoString = "";
        FileInputStream inputStream;

        try{
            inputStream = openFileInput(filename);

            while ((i = inputStream.read())!=-1) {
                userInfoString = userInfoString + (char)i;
            }

            inputStream.close();
            return userInfoString;

        }catch (Exception e){
            e.printStackTrace();
        }

        return null;
    }
}
