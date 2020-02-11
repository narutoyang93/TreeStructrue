package com.naruto.treestructrue;

import com.naruto.treestructrue.base.BaseTreeBean;

/**
 * @Purpose
 * @Author Naruto Yang
 * @CreateDate 2020/2/11 0011
 * @Note
 */
public class TestBean extends BaseTreeBean {
    public String name;

    public TestBean(String parentId, long id, int level, boolean hasChildren, String name) {
        super(parentId, id, level, hasChildren);
        this.name = name;
    }

    @Override
    public String getText() {
        return name;
    }
}
