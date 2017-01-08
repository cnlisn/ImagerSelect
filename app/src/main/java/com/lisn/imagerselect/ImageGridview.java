package com.lisn.imagerselect;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;


import java.util.ArrayList;

/**
 * Created by tanjunze on 2016/2/26.
 */
public class ImageGridview extends GridView {
    private ArrayList<String> datas = null;
    private ImageMinGridAdapter mAdapter;
    private AddImagesCallback callback = null;
    private OnItemClickCallback onItemClickCallback;

    private boolean isHide = false;

    public interface AddImagesCallback {
        public void addCallback();
    }

    public interface OnItemClickCallback {
        public void onItemClick(int postion, String path);
    }

    public void setAddImagesCallback(AddImagesCallback callback) {
        this.callback = callback;
    }

    public void setOnItemClickCallback(OnItemClickCallback onItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback;
    }

    public void setHide(boolean isHide) {
        if (mAdapter != null) {
            mAdapter.setHide(isHide);
        }
    }

    public ImageGridview(Context context) {
        super(context);
        init(context);
    }
    public ImageGridview(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }
    public ImageGridview(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }
    private void init(Context context) {
        setPadding(12, 12, 12, 12);
        setClipToPadding(false);
        setHorizontalSpacing(getResources().getDimensionPixelSize(R.dimen.space_size));
        setVerticalSpacing(getResources().getDimensionPixelSize(R.dimen.space_size));
        LayoutParams params = new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
        setLayoutParams(params);
        datas = new ArrayList<>();
        mAdapter = new ImageMinGridAdapter(context, 4);
        setAdapter(mAdapter);
        setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (mAdapter.getCount() - 1 == position) {
                    if (callback != null) {
                        callback.addCallback();
                    }
                } else {
                    if (onItemClickCallback != null) {
                        onItemClickCallback.onItemClick(position, datas.get(position));
                    }
                }
            }
        });
    }
    public void setData(ArrayList<String> datas) {
        this.datas.clear();
        this.datas = datas;
        mAdapter.setData(datas);
    }

    public void delectImage(String path) {
        datas.remove(path);
        mAdapter.notifyDataSetChanged();
    }
    public void addImage(int index,String path) {
        datas.add(0, path);
        mAdapter.notifyDataSetChanged();
    }
    public void replaceImage(int index,String path) {
        datas.remove(0);
        datas.add(0,path);
        mAdapter.notifyDataSetChanged();
    }

    public ArrayList<String> getImageByPath(){
//        mAdapter.getImageByPath()
        return datas;
    }
    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}
