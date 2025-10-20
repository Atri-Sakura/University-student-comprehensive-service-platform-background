<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="操作管理员ID" prop="adminId">
        <el-input
          v-model="queryParams.adminId"
          placeholder="请输入操作管理员ID"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="管理员姓名" prop="adminName">
        <el-input
          v-model="queryParams.adminName"
          placeholder="请输入管理员姓名"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="操作模块" prop="operModule">
        <el-input
          v-model="queryParams.operModule"
          placeholder="请输入操作模块"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="操作IP地址" prop="ipAddress">
        <el-input
          v-model="queryParams.ipAddress"
          placeholder="请输入操作IP地址"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="操作时间" prop="operTime">
        <el-date-picker clearable
          v-model="queryParams.operTime"
          type="date"
          value-format="yyyy-MM-dd"
          placeholder="请选择操作时间">
        </el-date-picker>
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
          v-hasPermi="['system:log:add']"
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
          v-hasPermi="['system:log:edit']"
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
          v-hasPermi="['system:log:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['system:log:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="logList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="日志唯一ID" align="center" prop="platformOperateLogId" />
      <el-table-column label="操作管理员ID" align="center" prop="adminId" />
      <el-table-column label="管理员姓名" align="center" prop="adminName" />
      <el-table-column label="操作类型" align="center" prop="operType" />
      <el-table-column label="操作模块" align="center" prop="operModule" />
      <el-table-column label="操作内容" align="center" prop="operContent" />
      <el-table-column label="操作IP地址" align="center" prop="ipAddress" />
      <el-table-column label="操作时间" align="center" prop="operTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.operTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="用户代理信息" align="center" prop="userAgent" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['system:log:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['system:log:remove']"
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

    <!-- 添加或修改系统操作日志对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="操作管理员ID" prop="adminId">
          <el-input v-model="form.adminId" placeholder="请输入操作管理员ID" />
        </el-form-item>
        <el-form-item label="管理员姓名" prop="adminName">
          <el-input v-model="form.adminName" placeholder="请输入管理员姓名" />
        </el-form-item>
        <el-form-item label="操作模块" prop="operModule">
          <el-input v-model="form.operModule" placeholder="请输入操作模块" />
        </el-form-item>
        <el-form-item label="操作内容">
          <editor v-model="form.operContent" :min-height="192"/>
        </el-form-item>
        <el-form-item label="操作IP地址" prop="ipAddress">
          <el-input v-model="form.ipAddress" placeholder="请输入操作IP地址" />
        </el-form-item>
        <el-form-item label="操作时间" prop="operTime">
          <el-date-picker clearable
            v-model="form.operTime"
            type="date"
            value-format="yyyy-MM-dd"
            placeholder="请选择操作时间">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="用户代理信息" prop="userAgent">
          <el-input v-model="form.userAgent" type="textarea" placeholder="请输入内容" />
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
import { listLog, getLog, delLog, addLog, updateLog } from "@/api/system/log"

export default {
  name: "Log",
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
      // 系统操作日志表格数据
      logList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        adminId: null,
        adminName: null,
        operType: null,
        operModule: null,
        operContent: null,
        ipAddress: null,
        operTime: null,
        userAgent: null
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        adminId: [
          { required: true, message: "操作管理员ID不能为空", trigger: "blur" }
        ],
        adminName: [
          { required: true, message: "管理员姓名不能为空", trigger: "blur" }
        ],
        operType: [
          { required: true, message: "操作类型不能为空", trigger: "change" }
        ],
        operModule: [
          { required: true, message: "操作模块不能为空", trigger: "blur" }
        ],
        operContent: [
          { required: true, message: "操作内容不能为空", trigger: "blur" }
        ],
        ipAddress: [
          { required: true, message: "操作IP地址不能为空", trigger: "blur" }
        ],
        operTime: [
          { required: true, message: "操作时间不能为空", trigger: "blur" }
        ],
      }
    }
  },
  created() {
    this.getList()
  },
  methods: {
    /** 查询系统操作日志列表 */
    getList() {
      this.loading = true
      listLog(this.queryParams).then(response => {
        this.logList = response.rows
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
        platformOperateLogId: null,
        adminId: null,
        adminName: null,
        operType: null,
        operModule: null,
        operContent: null,
        ipAddress: null,
        operTime: null,
        userAgent: null
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
      this.ids = selection.map(item => item.platformOperateLogId)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset()
      this.open = true
      this.title = "添加系统操作日志"
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset()
      const platformOperateLogId = row.platformOperateLogId || this.ids
      getLog(platformOperateLogId).then(response => {
        this.form = response.data
        this.open = true
        this.title = "修改系统操作日志"
      })
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.platformOperateLogId != null) {
            updateLog(this.form).then(response => {
              this.$modal.msgSuccess("修改成功")
              this.open = false
              this.getList()
            })
          } else {
            addLog(this.form).then(response => {
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
      const platformOperateLogIds = row.platformOperateLogId || this.ids
      this.$modal.confirm('是否确认删除系统操作日志编号为"' + platformOperateLogIds + '"的数据项？').then(function() {
        return delLog(platformOperateLogIds)
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess("删除成功")
      }).catch(() => {})
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('system/log/export', {
        ...this.queryParams
      }, `log_${new Date().getTime()}.xlsx`)
    }
  }
}
</script>
