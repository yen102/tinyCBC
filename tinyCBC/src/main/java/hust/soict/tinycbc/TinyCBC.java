package hust.soict.tinycbc;

import java.nio.charset.StandardCharsets;

public class TinyCBC {
    private final static char[] hexArray = "0123456789ABCDEF".toCharArray();
    static {
        System.loadLibrary("tinyCBC");
    }

    private native byte[] encryptByte(byte[] IV, byte[] key, byte[] bytes); // remember to pad before encrypt
    private native byte[] decryptByte(byte[] IV, byte[] key, byte[] bytes); // remember to unpad before decrypt

    public byte[] encrypt(byte[] iv, byte[] key, byte[] bytes) {
        byte[] pad = pad(bytes);
        return encryptByte(iv, key, pad);
    }

    public String encrypt(byte[] iv, byte[] key, String string) {
        byte[] bytes = string.getBytes(StandardCharsets.UTF_8);
        byte[] pad = pad(bytes);
        byte[] result = encryptByte(iv, key, pad);
        return bytesToHex(result);
    }

    public String decrypt(byte[] iv, byte[] key, String hex) {
        byte[] bytes = hexToBytes(hex);
        byte[] decrypted = decryptByte(iv, key, bytes);
        byte[] unpad = unpad(decrypted);
        return new String(unpad, StandardCharsets.UTF_8);
    }

    public byte[] decrypt(byte[] iv, byte[] key, byte[] bytes) {
        byte[] decrypted = decryptByte(iv, key, bytes);
        return unpad(decrypted);
    }


    private byte[] pad(byte[] bytes) {
        int size =  bytes.length;
        int padSize =  (16 - size % 16);
        byte[] result = new byte[size + padSize];
        for (int i = 0; i < size; i++) {
            result[i] = bytes[i]; // copy the leading elements in original array
        }
        for (int j = 0; j < padSize; j++) {
            result[j + size] = (byte) padSize; // add padding elements
        }
        return result;
    }

    private byte[] unpad(byte[] bytes) {
        int size = bytes.length;
        int padSize = (int) bytes[size - 1];
        byte[] result = new byte[size - padSize];
        for (int i = 0; i < result.length; i++) {
            result[i] = bytes[i];
        }
        return result;
    }

    private String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for ( int j = 0; j < bytes.length; j++ ) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }

    private byte[] hexToBytes(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i/2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i+1), 16));
        }
        return data;
    }

}
