function showLionAddDialog(projectName, curEnv) {
    var container = $('#hide-dialog-container');
    var dialog = container.find('#lion-add-dialog');
    var mapKeyOldValue = '';
    if (dialog.length == 0) {
        container.load('/div/lionmap/lion-add-dialog', {
            params: projectName + ',' + curEnv
        }, function () {
            dialog = $('#lion-add-dialog').modal('show');
            var mapKey = dialog.find('#mapKey');
            mapKey.blur(function () {
                if (mapKey.val() && mapKey.val() != mapKeyOldValue) {
                    mapKeyOldValue = mapKey.val();
                    $.ajax({
                        type: 'POST',
                        url: '/lion/env-check',
                        data: $('#lion-add-dialog-form').serialize(),
                        success: function (result) {
                            dialog.find('#envs input:checkbox').each(function (i, dom) {
                                if (result.indexOf((i + 1) + '') == -1) {
                                    $(dom).prop('checked', true).prop('disabled', false).parent('label').removeClass('disabled').css('color', '');
                                } else {
                                    $(dom).prop('checked', false).prop('disabled', true).parent('label').addClass('disabled').css('color', '#F00');
                                }
                            });
                        }
                    });
                }
            });
        });
    } else {
        dialog.find('#lion-add-dialog-form').clearFormData();
        dialog.modal('show');
    }
}
function addLionMap(curEnv) {
    if (!$('#mapKey').val()) {
        $.warning('key不能为空!', 3000);
        return;
    }

    if (!$('#mapValue').val()) {
        $.warning('value不能为空!', 3000);
        return;
    }

    var checks = false;
    var isDynamicAdd = false;
    $('#envs input:checkbox').each(function (i, dom) {
        if ($(dom).prop('checked')) {
            checks = true;
            if ((i + 1) == curEnv) {
                isDynamicAdd = true;
            }
        }
    });
    if (!checks) {
        $.warning('选择的环境不能为空!', 3000);
        return;
    }

    $.ajax({
        type: 'POST',
        url: '/lion/add',
        data: $('#lion-add-dialog-form').serialize(),
        success: function (result) {
            if (result) {
                if (isDynamicAdd) {
                    $('#dataTable tbody').append(
                        $('<tr />').attr('id', 'dataTable_tr_' + result.id).append(
                            $('<td />').text(result.id)
                        ).append(
                            $('<td />').text(result.mapKey)
                        ).append(
                            $('<td />').text(result.mapValue)
                        ).append(
                            $('<td />').text(result.lazy ? '是' : '否')
                        ).append(
                            $('<td />').html(
                                '<a class="btn btn-default btn-xs" href="javascript:showLionUpdateDialog(' +
                                result.id + ',\'' + result.projectName + '\',\'' + curEnv +
                                '\');"><span class="glyphicon glyphicon-upload" aria-hidden="true"></span> 更新</a>' +
                                '<a class="btn btn-default btn-xs" style="margin-left: 20px;" href="javascript:showLionDeleteDialog(' +
                                result.id + ',\'' + result.projectName + '\',\'' + curEnv +
                                '\');"><span class="glyphicon glyphicon-trash" aria-hidden="true"></span> 删除</a>'
                            )
                        )
                    );
                }
                $.warning('添加成功!', 3000);
                $('#lion-add-dialog').modal('hide');
            } else {
                $.warning('添加失败,检查是否已经存在该配置!');
            }
        }
    });
}
function showLionUpdateDialog(id, projectName, curEnv) {
    var container = $('#hide-dialog-container');
    var dialog = container.find('#lion-update-dialog');
    if (dialog.length == 0) {
        container.load('/div/lionmap/lion-update-dialog', {
            params: projectName + ',' + curEnv
        }, function () {
            $('#lion-update-dialog').modal('show');
            renderUpdateDialog(id, curEnv);
        });
    } else {
        dialog.modal('show');
        renderUpdateDialog(id, curEnv);
    }
}
function renderUpdateDialog(id, curEnv) {
    var tds = $('#dataTable_tr_' + id + ' td');
    $('#mapKey').val(tds[1].innerHTML);
    $('#mapValue').val(tds[2].innerHTML);
    if (tds[3].innerHTML == '是') {
        $('#lazy input:radio:eq(0)').prop('checked', true);
        $('#lazy input:radio:eq(1)').prop('checked', false);
    } else {
        $('#lazy input:radio:eq(1)').prop('checked', true);
        $('#lazy input:radio:eq(0)').prop('checked', false);
    }
    $.ajax({
        type: 'POST',
        url: '/lion/env-check',
        data: $('#lion-update-dialog-form').serialize(),
        success: function (result) {
            $('#envs input:checkbox').each(function (i, dom) {
                if (result.indexOf((i + 1) + '') == -1) {
                    $(dom).prop('checked', false).prop('disabled', true).parent('label').addClass('disabled').css('color', '#F00');
                } else {
                    if ((i + 1) == curEnv) {
                        $(dom).prop('checked', true).prop('disabled', false).parent('label').removeClass('disabled').css('color', '');
                    } else {
                        $(dom).prop('checked', false).prop('disabled', false).parent('label').removeClass('disabled').css('color', '');
                    }
                }
            });
        }
    });
    $('#updateBtn').unbind('click').bind('click', function () {
        if (!$('#mapValue').val()) {
            $.warning('value不能为空!', 3000);
            return;
        }
        var checks = false;
        var isDynamicUpdate = false;
        $('#envs input:checkbox').each(function (i, dom) {
            if ($(dom).prop('checked')) {
                checks = true;
                if ((i + 1) == curEnv) {
                    isDynamicUpdate = true;
                }
            }
        });
        if (!checks) {
            $.warning('更新的环境不能为空!', 3000);
            return;
        }
        $.ajax({
            type: 'POST',
            url: '/lion/update',
            data: $('#lion-update-dialog-form').serialize(),
            success: function (result) {
                if (result) {
                    if (isDynamicUpdate) {
                        tds[1].innerHTML = result.mapKey;
                        tds[2].innerHTML = result.mapValue;
                        tds[3].innerHTML = result.lazy ? '是' : '否';
                    }
                    $.warning('更新成功!', 3000);
                    $('#lion-update-dialog').modal('hide');
                } else {
                    $.warning('更新失败,检查是否存在该配置');
                }
            }
        });
    });
}
function showLionDeleteDialog(id, projectName, curEnv) {
    var container = $('#hide-dialog-container');
    var dialog = container.find('#lion-delete-dialog');
    if (dialog.length == 0) {
        container.load('/div/lionmap/lion-delete-dialog', {
            params: projectName + ',' + curEnv
        }, function () {
            $('#lion-delete-dialog').modal('show');
            renderDeleteDialog(id, curEnv);
        });
    } else {
        dialog.modal('show');
        renderDeleteDialog(id, curEnv);
    }
}
function renderDeleteDialog(id, curEnv) {
    var tds = $('#dataTable_tr_' + id + ' td');
    $('#mapKey').val(tds[1].innerHTML);
    $('#mapValue').val(tds[2].innerHTML);
    if (tds[3].innerHTML == '是') {
        $('#lazy input:radio:eq(0)').prop('checked', true);
        $('#lazy input:radio:eq(1)').prop('checked', false);
    } else {
        $('#lazy input:radio:eq(1)').prop('checked', true);
        $('#lazy input:radio:eq(0)').prop('checked', false);
    }
    $.ajax({
        type: 'POST',
        url: '/lion/env-check',
        data: $('#lion-delete-dialog-form').serialize(),
        success: function (result) {
            $('#envs input:checkbox').each(function (i, dom) {
                if (result.indexOf((i + 1) + '') == -1) {
                    $(dom).prop('checked', false).prop('disabled', true).parent('label').addClass('disabled').css('color', '#F00');
                } else {
                    $(dom).prop('checked', true).prop('disabled', false).parent('label').removeClass('disabled').css('color', '');
                }
            });
        }
    });
    $('#deleteBtn').unbind('click').bind('click', function () {
        if (!confirm("删除配置后将不可恢复,你确定要删除吗?")) {
            return;
        }
        var checks = false;
        var isDynamicDelete = false;
        $('#envs input:checkbox').each(function (i, dom) {
            if ($(dom).prop('checked')) {
                checks = true;
                if ((i + 1) == curEnv) {
                    isDynamicDelete = true;
                }
            }
        });
        if (!checks) {
            $.warning('更新的环境不能为空!', 3000);
            return;
        }
        $.ajax({
            type: 'POST',
            url: '/lion/delete',
            data: $('#lion-delete-dialog-form').serialize(),
            success: function (result) {
                if (result) {
                    if (isDynamicDelete) {
                        $('#dataTable_tr_' + id).remove();
                    }
                    $.warning('删除成功!', 3000);
                    $('#lion-delete-dialog').modal('hide');
                } else {
                    $.warning('删除失败,检查是否存在该配置');
                }
            }
        });
    });
}