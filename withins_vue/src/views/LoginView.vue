
<template>
  <RouterView></RouterView>
</template>

<script setup lang="ts">
import { watch } from "vue";
import { useRoute } from "vue-router";
const route = useRoute();
watch(() => route.path, (newPath, oldPath) => {
  console.log(`라우트 변화: ${oldPath} → ${newPath}`);
});
</script>

<style scoped>
</style>