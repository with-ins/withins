import "@/assets/fonts/fonts.css"
import "@/assets/css/common.css";

import { createApp } from "vue";
import App from "./App.vue";
import router from "./router";
import { createPinia } from "pinia";

createApp(App)
  .use(router)
  .use(createPinia())
  .mount("#app");
