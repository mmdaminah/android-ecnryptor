package com.example.project.ui.encryption;

import android.util.Base64;

import androidx.lifecycle.MutableLiveData;

import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.EncodedKeySpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class EncryptionService {

    private String publicKey;
    private String privateKey;

    public EncryptionService() {

    }


    public KeyPair generateKeys() {
        KeyPairGenerator generator = null;
        try {
            generator = KeyPairGenerator.getInstance("RSA");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        generator.initialize(2048);

        KeyPair pair = generator.generateKeyPair();

        PrivateKey privateKey = pair.getPrivate();
        PublicKey publicKey = pair.getPublic();

        String privateKeyString = Base64.encodeToString(privateKey.getEncoded(), 0);
        String publicKeyString = Base64.encodeToString(publicKey.getEncoded(), 0);

        setPrivateKey(privateKeyString);
        setPublicKey(publicKeyString);

        return pair;
    }

    public byte[] encryptMessage(byte[] messageBytes, PublicKey publicKey) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        Cipher encryptCipher = Cipher.getInstance("RSA");
        encryptCipher.init(Cipher.ENCRYPT_MODE, publicKey);
        return encryptCipher.doFinal(messageBytes);
    }

    public byte[] decryptMessage(byte[] encryptedMessage, PrivateKey privateKey) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        Cipher decryptCipher = Cipher.getInstance("RSA");
        decryptCipher.init(Cipher.DECRYPT_MODE, privateKey);
        return decryptCipher.doFinal(encryptedMessage);
    }

    public PublicKey stringToPublicKey(String publicKeyString) throws NoSuchAlgorithmException, InvalidKeySpecException {
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        byte[] publicKeyBytes =  Base64.decode(publicKeyString, Base64.DEFAULT);
        EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(publicKeyBytes);
        return keyFactory.generatePublic(publicKeySpec);
    }
    public void setPublicKey(String publicKey){
        this.publicKey = publicKey;
    }
    public void setPrivateKey(String privateKey){
        this.privateKey = privateKey;
    }
    public String getPublicKey(){
        return this.publicKey;
    }
    public String getPrivateKey() {
        return this.privateKey;
    }



}
