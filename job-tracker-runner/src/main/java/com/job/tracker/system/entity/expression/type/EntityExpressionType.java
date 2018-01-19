package com.job.tracker.system.entity.expression.type;


import com.system.db.entity.named.NamedEntity;

/**
 * The <class>EntityExpressionType</class> defines
 * different types of expressions that are used in this system.
 * <p>
 * Ex. Field Value Setting Expression, Folder Creation Expression
 *
 * @author Andrew Popp
 */
public class EntityExpressionType extends NamedEntity<Integer> {

    ///////////////////////////////////////////////////////////////////////
    ////////                                                     Properties                                                       //////////
    //////////////////////////////////////////////////////////////////////

    public static final String NUMBER_EXPRESSION_TYPE_NAME = "Number";
    public static final String FOLDER_EXPRESSION_TYPE_NAME = "Folder";
    public static final String OBJECT_EXPRESSION_TYPE_NAME = "Object";

    ///////////////////////////////////////////////////////////////////////
    ////////                                              Default Constructor                                           //////////
    //////////////////////////////////////////////////////////////////////

    public EntityExpressionType() {
    }

    public static EntityExpressionType newInstance(String name, String description) {
        EntityExpressionType entity = new EntityExpressionType();
        entity.setName(name);
        entity.setDescription(description);
        return entity;
    }

    ///////////////////////////////////////////////////////////////////////
    ////////                                             Basic   Getter/Setters                                          //////////
    //////////////////////////////////////////////////////////////////////

}
