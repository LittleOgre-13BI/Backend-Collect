package com.utils;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.vo.MyPage;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.List;

public class PageHelper {
    public static <P, V> MyPage<V> convert(MyPage<P> myPagePO, Class<V> vClass){
        IPage<V> page = new Page<>(myPagePO.getCurrPage(), myPagePO.getPageSize(), myPagePO.getTotal());
        List<V> records = new ArrayList<>();
        for( P p : myPagePO.getList()){
            V v = null;
            try{
                v = vClass.newInstance();
            } catch (InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            }
            BeanUtils.copyProperties(p, v);
            records.add(v);
        }
        page.setRecords(records);
        return new MyPage<V>(page);
    }
}
