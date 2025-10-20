<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="关联消息ID" prop="messageId">
        <el-input
          v-model="queryParams.messageId"
          placeholder="请输入关联消息ID"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="已读用户ID" prop="readerId">
        <el-input
          v-model="queryParams.readerId"
          placeholder="请输入已读用户ID"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="已读时间" prop="readTime">
        <el-date-picker clearable
          v-model="queryParams.readTime"
          type="date"
          value-format="yyyy-MM-dd"
          placeholder="请选择已读时间">
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
          v-hasPermi="['system:read:add']"
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
          v-hasPermi="['system:read:edit']"
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
          v-hasPermi="['system:read:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['system:read:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="readList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="已读记录唯一ID" align="center" prop="readId" />
      <el-table-column label="关联消息ID" align="center" prop="messageId" />
      <el-table-column label="已读用户类型：1-用户 2-骑手 3-商家" align="center" prop="readerType" />
      <el-table-column label="已读用户ID" align="center" prop="readerId" />
      <el-table-column label="已读状态：0-未读 1-已读" align="center" prop="readStatus" />
      <el-table-column label="已读时间" align="center" prop="readTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.readTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['system:read:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['system:read:remove']"
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

    <!-- 添加或修改消息已读状态（追踪每条消息的已读情况，支撑群聊扩展）对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="关联消息ID" prop="messageId">
          <el-input v-model="form.messageId" placeholder="请输入关联消息ID" />
        </el-form-item>
        <el-form-item label="已读用户ID" prop="readerId">
          <el-input v-model="form.readerId" placeholder="请输入已读用户ID" />
        </el-form-item>
        <el-form-item label="已读时间" prop="readTime">
          <el-date-picker clearable
            v-model="form.readTime"
            type="date"
            value-format="yyyy-MM-dd"
            placeholder="请选择已读时间">
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
import { listRead, getRead, delRead, addRead, updateRead } from "@/api/system/read"

export default {
  name: "Read",
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
      // 消息已读状态（追踪每条消息的已读情况，支撑群聊扩展）表格数据
      readList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        messageId: null,
        readerType: null,
        readerId: null,
        readStatus: null,
        readTime: null,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        messageId: [
          { required: true, message: "关联消息ID不能为空", trigger: "blur" }
        ],
        readerType: [
          { required: true, message: "已读用户类型：1-用户 2-骑手 3-商家不能为空", trigger: "change" }
        ],
        readerId: [
          { required: true, message: "已读用户ID不能为空", trigger: "blur" }
        ],
        readStatus: [
          { required: true, message: "已读状态：0-未读 1-已读不能为空", trigger: "change" }
        ],
        createTime: [
          { required: true, message: "$comment不能为空", trigger: "blur" }
        ],
        updateTime: [
          { required: true, message: "$comment不能为空", trigger: "blur" }
        ]
      }
    }
  },
  created() {
    this.getList()
  },
  methods: {
    /** 查询消息已读状态（追踪每条消息的已读情况，支撑群聊扩展）列表 */
    getList() {
      this.loading = true
      listRead(this.queryParams).then(response => {
        this.readList = response.rows
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
        readId: null,
        messageId: null,
        readerType: null,
        readerId: null,
        readStatus: null,
        readTime: null,
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
      this.ids = selection.map(item => item.readId)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset()
      this.open = true
      this.title = "添加消息已读状态（追踪每条消息的已读情况，支撑群聊扩展）"
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset()
      const readId = row.readId || this.ids
      getRead(readId).then(response => {
        this.form = response.data
        this.open = true
        this.title = "修改消息已读状态（追踪每条消息的已读情况，支撑群聊扩展）"
      })
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.readId != null) {
            updateRead(this.form).then(response => {
              this.$modal.msgSuccess("修改成功")
              this.open = false
              this.getList()
            })
          } else {
            addRead(this.form).then(response => {
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
      const readIds = row.readId || this.ids
      this.$modal.confirm('是否确认删除消息已读状态（追踪每条消息的已读情况，支撑群聊扩展）编号为"' + readIds + '"的数据项？').then(function() {
        return delRead(readIds)
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess("删除成功")
      }).catch(() => {})
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('system/read/export', {
        ...this.queryParams
      }, `read_${new Date().getTime()}.xlsx`)
    }
  }
}
</script>
