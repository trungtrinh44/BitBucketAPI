package trung.bitbucket.view.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import trung.bitbucket.R;
import trung.bitbucket.interfaces.PresenterViewOps;
import trung.bitbucket.interfaces.ViewOps;
import trung.bitbucket.model.Repositories;
import trung.bitbucket.model.Repository;
import trung.bitbucket.model.UserInfo;
import trung.bitbucket.presenter.MainPresenter;
import trung.bitbucket.view.activities.MainActivity;
import trung.bitbucket.view.adapters.RepoListAdapter;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class RepoListFragment extends BaseFragment implements ViewOps, CreateNewRepoFragment.OnFragmentInteractionListener {
    private final String REPOSITORIES = "Repositories";
    private OnListFragmentInteractionListener mListener;
    private PresenterViewOps presenter;
    private Repositories repositories;
    private RepoListAdapter adapter;
    private RecyclerView recyclerView;
    private UserInfo userInfo;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public RepoListFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static RepoListFragment newInstance(UserInfo info) {
        RepoListFragment fragment = new RepoListFragment();
        fragment.title = "Repositories";
        Log.d("RepoListFragment", "create new instance");
        return fragment;
    }

    public UserInfo getUserInfo() {
        return userInfo;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            Log.d("RepoListFragment", "save instance state not null");
            repositories = (Repositories) savedInstanceState.getSerializable(REPOSITORIES);
            if (repositories != null) Log.d("RepoListFragment", "get repositories oncreate");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.repofragment_item_list, container, false);

        // Set the adapter
        Context context = view.getContext();
        recyclerView = (RecyclerView) view.findViewById(R.id.repoList);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        userInfo = ((MainActivity) getContext()).getUserInfo();
        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity) getContext()).callCreateNewRepo(presenter);
            }
        });
        presenter = new MainPresenter(this);
        if (repositories != null) {
            adapter = new RepoListAdapter(repositories.values, mListener, recyclerView, this);
            recyclerView.setAdapter(adapter);
        } else if (presenter != null) {
            Log.d("RepoListFragment", "get repositories presenter");
            presenter.getRepositories(userInfo.username);
        }
        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void loginSuccessful(UserInfo userInfo, String token) {

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(REPOSITORIES, repositories);
    }

    @Override
    public void showToast(String message) {

    }

    @Override
    public void returnRepositories(Repositories repos) {
        repositories = repos;
        adapter = new RepoListAdapter(repositories.values, mListener, recyclerView, this);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void repoCreated(Repository repository) {
        adapter.addValue(repository);
    }

    @Override
    public void storeToken(String token) {
        ((MainActivity) getContext()).storeToken(token);
    }

    @Override
    public void repoDeleted(Repository repo) {
        adapter.deleteItem(repo);
    }

    @Override
    public void deleteRepo(Repository repo) {
        if (presenter != null) presenter.deleteRepo(repo);
    }

    @Override
    public void onFragmentInteraction() {

    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        void onListFragmentInteraction(Repository item);
    }
}
