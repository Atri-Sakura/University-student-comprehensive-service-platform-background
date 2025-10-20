<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="登录账号" prop="username">
        <el-input
          v-model="queryParams.username"
          placeholder="请输入登录账号"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="密码(BCrypt加密)" prop="password">
        <el-input
          v-model="queryParams.password"
          placeholder="请输入密码(BCrypt加密)"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="商家名称" prop="merchantName">
        <el-input
          v-model="queryParams.merchantName"
          placeholder="请输入商家名称"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="商家Logo URL" prop="logo">
        <el-input
          v-model="queryParams.logo"
          placeholder="请输入商家Logo URL"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="店铺地址ID" prop="merchantAddressId">
        <el-input
          v-model="queryParams.merchantAddressId"
          placeholder="请输入店铺地址ID"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="经营范围" prop="businessScope">
        <el-input
          v-model="queryParams.businessScope"
          placeholder="请输入经营范围"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="营业时间" prop="businessHours">
        <el-input
          v-model="queryParams.businessHours"
          placeholder="请输入营业时间"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="配送范围(公里)" prop="deliveryRange">
        <el-input
          v-model="queryParams.deliveryRange"
          placeholder="请输入配送范围(公里)"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="起送金额" prop="minOrderAmount">
        <el-input
          v-model="queryParams.minOrderAmount"
          placeholder="请输入起送金额"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="基础配送费" prop="deliveryFee">
        <el-input
          v-model="queryParams.deliveryFee"
          placeholder="请输入基础配送费"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="营业执照URL" prop="licenseImg">
        <el-input
          v-model="queryParams.licenseImg"
          placeholder="请输入营业执照URL"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="商家评分" prop="rating">
        <el-input
          v-model="queryParams.rating"
          placeholder="请输入商家评分"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="月销量" prop="monthSales">
        <el-input
          v-model="queryParams.monthSales"
          placeholder="请输入月销量"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="店铺经度" prop="longitude">
        <el-input
          v-model="queryParams.longitude"
          placeholder="请输入店铺经度"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="店铺纬度" prop="latitude">
        <el-input
          v-model="queryParams.latitude"
          placeholder="请输入店铺纬度"
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
          v-hasPermi="['system:base:add']"
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
          v-hasPermi="['system:base:edit']"
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
          v-hasPermi="['system:base:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['system:base:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="baseList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="商家唯一ID" align="center" prop="merchantBaseId" />
      <el-table-column label="登录账号" align="center" prop="username" />
      <el-table-column label="密码(BCrypt加密)" align="center" prop="password" />
      <el-table-column label="商家名称" align="center" prop="merchantName" />
      <el-table-column label="商家Logo URL" align="center" prop="logo" />
      <el-table-column label="店铺地址ID" align="center" prop="merchantAddressId" />
      <el-table-column label="经营范围" align="center" prop="businessScope" />
      <el-table-column label="营业时间" align="center" prop="businessHours" />
      <el-table-column label="配送范围(公里)" align="center" prop="deliveryRange" />
      <el-table-column label="起送金额" align="center" prop="minOrderAmount" />
      <el-table-column label="基础配送费" align="center" prop="deliveryFee" />
      <el-table-column label="营业执照URL" align="center" prop="licenseImg" />
      <el-table-column label="商家评分" align="center" prop="rating" />
      <el-table-column label="月销量" align="center" prop="monthSales" />
      <el-table-column label="审核状态：0-待审核 1-通过 2-拒绝" align="center" prop="auditStatus" />
      <el-table-column label="营业状态：0-停业 1-营业" align="center" prop="businessStatus" />
      <el-table-column label="店铺经度" align="center" prop="longitude" />
      <el-table-column label="店铺纬度" align="center" prop="latitude" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['system:base:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['system:base:remove']"
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

    <!-- 添加或修改商家基础信息对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="登录账号" prop="username">
          <el-input v-model="form.username" placeholder="请输入登录账号" />
        </el-form-item>
        <el-form-item label="密码(BCrypt加密)" prop="password">
          <el-input v-model="form.password" placeholder="请输入密码(BCrypt加密)" />
        </el-form-item>
        <el-form-item label="商家名称" prop="merchantName">
          <el-input v-model="form.merchantName" placeholder="请输入商家名称" />
        </el-form-item>
        <el-form-item label="商家Logo URL" prop="logo">
          <el-input v-model="form.logo" placeholder="请输入商家Logo URL" />
        </el-form-item>
        <el-form-item label="店铺地址ID" prop="merchantAddressId">
          <el-input v-model="form.merchantAddressId" placeholder="请输入店铺地址ID" />
        </el-form-item>
        <el-form-item label="经营范围" prop="businessScope">
          <el-input v-model="form.businessScope" placeholder="请输入经营范围" />
        </el-form-item>
        <el-form-item label="营业时间" prop="businessHours">
          <el-input v-model="form.businessHours" placeholder="请输入营业时间" />
        </el-form-item>
        <el-form-item label="配送范围(公里)" prop="deliveryRange">
          <el-input v-model="form.deliveryRange" placeholder="请输入配送范围(公里)" />
        </el-form-item>
        <el-form-item label="起送金额" prop="minOrderAmount">
          <el-input v-model="form.minOrderAmount" placeholder="请输入起送金额" />
        </el-form-item>
        <el-form-item label="基础配送费" prop="deliveryFee">
          <el-input v-model="form.deliveryFee" placeholder="请输入基础配送费" />
        </el-form-item>
        <el-form-item label="营业执照URL" prop="licenseImg">
          <el-input v-model="form.licenseImg" placeholder="请输入营业执照URL" />
        </el-form-item>
        <el-form-item label="商家评分" prop="rating">
          <el-input v-model="form.rating" placeholder="请输入商家评分" />
        </el-form-item>
        <el-form-item label="月销量" prop="monthSales">
          <el-input v-model="form.monthSales" placeholder="请输入月销量" />
        </el-form-item>
        <el-form-item label="店铺经度" prop="longitude">
          <el-input v-model="form.longitude" placeholder="请输入店铺经度" />
        </el-form-item>
        <el-form-item label="店铺纬度" prop="latitude">
          <el-input v-model="form.latitude" placeholder="请输入店铺纬度" />
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
import { listBase, getBase, delBase, addBase, updateBase } from "@/api/system/base"

export default {
  name: "Base",
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
      // 商家基础信息表格数据
      baseList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        username: null,
        password: null,
        merchantName: null,
        logo: null,
        merchantAddressId: null,
        businessScope: null,
        businessHours: null,
        deliveryRange: null,
        minOrderAmount: null,
        deliveryFee: null,
        licenseImg: null,
        rating: null,
        monthSales: null,
        auditStatus: null,
        businessStatus: null,
        longitude: null,
        latitude: null,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        username: [
          { required: true, message: "登录账号不能为空", trigger: "blur" }
        ],
        password: [
          { required: true, message: "密码(BCrypt加密)不能为空", trigger: "blur" }
        ],
        merchantName: [
          { required: true, message: "商家名称不能为空", trigger: "blur" }
        ],
        merchantAddressId: [
          { required: true, message: "店铺地址ID不能为空", trigger: "blur" }
        ],
        businessScope: [
          { required: true, message: "经营范围不能为空", trigger: "blur" }
        ],
        businessHours: [
          { required: true, message: "营业时间不能为空", trigger: "blur" }
        ],
        deliveryRange: [
          { required: true, message: "配送范围(公里)不能为空", trigger: "blur" }
        ],
        minOrderAmount: [
          { required: true, message: "起送金额不能为空", trigger: "blur" }
        ],
        deliveryFee: [
          { required: true, message: "基础配送费不能为空", trigger: "blur" }
        ],
        licenseImg: [
          { required: true, message: "营业执照URL不能为空", trigger: "blur" }
        ],
        rating: [
          { required: true, message: "商家评分不能为空", trigger: "blur" }
        ],
        monthSales: [
          { required: true, message: "月销量不能为空", trigger: "blur" }
        ],
        auditStatus: [
          { required: true, message: "审核状态：0-待审核 1-通过 2-拒绝不能为空", trigger: "change" }
        ],
        businessStatus: [
          { required: true, message: "营业状态：0-停业 1-营业不能为空", trigger: "change" }
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
    /** 查询商家基础信息列表 */
    getList() {
      this.loading = true
      listBase(this.queryParams).then(response => {
        this.baseList = response.rows
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
        merchantBaseId: null,
        username: null,
        password: null,
        merchantName: null,
        logo: null,
        merchantAddressId: null,
        businessScope: null,
        businessHours: null,
        deliveryRange: null,
        minOrderAmount: null,
        deliveryFee: null,
        licenseImg: null,
        rating: null,
        monthSales: null,
        auditStatus: null,
        businessStatus: null,
        longitude: null,
        latitude: null,
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
      this.ids = selection.map(item => item.merchantBaseId)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset()
      this.open = true
      this.title = "添加商家基础信息"
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset()
      const merchantBaseId = row.merchantBaseId || this.ids
      getBase(merchantBaseId).then(response => {
        this.form = response.data
        this.open = true
        this.title = "修改商家基础信息"
      })
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.merchantBaseId != null) {
            updateBase(this.form).then(response => {
              this.$modal.msgSuccess("修改成功")
              this.open = false
              this.getList()
            })
          } else {
            addBase(this.form).then(response => {
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
      const merchantBaseIds = row.merchantBaseId || this.ids
      this.$modal.confirm('是否确认删除商家基础信息编号为"' + merchantBaseIds + '"的数据项？').then(function() {
        return delBase(merchantBaseIds)
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess("删除成功")
      }).catch(() => {})
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('system/base/export', {
        ...this.queryParams
      }, `base_${new Date().getTime()}.xlsx`)
    }
  }
}
</script>
