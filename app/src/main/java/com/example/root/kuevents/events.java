package com.example.root.kuevents;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;


public class events extends android.support.v4.app.Fragment {


    private RecyclerView mEventList;
    private DatabaseReference mDatabase;


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate( R.layout.fragment_events, container, false );
        super.onCreate( savedInstanceState );

        mDatabase = FirebaseDatabase.getInstance().getReference().child( "Events" );
        mEventList = view.findViewById( R.id.event_list );
        mEventList.setHasFixedSize( true );
        mEventList.setLayoutManager( new LinearLayoutManager( this.getActivity() ) );

        setHasOptionsMenu( true );

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        FirebaseRecyclerAdapter<Event, EventViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Event, EventViewHolder>( Event.class, R.layout.event_row, EventViewHolder.class, mDatabase ) {
            @Override
            protected void populateViewHolder(EventViewHolder viewHolder, Event model, int position) {

                final String post_key = getRef( position ).getKey();

                viewHolder.setTitle( model.getTitle() );
                viewHolder.setImage( getActivity().getApplicationContext(), model.getImage() );
                viewHolder.setStart_Date( model.getStart_date() );
                viewHolder.mView.setOnClickListener( new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent singleEventIntent = new Intent( "com.example.root.kuevents.second" );
                        singleEventIntent.putExtra( "Event ID", post_key );
                        startActivity( singleEventIntent );
                    }
                } );
            }
        };
        mEventList.setAdapter( firebaseRecyclerAdapter );


    }

    public static class EventViewHolder extends RecyclerView.ViewHolder {

        View mView;

        public EventViewHolder(View itemView) {
            super( itemView );
            mView = itemView;

        }

        public void setTitle(String title) {
            TextView post_title = mView.findViewById( R.id.post_title );
            post_title.setText( title );

        }


        public void setImage(Context ctx, String image) {
            ImageView post_image = mView.findViewById( R.id.post_image );
            Picasso.with( ctx ).load( image ).into( post_image );

        }

        public void setStart_Date(String start_date) {
            TextView post_date = mView.findViewById( R.id.post_date );
            post_date.setText( start_date );
        }
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate( R.menu.main_menu, menu );
        super.onCreateOptionsMenu( menu, inflater );
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_logout:

                FirebaseAuth.getInstance().signOut();
                // finish();
                startActivity( new Intent( this.getActivity(), LoginActivity.class ) );

                break;

            case R.id.action_add:
                startActivity( new Intent( this.getActivity(), PostActivity.class ) );

                break;

        }

        return super.onOptionsItemSelected( item );
    }


}

