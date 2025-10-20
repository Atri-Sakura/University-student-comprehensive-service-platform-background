<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="订单ID" prop="orderMainId">
        <el-input
          v-model="queryParams.orderMainId"
          placeholder="请输入订单ID"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="骑手ID" prop="riderId">
        <el-input
          v-model="queryParams.riderId"
          placeholder="请输入骑手ID"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="骑手昵称(冗余)" prop="riderNickname">
        <el-input
          v-model="queryParams.riderNickname"
          placeholder="请输入骑手昵称(冗余)"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="配送费" prop="deliveryFee">
        <el-input
          v-model="queryParams.deliveryFee"
          placeholder="请输入配送费"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="实际取货经度" prop="actualPickLongitude">
        <el-input
          v-model="queryParams.actualPickLongitude"
          placeholder="请输入实际取货经度"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="实际取货纬度" prop="actualPickLatitude">
        <el-input
          v-model="queryParams.actualPickLatitude"
          placeholder="请输入实际取货纬度"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="实际送达经度" prop="actualDeliverLongitude">
        <el-input
          v-model="queryParams.actualDeliverLongitude"
          placeholder="请输入实际送达经度"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="实际送达纬度" prop="actualDeliverLatitude">
        <el-input
          v-model="queryParams.actualDeliverLatitude"
          placeholder="请输入实际送达纬度"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="派单时间" prop="assignTime">
        <el-date-picker clearable
          v-model="queryParams.assignTime"
          type="date"
          value-format="yyyy-MM-dd"
          placeholder="请选择派单时间">
        </el-date-picker>
      </el-form-item>
      <el-form-item label="接单时间" prop="receiveTime">
        <el-date-picker clearable
          v-model="queryParams.receiveTime"
          type="date"
          value-format="yyyy-MM-dd"
          placeholder="请选择接单时间">
        </el-date-picker>
      </el-form-item>
      <el-form-item label="取货时间" prop="pickTime">
        <el-date-picker clearable
          v-model="queryParams.pickTime"
          type="date"
          value-format="yyyy-MM-dd"
          placeholder="请选择取货时间">
        </el-date-picker>
      </el-form-item>
      <el-form-item label="送达时间" prop="deliverTime">
        <el-date-picker clearable
          v-model="queryParams.deliverTime"
          type="date"
          value-format="yyyy-MM-dd"
          placeholder="请选择送达时间">
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
          v-hasPermi="['system:delivery:add']"
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
          v-hasPermi="['system:delivery:edit']"
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
          v-hasPermi="['system:delivery:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['system:delivery:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="deliveryList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="配送记录ID" align="center" prop="orderDeliveryId" />
      <el-table-column label="订单ID" align="center" prop="orderMainId" />
      <el-table-column label="骑手ID" align="center" prop="riderId" />
      <el-table-column label="骑手昵称(冗余)" align="center" prop="riderNickname" />
      <el-table-column label="配送费" align="center" prop="deliveryFee" />
      <el-table-column label="实际取货经度" align="center" prop="actualPickLongitude" />
      <el-table-column label="实际取货纬度" align="center" prop="actualPickLatitude" />
      <el-table-column label="实际送达经度" align="center" prop="actualDeliverLongitude" />
      <el-table-column label="实际送达纬度" align="center" prop="actualDeliverLatitude" />
      <el-table-column label="派单时间" align="center" prop="assignTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.assignTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="接单时间" align="center" prop="receiveTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.receiveTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="取货时间" align="center" prop="pickTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.pickTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="送达时间" align="center" prop="deliverTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.deliverTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="配送状态：0-待分配 1-已接单 2-已取货 3-已送达" align="center" prop="deliveryStatus" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['system:delivery:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['system:delivery:remove']"
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

    <!-- 添加或修改订单配送（含实际配送定位）对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="订单ID" prop="orderMainId">
          <el-input v-model="form.orderMainId" placeholder="请输入订单ID" />
        </el-form-item>
        <el-form-item label="骑手ID" prop="riderId">
          <el-input v-model="form.riderId" placeholder="请输入骑手ID" />
        </el-form-item>
        <el-form-item label="骑手昵称(冗余)" prop="riderNickname">
          <el-input v-model="form.riderNickname" placeholder="请输入骑手昵称(冗余)" />
        </el-form-item>
        <el-form-item label="配送费" prop="deliveryFee">
          <el-input v-model="form.deliveryFee" placeholder="请输入配送费" />
        </el-form-item>
        <el-form-item label="实际取货经度" prop="actualPickLongitude">
          <el-input v-model="form.actualPickLongitude" placeholder="请输入实际取货经度" />
        </el-form-item>
        <el-form-item label="实际取货纬度" prop="actualPickLatitude">
          <el-input v-model="form.actualPickLatitude" placeholder="请输入实际取货纬度" />
        </el-form-item>
        <el-form-item label="实际送达经度" prop="actualDeliverLongitude">
          <el-input v-model="form.actualDeliverLongitude" placeholder="请输入实际送达经度" />
        </el-form-item>
        <el-form-item label="实际送达纬度" prop="actualDeliverLatitude">
          <el-input v-model="form.actualDeliverLatitude" placeholder="请输入实际送达纬度" />
        </el-form-item>
        <el-form-item label="派单时间" prop="assignTime">
          <el-date-picker clearable
            v-model="form.assignTime"
            type="date"
            value-format="yyyy-MM-dd"
            placeholder="请选择派单时间">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="接单时间" prop="receiveTime">
          <el-date-picker clearable
            v-model="form.receiveTime"
            type="date"
            value-format="yyyy-MM-dd"
            placeholder="请选择接单时间">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="取货时间" prop="pickTime">
          <el-date-picker clearable
            v-model="form.pickTime"
            type="date"
            value-format="yyyy-MM-dd"
            placeholder="请选择取货时间">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="送达时间" prop="deliverTime">
          <el-date-picker clearable
            v-model="form.deliverTime"
            type="date"
            value-format="yyyy-MM-dd"
            placeholder="请选择送达时间">
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
import { listDelivery, getDelivery, delDelivery, addDelivery, updateDelivery } from "@/api/system/delivery"

export default {
  name: "Delivery",
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
      // 订单配送（含实际配送定位）表格数据
      deliveryList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        orderMainId: null,
        riderId: null,
        riderNickname: null,
        deliveryFee: null,
        actualPickLongitude: null,
        actualPickLatitude: null,
        actualDeliverLongitude: null,
        actualDeliverLatitude: null,
        assignTime: null,
        receiveTime: null,
        pickTime: null,
        deliverTime: null,
        deliveryStatus: null
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        orderMainId: [
          { required: true, message: "订单ID不能为空", trigger: "blur" }
        ],
        deliveryFee: [
          { required: true, message: "配送费不能为空", trigger: "blur" }
        ],
        deliveryStatus: [
          { required: true, message: "配送状态：0-待分配 1-已接单 2-已取货 3-已送达不能为空", trigger: "change" }
        ]
      }
    }
  },
  created() {
    this.getList()
  },
  methods: {
    /** 查询订单配送（含实际配送定位）列表 */
    getList() {
      this.loading = true
      listDelivery(this.queryParams).then(response => {
        this.deliveryList = response.rows
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
        orderDeliveryId: null,
        orderMainId: null,
        riderId: null,
        riderNickname: null,
        deliveryFee: null,
        actualPickLongitude: null,
        actualPickLatitude: null,
        actualDeliverLongitude: null,
        actualDeliverLatitude: null,
        assignTime: null,
        receiveTime: null,
        pickTime: null,
        deliverTime: null,
        deliveryStatus: null
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
      this.ids = selection.map(item => item.orderDeliveryId)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset()
      this.open = true
      this.title = "添加订单配送（含实际配送定位）"
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset()
      const orderDeliveryId = row.orderDeliveryId || this.ids
      getDelivery(orderDeliveryId).then(response => {
        this.form = response.data
        this.open = true
        this.title = "修改订单配送（含实际配送定位）"
      })
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.orderDeliveryId != null) {
            updateDelivery(this.form).then(response => {
              this.$modal.msgSuccess("修改成功")
              this.open = false
              this.getList()
            })
          } else {
            addDelivery(this.form).then(response => {
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
      const orderDeliveryIds = row.orderDeliveryId || this.ids
      this.$modal.confirm('是否确认删除订单配送（含实际配送定位）编号为"' + orderDeliveryIds + '"的数据项？').then(function() {
        return delDelivery(orderDeliveryIds)
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess("删除成功")
      }).catch(() => {})
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('system/delivery/export', {
        ...this.queryParams
      }, `delivery_${new Date().getTime()}.xlsx`)
    }
  }
}
</script>
