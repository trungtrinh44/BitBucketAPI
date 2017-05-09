package trung.bitbucket.view.adapters;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import net.cachapa.expandablelayout.ExpandableLayout;

import java.util.List;

import trung.bitbucket.R;
import trung.bitbucket.model.Repository;
import trung.bitbucket.view.fragments.RepoListFragment;

public class RepoListAdapter extends RecyclerView.Adapter<RepoListAdapter.ViewHolder> {

    private static final int UNSELECTED = -1;
    private final RepoListFragment.OnListFragmentInteractionListener mListener;
    private List<Repository> mValues;
    private RecyclerView recyclerView;
    private int selectedItem = UNSELECTED;
    private RepoListFragment repoListFragment;

    public RepoListAdapter(List<Repository> items, RepoListFragment.OnListFragmentInteractionListener listener, RecyclerView recyclerView, RepoListFragment repoListFragment) {
        mValues = items;
        mListener = listener;
        this.repoListFragment = repoListFragment;
        this.recyclerView = recyclerView;
    }

    public void setmValues(List<Repository> mValues) {
        this.mValues = mValues;
        notifyDataSetChanged();
    }

    public void addValue(Repository value) {
        mValues.add(value);
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.repofragment_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mIdView.setText(holder.mItem.name);
        holder.mContentView.setText(holder.mItem.description.isEmpty() ? "No description" : holder.mItem.description);
        holder.imgLock.setVisibility(holder.mItem.isPrivate ? View.VISIBLE : View.INVISIBLE);
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return mValues != null ? mValues.size() : 0;
    }

    public void deleteItem(Repository repo) {
        mValues.remove(repo);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public final View mView;
        public final TextView mIdView;
        public final TextView mContentView;
        public final ExpandableLayout expandableLayout;
        public final RelativeLayout expandButton;
        public final ImageView imgLock;
        public final ImageView imgTrash;
        public final TextView owner;
        public Repository mItem;
        private int position;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mIdView = (TextView) view.findViewById(R.id.id);
            mContentView = (TextView) view.findViewById(R.id.content);
            expandableLayout = (ExpandableLayout) view.findViewById(R.id.expandable_layout);
            expandButton = (RelativeLayout) view.findViewById(R.id.expandButton);
            imgLock = (ImageView) view.findViewById(R.id.imgPrivate);
            imgTrash = (ImageView) view.findViewById(R.id.imgTrashCan);
            owner = (TextView) view.findViewById(R.id.owner);
            expandButton.setOnClickListener(this);
        }

        public void bind(int position) {
            this.position = position;
            expandButton.setSelected(false);
            expandableLayout.collapse(false);
            if (mItem.owner.uuid.equals(repoListFragment.getUserInfo().uuid)) {
                owner.setText("Owner: You");
                imgTrash.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(repoListFragment.getContext());
                        builder.setTitle("Delete Repository").setMessage("Do you want to delete this repository?\nYour action cannot be undone?")
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        repoListFragment.deleteRepo(mItem);

                                    }
                                })
                                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {

                                    }
                                }).create().show();
                    }
                });
            } else {
                owner.setText("Owner: " + mItem.owner.displayName);
                imgTrash.setVisibility(View.INVISIBLE);
            }
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }

        @Override
        public void onClick(View view) {
            ViewHolder holder = (ViewHolder) recyclerView.findViewHolderForAdapterPosition(selectedItem);
            if (holder != null) {
                holder.mIdView.setSelected(false);
                holder.expandableLayout.collapse();
            }

            if (position == selectedItem) {
                selectedItem = UNSELECTED;
            } else {
                expandButton.setSelected(true);
                expandableLayout.expand();
                selectedItem = position;
            }

        }
    }
}
