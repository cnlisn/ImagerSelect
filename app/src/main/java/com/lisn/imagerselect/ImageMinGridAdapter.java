package com.lisn.imagerselect;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

public class ImageMinGridAdapter extends BaseAdapter {
    private static final int TYPE_ADD = 0;
    private static final int TYPE_NORMAL = 1;
    private Context mContext;
    private LayoutInflater mInflater;
    private List<String> mImages = new ArrayList<>();
    final int mGridWidth;
    private boolean isHide = false;

    public void setHide(boolean isHide) {
        this.isHide = isHide;
    }

    public ImageMinGridAdapter(Context context, int column) {
        mContext = context;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        int width = 0;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            Point size = new Point();
            wm.getDefaultDisplay().getSize(size);
            width = size.x;
        } else {
            width = wm.getDefaultDisplay().getWidth();
        }
        mGridWidth = width / column;
    }

    public String getImageByPath(String path) {
        if (mImages != null && mImages.size() > 0) {
            for (String string : mImages) {
                if (string.equalsIgnoreCase(path)) {
                    return string;
                }
            }
        }
        return null;
    }
    /**
     * @param images
     */
    public void setData(List<String> images) {
        if (images != null && images.size() > 0) {
//            for (String path : images) {
//                if (path.contains("IMG_FISRT_")) {
////                    images.remove(path);
////                    images.add(0, path);
//                }
//            }
            mImages = images;
        } else {
            mImages.clear();
        }
        notifyDataSetChanged();
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == getCount() - 1) {
            return TYPE_ADD;
        }
        return TYPE_NORMAL;
    }

    @Override
    public int getCount() {
        return mImages.size() + 1;
    }

    @Override
    public String getItem(int i) {
        if (i == getCount() - 1) {
            return null;
        } else {
            return mImages.get(i);
        }
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (i == getCount() - 1) {
            view = mInflater.inflate(R.layout.list_item_add_image, viewGroup, false);
            if (isHide) {
                view.setVisibility(View.GONE);
            }
            return view;
        }
        ViewHolder holder;
        if (view == null) {
            view = mInflater.inflate(R.layout.list_item_min_image, viewGroup, false);
            holder = new ViewHolder(view);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        if (holder != null && i != getCount() - 1) {

            holder.bindData(getItem(i));
        }
        return view;
    }

    class ViewHolder {
        ImageView image;
        LinearLayout video;

        ViewHolder(View view) {
            image = (ImageView) view.findViewById(R.id.image);
            video = (LinearLayout) view.findViewById(R.id.Lin_video);
            view.setTag(this);
        }

        void bindData(final String data) {
            if (data == null) {
                return;
            }
            String url = "";
            if (data.contains("http://")) {
                url = data;
            } else {
                url = "file://" + data;
            }
            DisplayImageOptions options = new DisplayImageOptions.Builder()
                    .showStubImage(R.drawable.ic_empty)
                    .showImageForEmptyUri(R.drawable.ic_empty)
                    .showImageOnFail(R.drawable.ic_error)
                    .cacheInMemory(false)
                    .cacheOnDisk(true)
                    .considerExifParams(true)
                    .bitmapConfig(Bitmap.Config.RGB_565).build();
            if (data.contains("http://") && data.contains("mp4")) {
                image.setImageResource(R.drawable.image_mp4);
            } else {
                ImageLoader.getInstance().displayImage(url, image, options);
            }
        }
    }
}
