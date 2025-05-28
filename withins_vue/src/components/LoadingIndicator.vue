
<template>
  <!-- 기본 스피너 -->
  <div v-if="show" class="loading-overlay" :class="{ 'full-screen': fullScreen }">
    <div class="loading-content">
      <div v-if="type === 'spinner'" class="spinner"></div>
      <div v-else-if="type === 'dots'" class="dots-loader">
        <div class="dot"></div>
        <div class="dot"></div>
        <div class="dot"></div>
      </div>
      <div v-else-if="type === 'pulse'" class="pulse-loader"></div>
      <div v-else-if="type === 'bars'" class="bars-loader">
        <div class="bar"></div>
        <div class="bar"></div>
        <div class="bar"></div>
        <div class="bar"></div>
        <div class="bar"></div>
      </div>
    </div>
  </div>
</template>
<script lang="ts">
export default {
  name: 'LoadingIndicator',
  props: {
    show: {
      type: Boolean,
      default: false
    },
    type: {
      type: String,
      default: 'spinner',
      validator: (value: string) => ['spinner', 'dots', 'pulse', 'bars'].includes(value)
    },
    fullScreen: {
      type: Boolean,
      default: false
    }
  }
}
</script>

<style scoped>
.loading-overlay {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background-color: rgba(255, 255, 255, 0.9);
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 1000;
}

.loading-overlay.full-screen {
  position: fixed;
  top: 0;
  left: 0;
  width: 100vw;
  height: 100vh;
  background-color: rgba(0, 0, 0, 0.5);
}

.loading-content {
  text-align: center;
}


/* 스피너 로더 */
.spinner {
  width: 40px;
  height: 40px;
  border: 4px solid #f3f3f3;
  border-top: 4px solid #007bff;
  border-radius: 50%;
  animation: spin 1s linear infinite;
}

@keyframes spin {
  0% { transform: rotate(0deg); }
  100% { transform: rotate(360deg); }
}

/* 점 로더 */
.dots-loader {
  display: flex;
  gap: 4px;
}

.dot {
  width: 12px;
  height: 12px;
  background-color: #007bff;
  border-radius: 50%;
  animation: dot-bounce 1.4s ease-in-out infinite both;
}

.dot:nth-child(1) { animation-delay: -0.32s; }
.dot:nth-child(2) { animation-delay: -0.16s; }

@keyframes dot-bounce {
  0%, 80%, 100% {
    transform: scale(0);
  }
  40% {
    transform: scale(1);
  }
}

/* 펄스 로더 */
.pulse-loader {
  width: 40px;
  height: 40px;
  background-color: #007bff;
  border-radius: 50%;
  animation: pulse-scale 1s ease-in-out infinite;
}

@keyframes pulse-scale {
  0% {
    transform: scale(0);
    opacity: 1;
  }
  100% {
    transform: scale(1);
    opacity: 0;
  }
}

/* 바 로더 */
.bars-loader {
  display: flex;
  gap: 3px;
  align-items: end;
}

.bar {
  width: 6px;
  height: 40px;
  background-color: #007bff;
  animation: bar-stretch 1.2s ease-in-out infinite;
}

.bar:nth-child(1) { animation-delay: -1.1s; }
.bar:nth-child(2) { animation-delay: -1.0s; }
.bar:nth-child(3) { animation-delay: -0.9s; }
.bar:nth-child(4) { animation-delay: -0.8s; }
.bar:nth-child(5) { animation-delay: -0.7s; }

@keyframes bar-stretch {
  0%, 40%, 100% {
    transform: scaleY(0.4);
  }
  20% {
    transform: scaleY(1);
  }
}

/* 다크 테마 지원 */
.loading-overlay.dark {
  background-color: rgba(0, 0, 0, 0.7);
}

.loading-overlay.dark {
  color: #fff;
}

.loading-overlay.dark .spinner {
  border-color: #333;
  border-top-color: #fff;
}

.loading-overlay.dark .dot,
.loading-overlay.dark .pulse-loader,
.loading-overlay.dark .bar {
  background-color: #fff;
}
</style>