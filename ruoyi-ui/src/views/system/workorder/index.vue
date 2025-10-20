<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="关联订单ID" prop="orderId">
        <el-input
          v-model="queryParams.orderId"
          placeholder="请输入关联订单ID"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="提交用户ID" prop="userId">
        <el-input
          v-model="queryParams.userId"
          placeholder="请输入提交用户ID"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="用户昵称" prop="userNickname">
        <el-input
          v-model="queryParams.userNickname"
          placeholder="请输入用户昵称"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="处理人ID" prop="handlerId">
        <el-input
          v-model="queryParams.handlerId"
          placeholder="请输入处理人ID"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="处理人姓名" prop="handlerName">
        <el-input
          v-model="queryParams.handlerName"
          placeholder="请输入处理人姓名"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="处理时间" prop="handleTime">
        <el-date-picker clearable
          v-model="queryParams.handleTime"
          type="date"
          value-format="yyyy-MM-dd"
          placeholder="请选择处理时间">
        </el-date-picker>
      </el-form-item>
      <el-form-item label="关闭时间" prop="closeTime">
        <el-date-picker clearable
          v-model="queryParams.closeTime"
          type="date"
          value-format="yyyy-MM-dd"
          placeholder="请选择关闭时间">
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
          v-hasPermi="['system:workorder:add']"
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
          v-hasPermi="['system:workorder:edit']"
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
          v-hasPermi="['system:workorder:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['system:workorder:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="workorderList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="工单唯一ID" align="center" prop="platformWorkorderId" />
      <el-table-column label="关联订单ID" align="center" prop="orderId" />
      <el-table-column label="提交用户ID" align="center" prop="userId" />
      <el-table-column label="用户类型：1-学生 2-骑手 3-商家" align="center" prop="userType" />
      <el-table-column label="用户昵称" align="center" prop="userNickname" />
      <el-table-column label="工单内容" align="center" prop="content" />
      <el-table-column label="问题图片URL" align="center" prop="imgUrls" />
      <el-table-column label="工单类型：1-订单投诉 2-服务差评 3-系统故障 4-其他" align="center" prop="workorderType" />
      <el-table-column label="处理人ID" align="center" prop="handlerId" />
      <el-table-column label="处理人姓名" align="center" prop="handlerName" />
      <el-table-column label="处理状态：0-待处理 1-处理中 2-已解决 3-已关闭" align="center" prop="handleStatus" />
      <el-table-column label="处理结果" align="center" prop="handleResult" />
      <el-table-column label="处理时间" align="center" prop="handleTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.handleTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="关闭时间" align="center" prop="closeTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.closeTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['system:workorder:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['system:workorder:remove']"
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

    <!-- 添加或修改客服工单对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="关联订单ID" prop="orderId">
          <el-input v-model="form.orderId" placeholder="请输入关联订单ID" />
        </el-form-item>
        <el-form-item label="提交用户ID" prop="userId">
          <el-input v-model="form.userId" placeholder="请输入提交用户ID" />
        </el-form-item>
        <el-form-item label="用户昵称" prop="userNickname">
          <el-input v-model="form.userNickname" placeholder="请输入用户昵称" />
        </el-form-item>
        <el-form-item label="工单内容">
          <editor v-model="form.content" :min-height="192"/>
        </el-form-item>
        <el-form-item label="问题图片URL" prop="imgUrls">
          <el-input v-model="form.imgUrls" type="textarea" placeholder="请输入内容" />
        </el-form-item>
        <el-form-item label="处理人ID" prop="handlerId">
          <el-input v-model="form.handlerId" placeholder="请输入处理人ID" />
        </el-form-item>
        <el-form-item label="处理人姓名" prop="handlerName">
          <el-input v-model="form.handlerName" placeholder="请输入处理人姓名" />
        </el-form-item>
        <el-form-item label="处理结果" prop="handleResult">
          <el-input v-model="form.handleResult" type="textarea" placeholder="请输入内容" />
        </el-form-item>
        <el-form-item label="处理时间" prop="handleTime">
          <el-date-picker clearable
            v-model="form.handleTime"
            type="date"
            value-format="yyyy-MM-dd"
            placeholder="请选择处理时间">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="关闭时间" prop="closeTime">
          <el-date-picker clearable
            v-model="form.closeTime"
            type="date"
            value-format="yyyy-MM-dd"
            placeholder="请选择关闭时间">
          </el-date-picker>
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
import { listWorkorder, getWorkorder, delWorkorder, addWorkorder, updateWorkorder } from "@/api/system/workorder"

export default {
  name: "Workorder",
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
      // 客服工单表格数据
      workorderList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        orderId: null,
        userId: null,
        userType: null,
        userNickname: null,
        content: null,
        imgUrls: null,
        workorderType: null,
        handlerId: null,
        handlerName: null,
        handleStatus: null,
        handleResult: null,
        handleTime: null,
        closeTime: null
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        userId: [
          { required: true, message: "提交用户ID不能为空", trigger: "blur" }
        ],
        userType: [
          { required: true, message: "用户类型：1-学生 2-骑手 3-商家不能为空", trigger: "change" }
        ],
        userNickname: [
          { required: true, message: "用户昵称不能为空", trigger: "blur" }
        ],
        content: [
          { required: true, message: "工单内容不能为空", trigger: "blur" }
        ],
        workorderType: [
          { required: true, message: "工单类型：1-订单投诉 2-服务差评 3-系统故障 4-其他不能为空", trigger: "change" }
        ],
        handleStatus: [
          { required: true, message: "处理状态：0-待处理 1-处理中 2-已解决 3-已关闭不能为空", trigger: "change" }
        ],
        createTime: [
          { required: true, message: "创建时间不能为空", trigger: "blur" }
        ],
      }
    }
  },
  created() {
    this.getList()
  },
  methods: {
    /** 查询客服工单列表 */
    getList() {
      this.loading = true
      listWorkorder(this.queryParams).then(response => {
        this.workorderList = response.rows
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
        platformWorkorderId: null,
        orderId: null,
        userId: null,
        userType: null,
        userNickname: null,
        content: null,
        imgUrls: null,
        workorderType: null,
        handlerId: null,
        handlerName: null,
        handleStatus: null,
        handleResult: null,
        createTime: null,
        handleTime: null,
        closeTime: null
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
      this.ids = selection.map(item => item.platformWorkorderId)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset()
      this.open = true
      this.title = "添加客服工单"
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset()
      const platformWorkorderId = row.platformWorkorderId || this.ids
      getWorkorder(platformWorkorderId).then(response => {
        this.form = response.data
        this.open = true
        this.title = "修改客服工单"
      })
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.platformWorkorderId != null) {
            updateWorkorder(this.form).then(response => {
              this.$modal.msgSuccess("修改成功")
              this.open = false
              this.getList()
            })
          } else {
            addWorkorder(this.form).then(response => {
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
      const platformWorkorderIds = row.platformWorkorderId || this.ids
      this.$modal.confirm('是否确认删除客服工单编号为"' + platformWorkorderIds + '"的数据项？').then(function() {
        return delWorkorder(platformWorkorderIds)
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess("删除成功")
      }).catch(() => {})
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('system/workorder/export', {
        ...this.queryParams
      }, `workorder_${new Date().getTime()}.xlsx`)
    }
  }
}
</script>
