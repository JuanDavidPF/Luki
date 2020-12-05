package com.example.luki.Bills;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.luki.Gallery.ThumbnailView;
import com.example.luki.MainCustomer;
import com.example.luki.R;
import com.example.luki.model.Bill;

import java.util.ArrayList;

public class BillsAdapter extends RecyclerView.Adapter<BillView> {

    private ArrayList<Bill> bills;

    public BillsAdapter() {

        bills = new ArrayList<>();
    }//closes BillsAdapter class

    public void addBill(Bill bill) {

        bills.add(bill);
        this.notifyDataSetChanged();
    }//closes addBill class

    public ArrayList<Bill> getBills() {
        return bills;
    }

    @NonNull
    @Override
    public BillView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View rowXml = inflater.inflate(R.layout.bill_row, parent,false);
        ConstraintLayout row = (ConstraintLayout) rowXml;
        BillView billView = new BillView(row);
        return billView;
    }

    @Override
    public void onBindViewHolder(@NonNull BillView holder, int position) {

        Bill bill = bills.get(position);

        holder.getBillName().setText(bill.getTransferName());
        holder.getBillDate().setText(bill.getTransferDate());
        holder.getBillPrice().setText("-$" + bill.getTranferPrice());
        holder.setId(bill.getId());
    }

    @Override
    public int getItemCount() {
        return bills.size();
    }

}//closes class BillsAdapter
