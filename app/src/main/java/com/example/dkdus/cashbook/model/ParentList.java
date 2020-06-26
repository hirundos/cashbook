package com.example.dkdus.cashbook.model;

import java.util.List;


public class ParentList {
    public String title;
    public List<ChildList> items;

    public ParentList(String groupTitle, List<ChildList> item){
        title = groupTitle;
        items = item;
    }
}


