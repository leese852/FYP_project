<template>
  <a-card title="菜品管理">
    <!-- 搜索栏 -->
    <div class="mb-4">
      <a-input-search
          v-model:value="searchName"
          placeholder="搜索菜品名称"
          @search="handleSearch"
          style="width: 300px"
      />
      <a-button type="primary" @click="showAddModal" class="ml-4">
        <plus-outlined />
        添加菜品
      </a-button>
    </div>

    <!-- 表格 -->
    <a-table
        :columns="columns"
        :data-source="dishList"
        :loading="loading"
        :pagination="pagination"
        row-key="id"
    >
      <!-- 图片列 -->
      <template #bodyCell="{ column, record }">
        <template v-if="column.key === 'imgUrl'">
          <img
              v-if="record.imgUrl"
              :src="getImageUrl(record.imgUrl)"
              style="width: 50px; height: 50px; object-fit: cover"
          />
          <span v-else style="color: #999">无图</span>
        </template>

        <!-- 价格列 -->
        <template v-if="column.key === 'price'">
          ¥{{ record.price }}
        </template>

        <!-- 状态列 -->
        <template v-if="column.key === 'isAvailable'">
          <a-tag :color="record.isAvailable ? 'green' : 'red'">
            {{ record.isAvailable ? '上架' : '下架' }}
          </a-tag>
        </template>

        <!-- 操作列 -->
        <template v-if="column.key === 'action'">
          <a-space>
            <a-button size="small" @click="handleEdit(record)">编辑</a-button>
            <a-button
                size="small"
                :type="record.isAvailable ? 'dashed' : 'primary'"
                @click="toggleStatus(record)"
            >
              {{ record.isAvailable ? '下架' : '上架' }}
            </a-button>
            <a-popconfirm
                title="确定删除？"
                @confirm="handleDelete(record.id)"
            >
              <a-button size="small" danger>删除</a-button>
            </a-popconfirm>
          </a-space>
        </template>
      </template>
    </a-table>
  </a-card>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { message } from 'ant-design-vue'
import { PlusOutlined } from '@ant-design/icons-vue'
import { getDishList, toggleDishStatus, deleteDishBatch } from '@/api/dish'

const router = useRouter()
const searchName = ref('')
const dishList = ref<any[]>([])
const loading = ref(false)

// 分页
const pagination = ref({
  current: 1,
  pageSize: 10,
  total: 0,
  showSizeChanger: true,
  showTotal: (total: number) => `共 ${total} 条`,
})

// 表格列
const columns = [
  {
    title: '菜品图片',
    dataIndex: 'imgUrl',
    key: 'imgUrl',
    width: 80,
  },
  {
    title: '菜品名称',
    dataIndex: 'dishName',
    key: 'dishName',
  },
  {
    title: '价格',
    dataIndex: 'price',
    key: 'price',
    width: 100,
  },
  {
    title: '状态',
    dataIndex: 'isAvailable',
    key: 'isAvailable',
    width: 100,
  },
  {
    title: '更新时间',
    dataIndex: 'updateTime',
    key: 'updateTime',
    width: 180,
  },
  {
    title: '操作',
    key: 'action',
    width: 200,
  },
]

// 加载菜品列表
const loadDishList = async () => {
  try {
    loading.value = true
    const res = await getDishList(searchName.value)
    dishList.value = res.data || res
  } catch (error) {
    message.error('加载失败')
  } finally {
    loading.value = false
  }
}

// 搜索
const handleSearch = () => {
  loadDishList()
}

// 图片URL处理
const getImageUrl = (imgUrl: any) => {
  if (!imgUrl) return ''
  if (typeof imgUrl === 'string') return imgUrl
  // 处理字节数组
  const blob = new Blob([new Uint8Array(imgUrl)], { type: 'image/jpeg' })
  return URL.createObjectURL(blob)
}

// 添加菜品
const showAddModal = () => {
  router.push('/admin/dish/add')
}

// 编辑菜品
const handleEdit = (record: any) => {
  router.push(`/admin/dish/edit/${record.id}`)
}

// 切换状态
const toggleStatus = async (record: any) => {
  try {
    await toggleDishStatus(record.id)
    message.success('操作成功')
    loadDishList()
  } catch {
    message.error('操作失败')
  }
}

// 删除菜品
const handleDelete = async (id: number) => {
  try {
    await deleteDishBatch([id])
    message.success('删除成功')
    loadDishList()
  } catch {
    message.error('删除失败')
  }
}

// 初始化
onMounted(() => {
  loadDishList()
})
</script>

<style scoped>
.mb-4 {
  margin-bottom: 16px;
}
.ml-4 {
  margin-left: 16px;
}
</style>