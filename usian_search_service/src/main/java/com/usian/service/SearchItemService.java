package com.usian.service;

import com.usian.pojo.SearchItem;

import java.io.IOException;
import java.util.List;

public interface SearchItemService {
    public boolean importAll();

    List<SearchItem> selectByQ(String q, Long page, Integer pagesize);

    int insertDocument(String msg) throws IOException;
}
