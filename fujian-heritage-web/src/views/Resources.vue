<template>
  <div class="resources-wrapper">
    <!-- 头部背景 -->
    <div class="page-header">
      <div class="header-content">
        <h1 class="serif-font">数字资源库</h1>
        <p>DIGITAL ARCHIVES</p>
      </div>
    </div>

    <div class="content-container">
      <el-tabs v-model="activeTab" class="custom-tabs">
        <!-- 影像馆 -->
        <el-tab-pane name="video">
          <template #label>
            <span class="tab-label"><el-icon><VideoCamera /></el-icon> 非遗影像</span>
          </template>

          <el-row :gutter="25">
            <el-col :xs="24" :sm="12" :md="8" v-for="video in videoList" :key="video.id" class="mb-30">
              <div class="video-card">
                <div class="video-cover">
                  <!-- 实际项目中这里换成 <img :src="video.cover"> -->
                  <div class="placeholder-bg"></div>
                  <div class="video-overlay">
                    <el-icon class="play-btn"><CaretRight /></el-icon>
                  </div>
                  <span class="duration">{{ video.duration }}</span>
                </div>
                <div class="video-info">
                  <h3 class="video-title serif-font">{{ video.title }}</h3>
                  <div class="video-meta">
                    <span><el-icon><View /></el-icon> {{ video.views }}</span>
                    <span>{{ video.date }}</span>
                  </div>
                </div>
              </div>
            </el-col>
          </el-row>
        </el-tab-pane>

        <!-- 云图库 -->
        <el-tab-pane name="image">
          <template #label>
            <span class="tab-label"><el-icon><Picture /></el-icon> 匠心图谱</span>
          </template>

          <!-- 瀑布流布局 -->
          <div class="masonry-layout">
            <div v-for="img in imageList" :key="img.id" class="masonry-item">
              <div class="img-card">
                <el-image
                    :src="img.url"
                    :preview-src-list="[img.url]"
                    fit="cover"
                    loading="lazy"
                    class="gallery-img"
                >
                  <template #placeholder>
                    <div class="img-loading">加载中...</div>
                  </template>
                </el-image>
                <div class="img-caption">
                  <p class="img-title serif-font">{{ img.title }}</p>
                  <p class="img-desc">{{ img.desc }}</p>
                </div>
              </div>
            </div>
          </div>
        </el-tab-pane>
      </el-tabs>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'

const activeTab = ref('video')

const videoList = [
  { id: 1, title: "《非遗里的中国》福建篇：探秘南音古韵", duration: "45:20", views: "1.2w", date: "2025-10-15" },
  { id: 2, title: "指尖上的传承：福州软木画技艺全解析", duration: "12:05", views: "8.5k", date: "2025-11-02" },
  { id: 3, title: "千年窑火：德化白瓷的烧制秘密与鉴赏", duration: "28:15", views: "2.1w", date: "2025-09-20" },
  { id: 4, title: "惠安女服饰文化：不仅是美丽，更是历史", duration: "18:40", views: "5.6k", date: "2025-12-01" },
  { id: 5, title: "武夷山大红袍：一杯茶里的岩骨花香", duration: "08:50", views: "3.2k", date: "2025-11-11" },
  { id: 6, title: "莆田木雕：精微透雕工艺流程展示", duration: "15:30", views: "6.7k", date: "2025-10-30" },
]

const imageList = [
  { id: 1, url: "https://www.ihchina.cn/Uploads/Picture/2018/12/28/s5c25e0c52a0a2.jpg", title: "南音演奏", desc: "泉州南音，中国音乐活化石" },
  { id: 2, url: "https://www.ihchina.cn/Uploads/Picture/2019/04/23/s5cbe7b3b3a1a1.jpg", title: "惠安女服饰", desc: "独特的闽南沿海民俗风情" },
  { id: 3, url: "https://www.ihchina.cn/Uploads/Picture/2018/12/29/s5c272a2e4e1d1.jpg", title: "提线木偶", desc: "十指弦丝张弛有度" },
  { id: 4, url: "https://www.ihchina.cn/Uploads/Picture/2021/11/05/s61849a6e1a1a1.jpg", title: "德化白瓷", desc: "温润如玉的中国白" },
  { id: 5, url: "https://www.ihchina.cn/Uploads/Picture/2018/12/28/s5c25e1a2d1a1a.jpg", title: "油纸伞骨架", desc: "福州三宝之一" },
  { id: 6, url: "https://www.ihchina.cn/Uploads/Picture/2019/04/23/s5cbe7c3b2a2a2.jpg", title: "木版年画", desc: "漳州非遗传统技艺" },
]
</script>

<style scoped>
.resources-wrapper { width: 100%; }
.serif-font { font-family: "SimSun", serif; font-weight: bold; }

/* 头部背景 */
.page-header {
  height: 200px;
  background: linear-gradient(to right, #2c3e50, #000);
  display: flex; align-items: center; justify-content: center;
  color: #fff; text-align: center;
  background-image: url('https://www.ihchina.cn/Uploads/Picture/2018/11/02/s5bdbf7f63f5d.jpg'); /* 复用纹理 */
  background-blend-mode: multiply;
  background-size: cover;
}
.page-header h1 { margin: 0 0 10px 0; letter-spacing: 5px; font-size: 32px; }
.page-header p { margin: 0; font-size: 12px; letter-spacing: 2px; opacity: 0.8; }

.content-container { max-width: 1200px; margin: 40px auto; padding: 0 20px; }

/* Tab 样式优化 */
:deep(.el-tabs__item) { font-size: 16px; padding: 0 30px; height: 50px; line-height: 50px; }
:deep(.el-tabs__item.is-active) { color: #A40000; font-weight: bold; }
:deep(.el-tabs__active-bar) { background-color: #A40000; }
.tab-label { display: flex; align-items: center; gap: 8px; }

/* 视频卡片 */
.mb-30 { margin-bottom: 30px; }
.video-card {
  background: #fff; border-radius: 8px; overflow: hidden;
  box-shadow: 0 4px 12px rgba(0,0,0,0.05);
  transition: transform 0.3s; cursor: pointer;
}
.video-card:hover { transform: translateY(-5px); box-shadow: 0 10px 20px rgba(0,0,0,0.1); }

.video-cover { height: 200px; position: relative; overflow: hidden; }
.placeholder-bg { width: 100%; height: 100%; background: #333; }
.video-overlay {
  position: absolute; top: 0; left: 0; width: 100%; height: 100%;
  background: rgba(0,0,0,0.2); display: flex; justify-content: center; align-items: center;
  transition: background 0.3s;
}
.video-card:hover .video-overlay { background: rgba(0,0,0,0.1); }
.play-btn { font-size: 48px; color: #fff; opacity: 0.8; transition: transform 0.3s; }
.video-card:hover .play-btn { transform: scale(1.2); opacity: 1; }
.duration { position: absolute; bottom: 10px; right: 10px; color: #fff; background: rgba(0,0,0,0.6); padding: 2px 6px; border-radius: 4px; font-size: 12px; }

.video-info { padding: 15px; }
.video-title { margin: 0 0 10px 0; font-size: 16px; line-height: 1.4; color: #333; height: 44px; overflow: hidden; }
.video-meta { display: flex; justify-content: space-between; color: #999; font-size: 12px; }

/* 瀑布流 */
.masonry-layout { column-count: 3; column-gap: 20px; }
.masonry-item { break-inside: avoid; margin-bottom: 20px; }
.img-card { background: #fff; border: 1px solid #eee; padding: 10px; border-radius: 4px; transition: all 0.3s; }
.img-card:hover { box-shadow: 0 8px 16px rgba(0,0,0,0.1); border-color: #A40000; }
.gallery-img { width: 100%; border-radius: 2px; display: block; }
.img-loading { height: 200px; background: #f5f5f5; display: flex; align-items: center; justify-content: center; color: #999; }
.img-caption { margin-top: 10px; text-align: center; }
.img-title { margin: 0; font-size: 15px; color: #333; }
.img-desc { margin: 5px 0 0; font-size: 12px; color: #999; }

@media (max-width: 768px) {
  .masonry-layout { column-count: 1; }
}
</style>