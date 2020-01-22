package com.example.instagram;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseQuery;
import com.parse.ParseUser;


import java.util.ArrayList;
import java.util.List;

import libs.mjn.prettydialog.PrettyDialog;
import libs.mjn.prettydialog.PrettyDialogCallback;


/**
 * A simple {@link Fragment} subclass.
 */
public class User extends Fragment implements AdapterView.OnItemClickListener , AdapterView.OnItemLongClickListener {


    public User() {
        // Required empty public constructor
    }

    private ListView listView;
    //CatLoadingView cat;
    ArrayList<String> arrayList;
    ArrayAdapter arrayAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_user, container, false);
        listView = view.findViewById(R.id.userlist);
        arrayList = new ArrayList();
        arrayAdapter = new ArrayAdapter(getContext(), android.R.layout.simple_expandable_list_item_1, arrayList);
//        cat = new CatLoadingView();
//        cat.show(getFragmentManager(), "");
        listView.setOnItemClickListener(User.this);
        listView.setOnItemLongClickListener(User.this);
        ParseQuery<ParseUser> parsequery = ParseUser.getQuery();
        parsequery.whereNotEqualTo("username", ParseUser.getCurrentUser().getUsername());
        parsequery.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> objects, ParseException e) {
                if (e == null) {
                    if (objects.size() > 0) {
                        for (ParseUser user : objects) {
                            arrayList.add(user.getUsername());
                        }
                        listView.setAdapter(arrayAdapter);
                    }
                }
            }
        });
        return view;
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


            Intent intent = new Intent(getContext(), UplaoadedImages.class);
            intent.putExtra("username", arrayList.get(position));//send data from one activity or fragment to another
            startActivity(intent);

    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

        ParseQuery<ParseUser> parseQuery  = ParseUser.getQuery();
        parseQuery.whereEqualTo("username",arrayList.get(position));
        parseQuery.getFirstInBackground(new GetCallback<ParseUser>() {
            @Override
            public void done(ParseUser object, ParseException e) {
                if(e==null && object != null)
                {
                final PrettyDialog prettyDialog =new PrettyDialog(getContext());
                prettyDialog.setTitle(object.getUsername()+"'s Info \n"
                         +"Bio: "+object.get("ProfileBio")+" \n"
                        +"Prof: "+object.get("ProfileProfession")+" \n"
                        +"Hobbies: "+object.get("ProfileHobbies")+" \n"
                        +"Sports: "+object.get("ProfileSports ")+" \n")
                        .setIcon(R.drawable.person_image)
                        .addButton(
                                "Ok",
                                R.color.pdlg_color_white,
                                R.color.pdlg_color_blue,
                                new PrettyDialogCallback(){

                                    @Override
                                    public void onClick() {
                                        prettyDialog.dismiss();
                                    }
                                }
                        ).show();
            }
        }});
        return true;
    }
}
