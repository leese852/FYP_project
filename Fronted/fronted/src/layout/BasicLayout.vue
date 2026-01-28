<script setup lang="ts">
import GlobalHeader from "@/components/GlobalHeader.vue";
import {computed} from "vue";
// import { getCurrentUser } from "@/api/user";
// getCurrentUser().then((res) => {
//   console.log(res);
// });
// 计算属性：根据当前路由的meta信息决定是否显示Header

</script>

<template>
  <div id="basicLayout">
    <a-layout>
<!--      用router/index里的name-->
      <a-layout-header v-if="!['dishDetail'].includes($route.name as string)" class="header">
        <GlobalHeader />
      </a-layout-header>
      <a-layout-content class="content">
        <router-view />
      </a-layout-content>
      <a-layout-footer class="footer">
        <a href="https://youtube.com">System by leese</a>
      </a-layout-footer>
    </a-layout>
  </div>
</template>

<style scoped>
#basicLayout .footer {
  text-align: center;
  background: #efefef;
  position: fixed;
  bottom: 0;
  width: 100%;
  padding: 10px;
}

#basicLayout .content {
  margin-bottom: 20px;
}

#basicLayout .header {
  background: white;
  text-align: center;
}
</style>
