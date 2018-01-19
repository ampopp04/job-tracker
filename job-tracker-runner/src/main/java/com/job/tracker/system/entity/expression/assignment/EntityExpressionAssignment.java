package com.job.tracker.system.entity.expression.assignment;


import com.job.tracker.system.entity.expression.EntityExpression;
import com.job.tracker.system.entity.expression.operation.EntityExpressionOperation;
import com.system.db.entity.named.NamedEntity;
import com.system.db.schema.table.SchemaTable;
import com.system.db.schema.table.column.SchemaTableColumn;

import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

/**
 * The <class>EntityExpressionAssignment</class> defines
 * an assignment between a entity expression, an entity (or specific entity column/field), and an operation.
 * <p>
 * This allows for the ability to assign a specific entity expression operation to be
 * executed against any specific entity or to perform automatic field calculations.
 * <p>
 * Ex.
 *
 * @author Andrew Popp
 */
@Table(uniqueConstraints = {
        @UniqueConstraint(columnNames = {"name"}),
        @UniqueConstraint(columnNames = {"schema_table_id", "entity_expression_id", "entity_expression_assignment_operation_id"})
})
public class EntityExpressionAssignment extends NamedEntity<Integer> {

    ///////////////////////////////////////////////////////////////////////
    ////////                                                     Properties                                                       //////////
    //////////////////////////////////////////////////////////////////////

    /**
     * The entity this assignment relates to.
     * Ex. Job
     * Ex. Project
     * Ex. Task
     */
    @ManyToOne(optional = false)
    @JoinColumn(name = "schema_table_id")
    private SchemaTable schemaTable;

    /**
     * The entitys column this assignment relates to.
     * Ex. Job Number column on Job table
     * Ex. Project Number column on Project table
     */
    @ManyToOne
    @JoinColumn(name = "schema_table_column_id")
    private SchemaTableColumn schemaTableColumn;

    /**
     * The entity expression we will associate with the specific entity/field defined by the
     * schemaTable property and/or schemaTableColumn(field).
     */
    @ManyToOne(optional = false)
    @JoinColumn(name = "entity_expression_id")
    private EntityExpression entityExpression;

    /**
     * An observer will watch for the following operation.
     * If it occurs against the entity, specified above by the schemaTable,
     * we will then execute that operation with the provided expression.
     */
    @ManyToOne(optional = false)
    @JoinColumn(name = "entity_expression_assignment_operation_id")
    private EntityExpressionOperation entityExpressionOperation;

    ///////////////////////////////////////////////////////////////////////
    ////////                                              Default Constructor                                           //////////
    //////////////////////////////////////////////////////////////////////

    public EntityExpressionAssignment() {
    }

    private EntityExpressionAssignment(String name, String description, SchemaTable schemaTable, SchemaTableColumn schemaTableColumn, EntityExpression entityExpression, EntityExpressionOperation entityExpressionOperation) {
        setName(name);
        setDescription(description);
        this.schemaTable = schemaTable;
        this.schemaTableColumn = schemaTableColumn;
        this.entityExpression = entityExpression;
        this.entityExpressionOperation = entityExpressionOperation;
    }

    public static EntityExpressionAssignment newInstance(String name, String description, SchemaTable schemaTable, SchemaTableColumn schemaTableColumn, EntityExpression entityExpression, EntityExpressionOperation entityExpressionOperation) {
        return new EntityExpressionAssignment(name, description, schemaTable, schemaTableColumn, entityExpression, entityExpressionOperation);
    }

    ///////////////////////////////////////////////////////////////////////
    ////////                                             Basic   Getter/Setters                                          //////////
    //////////////////////////////////////////////////////////////////////

    public SchemaTable getSchemaTable() {
        return schemaTable;
    }

    public void setSchemaTable(SchemaTable schemaTable) {
        this.schemaTable = schemaTable;
    }

    public EntityExpression getEntityExpression() {
        return entityExpression;
    }

    public void setEntityExpression(EntityExpression entityExpression) {
        this.entityExpression = entityExpression;
    }

    public EntityExpressionOperation getEntityExpressionOperation() {
        return entityExpressionOperation;
    }

    public void setEntityExpressionOperation(EntityExpressionOperation entityExpressionOperation) {
        this.entityExpressionOperation = entityExpressionOperation;
    }

    public SchemaTableColumn getSchemaTableColumn() {
        return schemaTableColumn;
    }

    public void setSchemaTableColumn(SchemaTableColumn schemaTableColumn) {
        this.schemaTableColumn = schemaTableColumn;
    }
}
