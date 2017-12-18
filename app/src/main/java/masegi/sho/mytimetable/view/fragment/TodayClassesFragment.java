package masegi.sho.mytimetable.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Calendar;

import masegi.sho.mytimetable.R;
import masegi.sho.mytimetable.di.contract.ClassListContract;
import masegi.sho.mytimetable.domain.value.ClassObject;
import masegi.sho.mytimetable.domain.value.DayOfWeek;
import masegi.sho.mytimetable.preferences.ClassTablePreference;
import masegi.sho.mytimetable.view.activity.DetailActivity;
import masegi.sho.mytimetable.view.adapters.ClassPagerAdapter;


public class TodayClassesFragment extends Fragment
        implements ViewPager.OnPageChangeListener, ClassListContract.Views {

    public static final String TAG = TodayClassesFragment.class.getSimpleName();
    private FragmentPagerAdapter adapter;
    private ViewPager pager;
    private TabLayout tabLayout;
    private DayOfWeek[] days;

    private ClassListContract.Presenter classListPresenter;


    public TodayClassesFragment() {
        // Required empty public constructor
    }

    public static TodayClassesFragment newInstance() {

        return new TodayClassesFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        days = ClassTablePreference.getInstance().getDaysOfWeek();
        adapter = new ClassPagerAdapter(getChildFragmentManager(), classListPresenter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.frag_today,container,false);
        tabLayout = (TabLayout)getActivity().findViewById(R.id.main_tab);
        pager = (ViewPager)root.findViewById(R.id.today_pager);

        pager.setAdapter(adapter);
        pager.addOnPageChangeListener(this);
        tabLayout.setupWithViewPager(pager);

        int index = 0;
        Calendar today = Calendar.getInstance();
        DayOfWeek todayWeek = DayOfWeek.getWeekByOrdinal(today.get(Calendar.DAY_OF_WEEK) - 1);
        for (int i=0; i < days.length; i++) {

            if (todayWeek == days[i]) {

                index = i;
                break;
            }
        }
        pager.setCurrentItem(index);
        return root;
    }

    @Override
    public void onDestroy() {

        classListPresenter.onMainViewDestroy();
        super.onDestroy();
    }


    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

    @Override
    public void onPageSelected(int position) {}

    @Override
    public void onPageScrollStateChanged(int state) {}


    @Override
    public void setPresenter(@NonNull ClassListContract.Presenter presenter) {
        this.classListPresenter = presenter;
    }

    @Override
    public void rebuild() {

        days = ClassTablePreference.getInstance().getDaysOfWeek();
        if (adapter != null && tabLayout != null) {

            adapter = new ClassPagerAdapter(getChildFragmentManager(), classListPresenter);
            pager.setAdapter(adapter);
            adapter.notifyDataSetChanged();
            tabLayout.setupWithViewPager(pager);
        }
    }

    @Override
    public void startDetailActivity(ClassObject item) {

        Intent intent = new Intent(getActivity(), DetailActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable(DetailActivity.EXTRA_CLASS_OBJECT, item);
        intent.putExtra(DetailActivity.EXTRA_BUNDLE, bundle);
        startActivity(intent);
    }
}
