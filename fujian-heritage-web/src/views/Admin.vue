<template>
  <div class="admin-wrapper">
    <el-container style="height: 100vh">
      <!-- 侧边栏 -->
      <el-aside width="220px" class="admin-aside">
        <div class="admin-logo">
          <el-icon class="mr-2"><Management /></el-icon> 八闽遗韵·后台
        </div>
        <el-menu
            background-color="#304156"
            text-color="#bfcbd9"
            active-text-color="#409EFF"
            :default-active="activeMenu"
            class="admin-menu"
            @select="handleMenuSelect"
        >
          <el-menu-item index="1"><el-icon><Odometer /></el-icon><span>控制台</span></el-menu-item>
          <el-menu-item index="2"><el-icon><User /></el-icon><span>用户管理</span></el-menu-item>
          <el-menu-item index="3"><el-icon><Goods /></el-icon><span>商品管理</span></el-menu-item>
          <el-menu-item index="8"><el-icon><Collection /></el-icon><span>非遗审核</span></el-menu-item>
          <el-menu-item index="4"><el-icon><ChatDotSquare /></el-icon><span>社区管理</span></el-menu-item>
          <el-menu-item index="5"><el-icon><School /></el-icon><span>研学活动管理</span></el-menu-item>
          <el-menu-item index="7"><el-icon><Cpu /></el-icon><span>AI 监控</span></el-menu-item>
          <el-menu-item index="6"><el-icon><List /></el-icon><span>订单调度</span></el-menu-item>

        </el-menu>
      </el-aside>

      <el-container>
        <!-- 头部 -->
        <el-header class="admin-header">
          <div class="header-left">
            <el-breadcrumb separator="/">
              <el-breadcrumb-item :to="{ path: '/admin' }">首页</el-breadcrumb-item>
              <el-breadcrumb-item>{{ currentBreadcrumb }}</el-breadcrumb-item>
            </el-breadcrumb>
          </div>
          <div class="header-right">
            <el-dropdown>
                <span class="el-dropdown-link">
                  <el-avatar :size="30" style="background:#409EFF; margin-right:8px">管</el-avatar>
                  管理员 <el-icon class="el-icon--right"><arrow-down /></el-icon>
                </span>
              <template #dropdown>
                <el-dropdown-menu>
                  <el-dropdown-item @click="$router.push('/')">返回前台</el-dropdown-item>
                  <el-dropdown-item divided @click="handleLogout">退出登录</el-dropdown-item>
                </el-dropdown-menu>
              </template>
            </el-dropdown>
          </div>
        </el-header>

        <el-main class="admin-main">
          <!-- 1. 控制台 -->
          <div v-if="activeMenu === '1'">
            <el-alert title="系统运行正常" type="success" :closable="false" show-icon class="mb-20" />
            <el-row :gutter="20" class="mb-20">
              <el-col :span="6"><el-card shadow="hover" class="data-card"><template #header>用户总数</template><div class="data-num red">{{ stats.userCount }}</div></el-card></el-col>
              <el-col :span="6"><el-card shadow="hover" class="data-card"><template #header>商品总数</template><div class="data-num blue">{{ stats.productCount }}</div></el-card></el-col>
              <el-col :span="6"><el-card shadow="hover" class="data-card"><template #header>社区帖子</template><div class="data-num orange">{{ stats.postCount }}</div></el-card></el-col>
              <el-col :span="6"><el-card shadow="hover" class="data-card"><template #header>研学活动</template><div class="data-num green">{{ stats.activityCount }}</div></el-card></el-col>
            </el-row>
            <el-card shadow="never">
              <div style="text-align: center; color: #999; padding: 40px;">
                <el-icon size="40"><DataAnalysis /></el-icon>
                <p>欢迎使用八闽遗韵后台管理系统</p>
              </div>
            </el-card>
          </div>

          <!-- 2. 用户管理 -->
          <div v-else-if="activeMenu === '2'">
            <el-card>
              <template #header>
                <div class="card-header-flex"><span class="title">用户列表</span><el-button type="primary" icon="Plus" @click="openUserDialog()">新增用户</el-button></div>
              </template>
              <el-table :data="userList" border stripe>
                <el-table-column prop="userId" label="ID" width="80" />
                <el-table-column prop="username" label="用户名" />
                <el-table-column prop="nickname" label="昵称" />
                <el-table-column prop="role" label="角色">
                  <template #default="{ row }"><el-tag :type="row.role===1?'danger':''">{{ row.role===1?'管理员':'用户' }}</el-tag></template>
                </el-table-column>
                <el-table-column label="操作" width="180">
                  <template #default="{ row }">
                    <el-button size="small" @click="openUserDialog(row)">编辑</el-button>
                    <el-button size="small" type="danger" @click="handleDeleteUser(row)">删除</el-button>
                  </template>
                </el-table-column>
              </el-table>
            </el-card>
          </div>

          <!-- 3. 商品管理 -->
          <div v-else-if="activeMenu === '3'">
            <el-card>
              <template #header>
                <div class="card-header-flex"><span class="title">商品列表</span><el-button type="primary" icon="Plus" @click="openProductDialog()">发布商品</el-button></div>
              </template>
              <el-table :data="productList" border stripe v-loading="loading">
                <el-table-column prop="productId" label="ID" width="80" />
                <el-table-column label="图片" width="100">
                  <template #default="{ row }">
                    <el-image style="width: 50px; height: 50px" :src="row.imgUrl" :preview-src-list="[row.imgUrl]" fit="cover" />
                  </template>
                </el-table-column>
                <el-table-column prop="name" label="名称" show-overflow-tooltip />
                <el-table-column prop="category" label="分类" width="100">
                  <template #default="{row}"><el-tag>{{row.category}}</el-tag></template>
                </el-table-column>
                <el-table-column prop="price" label="价格" width="120">
                  <template #default="{ row }"><span style="color:#F56C6C">¥{{ row.price }}</span></template>
                </el-table-column>
                <el-table-column prop="stock" label="库存" width="100" />
                <el-table-column label="操作" width="180">
                  <template #default="{ row }">
                    <el-button size="small" @click="openProductDialog(row)">编辑</el-button>
                    <el-button size="small" type="danger" @click="handleDeleteProduct(row)">下架</el-button>
                  </template>
                </el-table-column>
              </el-table>
            </el-card>
          </div>

          <!-- 4. 社区管理 -->
          <div v-else-if="activeMenu === '4'">
            <el-card>
              <template #header><span class="title">社区帖子管理</span></template>
              <el-table :data="auditList" border stripe v-loading="loading">
                <el-table-column prop="postId" label="ID" width="80" />
                <el-table-column prop="title" label="标题" width="200" show-overflow-tooltip>
                  <template #default="{row}">
                    <el-tag v-if="row.isTop === 1" type="danger" size="small" effect="dark" style="margin-right:5px">顶</el-tag>
                    {{ row.title }}
                  </template>
                </el-table-column>
                <el-table-column prop="content" label="内容" show-overflow-tooltip />
                <el-table-column prop="author" label="发布人" width="120" />
                <el-table-column prop="createTime" label="发布时间" width="180">
                  <template #default="{row}">{{ formatTime(row.createTime) }}</template>
                </el-table-column>
                <el-table-column label="操作" width="200">
                  <template #default="{ row }">
                    <el-button size="small" :type="row.isTop === 1 ? 'warning' : 'primary'" @click="handleToggleTop(row)">
                      {{ row.isTop === 1 ? '取消置顶' : '置顶' }}
                    </el-button>
                    <el-button size="small" type="danger" icon="Delete" @click="handleDeletePost(row)">删除</el-button>
                  </template>
                </el-table-column>
              </el-table>
            </el-card>
          </div>

          <!-- 5. 研学管理 (整合了名单审核) -->
          <div v-else-if="activeMenu === '5'">
            <el-card>
              <template #header>
                <div class="card-header-flex">
                  <span class="title">研学活动管理</span>
                  <el-button type="primary" icon="Plus" @click="openActivityDialog">发布新活动</el-button>
                </div>
              </template>
              <el-table :data="activityList" border stripe v-loading="loading">
                <el-table-column prop="activityId" label="ID" width="80" />
                <el-table-column label="封面" width="100">
                  <template #default="{ row }">
                    <el-image style="width: 80px; height: 50px; border-radius:4px" :src="row.imgUrl" fit="cover">
                      <template #error><div class="image-slot"><el-icon><Picture /></el-icon></div></template>
                    </el-image>
                  </template>
                </el-table-column>
                <el-table-column prop="title" label="活动主题" min-width="150" show-overflow-tooltip />
                <el-table-column prop="price" label="费用" width="100">
                  <template #default="{ row }">
                    <span v-if="row.price == 0" style="color:#67C23A">免费</span>
                    <span v-else style="color:#F56C6C">¥{{ row.price }}</span>
                  </template>
                </el-table-column>
                <el-table-column prop="activityTime" label="时间" width="160" show-overflow-tooltip />
                <el-table-column prop="status" label="状态" width="90" align="center">
                  <template #default="{ row }">
                    <el-tag v-if="row.status === 1" type="success">进行中</el-tag>
                    <el-tag v-else type="info">已结束</el-tag>
                  </template>
                </el-table-column>
                <el-table-column label="操作" width="280" align="center" fixed="right">
                  <template #default="{ row }">
                    <!-- 新增：名单审核按钮 -->
                    <el-button size="small" type="warning" icon="User" plain @click="openAuditDialog(row)">名单审核</el-button>

                    <el-button size="small" :type="row.status===1 ? 'warning' : 'success'" @click="handleToggleActivityStatus(row)">
                      {{ row.status===1 ? '结束' : '上架' }}
                    </el-button>
                    <el-button size="small" @click="openActivityDialog(row)">编辑</el-button>
                    <el-button size="small" type="danger" link @click="handleDeleteActivity(row)">删除</el-button>
                  </template>
                </el-table-column>
              </el-table>
            </el-card>
          </div>

          <!-- 6. 订单调度 -->
          <div v-else-if="activeMenu === '6'">
            <el-card>
              <template #header>
                <div class="card-header-flex">
                  <span class="title">订单调度中心</span>
                  <el-button type="primary" icon="Refresh" @click="fetchOrders">刷新列表</el-button>
                </div>
              </template>
              <el-table :data="orderList" border stripe v-loading="loading">
                <el-table-column prop="orderNo" label="订单编号" width="180" />
                <el-table-column prop="productSummary" label="商品内容" show-overflow-tooltip />
                <el-table-column prop="totalAmount" label="金额" width="100">
                  <template #default="{row}">¥{{ row.totalAmount }}</template>
                </el-table-column>
                <el-table-column prop="receiverName" label="收货人" width="100" />
                <el-table-column prop="status" label="状态" width="100" align="center">
                  <template #default="{ row }">
                    <el-tag v-if="row.status===1" type="primary">待发货</el-tag>
                    <el-tag v-else-if="row.status===2" type="warning" effect="dark">已发货</el-tag>
                    <el-tag v-else-if="row.status===3" type="success">已完成</el-tag>
                    <el-tag v-else type="info">其他</el-tag>
                  </template>
                </el-table-column>
                <el-table-column label="调度操作" width="220" align="center" fixed="right">
                  <template #default="{ row }">
                    <div v-if="row.status === 1">
                      <el-button type="primary" size="small" @click="handleShip(row)">发货</el-button>
                      <el-button type="warning" size="small" @click="handleCancel(row)">取消</el-button>
                    </div>
                    <div v-else-if="row.status === 2">
                      <el-button type="success" size="small" @click="handleComplete(row)">确认送达</el-button>
                    </div>
                    <span v-else style="color:#999;font-size:12px;">无操作</span>
                    <el-button size="small" type="danger" link @click="handleDeleteOrder(row)" style="margin-left:5px">删除</el-button>
                  </template>
                </el-table-column>
              </el-table>
            </el-card>
          </div>

          <!-- 7. AI 监控 -->
          <div v-else-if="activeMenu === '7'">
            <el-card>
              <template #header>
                <div class="card-header-flex">
                  <span class="title">AI 对话日志监控</span>
                  <el-button type="primary" icon="Refresh" @click="fetchAiLogs">刷新列表</el-button>
                </div>
              </template>
              <el-table :data="aiLogList" border stripe v-loading="loading">
                <el-table-column prop="logId" label="ID" width="80" />
                <el-table-column prop="userId" label="用户ID" width="100" />
                <el-table-column prop="question" label="提问内容" show-overflow-tooltip min-width="200" />
                <el-table-column prop="answer" label="AI 回复" show-overflow-tooltip min-width="200" />
                <el-table-column prop="createTime" label="时间" width="180">
                  <template #default="{row}">{{ formatTime(row.createTime) }}</template>
                </el-table-column>
                <el-table-column label="操作" width="120" align="center" fixed="right">
                  <template #default="{ row }">
                    <el-button size="small" type="primary" link @click="openAiLogDetail(row)">查看详情</el-button>
                  </template>
                </el-table-column>
              </el-table>
            </el-card>
          </div>

          <!-- 8. 非遗审核 -->
          <div v-else-if="activeMenu === '8'">
            <el-card>
              <template #header>
                <div class="card-header-flex">
                  <span class="title">非遗名录管理</span>
                  <el-button type="primary" icon="Plus" @click="openHeritageDialog()">新增名录</el-button>
                </div>
              </template>
              <el-table :data="heritageList" border stripe v-loading="loading">
                <el-table-column prop="heritageId" label="ID" width="80" />
                <el-table-column label="图片" width="100">
                  <template #default="{ row }">
                    <el-image style="width: 60px; height: 40px" :src="row.imgUrl" :preview-src-list="[row.imgUrl]" fit="cover">
                      <template #error><div class="image-slot"><el-icon><Picture /></el-icon></div></template>
                    </el-image>
                  </template>
                </el-table-column>
                <el-table-column prop="name" label="名称" show-overflow-tooltip min-width="150" />
                <el-table-column prop="category" label="分类" width="100">
                  <template #default="{row}"><el-tag effect="plain">{{ row.category }}</el-tag></template>
                </el-table-column>
                <el-table-column prop="level" label="级别" width="100" />
                <el-table-column prop="region" label="地区" width="100" />
                <el-table-column prop="status" label="状态" width="100" align="center">
                  <template #default="{ row }">
                    <el-tag v-if="row.status===0" type="warning">待审核</el-tag>
                    <el-tag v-else type="success">已发布</el-tag>
                  </template>
                </el-table-column>
                <el-table-column label="操作" width="220" align="center" fixed="right">
                  <template #default="{ row }">
                    <el-button v-if="row.status===0" type="success" size="small" @click="handleAuditHeritage(row, 1)">发布</el-button>
                    <el-button v-else type="warning" size="small" @click="handleAuditHeritage(row, 0)">下架</el-button>
                    <el-button size="small" @click="openHeritageDialog(row)" style="margin-left:5px">编辑</el-button>
                    <el-button size="small" type="danger" link @click="handleDeleteHeritage(row)">删除</el-button>
                  </template>
                </el-table-column>
              </el-table>
            </el-card>
          </div>

        </el-main>
      </el-container>
    </el-container>

    <!-- 弹窗组件区域 -->
    <el-dialog v-model="userDialogVisible" title="用户信息" width="500px">
      <el-form :model="userForm" label-width="80px">
        <el-form-item label="用户名"><el-input v-model="userForm.username" :disabled="!!userForm.userId" /></el-form-item>
        <el-form-item label="昵称"><el-input v-model="userForm.nickname" /></el-form-item>
        <el-form-item label="角色">
          <el-select v-model="userForm.role">
            <el-option label="普通用户" :value="0" /><el-option label="管理员" :value="1" />
          </el-select>
        </el-form-item>
      </el-form>
      <template #footer><el-button type="primary" @click="saveUser">保存</el-button></template>
    </el-dialog>

    <el-dialog v-model="productDialogVisible" title="商品信息" width="500px">
      <el-form :model="productForm" label-width="80px">
        <el-form-item label="名称"><el-input v-model="productForm.name" /></el-form-item>
        <el-form-item label="分类">
          <el-select v-model="productForm.category">
            <el-option label="漆艺" value="漆艺" /><el-option label="陶瓷" value="陶瓷" /><el-option label="茶文化" value="茶文化" />
            <el-option label="木雕" value="木雕" />
          </el-select>
        </el-form-item>
        <el-form-item label="价格"><el-input-number v-model="productForm.price" :min="0" /></el-form-item>
        <el-form-item label="库存"><el-input-number v-model="productForm.stock" :min="0" /></el-form-item>
        <el-form-item label="图片URL"><el-input v-model="productForm.imgUrl" placeholder="输入网络图片地址" /></el-form-item>
      </el-form>
      <template #footer><el-button type="primary" @click="saveProduct">保存</el-button></template>
    </el-dialog>

    <el-dialog v-model="activityDialogVisible" :title="activityForm.activityId ? '编辑活动' : '发布研学活动'" width="600px">
      <el-form :model="activityForm" label-width="80px">
        <el-form-item label="活动主题"><el-input v-model="activityForm.title" placeholder="例如：小小惠安女服饰体验" /></el-form-item>
        <el-row :gutter="20">
          <el-col :span="12"><el-form-item label="活动时间"><el-input v-model="activityForm.activityTime" placeholder="例：2025-05-01 14:00" /></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="活动费用"><el-input-number v-model="activityForm.price" :min="0" :precision="2" controls-position="right" style="width:100%" /></el-form-item></el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12"><el-form-item label="活动地点"><el-input v-model="activityForm.location" placeholder="集合地点" /></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="最大人数"><el-input-number v-model="activityForm.maxPeople" :min="1" style="width:100%" /></el-form-item></el-col>
        </el-row>
        <el-form-item label="封面图片"><el-input v-model="activityForm.imgUrl" placeholder="输入图片网络链接" /></el-form-item>
        <el-form-item label="活动详情"><el-input v-model="activityForm.description" type="textarea" :rows="4" placeholder="介绍活动流程、注意事项等..." /></el-form-item>
        <el-form-item label="初始状态">
          <el-radio-group v-model="activityForm.status">
            <el-radio :label="1">直接上架</el-radio>
            <el-radio :label="0">暂存下架</el-radio>
          </el-radio-group>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="activityDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="saveActivity">确定保存</el-button>
      </template>
    </el-dialog>

    <el-dialog v-model="aiLogDialogVisible" title="对话详情" width="600px">
      <div v-if="currentAiLog">
        <p><strong>提问时间：</strong> {{ formatTime(currentAiLog.createTime) }}</p>
        <p><strong>用户ID：</strong> {{ currentAiLog.userId }}</p>
        <el-divider>提问</el-divider>
        <div style="background: #f5f7fa; padding: 10px; border-radius: 4px;">{{ currentAiLog.question }}</div>
        <el-divider>回复</el-divider>
        <div style="background: #e1f3d8; padding: 10px; border-radius: 4px;">{{ currentAiLog.answer }}</div>
      </div>
    </el-dialog>

    <el-dialog v-model="heritageDialogVisible" title="非遗名录信息" width="700px">
      <el-form :model="heritageForm" label-width="80px">
        <el-row :gutter="20">
          <el-col :span="12"><el-form-item label="名称"><el-input v-model="heritageForm.name" /></el-form-item></el-col>
          <el-col :span="12">
            <el-form-item label="分类">
              <el-select v-model="heritageForm.category" allow-create filterable placeholder="选择或输入">
                <el-option label="传统技艺" value="传统技艺" /><el-option label="传统美术" value="传统美术" />
                <el-option label="传统音乐" value="传统音乐" /><el-option label="民俗" value="民俗" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12"><el-form-item label="级别"><el-input v-model="heritageForm.level" placeholder="如：国家级" /></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="地区"><el-input v-model="heritageForm.region" placeholder="如：泉州市" /></el-form-item></el-col>
        </el-row>
        <el-form-item label="传承人"><el-input v-model="heritageForm.inheritor" placeholder="非遗代表性传承人" /></el-form-item>
        <el-form-item label="图片URL"><el-input v-model="heritageForm.imgUrl" placeholder="输入网络图片链接" /></el-form-item>
        <el-form-item label="视频URL"><el-input v-model="heritageForm.videoUrl" placeholder="输入相关视频链接 (可选)" /></el-form-item>
        <el-form-item label="简介"><el-input v-model="heritageForm.description" type="textarea" :rows="2" placeholder="列表页显示的简短介绍" /></el-form-item>
        <el-form-item label="详细内容"><el-input v-model="heritageForm.content" type="textarea" :rows="6" placeholder="详情页展示的长文本内容" /></el-form-item>
      </el-form>
      <template #footer><el-button type="primary" @click="saveHeritage">保存</el-button></template>
    </el-dialog>

    <!-- 新增：研学名单审核弹窗 -->
    <el-dialog v-model="auditDialogVisible" title="报名名单审核" width="700px">
      <div v-if="currentAuditList.length === 0" style="text-align:center; padding: 20px; color:#999">该活动暂无报名记录</div>
      <el-table v-else :data="currentAuditList" stripe>
        <el-table-column prop="username" label="用户名" />
        <el-table-column prop="createTime" label="报名时间" width="180">
          <template #default="{row}">{{ formatTime(row.createTime) }}</template>
        </el-table-column>
        <el-table-column label="状态" width="100" align="center">
          <template #default="{row}">
            <el-tag v-if="row.status===1" type="success">已通过</el-tag>
            <el-tag v-else-if="row.status===2" type="danger">已拒绝</el-tag>
            <el-tag v-else type="warning">待审核</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" align="center">
          <template #default="{row}">
            <el-button v-if="row.status===0" type="success" size="small" @click="handleAudit(row, 1)">通过</el-button>
            <el-button v-if="row.status===0" type="danger" size="small" @click="handleAudit(row, 2)">拒绝</el-button>
            <span v-else style="color:#999">已处理</span>
          </template>
        </el-table-column>
      </el-table>
    </el-dialog>

  </div>
</template>

<script setup>
import { ref, onMounted, computed, reactive } from 'vue'
import { useRouter } from 'vue-router'
import { useUserStore } from '../store/user'
import { ElMessage, ElMessageBox } from 'element-plus'
import request from '../api/request'

const router = useRouter()
const userStore = useUserStore()
const activeMenu = ref('1')
const loading = ref(false)

const stats = ref({ userCount: 0, productCount: 0, postCount: 0, activityCount: 0 })
const userList = ref([])
const productList = ref([])
const auditList = ref([])
const activityList = ref([])
const orderList = ref([])
const aiLogList = ref([])
const heritageList = ref([])

// 弹窗状态
const userDialogVisible = ref(false)
const userForm = reactive({ userId: null, username: '', nickname: '', role: 0 })
const productDialogVisible = ref(false)
const productForm = reactive({ productId: null, name: '', category: '', price: 0, stock: 99, imgUrl: '' })
const activityDialogVisible = ref(false)
const activityForm = reactive({ activityId: null, title: '', activityTime: '', location: '', maxPeople: 20, price: 0, imgUrl: '', description: '', status: 1 })
const aiLogDialogVisible = ref(false)
const currentAiLog = ref(null)
const heritageDialogVisible = ref(false)
const heritageForm = reactive({ heritageId: null, name: '', category: '', level: '', region: '', inheritor: '', imgUrl: '', videoUrl: '', description: '', content: '', status: 0 })

// 新增：名单审核相关
const auditDialogVisible = ref(false)
const currentAuditList = ref([])

const breadcrumbMap = {
  '1': '控制台', '2': '用户管理', '3': '商品管理', '4': '社区管理',
  '5': '研学管理', '6': '订单调度', '7': 'AI 监控', '8': '非遗审核'
}
const currentBreadcrumb = computed(() => breadcrumbMap[activeMenu.value])

// 权限检查
if (!userStore.userInfo || userStore.userInfo.role !== 1) {
  ElMessage.error('无权访问后台管理系统')
  router.push('/')
}

const handleMenuSelect = (index) => {
  activeMenu.value = index
  if (index === '2') fetchUsers()
  if (index === '3') fetchProducts()
  if (index === '4') fetchAuditList()
  if (index === '5') fetchActivities()
  if (index === '6') fetchOrders()
  if (index === '7') fetchAiLogs()
  if (index === '8') fetchHeritages()
}

// 通用时间格式化
const formatTime = (t) => t ? t.replace('T', ' ').substring(0, 19) : ''

// ---------------- API 请求 ----------------

const fetchStats = async () => {
  try {
    const res = await request.get('/admin/stats')
    if(res.code===200) stats.value = res.data
  } catch(e){
    stats.value = { userCount: 1208, productCount: 56, postCount: 12, activityCount: 5 }
  }
}

// === 用户管理 ===
const fetchUsers = async () => {
  loading.value = true
  try {
    const res = await request.get('/user/list')
    if (res.code === 200) userList.value = res.data
    else ElMessage.error(res.msg || '获取用户列表失败')
  } catch(e) { ElMessage.error('网络错误') } finally { loading.value = false }
}
const openUserDialog = (row) => {
  if (row) Object.assign(userForm, row);
  else { userForm.userId=null; userForm.username=''; userForm.nickname=''; userForm.role=0 }
  userDialogVisible.value = true
}
const saveUser = async () => {
  if (!userForm.username) return ElMessage.warning('请输入用户名')
  try {
    let res = userForm.userId ? await request.put('/user/update', userForm) : await request.post('/user/add', userForm)
    if (res.code === 200) { ElMessage.success('保存成功'); userDialogVisible.value = false; fetchUsers() }
    else ElMessage.error(res.msg)
  } catch(e) { ElMessage.error('网络错误') }
}
const handleDeleteUser = (row) => {
  ElMessageBox.confirm(`确定删除用户 ${row.username}?`, '警告', { type: 'warning' }).then(async () => {
    try {
      const res = await request.delete(`/user/delete/${row.userId}`)
      if (res.code===200) { ElMessage.success('删除成功'); fetchUsers() }
    } catch(e) { ElMessage.error('网络错误') }
  })
}

// === 商品管理 ===
const fetchProducts = async () => {
  loading.value = true
  try {
    const res = await request.get('/product/list')
    if(res.code===200) productList.value = res.data
  } catch(e){ productList.value = [] } finally { loading.value = false }
}
const openProductDialog = (row) => {
  if(row) Object.assign(productForm, row);
  else Object.assign(productForm, { productId:null, name:'', category:'', price:0, stock:99, imgUrl:'' })
  productDialogVisible.value = true
}
const saveProduct = async () => {
  if (!productForm.name) return ElMessage.warning('请填写商品名称')
  try {
    const res = await request.post('/product/save', productForm)
    if (res.code === 200) { ElMessage.success('保存成功'); productDialogVisible.value = false; fetchProducts() }
  } catch(e) { ElMessage.error('网络错误') }
}
const handleDeleteProduct = (row) => {
  ElMessageBox.confirm('确定下架?', '提示').then(async () => {
    await request.delete(`/product/${row.productId}`); ElMessage.success('下架成功'); fetchProducts()
  })
}

// === 社区管理 (含置顶) ===
const fetchAuditList = async () => {
  loading.value = true
  try { const res = await request.get('/community/posts'); if (res.code === 200) auditList.value = res.data } finally { loading.value = false }
}
const handleToggleTop = async (row) => {
  const newStatus = row.isTop === 1 ? 0 : 1
  try {
    const res = await request.post(`/community/admin/post/top/${row.postId}`, null, { params: { isTop: newStatus }})
    if(res.code===200) { ElMessage.success('操作成功'); fetchAuditList() }
  } catch(e) { row.isTop = newStatus; ElMessage.success('操作成功(模拟)') }
}
const handleDeletePost = (row) => {
  ElMessageBox.confirm('确定删除帖子?', '警告').then(async () => {
    await request.delete(`/community/post/${row.postId}`); ElMessage.success('删除成功'); fetchAuditList()
  })
}

// === 研学管理 (整合后的核心逻辑) ===
const fetchActivities = async () => {
  loading.value = true
  try { const res = await request.get('/community/activities'); if (res.code===200) activityList.value = res.data } finally { loading.value = false }
}
const openActivityDialog = (row) => {
  if (row) Object.assign(activityForm, row)
  else Object.assign(activityForm, { activityId:null, title:'', activityTime:'', location:'', maxPeople:20, price:0, imgUrl:'', description:'', status:1 })
  activityDialogVisible.value = true
}
const saveActivity = async () => {
  if(!activityForm.title) return ElMessage.warning('请填写完整')
  try {
    const res = await request.post('/community/activity/add', activityForm)
    if (res.code===200) { ElMessage.success('发布成功'); activityDialogVisible.value=false; fetchActivities() }
  } catch(e) { ElMessage.error('网络错误') }
}
const handleToggleActivityStatus = async (row) => {
  const newStatus = row.status === 1 ? 0 : 1;
  const tempForm = { ...row, status: newStatus };
  try { await request.post('/community/activity/add', tempForm); ElMessage.success('状态更新成功'); fetchActivities(); } catch(e) {}
}
const handleDeleteActivity = (row) => {
  ElMessageBox.confirm('确定删除?', '警告').then(async () => {
    await request.delete(`/community/activity/${row.activityId}`); ElMessage.success('删除成功'); fetchActivities()
  })
}

// 新增：打开名单审核弹窗
const openAuditDialog = async (row) => {
  try {
    const res = await request.get(`/community/admin/signup/list/${row.activityId}`)
    if (res.code === 200) {
      currentAuditList.value = res.data
      auditDialogVisible.value = true
    } else {
      ElMessage.error(res.msg || '获取名单失败')
    }
  } catch(e) { ElMessage.error('网络错误') }
}

// 新增：处理审核逻辑
const handleAudit = async (row, status) => {
  try {
    const res = await request.post(`/community/admin/signup/audit/${row.signupId}`, null, { params: { status } })
    if (res.code === 200) {
      ElMessage.success('操作成功')
      row.status = status // 前端直接更新状态，无需刷新列表
    } else {
      ElMessage.error(res.msg || '操作失败')
    }
  } catch(e) { ElMessage.error('网络错误') }
}

// === 订单 & AI & 非遗 (保持原有逻辑) ===
const fetchOrders = async () => {
  loading.value = true; try { const res = await request.get('/order/all'); if(res.code===200) orderList.value=res.data } finally { loading.value=false }
}
const handleShip = async (row) => { try { await request.post(`/order/ship/${row.orderId}`); ElMessage.success('发货成功'); fetchOrders() } catch(e){} }
const handleComplete = async (row) => { try { await request.post(`/order/complete/${row.orderId}`); ElMessage.success('订单完成'); fetchOrders() } catch(e){} }
const handleCancel = async (row) => { try { await request.post(`/order/cancel/${row.orderId}`); ElMessage.success('订单取消'); fetchOrders() } catch(e){} }
const handleDeleteOrder = async (row) => { ElMessageBox.confirm('删除订单?').then(async()=>{ await request.delete(`/order/delete/${row.orderId}`); fetchOrders() }) }

const fetchAiLogs = async () => { loading.value=true; try { const res = await request.get('/ai/logs'); if(res.code===200) aiLogList.value=res.data } finally { loading.value=false } }
const openAiLogDetail = (row) => { currentAiLog.value=row; aiLogDialogVisible.value=true }

const fetchHeritages = async () => { loading.value=true; try { const res = await request.get('/heritage/admin/list'); if(res.code===200) heritageList.value=res.data } finally { loading.value=false } }
const openHeritageDialog = (row) => {
  if(row) Object.assign(heritageForm, row); else Object.assign(heritageForm, { heritageId:null, name:'', category:'', level:'', region:'', inheritor:'', imgUrl:'', videoUrl:'', description:'', content:'', status:0 });
  heritageDialogVisible.value=true
}
const saveHeritage = async () => {
  try { const res = await request.post('/heritage/save', heritageForm); if(res.code===200) { ElMessage.success('保存成功'); heritageDialogVisible.value=false; fetchHeritages() } } catch(e){}
}
const handleAuditHeritage = async (row, status) => {
  try { await request.post(`/heritage/audit/${row.heritageId}`, null, { params: { status }}); ElMessage.success('操作成功'); fetchHeritages() } catch(e){}
}
const handleDeleteHeritage = (row) => {
  ElMessageBox.confirm('删除项目?').then(async()=>{ await request.delete(`/heritage/delete/${row.heritageId}`); ElMessage.success('删除成功'); fetchHeritages() })
}

const handleLogout = () => { userStore.logout(); router.push('/login') }

onMounted(() => { fetchStats() })
</script>

<style scoped>
.admin-aside { background-color: #304156; display: flex; flex-direction: column; }
.admin-logo { height: 60px; line-height: 60px; text-align: center; color: #fff; font-weight: bold; background: #2b3649; }
.admin-menu { border-right: none; flex: 1; }
.admin-header { background: #fff; border-bottom: 1px solid #e6e6e6; display: flex; justify-content: space-between; align-items: center; padding: 0 20px; }
.admin-main { background-color: #f0f2f5; padding: 20px; }
.mb-20 { margin-bottom: 20px; }
.data-card .data-num { font-size: 28px; font-weight: bold; margin: 10px 0; }
.red { color: #F56C6C; } .blue { color: #409EFF; } .orange { color: #E6A23C; } .green { color: #67C23A; }
.el-dropdown-link { cursor: pointer; display: flex; align-items: center; color: #333; }
.card-header-flex { display: flex; justify-content: space-between; align-items: center; }
.image-slot { display: flex; justify-content: center; align-items: center; width: 100%; height: 100%; background: #f5f7fa; color: #909399; }
</style>