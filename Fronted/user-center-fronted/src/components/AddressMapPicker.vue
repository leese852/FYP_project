<template>
  <div class="map-picker-container">
    <!-- 搜索框 -->
    <div class="search-box">
      <a-input-search
          v-model:value="searchKeyword"
          placeholder="搜索地址"
          @search="handleSearch"
          enter-button
      />
    </div>

    <!-- 地图容器 -->
    <LocationPicker
        ref="locationPickerRef"
        type="gmap"
        :map-key="googleMapsApiKey"
        :center="currentLocation"
        :zoom="15"
        locale="zh-CN"
        @location-selected="handleLocationSelected"
        @location-changed="handleLocationChanged"
    />

    <!-- 显示中文地址信息 -->
    <div class="location-info" v-if="selectedAddress">
      <a-descriptions :column="1" size="small" bordered>
        <a-descriptions-item label="当前位置">
          {{ selectedAddress.formattedAddress }}
        </a-descriptions-item>
      </a-descriptions>
    </div>

    <!-- 确认按钮 -->
    <div class="action-buttons">
      <a-button @click="handleCancel">取消</a-button>
      <a-button type="primary" @click="handleConfirm" :disabled="!selectedAddress">
        确认使用此地址
      </a-button>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { LocationPicker } from 'vue3-jag-location-picker'

const props = defineProps({
  initialAddress: {
    type: String,
    default: ''
  },
  initialLng: {
    type: Number,
    default: 114.1694
  },
  initialLat: {
    type: Number,
    default: 22.3193
  }
})

const emit = defineEmits(['confirm', 'cancel'])

const searchKeyword = ref('')
const selectedAddress = ref(null)
const currentLocation = ref([props.initialLng, props.initialLat])

const googleMapsApiKey = process.env.VUE_APP_GOOGLE_MAPS_API_KEY

// 🔥 中文地址格式化函数
const formatChineseAddress = (result) => {
  // 如果没有 address_components，返回原始地址
  if (!result.address_components) {
    return result.formattedAddress || result.address || ''
  }

  const components = result.address_components
  const addressParts = []

  // 地址层级顺序（从大到小）
  const order = [
    { type: 'country', name: '国家' },                    // 国家
    { type: 'administrative_area_level_1', name: '省' },  // 省/直辖市
    { type: 'administrative_area_level_2', name: '市' },  // 市
    { type: 'administrative_area_level_3', name: '区' },  // 区/县
    { type: 'locality', name: '城市' },                   // 城市
    { type: 'sublocality', name: '地区' },                // 子地区
    { type: 'route', name: '街道' },                      // 街道
    { type: 'street_number', name: '门牌号' }             // 门牌号
  ]

  // 按顺序提取地址组件
  for (const item of order) {
    const comp = components.find(c => c.types.includes(item.type))
    if (comp && comp.long_name && !addressParts.includes(comp.long_name)) {
      // 过滤掉重复的国家名称
      if (item.type === 'country' && comp.long_name === '中国') {
        addressParts.push(comp.long_name)
      } else if (item.type !== 'country') {
        addressParts.push(comp.long_name)
      }
    }
  }

  // 如果没有提取到地址组件，尝试使用 formattedAddress
  if (addressParts.length === 0) {
    return result.formattedAddress || result.address || ''
  }

  // 组合成完整地址
  let formattedAddress = addressParts.join('')

  // 获取地点名称（如建筑名、商铺名）
  if (result.name && !formattedAddress.includes(result.name)) {
    formattedAddress = result.name + ' ' + formattedAddress
  }

  return formattedAddress
}

// 位置选择事件 - 使用中文格式化
const handleLocationSelected = (result) => {
  console.log('选中的原始位置:', result)

  // 格式化中文地址
  const chineseAddress = formatChineseAddress(result)

  selectedAddress.value = {
    formattedAddress: chineseAddress,
    originalAddress: result.formattedAddress,
    lng: result.latlng[0],
    lat: result.latlng[1],
    placeName: result.name || '',
    addressComponents: result.address_components
  }

  console.log('格式化后的中文地址:', chineseAddress)
}

const handleLocationChanged = (result) => {
  console.log('位置变化:', result)
}

// 搜索地址
const handleSearch = () => {
  if (!searchKeyword.value.trim()) return
  // LocationPicker 组件内部会处理搜索
  console.log('搜索地址:', searchKeyword.value)
}

// 确认选择 - 传递中文地址
const handleConfirm = () => {
  if (selectedAddress.value) {
    emit('confirm', {
      formattedAddress: selectedAddress.value.formattedAddress,
      lng: selectedAddress.value.lng,
      lat: selectedAddress.value.lat
    })
  }
}

// 取消
const handleCancel = () => {
  emit('cancel')
}
</script>

<style scoped>
.map-picker-container {
  display: flex;
  flex-direction: column;
  height: 550px;
}

.search-box {
  margin-bottom: 12px;
  padding: 0 8px;
}

.location-info {
  margin-top: 12px;
  padding: 8px;
  background: #f5f5f5;
  border-radius: 8px;
}

.action-buttons {
  display: flex;
  justify-content: flex-end;
  gap: 12px;
  margin-top: 16px;
}
</style>