package com.example.project.ui.home.Tabs.send;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import java.security.KeyFactory;

import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.project.MainActivity;
import com.example.project.R;
import com.example.project.databinding.FragmentSendBinding;
import com.example.project.ui.encryption.EncryptionService;
import com.example.project.ui.share.ShareActivity;

import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.spec.EncodedKeySpec;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;


public class SendFragment extends Fragment {

    private FragmentSendBinding binding;
    private final EncryptionService encryptionService = new EncryptionService();


    public SendFragment() {
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        SendViewModel sendViewModel = new ViewModelProvider(this).get(SendViewModel.class);

        binding = FragmentSendBinding.inflate(inflater, container, false);
        View root = binding.getRoot();


        binding.sendEncryptBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String publicKeyString = binding.sendPublicKey.getText().toString();
                String plainText = binding.sendPlainText.getText().toString();

                if(publicKeyString.isEmpty()) {
                    Toast.makeText(root.getContext(), "Public key couldn't be empty", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(plainText.isEmpty()){
                    Toast.makeText(root.getContext(), "Plain text couldn't be empty", Toast.LENGTH_SHORT).show();
                    return;
                }


                try {

                    MainActivity mainActivity = ((MainActivity)getActivity());
                    KeyPair keyPair = mainActivity.getKeyPair();

                    PublicKey publicKey = encryptionService.stringToPublicKey(publicKeyString);

                    byte[] encryptedMessage = encryptionService.encryptMessage(plainText.getBytes(), publicKey);

                    ClipboardManager clipboard = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
                    /*ClipData clip = ClipData.newPlainText("encrypted message", new String(encryptedMessage, StandardCharsets.UTF_8));*/
                    ClipData clip = ClipData.newPlainText("encrypted message", Base64.encodeToString(encryptedMessage, Base64.DEFAULT));
                    clipboard.setPrimaryClip(clip);

                    Toast.makeText(getContext(), "Encrypted message copied to clipboard!", Toast.LENGTH_SHORT).show();


                } catch (NoSuchPaddingException | BadPaddingException | IllegalBlockSizeException |
                         InvalidKeyException | NoSuchAlgorithmException e ) {

                    Toast.makeText(root.getContext(), "there were some errors", Toast.LENGTH_SHORT).show();
                    Log.d("Runtime Error:", e.toString());

                } catch (InvalidKeySpecException e) {
                    Toast.makeText(root.getContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
                    /*throw new RuntimeException(e);*/
                }

            }
        });


        return root;
    }
}

