<template>
  <div class="knowledge-analysis">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>智能统计看板</span>
        </div>
      </template>

      <!-- 概览卡片 -->
      <el-row :gutter="20" class="overview-cards">
        <el-col :span="6">
          <el-card shadow="hover" class="stat-card">
            <div class="stat-icon" style="background-color: #409EFF;">
              <el-icon><Document /></el-icon>
            </div>
            <div class="stat-content">
              <div class="stat-value">{{ statistics.documentStatistics?.totalDocuments || 0 }}</div>
              <div class="stat-label">文档总数</div>
            </div>
          </el-card>
        </el-col>
        
        <el-col :span="6">
          <el-card shadow="hover" class="stat-card">
            <div class="stat-icon" style="background-color: #67C23A;">
              <el-icon><VideoPlay /></el-icon>
            </div>
            <div class="stat-content">
              <div class="stat-value">{{ statistics.viewStatistics?.totalViews || 0 }}</div>
              <div class="stat-label">总浏览量</div>
            </div>
          </el-card>
        </el-col>
        
        <el-col :span="6">
          <el-card shadow="hover" class="stat-card">
            <div class="stat-icon" style="background-color: #E6A23C;">
              <el-icon><Download /></el-icon>
            </div>
            <div class="stat-content">
              <div class="stat-value">{{ statistics.downloadStatistics?.totalDownloads || 0 }}</div>
              <div class="stat-label">总下载量</div>
            </div>
          </el-card>
        </el-col>
        
        <el-col :span="6">
          <el-card shadow="hover" class="stat-card">
            <div class="stat-icon" style="background-color: #F56C6C;">
              <el-icon><User /></el-icon>
            </div>
            <div class="stat-content">
              <div class="stat-value">{{ statistics.viewStatistics?.uniqueVisitors || 0 }}</div>
              <div class="stat-label">独立访客</div>
            </div>
          </el-card>
        </el-col>
      </el-row>

      <!-- 资源类型分布 -->
      <el-row :gutter="20" class="chart-row">
        <el-col :span="12">
          <el-card>
            <template #header>
              <span>资源类型分布</span>
            </template>
            <div id="typeDistribution" class="chart-container"></div>
          </el-card>
        </el-col>
        
        <el-col :span="12">
          <el-card>
            <template #header>
              <span>文档状态分布</span>
            </template>
            <div id="statusDistribution" class="chart-container"></div>
          </el-card>
        </el-col>
      </el-row>

      <!-- 趋势图表 -->
      <el-row :gutter="20" class="chart-row">
        <el-col :span="12">
          <el-card>
            <template #header>
              <span>浏览趋势（最近 7 天）</span>
            </template>
            <div id="viewTrend" class="chart-container"></div>
          </el-card>
        </el-col>
        
        <el-col :span="12">
          <el-card>
            <template #header>
              <span>下载趋势（最近 7 天）</span>
            </template>
            <div id="downloadTrend" class="chart-container"></div>
          </el-card>
        </el-col>
      </el-row>

      <!-- 热门排行 -->
      <el-row :gutter="20" class="chart-row">
        <el-col :span="12">
          <el-card>
            <template #header>
              <span>热门资源 Top 10（浏览）</span>
            </template>
            <el-table :data="topViewedResources" stripe style="width: 100%">
              <el-table-column type="index" label="排名" width="60" align="center" />
              <el-table-column prop="title" label="资源标题" min-width="200" show-overflow-tooltip />
              <el-table-column prop="viewCount" label="浏览量" width="100" align="center" />
            </el-table>
          </el-card>
        </el-col>
        
        <el-col :span="12">
          <el-card>
            <template #header>
              <span>热门资源 Top 10（下载）</span>
            </template>
            <el-table :data="topDownloadedResources" stripe style="width: 100%">
              <el-table-column type="index" label="排名" width="60" align="center" />
              <el-table-column prop="title" label="资源标题" min-width="200" show-overflow-tooltip />
              <el-table-column prop="downloadCount" label="下载量" width="100" align="center" />
            </el-table>
          </el-card>
        </el-col>
      </el-row>
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { Document, VideoPlay, Download, User } from '@element-plus/icons-vue'
import * as echarts from 'echarts'
import type { EChartsOption } from 'echarts'

const loading = ref(false)
const statistics = ref<any>({
  documentStatistics: {},
  viewStatistics: {},
  downloadStatistics: {},
  resourceTypeDistribution: []
})

const topViewedResources = ref<any[]>([])
const topDownloadedResources = ref<any[]>([])

// 获取统计数据（模拟数据）
const getStatistics = async () => {
  loading.value = true
  try {
    // TODO: 调用后端 API 获取统计数据
    // const data: any = await getAnalysisDashboard()
    
    // 模拟数据
    statistics.value = {
      documentStatistics: {
        totalDocuments: 156,
        publishedCount: 120,
        pendingCount: 20,
        rejectedCount: 10,
        offlineCount: 6
      },
      viewStatistics: {
        totalViews: 8520,
        uniqueVisitors: 5964,
        viewTrend: generateTrendData('view')
      },
      downloadStatistics: {
        totalDownloads: 3250,
        downloadTrend: generateTrendData('download')
      },
      resourceTypeDistribution: [
        { fileType: 'PDF', count: 65, percentage: 41.67 },
        { fileType: 'Word', count: 45, percentage: 28.85 },
        { fileType: 'PPT', count: 28, percentage: 17.95 },
        { fileType: 'MP4', count: 18, percentage: 11.54 }
      ]
    }
    
    // 模拟热门资源
    topViewedResources.value = Array.from({ length: 10 }, (_, i) => ({
      title: `护理知识文档 ${i + 1}`,
      viewCount: Math.floor(Math.random() * 1000) + 100
    }))
    
    topDownloadedResources.value = Array.from({ length: 10 }, (_, i) => ({
      title: `医疗培训资料 ${i + 1}`,
      downloadCount: Math.floor(Math.random() * 500) + 50
    }))
    
    // 渲染图表
    setTimeout(() => {
      renderTypeDistributionChart()
      renderStatusDistributionChart()
      renderViewTrendChart()
      renderDownloadTrendChart()
    }, 100)
    
  } catch (error) {
    console.error('获取统计数据失败', error)
  } finally {
    loading.value = false
  }
}

// 生成趋势数据
const generateTrendData = (type: string) => {
  const dates = []
  const counts = []
  for (let i = 6; i >= 0; i--) {
    const date = new Date()
    date.setDate(date.getDate() - i)
    dates.push(`${date.getMonth() + 1}/${date.getDate() + 1}`)
    counts.push(type === 'view' ? Math.floor(Math.random() * 500) + 200 : Math.floor(Math.random() * 200) + 50)
  }
  return { dates, counts }
}

// 渲染资源类型分布图
const renderTypeDistributionChart = () => {
  const chart = echarts.init(document.getElementById('typeDistribution')!)
  
  const option: EChartsOption = {
    tooltip: {
      trigger: 'item',
      formatter: '{b}: {c} ({d}%)'
    },
    legend: {
      orient: 'vertical',
      right: 10,
      top: 'center'
    },
    series: [
      {
        type: 'pie',
        radius: ['40%', '70%'],
        avoidLabelOverlap: false,
        itemStyle: {
          borderRadius: 10,
          borderColor: '#fff',
          borderWidth: 2
        },
        label: {
          show: false,
          position: 'center'
        },
        emphasis: {
          label: {
            show: true,
            fontSize: 20,
            fontWeight: 'bold'
          }
        },
        labelLine: {
          show: false
        },
        data: statistics.value.resourceTypeDistribution.map((item: any) => ({
          name: item.fileType,
          value: item.count
        }))
      }
    ]
  }
  
  chart.setOption(option)
  
  // 响应式调整
  window.addEventListener('resize', () => {
    chart.resize()
  })
}

// 渲染文档状态分布图
const renderStatusDistributionChart = () => {
  const chart = echarts.init(document.getElementById('statusDistribution')!)
  
  const docStats = statistics.value.documentStatistics
  
  const option: EChartsOption = {
    tooltip: {
      trigger: 'axis',
      axisPointer: {
        type: 'shadow'
      }
    },
    grid: {
      left: '3%',
      right: '4%',
      bottom: '3%',
      containLabel: true
    },
    xAxis: {
      type: 'value',
      boundaryGap: [0, 0.01]
    },
    yAxis: {
      type: 'category',
      data: ['已发布', '待审核', '已驳回', '已下架']
    },
    series: [
      {
        type: 'bar',
        data: [
          docStats.publishedCount || 0,
          docStats.pendingCount || 0,
          docStats.rejectedCount || 0,
          docStats.offlineCount || 0
        ],
        itemStyle: {
          color: (params: any) => {
            const colors = ['#67C23A', '#E6A23C', '#F56C6C', '#909399']
            return colors[params.dataIndex]
          }
        }
      }
    ]
  }
  
  chart.setOption(option)
  
  window.addEventListener('resize', () => {
    chart.resize()
  })
}

// 渲染浏览趋势图
const renderViewTrendChart = () => {
  const chart = echarts.init(document.getElementById('viewTrend')!)
  const trendData = statistics.value.viewStatistics.viewTrend
  
  const option: EChartsOption = {
    tooltip: {
      trigger: 'axis'
    },
    grid: {
      left: '3%',
      right: '4%',
      bottom: '3%',
      containLabel: true
    },
    xAxis: {
      type: 'category',
      boundaryGap: false,
      data: trendData.dates
    },
    yAxis: {
      type: 'value'
    },
    series: [
      {
        name: '浏览量',
        type: 'line',
        smooth: true,
        data: trendData.counts,
        areaStyle: {
          color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
            { offset: 0, color: 'rgba(64, 158, 255, 0.5)' },
            { offset: 1, color: 'rgba(64, 158, 255, 0.05)' }
          ])
        },
        itemStyle: {
          color: '#409EFF'
        }
      }
    ]
  }
  
  chart.setOption(option)
  
  window.addEventListener('resize', () => {
    chart.resize()
  })
}

// 渲染下载趋势图
const renderDownloadTrendChart = () => {
  const chart = echarts.init(document.getElementById('downloadTrend')!)
  const trendData = statistics.value.downloadStatistics.downloadTrend
  
  const option: EChartsOption = {
    tooltip: {
      trigger: 'axis'
    },
    grid: {
      left: '3%',
      right: '4%',
      bottom: '3%',
      containLabel: true
    },
    xAxis: {
      type: 'category',
      boundaryGap: false,
      data: trendData.dates
    },
    yAxis: {
      type: 'value'
    },
    series: [
      {
        name: '下载量',
        type: 'line',
        smooth: true,
        data: trendData.counts,
        areaStyle: {
          color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
            { offset: 0, color: 'rgba(230, 162, 60, 0.5)' },
            { offset: 1, color: 'rgba(230, 162, 60, 0.05)' }
          ])
        },
        itemStyle: {
          color: '#E6A23C'
        }
      }
    ]
  }
  
  chart.setOption(option)
  
  window.addEventListener('resize', () => {
    chart.resize()
  })
}

onMounted(() => {
  getStatistics()
})
</script>

<style scoped lang="scss">
.knowledge-analysis {
  .card-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    font-size: 18px;
    font-weight: bold;
  }
  
  .overview-cards {
    margin-bottom: 20px;
    
    .stat-card {
      display: flex;
      align-items: center;
      padding: 20px;
      
      .stat-icon {
        width: 60px;
        height: 60px;
        border-radius: 10px;
        display: flex;
        align-items: center;
        justify-content: center;
        margin-right: 20px;
        
        .el-icon {
          font-size: 30px;
          color: #fff;
        }
      }
      
      .stat-content {
        flex: 1;
        
        .stat-value {
          font-size: 28px;
          font-weight: bold;
          color: #333;
        }
        
        .stat-label {
          font-size: 14px;
          color: #999;
          margin-top: 5px;
        }
      }
    }
  }
  
  .chart-row {
    margin-bottom: 20px;
    
    .chart-container {
      height: 300px;
      width: 100%;
    }
  }
}
</style>
