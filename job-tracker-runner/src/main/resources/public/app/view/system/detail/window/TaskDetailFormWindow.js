/**
 * The <class>System.view.system.detail.window.TaskDetailFormWindow</class> defines
 * the basic detail window for tasks.
 *
 * @author Andrew
 */
Ext.define('System.view.system.detail.window.TaskDetailFormWindow', {
    extend: 'System.view.system.detail.window.TaskRevisionDetailFormWindow',

    requires: [],

    ///////////////////////////////////////////////////////////////////////
    ////////                                                     Properties                                                       //////////
    /////////////////////////////////////////////////////////////////////

    xtype: 'task-detail-form-window',

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
                    title: 'Revisions',
                    modelName: 'TaskRevisions',
                    xtype: 'base-system-grid-panel',

                    width: 900,
                    height: 450,

                    newEntityDetailWindowDefaultValueFilters: [{
                        "operator": "=",
                        "value": me.initialFormRecord.data.id,
                        "property": "task.id"
                    }],

                    defaultSearchFilter: [
                        {"operator": "=", "value": fkFieldId, "property": "task.id"}
                    ],
                    defaultSearchFilterForced: true
                }
            );

        }

        this.callParent([config]);
    }
});



