package com.foodies.mohitgupta.foodyyoucantstsyhungry;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.lang.reflect.Method;

public class MainArea extends AppCompatActivity {

    static RecyclerView StreetRecyclerView;
    static DatabaseReference StreetRef;
    static FirebaseAuth mAuth;
    static DatabaseReference myref;
    static de.hdodenhof.circleimageview.CircleImageView Profile;
    static RecyclerView rView;
    static DatabaseReference myRef1;
    static DatabaseReference myRef2;
    static DatabaseReference StreetRef1;
    static ViewPager viewPager;
    public static ProgressBar p1,p11;
    static String uid;
    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //Clear the Activity's bundle of the subsidiary fragments' bundles.
        outState.clear();
    }

    @Override
    protected void onCreate( Bundle savedInstanceState ) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_area);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        final TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));
        //final android.support.v7.widget.Toolbar toolbar=(android.support.v7.widget.Toolbar)findViewById(R.id.toolbar);
        myref= FirebaseDatabase.getInstance().getReference("User_Data");
        mAuth=FirebaseAuth.getInstance();
        uid=mAuth.getCurrentUser().getUid();
        viewPager=findViewById(R.id.container);
        final TabLayout t=(TabLayout)findViewById(R.id.tabs);
        final FloatingActionButton fab=(FloatingActionButton)findViewById(R.id.fab);
        final android.support.design.widget.AppBarLayout appBarLayout=(android.support.design.widget.AppBarLayout)findViewById(R.id.appbar);
        final NestedScrollView nn=(NestedScrollView)findViewById(R.id.nest);
        final android.support.v7.widget.Toolbar toolbar=(android.support.v7.widget.Toolbar)findViewById(R.id.tool);
        setSupportActionBar(toolbar);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled( int position, float positionOffset, int positionOffsetPixels ) {
                if(position==0)
                {
                    CoordinatorLayout.LayoutParams p=(CoordinatorLayout.LayoutParams)fab.getLayoutParams();
                    p.setAnchorId(View.NO_ID);
                    fab.setLayoutParams(p);
                    toolbar.setVisibility(View.GONE);
                    fab.setVisibility(View.GONE);
                    t.setVisibility(View.GONE);
                }
                else
                {
                    fab.show();
                    toolbar.setVisibility(View.VISIBLE);
                    t.setVisibility(View.VISIBLE);
                    appBarLayout.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onPageSelected( int position ) {

            }

            @Override
            public void onPageScrollStateChanged( int state ) {

            }
        });
        mViewPager.setCurrentItem(1);
        mAuth=FirebaseAuth.getInstance();

    }

    @Override
    public boolean onCreateOptionsMenu( Menu menu ) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main_area, menu);
        if(getParent()!=null)
        {
            return getParent().onCreateOptionsMenu(menu);
        }
        else
        {
            return true;
        }

    }

    @Override
    public boolean onOptionsItemSelected( MenuItem item ) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.logOut) {
            mAuth.signOut();
            Intent a=new Intent(MainArea.this,MainActivity.class);
            startActivity(a);
            finish();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Recycler View Holder
     */


    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        public static FirebaseRecyclerAdapter<StreetDataFetcher, Street_food.StreetViewHolder> FBRA;
        public static FirebaseRecyclerAdapter<StreetDataFetcher,Kitchen_food.StreetViewHolder> FBRA1;
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";


        public PlaceholderFragment() {
        }


        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance( int sectionNumber ) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView( final LayoutInflater inflater, final ViewGroup container,
                                  Bundle savedInstanceState ) {
            View rootView = null;

            if (getArguments().getInt(ARG_SECTION_NUMBER) == 1) {

                rootView = inflater.inflate(R.layout.fragment_user_profile, container, false);
                final ProgressBar p=(ProgressBar) rootView.findViewById(R.id.progressbar11);
                final TextView username = (TextView) rootView.findViewById(R.id.userMain);
                final TextView username2 = (TextView) rootView.findViewById(R.id.UserCardView);
                final TextView Phone = (TextView) rootView.findViewById(R.id.Phone);
                MainArea.ValueUpdate(username, username2, Phone);
                Profile = (de.hdodenhof.circleimageview.CircleImageView) rootView.findViewById(R.id.ImageUser);
                myref.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange( DataSnapshot dataSnapshot ) {
                        if (dataSnapshot.child(mAuth.getCurrentUser().getUid()).hasChild("ProfileImage")) {
                            String url = dataSnapshot.child(mAuth.getCurrentUser().getUid()).child("ProfileImage").getValue().toString();
                            Picasso.with(getContext()).load(url).fit().centerCrop().into(Profile, new com.squareup.picasso.Callback() {
                                @Override
                                public void onSuccess() {
                                    p.setVisibility(View.INVISIBLE);
                                }

                                @Override
                                public void onError() {

                                }
                            });
                        } else {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                Profile.setImageDrawable(getResources().getDrawable(R.drawable.food2, getContext().getTheme()));
                            } else {
                                Profile.setImageDrawable(getResources().getDrawable(R.drawable.food2));
                            }
                        }
                    }

                    @Override
                    public void onCancelled( DatabaseError databaseError ) {

                    }
                });
                Profile.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick( View v ) {
                        Intent intent = new Intent(getActivity(), UserImageChange.class);
                        startActivity(intent);
                    }
                });
                ImageView button = (ImageView) rootView.findViewById(R.id.PencilClicked);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick( View v ) {
                        final Dialog MyDialog = new Dialog(getActivity());
                        MyDialog.setContentView(R.layout.username_edit);
                        MyDialog.setCancelable(true);
                        MyDialog.show();
                        Button bb = (Button) MyDialog.findViewById(R.id.UsernameChange);
                        bb.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick( View v ) {
                                EditText e = (EditText) MyDialog.findViewById(R.id.EditUsername);
                                MyDialog.cancel();
                                String user = e.getText().toString();
                                myref.child(uid).child("Name").setValue(user);
                                username.setText(user);
                                username2.setText(user);

                            }
                        });
                    }
                });
                return rootView;
            }

            //Street Work


            else if (getArguments().getInt(ARG_SECTION_NUMBER) == 2) {
                rootView = inflater.inflate(R.layout.fragment_street_food, container, false);

                p1=(ProgressBar)rootView.findViewById(R.id.StreetRecyclerProgress);
                p1.setVisibility(View.VISIBLE);
                //final LinearLayout likeLayout=(LinearLayout)rootView.findViewById(R.id.Likee);
                MainArea.viewPager.setBackgroundResource(R.drawable.grey_background);
                myRef1 = FirebaseDatabase.getInstance().getReference().child("Video_Upload").child("Street");
                myRef1.keepSynced(true);
                myRef1.orderByKey();
                //myRef1.limitToFirst(3);
                rView = (RecyclerView) rootView.findViewById(R.id.StreetRecyclerView);
                rView.hasFixedSize();
                LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
                rView.setLayoutManager(layoutManager);
                p1.setVisibility(View.VISIBLE);
                FBRA = new FirebaseRecyclerAdapter<StreetDataFetcher, Street_food.StreetViewHolder>(
                        StreetDataFetcher.class,
                        R.layout.card_recycler_view,
                        Street_food.StreetViewHolder.class,
                        myRef1
                ) {

                    @Override
                    protected void populateViewHolder( Street_food.StreetViewHolder viewHolder, StreetDataFetcher model, final int position ) {
                        p1.setVisibility(View.INVISIBLE);
                        viewHolder.setTitle(model.getVideoName());
                        viewHolder.setShortDesc(model.getShort_Desc());
                        viewHolder.setUsername(model.getUserName());
                        viewHolder.setDate(model.getDateTime());
                        viewHolder.setVideo_Thumb(model.getVideo_Thumb());
                        viewHolder.setProfile(model.getProfileImage());
                        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick( View v ) {
                                final String mref;
                                mref=FBRA.getRef(position).getKey().toString().trim();
                                //Toast.makeText(getActivity(),mref.toString(),Toast.LENGTH_SHORT).show();
                                 Intent i=new Intent(getActivity(),StreetVideoView.class);
                                i.putExtra("position",mref);
                                startActivity(i);
                             //   getActivity().finish();

                            }
                        });
                     /*   viewHolder.itemView.findViewById(R.id.Likee).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick( View v ) {
                                ImageView a;
                                a=(ImageView)v.findViewById(R.id.likeImage);
                                a.setImageResource(R.drawable.ic_heart_on);
                                final String mref;
                                mref=FBRA.getRef(position).getKey().toString().trim();
                                final DatabaseReference myyRef=FirebaseDatabase.getInstance().getReference().child("Video_Upload").child("Street").child(mref).child("Likes");
                                myyRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange( DataSnapshot dataSnapshot ) {
                                        Integer i=Integer.valueOf(dataSnapshot.getValue().toString());
                                        int ii=i+1;
                                        String like_value= String.valueOf(ii);
                                        myyRef.setValue(like_value);
                                    }

                                    @Override
                                    public void onCancelled( DatabaseError databaseError ) {

                                    }
                                });
                            }
                        });*/
                    }
                };
                rView.setNestedScrollingEnabled(false);
                rView.setAdapter(FBRA);

                return rootView;
            }


            //Kitchen Work



            else if(getArguments().getInt(ARG_SECTION_NUMBER)==3) {
                rootView = inflater.inflate(R.layout.fragment_kitchen_food, container, false);
                p11 = (ProgressBar) rootView.findViewById(R.id.KitchenRecyclerProgress);
                p11.setVisibility(View.VISIBLE);
       //         final LinearLayout likeLayout = (LinearLayout) rootView.findViewById(R.id.Likee);
                MainArea.viewPager.setBackgroundResource(R.drawable.grey_background);
                myRef1 = FirebaseDatabase.getInstance().getReference().child("Video_Upload").child("Kitchen");
                myRef1.keepSynced(true);
                myRef1.orderByKey();
                rView = (RecyclerView) rootView.findViewById(R.id.KitchenRecyclerView);
                rView.hasFixedSize();
                LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());

                rView.setLayoutManager(layoutManager);
               // final FirebaseRecyclerAdapter<StreetDataFetcher, Kitchen_food.StreetViewHolder> FBRA1;
                p11.setVisibility(View.VISIBLE);
                FBRA1 = new FirebaseRecyclerAdapter<StreetDataFetcher, Kitchen_food.StreetViewHolder>(
                        StreetDataFetcher.class,
                        R.layout.card_recycler_view,
                        Kitchen_food.StreetViewHolder.class,
                        myRef1
                ) {

                    @Override
                    protected void populateViewHolder( Kitchen_food.StreetViewHolder viewHolder, StreetDataFetcher model, final int position1 ) {
                        p11.setVisibility(View.INVISIBLE);
                        viewHolder.setTitle(model.getVideoName());
                        viewHolder.setShortDesc(model.getShort_Desc());
                        viewHolder.setUsername(model.getUserName());
                        viewHolder.setDate(model.getDateTime());
                        viewHolder.setVideo_Thumb(model.getVideo_Thumb());
                        viewHolder.setProfile(model.getProfileImage());
                        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick( View v ) {
                                final String mref;
                                mref = FBRA1.getRef(position1).getKey().toString().trim();
                                Intent i = new Intent(getActivity(), KitchenVideoView.class);
                                i.putExtra("position", mref);
                                startActivity(i);
                               // getActivity().finish();
                            }
                        });
                     /*   viewHolder.itemView.findViewById(R.id.Likee).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick( View v ) {
                                ImageView a;
                                a = (ImageView) v.findViewById(R.id.likeImage);
                                a.setImageResource(R.drawable.ic_heart_on);
                                final String mref;
                                mref = FBRA1.getRef(position1).getKey().toString().trim();
                                final DatabaseReference myyRef = FirebaseDatabase.getInstance().getReference().child("Video_Upload").child("Kitchen").child(mref).child("Likes");
                                myyRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange( DataSnapshot dataSnapshot ) {
                                        Integer i = Integer.valueOf(dataSnapshot.getValue().toString());
                                        int ii = i + 1;
                                        String like_value = String.valueOf(ii);
                                        myyRef.setValue(like_value);
                                    }

                                    @Override
                                    public void onCancelled( DatabaseError databaseError ) {

                                    }
                                });

                            }
                        });*/



                    }
                };
                rView.setNestedScrollingEnabled(false);
                rView.setAdapter(FBRA1);

                return rootView;
            }

            else
            {
                return rootView;
            }

        }
    }

    /*class StreetViewHolder extends RecyclerView.ViewHolder
    {
        TextView Username;
        TextView datee;
        TextView Title;
        TextView ShortDesc;

        public StreetViewHolder( View itemView ) {
            super(itemView);
            Username=(TextView)itemView.findViewById(R.id.UsernameText);
            datee=(TextView)itemView.findViewById(R.id.date);
            Title=(TextView)itemView.findViewById(R.id.Title);
            ShortDesc=(TextView)itemView.findViewById(R.id.ShortDescription);
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
            datee.setText(date);
        }
    }*/


    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter( FragmentManager fm ) {
            super(fm);
        }

        @Override
        public Fragment getItem( int position ) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return PlaceholderFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }


    }

    public void FloatingButtonClicked(View v)
    {
        Intent i=new Intent(this,UploadVideo.class);
        i.putExtra("Uid","abc");
        startActivity(i);

    }





    public static void ValueUpdate( final TextView username, final TextView username2, final TextView Phone ) {
        myref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange( DataSnapshot dataSnapshot ) {
                uid=mAuth.getCurrentUser().getUid();
                String value2=dataSnapshot.child(uid).child("Phone").getValue().toString();
                Phone.setText("+91"+" "+value2);
                if(dataSnapshot.child(uid).hasChild("Name"))
                {
                    String value=dataSnapshot.child(uid).child("Name").getValue().toString();
                    username.setText(value);
                    username2.setText(value);
                }
                else
                {
                    username.setText("UserName");
                    username2.setText("UserName");
                }
            }

            @Override
            public void onCancelled( DatabaseError databaseError ) {

            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        MobileDataCheck();
        MainActivity.checkPermissionStorage(this);
        MainActivity.checkPermissionReadStorage(this);
        MainActivity.checkPermissionInternet(this);
        MainActivity.checkPermissionNetwork(this);
        MainActivity.checkPermissionCalender(this);

    }

    public void MobileDataCheck()
    {
        boolean mobileDataEnabled = false; // Assume disabled
        ConnectivityManager cm = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        try {
            Class cmClass = Class.forName(cm.getClass().getName());
            Method method = cmClass.getDeclaredMethod("getMobileDataEnabled");
            method.setAccessible(true); // Make the method callable
            // get the setting for "mobile data"
            mobileDataEnabled = (Boolean)method.invoke(cm);
        } catch (Exception e) {
            // Some problem accessible private API
            // TODO do whatever error handling you want here
        }

        if(mobileDataEnabled==false)
        {
            final Dialog dialog=new Dialog(this);
            dialog.setContentView(R.layout.internet);
            dialog.setCancelable(false);
            Button b=(Button)dialog.findViewById(R.id.buttonn);
            b.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick( View v ) {
                    dialog.cancel();
                }
            });
            dialog.show();
        }
    }


}
