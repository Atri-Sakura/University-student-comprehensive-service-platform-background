<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="所属会话ID" prop="sessionId">
        <el-input
          v-model="queryParams.sessionId"
          placeholder="请输入所属会话ID"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="发送方ID" prop="fromId">
        <el-input
          v-model="queryParams.fromId"
          placeholder="请输入发送方ID"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="接收方ID" prop="toId">
        <el-input
          v-model="queryParams.toId"
          placeholder="请输入接收方ID"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="消息发送时间" prop="sendTime">
        <el-date-picker clearable
          v-model="queryParams.sendTime"
          type="date"
          value-format="yyyy-MM-dd"
          placeholder="请选择消息发送时间">
        </el-date-picker>
      </el-form-item>
      <el-form-item label="消息送达时间" prop="deliverTime">
        <el-date-picker clearable
          v-model="queryParams.deliverTime"
          type="date"
          value-format="yyyy-MM-dd"
          placeholder="请选择消息送达时间">
        </el-date-picker>
      </el-form-item>
      <el-form-item label="消息已读时间" prop="readTime">
        <el-date-picker clearable
          v-model="queryParams.readTime"
          type="date"
          value-format="yyyy-MM-dd"
          placeholder="请选择消息已读时间">
        </el-date-picker>
      </el-form-item>
      <el-form-item label="是否删除" prop="isDeleted">
        <el-input
          v-model="queryParams.isDeleted"
          placeholder="请输入是否删除"
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
          v-hasPermi="['system:message:add']"
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
          v-hasPermi="['system:message:edit']"
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
          v-hasPermi="['system:message:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['system:message:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="messageList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="消息唯一ID" align="center" prop="messageId" />
      <el-table-column label="所属会话ID" align="center" prop="sessionId" />
      <el-table-column label="发送方类型：1-用户 2-骑手 3-商家 4-系统" align="center" prop="fromType" />
      <el-table-column label="发送方ID" align="center" prop="fromId" />
      <el-table-column label="接收方类型：1-用户 2-骑手 3-商家" align="center" prop="toType" />
      <el-table-column label="接收方ID" align="center" prop="toId" />
      <el-table-column label="消息类型：1-文本 2-图片 3-语音 4-系统通知" align="center" prop="msgType" />
      <el-table-column label="消息内容：文本消息存内容；图片/语音存MinIO的URL；系统通知存模板内容" align="center" prop="msgContent" />
      <el-table-column label="消息状态：0-发送中 1-已送达 2-已读 3-已撤回 4-发送失败" align="center" prop="msgStatus" />
      <el-table-column label="消息发送时间" align="center" prop="sendTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.sendTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="消息送达时间" align="center" prop="deliverTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.deliverTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="消息已读时间" align="center" prop="readTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.readTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="是否删除" align="center" prop="isDeleted" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['system:message:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['system:message:remove']"
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

    <!-- 添加或修改聊天消息（存储单条消息的核心信息）对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="所属会话ID" prop="sessionId">
          <el-input v-model="form.sessionId" placeholder="请输入所属会话ID" />
        </el-form-item>
        <el-form-item label="发送方ID" prop="fromId">
          <el-input v-model="form.fromId" placeholder="请输入发送方ID" />
        </el-form-item>
        <el-form-item label="接收方ID" prop="toId">
          <el-input v-model="form.toId" placeholder="请输入接收方ID" />
        </el-form-item>
        <el-form-item label="消息内容：文本消息存内容；图片/语音存MinIO的URL；系统通知存模板内容">
          <editor v-model="form.msgContent" :min-height="192"/>
        </el-form-item>
        <el-form-item label="消息发送时间" prop="sendTime">
          <el-date-picker clearable
            v-model="form.sendTime"
            type="date"
            value-format="yyyy-MM-dd"
            placeholder="请选择消息发送时间">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="消息送达时间" prop="deliverTime">
          <el-date-picker clearable
            v-model="form.deliverTime"
            type="date"
            value-format="yyyy-MM-dd"
            placeholder="请选择消息送达时间">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="消息已读时间" prop="readTime">
          <el-date-picker clearable
            v-model="form.readTime"
            type="date"
            value-format="yyyy-MM-dd"
            placeholder="请选择消息已读时间">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="是否删除" prop="isDeleted">
          <el-input v-model="form.isDeleted" placeholder="请输入是否删除" />
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
import { listMessage, getMessage, delMessage, addMessage, updateMessage } from "@/api/system/message"

export default {
  name: "Message",
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
      // 聊天消息（存储单条消息的核心信息）表格数据
      messageList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        sessionId: null,
        fromType: null,
        fromId: null,
        toType: null,
        toId: null,
        msgType: null,
        msgContent: null,
        msgStatus: null,
        sendTime: null,
        deliverTime: null,
        readTime: null,
        isDeleted: null,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        sessionId: [
          { required: true, message: "所属会话ID不能为空", trigger: "blur" }
        ],
        fromType: [
          { required: true, message: "发送方类型：1-用户 2-骑手 3-商家 4-系统不能为空", trigger: "change" }
        ],
        fromId: [
          { required: true, message: "发送方ID不能为空", trigger: "blur" }
        ],
        toType: [
          { required: true, message: "接收方类型：1-用户 2-骑手 3-商家不能为空", trigger: "change" }
        ],
        toId: [
          { required: true, message: "接收方ID不能为空", trigger: "blur" }
        ],
        msgType: [
          { required: true, message: "消息类型：1-文本 2-图片 3-语音 4-系统通知不能为空", trigger: "change" }
        ],
        msgStatus: [
          { required: true, message: "消息状态：0-发送中 1-已送达 2-已读 3-已撤回 4-发送失败不能为空", trigger: "change" }
        ],
        sendTime: [
          { required: true, message: "消息发送时间不能为空", trigger: "blur" }
        ],
        isDeleted: [
          { required: true, message: "是否删除不能为空", trigger: "blur" }
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
    /** 查询聊天消息（存储单条消息的核心信息）列表 */
    getList() {
      this.loading = true
      listMessage(this.queryParams).then(response => {
        this.messageList = response.rows
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
        messageId: null,
        sessionId: null,
        fromType: null,
        fromId: null,
        toType: null,
        toId: null,
        msgType: null,
        msgContent: null,
        msgStatus: null,
        sendTime: null,
        deliverTime: null,
        readTime: null,
        isDeleted: null,
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
      this.ids = selection.map(item => item.messageId)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset()
      this.open = true
      this.title = "添加聊天消息（存储单条消息的核心信息）"
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset()
      const messageId = row.messageId || this.ids
      getMessage(messageId).then(response => {
        this.form = response.data
        this.open = true
        this.title = "修改聊天消息（存储单条消息的核心信息）"
      })
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.messageId != null) {
            updateMessage(this.form).then(response => {
              this.$modal.msgSuccess("修改成功")
              this.open = false
              this.getList()
            })
          } else {
            addMessage(this.form).then(response => {
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
      const messageIds = row.messageId || this.ids
      this.$modal.confirm('是否确认删除聊天消息（存储单条消息的核心信息）编号为"' + messageIds + '"的数据项？').then(function() {
        return delMessage(messageIds)
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess("删除成功")
      }).catch(() => {})
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('system/message/export', {
        ...this.queryParams
      }, `message_${new Date().getTime()}.xlsx`)
    }
  }
}
</script>
