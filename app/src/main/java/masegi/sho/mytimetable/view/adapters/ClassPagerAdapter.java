package masegi.sho.mytimetable.view.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import masegi.sho.mytimetable.di.contract.ClassListContract;
import masegi.sho.mytimetable.domain.entity.ClassTableEntity;
import masegi.sho.mytimetable.domain.value.DayOfWeek;
import masegi.sho.mytimetable.preferences.ClassTablePreference;
import masegi.sho.mytimetable.view.fragment.ClassListFragment;

/**
 * Created by masegi on 2017/10/04.
 */

public class ClassPagerAdapter extends FragmentPagerAdapter {

    private final ClassListContract.Presenter presenter;
    private final DayOfWeek[] days;

    public ClassPagerAdapter(FragmentManager manager, ClassListContract.Presenter presenter) {

        super(manager);
        this.presenter = presenter;
        this.days = ClassTablePreference.getInstance().getDaysOfWeek();
    }

    @Override
    public int getItemPosition(Object object) {

        ClassListContract.ListViews view = (ClassListContract.ListViews)object;
        if (view != null) view.update();
        return super.getItemPosition(object);
    }

    @Override
    public Fragment getItem(int position) {

        ClassListFragment fragment = (ClassListFragment) presenter.getView(position);
        if (fragment == null) {

            fragment = ClassListFragment.newInstance(position);
            presenter.attachListViews(position, fragment);
        }
        return fragment;
    }

    @Override
    public CharSequence getPageTitle(int position) {

        if (days.length > 5) {

            return days[position].getWeekName().substring(0, 1);
        }
        else {

            return days[position].getWeekName().substring(0, 3);
        }
    }

    @Override
    public int getCount() {

        return days.length;
    }
}
