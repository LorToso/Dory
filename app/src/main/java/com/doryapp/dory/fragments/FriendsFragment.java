package com.doryapp.dory.fragments;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.SearchView;

import com.doryapp.backend.myApi.model.DoryUser;
import com.doryapp.dory.R;
import com.doryapp.dory.activities.AddFriendActivity;
import com.doryapp.dory.apiCalls.ApiCall;
import com.doryapp.dory.apiCalls.GetFriendsByNicknameCall;
import com.doryapp.dory.apiCalls.GetFriendsCall;
import com.doryapp.dory.apiCalls.GetRequestingUsersCall;
import com.doryapp.dory.extendedViews.FriendRequestView;
import com.doryapp.dory.extendedViews.UserDetailsView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FriendsFragment extends Fragment {

    List<DoryUser> friendRequests = new ArrayList<>();
    List<DoryUser> displayedUsers = new ArrayList<>();
    Handler mHandler = new Handler(Looper.getMainLooper()){
        @Override
        public void handleMessage(Message inputMessage) {
            displayUsers();
        }
    };


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onStart()
    {
        super.onStart();
        findFriendRequests();
        findAllFriends();
    }

    private void findFriendRequests() {
        new GetRequestingUsersCall(getActivity()).onComplete(new ApiCall.OnComplete<List<DoryUser>>() {
            @Override
            public void execute(List<DoryUser> users) {
                friendRequests = users;

            }
        }).execute();
    }

    private void findAllFriends() {
        new GetFriendsCall(getActivity()).onComplete(new ApiCall.OnComplete<List<DoryUser>>() {
            @Override
            public void execute(List<DoryUser> users) {
                displayedUsers = users;
                notifyUIThread();
            }
        }).execute();
    }

    private void notifyUIThread() {
        mHandler.sendEmptyMessage(0);
    }

    private void findUsersWithFilter(String nickName) {
        new GetFriendsByNicknameCall(getActivity(), nickName).onComplete(new ApiCall.OnComplete<List<DoryUser>>() {
            @Override
            public void execute(List<DoryUser> users) {
                displayedUsers = users;
                notifyUIThread();
            }
        }).execute();
    }

    public void onClickSearch(View v) throws IOException {
        SearchView searchView  = (SearchView)v;
        CharSequence searchedName = searchView.getQuery();
        findUsersWithFilter(searchedName.toString());
    }

    public void onClickAddFriend()
    {
        Intent startActivity = new Intent(getActivity(),AddFriendActivity.class);
        startActivityForResult(startActivity, 0);
    }

    private void displayUsers() {
        LinearLayout view = (LinearLayout)getActivity().findViewById(R.id.userListView);

        clearViews(view);
        showFriendRequests(view);
        showFriends(view);

    }

    private void showFriendRequests(LinearLayout view) {
        View.OnClickListener acceptRequestCall = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO
            }
        };
        View.OnClickListener ignoreRequestCall = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO
            }
        };


        for(DoryUser user : displayedUsers)
        {
            FriendRequestView requestView = new FriendRequestView(getActivity(), user);
            requestView.setAcceptButtonListener(acceptRequestCall);
            requestView.setIgnoreButtonListener(ignoreRequestCall);
            view.addView(requestView);
        }
    }

    private void clearViews(LinearLayout view) {
        if(view.getChildCount() > 0)
            view.removeAllViews();
    }

    private void showFriends(LinearLayout view) {
        for(DoryUser user : displayedUsers)
        {
            view.addView(new UserDetailsView(getActivity(), user));
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view =  inflater.inflate(
                R.layout.fragment_friends, container, false);


        FloatingActionButton button = (FloatingActionButton) view.findViewById(R.id.btnAddFriendFloating);

        if(button != null)
        {
            button.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    onClickAddFriend();
                }
            });
        }

        return view;
    }
}

