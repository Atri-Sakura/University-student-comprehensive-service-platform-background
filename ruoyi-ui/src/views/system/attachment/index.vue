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
      <el-form-item label="附件存储URL" prop="attachmentUrl">
        <el-input
          v-model="queryParams.attachmentUrl"
          placeholder="请输入附件存储URL"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="原始文件名" prop="fileName">
        <el-input
          v-model="queryParams.fileName"
          placeholder="请输入原始文件名"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="文件大小" prop="fileSize">
        <el-input
          v-model="queryParams.fileSize"
          placeholder="请输入文件大小"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="文件后缀" prop="fileExt">
        <el-input
          v-model="queryParams.fileExt"
          placeholder="请输入文件后缀"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="过期时间" prop="expireTime">
        <el-date-picker clearable
          v-model="queryParams.expireTime"
          type="date"
          value-format="yyyy-MM-dd"
          placeholder="请选择过期时间">
        </el-date-picker>
      </el-form-item>
      <el-form-item label="是否有效：0-无效" prop="isValid">
        <el-input
          v-model="queryParams.isValid"
          placeholder="请输入是否有效：0-无效"
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
          v-hasPermi="['system:attachment:add']"
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
          v-hasPermi="['system:attachment:edit']"
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
          v-hasPermi="['system:attachment:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['system:attachment:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="attachmentList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="附件唯一ID" align="center" prop="attachmentId" />
      <el-table-column label="关联消息ID" align="center" prop="messageId" />
      <el-table-column label="附件类型：1-图片 2-语音" align="center" prop="attachmentType" />
      <el-table-column label="附件存储URL" align="center" prop="attachmentUrl" />
      <el-table-column label="原始文件名" align="center" prop="fileName" />
      <el-table-column label="文件大小" align="center" prop="fileSize" />
      <el-table-column label="文件后缀" align="center" prop="fileExt" />
      <el-table-column label="过期时间" align="center" prop="expireTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.expireTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="是否有效：0-无效" align="center" prop="isValid" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['system:attachment:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['system:attachment:remove']"
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

    <!-- 添加或修改消息附件（存储图片/语音等附件的元信息）对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="关联消息ID" prop="messageId">
          <el-input v-model="form.messageId" placeholder="请输入关联消息ID" />
        </el-form-item>
        <el-form-item label="附件存储URL" prop="attachmentUrl">
          <el-input v-model="form.attachmentUrl" placeholder="请输入附件存储URL" />
        </el-form-item>
        <el-form-item label="原始文件名" prop="fileName">
          <el-input v-model="form.fileName" placeholder="请输入原始文件名" />
        </el-form-item>
        <el-form-item label="文件大小" prop="fileSize">
          <el-input v-model="form.fileSize" placeholder="请输入文件大小" />
        </el-form-item>
        <el-form-item label="文件后缀" prop="fileExt">
          <el-input v-model="form.fileExt" placeholder="请输入文件后缀" />
        </el-form-item>
        <el-form-item label="过期时间" prop="expireTime">
          <el-date-picker clearable
            v-model="form.expireTime"
            type="date"
            value-format="yyyy-MM-dd"
            placeholder="请选择过期时间">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="是否有效：0-无效" prop="isValid">
          <el-input v-model="form.isValid" placeholder="请输入是否有效：0-无效" />
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
import { listAttachment, getAttachment, delAttachment, addAttachment, updateAttachment } from "@/api/system/attachment"

export default {
  name: "Attachment",
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
      // 消息附件（存储图片/语音等附件的元信息）表格数据
      attachmentList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        messageId: null,
        attachmentType: null,
        attachmentUrl: null,
        fileName: null,
        fileSize: null,
        fileExt: null,
        expireTime: null,
        isValid: null,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        messageId: [
          { required: true, message: "关联消息ID不能为空", trigger: "blur" }
        ],
        attachmentType: [
          { required: true, message: "附件类型：1-图片 2-语音不能为空", trigger: "change" }
        ],
        attachmentUrl: [
          { required: true, message: "附件存储URL不能为空", trigger: "blur" }
        ],
        fileSize: [
          { required: true, message: "文件大小不能为空", trigger: "blur" }
        ],
        isValid: [
          { required: true, message: "是否有效：0-无效不能为空", trigger: "blur" }
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
    /** 查询消息附件（存储图片/语音等附件的元信息）列表 */
    getList() {
      this.loading = true
      listAttachment(this.queryParams).then(response => {
        this.attachmentList = response.rows
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
        attachmentId: null,
        messageId: null,
        attachmentType: null,
        attachmentUrl: null,
        fileName: null,
        fileSize: null,
        fileExt: null,
        expireTime: null,
        isValid: null,
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
      this.ids = selection.map(item => item.attachmentId)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset()
      this.open = true
      this.title = "添加消息附件（存储图片/语音等附件的元信息）"
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset()
      const attachmentId = row.attachmentId || this.ids
      getAttachment(attachmentId).then(response => {
        this.form = response.data
        this.open = true
        this.title = "修改消息附件（存储图片/语音等附件的元信息）"
      })
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.attachmentId != null) {
            updateAttachment(this.form).then(response => {
              this.$modal.msgSuccess("修改成功")
              this.open = false
              this.getList()
            })
          } else {
            addAttachment(this.form).then(response => {
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
      const attachmentIds = row.attachmentId || this.ids
      this.$modal.confirm('是否确认删除消息附件（存储图片/语音等附件的元信息）编号为"' + attachmentIds + '"的数据项？').then(function() {
        return delAttachment(attachmentIds)
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess("删除成功")
      }).catch(() => {})
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('system/attachment/export', {
        ...this.queryParams
      }, `attachment_${new Date().getTime()}.xlsx`)
    }
  }
}
</script>
