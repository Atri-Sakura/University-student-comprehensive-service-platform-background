<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
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
      <el-form-item label="最后一条消息的ID" prop="lastMsgId">
        <el-input
          v-model="queryParams.lastMsgId"
          placeholder="请输入最后一条消息的ID"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="最后一条消息发送时间" prop="lastMsgTime">
        <el-date-picker clearable
          v-model="queryParams.lastMsgTime"
          type="date"
          value-format="yyyy-MM-dd"
          placeholder="请选择最后一条消息发送时间">
        </el-date-picker>
      </el-form-item>
      <el-form-item label="未读消息数" prop="unreadCount">
        <el-input
          v-model="queryParams.unreadCount"
          placeholder="请输入未读消息数"
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
          v-hasPermi="['system:session:add']"
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
          v-hasPermi="['system:session:edit']"
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
          v-hasPermi="['system:session:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['system:session:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="sessionList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="会话唯一ID" align="center" prop="sessionId" />
      <el-table-column label="发送方类型：1-用户 2-骑手 3-商家" align="center" prop="fromType" />
      <el-table-column label="发送方ID" align="center" prop="fromId" />
      <el-table-column label="接收方类型：1-用户 2-骑手 3-商家" align="center" prop="toType" />
      <el-table-column label="接收方ID" align="center" prop="toId" />
      <el-table-column label="最后一条消息的ID" align="center" prop="lastMsgId" />
      <el-table-column label="最后一条消息内容" align="center" prop="lastMsgContent" />
      <el-table-column label="最后一条消息类型：1-文本 2-图片 3-语音 4-系统通知" align="center" prop="lastMsgType" />
      <el-table-column label="最后一条消息发送时间" align="center" prop="lastMsgTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.lastMsgTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="未读消息数" align="center" prop="unreadCount" />
      <el-table-column label="会话状态：0-已删除 1-正常 2-已屏蔽" align="center" prop="sessionStatus" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['system:session:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['system:session:remove']"
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

    <!-- 添加或修改聊天会话（管理双方的聊天窗口关系）对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="发送方ID" prop="fromId">
          <el-input v-model="form.fromId" placeholder="请输入发送方ID" />
        </el-form-item>
        <el-form-item label="接收方ID" prop="toId">
          <el-input v-model="form.toId" placeholder="请输入接收方ID" />
        </el-form-item>
        <el-form-item label="最后一条消息的ID" prop="lastMsgId">
          <el-input v-model="form.lastMsgId" placeholder="请输入最后一条消息的ID" />
        </el-form-item>
        <el-form-item label="最后一条消息内容">
          <editor v-model="form.lastMsgContent" :min-height="192"/>
        </el-form-item>
        <el-form-item label="最后一条消息发送时间" prop="lastMsgTime">
          <el-date-picker clearable
            v-model="form.lastMsgTime"
            type="date"
            value-format="yyyy-MM-dd"
            placeholder="请选择最后一条消息发送时间">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="未读消息数" prop="unreadCount">
          <el-input v-model="form.unreadCount" placeholder="请输入未读消息数" />
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
import { listSession, getSession, delSession, addSession, updateSession } from "@/api/system/session"

export default {
  name: "Session",
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
      // 聊天会话（管理双方的聊天窗口关系）表格数据
      sessionList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        fromType: null,
        fromId: null,
        toType: null,
        toId: null,
        lastMsgId: null,
        lastMsgContent: null,
        lastMsgType: null,
        lastMsgTime: null,
        unreadCount: null,
        sessionStatus: null,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        fromType: [
          { required: true, message: "发送方类型：1-用户 2-骑手 3-商家不能为空", trigger: "change" }
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
        unreadCount: [
          { required: true, message: "未读消息数不能为空", trigger: "blur" }
        ],
        sessionStatus: [
          { required: true, message: "会话状态：0-已删除 1-正常 2-已屏蔽不能为空", trigger: "change" }
        ],
        createTime: [
          { required: true, message: "会话创建时间不能为空", trigger: "blur" }
        ],
        updateTime: [
          { required: true, message: "会话更新时间不能为空", trigger: "blur" }
        ]
      }
    }
  },
  created() {
    this.getList()
  },
  methods: {
    /** 查询聊天会话（管理双方的聊天窗口关系）列表 */
    getList() {
      this.loading = true
      listSession(this.queryParams).then(response => {
        this.sessionList = response.rows
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
        sessionId: null,
        fromType: null,
        fromId: null,
        toType: null,
        toId: null,
        lastMsgId: null,
        lastMsgContent: null,
        lastMsgType: null,
        lastMsgTime: null,
        unreadCount: null,
        sessionStatus: null,
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
      this.ids = selection.map(item => item.sessionId)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset()
      this.open = true
      this.title = "添加聊天会话（管理双方的聊天窗口关系）"
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset()
      const sessionId = row.sessionId || this.ids
      getSession(sessionId).then(response => {
        this.form = response.data
        this.open = true
        this.title = "修改聊天会话（管理双方的聊天窗口关系）"
      })
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.sessionId != null) {
            updateSession(this.form).then(response => {
              this.$modal.msgSuccess("修改成功")
              this.open = false
              this.getList()
            })
          } else {
            addSession(this.form).then(response => {
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
      const sessionIds = row.sessionId || this.ids
      this.$modal.confirm('是否确认删除聊天会话（管理双方的聊天窗口关系）编号为"' + sessionIds + '"的数据项？').then(function() {
        return delSession(sessionIds)
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess("删除成功")
      }).catch(() => {})
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('system/session/export', {
        ...this.queryParams
      }, `session_${new Date().getTime()}.xlsx`)
    }
  }
}
</script>
