package com.jkm.base.common.entity;

import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import lombok.Data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by yulong.zhang on 2016/11/15.
 */
@Data
public class PageModel<T> {
    /**
     * 总记录数
     */
    private long count;
    /**
     * 页码
     */
    private int pageNO;
    /**
     * 总页数
     */
    private int totalPage;
    /**
     * 是否有下一页
     */
    private boolean hasNextPage;
    /**
     * 每页大小
     */
    private int pageSize;
    /**
     * 单页记录
     */
    private List<T> records = new ArrayList<>();
    /**
     * 起始index
     */
    private int firstIndex;

    /**
     * constructor
     */
    public PageModel() {
        super();
    }

    /**
     * constructor
     *
     * @param pageNO
     * @param pageSize
     */
    public PageModel(final int pageNO, final int pageSize) {
        super();
        Preconditions.checkArgument(pageNO > 0 && pageSize > 0);
        this.pageNO = pageNO;
        this.pageSize = pageSize;
        this.records = Collections.emptyList();
        this.firstIndex = (pageNO - 1) * pageSize;
    }

    /**
     * 设置总记录数
     *
     * @param count
     */
    public void setCount(final long count) {
        this.count = count;
        this.totalPage = (int) ((count + pageSize - 1) / pageSize);
        this.hasNextPage = pageNO < totalPage;
    }

    /**
     * deep copy
     *
     * @param function
     * @param <K>
     * @return
     */
    public <K> PageModel<K> copy(final Function<T, K> function) {
        final PageModel<K> pageModel = new PageModel<>(this.pageNO, this.pageSize);
        pageModel.setCount(this.count);
        pageModel.setRecords(Lists.transform(this.records, function));
        return pageModel;
    }
}
