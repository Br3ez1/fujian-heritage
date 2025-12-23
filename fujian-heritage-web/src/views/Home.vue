{
type: uploaded file
fileName: src/views/Home.vue
fullContent:
<template>
  <div class="home-wrapper">
    <div class="section-hero">
      <el-carousel height="calc(100vh - 80px)" :interval="5000" arrow="always">
        <el-carousel-item v-for="(item, index) in banners" :key="index">
          <div class="banner-item" :style="{ backgroundImage: `url(${item.img})` }">
            <div class="banner-content">
              <div class="content-inner">
                <h1 class="banner-title serif-font">{{ item.title }}</h1>
                <p class="banner-desc">{{ item.desc }}</p>
                <el-button type="primary" plain round size="large" class="banner-btn" @click="scrollToContent">
                  探索非遗世界
                </el-button>
              </div>
            </div>
          </div>
        </el-carousel-item>
      </el-carousel>

      <div class="scroll-hint" @click="scrollToContent">
        <span>向下滑动</span>
        <el-icon class="bounce"><ArrowDown /></el-icon>
      </div>
    </div>

    <div class="section-content" id="news-section">
      <div class="container">
        <div class="section-header">
          <h2 class="serif-font">非遗 · 资讯</h2>
          <span class="sub-title">LATEST NEWS</span>
        </div>

        <el-row :gutter="40">
          <el-col :xs="24" :md="10">
            <div class="headline-news" @click="$router.push('/community')">
              <div class="headline-img-wrapper">
                <img src="../assets/images/news_headline.jpg" alt="Headline" class="headline-img" />
                <span class="tag">焦点</span>
              </div>
              <div class="headline-info">
                <h3 class="serif-font">2026年“文化和自然遗产日”福建主会场活动前瞻</h3>
                <p>本次活动将集中展示福建省近年来非遗保护传承的重要成果，百名传承人将现场献艺，带来前所未有的文化盛宴。</p>
              </div>
            </div>
          </el-col>
          <el-col :xs="24" :md="14">
            <div class="news-list">
              <div v-for="(news, i) in newsList" :key="i" class="news-item">
                <div class="date-box">
                  <span class="day">{{ news.day }}</span>
                  <span class="month">{{ news.month }}</span>
                </div>
                <div class="news-detail">
                  <h4>{{ news.title }}</h4>
                  <p>{{ news.summary }}</p>
                </div>
              </div>
            </div>
          </el-col>
        </el-row>
      </div>
    </div>

    <div class="section-content bg-gray">
      <div class="container">
        <div class="section-header">
          <h2 class="serif-font">非遗 · 瑰宝</h2>
          <span class="sub-title">FEATURED HERITAGE</span>
        </div>

        <el-row :gutter="20">
          <el-col :xs="24" :sm="8" v-for="(item, i) in featuredItems" :key="i" class="mb-20">
            <div class="feature-card" @click="$router.push('/directory')">
              <div class="card-bg" :style="{
            backgroundImage: `url(${item.img})`,
            backgroundColor: item.color
          }">
                <span class="region-badge">{{ item.region }}</span>
              </div>
              <div class="card-text">
                <h3 class="serif-font">{{ item.title }}</h3>
                <div class="divider"></div>
                <p>{{ item.desc }}</p>
              </div>
            </div>
          </el-col>
        </el-row>
      </div>
    </div>


    <div class="section-content">
      <div class="container">
        <div class="section-header">
          <h2 class="serif-font">八闽 · 寻踪</h2>
          <span class="sub-title">REGIONAL EXPLORATION</span>
        </div>

        <div class="map-wrapper">
          <div class="map-visual">
            <img src="../assets/images/map.jpg" class="map-base" alt="Fujian Map" />

            <div
                v-for="city in cities"
                :key="city.name"
                class="map-dot"
                :class="{ active: activeCity === city.name }"
                :style="{ top: city.top, left: city.left }"
                @click="activeCity = city.name"
            >
              <span class="dot-label">{{ city.name }}</span>
            </div>

            <div class="map-tip">点击节点切换，点击右侧地名探索详情</div>
          </div>

          <div class="map-info">
            <transition name="fade" mode="out-in">
              <div :key="activeCity" class="city-detail">
                <h1 class="serif-font city-name" @click="handleCityJump" title="点击探索该地区非遗项目">
                  {{ activeCityData.name }}
                  <el-icon class="jump-icon"><Right /></el-icon>
                </h1>

                <p class="city-desc">{{ activeCityData.desc }}</p>
                <div class="city-tags">
                  <el-tag
                      v-for="tag in activeCityData.items"
                      :key="tag"
                      effect="dark"
                      color="#A40000"
                      style="border: none; margin-right: 8px; margin-bottom: 8px;"
                  >
                    {{ tag }}
                  </el-tag>
                </div>
              </div>
            </transition>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'
import { useRouter } from 'vue-router'
// 确保引入 Right 图标
import { ArrowDown, Right } from '@element-plus/icons-vue'

const router = useRouter()

// ==========================================
// 轮播图配置
// ==========================================
const banners = [
  {
    title: "福建非遗 · 智享未来",
    desc: "数字化赋能传统文化，让传承触手可及",
    img: new URL('../assets/images/banner1.jpg',import.meta.url).href
  },
  {
    title: "中国白 · 德化瓷",
    desc: "世界陶瓷之都的艺术瑰宝",
    img: new URL('../assets/images/banner2.jpg',import.meta.url).href
  },
  {
    title: "茶韵 · 铁观音",
    desc: "传统制作技艺，百年的茶香",
    img: new URL('../assets/images/banner3.jpg',import.meta.url).href
  },
  {
    title: "土楼 · 东方古堡",
    desc: "独一无二的神话般山区建筑",
    img: new URL('../assets/images/banner4.jpg',import.meta.url).href
  },
  {
    title: "妈祖 · 海上女神",
    desc: "中国首个信俗类世界遗产，护佑四海平安",
    img: new URL('../assets/images/banner5.jpg',import.meta.url).href
  }
]

// 资讯数据
const newsList = [
  { day: "15", month: "12月", title: "关于举办“小小传承人”非遗研学冬令营的通知", summary: "为弘扬传统文化，特举办针对青少年的沉浸式非遗体验活动..." },
  { day: "08", month: "12月", title: "福建省第五批省级非物质文化遗产代表性项目名录公布", summary: "新增项目涵盖民间文学、传统音乐、传统技艺等十大类别..." },
  { day: "21", month: "11月", title: "“非遗+电商”：文创产品销售额突破新高", summary: "数字化赋能非遗产业，助力乡村振兴与文化传播..." },
]

// 精选数据
const featuredItems = [
  {
    title: "福州脱胎漆器",
    region: "福州",
    desc: "质地轻巧坚固，色泽鲜艳古朴，与北京景泰蓝、江西景德镇瓷器并称中国工艺三宝。",
    img: new URL('../assets/images/feature1.jpg',import.meta.url).href
  },
  {
    title: "惠安石雕",
    region: "泉州",
    desc: "源于中原，流传于福建惠安，具有强烈的民族性和浓郁的宗教色彩。",
    img: new URL('../assets/images/feature2.jpg',import.meta.url).href
  },
  {
    title: "妈祖信俗",
    region: "莆田",
    desc: "中国首个信俗类世界遗产，以崇奉和颂扬妈祖立德、行善、大爱精神为核心。",
    img: new URL('../assets/images/feature3.jpg',import.meta.url).href
  }
];

// 地图数据
const activeCity = ref('福州')
const cities = [
  { name: '福州', top: '43%', left: '75%' },
  { name: '莆田', top: '54%', left: '75%' },
  { name: '泉州', top: '60%', left: '67%' },
  { name: '厦门', top: '70%', left: '50%' },
  { name: '南平', top: '32%', left: '47%' },
  { name: '宁德', top: '30%', left: '83%' },
  { name: '三明', top: '36%', left: '31%' },
  { name: '龙岩', top: '50%', left: '20%' },
]
const cityDataMap = {
  '福州': { name: '福州', desc: '八闽首府，三山鼎峙。拥有脱胎漆器、软木画、寿山石雕等“榕城三宝”。', items: ['脱胎漆器', '寿山石雕', '软木画'] },
  '莆田': { name: '莆田', desc: '海滨邹鲁，文献名邦。妈祖信俗的发源地，木雕工艺精湛。', items: ['妈祖信俗', '莆田木雕', '莆仙戏'] },
  '泉州': { name: '泉州', desc: '海上丝绸之路起点，世界宗教博物馆。南音古朴，提线木偶惟妙惟肖。', items: ['南音', '提线木偶', '惠安石雕'] },
  '厦门': { name: '厦门', desc: '鹭岛风情，中西合璧。漆线雕工艺金碧辉煌。', items: ['漆线雕', '歌仔戏', '中秋博饼'] },
  '南平': { name: '南平', desc: '武夷山水，茶香四溢。建盏烧制技艺独步天下。', items: ['大红袍制作', '建盏', '建本'] },
  '宁德': { name: '宁德', desc: '闽东之光，山海交响。柘荣剪纸古朴生动，霍童线狮惊险绝伦。', items: ['柘荣剪纸', '霍童线狮', '福安平讲戏'] },
  '龙岩': { name: '龙岩', desc: '客家祖地，红色圣地。连城培田古民居建筑精湛，闽西汉剧唱腔独特。', items: ['连城培田古民居', '闽西汉剧', '永定土楼营造技艺'] },
  '三明': { name: '三明', desc: '绿色林海，文明之城。沙县小吃制作技艺闻名遐迩，泰宁梅林戏独具特色。', items: ['沙县小吃制作技艺', '泰宁梅林戏', '永安大腔戏'] }
}
const activeCityData = computed(() => cityDataMap[activeCity.value])

// 新增：点击城市大字跳转
const handleCityJump = () => {
  router.push({ path: '/directory', query: { region: activeCity.value } })
}

const scrollToContent = () => {
  document.getElementById('news-section').scrollIntoView({ behavior: 'smooth' })
}
</script>

<style scoped>
.home-wrapper { width: 100%; overflow-x: hidden; }
.serif-font { font-family: "SimSun", "STSong", serif; font-weight: bold; }

/* 第一屏：Hero Banner */
.section-hero { position: relative; height: calc(100vh - 80px); background: #000; }

.banner-item {
  width: 100%; height: 100%;
  background-position: center;
  background-size: cover;
  background-repeat: no-repeat;
  display: flex; align-items: center; justify-content: center;
  position: relative;
}
.banner-item::before {
  content: ""; position: absolute; top: 0; left: 0; right: 0; bottom: 0;
  background: rgba(0, 0, 0, 0.35);
}

.banner-content { position: relative; z-index: 2; text-align: center; color: #fff; padding: 0 20px; }
.content-inner {
  border: 1px solid rgba(255,255,255,0.5);
  padding: 60px 80px;
  backdrop-filter: blur(4px);
  max-width: 800px;
  margin: 0 auto;
}
.banner-title { font-size: 3.5rem; margin-bottom: 20px; text-shadow: 2px 2px 4px rgba(0,0,0,0.5); }
.banner-desc { font-size: 1.2rem; margin-bottom: 40px; opacity: 0.9; letter-spacing: 2px; }
.banner-btn { padding: 12px 30px; font-size: 16px; border-width: 2px; }

/* 下滑箭头 */
.scroll-hint {
  position: absolute; bottom: 30px; left: 50%; transform: translateX(-50%);
  color: #fff; cursor: pointer; text-align: center; z-index: 10;
  display: flex; flex-direction: column; align-items: center;
}
.scroll-hint span { font-size: 12px; margin-bottom: 5px; opacity: 0.8; }
.bounce { animation: bounce 2s infinite; font-size: 20px; }
@keyframes bounce { 0%, 20%, 50%, 80%, 100% {transform: translateY(0);} 40% {transform: translateY(-10px);} 60% {transform: translateY(-5px);} }

/* 通用内容区块 */
.section-content { padding: 80px 0; background: #fff; }
.bg-gray { background: #f9f9f9; }
.container { max-width: 1200px; margin: 0 auto; padding: 0 20px; }

.section-header { text-align: center; margin-bottom: 50px; }
.section-header h2 { font-size: 32px; color: #333; margin: 0; position: relative; display: inline-block; }
.section-header h2::after { content: ""; display: block; width: 40px; height: 3px; background: #A40000; margin: 10px auto 0; }
.sub-title { font-size: 12px; color: #999; letter-spacing: 2px; margin-top: 5px; display: block; }

/* 资讯模块 */
.headline-news { cursor: pointer; transition: transform 0.3s; margin-bottom: 20px; }
.headline-news:hover { transform: translateY(-5px); }
.headline-img-wrapper { position: relative; height: 260px; overflow: hidden; border-radius: 4px; }
.headline-img { width: 100%; height: 100%; object-fit: cover; transition: transform 0.5s; }
.headline-news:hover .headline-img { transform: scale(1.05); }
.tag { position: absolute; top: 15px; left: 15px; background: #A40000; color: #fff; padding: 4px 12px; font-size: 12px; border-radius: 2px; }
.headline-info h3 { margin-top: 15px; font-size: 18px; color: #333; }
.headline-info p { font-size: 14px; color: #666; line-height: 1.6; margin-top: 10px; }

.news-list { padding-left: 20px; }
.news-item { display: flex; gap: 20px; padding: 15px 0; border-bottom: 1px dashed #eee; cursor: pointer; }
.news-item:hover h4 { color: #A40000; }
.date-box { text-align: center; border: 1px solid #ddd; border-radius: 4px; padding: 5px 10px; height: fit-content; }
.day { display: block; font-size: 24px; font-weight: bold; color: #A40000; line-height: 1; }
.month { font-size: 12px; color: #999; }
.news-detail h4 { margin: 0 0 5px; font-size: 16px; color: #333; transition: color 0.3s; }
.news-detail p { margin: 0; font-size: 13px; color: #888; overflow: hidden; text-overflow: ellipsis; white-space: nowrap; max-width: 400px; }

/* 精选模块 */
.mb-20 { margin-bottom: 20px; }
.feature-card { background: #fff; border: 1px solid #eee; transition: all 0.3s; cursor: pointer; height: 100%; }
.feature-card:hover { transform: translateY(-5px); box-shadow: 0 10px 20px rgba(0,0,0,0.1); border-color: #A40000; }
.card-bg {
  height: 160px;
  display: flex;
  align-items: center;
  justify-content: center;
  position: relative;
  background-size: cover;
  background-position: center;
  background-repeat: no-repeat;
  background-color: rgba(0, 0, 0, 0.3);
  background-blend-mode: multiply;
}
.region-badge { border: 1px solid rgba(255,255,255,0.6); color: #fff; padding: 4px 12px; font-size: 14px; }
.card-text { padding: 25px; text-align: center; }
.card-text h3 { margin: 0 0 15px; color: #333; }
.divider { width: 30px; height: 2px; background: #A40000; margin: 0 auto 15px; }
.card-text p { font-size: 13px; color: #666; line-height: 1.6; text-align: justify; }

/* 地图模块 */
.map-wrapper { display: flex; height: 500px; background: #fff; border: 1px solid #eee; box-shadow: 0 5px 15px rgba(0,0,0,0.05); }
.map-visual { width: 50%; position: relative; background: #fff; border-right: 1px solid #eee; overflow: hidden; }
.map-base {position: absolute;top: 0;left: 0;width: 100%;height: 100%;object-fit: cover;opacity: 0.5;}
.map-dot {
  position: absolute; width: 16px; height: 16px; background: #333; border: 3px solid #fff; border-radius: 50%;
  cursor: pointer; box-shadow: 0 2px 5px rgba(0,0,0,0.2); transition: all 0.3s;
}
.map-dot:hover, .map-dot.active { background: #A40000; transform: scale(1.3); }
.dot-label { position: absolute; top: -25px; left: 50%; transform: translateX(-50%); white-space: nowrap; font-size: 12px; font-weight: bold; color: #333; pointer-events: none; }
.map-tip { position: absolute; bottom: 15px; left: 15px; background: rgba(255,255,255,0.9); padding: 5px 10px; font-size: 12px; color: #999; border-radius: 4px; }

.map-info { width: 50%; padding: 60px; display: flex; flex-direction: column; justify-content: center; background: #fdfdfd; }

/* 城市大字样式修改 */
.city-name {
  font-size: 48px;
  color: #A40000;
  margin: 0 0 20px;
  cursor: pointer; /* 鼠标手势 */
  display: inline-flex;
  align-items: center;
  transition: all 0.3s ease;
}
.city-name:hover {
  transform: translateX(5px); /* 悬停右移特效 */
  text-shadow: 2px 2px 4px rgba(164, 0, 0, 0.1);
}
.jump-icon {
  font-size: 24px;
  margin-left: 10px;
  opacity: 0.6;
}
.city-name:hover .jump-icon {
  opacity: 1;
}

.city-desc { font-size: 16px; color: #555; line-height: 1.8; margin-bottom: 30px; }
.city-tags { display: flex; flex-wrap: wrap; }

/* 响应式 */
@media (max-width: 768px) {
  .news-list { padding: 0; margin-top: 20px; }
  .map-wrapper { flex-direction: column; height: auto; }
  .map-visual { width: 50%; height: 350px; }
  .map-info { width: 100%; padding: 30px; }
}

.fade-enter-active, .fade-leave-active { transition: opacity 0.3s ease; }
.fade-enter-from, .fade-leave-to { opacity: 0; }
</style>
}