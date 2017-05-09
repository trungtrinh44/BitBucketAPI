package trung.bitbucket.view.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

import trung.bitbucket.view.activities.MainActivity;

/**
 * mai
 */

public abstract class BaseFragment extends Fragment {
    private final String TITLE = "title";
    protected String title;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof MainActivity) {

            ((MainActivity) context).setCurrentFragment(this);
        }
        ((AppCompatActivity) context).setTitle(title);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            title = savedInstanceState.getString(TITLE);
            ((AppCompatActivity) getContext()).setTitle(title);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(TITLE, title);
    }
}
