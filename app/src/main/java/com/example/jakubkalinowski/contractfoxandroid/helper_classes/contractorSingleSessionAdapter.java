package com.example.jakubkalinowski.contractfoxandroid.helper_classes;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.jakubkalinowski.contractfoxandroid.Model.ContractorDutySession;
import com.example.jakubkalinowski.contractfoxandroid.R;

import java.util.List;

/**
 * Created by MD on 11/9/2016.
 */

public class ContractorSingleSessionAdapter extends RecyclerView.Adapter<ContractorSingleSessionAdapter.MyViewHolder>{


    public List<ContractorDutySession> dutyList;



    public class MyViewHolder extends RecyclerView.ViewHolder{
        private TextView serviceTitle, serviceDate, serviceStartTime, serviceEndTime;
        private CardView serviceCardView;
        public MyViewHolder(View view){
            super(view);
            serviceTitle = (TextView) view.findViewById(R.id.service_title);
            serviceDate = (TextView) view.findViewById(R.id.service_date);
            serviceStartTime = (TextView) view.findViewById(R.id.service_from_time);
            serviceEndTime = (TextView) view.findViewById(R.id.service_end_time);
            serviceCardView = (CardView) view.findViewById(R.id.row_cardView_contract);
        }
    }


    public ContractorSingleSessionAdapter(List<ContractorDutySession> dutyList){
        this.dutyList = dutyList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.single_duty_session_row_contractor_schedule, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        ContractorDutySession service = dutyList.get(position);
        holder.serviceTitle.setText(service.getDescription());
        holder.serviceDate.setText(service.getReadableSessionDate());
        holder.serviceStartTime.setText(service.getReadableAppointmentStartTime());
        holder.serviceEndTime.setText(service.getReadableAppointmentEndTime());
    }

    @Override
    public int getItemCount() {
        return dutyList.size();
    }


}
