/**
 * The <class>System.view.job.tracker.dashboard.DashboardWindow</class> defines the main
 * job tracker dashboard screen
 *
 * @author Andrew
 */
Ext.define('System.view.job.tracker.dashboard.DashboardWindow', {
    extend: 'System.view.component.window.BaseSystemWindow',

    requires: [
        'Ext.Img'
    ],

    items: [{

        xtype: 'panel',
        region: 'center',
        layout: 'fit',

        dockedItems: false,
        tbar: false,

        header: {
            style: 'padding:2px 2px 2px 6px',
            titlePosition: 1,
            items: [{
                xtype: 'image',
                src: '/resources/logo.png',
                style: 'background-color: #005d94;',
                height: 33,
                width: 112
            }]
        },

        items: [
            {
                xtype: 'dashboard-tree-grid'
            }
        ]

    }],


    header: {
        titlePosition: 0,
        items: [{
            xtype: 'button',
            iconCls: 'fa fa-sign-out',
            arrowVisible: false,
            border: false,
            tooltip: 'Logout',
            handler: function () {
                System.util.application.UserUtils.onLogout()
            }
        }]
    },

    alwaysOnTop: -1,
    maximized: true,
    closable: false,
    draggable: false,

    constructor: function (config) {
        if (config == undefined) {
            config = {};
        }

        this.configureAdminHeader();

        this.callParent([config]);
    },

    configureAdminHeader: function () {

        Ext.TaskManager.start({
            run: function () {
                var task = this;
                var mainToolbar = task.mainToolbar;

                if (mainToolbar.userIsLoaded == undefined) {
                    var loggedInUserAdmin = System.util.system.UserUtils.getLoggedInEmployee().admin;

                    if (!Ext.isEmpty(loggedInUserAdmin) && loggedInUserAdmin == true) {
                        mainToolbar.header.add(mainToolbar.getAdminHeader());
                        mainToolbar.userIsLoaded = true;
                    }
                }

                if (task.taskRunCount > 5) {
                    task.interval = 150;
                }
            },
            interval: 5,
            repeat: 50,
            mainToolbar: this
        });

    },

    getAdminHeader: function () {
        return {
            xtype: 'button',
            iconCls: 'fa fa-cog',
            arrowVisible: false,
            border: false,
            menu: [{
                text: 'Project Management',
                iconCls: 'x-fa fa-list',
                handler: function () {
                    System.util.system.JobUtils.createWindow('System.view.project.management.ProjectManagementWindow');
                }
            }, {
                text: 'Business Management',
                iconCls: 'fa fa-users',
                handler: function () {
                    System.util.system.JobUtils.createWindow('System.view.business.management.BusinessManagementWindow');
                }
            }, '-', {
                text: 'Setup',
                iconCls: 'fa fa-wrench',
                menu: [
                    {
                        text: 'Report Setup',
                        iconCls: 'fa fa-files-o',
                        handler: function () {
                            System.util.system.JobUtils.createWindow('System.view.report.management.ReportManagementWindow');
                        }
                    },
                    {
                        text: 'Type Setup',
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
        };
    }

});