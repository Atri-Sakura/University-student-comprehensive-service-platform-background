<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="关联订单ID" prop="orderMainId">
        <el-input
          v-model="queryParams.orderMainId"
          placeholder="请输入关联订单ID"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="商家ID" prop="merchantId">
        <el-input
          v-model="queryParams.merchantId"
          placeholder="请输入商家ID"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="商家名称(冗余)" prop="merchantName">
        <el-input
          v-model="queryParams.merchantName"
          placeholder="请输入商家名称(冗余)"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="商品ID" prop="goodsId">
        <el-input
          v-model="queryParams.goodsId"
          placeholder="请输入商品ID"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="商品名称(冗余)" prop="goodsName">
        <el-input
          v-model="queryParams.goodsName"
          placeholder="请输入商品名称(冗余)"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="商品单价" prop="goodsPrice">
        <el-input
          v-model="queryParams.goodsPrice"
          placeholder="请输入商品单价"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="购买数量" prop="quantity">
        <el-input
          v-model="queryParams.quantity"
          placeholder="请输入购买数量"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="小计金额" prop="subtotal">
        <el-input
          v-model="queryParams.subtotal"
          placeholder="请输入小计金额"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="商品规格" prop="goodsSpec">
        <el-input
          v-model="queryParams.goodsSpec"
          placeholder="请输入商品规格"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="商品标签(冗余，如“甜口/冰饮”，用于推荐)" prop="goodsTags">
        <el-input
          v-model="queryParams.goodsTags"
          placeholder="请输入商品标签(冗余，如“甜口/冰饮”，用于推荐)"
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
          v-hasPermi="['system:detail:add']"
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
          v-hasPermi="['system:detail:edit']"
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
          v-hasPermi="['system:detail:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['system:detail:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="detailList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="明细唯一ID" align="center" prop="orderTakeoutDetailId" />
      <el-table-column label="关联订单ID" align="center" prop="orderMainId" />
      <el-table-column label="商家ID" align="center" prop="merchantId" />
      <el-table-column label="商家名称(冗余)" align="center" prop="merchantName" />
      <el-table-column label="商品ID" align="center" prop="goodsId" />
      <el-table-column label="商品名称(冗余)" align="center" prop="goodsName" />
      <el-table-column label="商品单价" align="center" prop="goodsPrice" />
      <el-table-column label="购买数量" align="center" prop="quantity" />
      <el-table-column label="小计金额" align="center" prop="subtotal" />
      <el-table-column label="商品规格" align="center" prop="goodsSpec" />
      <el-table-column label="商品标签(冗余，如“甜口/冰饮”，用于推荐)" align="center" prop="goodsTags" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['system:detail:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['system:detail:remove']"
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

    <!-- 添加或修改外卖订单明细（不含地址信息）对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="关联订单ID" prop="orderMainId">
          <el-input v-model="form.orderMainId" placeholder="请输入关联订单ID" />
        </el-form-item>
        <el-form-item label="商家ID" prop="merchantId">
          <el-input v-model="form.merchantId" placeholder="请输入商家ID" />
        </el-form-item>
        <el-form-item label="商家名称(冗余)" prop="merchantName">
          <el-input v-model="form.merchantName" placeholder="请输入商家名称(冗余)" />
        </el-form-item>
        <el-form-item label="商品ID" prop="goodsId">
          <el-input v-model="form.goodsId" placeholder="请输入商品ID" />
        </el-form-item>
        <el-form-item label="商品名称(冗余)" prop="goodsName">
          <el-input v-model="form.goodsName" placeholder="请输入商品名称(冗余)" />
        </el-form-item>
        <el-form-item label="商品单价" prop="goodsPrice">
          <el-input v-model="form.goodsPrice" placeholder="请输入商品单价" />
        </el-form-item>
        <el-form-item label="购买数量" prop="quantity">
          <el-input v-model="form.quantity" placeholder="请输入购买数量" />
        </el-form-item>
        <el-form-item label="小计金额" prop="subtotal">
          <el-input v-model="form.subtotal" placeholder="请输入小计金额" />
        </el-form-item>
        <el-form-item label="商品规格" prop="goodsSpec">
          <el-input v-model="form.goodsSpec" placeholder="请输入商品规格" />
        </el-form-item>
        <el-form-item label="商品标签(冗余，如“甜口/冰饮”，用于推荐)" prop="goodsTags">
          <el-input v-model="form.goodsTags" placeholder="请输入商品标签(冗余，如“甜口/冰饮”，用于推荐)" />
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
import { listDetail, getDetail, delDetail, addDetail, updateDetail } from "@/api/system/detail"

export default {
  name: "Detail",
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
      // 外卖订单明细（不含地址信息）表格数据
      detailList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        orderMainId: null,
        merchantId: null,
        merchantName: null,
        goodsId: null,
        goodsName: null,
        goodsPrice: null,
        quantity: null,
        subtotal: null,
        goodsSpec: null,
        goodsTags: null
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        orderMainId: [
          { required: true, message: "关联订单ID不能为空", trigger: "blur" }
        ],
        merchantId: [
          { required: true, message: "商家ID不能为空", trigger: "blur" }
        ],
        merchantName: [
          { required: true, message: "商家名称(冗余)不能为空", trigger: "blur" }
        ],
        goodsId: [
          { required: true, message: "商品ID不能为空", trigger: "blur" }
        ],
        goodsName: [
          { required: true, message: "商品名称(冗余)不能为空", trigger: "blur" }
        ],
        goodsPrice: [
          { required: true, message: "商品单价不能为空", trigger: "blur" }
        ],
        quantity: [
          { required: true, message: "购买数量不能为空", trigger: "blur" }
        ],
        subtotal: [
          { required: true, message: "小计金额不能为空", trigger: "blur" }
        ],
      }
    }
  },
  created() {
    this.getList()
  },
  methods: {
    /** 查询外卖订单明细（不含地址信息）列表 */
    getList() {
      this.loading = true
      listDetail(this.queryParams).then(response => {
        this.detailList = response.rows
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
        orderTakeoutDetailId: null,
        orderMainId: null,
        merchantId: null,
        merchantName: null,
        goodsId: null,
        goodsName: null,
        goodsPrice: null,
        quantity: null,
        subtotal: null,
        goodsSpec: null,
        goodsTags: null
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
      this.ids = selection.map(item => item.orderTakeoutDetailId)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset()
      this.open = true
      this.title = "添加外卖订单明细（不含地址信息）"
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset()
      const orderTakeoutDetailId = row.orderTakeoutDetailId || this.ids
      getDetail(orderTakeoutDetailId).then(response => {
        this.form = response.data
        this.open = true
        this.title = "修改外卖订单明细（不含地址信息）"
      })
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.orderTakeoutDetailId != null) {
            updateDetail(this.form).then(response => {
              this.$modal.msgSuccess("修改成功")
              this.open = false
              this.getList()
            })
          } else {
            addDetail(this.form).then(response => {
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
      const orderTakeoutDetailIds = row.orderTakeoutDetailId || this.ids
      this.$modal.confirm('是否确认删除外卖订单明细（不含地址信息）编号为"' + orderTakeoutDetailIds + '"的数据项？').then(function() {
        return delDetail(orderTakeoutDetailIds)
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess("删除成功")
      }).catch(() => {})
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('system/detail/export', {
        ...this.queryParams
      }, `detail_${new Date().getTime()}.xlsx`)
    }
  }
}
</script>
