<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="骑手ID" prop="riderBaseId">
        <el-input
          v-model="queryParams.riderBaseId"
          placeholder="请输入骑手ID"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="关联订单ID" prop="orderId">
        <el-input
          v-model="queryParams.orderId"
          placeholder="请输入关联订单ID"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="接单时间" prop="receiveTime">
        <el-date-picker clearable
          v-model="queryParams.receiveTime"
          type="date"
          value-format="yyyy-MM-dd"
          placeholder="请选择接单时间">
        </el-date-picker>
      </el-form-item>
      <el-form-item label="取货时间" prop="pickUpTime">
        <el-date-picker clearable
          v-model="queryParams.pickUpTime"
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
      <el-form-item label="异常原因" prop="abnormalReason">
        <el-input
          v-model="queryParams.abnormalReason"
          placeholder="请输入异常原因"
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
          v-hasPermi="['system:rel:add']"
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
          v-hasPermi="['system:rel:edit']"
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
          v-hasPermi="['system:rel:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['system:rel:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="relList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="关联记录唯一ID" align="center" prop="riderOrderRelId" />
      <el-table-column label="骑手ID" align="center" prop="riderBaseId" />
      <el-table-column label="关联订单ID" align="center" prop="orderId" />
      <el-table-column label="订单类型：1-外卖单 2-跑腿单" align="center" prop="orderType" />
      <el-table-column label="接单时间" align="center" prop="receiveTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.receiveTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="取货时间" align="center" prop="pickUpTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.pickUpTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="送达时间" align="center" prop="deliverTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.deliverTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="配送状态：1-待取货 2-配送中 3-已送达 4-异常取消" align="center" prop="deliveryStatus" />
      <el-table-column label="异常原因" align="center" prop="abnormalReason" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['system:rel:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['system:rel:remove']"
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

    <!-- 添加或修改骑手接单关联对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="骑手ID" prop="riderBaseId">
          <el-input v-model="form.riderBaseId" placeholder="请输入骑手ID" />
        </el-form-item>
        <el-form-item label="关联订单ID" prop="orderId">
          <el-input v-model="form.orderId" placeholder="请输入关联订单ID" />
        </el-form-item>
        <el-form-item label="接单时间" prop="receiveTime">
          <el-date-picker clearable
            v-model="form.receiveTime"
            type="date"
            value-format="yyyy-MM-dd"
            placeholder="请选择接单时间">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="取货时间" prop="pickUpTime">
          <el-date-picker clearable
            v-model="form.pickUpTime"
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
        <el-form-item label="异常原因" prop="abnormalReason">
          <el-input v-model="form.abnormalReason" placeholder="请输入异常原因" />
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
import { listRel, getRel, delRel, addRel, updateRel } from "@/api/system/rel"

export default {
  name: "Rel",
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
      // 骑手接单关联表格数据
      relList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        riderBaseId: null,
        orderId: null,
        orderType: null,
        receiveTime: null,
        pickUpTime: null,
        deliverTime: null,
        deliveryStatus: null,
        abnormalReason: null,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        riderBaseId: [
          { required: true, message: "骑手ID不能为空", trigger: "blur" }
        ],
        orderId: [
          { required: true, message: "关联订单ID不能为空", trigger: "blur" }
        ],
        orderType: [
          { required: true, message: "订单类型：1-外卖单 2-跑腿单不能为空", trigger: "change" }
        ],
        receiveTime: [
          { required: true, message: "接单时间不能为空", trigger: "blur" }
        ],
        deliveryStatus: [
          { required: true, message: "配送状态：1-待取货 2-配送中 3-已送达 4-异常取消不能为空", trigger: "change" }
        ],
        createTime: [
          { required: true, message: "创建时间不能为空", trigger: "blur" }
        ],
        updateTime: [
          { required: true, message: "最后更新时间不能为空", trigger: "blur" }
        ]
      }
    }
  },
  created() {
    this.getList()
  },
  methods: {
    /** 查询骑手接单关联列表 */
    getList() {
      this.loading = true
      listRel(this.queryParams).then(response => {
        this.relList = response.rows
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
        riderOrderRelId: null,
        riderBaseId: null,
        orderId: null,
        orderType: null,
        receiveTime: null,
        pickUpTime: null,
        deliverTime: null,
        deliveryStatus: null,
        abnormalReason: null,
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
      this.ids = selection.map(item => item.riderOrderRelId)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset()
      this.open = true
      this.title = "添加骑手接单关联"
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset()
      const riderOrderRelId = row.riderOrderRelId || this.ids
      getRel(riderOrderRelId).then(response => {
        this.form = response.data
        this.open = true
        this.title = "修改骑手接单关联"
      })
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.riderOrderRelId != null) {
            updateRel(this.form).then(response => {
              this.$modal.msgSuccess("修改成功")
              this.open = false
              this.getList()
            })
          } else {
            addRel(this.form).then(response => {
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
      const riderOrderRelIds = row.riderOrderRelId || this.ids
      this.$modal.confirm('是否确认删除骑手接单关联编号为"' + riderOrderRelIds + '"的数据项？').then(function() {
        return delRel(riderOrderRelIds)
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess("删除成功")
      }).catch(() => {})
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('system/rel/export', {
        ...this.queryParams
      }, `rel_${new Date().getTime()}.xlsx`)
    }
  }
}
</script>
