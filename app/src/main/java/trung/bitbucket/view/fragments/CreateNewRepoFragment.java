package trung.bitbucket.view.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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
import trung.bitbucket.view.activities.MainActivity;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CreateNewRepoFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CreateNewRepoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CreateNewRepoFragment extends BaseFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static PresenterViewOps presenter;
    private EditText edtName;
    private EditText edtDesc;
    private CheckBox checkBox;
    private RadioButton gitBtn;
    private RadioButton mecurialBtn;
    private Spinner forkOption;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private OnFragmentInteractionListener mListener;

    public CreateNewRepoFragment() {
        // Required empty public constructor
    }

    public static CreateNewRepoFragment newInstance(OnFragmentInteractionListener mListener, PresenterViewOps presenter) {
        CreateNewRepoFragment fragment = new CreateNewRepoFragment();
        fragment.mListener = mListener;
        CreateNewRepoFragment.presenter = presenter;
        fragment.title = "Create new repository";
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
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
                String username = ((MainActivity) getContext()).getUserInfo().username;
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
                presenter.createRepositories(new CreateNewRepo.RepoInfo(isPrivate, desc, scm, name, fork_policy), username);
                if (mListener != null) mListener.onFragmentInteraction();
            }
        });
        return root;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        }
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
        void onFragmentInteraction();
    }
}
