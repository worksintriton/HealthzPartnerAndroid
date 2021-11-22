package com.triton.healthZpartners.interfaces;

import com.triton.healthZpartners.responsepojo.DropDownListResponse;

import java.util.List;

public interface PetHandledTypeCheckedListener {

    void onItemPetCheck(int position, String pethandleValue, List<DropDownListResponse.DataBean.PetHandleBean> petHandleBeanList);

    void onItemPetUnCheck(int position, String pethandleValue);

}
