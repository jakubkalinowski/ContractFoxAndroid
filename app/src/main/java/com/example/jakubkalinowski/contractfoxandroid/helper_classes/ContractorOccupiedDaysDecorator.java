package com.example.jakubkalinowski.contractfoxandroid.helper_classes;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;
import com.prolificinteractive.materialcalendarview.spans.DotSpan;

import java.util.Collection;
import java.util.HashSet;

/**
 * Created by MD on 11/6/2016.
 */

public class ContractorOccupiedDaysDecorator implements DayViewDecorator {

    private final int color;
    private final HashSet<CalendarDay> dates;

    public ContractorOccupiedDaysDecorator(int color, Collection<CalendarDay> dates){
        this.color = color;
        this.dates = new HashSet<>(dates);
    }


    @Override
    public boolean shouldDecorate(CalendarDay day) {
        return dates.contains(day);
    }

    @Override
    public void decorate(DayViewFacade view) {
        view.addSpan(new DotSpan(20, color));
    }


}
