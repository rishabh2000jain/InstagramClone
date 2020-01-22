package com.example.instagram;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.shashank.sony.fancytoastlib.FancyToast;

import static com.parse.Parse.getApplicationContext;


/**
 * A simple {@link Fragment} subclass.
 */
public class Profile extends Fragment {

        Button button;
        EditText Pname,Pbio,Pprofession,hobbies,sports;

    public Profile() {
            // Required empty public constructor
        }


        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            // Inflate the layout for this fragment
            View view = inflater.inflate(R.layout.fragment_profile, container, false);
            button = view.findViewById(R.id.updatebutton);
            Pname = view.findViewById(R.id.profilename);
            Pbio = view.findViewById(R.id.bio);
            Pprofession = view.findViewById(R.id.profession);
            hobbies = view.findViewById(R.id.hobbies);
            sports = view.findViewById(R.id.sport);
            final ParseUser parseUser = ParseUser.getCurrentUser();


            try
            {
                if(parseUser.get("ProfileName")==null) {
                Pname.setText(parseUser.getUsername()+"");
                }
                else {Pname.setText(parseUser.get("ProfileName").toString());}

        Pbio.setText(parseUser.get("ProfileBio").toString());
        Pprofession.setText(parseUser.get("ProfileProfession").toString());
        hobbies.setText(parseUser.get("ProfileHobbies").toString());
        sports.setText(parseUser.get("ProfileSports").toString());}
            catch (NullPointerException e)
            {

            }
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                parseUser.put("ProfileName",Pname.getText().toString());
                parseUser.put("ProfileBio",Pbio.getText().toString());
                parseUser.put("ProfileProfession",Pprofession.getText().toString());
                parseUser.put("ProfileHobbies",hobbies.getText().toString());
                parseUser.put("ProfileSports",sports.getText().toString());
                parseUser.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if(e==null)
                        {
                            FancyToast.makeText(getApplicationContext(),
                                    "done",
                                    FancyToast.LENGTH_SHORT,
                                    FancyToast.SUCCESS,
                                    false).show();

                        }
                    }
                });

            }
        });


        return view;
    }

}
