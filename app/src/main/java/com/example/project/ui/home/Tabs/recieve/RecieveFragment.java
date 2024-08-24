package com.example.project.ui.home.Tabs.recieve;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.project.MainActivity;
import com.example.project.R;
import com.example.project.databinding.FragmentRecieveBinding;
import com.example.project.ui.encryption.EncryptionService;
import com.example.project.ui.share.ShareActivity;

import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;


public class RecieveFragment extends Fragment {

    private FragmentRecieveBinding binding;
    private final EncryptionService encryptionService = new EncryptionService();

    public RecieveFragment() {
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentRecieveBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        binding.receiveDecryptBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String plainText = binding.receivePlainText.getText().toString();

                if(plainText.isEmpty()){
                    Toast.makeText(root.getContext(), "Text can not be Empty!", Toast.LENGTH_SHORT).show();
                    return;
                }

                try {

                    MainActivity mainActivity = ((MainActivity)getActivity());
                    KeyPair keyPair = mainActivity.getKeyPair();

                    byte[] message = encryptionService.decryptMessage(Base64.decode(plainText, Base64.DEFAULT) , keyPair.getPrivate());


                    Intent intent = new Intent(root.getContext(), ShareActivity.class);
                    intent.putExtra(Intent.EXTRA_TEXT, new String(message, StandardCharsets.UTF_8));
                    startActivity(intent);


                } catch (NoSuchPaddingException | InvalidKeyException | BadPaddingException |
                         NoSuchAlgorithmException | IllegalBlockSizeException e) {
                    Toast.makeText(root.getContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
                    /*throw new RuntimeException(e);*/
                }

            }
        });


        return root;
    }
}