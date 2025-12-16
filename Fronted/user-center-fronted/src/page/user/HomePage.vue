
<template>
  <div id="homePage">
    <div id="search-bar">
        <a-input-search
            v-model:value="value"
            placeholder="input search text"
            enter-button
            @search="onSearch"
        />
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue';
const value = ref<string>('');
</script>

<style scoped></style>
