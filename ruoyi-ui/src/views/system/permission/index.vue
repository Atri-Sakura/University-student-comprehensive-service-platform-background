<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="权限名称" prop="permName">
        <el-input
          v-model="queryParams.permName"
          placeholder="请输入权限名称"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="权限标识" prop="permKey">
        <el-input
          v-model="queryParams.permKey"
          placeholder="请输入权限标识"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="父权限ID" prop="parentPermId">
        <el-input
          v-model="queryParams.parentPermId"
          placeholder="请输入父权限ID"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="菜单路径" prop="menuPath">
        <el-input
          v-model="queryParams.menuPath"
          placeholder="请输入菜单路径"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="排序序号" prop="sort">
        <el-input
          v-model="queryParams.sort"
          placeholder="请输入排序序号"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="primary"
          plain
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
          v-hasPermi="['system:permission:add']"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="el-icon-edit"
          size="mini"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['system:permission:edit']"
        >修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['system:permission:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['system:permission:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="permissionList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="权限唯一ID" align="center" prop="platformPermissionId" />
      <el-table-column label="权限名称" align="center" prop="permName" />
      <el-table-column label="权限标识" align="center" prop="permKey" />
      <el-table-column label="权限类型：1-菜单 2-按钮" align="center" prop="permType" />
      <el-table-column label="父权限ID" align="center" prop="parentPermId" />
      <el-table-column label="菜单路径" align="center" prop="menuPath" />
      <el-table-column label="排序序号" align="center" prop="sort" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['system:permission:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['system:permission:remove']"
          >删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    
    <pagination
      v-show="total>0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />

    <!-- 添加或修改权限对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="权限名称" prop="permName">
          <el-input v-model="form.permName" placeholder="请输入权限名称" />
        </el-form-item>
        <el-form-item label="权限标识" prop="permKey">
          <el-input v-model="form.permKey" placeholder="请输入权限标识" />
        </el-form-item>
        <el-form-item label="父权限ID" prop="parentPermId">
          <el-input v-model="form.parentPermId" placeholder="请输入父权限ID" />
        </el-form-item>
        <el-form-item label="菜单路径" prop="menuPath">
          <el-input v-model="form.menuPath" placeholder="请输入菜单路径" />
        </el-form-item>
        <el-form-item label="排序序号" prop="sort">
          <el-input v-model="form.sort" placeholder="请输入排序序号" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listPermission, getPermission, delPermission, addPermission, updatePermission } from "@/api/system/permission"

export default {
  name: "Permission",
  data() {
    return {
      // 遮罩层
      loading: true,
      // 选中数组
      ids: [],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 权限表格数据
      permissionList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        permName: null,
        permKey: null,
        permType: null,
        parentPermId: null,
        menuPath: null,
        sort: null,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        permName: [
          { required: true, message: "权限名称不能为空", trigger: "blur" }
        ],
        permKey: [
          { required: true, message: "权限标识不能为空", trigger: "blur" }
        ],
        permType: [
          { required: true, message: "权限类型：1-菜单 2-按钮不能为空", trigger: "change" }
        ],
        createTime: [
          { required: true, message: "创建时间不能为空", trigger: "blur" }
        ],
        updateTime: [
          { required: true, message: "最后更新时间不能为空", trigger: "blur" }
        ]
      }
    }
  },
  created() {
    this.getList()
  },
  methods: {
    /** 查询权限列表 */
    getList() {
      this.loading = true
      listPermission(this.queryParams).then(response => {
        this.permissionList = response.rows
        this.total = response.total
        this.loading = false
      })
    },
    // 取消按钮
    cancel() {
      this.open = false
      this.reset()
    },
    // 表单重置
    reset() {
      this.form = {
        platformPermissionId: null,
        permName: null,
        permKey: null,
        permType: null,
        parentPermId: null,
        menuPath: null,
        sort: null,
        createTime: null,
        updateTime: null
      }
      this.resetForm("form")
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1
      this.getList()
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.resetForm("queryForm")
      this.handleQuery()
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.platformPermissionId)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset()
      this.open = true
      this.title = "添加权限"
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset()
      const platformPermissionId = row.platformPermissionId || this.ids
      getPermission(platformPermissionId).then(response => {
        this.form = response.data
        this.open = true
        this.title = "修改权限"
      })
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.platformPermissionId != null) {
            updatePermission(this.form).then(response => {
              this.$modal.msgSuccess("修改成功")
              this.open = false
              this.getList()
            })
          } else {
            addPermission(this.form).then(response => {
              this.$modal.msgSuccess("新增成功")
              this.open = false
              this.getList()
            })
          }
        }
      })
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const platformPermissionIds = row.platformPermissionId || this.ids
      this.$modal.confirm('是否确认删除权限编号为"' + platformPermissionIds + '"的数据项？').then(function() {
        return delPermission(platformPermissionIds)
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess("删除成功")
      }).catch(() => {})
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('system/permission/export', {
        ...this.queryParams
      }, `permission_${new Date().getTime()}.xlsx`)
    }
  }
}
</script>
