package com.example.listviewpulldownrefreshpulluploadmore;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;

/**
 * Created by dumingwei on 2016/8/20.
 */
public class MyListView extends ListView {

    private boolean isPullUpLoading = false;//是否正在加载更多
    private boolean noMoreItem = false;//是否还有更多数据
    private LoadMoreListener loadMoreListener;//加载更多的回调接口

    public void setNoMoreItem(boolean noMoreItem) {
        this.noMoreItem = noMoreItem;
    }


    private PullUpLoadListViewFooter mFooterView;

    public void setPullUpLoading(boolean pullUpLoading) {
        isPullUpLoading = pullUpLoading;
    }

    public void setLoadMoreListener(LoadMoreListener loadMoreListener) {
        this.loadMoreListener = loadMoreListener;
    }

    public MyListView(Context context) {
        this(context, null);
    }

    public MyListView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        isPullUpLoading = false;
        mFooterView = new PullUpLoadListViewFooter(getContext());
        setOnScrollListener(new MyOnScrollerListener());
    }

    private class MyOnScrollerListener implements OnScrollListener {

        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {
            //do nothing here
        }

        /**
         * Callback method to be invoked when the list or grid has been scrolled. This will be
         * called after the scroll has completed
         *
         * @param view             The view whose scroll state is being reported
         * @param firstVisibleItem the index of the first visible cell (ignore if
         *                         visibleItemCount == 0)
         * @param visibleItemCount the number of visible cells
         * @param totalItemCount   the number of items in the list adaptor
         */
        @Override
        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
/**
 * 判断条件表示的意思
 * !isPullUpLoading 不是处于正在加载更多的状态中
 * (firstVisibleItem + visibleItemCount) == totalItemCount ，ListView的最后一个item可见
 *  getScrollDistance() > 0 ，ListView 向上滑动的距离大于0（如果不加这个条件，当ListView最初只有几项数据，以至于，前两个条件满足，会触发加载更多操作）
 */
            if (!isPullUpLoading && !noMoreItem && (firstVisibleItem + visibleItemCount) == totalItemCount && getScrollDistance() > 0) {
                showFootView();
                if (loadMoreListener != null) {
                    loadMoreListener.loadMore();
                }
            }
        }
    }

    /**
     * 的到ListView 在竖直方向上的滑动距离
     *
     * @return
     */
    public int getScrollDistance() {
        View c = getChildAt(0);
        if (c == null) {
            return 0;
        }
        int firstVisiblePosition = getFirstVisiblePosition();
        int top = c.getTop();
        return -top + firstVisiblePosition * c.getHeight();
    }

    /**
     * 加载更多的回调接口
     */
    public interface LoadMoreListener {
        void loadMore();
    }

    /**
     * 显示footView
     */
    public void showFootView() {
        if (getFooterViewsCount() < 1) {
            addFooterView(mFooterView);
        }
        mFooterView.setVisibility(View.VISIBLE);
        mFooterView.updateView(PullUpLoadListViewFooter.State.LOADING);//显示Loading
    }

    /**
     * 隐藏掉footView
     */
    public void removeFootView() {
        setPullUpLoading(false);
        // removeFooterView(mFooterView);
        mFooterView.setVisibility(View.GONE);
    }

    /**
     * 数据已经完全加载完毕，footView 显示 no  more
     */
    public void haveLoadAll() {
        setNoMoreItem(true);//没有更多数据
        if (mFooterView != null) {
            mFooterView.setVisibility(View.VISIBLE);
            mFooterView.updateView(PullUpLoadListViewFooter.State.NOT_LOADING);//显示no more data
        }

    }
}

