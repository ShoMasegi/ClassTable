package masegi.sho.mytimetable.view.fragment;


import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import masegi.sho.mytimetable.R;
import masegi.sho.mytimetable.api.OrdinalNumber;
import masegi.sho.mytimetable.di.contract.ClassListContract;
import masegi.sho.mytimetable.domain.value.ClassObject;
import masegi.sho.mytimetable.domain.value.ClassTime;
import masegi.sho.mytimetable.domain.value.ThemeColor;
import masegi.sho.mytimetable.preferences.ClassTablePreference;
import masegi.sho.mytimetable.presenter.SettingsClassTimePresenter;
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

    private ClassListContract.Presenter classListPresenter;

    int page;
    private ClassObject[] classes;
    private HashMap<String,String> memoMap;
    private boolean isEmpty = false;

    public ClassListFragment() {
        // Required empty public constructor
    }

    public static ClassListFragment newInstance(int page){
        ClassListFragment fragment = new ClassListFragment();
        Bundle args = new Bundle();
        args.putInt(PAGE_KEY,page);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        page = getArguments().getInt(PAGE_KEY,0);
        classListPresenter.prepare(page);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

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
                classListPresenter.onItemClick(item);
            }
        };
        recyclerView.setAdapter(adapter);
        setupViews();
        return root;
    }


    @Override
    public void setPresenter(@NonNull ClassListContract.Presenter presenter) {

        this.classListPresenter = presenter;
    }

    @Override
    public void setData(ClassObject[] classObjects, HashMap memoMap) {

        this.classes = classObjects;
        this.memoMap = memoMap;
        isEmpty = false;
    }

    @Override
    public void showNoData() {

        isEmpty = true;
    }

    @Override
    public void update() {

        classListPresenter.prepare(page);
        adapter.setDataSet(classes, memoMap);
        adapter.notifyDataSetChanged();
        setupViews();
    }


    private void setupViews() {

        if (isEmpty) {

            emptyStateParent.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        } else {

            emptyStateParent.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }
    }
}
