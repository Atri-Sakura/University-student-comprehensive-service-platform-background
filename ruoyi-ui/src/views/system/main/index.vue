<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="订单编号" prop="orderNo">
        <el-input
          v-model="queryParams.orderNo"
          placeholder="请输入订单编号"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="下单用户ID" prop="userId">
        <el-input
          v-model="queryParams.userId"
          placeholder="请输入下单用户ID"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="用户昵称(冗余)" prop="userNickname">
        <el-input
          v-model="queryParams.userNickname"
          placeholder="请输入用户昵称(冗余)"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="订单总金额" prop="totalAmount">
        <el-input
          v-model="queryParams.totalAmount"
          placeholder="请输入订单总金额"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="实付金额" prop="payAmount">
        <el-input
          v-model="queryParams.payAmount"
          placeholder="请输入实付金额"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="优惠金额" prop="discountAmount">
        <el-input
          v-model="queryParams.discountAmount"
          placeholder="请输入优惠金额"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="支付时间" prop="payTime">
        <el-date-picker clearable
          v-model="queryParams.payTime"
          type="date"
          value-format="yyyy-MM-dd"
          placeholder="请选择支付时间">
        </el-date-picker>
      </el-form-item>
      <el-form-item label="取消原因" prop="cancelReason">
        <el-input
          v-model="queryParams.cancelReason"
          placeholder="请输入取消原因"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="取消操作人" prop="cancelOperator">
        <el-input
          v-model="queryParams.cancelOperator"
          placeholder="请输入取消操作人"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="取货地址ID" prop="pickAddressId">
        <el-input
          v-model="queryParams.pickAddressId"
          placeholder="请输入取货地址ID"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="取货地址文本" prop="pickAddress">
        <el-input
          v-model="queryParams.pickAddress"
          placeholder="请输入取货地址文本"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="取货联系人" prop="pickContact">
        <el-input
          v-model="queryParams.pickContact"
          placeholder="请输入取货联系人"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="取货电话" prop="pickPhone">
        <el-input
          v-model="queryParams.pickPhone"
          placeholder="请输入取货电话"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="取货经度" prop="pickLongitude">
        <el-input
          v-model="queryParams.pickLongitude"
          placeholder="请输入取货经度"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="取货纬度" prop="pickLatitude">
        <el-input
          v-model="queryParams.pickLatitude"
          placeholder="请输入取货纬度"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="送货地址ID" prop="deliverAddressId">
        <el-input
          v-model="queryParams.deliverAddressId"
          placeholder="请输入送货地址ID"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="送货地址文本" prop="deliverAddress">
        <el-input
          v-model="queryParams.deliverAddress"
          placeholder="请输入送货地址文本"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="收货联系人" prop="deliverContact">
        <el-input
          v-model="queryParams.deliverContact"
          placeholder="请输入收货联系人"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="收货电话" prop="deliverPhone">
        <el-input
          v-model="queryParams.deliverPhone"
          placeholder="请输入收货电话"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="送货经度" prop="deliverLongitude">
        <el-input
          v-model="queryParams.deliverLongitude"
          placeholder="请输入送货经度"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="送货纬度" prop="deliverLatitude">
        <el-input
          v-model="queryParams.deliverLatitude"
          placeholder="请输入送货纬度"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="完成时间" prop="completeTime">
        <el-date-picker clearable
          v-model="queryParams.completeTime"
          type="date"
          value-format="yyyy-MM-dd"
          placeholder="请选择完成时间">
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
          v-hasPermi="['system:main:add']"
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
          v-hasPermi="['system:main:edit']"
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
          v-hasPermi="['system:main:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['system:main:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="mainList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="订单唯一ID" align="center" prop="orderMainId" />
      <el-table-column label="订单编号" align="center" prop="orderNo" />
      <el-table-column label="下单用户ID" align="center" prop="userId" />
      <el-table-column label="用户昵称(冗余)" align="center" prop="userNickname" />
      <el-table-column label="订单类型：1-外卖单 2-跑腿单 3-二手交易单" align="center" prop="orderType" />
      <el-table-column label="订单总金额" align="center" prop="totalAmount" />
      <el-table-column label="实付金额" align="center" prop="payAmount" />
      <el-table-column label="优惠金额" align="center" prop="discountAmount" />
      <el-table-column label="支付状态：0-未支付 1-已支付 2-退款中 3-已退款" align="center" prop="payStatus" />
      <el-table-column label="支付时间" align="center" prop="payTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.payTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="支付方式：1-余额 2-微信 3-支付宝" align="center" prop="payType" />
      <el-table-column label="订单状态：1-待接单 2-待取货 3-配送中 4-已完成 5-已取消" align="center" prop="orderStatus" />
      <el-table-column label="取消原因" align="center" prop="cancelReason" />
      <el-table-column label="取消操作人" align="center" prop="cancelOperator" />
      <el-table-column label="取货地址ID" align="center" prop="pickAddressId" />
      <el-table-column label="取货地址文本" align="center" prop="pickAddress" />
      <el-table-column label="取货联系人" align="center" prop="pickContact" />
      <el-table-column label="取货电话" align="center" prop="pickPhone" />
      <el-table-column label="取货经度" align="center" prop="pickLongitude" />
      <el-table-column label="取货纬度" align="center" prop="pickLatitude" />
      <el-table-column label="送货地址ID" align="center" prop="deliverAddressId" />
      <el-table-column label="送货地址文本" align="center" prop="deliverAddress" />
      <el-table-column label="收货联系人" align="center" prop="deliverContact" />
      <el-table-column label="收货电话" align="center" prop="deliverPhone" />
      <el-table-column label="送货经度" align="center" prop="deliverLongitude" />
      <el-table-column label="送货纬度" align="center" prop="deliverLatitude" />
      <el-table-column label="完成时间" align="center" prop="completeTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.completeTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['system:main:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['system:main:remove']"
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

    <!-- 添加或修改订单主（整合地址与定位信息）对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="订单编号" prop="orderNo">
          <el-input v-model="form.orderNo" placeholder="请输入订单编号" />
        </el-form-item>
        <el-form-item label="下单用户ID" prop="userId">
          <el-input v-model="form.userId" placeholder="请输入下单用户ID" />
        </el-form-item>
        <el-form-item label="用户昵称(冗余)" prop="userNickname">
          <el-input v-model="form.userNickname" placeholder="请输入用户昵称(冗余)" />
        </el-form-item>
        <el-form-item label="订单总金额" prop="totalAmount">
          <el-input v-model="form.totalAmount" placeholder="请输入订单总金额" />
        </el-form-item>
        <el-form-item label="实付金额" prop="payAmount">
          <el-input v-model="form.payAmount" placeholder="请输入实付金额" />
        </el-form-item>
        <el-form-item label="优惠金额" prop="discountAmount">
          <el-input v-model="form.discountAmount" placeholder="请输入优惠金额" />
        </el-form-item>
        <el-form-item label="支付时间" prop="payTime">
          <el-date-picker clearable
            v-model="form.payTime"
            type="date"
            value-format="yyyy-MM-dd"
            placeholder="请选择支付时间">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="取消原因" prop="cancelReason">
          <el-input v-model="form.cancelReason" placeholder="请输入取消原因" />
        </el-form-item>
        <el-form-item label="取消操作人" prop="cancelOperator">
          <el-input v-model="form.cancelOperator" placeholder="请输入取消操作人" />
        </el-form-item>
        <el-form-item label="取货地址ID" prop="pickAddressId">
          <el-input v-model="form.pickAddressId" placeholder="请输入取货地址ID" />
        </el-form-item>
        <el-form-item label="取货地址文本" prop="pickAddress">
          <el-input v-model="form.pickAddress" placeholder="请输入取货地址文本" />
        </el-form-item>
        <el-form-item label="取货联系人" prop="pickContact">
          <el-input v-model="form.pickContact" placeholder="请输入取货联系人" />
        </el-form-item>
        <el-form-item label="取货电话" prop="pickPhone">
          <el-input v-model="form.pickPhone" placeholder="请输入取货电话" />
        </el-form-item>
        <el-form-item label="取货经度" prop="pickLongitude">
          <el-input v-model="form.pickLongitude" placeholder="请输入取货经度" />
        </el-form-item>
        <el-form-item label="取货纬度" prop="pickLatitude">
          <el-input v-model="form.pickLatitude" placeholder="请输入取货纬度" />
        </el-form-item>
        <el-form-item label="送货地址ID" prop="deliverAddressId">
          <el-input v-model="form.deliverAddressId" placeholder="请输入送货地址ID" />
        </el-form-item>
        <el-form-item label="送货地址文本" prop="deliverAddress">
          <el-input v-model="form.deliverAddress" placeholder="请输入送货地址文本" />
        </el-form-item>
        <el-form-item label="收货联系人" prop="deliverContact">
          <el-input v-model="form.deliverContact" placeholder="请输入收货联系人" />
        </el-form-item>
        <el-form-item label="收货电话" prop="deliverPhone">
          <el-input v-model="form.deliverPhone" placeholder="请输入收货电话" />
        </el-form-item>
        <el-form-item label="送货经度" prop="deliverLongitude">
          <el-input v-model="form.deliverLongitude" placeholder="请输入送货经度" />
        </el-form-item>
        <el-form-item label="送货纬度" prop="deliverLatitude">
          <el-input v-model="form.deliverLatitude" placeholder="请输入送货纬度" />
        </el-form-item>
        <el-form-item label="完成时间" prop="completeTime">
          <el-date-picker clearable
            v-model="form.completeTime"
            type="date"
            value-format="yyyy-MM-dd"
            placeholder="请选择完成时间">
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
import { listMain, getMain, delMain, addMain, updateMain } from "@/api/system/main"

export default {
  name: "Main",
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
      // 订单主（整合地址与定位信息）表格数据
      mainList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        orderNo: null,
        userId: null,
        userNickname: null,
        orderType: null,
        totalAmount: null,
        payAmount: null,
        discountAmount: null,
        payStatus: null,
        payTime: null,
        payType: null,
        orderStatus: null,
        cancelReason: null,
        cancelOperator: null,
        pickAddressId: null,
        pickAddress: null,
        pickContact: null,
        pickPhone: null,
        pickLongitude: null,
        pickLatitude: null,
        deliverAddressId: null,
        deliverAddress: null,
        deliverContact: null,
        deliverPhone: null,
        deliverLongitude: null,
        deliverLatitude: null,
        completeTime: null
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        orderNo: [
          { required: true, message: "订单编号不能为空", trigger: "blur" }
        ],
        userId: [
          { required: true, message: "下单用户ID不能为空", trigger: "blur" }
        ],
        userNickname: [
          { required: true, message: "用户昵称(冗余)不能为空", trigger: "blur" }
        ],
        orderType: [
          { required: true, message: "订单类型：1-外卖单 2-跑腿单 3-二手交易单不能为空", trigger: "change" }
        ],
        totalAmount: [
          { required: true, message: "订单总金额不能为空", trigger: "blur" }
        ],
        payAmount: [
          { required: true, message: "实付金额不能为空", trigger: "blur" }
        ],
        discountAmount: [
          { required: true, message: "优惠金额不能为空", trigger: "blur" }
        ],
        payStatus: [
          { required: true, message: "支付状态：0-未支付 1-已支付 2-退款中 3-已退款不能为空", trigger: "change" }
        ],
        orderStatus: [
          { required: true, message: "订单状态：1-待接单 2-待取货 3-配送中 4-已完成 5-已取消不能为空", trigger: "change" }
        ],
        pickAddressId: [
          { required: true, message: "取货地址ID不能为空", trigger: "blur" }
        ],
        pickAddress: [
          { required: true, message: "取货地址文本不能为空", trigger: "blur" }
        ],
        pickContact: [
          { required: true, message: "取货联系人不能为空", trigger: "blur" }
        ],
        pickPhone: [
          { required: true, message: "取货电话不能为空", trigger: "blur" }
        ],
        createTime: [
          { required: true, message: "创建时间不能为空", trigger: "blur" }
        ],
        updateTime: [
          { required: true, message: "最后更新时间不能为空", trigger: "blur" }
        ],
      }
    }
  },
  created() {
    this.getList()
  },
  methods: {
    /** 查询订单主（整合地址与定位信息）列表 */
    getList() {
      this.loading = true
      listMain(this.queryParams).then(response => {
        this.mainList = response.rows
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
        orderMainId: null,
        orderNo: null,
        userId: null,
        userNickname: null,
        orderType: null,
        totalAmount: null,
        payAmount: null,
        discountAmount: null,
        payStatus: null,
        payTime: null,
        payType: null,
        orderStatus: null,
        cancelReason: null,
        cancelOperator: null,
        pickAddressId: null,
        pickAddress: null,
        pickContact: null,
        pickPhone: null,
        pickLongitude: null,
        pickLatitude: null,
        deliverAddressId: null,
        deliverAddress: null,
        deliverContact: null,
        deliverPhone: null,
        deliverLongitude: null,
        deliverLatitude: null,
        createTime: null,
        updateTime: null,
        completeTime: null
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
      this.ids = selection.map(item => item.orderMainId)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset()
      this.open = true
      this.title = "添加订单主（整合地址与定位信息）"
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset()
      const orderMainId = row.orderMainId || this.ids
      getMain(orderMainId).then(response => {
        this.form = response.data
        this.open = true
        this.title = "修改订单主（整合地址与定位信息）"
      })
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.orderMainId != null) {
            updateMain(this.form).then(response => {
              this.$modal.msgSuccess("修改成功")
              this.open = false
              this.getList()
            })
          } else {
            addMain(this.form).then(response => {
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
      const orderMainIds = row.orderMainId || this.ids
      this.$modal.confirm('是否确认删除订单主（整合地址与定位信息）编号为"' + orderMainIds + '"的数据项？').then(function() {
        return delMain(orderMainIds)
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess("删除成功")
      }).catch(() => {})
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('system/main/export', {
        ...this.queryParams
      }, `main_${new Date().getTime()}.xlsx`)
    }
  }
}
</script>
