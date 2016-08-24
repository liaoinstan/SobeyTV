package com.sobey.tvcust.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.bartoszlipinski.recyclerviewheader2.RecyclerViewHeader;
import com.bumptech.glide.Glide;
import com.sobey.common.entity.Images;
import com.sobey.common.utils.StrUtils;
import com.sobey.common.view.BannerView;
import com.sobey.tvcust.R;
import com.sobey.tvcust.common.AppData;
import com.sobey.tvcust.common.CommonNet;
import com.sobey.tvcust.common.DividerItemDecoration;
import com.sobey.tvcust.common.LoadingViewUtil;
import com.sobey.common.entity.Article;
import com.sobey.tvcust.entity.ArticlePojo;
import com.sobey.tvcust.entity.BannerPojo;
import com.sobey.tvcust.ui.activity.InfoDetailActivity;
import com.sobey.tvcust.interfaces.OnRecycleItemClickListener;
import com.sobey.tvcust.ui.activity.WebActivity;
import com.sobey.tvcust.ui.adapter.RecycleAdapterInfoQuan;
import com.sobey.tvcust.utils.AppHelper;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/6/2 0002.
 */
public class InfoQuanFragment extends BaseFragment implements OnRecycleItemClickListener, BannerView.OnBannerClickListener {

    private int position;
    private View rootView;
    private ViewGroup showingroup;
    private View showin;

    private BannerView banner;
    private RecyclerView recyclerView;
    private RecycleAdapterInfoQuan adapter;
    private List<Article> results = new ArrayList<>();
    private List<Images> images = new ArrayList<>();
    private int page;
    private final int PAGE_COUNT = 5;

    private SwipeRefreshLayout swipe;

    private Callback.Cancelable cancelable;
    private Callback.Cancelable cancelablemore;

    public static Fragment newInstance(int position) {
        InfoQuanFragment f = new InfoQuanFragment();
        Bundle b = new Bundle();
        b.putInt("position", position);
        f.setArguments(b);
        return f;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.position = getArguments().getInt("position");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_info_quan, container, false);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initBase();
        initView();
        initData(true);
        initCtrl();
    }

    private void initBase() {

    }

    private void initView() {
        showingroup = (ViewGroup) getView().findViewById(R.id.showingroup);
        recyclerView = (RecyclerView) getView().findViewById(R.id.recycle_info_quan);
        swipe = (SwipeRefreshLayout) getView().findViewById(R.id.swipe_info_quan);
        banner = (BannerView) getView().findViewById(R.id.banner);

        banner.setOnLoadImgListener(new BannerView.OnLoadImgListener() {
            @Override
            public void onloadImg(ImageView imageView, String imgurl, int defaultSrc) {
                Glide.with(getActivity()).load(AppHelper.getRealImgPath(imgurl)).placeholder(defaultSrc).crossFade().into(imageView);
            }
        });
        //事先隐藏banner，加载数据后会显示出来
        //banner.setVisibility(View.GONE);
    }

    private void initData(boolean isFirst) {
        if (cancelable != null) {
            cancelable.cancel();
        }
        if (cancelablemore != null) {
            cancelablemore.cancel();
        }
        netBanner();
        freshDate(isFirst);
    }

    private void freshDate(final boolean isFirst) {
        final RequestParams params = new RequestParams(AppData.Url.getNewsList);
        params.addHeader("token", AppData.App.getToken());
        params.addBodyParameter("pageNO", 1 + "");
        params.addBodyParameter("pageSize", PAGE_COUNT + "");
        params.addBodyParameter("typeId", position + 1 + "");
        cancelable = CommonNet.samplepost(params, ArticlePojo.class, new CommonNet.SampleNetHander() {
            @Override
            public void netGo(int code, Object pojo, String text, Object obj) {
                if (pojo == null) netSetError(code, "错误：返回数据为空");
                else {
                    ArticlePojo articlePojo = (ArticlePojo) pojo;
                    List<Article> articles = articlePojo.getDataList();
                    //有数据才添加，否则显示lack信息
                    if (articles != null && articles.size() != 0) {
                        List<Article> results = adapter.getResults();
                        results.clear();
                        results.addAll(articles);
                        freshCtrl();
                        page = 1;
                        if (isFirst) {
                            LoadingViewUtil.showout(showingroup, showin);
                        } else {
                            LoadingViewUtil.showout(showingroup, showin);
                            swipe.setRefreshing(false);
                        }
                    } else {
                        swipe.setRefreshing(false);
//                        showin = LoadingViewUtil.showin(showingroup, R.layout.layout_lack, showin, new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//                                initData(true);
//                            }
//                        });
                    }
                }
            }

            @Override
            public void netSetError(int code, String text) {
                if (isFirst) {
                    Toast.makeText(getActivity(), text, Toast.LENGTH_SHORT).show();
                    showin = LoadingViewUtil.showin(showingroup, R.layout.layout_fail, showin, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            initData(true);
                        }
                    });
                } else {
                    swipe.setRefreshing(false);
                }
            }

            @Override
            public void netStart(int code) {
                if (isFirst) {
                    showin = LoadingViewUtil.showin(showingroup, R.layout.layout_loading, showin);
                }
            }
        });
    }

    private boolean isloadmore = false;
    private long noTime;

    private void loadMoreData() {
        if (isloadmore) return;
        if ((System.currentTimeMillis() - noTime) < 10000) return;
        final RequestParams params = new RequestParams(AppData.Url.getNewsList);
        params.addHeader("token", AppData.App.getToken());
        params.addBodyParameter("pageNO", page + 1 + "");
        params.addBodyParameter("pageSize", PAGE_COUNT + "");
        params.addBodyParameter("typeId", position + 1 + "");
        cancelablemore = CommonNet.samplepost(params, ArticlePojo.class, new CommonNet.SampleNetHander() {
            @Override
            public void netGo(int code, Object pojo, String text, Object obj) {
                if (pojo == null) netSetError(code, text);
                else {
                    ArticlePojo articlePojo = (ArticlePojo) pojo;
                    List<Article> articles = articlePojo.getDataList();
                    if (articles != null && articles.size() != 0) {
                        List<Article> results = adapter.getResults();
                        results.addAll(articles);
                        Toast.makeText(getActivity(), "加载完成", Toast.LENGTH_SHORT).show();
                        freshCtrl();
                        page++;
                    } else {
                        Snackbar.make(showingroup, "没有更多的数据了", Snackbar.LENGTH_SHORT).show();
                        noTime = System.currentTimeMillis();
                    }
                }
            }

            @Override
            public void netSetError(int code, String text) {
                Toast.makeText(getActivity(), text, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void netStart(int code) {
                isloadmore = true;
            }

            @Override
            public void netEnd(int code) {
                isloadmore = false;
                swipe.setRefreshing(false);
            }
        });
    }

    private void netBanner() {
        RequestParams params = new RequestParams(AppData.Url.getBanners);
        params.addHeader("token", AppData.App.getToken());
        CommonNet.samplepost(params, BannerPojo.class, new CommonNet.SampleNetHander() {
            @Override
            public void netGo(int code, Object pojo, String text, Object obj) {
                if (pojo == null) netSetError(code, "错误：返回数据为空");
                else {
                    BannerPojo bannerPojo = (BannerPojo) pojo;
                    List<Images> imagelist = bannerPojo.getDataList();
                    images.clear();
                    images.addAll(imagelist);
                    freshBanner();
                }
            }

            @Override
            public void netSetError(int code, String text) {
            }
        });
    }

    private void initCtrl() {
        adapter = new RecycleAdapterInfoQuan(getActivity(), results);
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext(), LinearLayoutManager.VERTICAL, false));
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity()));
        recyclerView.setAdapter(adapter);
        RecyclerViewHeader header = (RecyclerViewHeader) getView().findViewById(R.id.header);
        header.attachTo(recyclerView);
        recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            //用来标记是否正在向最后一个滑动，既是否向右滑动或向下滑动
            boolean isSlidingToLast = false;

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                LinearLayoutManager manager = (LinearLayoutManager) recyclerView.getLayoutManager();
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    boolean b = !ViewCompat.canScrollVertically(recyclerView, 1);
                    if (b) {
                        loadMoreData();
                    }
                }
            }
        });
        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                initData(false);
            }
        });
        adapter.setOnItemClickListener(this);
    }

    private void freshCtrl() {
        adapter.notifyDataSetChanged();
    }

    private void freshBanner() {
        banner.showTitle(false);
        banner.setDatas(images);
        banner.setOnBannerClickListener(this);
    }

    @Override
    public void onItemClick(RecyclerView.ViewHolder viewHolder) {
        Article article = adapter.getResults().get(viewHolder.getLayoutPosition());

        int isUrl = article.getIsUrl();
        String linkUrl;
        if (isUrl == 1) {
            linkUrl = article.getLinkUrl();
            Intent intent = new Intent(getActivity(), WebActivity.class);
            intent.putExtra("url", linkUrl);
            intent.putExtra("title", "资讯详情");
            startActivity(intent);
        } else {
            linkUrl = AppData.Url.newsDetail + "?newsId=" + article.getId() + "&token=" + AppData.App.getToken();
            Intent intent = new Intent(getActivity(), InfoDetailActivity.class);
            intent.putExtra("url", linkUrl);
            intent.putExtra("newsId", article.getId());
            intent.putExtra("article", article);
            startActivity(intent);
        }
    }

    @Override
    public void onBannerClick(int position) {
        Images image = this.images.get(position);
        if (image.getIsInside() == 0) {
            if (StrUtils.isUrl(image.getUrl())) {
                Intent intent = new Intent(getActivity(), WebActivity.class);
                intent.putExtra("title", "资讯");
                intent.putExtra("url", image.getUrl());//https://github.com    //http://cn.bing.com
                getActivity().startActivity(intent);
            }
        } else {
            //内部文章
            String linkUrl = AppData.Url.newsDetail + "?newsId=" + image.getNewsId() + "&token=" + AppData.App.getToken();
            Intent intent = new Intent(getActivity(), InfoDetailActivity.class);
            intent.putExtra("url", linkUrl);
            intent.putExtra("newsId", image.getNewsId());
            intent.putExtra("article", image.getArticle());
            startActivity(intent);
        }


    }
}
