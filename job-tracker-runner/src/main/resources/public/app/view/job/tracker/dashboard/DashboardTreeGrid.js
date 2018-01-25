/**
 * The <class>System.view.job.tracker.dashboard.DashboardTreeGrid</class> defines the main
 * job.tracker dashboard tree grid
 *
 * @author Andrew
 */
Ext.define('System.view.job.tracker.dashboard.DashboardTreeGrid', {
    extend: 'System.view.component.panel.tree.BaseSystemTreeGridPanel',

    requires: [],

    xtype: 'dashboard-tree-grid',

    title: 'Dashboard',

    store: 'JobTreeStore',

    listeners: {

        beforeitemdblclick: function (view, record, item, index, e, eOpts) {
            //Ignore double clicks on these row action columns (Project Number and Task header)
            var columnText = e.position.column.text;
            if (columnText == 'Project Number' || columnText == 'Task') {
                return false;
            }

            var grid = view.grid;
            var entityId = record.data.entityId;
            var modelName = record.data.taskNumber ? "Tasks" : "Jobs";
            var detailPage = record.data.taskNumber ? 'System.view.system.detail.window.TaskDetailFormWindow' : 'System.view.system.detail.window.NoteDetailFormWindow';
            var revisionNumber = record.data.revisionNumber;

            System.util.component.GridColumnUtils.getStoreByModelName(modelName, function (store) {

                System.util.data.StoreUtils.queryStoreById(store, entityId, function (records, operation, success, scope) {

                    var record = records[0];
                    record.store = operation._scope.store;
                    record.data.revisionNumber = revisionNumber;
                    System.util.system.detail.DetailWindowFormUtils.createNewDetailFormWindowFromRecord(record, scope, detailPage);

                }, undefined, grid.up('window'));
            });

            return false;
        }

    },

    config: {
        modelName: 'Jobs'
    },

    /**
     * PULLED FROM System.view.component.panel.grid.BaseSystemGridPanel END
     */


    constructor: function (config) {
        Ext.suspendLayouts();

        var me = this;
        me.gridSetup(me);
        me.configureGridRefreshEvents(me);

        this.callParent([config]);
        Ext.resumeLayouts(true);
    },

    gridSetup: function (grid) {
        var me = grid;

        me.createdSearchFilter = Ext.create('System.view.component.field.search.SearchField', {
            parentEntity: me,
            manualInitialize: true
        });

        me.initializeSearch(me.createdSearchFilter);

        //Once done update to be within the search fields initComponent method and the render call will only execute
        //if the search field hasn't been initialized
        me.createdSearchFilter.configureSearchField(me.createdSearchFilter);

        Ext.apply(me, {
            store: new System.view.job.data.store.JobTreeStore(),

            dockedItems: [{
                dock: 'top',
                xtype: 'toolbar',
                items: [
                    '->',
                    me.createdSearchFilter
                ]
            },
                {
                    dock: 'left',
                    xtype: 'toolbar',
                    padding: 0,
                    items: [
                        {
                            xtype: 'button',
                            border: false,
                            iconCls: 'x-fa fa-refresh',
                            handler: function () {
                                this.up().up().store.reload();
                            }
                        },
                        System.util.component.ToolbarUtils.addButton(me)
                    ]
                }],

            columns: System.view.job.tracker.dashboard.columns.DashboardColumnUtils.defaultGridColumns(),

            defaults:
                {
                    flex: 1
                }
        });
    },

    initializeSearch: function (searchField) {
        Ext.TaskManager.start({
            run: function () {
                var task = this;
                var me = task.searchField;
                if (me.userIsLoaded == undefined) {
                    var loggedInUserFirstBranchName = System.util.system.UserUtils.getLoggedInEmployee().branch;

                    if (!Ext.isEmpty(loggedInUserFirstBranchName)) {

                        //Set our default branch filter once the user branch information is loaded
                        me.defaultSearchValue = [{
                            "operator": "=",
                            "value": loggedInUserFirstBranchName,
                            "property": "project.branch.name || job.project.branch.name"
                        }];

                        //Initialize and configure our search to execute our initial load
                        me.configureSearchField(me);

                        //The user is loaded so we are done attempting
                        me.userIsLoaded = true;
                    }

                    if (task.taskRunCount > 5) {
                        task.interval = 150;
                    }

                    if (task.taskRunCount == 49) {
                        //Initialize and configure our search to execute our initial load
                        me.configureSearchField(me);
                    }
                }
            },
            interval: 10,
            repeat: 50,
            searchField: searchField
        });
    },

    configureGridRefreshEvents: function (treeGrid) {

        treeGrid.gridRefreshFunction = function (store, record, operation, modifiedFieldNames, details, eOpts) {
            var treeGrid = this;
            //A write operation to the server has been performed
            // against one of the stores we are listening on
            //Since their data changed and we depend on it
            //Let's refresh our dashboard grid to reflect any changes
            if (treeGrid.store) {

                treeGrid.store.reload({
                    scope: treeGrid,
                    callback: function (records, operation, success) {
                        this.view.refreshView();
                    }
                });

            }
        };

        treeGrid.refreshListenerStoreKeys = ['ProjectsStore', 'JobsStore', 'TasksStore', 'TaskRevisionsStore'];

        Ext.data.StoreManager.on(
            'add',
            function (index, o, key, eOpts) {
                var me = this;

                if (Ext.Array.contains(this.refreshListenerStoreKeys, key)) {
                    //A store that we want to listen on was just created and added to the store manager
                    me.configureGridStoreRefreshListeners(o, me.gridRefreshFunction, me);
                }

            },
            treeGrid
        );

        treeGrid.store.sort('taskNumber', 'ASC');

        System.util.component.GridColumnUtils.fullyInitializeStoreByModelName("Tasks");
        System.util.component.GridColumnUtils.fullyInitializeStoreByModelName("Jobs");

    },

    configureGridStoreRefreshListeners: function (store, gridRefreshFunction, treeGrid) {
        store.on('update', gridRefreshFunction, treeGrid);
    }

});