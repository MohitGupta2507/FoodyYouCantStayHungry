package com.foodies.mohitgupta.foodyyoucantstsyhungry;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Kitchen_food.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Kitchen_food#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Kitchen_food extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public Kitchen_food() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Kitchen_food.
     */
    // TODO: Rename and change types and number of parameters
    public static Kitchen_food newInstance( String param1, String param2 ) {
        Kitchen_food fragment = new Kitchen_food();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate( Bundle savedInstanceState ) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView( LayoutInflater inflater, ViewGroup container,
                              Bundle savedInstanceState ) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_kitchen_food, container, false);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed( Uri uri ) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach( Context context ) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction( Uri uri );
    }

    public static class StreetViewHolder extends RecyclerView.ViewHolder
    {
        TextView Username;
        TextView datee;
        TextView Title;
        TextView ShortDesc;
        ImageView Img1;
        Context ctx;
        de.hdodenhof.circleimageview.CircleImageView Img2;

        public StreetViewHolder( View itemView ) {
            super(itemView);
            ctx=itemView.getContext();
            Username=(TextView)itemView.findViewById(R.id.UsernameText);
            datee=(TextView)itemView.findViewById(R.id.date);
            Title=(TextView)itemView.findViewById(R.id.Title);
            ShortDesc=(TextView)itemView.findViewById(R.id.ShortDescription);
            Img1=(ImageView)itemView.findViewById(R.id.VideoName);
            Img2=(de.hdodenhof.circleimageview.CircleImageView)itemView.findViewById(R.id.ProfilePicture);
        }

        public void setTitle( String title ) {
            Title.setText(title);
        }


        public void setShortDesc( String shortDesc ) {
            ShortDesc.setText(shortDesc);
        }

        public void setUsername( String username ) {
            Username.setText(username);
        }


        public void setDate( String date ) {
            datee.setText(date.toString());
        }

        public void setVideo_Thumb( String Video_Thumb ) {

//                bitmap.compress(Bitmap.CompressFormat.JPEG,100,fout);

            Picasso.with(ctx).load(Video_Thumb).resize(100,100).centerCrop().into(Img1);

        }

        public void setProfile( String profile ) {
            Picasso.with(ctx).load(profile).resize(100,100).centerCrop().into(Img2);        }


    }

}
