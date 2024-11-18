package com.pda.hm_texas.helper;

import com.pda.hm_texas.dto.PlcMatrailDTO;
import com.pda.hm_texas.dto.ProdCompsDTO;
import com.pda.hm_texas.dto.ProdOrderDTO;
public class ProdHelper {

    private String rout;
    private ProdOrderDTO prodOrder;
    private ProdCompsDTO prodComps;
    private PlcMatrailDTO prodPlc;

    // 싱글톤 인스턴스 변수 (Double-Checked Locking 적용)
    private static volatile ProdHelper instance = null;

    // private 생성자: 외부에서 인스턴스 생성 불가
    private ProdHelper() {}

    // 싱글톤 인스턴스 반환 메서드
    public static ProdHelper getInstance(){
        if (instance == null) {
            synchronized (ProdHelper.class) {
                if (instance == null) {
                    instance = new ProdHelper();
                }
            }
        }
        return instance;
    }

    // Getter and Setter methods for fields
    public ProdOrderDTO getProdOrder() {
        return prodOrder;
    }

    public void setProdOrder(ProdOrderDTO dto) {
        this.prodOrder = dto;
    }

    public String getRout() {
        return rout;
    }

    public void setRout(String routCode) {
        this.rout = routCode;
    }

    public ProdCompsDTO getProdComps() {
        return prodComps;
    }

    public void setProdComps(ProdCompsDTO prodComps) {
        this.prodComps = prodComps;
    }

    public PlcMatrailDTO getProdPlc() {
        return prodPlc;
    }

    public void setProdPlc(PlcMatrailDTO prodPlc) {
        this.prodPlc = prodPlc;
    }
}
