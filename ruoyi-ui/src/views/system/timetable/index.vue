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
      <el-form-item label="课程名称" prop="courseName">
        <el-input
          v-model="queryParams.courseName"
          placeholder="请输入课程名称"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="授课教师姓名" prop="teacherName">
        <el-input
          v-model="queryParams.teacherName"
          placeholder="请输入授课教师姓名"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="上课教室" prop="classRoom">
        <el-input
          v-model="queryParams.classRoom"
          placeholder="请输入上课教室"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="星期(1-周一 7-周日)" prop="weekDay">
        <el-input
          v-model="queryParams.weekDay"
          placeholder="请输入星期(1-周一 7-周日)"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="开始节次" prop="startPeriod">
        <el-input
          v-model="queryParams.startPeriod"
          placeholder="请输入开始节次"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="结束节次" prop="endPeriod">
        <el-input
          v-model="queryParams.endPeriod"
          placeholder="请输入结束节次"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="开始时间" prop="startTime">
        <el-input
          v-model="queryParams.startTime"
          placeholder="请输入开始时间"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="结束时间" prop="endTime">
        <el-input
          v-model="queryParams.endTime"
          placeholder="请输入结束时间"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="课程开始日期" prop="startDate">
        <el-date-picker clearable
          v-model="queryParams.startDate"
          type="date"
          value-format="yyyy-MM-dd"
          placeholder="请选择课程开始日期">
        </el-date-picker>
      </el-form-item>
      <el-form-item label="课程结束日期" prop="endDate">
        <el-date-picker clearable
          v-model="queryParams.endDate"
          type="date"
          value-format="yyyy-MM-dd"
          placeholder="请选择课程结束日期">
        </el-date-picker>
      </el-form-item>
      <el-form-item label="导入来源" prop="importSource">
        <el-input
          v-model="queryParams.importSource"
          placeholder="请输入导入来源"
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
          v-hasPermi="['system:timetable:add']"
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
          v-hasPermi="['system:timetable:edit']"
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
          v-hasPermi="['system:timetable:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['system:timetable:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="timetableList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="课表记录唯一ID" align="center" prop="userTimetableId" />
      <el-table-column label="所属用户ID" align="center" prop="userBaseId" />
      <el-table-column label="课程名称" align="center" prop="courseName" />
      <el-table-column label="授课教师姓名" align="center" prop="teacherName" />
      <el-table-column label="上课教室" align="center" prop="classRoom" />
      <el-table-column label="星期(1-周一 7-周日)" align="center" prop="weekDay" />
      <el-table-column label="开始节次" align="center" prop="startPeriod" />
      <el-table-column label="结束节次" align="center" prop="endPeriod" />
      <el-table-column label="开始时间" align="center" prop="startTime" />
      <el-table-column label="结束时间" align="center" prop="endTime" />
      <el-table-column label="课程开始日期" align="center" prop="startDate" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.startDate, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="课程结束日期" align="center" prop="endDate" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.endDate, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="导入来源" align="center" prop="importSource" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['system:timetable:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['system:timetable:remove']"
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

    <!-- 添加或修改个人课对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="所属用户ID" prop="userBaseId">
          <el-input v-model="form.userBaseId" placeholder="请输入所属用户ID" />
        </el-form-item>
        <el-form-item label="课程名称" prop="courseName">
          <el-input v-model="form.courseName" placeholder="请输入课程名称" />
        </el-form-item>
        <el-form-item label="授课教师姓名" prop="teacherName">
          <el-input v-model="form.teacherName" placeholder="请输入授课教师姓名" />
        </el-form-item>
        <el-form-item label="上课教室" prop="classRoom">
          <el-input v-model="form.classRoom" placeholder="请输入上课教室" />
        </el-form-item>
        <el-form-item label="星期(1-周一 7-周日)" prop="weekDay">
          <el-input v-model="form.weekDay" placeholder="请输入星期(1-周一 7-周日)" />
        </el-form-item>
        <el-form-item label="开始节次" prop="startPeriod">
          <el-input v-model="form.startPeriod" placeholder="请输入开始节次" />
        </el-form-item>
        <el-form-item label="结束节次" prop="endPeriod">
          <el-input v-model="form.endPeriod" placeholder="请输入结束节次" />
        </el-form-item>
        <el-form-item label="开始时间" prop="startTime">
          <el-input v-model="form.startTime" placeholder="请输入开始时间" />
        </el-form-item>
        <el-form-item label="结束时间" prop="endTime">
          <el-input v-model="form.endTime" placeholder="请输入结束时间" />
        </el-form-item>
        <el-form-item label="课程开始日期" prop="startDate">
          <el-date-picker clearable
            v-model="form.startDate"
            type="date"
            value-format="yyyy-MM-dd"
            placeholder="请选择课程开始日期">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="课程结束日期" prop="endDate">
          <el-date-picker clearable
            v-model="form.endDate"
            type="date"
            value-format="yyyy-MM-dd"
            placeholder="请选择课程结束日期">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="导入来源" prop="importSource">
          <el-input v-model="form.importSource" placeholder="请输入导入来源" />
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
import { listTimetable, getTimetable, delTimetable, addTimetable, updateTimetable } from "@/api/system/timetable"

export default {
  name: "Timetable",
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
      // 个人课表格数据
      timetableList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        userBaseId: null,
        courseName: null,
        teacherName: null,
        classRoom: null,
        weekDay: null,
        startPeriod: null,
        endPeriod: null,
        startTime: null,
        endTime: null,
        startDate: null,
        endDate: null,
        importSource: null,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        userBaseId: [
          { required: true, message: "所属用户ID不能为空", trigger: "blur" }
        ],
        courseName: [
          { required: true, message: "课程名称不能为空", trigger: "blur" }
        ],
        classRoom: [
          { required: true, message: "上课教室不能为空", trigger: "blur" }
        ],
        weekDay: [
          { required: true, message: "星期(1-周一 7-周日)不能为空", trigger: "blur" }
        ],
        startPeriod: [
          { required: true, message: "开始节次不能为空", trigger: "blur" }
        ],
        endPeriod: [
          { required: true, message: "结束节次不能为空", trigger: "blur" }
        ],
        startTime: [
          { required: true, message: "开始时间不能为空", trigger: "blur" }
        ],
        endTime: [
          { required: true, message: "结束时间不能为空", trigger: "blur" }
        ],
        startDate: [
          { required: true, message: "课程开始日期不能为空", trigger: "blur" }
        ],
        endDate: [
          { required: true, message: "课程结束日期不能为空", trigger: "blur" }
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
    /** 查询个人课列表 */
    getList() {
      this.loading = true
      listTimetable(this.queryParams).then(response => {
        this.timetableList = response.rows
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
        userTimetableId: null,
        userBaseId: null,
        courseName: null,
        teacherName: null,
        classRoom: null,
        weekDay: null,
        startPeriod: null,
        endPeriod: null,
        startTime: null,
        endTime: null,
        startDate: null,
        endDate: null,
        importSource: null,
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
      this.ids = selection.map(item => item.userTimetableId)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset()
      this.open = true
      this.title = "添加个人课"
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset()
      const userTimetableId = row.userTimetableId || this.ids
      getTimetable(userTimetableId).then(response => {
        this.form = response.data
        this.open = true
        this.title = "修改个人课"
      })
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.userTimetableId != null) {
            updateTimetable(this.form).then(response => {
              this.$modal.msgSuccess("修改成功")
              this.open = false
              this.getList()
            })
          } else {
            addTimetable(this.form).then(response => {
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
      const userTimetableIds = row.userTimetableId || this.ids
      this.$modal.confirm('是否确认删除个人课编号为"' + userTimetableIds + '"的数据项？').then(function() {
        return delTimetable(userTimetableIds)
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess("删除成功")
      }).catch(() => {})
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('system/timetable/export', {
        ...this.queryParams
      }, `timetable_${new Date().getTime()}.xlsx`)
    }
  }
}
</script>
