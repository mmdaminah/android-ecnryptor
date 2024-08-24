package com.example.project.ui.encryption;

import android.util.Base64;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;

public class EncryptionViewModel extends ViewModel {


    private final MutableLiveData<String> publicKey;
    private final MutableLiveData<String> privateKey;

    public EncryptionViewModel() {

        publicKey = new MutableLiveData<>();
        privateKey = new MutableLiveData<>();

    }


    public String getPublicKey(){
        return this.publicKey.getValue();
    }
    public String getPrivateKey(){
        return this.privateKey.getValue();
    }
    public void setPublicKey(String publicKey){
        this.publicKey.setValue(publicKey);
    }
    public void setPrivateKey(String privateKey){
        this.privateKey.setValue(privateKey);
    }

}