/**
 * The <class>System.view.report.management.ReportManagementWindow</class> defines
 * a simplified view of the report management section of this application. This
 * focuses primarily on report documents generated to the UI.
 *
 * @author Andrew
 */
Ext.define('System.view.report.management.ReportManagementWindow', {
    extend: 'System.view.component.window.tab.grid.TabGridSystemWindow',

    title: 'Report Setup',
    width: 1200,

    constructor: function (config) {
        var me = config;

        me.tabs = [
            {
                title: 'Report Template',
                modelName: 'SystemExportTemplates',
                hideTemplateUploadTableField: false,
                deleteEntityAllowed: false
            },
            {
                title: 'Generated Reports',
                modelName: 'SystemExportGeneratorContents',
                deleteEntityAllowed: false
            },
            {
                title: 'Report Types',
                modelName: 'SystemExportTaskTypes',
                deleteEntityAllowed: false
            },
            {
                title: 'Report Assignments',
                modelName: 'SystemExportTaskAssignments',
                deleteEntityAllowed: false
            }
        ];

        this.callParent([config]);
    }

});