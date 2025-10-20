<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="所属用户ID" prop="userBaseId">
        <el-input
          v-model="queryParams.userBaseId"
          placeholder="请输入所属用户ID"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="银行名称" prop="bankName">
        <el-input
          v-model="queryParams.bankName"
          placeholder="请输入银行名称"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="银行卡号" prop="cardNumber">
        <el-input
          v-model="queryParams.cardNumber"
          placeholder="请输入银行卡号"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="卡号尾号" prop="cardTailNumber">
        <el-input
          v-model="queryParams.cardTailNumber"
          placeholder="请输入卡号尾号"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="持卡人姓名" prop="holderName">
        <el-input
          v-model="queryParams.holderName"
          placeholder="请输入持卡人姓名"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="身份证号" prop="idNumber">
        <el-input
          v-model="queryParams.idNumber"
          placeholder="请输入身份证号"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="预留手机号" prop="reservePhone">
        <el-input
          v-model="queryParams.reservePhone"
          placeholder="请输入预留手机号"
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
          v-hasPermi="['system:card:add']"
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
          v-hasPermi="['system:card:edit']"
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
          v-hasPermi="['system:card:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['system:card:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="cardList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="主键" align="center" prop="id" />
      <el-table-column label="所属用户ID" align="center" prop="userBaseId" />
      <el-table-column label="银行名称" align="center" prop="bankName" />
      <el-table-column label="卡类型：1-储蓄卡 2-信用卡" align="center" prop="bankCardType" />
      <el-table-column label="银行卡号" align="center" prop="cardNumber" />
      <el-table-column label="卡号尾号" align="center" prop="cardTailNumber" />
      <el-table-column label="持卡人姓名" align="center" prop="holderName" />
      <el-table-column label="身份证号" align="center" prop="idNumber" />
      <el-table-column label="预留手机号" align="center" prop="reservePhone" />
      <el-table-column label="绑定状态：1-正常 0-已解绑" align="center" prop="bindStatus" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['system:card:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['system:card:remove']"
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

    <!-- 添加或修改用户银行卡绑定对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="所属用户ID" prop="userBaseId">
          <el-input v-model="form.userBaseId" placeholder="请输入所属用户ID" />
        </el-form-item>
        <el-form-item label="银行名称" prop="bankName">
          <el-input v-model="form.bankName" placeholder="请输入银行名称" />
        </el-form-item>
        <el-form-item label="银行卡号" prop="cardNumber">
          <el-input v-model="form.cardNumber" placeholder="请输入银行卡号" />
        </el-form-item>
        <el-form-item label="卡号尾号" prop="cardTailNumber">
          <el-input v-model="form.cardTailNumber" placeholder="请输入卡号尾号" />
        </el-form-item>
        <el-form-item label="持卡人姓名" prop="holderName">
          <el-input v-model="form.holderName" placeholder="请输入持卡人姓名" />
        </el-form-item>
        <el-form-item label="身份证号" prop="idNumber">
          <el-input v-model="form.idNumber" placeholder="请输入身份证号" />
        </el-form-item>
        <el-form-item label="预留手机号" prop="reservePhone">
          <el-input v-model="form.reservePhone" placeholder="请输入预留手机号" />
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
import { listCard, getCard, delCard, addCard, updateCard } from "@/api/system/card"

export default {
  name: "Card",
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
      // 用户银行卡绑定表格数据
      cardList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        userBaseId: null,
        bankName: null,
        bankCardType: null,
        cardNumber: null,
        cardTailNumber: null,
        holderName: null,
        idNumber: null,
        reservePhone: null,
        bindStatus: null,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        userBaseId: [
          { required: true, message: "所属用户ID不能为空", trigger: "blur" }
        ],
        bankName: [
          { required: true, message: "银行名称不能为空", trigger: "blur" }
        ],
        bankCardType: [
          { required: true, message: "卡类型：1-储蓄卡 2-信用卡不能为空", trigger: "change" }
        ],
        cardNumber: [
          { required: true, message: "银行卡号不能为空", trigger: "blur" }
        ],
        cardTailNumber: [
          { required: true, message: "卡号尾号不能为空", trigger: "blur" }
        ],
        holderName: [
          { required: true, message: "持卡人姓名不能为空", trigger: "blur" }
        ],
        idNumber: [
          { required: true, message: "身份证号不能为空", trigger: "blur" }
        ],
        reservePhone: [
          { required: true, message: "预留手机号不能为空", trigger: "blur" }
        ],
        bindStatus: [
          { required: true, message: "绑定状态：1-正常 0-已解绑不能为空", trigger: "change" }
        ],
        createTime: [
          { required: true, message: "绑定时间不能为空", trigger: "blur" }
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
    /** 查询用户银行卡绑定列表 */
    getList() {
      this.loading = true
      listCard(this.queryParams).then(response => {
        this.cardList = response.rows
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
        id: null,
        userBaseId: null,
        bankName: null,
        bankCardType: null,
        cardNumber: null,
        cardTailNumber: null,
        holderName: null,
        idNumber: null,
        reservePhone: null,
        bindStatus: null,
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
      this.ids = selection.map(item => item.id)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset()
      this.open = true
      this.title = "添加用户银行卡绑定"
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset()
      const id = row.id || this.ids
      getCard(id).then(response => {
        this.form = response.data
        this.open = true
        this.title = "修改用户银行卡绑定"
      })
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateCard(this.form).then(response => {
              this.$modal.msgSuccess("修改成功")
              this.open = false
              this.getList()
            })
          } else {
            addCard(this.form).then(response => {
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
      const ids = row.id || this.ids
      this.$modal.confirm('是否确认删除用户银行卡绑定编号为"' + ids + '"的数据项？').then(function() {
        return delCard(ids)
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess("删除成功")
      }).catch(() => {})
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('system/card/export', {
        ...this.queryParams
      }, `card_${new Date().getTime()}.xlsx`)
    }
  }
}
</script>
