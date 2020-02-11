package com.naruto.treestructrue.base;

/**
 * @Purpose
 * @Author Naruto Yang
 * @CreateDate 2020/2/10 0010
 * @Note
 */
public abstract class BaseTreeBean {
    public String parentId;
    protected long id;
    protected boolean isFold = true;
    public int level;
    protected boolean hasChildren;

    public BaseTreeBean(String parentId, long id, int level, boolean hasChildren) {
        this.parentId = parentId;
        this.id = id;
        this.level = level;
        this.hasChildren = hasChildren;
    }

    public String makeChildParentId() {
        return parentId + "-" + id;
    }

    public abstract String getText();
}
