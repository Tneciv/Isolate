package com.tneciv.zhihudaily.theme.presenter;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.tneciv.zhihudaily.api.ZhihuApi;
import com.tneciv.zhihudaily.constants.ErrorEntity;
import com.tneciv.zhihudaily.theme.model.ThemeEntity;
import com.tneciv.zhihudaily.theme.model.ThemeResultEntity;
import com.tneciv.zhihudaily.utils.CacheUtil;
import com.tneciv.zhihudaily.utils.OkhttpUtil;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Tneciv
 * on 2016-06-24 17:03 .
 */
public class ThemePresenterCompl implements IThemePresenter {

    private Context mContext;

    public ThemePresenterCompl(Context context) {
        this.mContext = context.getApplicationContext();
    }

    @Override
    public void handleRequestUrl(final String url) {
        Request request = new Request.Builder().url(url).get().build();
        OkhttpUtil.getInstance().newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                ErrorEntity entity = new ErrorEntity("网络连接异常", "net error");
                EventBus.getDefault().post(entity);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String string = response.body().string();
                new CacheUtil(mContext).cacheFiles(url, string);
                handleResponse(string, url);
            }
        });
    }

    private void handleResponse(String response, String url) {
        Gson gson = new Gson();
        if (url.equals(ZhihuApi.THEME_LIST)) {
            Type type = new TypeToken<List<ThemeEntity>>() {
            }.getType();
            List<ThemeEntity> themeEntities = new ArrayList<>();
            try {
                JsonElement jsonElement = new JsonParser().parse(response).getAsJsonObject().get("data");
                themeEntities = gson.fromJson(jsonElement, type);
                ThemeResultEntity.ThemeList list = new ThemeResultEntity.ThemeList(themeEntities);
                EventBus.getDefault().post(list);
            } catch (JsonSyntaxException | IllegalStateException | NullPointerException e) {
                e.printStackTrace();
                ErrorEntity entity = new ErrorEntity("服务器返回数据异常", "server error");
                EventBus.getDefault().post(entity);
            }

        }

    }
}
