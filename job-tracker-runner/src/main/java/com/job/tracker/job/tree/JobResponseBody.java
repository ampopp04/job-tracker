package com.job.tracker.job.tree;

import java.util.List;

public class JobResponseBody {
    private String text = ".";
    private List<TreeJobDTO> children;
    private Boolean expanded = false;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public List<TreeJobDTO> getChildren() {
        return children;
    }

    public void setChildren(List<TreeJobDTO> children) {
        this.children = children;
    }

    public Boolean getExpanded() {
        return expanded;
    }

    public void setExpanded(Boolean expanded) {
        this.expanded = expanded;
    }
}
