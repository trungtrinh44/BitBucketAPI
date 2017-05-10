package trung.bitbucket.view.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import trung.bitbucket.R;
import trung.bitbucket.interfaces.PresenterViewOps;
import trung.bitbucket.model.CreateNewRepo;
import trung.bitbucket.utils.Constants;


public class CreateNewRepoFragment extends BaseFragment {

    private static PresenterViewOps presenter;
    private EditText edtName;
    private EditText edtDesc;
    private CheckBox checkBox;
    private RadioButton gitBtn;
    private RadioButton mecurialBtn;
    private Spinner forkOption;

    public CreateNewRepoFragment() {
        // Required empty public constructor
    }

    public static CreateNewRepoFragment newInstance(PresenterViewOps presenter) {
        CreateNewRepoFragment fragment = new CreateNewRepoFragment();
        CreateNewRepoFragment.presenter = presenter;
        fragment.title = "Create new repository";
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_create_new_repo, container, false);
        edtDesc = (EditText) root.findViewById(R.id.edtDesc);
        edtName = (EditText) root.findViewById(R.id.edtName);
        checkBox = (CheckBox) root.findViewById(R.id.checkBox);
        gitBtn = (RadioButton) root.findViewById(R.id.gitRadioBtn);
        mecurialBtn = (RadioButton) root.findViewById(R.id.mecurialRadioBtn);
        forkOption = (Spinner) root.findViewById(R.id.forkOptSpinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(), R.array.forkOptions, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        forkOption.setAdapter(adapter);
        root.findViewById(R.id.btnCreateRepo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = edtName.getText().toString();
                if (name.trim().equals("")) {
                    Toast.makeText(getContext(), "Repository name cannot be empty", Toast.LENGTH_SHORT).show();
                    return;
                }
                String desc = edtDesc.getText().toString();
                boolean isPrivate = checkBox.isChecked();
                String scm = mecurialBtn.isChecked() ? "hg" : "git";
                String fork_policy;
                switch (forkOption.getSelectedItemPosition()) {
                    case 1:
                        fork_policy = "no_public_forks";
                        break;
                    case 2:
                        fork_policy = "no_forks";
                        break;
                    default:
                        fork_policy = "allow_forks";
                        break;
                }
                Log.d("CreateNewRepo", name + " " + desc + " " + scm + " " + fork_policy);
                presenter.createRepository(new CreateNewRepo.RepoInfo(isPrivate, desc, scm, name, fork_policy), Constants.userInfo.username);
                getActivity().onBackPressed();
            }
        });
        return root;
    }
}
