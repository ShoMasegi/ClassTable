package masegi.sho.mytimetable.view.helper;

import android.graphics.Canvas;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;

import masegi.sho.mytimetable.view.adapters.MainTodoListAdapter;

/**
 * Created by masegi on 2017/10/13.
 */

public class RecyclerItemTouchHelper extends ItemTouchHelper.SimpleCallback {

    private RecyclerItemTouchHelperListener listener;

    public RecyclerItemTouchHelper(int dragDirs, int swipeDirs, RecyclerItemTouchHelperListener listener) {

        super(dragDirs, swipeDirs);
        this.listener = listener;
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {

        if (viewHolder instanceof MainTodoListAdapter.HolderViewHolder) {

            return true;
        } else {

            return false;
        }
    }

    @Override
    public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {

        if (viewHolder != null) {

            if (!(viewHolder instanceof MainTodoListAdapter.HolderViewHolder)) return;
            final View foregroundView = ((MainTodoListAdapter.HolderViewHolder)viewHolder).foregroundView;
            getDefaultUIUtil().onSelected(foregroundView);
        }
    }

    @Override
    public void onChildDrawOver(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {

        if (!(viewHolder instanceof MainTodoListAdapter.HolderViewHolder)) return;
        final View foregroundView = ((MainTodoListAdapter.HolderViewHolder)viewHolder).foregroundView;
        getDefaultUIUtil().onDrawOver(c, recyclerView, foregroundView, dX, dY, actionState,isCurrentlyActive);
    }

    @Override
    public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {

        if (!(viewHolder instanceof MainTodoListAdapter.HolderViewHolder)) return;
        final View foregroundView = ((MainTodoListAdapter.HolderViewHolder)viewHolder).foregroundView;
        getDefaultUIUtil().onDraw(c, recyclerView, foregroundView, dX, dY, actionState,isCurrentlyActive);
    }

    @Override
    public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {

        if (!(viewHolder instanceof MainTodoListAdapter.HolderViewHolder)) return;
        final View foregroundView = ((MainTodoListAdapter.HolderViewHolder)viewHolder).foregroundView;
        getDefaultUIUtil().clearView(foregroundView);
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {

        if (!(viewHolder instanceof MainTodoListAdapter.HolderViewHolder)) return;
        listener.onSwiped(viewHolder, direction, viewHolder.getAdapterPosition());
    }

    @Override
    public int convertToAbsoluteDirection(int flags, int layoutDirection) {

        return super.convertToAbsoluteDirection(flags, layoutDirection);
    }


    public interface RecyclerItemTouchHelperListener {

        void onSwiped(RecyclerView.ViewHolder holder, int direction, int position);
    }
}
