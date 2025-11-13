package com.pda.hm_texas.impl;

import com.pda.hm_texas.dto.AppVersionDTO;
import com.pda.hm_texas.dto.DbResultVO;
import com.pda.hm_texas.dto.EnterOrderDTO;
import com.pda.hm_texas.dto.FactoryDTO;
import com.pda.hm_texas.dto.LocationDTO;
import com.pda.hm_texas.dto.PickingDTO;
import com.pda.hm_texas.dto.PlcMatrailDTO;
import com.pda.hm_texas.dto.ProdCompsDTO;
import com.pda.hm_texas.dto.ProdOrderDTO;
import com.pda.hm_texas.dto.RackDTO;
import com.pda.hm_texas.dto.RackMoveDTO;
import com.pda.hm_texas.dto.ReleaseDTO;
import com.pda.hm_texas.dto.SaleOrderDTO;
import com.pda.hm_texas.dto.StockItemDTO;
import com.pda.hm_texas.dto.TransBarcodeItemDTO;
import com.pda.hm_texas.dto.UserDTO;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApiService {

    @GET("/pda/getAPK")
    Call<ResponseBody> downloadFileWithDynamicUrlSync(@Query("pgtype") String pgtype);

    @GET("/pda/getver")
    Call<AppVersionDTO> getVersion(@Query("pgtype") String pgtype);

    @POST("/pda/login")
    Call<UserDTO> Login(@Query("id") String id, @Query("pw") String pw);

    @POST("/pda/getloc")
    Call<List<LocationDTO>> getLocation();

    @POST("/pda/prod/prodorder")
    Call<List<ProdOrderDTO>> getProdOrder(@Query("from") String from, @Query("to") String to, @Query("rout") String rout);

    @POST("/pda/prod/recipe")
    Call<List<ProdCompsDTO>> getPdaProdRecipe(@Query("ordno") String ordNo, @Query("lineno") int line);

    @POST("/pda/prod/plc")
    Call<List<PlcMatrailDTO>> getPlcInfo(@Query("item") String item);

    @POST("/pda/prod/scbarcode")
    Call<List<StockItemDTO>> getStockItemInfo(@Query("barcode") String barcode, @Query("loc") String loc);

    @POST("/pda/prod/release")
    Call<DbResultVO> addWorkRelease(@Body List<ReleaseDTO> items, @Query("user") String user, @Query("plcstep") int plcstep, @Query("plcid") long plcid);


    @POST("/pda/enter/order")
    Call<List<EnterOrderDTO>> getEnterOrder(@Query("from") String from, @Query("to") String to, @Query("vender") String vender);

    @POST("/pda/sale/order")
    Call<List<SaleOrderDTO>> getSaleOrder(@Query("from") String from, @Query("to") String to, @Query("vender") String vender);

    @POST("/pda/sale/stockinlot")
    Call<List<StockItemDTO>> getSaleLotInStock(@Query("ordno") String ordno, @Query("lineno") String lineno, @Query("item") String item, @Query("loc") String loc);

    @POST("/pda/sale/finditem")
    Call<List<StockItemDTO>> getSaleFindBarcode(@Query("item") String item, @Query("barcode") String barcode, @Query("loc") String loc);

    @POST("/pda/sale/picking")
    Call<DbResultVO> setPicking(@Body PickingDTO pickings, @Query("user") String user);

    @POST("/pda/stock/getitem")
    Call<List<TransBarcodeItemDTO>> getCustStockItemInfo(@Query("barcode") String barcode, @Query("loc") String loc);

    @POST("/pda/stock/set")
    Call<DbResultVO> setCustBarcode(@Body List<TransBarcodeItemDTO> items);

    @POST("/pda/stock/finditem")
    Call<List<StockItemDTO>> getindBarcode(@Query("item") String item, @Query("barcode") String barcode, @Query("loc") String loc);

    @POST("/pda/stock/findbcr")
    Call<List<StockItemDTO>> getBarcode(@Query("item") String item, @Query("barcode") String barcode, @Query("loc") String loc);

    @POST("/pda/stock/moveitem")
    Call<DbResultVO> MoveItemStock(@Body List<StockItemDTO> items, @Query("loc") String loc, @Query("user") String user, @Query("nowdt") String nowdt);

    @GET("/pda/stock/racklist")
    Call<List<RackDTO>> getRackList(@Query("loc") String loc);

    @GET("/pda/stock/findrack")
    Call<RackDTO> getRack(@Query("loc") String loc, @Query("rc") String rackCode);

    @POST("/pda/stock/moverack")
    Call<DbResultVO> RackMove(@Body List<RackMoveDTO> items);

    @GET("/pda/getfactory")
    Call<List<FactoryDTO>> getFactory(@Query("fcode") String fcode);

}
