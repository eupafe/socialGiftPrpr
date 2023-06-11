package com.example.socialgiftprpr.Authentication;

import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.example.socialgiftprpr.R;
import com.example.socialgiftprpr.MainWindow;

import java.io.IOException;

public class SignUp extends Fragment {

    private ImageView imageView;
    private Uri imagePath;

    private Button signUp;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_sign_up, container, false);
        imageView = view.findViewById(R.id.profileImage);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(Intent.createChooser(intent, "Select picture"), 1);
            }
        });

        signUp = view.findViewById(R.id.signup_button);

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MainWindow.class);
                startActivity(intent);
            }
        });

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK && data!=null){
            imagePath = data.getData();
            getImageInImageView();
        }
    }

    private void getImageInImageView(){

        Bitmap bitmap = null;
        try{
            bitmap = MediaStore.Images.Media.getBitmap(requireContext().getContentResolver(),imagePath);

        }catch (IOException e){
            e.printStackTrace();
        }
        imageView.setImageBitmap(bitmap);

    }

}