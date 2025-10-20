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
      <el-form-item label="是否开启个性化推荐：0-关闭 1-开启" prop="isRecommendEnabled">
        <el-input
          v-model="queryParams.isRecommendEnabled"
          placeholder="请输入是否开启个性化推荐：0-关闭 1-开启"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="推荐频率：1-高频 2-中频 3-低频" prop="recommendFreq">
        <el-input
          v-model="queryParams.recommendFreq"
          placeholder="请输入推荐频率：1-高频 2-中频 3-低频"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="偏好推荐场景" prop="preferredScene">
        <el-input
          v-model="queryParams.preferredScene"
          placeholder="请输入偏好推荐场景"
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
          v-hasPermi="['system:setting:add']"
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
          v-hasPermi="['system:setting:edit']"
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
          v-hasPermi="['system:setting:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['system:setting:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="settingList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="唯一ID" align="center" prop="userRecommendSettingId" />
      <el-table-column label="所属用户ID" align="center" prop="userBaseId" />
      <el-table-column label="是否开启个性化推荐：0-关闭 1-开启" align="center" prop="isRecommendEnabled" />
      <el-table-column label="推荐频率：1-高频 2-中频 3-低频" align="center" prop="recommendFreq" />
      <el-table-column label="屏蔽的标签编码" align="center" prop="shieldedTagCodes" />
      <el-table-column label="偏好推荐场景" align="center" prop="preferredScene" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['system:setting:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['system:setting:remove']"
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

    <!-- 添加或修改用户个性化推荐设置对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="所属用户ID" prop="userBaseId">
          <el-input v-model="form.userBaseId" placeholder="请输入所属用户ID" />
        </el-form-item>
        <el-form-item label="是否开启个性化推荐：0-关闭 1-开启" prop="isRecommendEnabled">
          <el-input v-model="form.isRecommendEnabled" placeholder="请输入是否开启个性化推荐：0-关闭 1-开启" />
        </el-form-item>
        <el-form-item label="推荐频率：1-高频 2-中频 3-低频" prop="recommendFreq">
          <el-input v-model="form.recommendFreq" placeholder="请输入推荐频率：1-高频 2-中频 3-低频" />
        </el-form-item>
        <el-form-item label="屏蔽的标签编码" prop="shieldedTagCodes">
          <el-input v-model="form.shieldedTagCodes" type="textarea" placeholder="请输入内容" />
        </el-form-item>
        <el-form-item label="偏好推荐场景" prop="preferredScene">
          <el-input v-model="form.preferredScene" placeholder="请输入偏好推荐场景" />
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
import { listSetting, getSetting, delSetting, addSetting, updateSetting } from "@/api/system/setting"

export default {
  name: "Setting",
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
      // 用户个性化推荐设置表格数据
      settingList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        userBaseId: null,
        isRecommendEnabled: null,
        recommendFreq: null,
        shieldedTagCodes: null,
        preferredScene: null,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        userBaseId: [
          { required: true, message: "所属用户ID不能为空", trigger: "blur" }
        ],
        isRecommendEnabled: [
          { required: true, message: "是否开启个性化推荐：0-关闭 1-开启不能为空", trigger: "blur" }
        ],
        recommendFreq: [
          { required: true, message: "推荐频率：1-高频 2-中频 3-低频不能为空", trigger: "blur" }
        ],
        updateTime: [
          { required: true, message: "设置更新时间不能为空", trigger: "blur" }
        ],
        createTime: [
          { required: true, message: "创建时间不能为空", trigger: "blur" }
        ]
      }
    }
  },
  created() {
    this.getList()
  },
  methods: {
    /** 查询用户个性化推荐设置列表 */
    getList() {
      this.loading = true
      listSetting(this.queryParams).then(response => {
        this.settingList = response.rows
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
        userRecommendSettingId: null,
        userBaseId: null,
        isRecommendEnabled: null,
        recommendFreq: null,
        shieldedTagCodes: null,
        preferredScene: null,
        updateTime: null,
        createTime: null
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
      this.ids = selection.map(item => item.userRecommendSettingId)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset()
      this.open = true
      this.title = "添加用户个性化推荐设置"
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset()
      const userRecommendSettingId = row.userRecommendSettingId || this.ids
      getSetting(userRecommendSettingId).then(response => {
        this.form = response.data
        this.open = true
        this.title = "修改用户个性化推荐设置"
      })
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.userRecommendSettingId != null) {
            updateSetting(this.form).then(response => {
              this.$modal.msgSuccess("修改成功")
              this.open = false
              this.getList()
            })
          } else {
            addSetting(this.form).then(response => {
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
      const userRecommendSettingIds = row.userRecommendSettingId || this.ids
      this.$modal.confirm('是否确认删除用户个性化推荐设置编号为"' + userRecommendSettingIds + '"的数据项？').then(function() {
        return delSetting(userRecommendSettingIds)
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess("删除成功")
      }).catch(() => {})
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('system/setting/export', {
        ...this.queryParams
      }, `setting_${new Date().getTime()}.xlsx`)
    }
  }
}
</script>
