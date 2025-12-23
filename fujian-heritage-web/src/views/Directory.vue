<template>
  <div class="directory-container">
    <div class="section-header">
      <h2 class="serif-font">非遗名录体系</h2>
      <span>INTANGIBLE CULTURAL HERITAGE DIRECTORY</span>
    </div>

    <div class="toolbar">
      <div class="search-box" v-if="viewMode === 'list'">
        <el-input
            v-model="searchQuery"
            placeholder="搜索非遗项目..."
            class="search-input"
            clearable
            @clear="handleSearch"
            @keyup.enter="handleSearch"
        >
          <template #prefix>
            <el-icon><Search /></el-icon>
          </template>
        </el-input>

        <el-select
            v-model="selectedCategory"
            placeholder="所有类别"
            class="category-select"
            clearable
            @change="handleSearch"
        >
          <el-option label="传统技艺" value="传统技艺" />
          <el-option label="传统美术" value="传统美术" />
          <el-option label="传统音乐" value="传统音乐" />
          <el-option label="民俗" value="民俗" />
          <el-option label="传统戏剧" value="传统戏剧" />
        </el-select>

        <el-button type="primary" color="#A40000" @click="handleSearch">查询</el-button>

        <el-tag
            v-if="searchRegion"
            closable
            type="warning"
            effect="dark"
            style="margin-left: 10px; font-size: 14px; padding: 18px 10px;"
            @close="clearRegion"
        >
          <el-icon style="margin-right: 4px"><Location /></el-icon>
          地区：{{ searchRegion }}
        </el-tag>
      </div>

      <div class="spacer"></div>

      <el-radio-group v-model="viewMode" size="small" fill="#A40000">
        <el-radio-button label="list">
          <el-icon><List /></el-icon> 列表视图
        </el-radio-button>
        <el-radio-button label="chart">
          <el-icon><TrendCharts /></el-icon> 数据概览
        </el-radio-button>
      </el-radio-group>
    </div>

    <div v-show="viewMode === 'list'" class="list-view">
      <el-table :data="tableData" stripe style="width: 100%" v-loading="loading">
        <el-table-column prop="code" label="编号" width="120" />
        <el-table-column prop="name" label="项目名称" min-width="200">
          <template #default="{ row }">
            <span style="font-weight: bold; color: #333;">{{ row.name }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="category" label="类别" width="120">
          <template #default="{ row }">
            <el-tag effect="plain" type="danger" size="small">{{ row.category }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="region" label="申报地区" width="150">
          <template #default="{ row }">
            <span :style="searchRegion && row.region.includes(searchRegion) ? 'color: #A40000; font-weight:bold' : ''">
              {{ row.region }}
            </span>
          </template>
        </el-table-column>
        <el-table-column prop="level" label="级别" width="100" />
        <el-table-column label="操作" width="180">
          <template #default="{ row }">
            <el-button link type="primary" size="small" @click="viewDetail(row)">查看详情</el-button>
            <el-button link type="warning" size="small" icon="Microphone" @click="handleAiExplain(row)">AI解说</el-button>
          </template>
        </el-table-column>
      </el-table>

      <el-empty v-if="!loading && tableData.length === 0" description="暂无符合条件的项目" />

      <div class="pagination" v-if="tableData.length > 0">
        <el-pagination background layout="prev, pager, next" :total="tableData.length" />
      </div>
    </div>

    <div v-if="viewMode === 'chart'" class="chart-view" v-loading="chartLoading">
      <el-row :gutter="20">
        <el-col :span="24" style="margin-bottom: 30px;">
          <el-card shadow="hover" class="chart-card">
            <template #header><span class="chart-title">非遗类别占比</span></template>
            <div id="pieChart" style="width: 100%; height: 500px;"></div>
          </el-card>
        </el-col>
        <el-col :span="24">
          <el-card shadow="hover" class="chart-card">
            <template #header><span class="chart-title">各地区项目数量分布</span></template>
            <div id="barChart" style="width: 100%; height: 500px;"></div>
          </el-card>
        </el-col>
      </el-row>
    </div>
  </div>
</template>

<script setup>
import { ref, watch, nextTick, onMounted, onUnmounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import * as echarts from 'echarts'
import request from '../api/request'
import { ElMessage, ElMessageBox } from 'element-plus'
import { useChatStore } from '../store/chat'
import { Search, Location, List, TrendCharts, Microphone } from '@element-plus/icons-vue' // 确保引入图标

const chatStore = useChatStore()
const route = useRoute()
const router = useRouter()

const viewMode = ref('list')
const searchQuery = ref('')
const selectedCategory = ref('')
const searchRegion = ref('')
const loading = ref(false)
const chartLoading = ref(false)
const tableData = ref([])

// Mock Data
const mockList = [
  { code: "II-24", name: "南音", category: "传统音乐", level: "国家级", region: "泉州市" },
  { code: "VII-15", name: "妈祖信俗", category: "民俗", level: "国家级", region: "莆田市" },
  { code: "VIII-43", name: "福州脱胎漆器髹饰技艺", category: "传统技艺", level: "国家级", region: "福州市" },
  { code: "VIII-44", name: "寿山石雕", category: "传统美术", level: "国家级", region: "福州市" },
  { code: "VIII-67", name: "德化瓷烧制技艺", category: "传统技艺", level: "国家级", region: "泉州市" },
  { code: "VIII-91", name: "大红袍制作技艺", category: "传统技艺", level: "国家级", region: "南平市" },
  { code: "IV-88", name: "泉州提线木偶戏", category: "传统戏剧", level: "国家级", region: "泉州市" },
  { code: "X-32", name: "闽南传统民居营造技艺", category: "传统技艺", level: "国家级", region: "泉州市" },
  { code: "VI-01", name: "漆线雕技艺", category: "传统技艺", level: "国家级", region: "厦门市" },
  { code: "VI-02", name: "中秋博饼", category: "民俗", level: "国家级", region: "厦门市" },
  { code: "VI-03", name: "柘荣剪纸", category: "传统美术", level: "国家级", region: "宁德市" },
  { code: "VI-04", name: "霍童线狮", category: "民俗", level: "国家级", region: "宁德市" },
  { code: "VI-05", name: "沙县小吃制作技艺", category: "传统技艺", level: "国家级", region: "三明市" },
  { code: "VI-06", name: "泰宁梅林戏", category: "传统戏剧", level: "国家级", region: "三明市" },
  { code: "VI-07", name: "连城培田古民居", category: "传统技艺", level: "国家级", region: "龙岩市" },
  { code: "VI-08", name: "闽西汉剧", category: "传统戏剧", level: "国家级", region: "龙岩市" }
]

const mockPieData = [
  { value: 6, name: '传统技艺' },
  { value: 2, name: '传统美术' },
  { value: 1, name: '传统音乐' },
  { value: 3, name: '民俗' },
  { value: 3, name: '传统戏剧' }
]

const mockBarData = {
  xData: ['福州', '泉州', '莆田', '南平', '厦门', '宁德', '三明', '龙岩'],
  yData: [120, 200, 80, 70, 150, 90, 85, 110]
}

const fetchList = async () => {
  loading.value = true
  try {
    const params = {
      name: searchQuery.value,
      category: selectedCategory.value,
      region: searchRegion.value
    }
    const res = await request.get('/heritage/list', { params })
    if (res.code === 200) {
      tableData.value = res.data
    } else {
      throw new Error(res.msg)
    }
  } catch (error) {
    tableData.value = mockList.filter(item => {
      const matchName = !searchQuery.value || item.name.includes(searchQuery.value)
      const matchCat = !selectedCategory.value || item.category === selectedCategory.value
      const matchRegion = !searchRegion.value || (item.region && item.region.includes(searchRegion.value))
      return matchName && matchCat && matchRegion
    })
  } finally {
    loading.value = false
  }
}

const handleSearch = () => {
  fetchList()
}

const clearRegion = () => {
  searchRegion.value = ''
  router.replace({ query: { ...route.query, region: undefined } })
  fetchList()
}

const viewDetail = (row) => {
  ElMessageBox.alert(
      `<p><strong>简介：</strong>${row.description || '暂无详细介绍'}</p>`,
      `${row.name} - 详情`,
      { dangerouslyUseHTMLString: true, confirmButtonText: '关闭' }
  )
}

const handleAiExplain = (row) => {
  const query = `请详细介绍一下福建非遗项目：${row.name}，包括它的历史渊源和艺术特点。`
  // 这里调用 store 里的 openWithQuery，它会触发 sendMessage
  chatStore.openWithQuery(query, 'general')
}

watch(
    () => route.query.region,
    (newRegion) => {
      searchRegion.value = newRegion || ''
      if (viewMode.value === 'list') {
        fetchList()
      }
    },
    { immediate: true }
)

let pieChart = null
let barChart = null

const initCharts = async () => {
  chartLoading.value = true
  let pieData = []
  let barData = []
  try {
    const res = await request.get('/heritage/stats')
    if (res.code === 200) {
      pieData = res.data.pieData
      const rawBarData = res.data.barData
      barData = {
        xData: rawBarData.map(item => item.region),
        yData: rawBarData.map(item => item.count)
      }
    } else {
      throw new Error('API Error')
    }
  } catch (error) {
    pieData = mockPieData
    barData = mockBarData
  } finally {
    renderCharts(pieData, barData)
    chartLoading.value = false
  }
}

const renderCharts = (pieData, barDataObj) => {
  const pieDom = document.getElementById('pieChart')
  const barDom = document.getElementById('barChart')
  if (pieDom) {
    if (pieChart) pieChart.dispose()
    pieChart = echarts.init(pieDom)
    pieChart.setOption({
      tooltip: { trigger: 'item', formatter: '{a} <br/>{b} : {c} ({d}%)' },
      legend: { bottom: '5%', left: 'center' },
      color: ['#A40000', '#D4AF37', '#2F4F4F', '#E6A23C', '#909399'],
      series: [{
        name: '非遗类别',
        type: 'pie',
        radius: ['40%', '65%'],
        center: ['50%', '50%'],
        itemStyle: { borderRadius: 8, borderColor: '#fff', borderWidth: 2 },
        data: pieData
      }]
    })
  }
  if (barDom) {
    if (barChart) barChart.dispose()
    barChart = echarts.init(barDom)
    barChart.setOption({
      tooltip: { trigger: 'axis' },
      xAxis: { type: 'category', data: barDataObj.xData, axisLabel: { rotate: 45 } },
      yAxis: { type: 'value' },
      series: [{
        data: barDataObj.yData,
        type: 'bar',
        barWidth: '30%',
        itemStyle: { color: '#A40000', borderRadius: [4, 4, 0, 0] }
      }]
    })
  }
}

watch(viewMode, (newVal) => {
  if (newVal === 'chart') {
    nextTick(() => {
      initCharts()
    })
  }
})

const resizeHandler = () => {
  pieChart && pieChart.resize()
  barChart && barChart.resize()
}

onMounted(() => {
  window.addEventListener('resize', resizeHandler)
  fetchList()
})

onUnmounted(() => {
  window.removeEventListener('resize', resizeHandler)
  if (pieChart) pieChart.dispose()
  if (barChart) barChart.dispose()
})
</script>

<style scoped>
.directory-container { max-width: 1200px; margin: 20px auto; padding: 0 20px; }
.section-header { text-align: center; margin: 40px 0 30px; }
.section-header h2 { font-size: 28px; color: #333; margin: 0; }
.section-header span { color: #999; font-size: 12px; letter-spacing: 1px; }
.serif-font { font-family: "SimSun", serif; font-weight: bold; }

.toolbar {
  display: flex;
  align-items: center;
  margin-bottom: 20px;
  background: #fff;
  padding: 15px;
  border: 1px solid #E6E0D0;
  border-radius: 4px;
}
.search-box { display: flex; gap: 10px; align-items: center; flex-wrap: wrap; }
.search-input { width: 220px; }
.category-select { width: 160px; }
.spacer { flex: 1; }

.list-view { background: #fff; padding: 20px; border: 1px solid #E6E0D0; min-height: 400px; }
.pagination { margin-top: 20px; display: flex; justify-content: center; }

.chart-view { margin-top: 20px; }
.chart-card { border: 1px solid #E6E0D0; }
.chart-title { font-weight: bold; color: #333; font-size: 18px; }
</style>