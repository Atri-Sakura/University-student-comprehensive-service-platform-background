<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="所属商家ID" prop="merchantBaseId">
        <el-input
          v-model="queryParams.merchantBaseId"
          placeholder="请输入所属商家ID"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="商品名称" prop="goodsName">
        <el-input
          v-model="queryParams.goodsName"
          placeholder="请输入商品名称"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="商品分类" prop="category">
        <el-input
          v-model="queryParams.category"
          placeholder="请输入商品分类"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="商品子分类" prop="subCategory">
        <el-input
          v-model="queryParams.subCategory"
          placeholder="请输入商品子分类"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="单价" prop="price">
        <el-input
          v-model="queryParams.price"
          placeholder="请输入单价"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="原价" prop="originalPrice">
        <el-input
          v-model="queryParams.originalPrice"
          placeholder="请输入原价"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="库存" prop="stock">
        <el-input
          v-model="queryParams.stock"
          placeholder="请输入库存"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="销量" prop="salesCount">
        <el-input
          v-model="queryParams.salesCount"
          placeholder="请输入销量"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="商品平均评分" prop="avgRating">
        <el-input
          v-model="queryParams.avgRating"
          placeholder="请输入商品平均评分"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="评分总次数" prop="ratingCount">
        <el-input
          v-model="queryParams.ratingCount"
          placeholder="请输入评分总次数"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="五星好评率(%)" prop="fiveStarRate">
        <el-input
          v-model="queryParams.fiveStarRate"
          placeholder="请输入五星好评率(%)"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="四星好评率(%)" prop="fourStarRate">
        <el-input
          v-model="queryParams.fourStarRate"
          placeholder="请输入四星好评率(%)"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="三星评价率(%)" prop="threeStarRate">
        <el-input
          v-model="queryParams.threeStarRate"
          placeholder="请输入三星评价率(%)"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="二星评价率(%)" prop="twoStarRate">
        <el-input
          v-model="queryParams.twoStarRate"
          placeholder="请输入二星评价率(%)"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="一星差评率(%)" prop="oneStarRate">
        <el-input
          v-model="queryParams.oneStarRate"
          placeholder="请输入一星差评率(%)"
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
          v-hasPermi="['system:goods:add']"
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
          v-hasPermi="['system:goods:edit']"
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
          v-hasPermi="['system:goods:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['system:goods:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="goodsList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="商品唯一ID" align="center" prop="merchantGoodsId" />
      <el-table-column label="所属商家ID" align="center" prop="merchantBaseId" />
      <el-table-column label="商品名称" align="center" prop="goodsName" />
      <el-table-column label="商品分类" align="center" prop="category" />
      <el-table-column label="商品子分类" align="center" prop="subCategory" />
      <el-table-column label="单价" align="center" prop="price" />
      <el-table-column label="原价" align="center" prop="originalPrice" />
      <el-table-column label="库存" align="center" prop="stock" />
      <el-table-column label="销量" align="center" prop="salesCount" />
      <el-table-column label="商品描述" align="center" prop="description" />
      <el-table-column label="商品标签编码" align="center" prop="tagCodes" />
      <el-table-column label="状态：0-下架 1-上架" align="center" prop="status" />
      <el-table-column label="商品平均评分" align="center" prop="avgRating" />
      <el-table-column label="评分总次数" align="center" prop="ratingCount" />
      <el-table-column label="五星好评率(%)" align="center" prop="fiveStarRate" />
      <el-table-column label="四星好评率(%)" align="center" prop="fourStarRate" />
      <el-table-column label="三星评价率(%)" align="center" prop="threeStarRate" />
      <el-table-column label="二星评价率(%)" align="center" prop="twoStarRate" />
      <el-table-column label="一星差评率(%)" align="center" prop="oneStarRate" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['system:goods:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['system:goods:remove']"
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

    <!-- 添加或修改商品对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="所属商家ID" prop="merchantBaseId">
          <el-input v-model="form.merchantBaseId" placeholder="请输入所属商家ID" />
        </el-form-item>
        <el-form-item label="商品名称" prop="goodsName">
          <el-input v-model="form.goodsName" placeholder="请输入商品名称" />
        </el-form-item>
        <el-form-item label="商品分类" prop="category">
          <el-input v-model="form.category" placeholder="请输入商品分类" />
        </el-form-item>
        <el-form-item label="商品子分类" prop="subCategory">
          <el-input v-model="form.subCategory" placeholder="请输入商品子分类" />
        </el-form-item>
        <el-form-item label="单价" prop="price">
          <el-input v-model="form.price" placeholder="请输入单价" />
        </el-form-item>
        <el-form-item label="原价" prop="originalPrice">
          <el-input v-model="form.originalPrice" placeholder="请输入原价" />
        </el-form-item>
        <el-form-item label="库存" prop="stock">
          <el-input v-model="form.stock" placeholder="请输入库存" />
        </el-form-item>
        <el-form-item label="销量" prop="salesCount">
          <el-input v-model="form.salesCount" placeholder="请输入销量" />
        </el-form-item>
        <el-form-item label="商品描述" prop="description">
          <el-input v-model="form.description" type="textarea" placeholder="请输入内容" />
        </el-form-item>
        <el-form-item label="商品标签编码" prop="tagCodes">
          <el-input v-model="form.tagCodes" type="textarea" placeholder="请输入内容" />
        </el-form-item>
        <el-form-item label="商品平均评分" prop="avgRating">
          <el-input v-model="form.avgRating" placeholder="请输入商品平均评分" />
        </el-form-item>
        <el-form-item label="评分总次数" prop="ratingCount">
          <el-input v-model="form.ratingCount" placeholder="请输入评分总次数" />
        </el-form-item>
        <el-form-item label="五星好评率(%)" prop="fiveStarRate">
          <el-input v-model="form.fiveStarRate" placeholder="请输入五星好评率(%)" />
        </el-form-item>
        <el-form-item label="四星好评率(%)" prop="fourStarRate">
          <el-input v-model="form.fourStarRate" placeholder="请输入四星好评率(%)" />
        </el-form-item>
        <el-form-item label="三星评价率(%)" prop="threeStarRate">
          <el-input v-model="form.threeStarRate" placeholder="请输入三星评价率(%)" />
        </el-form-item>
        <el-form-item label="二星评价率(%)" prop="twoStarRate">
          <el-input v-model="form.twoStarRate" placeholder="请输入二星评价率(%)" />
        </el-form-item>
        <el-form-item label="一星差评率(%)" prop="oneStarRate">
          <el-input v-model="form.oneStarRate" placeholder="请输入一星差评率(%)" />
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
import { listGoods, getGoods, delGoods, addGoods, updateGoods } from "@/api/system/goods"

export default {
  name: "Goods",
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
      // 商品表格数据
      goodsList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        merchantBaseId: null,
        goodsName: null,
        category: null,
        subCategory: null,
        price: null,
        originalPrice: null,
        stock: null,
        salesCount: null,
        description: null,
        tagCodes: null,
        status: null,
        avgRating: null,
        ratingCount: null,
        fiveStarRate: null,
        fourStarRate: null,
        threeStarRate: null,
        twoStarRate: null,
        oneStarRate: null,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        merchantBaseId: [
          { required: true, message: "所属商家ID不能为空", trigger: "blur" }
        ],
        goodsName: [
          { required: true, message: "商品名称不能为空", trigger: "blur" }
        ],
        category: [
          { required: true, message: "商品分类不能为空", trigger: "blur" }
        ],
        price: [
          { required: true, message: "单价不能为空", trigger: "blur" }
        ],
        stock: [
          { required: true, message: "库存不能为空", trigger: "blur" }
        ],
        salesCount: [
          { required: true, message: "销量不能为空", trigger: "blur" }
        ],
        status: [
          { required: true, message: "状态：0-下架 1-上架不能为空", trigger: "change" }
        ],
        avgRating: [
          { required: true, message: "商品平均评分不能为空", trigger: "blur" }
        ],
        ratingCount: [
          { required: true, message: "评分总次数不能为空", trigger: "blur" }
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
    /** 查询商品列表 */
    getList() {
      this.loading = true
      listGoods(this.queryParams).then(response => {
        this.goodsList = response.rows
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
        merchantGoodsId: null,
        merchantBaseId: null,
        goodsName: null,
        category: null,
        subCategory: null,
        price: null,
        originalPrice: null,
        stock: null,
        salesCount: null,
        description: null,
        tagCodes: null,
        status: null,
        avgRating: null,
        ratingCount: null,
        fiveStarRate: null,
        fourStarRate: null,
        threeStarRate: null,
        twoStarRate: null,
        oneStarRate: null,
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
      this.ids = selection.map(item => item.merchantGoodsId)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset()
      this.open = true
      this.title = "添加商品"
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset()
      const merchantGoodsId = row.merchantGoodsId || this.ids
      getGoods(merchantGoodsId).then(response => {
        this.form = response.data
        this.open = true
        this.title = "修改商品"
      })
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.merchantGoodsId != null) {
            updateGoods(this.form).then(response => {
              this.$modal.msgSuccess("修改成功")
              this.open = false
              this.getList()
            })
          } else {
            addGoods(this.form).then(response => {
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
      const merchantGoodsIds = row.merchantGoodsId || this.ids
      this.$modal.confirm('是否确认删除商品编号为"' + merchantGoodsIds + '"的数据项？').then(function() {
        return delGoods(merchantGoodsIds)
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess("删除成功")
      }).catch(() => {})
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('system/goods/export', {
        ...this.queryParams
      }, `goods_${new Date().getTime()}.xlsx`)
    }
  }
}
</script>
