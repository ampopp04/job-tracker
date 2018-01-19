package com.job.tracker.system.entity.expression;


import com.job.tracker.system.entity.expression.type.EntityExpressionType;
import com.system.db.entity.named.NamedEntity;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.util.Collection;
import java.util.List;

import static com.system.util.collection.CollectionUtils.iterable;
import static com.system.util.collection.CollectionUtils.newList;
import static javax.persistence.FetchType.EAGER;

/**
 * The <class>EntityExpression</class> defines
 * an abstract expression.
 * <p>
 * Meaning it could be anything. This expression also supports
 * freemarker template expressions and will be applied to
 * objects later through the EntityExpressionAssignment table.
 *
 * @author Andrew Popp
 */
public class EntityExpression extends NamedEntity<Integer> {

    ///////////////////////////////////////////////////////////////////////
    ////////                                                     Properties                                                       //////////
    //////////////////////////////////////////////////////////////////////

    @ManyToOne
    @JoinColumn(name = "entity_expression_type_id", nullable = false)
    private EntityExpressionType entityExpressionType;

    /**
     * Normal entity expression or one that contains
     * a freemarker expression
     */
    @Column(nullable = false, length = 500)
    private String entityExpression;

    /**
     * This path is expected to resolve to an actual EntityExpression object.
     * This object will be used and it's children resolved as a list.
     */
    @Column(length = 500)
    private String childEntityExpressionPath;

    /**
     * The parent entity expression that can be used to build more complex expression
     */
    @ManyToOne(fetch = EAGER)
    @JoinColumn(name = "parent_entity_expression_id")
    private EntityExpression parentEntityExpression;

    /**
     * The collection of entity expression children who have this entity expression as their parentEntityExpression
     */
    @LazyCollection(LazyCollectionOption.TRUE)
    @OneToMany(mappedBy = "parentEntityExpression")
    private Collection<EntityExpression> entityExpressionChildCollection;

    ///////////////////////////////////////////////////////////////////////
    ////////                                              Default Constructor                                           //////////
    //////////////////////////////////////////////////////////////////////

    public EntityExpression() {
    }

    public static EntityExpression newInstance(String name, String description, String entityExpression, EntityExpression parentEntityExpression, EntityExpressionType entityExpressionType) {
        return newInstance(name, description, entityExpression, null, parentEntityExpression, entityExpressionType);
    }

    public static EntityExpression newInstance(String name, String description, String entityExpression, String childEntityExpressionString, EntityExpression parentEntityExpression, EntityExpressionType entityExpressionType) {
        EntityExpression entity = new EntityExpression();

        entity.setName(name);
        entity.setDescription(description);
        entity.setEntityExpression(entityExpression);
        entity.setChildEntityExpressionPath(childEntityExpressionString);
        entity.setParentEntityExpression(parentEntityExpression);
        entity.setEntityExpressionType(entityExpressionType);

        return entity;
    }

    public String getEntityExpressionExpanded() {
        String result = this.getEntityExpression();
        EntityExpression entityExpression = this.getParentEntityExpression();

        int depth = 0;

        while (entityExpression != null) {

            result = entityExpression.getEntityExpression() + result;
            entityExpression = entityExpression.getParentEntityExpression();

            ++depth;
            if (depth > 20) {
                break;
            }
        }

        return result;
    }

    public List<String> getEntityExpressionChildExpressionExpandedList() {
        List<String> expandedExpressionWithChildrenList = newList();
        String expandedExpression = this.getEntityExpressionExpanded();

        for (EntityExpression childExpression : iterable(getEntityExpressionChildCollection())) {
            for (String expandedChildExpression : iterable(childExpression.getEntityExpressionExpandedWithChildrenExpandedList())) {
                expandedExpressionWithChildrenList.add(expandedExpression + expandedChildExpression);
            }
        }

        return expandedExpressionWithChildrenList;
    }

    public List<String> getEntityExpressionExpandedWithChildrenExpandedList() {
        List<String> expandedExpressionWithChildrenList = newList();
        expandedExpressionWithChildrenList.add(getEntityExpressionExpanded());
        expandedExpressionWithChildrenList.addAll(getEntityExpressionChildExpressionExpandedList());
        return expandedExpressionWithChildrenList;
    }

    ///////////////////////////////////////////////////////////////////////
    ////////                                             Basic   Getter/Setters                                          //////////
    //////////////////////////////////////////////////////////////////////

    public EntityExpressionType getEntityExpressionType() {
        return entityExpressionType;
    }

    public void setEntityExpressionType(EntityExpressionType entityExpressionType) {
        this.entityExpressionType = entityExpressionType;
    }

    public String getEntityExpression() {
        return entityExpression;
    }

    public void setEntityExpression(String entityExpression) {
        this.entityExpression = entityExpression;
    }

    public String getChildEntityExpressionPath() {
        return childEntityExpressionPath;
    }

    public void setChildEntityExpressionPath(String childEntityExpressionPath) {
        this.childEntityExpressionPath = childEntityExpressionPath;
    }

    public Collection<EntityExpression> getEntityExpressionChildCollection() {
        return entityExpressionChildCollection;
    }

    public void setEntityExpressionChildCollection(Collection<EntityExpression> entityExpressionChildCollection) {
        this.entityExpressionChildCollection = entityExpressionChildCollection;
    }

    public EntityExpression getParentEntityExpression() {
        return parentEntityExpression;
    }

    public void setParentEntityExpression(EntityExpression parentEntityExpression) {
        this.parentEntityExpression = parentEntityExpression;
    }
}
