/**
 * The <class>System.view.system.detail.window.TaskRevisionDetailFormWindow</class> defines
 * the basic detail window for task revisions.
 *
 * @author Andrew
 */
Ext.define('System.view.system.detail.window.TaskRevisionDetailFormWindow', {
    extend: 'System.view.system.detail.window.NoteDetailFormWindow',

    requires: ['System.util.system.UserUtils'],

    ///////////////////////////////////////////////////////////////////////
    ////////                                                     Properties                                                       //////////
    /////////////////////////////////////////////////////////////////////

    xtype: 'task-revision-detail-form-window',

    /**
     * This detail form window primarily consists on the detail form panel
     */
    constructor: function (config) {
        var me = config;

        var fkFieldId = this.getInitialFormRecordEntityId(me);

        if (fkFieldId) {

            if (me.tabs === undefined) {
                me.tabs = [];
            }

            me.tabs.push(
                {
                    title: 'Hours',
                    modelName: 'TaskHours',
                    xtype: 'base-system-grid-panel',

                    // groupByField: 'taskRevision',
                    //gridSummaryEnabled: true,

                    width: 900,
                    height: 450,

                    newEntityDetailWindowDefaultValueFilters: [{
                        "operator": "=",
                        "value": me.initialFormRecord.data.id,
                        "property": "task.id"
                    }, {
                        "operator": "=",
                        "value": me.initialFormRecord.data.id,
                        "property": "taskRevision.task.id"
                    }, {
                        "operator": "=",
                        "value": System.util.system.UserUtils.getLoggedInEmployee().id,
                        "property": "employee.id"
                    }],

                    defaultSearchFilter: [
                        {"operator": "=", "value": fkFieldId, "property": "task.id"},
                        {"operator": "=", "value": fkFieldId, "property": "taskRevision.task.id"}
                    ],
                    defaultSearchFilterForced: true
                }
            );

        }

        this.callParent([config]);
    }

});