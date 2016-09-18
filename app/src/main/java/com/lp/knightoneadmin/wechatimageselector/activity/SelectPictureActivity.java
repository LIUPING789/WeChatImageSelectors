package com.lp.knightoneadmin.wechatimageselector.activity;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.lp.knightoneadmin.wechatimageselector.R;
import com.lp.knightoneadmin.wechatimageselector.utils.BitmapUtil;
import com.lp.knightoneadmin.wechatimageselector.utils.FileUtil;
import com.lp.knightoneadmin.wechatimageselector.utils.FinalNumInter;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @Module :
 * @Comments : SelectPictureActivity
 * @Author : KnightOneAdmin
 * @CreateDate : 16/9/11
 * @ModifiedBy : KnightOneAdmin
 * @ModifiedDate: 下午3:45
 * @Modified : SelectPictureActivity
 */
public class SelectPictureActivity extends AppCompatActivity implements FinalNumInter,View.OnClickListener{
    /**
     * 最多选择图片的个数
     */
    private int MAX_NUM;
    private static final int MAX_NUM_IMAGE = 9;
    private static final int TAKE_PICTURE = 520;
    public static final String INTENT_MAX_NUM = "intent_max_num";
    public static final String INTENT_SELECTED_PICTURE = "paths";
    private File picture;
    private Context context;
    @BindView(R.id.gridview)
    GridView gridview;
    @BindView(R.id.btn_select)
    Button btn_select;
    @BindView(R.id.btn_ok)
    Button btn_ok;
    @BindView(R.id.listview)
    ListView listview;
    private PictureAdapter adapter;
    @BindView(R.id.btn_back)
    ImageView btn_back;
    /**
     * 临时的辅助类，用于防止同一个文件夹的多次扫描
     */
    private HashMap<String, Integer> tmpDir = new HashMap<String, Integer>();
    private ArrayList<ImageFloder> mDirPaths = new ArrayList<ImageFloder>();
    private ContentResolver mContentResolver;
    private FolderAdapter folderAdapter;
    private ImageFloder imageAll, currentImageFolder;
    /**
     * 已选择的图片
     */
    private ArrayList<String> selectedPicture = new ArrayList<String>();
    private String cameraPath = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_picture);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        initData();
        initOnClickListener();
    }

    private void initOnClickListener() {
        btn_back.setOnClickListener(this);
    }

    private void initData() {
        MAX_NUM = MAX_NUM_IMAGE - getIntent().getIntExtra(INTENT_MAX_NUM, 0);
        context = this;
        mContentResolver = getContentResolver();
        imageAll = new ImageFloder();
        imageAll.setDir("/所有图片");
        currentImageFolder = imageAll;
        mDirPaths.add(imageAll);
        btn_ok = (Button) findViewById(R.id.btn_ok);
        btn_select = (Button) findViewById(R.id.btn_select);
        btn_ok.setText(String.format("完成0/%d", MAX_NUM));
        ((TextView) findViewById(R.id.date_title)).setText("预览");
        gridview = (GridView) findViewById(R.id.gridview);
        adapter = new PictureAdapter();
        gridview.setAdapter(adapter);
        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    goCamare();
                }
            }
        });
        listview = (ListView) findViewById(R.id.listview);
        folderAdapter = new FolderAdapter();
        listview.setAdapter(folderAdapter);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                currentImageFolder = mDirPaths.get(position);
                hideListAnimation();
                adapter.notifyDataSetChanged();
                btn_select.setText(currentImageFolder.getName());
            }
        });
        getThumbnail();
    }


    public void select(View v) {
        if (listview.getVisibility() == View.VISIBLE) {
            hideListAnimation();
        } else {
            listview.setVisibility(View.VISIBLE);
            showListAnimation();
            folderAdapter.notifyDataSetChanged();
        }
    }

    public void showListAnimation() {
        TranslateAnimation ta = new TranslateAnimation(1, 0f, 1, 0f, 1, 1f, 1, 0f);
        ta.setDuration(200);
        listview.startAnimation(ta);
    }

    public void hideListAnimation() {
        TranslateAnimation ta = new TranslateAnimation(1, 0f, 1, 0f, 1, 0f, 1, 1f);
        ta.setDuration(200);
        listview.startAnimation(ta);
        ta.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                listview.setVisibility(View.GONE);
            }
        });
    }

    /**
     * 点击完成按钮
     */
    public void ok(View v) {
        if (selectedPicture.size() > 0) {
            Intent data = new Intent();
            data.putExtra(INTENT_SELECTED_PICTURE, selectedPicture);
            setResult(PHOTORESULT, data);
            this.finish();
        }

    }

    /**
     * 使用相机拍照
     */
    protected void goCamare() {
        if (selectedPicture.size() + 1 > MAX_NUM) {
            Toast.makeText(context, "最多选择" + MAX_NUM + "张", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent openCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        String fileName = System.currentTimeMillis() + ".jpg";
        picture = FileUtil.getFile(fileName);
        openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(picture));
        openCameraIntent.putExtra(MediaStore.Images.Media.ORIENTATION, 0);
        startActivityForResult(openCameraIntent, TAKE_PICTURE);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // 防止有些手机当activity挂着的时候回回收变量
        if (null == picture)
            return;
        outState.putString("path", picture.getAbsolutePath()); // 暂存在outState中
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        String path = savedInstanceState.getString("path"); // 从保存的数据中恢复
        if (null != path)
            picture = new File(path);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
         super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PHOTO_RESOULT) {
            selectedPicture.add(picture.getAbsolutePath());
            Intent data2 = new Intent();
            data2.putExtra(INTENT_SELECTED_PICTURE, selectedPicture);
            setResult(RESULT_OK, data2);
            this.finish();
            return;
        } else if (requestCode == TAKE_PICTURE) {
            // 设置文件保存路径
            startPhotoZoom(Uri.fromFile(picture));
            return;
        }
    }

    private void startPhotoZoom(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");// 调用Android系统自带的一个图片剪裁页面,
        intent.setDataAndType(uri, FinalNumInter.IMAGE_UNSPECIFIED);
        intent.putExtra("crop", "true");// 进行修剪
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", FinalNumInter.PHOTO_ZOOM_OUTPUT_X);
        intent.putExtra("outputY", FinalNumInter.PHOTO_ZOOM_OUTPUT_Y);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, PHOTO_RESOULT);
    }

    @Override
    public void onClick(View view) {

    }

    class PictureAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return currentImageFolder.images.size() + 1;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            ViewHolder holder;
            if (convertView == null) {
                convertView = View.inflate(context, R.layout.grid_item_picture, null);
                holder = new ViewHolder();
                holder.iv = (ImageView) convertView.findViewById(R.id.iv);
                holder.checkBox = (Button) convertView.findViewById(R.id.check);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            if (position == 0) {
                holder.iv.setImageResource(R.mipmap.pickphotos_to_camera_normal);
                holder.checkBox.setVisibility(View.GONE);
            } else {
                position = position - 1;
                holder.checkBox.setVisibility(View.VISIBLE);
                final ImageItem item = currentImageFolder.images.get(position);
                BitmapUtil.setImageViewByImagLoading(context, "file://" + item.path, holder.iv);
                boolean isSelected = selectedPicture.contains(item.path);
                if (isSelected) {
                    holder.checkBox.setSelected(true);
                    holder.checkBox.setEnabled(false);
                    holder.checkBox.setOnClickListener(null);
                } else {
                    holder.checkBox.setSelected(false);
                    holder.checkBox.setEnabled(true);
                    holder.checkBox.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (!v.isSelected() && selectedPicture.size() + 1 > MAX_NUM) {
                                Toast.makeText(context, "最多选择" + MAX_NUM + "张", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            if (selectedPicture.contains(item.path)) {
                                selectedPicture.remove(item.path);
                            } else {
                                selectedPicture.add(item.path);
                            }
                            btn_ok.setEnabled(selectedPicture.size() > 0);
                            btn_ok.setText(String.format("完成%d/%d", selectedPicture.size(), MAX_NUM));
                            v.setSelected(selectedPicture.contains(item.path));
                        }
                    });
                }

                holder.checkBox.setSelected(isSelected);
            }
            return convertView;
        }
    }

    class FolderViewHolder {
        ImageView id_dir_item_image;
        ImageView choose;
        TextView id_dir_item_name;
        TextView id_dir_item_count;
    }

    /**
     * 得到缩略图
     */
    private void getThumbnail() {
        Cursor mCursor = mContentResolver.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                new String[]{MediaStore.Images.ImageColumns.DATA}, "", null,
                MediaStore.MediaColumns.DATE_ADDED + " DESC");
        // Log.e("TAG", mCursor.getCount() + "");
        if (mCursor.moveToFirst()) {
            int _date = mCursor.getColumnIndex(MediaStore.Images.Media.DATA);
            do {
                // 获取图片的路径
                String path = mCursor.getString(_date);
                // Log.e("TAG", path);
                imageAll.images.add(new ImageItem(path));
                // 获取该图片的父路径名
                File parentFile = new File(path).getParentFile();
                if (parentFile == null) {
                    continue;
                }
                ImageFloder imageFloder;
                String dirPath = parentFile.getAbsolutePath();
                if (!tmpDir.containsKey(dirPath)) {
                    // 初始化imageFloder
                    imageFloder = new ImageFloder();
                    imageFloder.setDir(dirPath);
                    imageFloder.setFirstImagePath(path);
                    mDirPaths.add(imageFloder);
                    // Log.d("zyh", dirPath + "," + path);
                    tmpDir.put(dirPath, mDirPaths.indexOf(imageFloder));
                } else {
                    imageFloder = mDirPaths.get(tmpDir.get(dirPath));
                }
                imageFloder.images.add(new ImageItem(path));
            } while (mCursor.moveToNext());
        }
        mCursor.close();
        tmpDir = null;
    }

    class ViewHolder {
        ImageView iv;
        Button checkBox;
    }

    class FolderAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return mDirPaths.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            FolderViewHolder holder;
            if (convertView == null) {
                convertView = View.inflate(context, R.layout.list_dir_item, null);
                holder = new FolderViewHolder();
                holder.id_dir_item_image = (ImageView) convertView.findViewById(R.id.id_dir_item_image);
                holder.id_dir_item_name = (TextView) convertView.findViewById(R.id.id_dir_item_name);
                holder.id_dir_item_count = (TextView) convertView.findViewById(R.id.id_dir_item_count);
                holder.choose = (ImageView) convertView.findViewById(R.id.choose);
                convertView.setTag(holder);
            } else {
                holder = (FolderViewHolder) convertView.getTag();
            }
            ImageFloder item = mDirPaths.get(position);
            BitmapUtil.setImageViewByImagLoading(context, "file://" + item.getFirstImagePath(), holder.id_dir_item_image);
            holder.id_dir_item_count.setText(String.format("%d张", item.images.size()));
            holder.id_dir_item_name.setText(item.getName());
            holder.choose.setVisibility(currentImageFolder == item ? View.VISIBLE : View.GONE);
            return convertView;
        }
    }

    class ImageFloder {
        /**
         * 图片的文件夹路径
         */
        private String dir;

        /**
         * 第一张图片的路径
         */
        private String firstImagePath;
        /**
         * 文件夹的名称
         */
        private String name;

        public List<ImageItem> images = new ArrayList<ImageItem>();

        public String getDir() {
            return dir;
        }

        public void setDir(String dir) {
            this.dir = dir;
            int lastIndexOf = this.dir.lastIndexOf("/");
            this.name = this.dir.substring(lastIndexOf);
        }

        public String getFirstImagePath() {
            return firstImagePath;
        }

        public void setFirstImagePath(String firstImagePath) {
            this.firstImagePath = firstImagePath;
        }

        public String getName() {
            return name;
        }

    }

    class ImageItem {
        String path;
        public ImageItem(String p) {
            this.path = p;
        }
    }
}
