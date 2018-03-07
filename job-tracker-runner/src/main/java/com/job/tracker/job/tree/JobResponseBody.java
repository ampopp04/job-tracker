package com.job.tracker.job.tree;

import java.util.List;

/**
 * The <class>JobResponseBody</class> defines a wrapper
 * object for housing the tree like structure of our dashboard
 * tree grid view response data.
 *
 * @author Andrew Popp
 */
public class JobResponseBody {

    ///////////////////////////////////////////////////////////////////////
    ////////                                                     Properties                                                       //////////
    //////////////////////////////////////////////////////////////////////

    private String text = ".";
    private List<TreeJobDTO> children;
    private Boolean expanded = false;

    ///////////////////////////////////////////////////////////////////////
    ////////                                             Basic   Getter/Setters                                          //////////
    //////////////////////////////////////////////////////////////////////

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
