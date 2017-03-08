package com.rashata.jamie.spend.manager.http;


import com.rashata.jamie.spend.manager.http.dao.ListCurrencyDao;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by jjamierashata on 9/15/16 AD.
 */
public interface ApiService {

    @GET("latest")
    Call<ListCurrencyDao> listCurrency(@Query("base") String base);


}
