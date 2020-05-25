package com.usian.utils;

import com.usian.pojo.TbItem;

import java.io.Serializable;

public class UpdateResult implements Serializable {
    private String itenCat;
    private TbItem tbItem;
    private String itemDesc;
    private String itemParamItem;

    public String getItenCat() {
        return itenCat;
    }

    public void setItenCat(String itenCat) {
        this.itenCat = itenCat;
    }

    public TbItem getTbItem() {
        return tbItem;
    }

    public void setTbItem(TbItem tbItem) {
        this.tbItem = tbItem;
    }

    public String getItemDesc() {
        return itemDesc;
    }

    public void setItemDesc(String itemDesc) {
        this.itemDesc = itemDesc;
    }

    public String getItemParamItem() {
        return itemParamItem;
    }

    public void setItemParamItem(String itemParamItem) {
        this.itemParamItem = itemParamItem;
    }
}
