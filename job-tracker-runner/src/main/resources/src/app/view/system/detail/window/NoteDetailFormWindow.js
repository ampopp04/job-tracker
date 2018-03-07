/**
 * The <class>System.view.system.detail.window.DetailFormWindow</class> defines
 * the basic detail for window. This is the window that is used when drilling into entities.
 *
 * This window would be used when double clicking on a grid row or a combo box to drill into
 * the details of that entity
 *
 * @author Andrew
 */
Ext.define('System.view.system.detail.window.NoteDetailFormWindow', {
    extend: 'System.view.system.detail.window.DetailFormWindow',

    requires: [],

    ///////////////////////////////////////////////////////////////////////
    ////////                                                     Properties                                                       //////////
    /////////////////////////////////////////////////////////////////////

    xtype: 'note-detail-form-window',

    /**
     * This detail form window primarily consists on the detail form panel
     */
    constructor: function (config) {
        var me = config;

        var fkFieldTableName = this.getInitialFormRecordEntityTableName(me);
        var fkFieldId = this.getInitialFormRecordEntityId(me);

        if (fkFieldTableName && fkFieldId) {

            if (me.tabs === undefined) {
                me.tabs = [];
            }

            me.tabs.push(
                {
                    title: 'Notes',
                    modelName: 'SystemEntityNotes',
                    xtype: 'base-system-grid-panel',

                    newEntityDetailWindowDefaultValueFilters: [{
                        "operator": "=",
                        "value": fkFieldId,
                        "property": "fkEntityId"
                    }, {
                        "operator": "=",
                        "value": me.fieldSet[0].schemaTableColumn.data.id,
                        "property": "systemEntityNoteType.schemaTableColumn.id"
                    }],

                    width: 900,
                    height: 450,

                    defaultSearchFilter: [
                        {"operator": "=", "value": fkFieldId, "property": "fkEntityId"},
                        {
                            "operator": "=",
                            "value": fkFieldTableName,
                            "property": "systemEntityNoteType.schemaTableColumn.schemaTable.name"
                        }
                    ],
                    defaultSearchFilterForced: true
                }
            );

        }
        this.callParent([config]);
    }

});



