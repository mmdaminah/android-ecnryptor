package com.example.project.ui.encryption;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.project.MainActivity;
import com.example.project.R;


import com.example.project.databinding.FragmentEncryptionBinding;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.KeyPair;

public class EncryptionFragment extends Fragment {

    private FragmentEncryptionBinding binding;
    private ActivityResultLauncher<Intent> activityResultLauncher;
    private final EncryptionService encryptionService = new EncryptionService();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        EncryptionViewModel encryptionViewModel =
                new ViewModelProvider(this).get(EncryptionViewModel.class);

        binding = FragmentEncryptionBinding.inflate(inflater, container, false);
        View root = binding.getRoot();



        TextView publicKeyText = root.findViewById(R.id.encryption_public_key);
        TextView privateKeyText = root.findViewById(R.id.encryption_private_key_input);

        binding.encryptionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                KeyPair keyPair = encryptionService.generateKeys();

                ((MainActivity)getActivity()).setKeyPair(keyPair);

                encryptionViewModel.setPrivateKey(encryptionService.getPrivateKey());
                encryptionViewModel.setPublicKey(encryptionService.getPublicKey());

                publicKeyText.setText(encryptionService.getPublicKey());
                privateKeyText.setText(encryptionService.getPrivateKey());


                binding.encExportBtn.setVisibility(View.VISIBLE);
                binding.encryptionButton.setText(R.string.regenerate);


            }
        });

        // Register the activity result launcher
        activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            Intent data = result.getData();


                            File file = new File(data.toUri(Intent.URI_INTENT_SCHEME), "public_key.txt");
                            FileOutputStream outputStream = null;
                            try {
                                outputStream = new FileOutputStream(file);
                                outputStream.write(publicKeyText.getText().toString().getBytes());
                                outputStream.close();

                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    }
                }
        );

        binding.encExportBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_CREATE_DOCUMENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("application/txt");
                intent.putExtra(Intent.EXTRA_TITLE, "public_key.txt");

                activityResultLauncher.launch(intent);

            }

        });



        binding.encryptionCopyPublicKey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClipboardManager clipboard = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("public key", publicKeyText.getText());
                clipboard.setPrimaryClip(clip);
            }
        });

        binding.encryptionCopyPrivateKey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClipboardManager clipboard = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("private key", privateKeyText.getText());
                clipboard.setPrimaryClip(clip);
            }
        });

        return root;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}