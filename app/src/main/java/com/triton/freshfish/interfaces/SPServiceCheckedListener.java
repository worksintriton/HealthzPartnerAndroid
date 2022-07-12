package com.triton.freshfish.interfaces;

public interface SPServiceCheckedListener {

    void onItemSPServiceCheck(int position, String specValue,boolean isChbxChecked);

    void onItemSPServiceUnCheck(int position, String specValue,boolean isChbxChecked);

}