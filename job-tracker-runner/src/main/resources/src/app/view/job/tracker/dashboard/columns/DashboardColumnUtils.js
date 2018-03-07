/**
 * The <class>System.view.job.tracker.dashboard.columns.DashboardColumnUtils</class> defines
 * static utility methods for creating portions of the dashboard grid screen
 *
 * @author Andrew
 */
Ext.define('System.view.job.tracker.dashboard.columns.DashboardColumnUtils', {

    requires: [],

    ///////////////////////////////////////////////////////////////////////
    ////////                                                         Methods                                                      //////////
    /////////////////////////////////////////////////////////////////////

    statics: {

        /**
         * Returns the default columns used by the dashboard grid screen
         */
        defaultGridColumns: function () {
            return [
                {
                    xtype: 'treecolumn', //this is so we know which column will show the tree
                    text: 'Project Number',
                    autoSizeColumn: true,
                    sortable: true,
                    dataIndex: 'jobNumber',
                    flex: undefined,
                    maxWidth: 200,
                    renderer: function (value, cell, record) {
                        if (record && record.data && record.data.taskNumber) {
                            return "";
                        }
                        return value;
                    }
                }, {
                    text: 'Branch',
                    autoSizeColumn: true,
                    dataIndex: 'branchName',
                    sortable: true,
                    hidden: true
                }, {
                    text: 'ID',
                    dataIndex: 'entityId',
                    autoSizeColumn: true,
                    hidden: true
                }, {
                    text: 'Task #',
                    autoSizeColumn: true,
                    dataIndex: 'taskNumber',
                    align: 'center',
                    xtype: 'numbercolumn',
                    format: '0',
                    filter: {type: 'number', hideTrigger: true, keyNavEnabled: false, mouseWheelEnabled: false},
                    sortable: true,
                    flex: undefined,
                    maxWidth: 70,
                    renderer: function (value, record) {
                        if (value == null || value < 1) {
                            return undefined;
                        }
                        return value;
                    }
                }, {
                    text: 'Task Type',
                    autoSizeColumn: true,
                    dataIndex: 'taskTypeName',
                    sortable: true
                }, {
                    text: 'Date(m/d/y)',
                    autoSizeColumn: true,
                    dataIndex: 'date',
                    sortable: true,
                    xtype: 'datecolumn', format: 'm-d-Y', filter: 'date',
                    flex: undefined,
                    maxWidth: 110
                }, {
                    text: 'Client',
                    autoSizeColumn: true,
                    dataIndex: 'businessClientName',
                    sortable: true
                }, {
                    text: 'Files',
                    autoSizeColumn: true,
                    tooltip: 'Open Files',
                    align: 'center',
                    dataIndex: 'fileFolderPath',
                    flex: undefined,
                    maxWidth: 55,
                    renderer: function (value) {
                        //Default browser assumption is IE
                        var fileLinkPrefix = 'file://';

                        if (!Ext.isIE) {
                            //Assume the browser is chrome or other
                            fileLinkPrefix = 'file:///';
                        }

                        return '<a href="' + fileLinkPrefix + value + '" target="_blank"><div align="center" tabindex="-1" role="button" class="x-action-col-icon x-action-col-0  x-fa fa-folder-open-o" data-qtip="Open Files" data-tabindex-value="0" data-tabindex-counter="1"></div></a>';
                    }
                }, {
                    text: 'Project',
                    autoSizeColumn: true,
                    dataIndex: 'projectName',
                    sortable: true
                }, {
                    text: 'Description',
                    autoSizeColumn: true,
                    dataIndex: 'description',
                    sortable: true
                }, {
                    text: 'Assigned To',
                    autoSizeColumn: true,
                    dataIndex: 'assignedTo',
                    sortable: true
                }, {
                    text: 'Rev #',
                    xtype: 'gridcolumn',

                    align: 'center',
                    flex: undefined,
                    menuDisabled: true,
                    resizeable: false,

                    columns: [
                        {
                            text: "",
                            dataIndex: 'revisionNumber',
                            align: 'center',
                            resizeable: false,
                            width: 30,
                            height: 0,
                            flex: undefined,
                            menuDisabled: true,
                            sortable: false,
                            renderer: function (value, cell, record) {
                                if (record && record.data && !Ext.isEmpty(record.data.revisionNumber)) {
                                    return value;
                                }
                                return undefined;
                            }
                        },
                        {
                            text: "",
                            xtype: 'actioncolumn',
                            dataIndex: 'revisionNumber',

                            align: 'center',
                            resizeable: false,
                            width: 40,
                            height: 0,
                            flex: undefined,
                            menuDisabled: true,

                            handler: function (grid, rowindex, cellIndex, a, e, record, tr) {
                                //Add Task Revision
                                var modelName = "TaskRevisions";

                                System.util.component.GridColumnUtils.getStoreByModelName(modelName, function (store) {
                                    System.util.system.detail.DetailWindowFormUtils.createNewDetailFormWindow(store.model.entityName, grid.up('window'), {task: record.data.entityId});
                                });

                            },
                            getTip: function (v, meta, record) {
                                return 'Add Revision';
                            },
                            getClass: function (v, meta, record) {
                                if (record && record.data && record.data.taskNumber) {
                                    return 'x-fa fa-plus';
                                }
                                return undefined;
                            },
                            isDisabled: function (view, rowIndex, colIndex, item, record) {
                                return !record.data.taskNumber;
                            },
                            renderer: function (value, cell, record) {
                                if (record && record.data && !record.data.taskNumber) {
                                    return undefined;
                                }
                                return value;
                            }
                        }
                    ]
                }, {
                    text: 'Task',
                    autoSizeColumn: true,
                    menuDisabled: true,
                    xtype: 'actioncolumn',
                    align: 'center',
                    flex: undefined,
                    maxWidth: 55,

                    handler: function (grid, rowindex, cellIndex, a, e, record, tr) {
                        if (record.data.taskNumber) {
                            //Delete Task

                            System.util.application.Util.showConfirmationMessage("Delete task?",
                                function (btnText) {

                                    if (btnText === "ok") {
                                        var modelName = "Tasks";

                                        System.util.component.GridColumnUtils.getStoreByModelName(modelName, function (store) {
                                            var taskId = record.data.entityId;

                                            var taskRecord = store.getById(taskId);

                                            if (taskRecord) {
                                                store.remove(taskRecord);
                                            } else {
                                                System.util.data.StoreUtils.queryStoreById(store, taskId, function (records, operation, success, scope) {
                                                    records[0].erase();
                                                }, undefined, store);
                                            }

                                        });

                                    }
                                }, this);

                        } else {
                            //Add Task
                            var modelName = "Tasks";

                            System.util.component.GridColumnUtils.getStoreByModelName(modelName, function (store) {
                                System.util.system.detail.DetailWindowFormUtils.createNewDetailFormWindow(store.model.entityName, grid.up('window'), {job: record.data.entityId});
                            });
                        }
                    },
                    getTip: function (v, meta, record) {
                        if (record.data.taskNumber) {
                            return 'Delete Task';
                        } else {
                            return 'Add Task';
                        }
                    },
                    getClass: function (v, meta, record) {
                        if (record.data.taskNumber) {
                            return 'x-fa fa-minus';
                        } else {
                            return 'x-fa fa-plus';
                        }
                    }
                }, {
                    text: 'Docs',
                    autoSizeColumn: true,
                    menuDisabled: true,
                    xtype: 'actioncolumn',
                    tooltip: 'Generate Report',
                    align: 'center',
                    iconCls: 'x-fa fa-file-text-o',
                    flex: undefined,
                    maxWidth: 60,
                    handler: function (grid, rowIndex, colIndex, actionItem, e, record, row) {

                        if (record.data.taskNumber) {
                            System.util.export.report.ExportUtils.populateAndShowExportMenu(e, record, 'Tasks', 'Task Reports', 'fa fa-tasks');
                        } else {
                            System.util.export.report.ExportUtils.populateAndShowExportMenu(e, record, 'Jobs', 'Job Reports', 'fa fa-clipboard');
                        }

                    },
                    getTip: function (v, meta, record) {
                        return 'Document Export';
                    },
                    isDisabled: function (view, rowIndex, colIndex, item, record) {
                        return false; //TODO return true if it has no menu items
                    }
                }, {
                    text: 'Status',
                    autoSizeColumn: true,
                    dataIndex: 'status',
                    sortable: true,
                    flex: undefined,
                    maxWidth: 100
                }
            ];
        }

    }

});