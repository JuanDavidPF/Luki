package com.example.luki.Bills;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.luki.R;

public class BillView  extends RecyclerView.ViewHolder {


    private ConstraintLayout root;
    private TextView billName;
    private TextView billDate;
    private TextView billPrice;
    private String id;

    public BillView(ConstraintLayout root) {
        super(root);
        this.root=root;
        billName= root.findViewById(R.id.bill_name_tv);
        billDate= root.findViewById(R.id.bill_date_tv);
        billPrice= root.findViewById(R.id.bill_price_tv);
    }

    public void setId(String id) {
        this.id = id;
    }

    public ConstraintLayout getRoot() {
        return root;
    }

    public void setRoot(ConstraintLayout root) {
        this.root = root;
    }

    public TextView getBillName() {
        return billName;
    }

    public void setBillName(TextView billName) {
        this.billName = billName;
    }

    public TextView getBillDate() {
        return billDate;
    }

    public void setBillDate(TextView billDate) {
        this.billDate = billDate;
    }

    public TextView getBillPrice() {
        return billPrice;
    }

    public void setBillPrice(TextView billPrice) {
        this.billPrice = billPrice;
    }
}//closes BillView class
