<!-- src/page/admin/dish/DishList.vue -->
<template>
  <div class="dish-list-page">
    <!-- 搜索和操作栏 -->
    <div class="list-header">
      <div class="search-box">
        <a-input-search
            v-model:value="searchKeyword"
            placeholder="搜索菜品名称"
            enter-button="搜索"
            style="width: 300px"
            @search="handleSearch"
        />
      </div>

      <div class="action-buttons">
        <a-button type="primary" @click="handleAddDish">
          新增菜品
        </a-button>
      </div>
    </div>

    <!-- 菜品列表表格 -->

    <div class="dish-table">
      <a-table
          :data-source="dishList"
          :columns="columns"
          :row-key="record => record.id"
          :loading="loading"
          bordered
      />
    </div>
  </div>
</template>

<script setup>
import {ref, reactive, onMounted, h} from 'vue'
import { useRouter } from 'vue-router'
import {message, Tag} from 'ant-design-vue'
import { PictureOutlined } from '@ant-design/icons-vue'
import {getDishList} from '@/api/dish'

const router = useRouter()

// 搜索关键词
const searchKeyword = ref('')
// 菜品列表
const dishList = ref([])
// 加载状态
const loading = ref(false)

// 表格列配置
const columns = [
  {
    title: 'ID',
    dataIndex: 'id',
    key: 'id',
    width: 80,
    align: 'center'
  },
  {
    title: '菜品图片',
    dataIndex: 'imgUrl',
    key: 'image',
    width: 100,
    align: 'center'
  },
  {
    title: '菜品名称',
    dataIndex: 'dishName',
    key: 'dishName',
    width: 200
  },
  {
    title: '价格',
    dataIndex: 'price',
    key: 'price',
    width: 120,
    align: 'center'
  },
  {
    title: '分类',
    dataIndex: 'categoryId',
    key: 'category',
    width: 120,
    align: 'center'
  },
  {
    title: '状态',
    dataIndex: 'isAvailable',
    key: 'status',
    width: 100,
    align: 'center',
    customRender: ({ text }) => {
      if (text === null || text === undefined) return '';
      return h(Tag, { color: text === 1 ? 'green' : 'red' }, () => text === 1 ? '正常' : '下架');
    }
  },
  // {
  //   title: '更新时间',
  //   dataIndex: 'updateTime',
  //   key: 'updateTime',
  //   width: 180,
  //   align: 'center'
  // },
  {
    title: '口味',
    key: 'flavors',
    width: 150
  },
  {
    title: '操作',
    key: 'actions',
    width: 150,
    align: 'center'
  }
]

// 加载菜品列表
const loadDishList = async () => {
  loading.value = true
  try {
    const response = await getDishList()

    if (response.data.code === 0) {
      dishList.value = response.data.data || []
      console.log('获取到的数据:', dishList.value)
    } else {
      message.error(response.data.message || '加载菜品列表失败')
      dishList.value = []
    }
  } catch (error) {
    console.error('加载菜品列表失败:', error)
    message.error('加载失败，请稍后重试')
    dishList.value = []
  } finally {
    loading.value = false
  }
}

// 搜索
const handleSearch = () => {
  pagination.current = 1
  loadDishList()
}

// 表格变化（分页）
const handleTableChange = (newPagination) => {
  pagination.current = newPagination.current
  pagination.pageSize = newPagination.pageSize
  loadDishList()
}

// 编辑菜品
const handleEdit = (record) => {
  router.push({
    name: 'dishEdit',
    params: { id: record.id }
  })
}

// 新增菜品
const handleAddDish = () => {
  router.push({
    name: 'dishAdd'
  })
}

// 切换状态
const handleToggleStatus = async (record) => {
  try {
    const response = await dishApi.toggleDishStatus(record.id)
    if (response.code === 200) {
      message.success(record.isAvailable === 1 ? '下架成功' : '上架成功')
      loadDishList()
    } else {
      message.error(response.message || '操作失败')
    }
  } catch (error) {
    console.error('切换状态失败:', error)
    message.error('操作失败，请稍后重试')
  }
}

// 工具函数：图片处理
const getImageUrl = (imgData) => {
  if (!imgData || imgData.length === 0) return ''
  // 如果是字节数组，转换成base64
  if (Array.isArray(imgData)) {
    const binaryString = imgData.reduce((acc, byte) => acc + String.fromCharCode(byte), '')
    return `data:image/jpeg;base64,${btoa(binaryString)}`
  }
  return imgData
}

// 工具函数：截断文本
const truncateText = (text, length) => {
  if (!text) return ''
  return text.length > length ? text.substring(0, length) + '...' : text
}

// 初始化
onMounted(() => {
  loadDishList()
})
</script>

<style scoped>
.dish-list-page {
  padding: 20px;
  background: #fff;
  min-height: 100%;
}

.list-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
  padding-bottom: 16px;
  border-bottom: 1px solid #f0f0f0;
}

.dish-table {
  margin-top: 16px;
}

.dish-image {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 50px;
}

.dish-img {
  width: 40px;
  height: 40px;
  object-fit: cover;
  border-radius: 4px;
}

.no-image {
  width: 40px;
  height: 40px;
  background: #f5f5f5;
  border-radius: 4px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #bfbfbf;
}

.description-text {
  font-size: 12px;
  color: #666;
  margin-top: 4px;
  line-height: 1.4;
}

@media (max-width: 768px) {
  .dish-list-page {
    padding: 16px;
  }

  .list-header {
    flex-direction: column;
    gap: 12px;
    align-items: stretch;
  }

  .search-box {
    display: flex;
    justify-content: center;
  }
}
</style>