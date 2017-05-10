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

import static trung.bitbucket.utils.Constants.userInfo;

public class RepoListFragment extends BaseFragment implements ViewOps {
    private final String REPOSITORIES = "Repositories";
    private PresenterViewOps presenter;
    private Repositories repositories;
    private RepoListAdapter adapter;
    private RecyclerView recyclerView;


    public RepoListFragment() {
    }


    public static RepoListFragment newInstance() {
        RepoListFragment fragment = new RepoListFragment();
        fragment.title = "Repositories";
        Log.d("RepoListFragment", "create new instance");
        return fragment;
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
        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity) getContext()).callCreateNewRepo(presenter);
            }
        });
        presenter = new MainPresenter(this);
        if (repositories != null) {
            adapter = new RepoListAdapter(repositories.values, recyclerView, this);
            recyclerView.setAdapter(adapter);
        } else {
            Log.d("RepoListFragment", "get repositories presenter");
            presenter.getRepositories(userInfo.username);
        }
        return view;
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
        adapter = new RepoListAdapter(repositories.values, recyclerView, this);
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


}
