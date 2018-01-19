/**
 * The <class>System.view.job.tracker.dashboard.DashboardWindow</class> defines the main
 * job tracker dashboard screen
 *
 * @author Andrew
 */
Ext.define('System.view.job.tracker.dashboard.DashboardWindow', {
    extend: 'System.view.component.window.tab.TabSystemWindow',

    requires: [
        'Ext.plugin.LazyItems'
    ],

    alwaysOnTop: -1,

    header: {
        titlePosition: 0,
        items: [
            {
                xtype: 'button',
                iconCls: 'fa fa-cog',
                arrowVisible: false,
                border: false,
                menu: [
                    {
                        text: 'Setup',
                        iconCls: 'fa fa-wrench',
                        menu: [
                            {
                                text: 'Type Management',
                                iconCls: 'fa fa-book',
                                handler: function () {
                                    System.util.system.JobUtils.createWindow('System.view.type.TypeSetupWindow');
                                }
                            },
                            {
                                text: 'Folder Setup',
                                iconCls: 'fa fa-folder-open-o',
                                handler: function () {
                                    System.util.system.JobUtils.createWindow('System.view.folder.path.FolderSetupWindow');
                                }
                            }
                        ]
                    }]
            },
            {
                xtype: 'button',
                iconCls: 'fa fa-sign-out',
                arrowVisible: false,
                border: false,
                tooltip: 'Logout',
                handler: function () {
                    System.util.application.UserUtils.onLogout()
                }
            }
        ]
    },

    tabs: [
        {
            xtype: 'dashboard-tree-grid'
        }, {

            title: 'Tasks',

            layout: 'fit',
            plugins: {
                lazyitems: {
                    layout: 'fit',
                    items: [{
                        xtype: 'base-system-grid-panel',
                        detailFormWindow: 'System.view.system.detail.window.TaskDetailFormWindow',
                        modelName: 'Tasks'//,

                        //groupByField: 'job'
                    }]
                }
            }

        }, {
            title: 'Revisions',

            layout: 'fit',
            plugins: {
                lazyitems: {
                    layout: 'fit',
                    items: [{
                        xtype: 'base-system-grid-panel',
                        detailFormWindow: 'System.view.system.detail.window.NoteDetailFormWindow',
                        modelName: 'TaskRevisions'//,

                        // groupByField: 'task'
                    }]
                }
            }
        }, {
            title: 'Jobs',

            layout: 'fit',
            plugins: {
                lazyitems: {
                    layout: 'fit',
                    items: [{
                        xtype: 'base-system-grid-panel',
                        detailFormWindow: 'System.view.system.detail.window.NoteDetailFormWindow',
                        modelName: 'Jobs'//,

                        //groupByField: 'project'
                    }]
                }
            }

        }, {
            title: 'Projects',

            layout: 'fit',
            plugins: {
                lazyitems: {
                    layout: 'fit',
                    items: [{
                        xtype: 'base-system-grid-panel',
                        detailFormWindow: 'System.view.system.detail.window.NoteDetailFormWindow',
                        modelName: 'Projects'//,

                        //groupByField: 'branch'
                    }]
                }
            }

        }, {
            title: 'Customers',

            layout: 'fit',
            plugins: {
                lazyitems: {
                    layout: 'fit',
                    items: [{
                        xtype: 'base-system-grid-panel',
                        detailFormWindow: 'System.view.system.detail.window.NoteDetailFormWindow',
                        modelName: 'Customers'
                    }]
                }
            }

        }, {
            title: 'Employees',

            layout: 'fit',
            plugins: {
                lazyitems: {
                    layout: 'fit',
                    items: [{
                        xtype: 'base-system-grid-panel',
                        detailFormWindow: 'System.view.system.detail.window.NoteDetailFormWindow',
                        modelName: 'Employees'
                    }]
                }
            }

        }, {
            title: 'Branches',

            layout: 'fit',
            plugins: {
                lazyitems: {
                    layout: 'fit',
                    items: [{
                        xtype: 'base-system-grid-panel',
                        detailFormWindow: 'System.view.system.detail.window.NoteDetailFormWindow',
                        modelName: 'Branches'
                    }]
                }
            }

        }
    ],

    maximized: true,
    closable: false,
    draggable: false,

    constructor: function (config) {
        if (config == undefined) {
            config = {};
        }

        config.tabs = this.tabs;
        config.header = this.header;

        config.maximized = this.maximized;
        config.closable = this.closable;
        config.draggable = this.draggable;

        this.callParent([config]);
    }

});