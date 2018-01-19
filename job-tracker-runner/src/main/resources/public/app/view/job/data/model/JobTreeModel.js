/**
 * The <class>System.view.job.data.model.JobTreeModel</class> defines the
 * model used to load jobs and tasks combined in a hierarchical tree graph model.
 *
 * A job has a list of tasks. This will load the job and the tasks as children of the job in
 * a single request.
 *
 * @author Andrew
 */
Ext.define('System.view.job.data.model.JobTreeModel', {
    extend: 'System.model.base.tree.BaseTreeModel',

    ///////////////////////////////////////////////////////////////////////
    ////////                                                     Properties                                                       //////////
    /////////////////////////////////////////////////////////////////////

    entityName: 'JobTreeModel',

    clientIdProperty: 'jobNumber',
    idProperty: 'jobNumber',

    ///////////////////////////////////////////////////////////////////////
    ////////                                                          Columns                                                        //////////
    /////////////////////////////////////////////////////////////////////

    fields: [{
        name: 'jobNumber',
        type: 'string'
    }, {
        name: 'branchName',
        type: 'string'
    }, {
        name: 'entityId',
        type: 'string'
    }, {
        name: 'taskNumber',
        type: 'number',
        allowNull: true
    }, {
        name: 'taskTypeName',
        type: 'string'
    }, {
        name: 'date',
        type: 'date',
        dateFormat: 'm-d-Y'
    }, {
        name: 'businessClientName',
        type: 'string'
    }, {
        name: 'fileFolderPath',
        type: 'string'
    }, {
        name: 'projectName',
        type: 'string'
    }, {
        name: 'description',
        type: 'string'
    }, {
        name: 'assignedTo',
        type: 'string'
    }, {
        name: 'revisionNumber',
        type: 'number',
        allowNull: true
    }, {
        name: 'status',
        type: 'string'
    }]
});