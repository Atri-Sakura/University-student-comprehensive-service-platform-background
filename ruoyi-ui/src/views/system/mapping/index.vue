<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="登录账号" prop="username">
        <el-input
          v-model="queryParams.username"
          placeholder="请输入登录账号"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="目标数据库：user_db/rider_db/merchant_db/platform_db" prop="targetDb">
        <el-input
          v-model="queryParams.targetDb"
          placeholder="请输入目标数据库：user_db/rider_db/merchant_db/platform_db"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="目标表：user_base/rider_base/merchant_base/platform_admin" prop="targetTable">
        <el-input
          v-model="queryParams.targetTable"
          placeholder="请输入目标表：user_base/rider_base/merchant_base/platform_admin"
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
          v-hasPermi="['system:mapping:add']"
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
          v-hasPermi="['system:mapping:edit']"
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
          v-hasPermi="['system:mapping:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['system:mapping:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="mappingList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="主键ID" align="center" prop="platformRoleMappingId" />
      <el-table-column label="登录账号" align="center" prop="username" />
      <el-table-column label="角色类型：1-学生用户 2-骑手 3-商家 4-平台管理员" align="center" prop="roleType" />
      <el-table-column label="目标数据库：user_db/rider_db/merchant_db/platform_db" align="center" prop="targetDb" />
      <el-table-column label="目标表：user_base/rider_base/merchant_base/platform_admin" align="center" prop="targetTable" />
      <el-table-column label="账号全局状态：0-禁用 1-正常" align="center" prop="accountStatus" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['system:mapping:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['system:mapping:remove']"
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

    <!-- 添加或修改角色-账号映射（多角色登录路由核心）对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="登录账号" prop="username">
          <el-input v-model="form.username" placeholder="请输入登录账号" />
        </el-form-item>
        <el-form-item label="目标数据库：user_db/rider_db/merchant_db/platform_db" prop="targetDb">
          <el-input v-model="form.targetDb" placeholder="请输入目标数据库：user_db/rider_db/merchant_db/platform_db" />
        </el-form-item>
        <el-form-item label="目标表：user_base/rider_base/merchant_base/platform_admin" prop="targetTable">
          <el-input v-model="form.targetTable" placeholder="请输入目标表：user_base/rider_base/merchant_base/platform_admin" />
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
import { listMapping, getMapping, delMapping, addMapping, updateMapping } from "@/api/system/mapping"

export default {
  name: "Mapping",
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
      // 角色-账号映射（多角色登录路由核心）表格数据
      mappingList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        username: null,
        roleType: null,
        targetDb: null,
        targetTable: null,
        accountStatus: null,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        username: [
          { required: true, message: "登录账号不能为空", trigger: "blur" }
        ],
        roleType: [
          { required: true, message: "角色类型：1-学生用户 2-骑手 3-商家 4-平台管理员不能为空", trigger: "change" }
        ],
        targetDb: [
          { required: true, message: "目标数据库：user_db/rider_db/merchant_db/platform_db不能为空", trigger: "blur" }
        ],
        targetTable: [
          { required: true, message: "目标表：user_base/rider_base/merchant_base/platform_admin不能为空", trigger: "blur" }
        ],
        accountStatus: [
          { required: true, message: "账号全局状态：0-禁用 1-正常不能为空", trigger: "change" }
        ],
        createTime: [
          { required: true, message: "创建时间不能为空", trigger: "blur" }
        ],
        updateTime: [
          { required: true, message: "更新时间不能为空", trigger: "blur" }
        ]
      }
    }
  },
  created() {
    this.getList()
  },
  methods: {
    /** 查询角色-账号映射（多角色登录路由核心）列表 */
    getList() {
      this.loading = true
      listMapping(this.queryParams).then(response => {
        this.mappingList = response.rows
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
        platformRoleMappingId: null,
        username: null,
        roleType: null,
        targetDb: null,
        targetTable: null,
        accountStatus: null,
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
      this.ids = selection.map(item => item.platformRoleMappingId)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset()
      this.open = true
      this.title = "添加角色-账号映射（多角色登录路由核心）"
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset()
      const platformRoleMappingId = row.platformRoleMappingId || this.ids
      getMapping(platformRoleMappingId).then(response => {
        this.form = response.data
        this.open = true
        this.title = "修改角色-账号映射（多角色登录路由核心）"
      })
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.platformRoleMappingId != null) {
            updateMapping(this.form).then(response => {
              this.$modal.msgSuccess("修改成功")
              this.open = false
              this.getList()
            })
          } else {
            addMapping(this.form).then(response => {
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
      const platformRoleMappingIds = row.platformRoleMappingId || this.ids
      this.$modal.confirm('是否确认删除角色-账号映射（多角色登录路由核心）编号为"' + platformRoleMappingIds + '"的数据项？').then(function() {
        return delMapping(platformRoleMappingIds)
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess("删除成功")
      }).catch(() => {})
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('system/mapping/export', {
        ...this.queryParams
      }, `mapping_${new Date().getTime()}.xlsx`)
    }
  }
}
</script>
