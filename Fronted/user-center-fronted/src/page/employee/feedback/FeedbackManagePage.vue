<template>
  <div class="feedback-manage">
    <el-card>
      <template #header>
        <div class="header">
          <span>用户反馈管理</span>
          <el-button type="primary" @click="refresh">刷新</el-button>
        </div>
      </template>

      <el-table :data="feedbackList" v-loading="loading">
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="userName" label="用户" />
        <el-table-column prop="type" label="类型">
          <template #default="{row}">
            <el-tag :type="getTypeTag(row.type)">
              {{ getTypeText(row.type) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="content" label="内容" />
        <el-table-column prop="status" label="状态">
          <template #default="{row}">
            <el-tag :type="row.status === 'PENDING' ? 'warning' : 'success'">
              {{ row.status === 'PENDING' ? '待处理' : '已处理' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createdAt" label="提交时间" />
        <el-table-column label="操作" width="150">
          <template #default="{row}">
            <el-button size="small" @click="viewDetail(row.id)">
              查看
            </el-button>
            <el-button
                size="small"
                type="danger"
                @click="deleteFeedback(row.id)"
            >
              删除
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
  </div>
</template>