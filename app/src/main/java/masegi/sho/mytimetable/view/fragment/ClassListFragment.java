package masegi.sho.mytimetable.view.fragment;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import java.util.HashMap;

import masegi.sho.mytimetable.R;
import masegi.sho.mytimetable.di.contract.ClassListContract;
import masegi.sho.mytimetable.domain.value.ClassObject;
import masegi.sho.mytimetable.view.adapters.ClassCardAdapter;

/**
 * A simple {@link Fragment} subclass.
 */
public class ClassListFragment extends Fragment implements ClassListContract.ListViews{

    private static final String PAGE_KEY = "page";

    private View root;
    private RecyclerView recyclerView;
    private RelativeLayout emptyStateParent;
    private ClassCardAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    private ClassListContract.Presenter presenter;

    private int page;
    private ClassObject[] classes;
    private HashMap<String,String> memoMap;

    public ClassListFragment() {
        // Required empty public constructor
    }

    public static ClassListFragment newInstance(int page) {

        ClassListFragment fragment = new ClassListFragment();
        Bundle args = new Bundle();
        args.putInt(PAGE_KEY, page);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        page = getArguments().getInt(PAGE_KEY, 0);
        presenter.onListViewCreate(page);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        presenter.onListViewCreateView(page);
        root = inflater.inflate(R.layout.frag_classes_list, container, false);
        recyclerView = (RecyclerView)root.findViewById(R.id.classList_recycler_view);
        emptyStateParent = (RelativeLayout)root.findViewById(R.id.classList_empty_parent);
        recyclerView.setHasFixedSize(false);
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        adapter = new ClassCardAdapter(getContext(), classes, memoMap) {

            @Override
            protected void onItemClicked(ClassObject item) {

                super.onItemClicked(item);
                presenter.onItemClick(item);
            }
        };
        recyclerView.setAdapter(adapter);
        this.setupViews();
        return root;
    }

    @Override
    public void onDestroy() {

        presenter.onListViewDestroy(this.page);
        super.onDestroy();
    }

    @Override
    public void setPresenter(@NonNull ClassListContract.Presenter presenter) {

        this.presenter = presenter;
    }

    @Override
    public void setData(ClassObject[] classObjects, HashMap memoMap) {

        this.classes = classObjects;
        this.memoMap = memoMap;
        this.setupViews();
    }

    @Override
    public void update(ClassObject[] classObjects, HashMap memoMap) {

        this.setData(classObjects, memoMap);
        if (adapter != null) {

            adapter.setDataSet(classes, memoMap);
            adapter.notifyDataSetChanged();
        }
    }


    private void setupViews() {

        if (emptyStateParent == null || recyclerView == null) return;

        if (this.classes == null) {

            emptyStateParent.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        }
        else if (!(this.classes.length > 0)) {

            emptyStateParent.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        }
        else {

            emptyStateParent.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }
    }
}
