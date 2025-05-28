<template>
  <TheHeader></TheHeader>
  <TheMain></TheMain>
  <TheFooter></TheFooter>
</template>

<style>
#app {
  min-height: 100vh;
}
header, main {
  margin: 0 auto;
  width: calc(100vw - 200px);
  max-width: 1200px;
}

@media (max-width: 768px) {
  header, main {
    width: initial;
    margin: 0 5vw;
  }
}
</style>

<script setup lang="ts">
import TheHeader from "@/layouts/TheHeader.vue";
import TheMain from "@/layouts/TheMain.vue";
import TheFooter from "@/layouts/TheFooter.vue";
</script>
