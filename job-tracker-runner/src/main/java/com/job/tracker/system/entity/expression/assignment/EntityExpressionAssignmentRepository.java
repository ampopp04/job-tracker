package com.job.tracker.system.entity.expression.assignment;

import com.system.db.repository.base.named.NamedEntityRepository;

import java.util.List;


/**
 * The <class>EntityExpressionAssignmentRepository</class> defines the
 * database access repository for the associated entity
 *
 * @author Andrew
 */
public interface EntityExpressionAssignmentRepository extends NamedEntityRepository<EntityExpressionAssignment> {

    /**
     * Find the EntityExpressionAssignments for
     * a specific entity via it's schema table name
     *
     * @param schemaTableName - The SchemaTable name of the associated entity
     */
    //TODO: caching
    public List<EntityExpressionAssignment> findBySchemaTableName(String schemaTableName);
}