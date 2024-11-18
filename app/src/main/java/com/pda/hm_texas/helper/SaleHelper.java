package com.pda.hm_texas.helper;

import com.pda.hm_texas.dto.SaleOrderDTO;

public class SaleHelper {

    private SaleOrderDTO order;
    private static volatile SaleHelper instance = null;

    private SaleHelper() {}

    // 싱글톤 인스턴스 반환 메서드
    public static SaleHelper getInstance(){
        if (instance == null) {
            synchronized (SaleHelper.class) {
                if (instance == null) {
                    instance = new SaleHelper();
                }
            }
        }
        return instance;
    }

    public SaleOrderDTO getOrder() {
        return order;
    }

    public void setOrder(SaleOrderDTO order) {
        this.order = order;
    }
}
