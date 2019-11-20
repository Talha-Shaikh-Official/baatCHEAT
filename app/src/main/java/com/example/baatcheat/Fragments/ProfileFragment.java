package com.example.baatcheat.Fragments;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.BundleCompat;
import androidx.core.content.ContextCompat;
import androidx.emoji.bundled.BundledEmojiCompatConfig;
import androidx.emoji.text.EmojiCompat;
import androidx.emoji.text.FontRequestEmojiCompatConfig;
import androidx.emoji.widget.EmojiTextView;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.webkit.MimeTypeMap;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.agrawalsuneet.dotsloader.loaders.TashieLoader;
import com.example.baatcheat.BioActivity;
import com.example.baatcheat.LoginActivity;
import com.example.baatcheat.Model.User;
import com.example.baatcheat.R;
import com.example.baatcheat.ShowNumberActivity;
import com.example.baatcheat.StartActivity;
import com.example.baatcheat.UsernameActivity;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.media.MediaRecorder.VideoSource.CAMERA;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {

    private TextView tv_username, myPhoneNumner;
    private CircleImageView image_profile;
    private EmojiTextView myBio;
    private LinearLayout Username;
    private LinearLayout phone;
    private LinearLayout logout;
    private LinearLayout bio;

    private TextView fixedText, Editusername;

    private FirebaseAuth mAuth;

    TashieLoader tashieLoader;

    FirebaseUser firebaseUser;

    StorageReference storageReference;

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        EmojiCompat.Config config = new BundledEmojiCompatConfig(getActivity());
        EmojiCompat.init(config);

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        tv_username = view.findViewById(R.id.tv_username);
        myPhoneNumner = view.findViewById(R.id.myphonenumner);
        image_profile = view.findViewById(R.id.image_profile);
        myBio = (EmojiTextView) view.findViewById(R.id.myBio);
        Username = view.findViewById(R.id.username);
        phone = view.findViewById(R.id.phone);
        bio = view.findViewById(R.id.bio);
        logout = view.findViewById(R.id.logout);

        fixedText = view.findViewById(R.id.fixedText);
        fixedText.setText("@");
        Editusername = view.findViewById(R.id.Editusername);

        mAuth = FirebaseAuth.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        tashieLoader = view.findViewById(R.id.lazyLoader);

        DottedLoader();
        updateProfileInfo();

        image_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPictureDialog();
            }
        });

        Username.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), UsernameActivity.class);
                startActivity(intent);
            }
        });

        phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ShowNumberActivity.class);
                startActivity(intent);
            }
        });

        bio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), BioActivity.class);
                startActivity(intent);
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();

                Intent intent = new Intent(getActivity(), LoginActivity.class);
//                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

        return view;
    }

    private void updateProfileInfo() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                User users = dataSnapshot.getValue(User.class);
                Editusername.setText(users.getUsername());
                tv_username.setText(users.getUsername());
                myPhoneNumner.setText(users.getPhone());
                myBio.setText(users.getBio());
                tashieLoader.setVisibility(View.GONE);
//                Glide.with(getApplicationContext()).load(users.getImageurl()).into(imageView);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void showPictureDialog() {

    }

    void DottedLoader() {
        TashieLoader tashie = new TashieLoader(
                getContext(), 5,
                30, 10,
                ContextCompat.getColor(getContext(), R.color.colorPrimaryDark));

        tashie.setAnimDuration(500);
        tashie.setAnimDelay(100);
        tashie.setInterpolator(new LinearInterpolator());

        tashieLoader.addView(tashie);
        tashieLoader.setVisibility(View.VISIBLE);

    }
}
