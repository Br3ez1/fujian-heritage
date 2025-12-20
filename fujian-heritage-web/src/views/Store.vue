<template>
  <div class="store-wrapper">
    <div class="store-banner">
      <div class="banner-inner">
        <h1 class="serif-font">非遗 · 文创</h1>
        <p>让传统走进生活，把非遗带回家</p>
      </div>
    </div>

    <div class="content-container">
      <div class="toolbar-wrapper">
        <div class="filter-box">
          <span class="filter-label">品类筛选：</span>
          <el-radio-group v-model="filterCategory" size="default" fill="#A40000" @change="fetchProducts">
            <el-radio-button label="全部" />
            <el-radio-button label="漆艺" />
            <el-radio-button label="陶瓷" />
            <el-radio-button label="木雕" />
            <el-radio-button label="茶文化" />
          </el-radio-group>
        </div>

        <div class="action-area">
          <div v-if="!isLogin" class="auth-box">
            <el-button type="primary" link @click="handleLogin">登入 / 注册</el-button>
            <el-divider direction="vertical" />
          </div>

          <div v-else class="user-mini-profile" @click="userDrawer = true">
            <el-avatar :size="32" style="background-color: #A40000; color: #fff; font-size: 14px; cursor: pointer;">
              {{ userStore.userInfo?.nickname?.charAt(0) || '客' }}
            </el-avatar>
            <span class="mini-name">{{ userStore.userInfo?.nickname || '用户' }}</span>
            <el-divider direction="vertical" />
          </div>

          <el-badge :value="cart.length" :hidden="cart.length === 0" class="cart-badge">
            <el-button color="#A40000" plain icon="ShoppingCart" @click="drawer = true" class="cart-btn">
              我的雅集
            </el-button>
          </el-badge>
        </div>
      </div>

      <div class="product-grid" v-loading="loading">
        <el-row :gutter="25">
          <el-col :xs="24" :sm="12" :md="6" v-for="product in productList" :key="product.productId" class="mb-20">
            <el-card class="product-card" :body-style="{ padding: '0px' }" shadow="hover">
              <div class="product-img-wrapper">
                <img :src="product.img" class="product-img" loading="lazy" alt="商品展示" />
                <div class="product-tag" v-if="product.tag">{{ product.tag }}</div>
              </div>

              <div class="product-info">
                <h3 class="product-title serif-font" :title="product.name">{{ product.name }}</h3>
                <div class="product-meta">
                  <div class="meta-left">
                    <div class="origin-tag">{{ product.category }}</div>
                    <div class="price">¥{{ product.price }}</div>
                  </div>
                  <el-button type="primary" color="#A40000" circle size="small" icon="Plus" @click="addToCart(product)"></el-button>
                </div>
              </div>
            </el-card>
          </el-col>
        </el-row>

        <el-empty v-if="!loading && productList.length === 0" description="暂无该分类藏品" />
      </div>
    </div>

    <!-- 购物车抽屉 -->
    <el-drawer v-model="drawer" title="我的雅集（购物车）" direction="rtl" size="380px">
      <div v-if="cart.length === 0" class="empty-cart">
        <el-icon size="60" color="#ddd"><ShoppingCart /></el-icon>
        <p>雅集暂无藏品</p>
      </div>
      <div v-else class="cart-list">
        <div v-for="(item, index) in cart" :key="index" class="cart-item">
          <img :src="item.img" class="cart-img" />
          <div class="cart-info">
            <div class="cart-name serif-font">{{ item.name }}</div>
            <div class="cart-price">¥{{ item.price }}</div>
          </div>
          <el-button type="danger" link icon="Delete" @click="removeFromCart(index)"></el-button>
        </div>

        <div class="cart-footer">
          <div class="total-price">
            <span>合计:</span>
            <span class="price-num">¥{{ cartTotal }}</span>
          </div>
          <el-button type="primary" color="#A40000" class="checkout-btn" @click="handleCheckout" :loading="checkoutLoading">
            立即结算
          </el-button>
        </div>
      </div>
    </el-drawer>

    <!-- 个人中心抽屉 -->
    <UserDrawer v-model="userDrawer" />
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import request from '../api/request'
import { ElMessage, ElMessageBox } from 'element-plus'
import { useUserStore } from '../store/user'
import { useRouter } from 'vue-router'
import UserDrawer from '../components/UserDrawer.vue'

const userStore = useUserStore()
const router = useRouter()

const filterCategory = ref('全部')
const drawer = ref(false)
const userDrawer = ref(false)
const cart = ref([])
const loading = ref(false)
const checkoutLoading = ref(false)
const productList = ref([])

const isLogin = computed(() => !!userStore.userInfo)

const mockProducts = [
  { productId: 1, name: "福州脱胎漆器·金缮手工碗", category: "漆艺", price: 1280, stock: 5, tag: '典藏' },
  { productId: 2, name: "德化白瓷·大师手作羊脂玉茶具", category: "陶瓷", price: 299, stock: 100, tag: '热销' },
  { productId: 3, name: "安溪铁观音·2025秋茶", category: "茶文化", price: 198, stock: 50 },
  { productId: 4, name: "惠安木雕·妈祖神韵摆件", category: "木雕", price: 580, stock: 8, tag: '孤品' },
  { productId: 5, name: "福州软木画·三坊七巷全景", category: "木雕", price: 360, stock: 12 },
  { productId: 6, name: "建阳建盏·非遗传承兔毫盏", category: "陶瓷", price: 688, stock: 20 },
  { productId: 7, name: "武夷岩茶·大红袍礼盒", category: "茶文化", price: 888, stock: 15, tag: '送礼' }
]

const fetchProducts = async () => {
  loading.value = true
  try {
    const res = await request.get('/product/list', {
      params: { category: filterCategory.value === '全部' ? '' : filterCategory.value }
    })
    if (res.code === 200) {
      productList.value = res.data.map((item, index) => {
        const autoImg = `/images/p${(index % 7) + 1}.jpg`
        return {
          ...item,
          img: item.img_url || autoImg,
          tag: item.stock < 10 ? '少量' : ''
        }
      })
    } else {
      throw new Error(res.msg)
    }
  } catch (error) {
    console.warn('后端连接失败，使用Mock数据')
    let sourceData = mockProducts
    if (filterCategory.value !== '全部') {
      sourceData = mockProducts.filter(p => p.category === filterCategory.value)
    }
    productList.value = sourceData.map((item, index) => ({
      ...item,
      img: `/images/p${(index % 7) + 1}.jpg`
    }))
  } finally {
    loading.value = false
  }
}

const cartTotal = computed(() => cart.value.reduce((sum, item) => sum + item.price, 0))

const handleLogin = () => router.push('/login')

const addToCart = (product) => {
  cart.value.push(product)
  ElMessage.success({ message: `已将 ${product.name} 收入雅集`, type: 'success' })
}

const removeFromCart = (index) => {
  cart.value.splice(index, 1)
}

// ★★★ 核心修改：真实下单 ★★★
const handleCheckout = () => {
  if (!isLogin.value) {
    ElMessage.warning('请先登录后再结算')
    setTimeout(() => router.push('/login'), 1000)
    return
  }

  ElMessageBox.prompt('请输入收货地址', '订单确认', {
    confirmButtonText: '支付并下单',
    cancelButtonText: '取消',
    inputPattern: /.+/,
    inputErrorMessage: '地址不能为空'
  }).then(async ({ value: address }) => {

    checkoutLoading.value = true

    // 1. 拼接商品摘要
    const summary = cart.value.map(p => p.name).join(', ')

    // 2. 构造订单对象
    const orderData = {
      userId: userStore.userInfo.userId,
      totalAmount: cartTotal.value,
      receiverName: userStore.userInfo.nickname || userStore.userInfo.username, // 默认收货人
      address: address,
      contact: "13800000000", // 默认手机号，实际应从用户表查
      productSummary: summary.length > 200 ? summary.substring(0, 197) + "..." : summary
    }

    try {
      const res = await request.post('/order/create', orderData)
      if (res.code === 200) {
        ElMessage.success('支付成功！已生成订单')
        cart.value = []
        drawer.value = false
      } else {
        ElMessage.error(res.msg || '下单失败')
      }
    } catch (e) {
      ElMessage.error('网络错误，下单失败')
    } finally {
      checkoutLoading.value = false
    }

  }).catch(() => {})
}

onMounted(() => {
  fetchProducts()
})
</script>

<style scoped>
/* 样式保持不变，省略以节省空间，请复制之前的样式 */
.store-wrapper { background: #fdfdfd; min-height: 100vh; }
.serif-font { font-family: "SimSun", serif; font-weight: bold; }

.store-banner {
  height: 200px;
  background: linear-gradient(135deg, #A40000, #8B0000);
  background-image: url('https://www.ihchina.cn/Uploads/Picture/2018/11/02/s5bdbf7f63f5d.jpg');
  background-blend-mode: multiply;
  background-size: cover;
  color: #fff;
  display: flex; flex-direction: column; align-items: center; justify-content: center;
  text-align: center; margin-bottom: 40px;
}
.banner-inner h1 { margin: 0 0 10px 0; font-size: 36px; letter-spacing: 4px; text-shadow: 2px 2px 4px rgba(0,0,0,0.3); }
.banner-inner p { opacity: 0.9; margin-bottom: 0; font-size: 16px; letter-spacing: 1px; }

.content-container { max-width: 1200px; margin: 0 auto; padding: 0 20px 40px; }

.toolbar-wrapper {
  display: flex; justify-content: space-between; align-items: center;
  margin-bottom: 30px; padding: 15px 20px;
  background: #fff; border: 1px solid #E6E0D0; border-radius: 4px;
}
.filter-box { display: flex; align-items: center; overflow-x: auto; }
.filter-label { font-size: 14px; font-weight: bold; color: #333; margin-right: 15px; white-space: nowrap; }

.action-area { display: flex; align-items: center; gap: 15px; }
.cart-btn { font-size: 14px; padding: 8px 18px; }

.user-mini-profile { display: flex; align-items: center; gap: 8px; cursor: pointer; transition: opacity 0.2s; }
.user-mini-profile:hover { opacity: 0.8; }
.mini-name { font-size: 14px; color: #333; font-weight: bold; }

.mb-20 { margin-bottom: 25px; }
.product-card {
  border: none; transition: transform 0.3s; overflow: hidden; border: 1px solid #eee; height: 100%;
  display: flex; flex-direction: column;
}
.product-card:hover { transform: translateY(-5px); box-shadow: 0 10px 20px rgba(0,0,0,0.1); border-color: #A40000; }

.product-img-wrapper {
  height: 220px; width: 100%; position: relative; overflow: hidden; background: #f5f5f5;
}
.product-img {
  width: 100%; height: 100%; object-fit: cover; transition: transform 0.5s;
}
.product-card:hover .product-img { transform: scale(1.05); }

.product-tag {
  position: absolute; top: 10px; left: 10px;
  background: rgba(164, 0, 0, 0.9); color: #fff;
  font-size: 12px; padding: 2px 8px; border-radius: 2px;
}

.product-info { padding: 15px; flex: 1; display: flex; flex-direction: column; justify-content: space-between; }
.product-title { margin: 0 0 10px; font-size: 16px; white-space: nowrap; overflow: hidden; text-overflow: ellipsis; color: #333; }
.product-meta { display: flex; justify-content: space-between; align-items: flex-end; }
.origin-tag { font-size: 12px; color: #999; margin-bottom: 4px; }
.price { color: #A40000; font-size: 18px; font-weight: bold; font-family: Arial; }

.empty-cart { text-align: center; margin-top: 100px; color: #999; }
.cart-item { display: flex; margin-bottom: 15px; border-bottom: 1px solid #eee; padding-bottom: 10px; align-items: center; }
.cart-img { width: 50px; height: 50px; border-radius: 4px; margin-right: 10px; object-fit: cover; border: 1px solid #eee; }
.cart-info { flex: 1; }
.cart-name { font-size: 14px; font-weight: bold; color: #333; }
.cart-price { color: #A40000; font-size: 12px; }
.cart-footer { margin-top: 20px; border-top: 2px solid #A40000; padding-top: 15px; }
.total-price { display: flex; justify-content: space-between; font-size: 16px; font-weight: bold; margin-bottom: 15px; }
.price-num { color: #A40000; }
.checkout-btn { width: 100%; height: 40px; font-size: 16px; }
</style>